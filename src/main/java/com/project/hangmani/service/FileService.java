package com.project.hangmani.service;

import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.config.WebClientConfig;
import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.domain.Store;
import com.project.hangmani.domain.StoreAttachment;
import com.project.hangmani.dto.FileDTO.RequestStoreFileDTO;
import com.project.hangmani.dto.FileDTO.ResponseStoreFileDTO;
import com.project.hangmani.exception.FailInsertData;
import com.project.hangmani.exception.NotFoundAttachment;
import com.project.hangmani.exception.NotFoundStore;
import com.project.hangmani.repository.FileRepository;
import com.project.hangmani.repository.StoreRepository;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import io.netty.handler.codec.http.HttpScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.project.hangmani.config.OAuthConst.*;

@Service
@Slf4j
public class FileService {
    private final FileRepository fileRepository;
    private final StoreRepository storeRepository;
    private final WebClientConfig webClient;
    private final PropertiesValues propertiesValues;
    private Util util;
    private RequestConvert requestConvert;
    private int height = 300;
    private int width = 288;

    public FileService(FileRepository fileRepository,
                       StoreRepository storeRepository,
                       WebClientConfig webClient,
                       PropertiesValues propertiesValues) {
        this.fileRepository = fileRepository;
        this.storeRepository = storeRepository;
        this.webClient = webClient;
        this.requestConvert = new RequestConvert(propertiesValues);
        this.util = new Util(propertiesValues);
        this.propertiesValues = propertiesValues;
    }
    @Transactional
    public void insertAttachment(RequestStoreFileDTO requestDTO) {
        if (requestDTO.getFilesData().size() == 0)
            return;
        for(MultipartFile file: requestDTO.getFilesData()){
            //generate upload time
            Date uploadTime = this.util.getSqlDate();

            //generate saved file name
            String savedFileName = this.util.getRenameWithDate(uploadTime, this.util.getRandomValue(),
                    file.getOriginalFilename());
            StoreAttachment storeAttachment = requestConvert.convertDTO
                    (file, savedFileName, requestDTO.getStoreUuid(), uploadTime);

            int i = fileRepository.insertAttachment(storeAttachment);
            if (i == 0)
                throw new FailInsertData();
            //save file

            try {
                this.util.savedAttachmentFile(file.getInputStream(), savedFileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public List<StoreAttachment> getAttachmentsByUuid(String storeUuid) {
        return fileRepository.getAttachmentsByUuid(storeUuid);
    }

    @Transactional
    public ResponseStoreFileDTO getMapAttachmentUrls(RequestStoreFileDTO dto) {
//        List<StoreAttachment> urls = getAttachmentsByUuid(dto.getStoreUUID());
        List<String> savedFileNames = dto.getSavedFileNames();
        List<String> ret = new ArrayList<>();
        boolean flag = false;
        if (savedFileNames != null && savedFileNames.size() > 0){
            for (String st :
                    savedFileNames) {
                if (st.indexOf(dto.getStoreUuid()) > -1){
                   flag = true;
                   ret.add(st);
                }
            }
        }
        if(!flag) {
            Double latitude = dto.getStoreLatitude();
            Double longitude = dto.getStoreLongitude();

            if (latitude == 0 || longitude == 0) {
                List<Store> stores = storeRepository.getStoreInfoByUuid(dto.getStoreUuid());
                Store storeInfo = stores.get(0);
                if (storeInfo.getStoreUuid() == null) {
                    throw new NotFoundStore();
                }
                latitude = storeInfo.getStoreLatitude();
                longitude = storeInfo.getStoreLongitude();
            }
            StoreAttachment save = getImageAndSave(latitude,  longitude, dto.getStoreUuid());
            if (null == save)
                return null;
            fileRepository.insertAttachment(save);
            ret.add(save.getSavedFileName());
        }
        return ResponseStoreFileDTO.builder()
                .domainUrls(ret)
                .storeUuid(dto.getStoreUuid())
                .build();
    }
    private StoreAttachment getImageAndSave(Double latitude, Double longitude, String storeUuid) {
        String param_markers = "type:d|size:mid|pos:"+longitude+" "+latitude;
        WebClient wc = webClient.webClient();

         byte[] responseBody = wc.get()
                .uri(builder -> builder.scheme(String.valueOf(HttpScheme.HTTPS))
                        .host(MARKER_API_URL)
                        .path(MARKER_API_URI)
                        .queryParam("h", height)
                        .queryParam("w", width)
                        .queryParam("markers", param_markers)
                        .build())
                .header("X-NCP-APIGW-API-KEY-ID", "a0k2s3ayc4")
                .header("X-NCP-APIGW-API-KEY", "w7tV9beHe9gDtOo042YuZCZuHbaJSisHgugMAjvj")
                .exchange()
                .block()
                .bodyToMono(byte[].class)
                .block();
        Date sqlDate = util.getSqlDate();
        String savedFileName = util.getRenameWithDate(sqlDate, this.util.getRandomValue(), ".jpg");
        savedFileName = storeUuid + "_" + savedFileName;
        CompletableFuture<Void> ret = this.util.savedAttachmentFile(
                new ByteArrayInputStream(responseBody),
                savedFileName);
        if (ret != null)
            ret.join();
        else {
            log.info("ERROR");
            return null;
        }
        return StoreAttachment.builder()
                .originalFileName(storeUuid + ".jpg")
                .uploadDate(sqlDate)
                .storeUuid(storeUuid)
                .fileSize(responseBody.length)
                .savedFileName(savedFileName)
                .build();
    }
    public Resource getImg(String fileName){
        Path filePath = Paths.get(propertiesValues.getUploadDir(), fileName);
        try {
            UrlResource resource = new UrlResource(filePath.toUri());
            if (resource.contentLength() > 0)
                return resource;
            else
                throw new NotFoundAttachment();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e){
            throw new NotFoundAttachment();
        }
    }
}
