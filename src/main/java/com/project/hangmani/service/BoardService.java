package com.project.hangmani.service;

import com.project.hangmani.domain.Board;
import com.project.hangmani.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
    @Transactional
    public Board createBoard(Board board) {
        return boardRepository.insertBoard(board);
    }
}