package com.project.hangmani.controller;

import com.project.hangmani.dto.FileDTO;
import com.project.hangmani.dto.FileDTO.ResponseStoreFileDTO;
import com.project.hangmani.dto.ResponseDTO;
import com.project.hangmani.dto.StoreDTO;
import com.project.hangmani.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Slf4j
@RequestMapping("/api/v1/file")
public class FileController {
    @Value("${file.upload-dir}")
    private String uploadDir;
    private FileService fileService;
    public FileController(FileService fileService){
        log.info("FileContoller init");
        this.fileService = fileService;
    }
    @GetMapping("/img/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable("fileName") String fileName) throws IOException {
        log.info("start");
        Path filePath = Paths.get(uploadDir, fileName);
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(resource.contentLength())
                .body(resource);
    }
    @GetMapping("/{storeUuid}")
    public ResponseDTO<ResponseStoreFileDTO> getStoreAttachmentUrl(@PathVariable("storeUuid") String storeUuid) {
        return ResponseDTO.<ResponseStoreFileDTO>builder()
                .data(fileService.getMapAttachmentUrls(storeUuid))
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .build();


    }
}
