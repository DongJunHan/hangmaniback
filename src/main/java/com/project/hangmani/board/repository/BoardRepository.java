package com.project.hangmani.board.repository;

import com.project.hangmani.board.model.dto.BoardDTO;
import com.project.hangmani.board.model.dto.RequestRangeDTO;
import com.project.hangmani.board.model.dto.RequestWriterDTO;
import com.project.hangmani.board.model.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Optional<BoardDTO> getByNo(int boardNo, int offset, int limit);
    int add(Board board);
    List<BoardDTO> getList(RequestRangeDTO boardDTO);
    List<BoardDTO> getByWriter(RequestWriterDTO boardDTO);
    int delete(int boardNo);
}
