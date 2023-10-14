package com.project.hangmani.board.model.dto;

import com.project.hangmani.file.model.dto.AttachmentDTO;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDTO {
    private Integer boardNo;
    private String boardTitle;
    private String boardContent;
    private String boardWriter;
    private Date createAt;
    private Date updateAt;
    private Boolean isDelete;
    private List<AttachmentDTO> files;
    @Builder
    private BoardDTO(Integer boardNo, String boardTitle, String boardContent,
                     String boardWriter, Date createAt, Date updateAt, Boolean isDelete,
                     List<AttachmentDTO> files) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardWriter = boardWriter;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.isDelete = isDelete;
        this.files = files;
    }
}
