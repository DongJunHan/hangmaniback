package com.project.hangmani.service;

import com.project.hangmani.dto.FileDTO.RequestStoreFileDTO;
import com.project.hangmani.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void insertAttachment(RequestStoreFileDTO requestDTO) {
        if (requestDTO.getFileSize() == 0 && requestDTO.getOriginalFileName() == null)
            return;
        fileRepository.insertAttachment(requestDTO);

    }

}
