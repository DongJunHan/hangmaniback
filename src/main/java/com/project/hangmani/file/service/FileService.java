package com.project.hangmani.file.service;

import com.project.hangmani.board.model.dto.RequestInsertDTO;
import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.config.WebClientConfig;
import com.project.hangmani.exception.FailInsertData;
import com.project.hangmani.exception.NotFoundAttachment;
import com.project.hangmani.exception.NotFoundStore;
import com.project.hangmani.file.model.dto.AttachmentDTO;
import com.project.hangmani.file.model.dto.RequestSaveDTO;
import com.project.hangmani.file.repository.FileRepository;
import com.project.hangmani.store.model.dto.RequestAttachmentDTO;
import com.project.hangmani.store.model.dto.ResponseAttachmentDTO;
import com.project.hangmani.store.model.dto.StoreDTO;
import com.project.hangmani.store.repository.StoreRepository;
import com.project.hangmani.util.Util;
import io.netty.handler.codec.http.HttpScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.project.hangmani.config.OAuthConst.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final WebClientConfig webClient;
    private final PropertiesValues propertiesValues;
    private Util util = new Util(propertiesValues);
    private int height = 300;
    private int width = 288;

    public List<AttachmentDTO> saveAttachment(RequestSaveDTO saveDTO) throws IOException {
        if (saveDTO.getAttachFiles() == null) {
            return null;
        }
        List<AttachmentDTO> files = new ArrayList<>();
        //save extension
        for (MultipartFile attachFile : saveDTO.getAttachFiles()) {
            if (!attachFile.isEmpty()) {
                files.add(save(attachFile));
            }
        }
        return files;
    }

    public List<com.project.hangmani.store.model.dto.AttachmentDTO> saveAttachment(String storeUuid, RequestSaveDTO saveDTO) throws IOException {
        if (saveDTO.getAttachFiles() == null) {
            return null;
        }
        List<com.project.hangmani.store.model.dto.AttachmentDTO> files = new ArrayList<>();
        //save extension
        for (MultipartFile attachFile : saveDTO.getAttachFiles()) {
            if (!attachFile.isEmpty()) {
                AttachmentDTO save = save(attachFile);
                files.add(
                        com.project.hangmani.store.model.dto.AttachmentDTO
                                .builder()
                                .storeUuid(storeUuid)
                                .savedFileName(save.getSavedFileName())
                                .originalFileName(save.getOriginalFileName())
                                .build()
                );
            }
        }
        return files;
    }


    private AttachmentDTO save(MultipartFile attachFile) throws IOException {
        //get extension
        String savedFileName = createStoreFileName(attachFile.getOriginalFilename());
        //save
        attachFile.transferTo(new File(getFullPath(savedFileName)));
        return new AttachmentDTO(attachFile.getOriginalFilename(), savedFileName);
    }

    public String getFullPath(String filename) {
        return propertiesValues.getUploadDir() + filename;
    }

//    @Transactional
//    public ResponseAttachmentDTO getMapAttachmentUrls(RequestAttachmentDTO dto) {
////        List<StoreAttachment> urls = getAttachmentsByUuid(dto.getStoreUUID());
//        List<String> savedFileNames = dto.getSavedFileNames();
//        List<String> ret = new ArrayList<>();
//        boolean flag = false;
//        if (savedFileNames != null && savedFileNames.size() > 0){
//            for (String st :
//                    savedFileNames) {
//                if (st.indexOf(dto.getStoreUuid()) > -1){
//                   flag = true;
//                   ret.add(st);
//                }
//            }
//        }
//        if(!flag) {
//            Double latitude = dto.getStoreLatitude();
//            Double longitude = dto.getStoreLongitude();
//
//            if (latitude == 0 || longitude == 0) {
//                List<StoreDTO> stores = storeRepository.getByUuid(dto.getStoreUuid());
//                StoreDTO storeInfo = stores.get(0);
//                if (storeInfo.getStoreUuid() == null) {
//                    throw new NotFoundStore();
//                }
//                latitude = storeInfo.getStoreLatitude();
//                longitude = storeInfo.getStoreLongitude();
//            }
//            StoreAttachment save = getImageAndSave(latitude,  longitude, dto.getStoreUuid());
//            if (null == save)
//                return null;
//            //TODO insert attachment
////            fileRepository.insertAttachment(save);
//            ret.add(save.getSavedFileName());
//        }
//        return ResponseAttachmentDTO.builder()
//                .domainUrls(ret)
//                .storeUuid(dto.getStoreUuid())
//                .build();
//    }
//    private StoreAttachment getImageAndSave(Double latitude, Double longitude, String storeUuid) {
//        String param_markers = "type:d|size:mid|pos:"+longitude+" "+latitude;
//        WebClient wc = webClient.webClient();
//
//         byte[] responseBody = wc.get()
//                .uri(builder -> builder.scheme(String.valueOf(HttpScheme.HTTPS))
//                        .host(MARKER_API_URL)
//                        .path(MARKER_API_URI)
//                        .queryParam("h", height)
//                        .queryParam("w", width)
//                        .queryParam("markers", param_markers)
//                        .build())
//                .header("X-NCP-APIGW-API-KEY-ID", "a0k2s3ayc4")
//                .header("X-NCP-APIGW-API-KEY", "w7tV9beHe9gDtOo042YuZCZuHbaJSisHgugMAjvj")
//                .exchange()
//                .block()
//                .bodyToMono(byte[].class)
//                .block();
//        Date sqlDate = util.getSqlDate();
//        String savedFileName = util.getRenameWithDate(sqlDate, this.util.getRandomValue(), ".jpg");
//        savedFileName = storeUuid + "_" + savedFileName;
//        CompletableFuture<Void> ret = this.util.savedAttachmentFile(
//                new ByteArrayInputStream(responseBody),
//                savedFileName);
//        if (ret != null)
//            ret.join();
//        else {
//            log.info("ERROR");
//            return null;
//        }
//        return StoreAttachment.builder()
//                .originalFileName(storeUuid + ".jpg")
//                .uploadDate(sqlDate)
//                .storeUuid(storeUuid)
//                .fileSize(responseBody.length)
//                .savedFileName(savedFileName)
//                .build();
//    }
//    public Resource getImg(String fileName){
//        Path filePath = Paths.get(propertiesValues.getUploadDir(), fileName);
//        try {
//            UrlResource resource = new UrlResource(filePath.toUri());
//            if (resource.contentLength() > 0)
//                return resource;
//            else
//                throw new NotFoundAttachment();
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e){
//            throw new NotFoundAttachment();
//        }
//    }
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String filename) {
        int pos = filename.lastIndexOf(".");
        return filename.substring(pos + 1);
    }
}
