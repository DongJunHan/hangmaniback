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
    public Board convertEntity(BoardDTO.RequestBoardDTO boardDTO) {
        String boardWriter = boardDTO.getBoardWriter();
        String content = boardDTO.getBoardContent();
        String title = boardDTO.getBoardTitle();

        Board board = new Board();
        board.setBoardWriter(boardWriter);
        board.setBoardContent(content);
        board.setBoardTitle(title);
        return board;
    }
}
