package com.project.hangmani.service;

import com.project.hangmani.domain.StoreAttachment;
import com.project.hangmani.dto.FileDTO.RequestStoreFileDTO;
import com.project.hangmani.repository.FileRepository;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
@Slf4j
public class FileService {
    private final FileRepository fileRepository;
    private final ConvertData convertData;
    private final Util util;

    public FileService(FileRepository fileRepository, Util util) {
        this.fileRepository = fileRepository;
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

    public List<StoreAttachment> getAttachmentsByUuid(String uuid) {
        return fileRepository.getAttachmentsByUuid(uuid);
    }

}
