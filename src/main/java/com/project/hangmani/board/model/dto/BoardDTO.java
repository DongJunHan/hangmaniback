package com.project.hangmani.board.model.dto;

import com.project.hangmani.file.model.dto.AttachmentDTO;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {
    private Integer boardNo;
    private String boardTitle;
    private String boardContent;
    private String boardWriter;
    private Date createAt;
    private Date updateAt;
    private Boolean isDelete;
    private List<AttachmentDTO> files;
}
