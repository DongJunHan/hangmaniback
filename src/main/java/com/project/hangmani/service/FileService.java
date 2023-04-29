package com.project.hangmani.service;

import com.project.hangmani.config.WebClientConfig;
import com.project.hangmani.domain.Store;
import com.project.hangmani.domain.StoreAttachment;
import com.project.hangmani.dto.FileDTO.RequestStoreFileDTO;
import com.project.hangmani.repository.FileRepository;
import com.project.hangmani.repository.StoreRepository;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Date;
import java.util.List;

@Service
@Slf4j
public class FileService {
    private final FileRepository fileRepository;
    private final StoreRepository storeRepository;
    private final ConvertData convertData;
    private final WebClientConfig webClient;
    private final Util util;
    @Value("${file.upload-dir}")
    private String uploadDir;

    public FileService(FileRepository fileRepository,
                       StoreRepository storeRepository,
                       WebClientConfig webClient,
                       Util util) {
        this.fileRepository = fileRepository;
        this.storeRepository = storeRepository;
        this.webClient = webClient;
        this.convertData = new ConvertData();
        this.util = util;
    }
    @Transactional
    public void insertAttachment(RequestStoreFileDTO requestDTO) {
        if (requestDTO.getFileSize() == 0 && requestDTO.getOriginalFileName().equals(""))
            return;

        //generate upload time
        Date uploadTime = this.convertData.getSqlDate();
        requestDTO.setUploadTime(uploadTime);
        //generate saved file name
        requestDTO.setSavedFileName(this.util.getRenameWithDate(uploadTime, this.util.getRandomValue(),
                requestDTO.getOriginalFileName()));

        fileRepository.insertAttachment(requestDTO);

        //save file
        this.util.savedMultipartFile(requestDTO.getFileData(), requestDTO.getSavedFileName());

    }

    public List<StoreAttachment> getAttachmentsByUuid(String storeUuid) {
        return fileRepository.getAttachmentsByUuid(storeUuid);
    }
    public List<String> getAttachmentUrls(String storeUuid) {
        List<StoreAttachment> urls = getAttachmentsByUuid(storeUuid);
        if (urls.size() == 0){
            Store storeInfo = storeRepository.getStoreInfoByUuid(storeUuid);
            getImageAndSave(storeInfo.getStoreLatitude(), storeInfo.getStoreLongitude());
        }
        return null;
    }
    private void getImageAndSave(Double latitude, Double Longitude) {
        webClient.webClient();
    }
}
