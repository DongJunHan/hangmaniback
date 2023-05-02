package com.project.hangmani.service;

import com.ibm.jvm.trace.format.api.ByteStream;
import com.project.hangmani.config.WebClientConfig;
import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.domain.Store;
import com.project.hangmani.domain.StoreAttachment;
import com.project.hangmani.dto.FileDTO.RequestStoreFileDTO;
import com.project.hangmani.exception.FailInsertData;
import com.project.hangmani.exception.IO;
import com.project.hangmani.exception.NotFoundUser;
import com.project.hangmani.repository.FileRepository;
import com.project.hangmani.repository.StoreRepository;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import io.netty.handler.codec.http.HttpScheme;
import jakarta.websocket.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.project.hangmani.config.OAuthConst.*;

@Service
@Slf4j
public class FileService {
    private final FileRepository fileRepository;
    private final StoreRepository storeRepository;
    private final ConvertData convertData;
    private final WebClientConfig webClient;
    private final RequestConvert requestConvert;
    private final Util util;
    @Value("${file.upload-dir}")
    private String uploadDir;
    private int height = 300;
    private int width = 288;

    public FileService(FileRepository fileRepository,
                       StoreRepository storeRepository,
                       WebClientConfig webClient,
                       Util util) {
        this.fileRepository = fileRepository;
        this.storeRepository = storeRepository;
        this.webClient = webClient;
        this.convertData = new ConvertData();
        this.requestConvert = new RequestConvert();
        this.util = util;
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
                    (file, savedFileName, requestDTO.getStoreUUID(), uploadTime);

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
    public List<String> getAttachmentUrls(String storeUuid) {
        List<StoreAttachment> urls = getAttachmentsByUuid(storeUuid);
        boolean flag = false;
        if (urls.size() > 0){
            for (StoreAttachment st :
                    urls) {
                if (st.getSavedFileName().indexOf(storeUuid) > -1){
                   flag = true;
                }
            }
        }
        if(!flag) {
            Store storeInfo = storeRepository.getStoreInfoByUuid(storeUuid);
            if (storeInfo == null) {
                throw new NotFoundUser();
            }
            getImageAndSave(storeInfo.getStoreLatitude(), storeInfo.getStoreLongitude(), storeUuid);
        }
        return null;
    }
    private String getImageAndSave(Double latitude, Double longitude, String storeUuid) {
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
        String savedFileName = util.getRenameWithDate(util.getSqlDate(), this.util.getRandomValue(), storeUuid+".jpg");
        CompletableFuture<Void> ret = this.util.savedAttachmentFile(
                new ByteArrayInputStream(responseBody),
                savedFileName);
        ret.join();
        return savedFileName;
//        Path path = Paths.get("/home/IMG_0339.jpg");
//        byte[] bytes = null;
//        try {
//            bytes = Files.readAllBytes(path);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        InputStream is = new ByteArrayInputStream(bytes);
//        this.util.savedAttachmentFile(is, path.getFileName().toString());
    }
}
