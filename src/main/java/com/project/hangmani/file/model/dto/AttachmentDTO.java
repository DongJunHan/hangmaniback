package com.project.hangmani.file.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachmentDTO {
    private String originalFileName;
    private String savedFileName;
}
