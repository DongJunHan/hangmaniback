package com.project.hangmani.board.model.dto;

import com.project.hangmani.board.model.entity.Board;
import com.project.hangmani.file.model.dto.AttachmentDTO;
import com.project.hangmani.file.model.dto.RequestSaveDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestInsertDTO {
    @NotBlank(message = "boardTitle insert")
    private String boardTitle;
    @NotNull(message = "boardContent insert")
    private String boardContent;
    @NotNull(message = "boardWriter insert")
    private String boardWriter;
    private List<MultipartFile> attachFiles;

    public Board convertToEntity() {
        return Board.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardWriter(boardWriter)
                .build();
    }

    public RequestSaveDTO convertToDTO() {
        return RequestSaveDTO.builder()
                .attachFiles(attachFiles)
                .build();
    }
}