package com.project.hangmani.controller;

import com.project.hangmani.dto.ResponseDTO;
import com.project.hangmani.dto.StoreDTO;
import com.project.hangmani.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
@RequestMapping("/file")
public class FileController {
    @Value("${file.upload-dir}")
    private String uploadDir;
    private FileService fileService;
    public FileController(FileService fileService){
        this.fileService = fileService;
    }
    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable("fileName") String fileName) throws IOException {
        log.info("start");
        Path filePath = Paths.get(uploadDir, fileName);
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(resource.contentLength())
                .body(resource);
    }
    //TODO naver api에 static map받아오기. 저장된 지도이미지 없으면 api호출 아니면 디비에서 가져오기.
    @GetMapping("/{storeUuid}")
    public ResponseDTO<Object> getStoreAttachmentUrl(@PathVariable("storeUuid") String storeUuid) {
        fileService.getAttachmentUrls(storeUuid);
        return null;


    }
}
