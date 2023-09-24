package com.project.hangmani.file.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestSaveDTO {
    private List<MultipartFile> attachFiles;
}
