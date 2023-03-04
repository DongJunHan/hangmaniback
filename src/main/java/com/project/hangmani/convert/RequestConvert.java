package com.project.hangmani.convert;

import com.project.hangmani.domain.Board;
import com.project.hangmani.dto.BoardDTO;

public class RequestConvert {
    /**
     * boardInsert
     * convert DTO to Entity
     * @param boardDTO
     * @return
     */
    public Board boardInsertDTOToBoard(BoardDTO.RequestBoardDTO boardDTO) {
        String boardWriter = boardDTO.getBoardWriter();
        String content = boardDTO.getContent();
        String title = boardDTO.getTitle();
        Board board = new Board();
        board.setBoardWriter(boardWriter);
        board.setContent(content);
        board.setTitle(title);
        return board;
    }
}
