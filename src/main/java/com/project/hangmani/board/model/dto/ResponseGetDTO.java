package com.project.hangmani.board.model.dto;

import com.project.hangmani.board.model.entity.Board;
import com.project.hangmani.file.model.dto.AttachmentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseGetDTO {
    private Integer boardNo;
    private String boardTitle;
    private String boardContent;
    private String boardWriter;
    private Date createAt;
    private Date updateAt;
    private List<AttachmentDTO> files;

    public ResponseGetDTO convertToDTO(Board board) {
        return ResponseGetDTO.builder()
                .boardNo(board.getBoardNo())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .boardWriter(board.getBoardWriter())
                .createAt(board.getCreateAt())
                .updateAt(board.getUpdateAt())
                .build();
    }

    public ResponseGetDTO convertToDTO(BoardDTO dto) {
        return ResponseGetDTO.builder()
                .boardNo(dto.getBoardNo())
                .boardTitle(dto.getBoardTitle())
                .boardContent(dto.getBoardContent())
                .boardWriter(dto.getBoardWriter())
                .createAt(dto.getCreateAt())
                .updateAt(dto.getUpdateAt())
                .files(dto.getFiles())
                .build();
    }

    public List<ResponseGetDTO> convertToList(List<BoardDTO> list) {
        List<ResponseGetDTO> boards = new ArrayList<>();
        for (BoardDTO b:
                list) {
            boards.add(ResponseGetDTO.builder()
                    .boardContent(b.getBoardContent())
                    .boardTitle(b.getBoardTitle())
                    .boardNo(b.getBoardNo())
                    .updateAt(b.getUpdateAt())
                    .createAt(b.getCreateAt())
                    .boardWriter(b.getBoardWriter())
                    .build());
        }
        return boards;
    }
}
