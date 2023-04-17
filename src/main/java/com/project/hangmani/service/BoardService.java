package com.project.hangmani.service;

import com.project.hangmani.convert.ResponseConvert;
import com.project.hangmani.domain.Board;
import com.project.hangmani.dto.BoardDTO.RequestBoardDTO;
import com.project.hangmani.dto.BoardDTO.RequestDeleteDTO;
import com.project.hangmani.dto.BoardDTO.ResponseBoardDTO;
import com.project.hangmani.dto.BoardDTO.ResponseDeleteDTO;
import com.project.hangmani.exception.FailDeleteData;
import com.project.hangmani.exception.NotFoundUser;
import com.project.hangmani.repository.BoardRepository;
import com.project.hangmani.repository.UserRepository;
import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final RequestConvert requestConvert;
    private final ResponseConvert responseConvert;
    private final Util util;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository, Util util) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.requestConvert = new RequestConvert();
        this.util = util;
        this.responseConvert = new ResponseConvert(this.util);
    }

    /**
     *
     * @param boardDTO
     * @return ResponseBoardDTO
     */
    @Transactional
    public ResponseBoardDTO createBoard(RequestBoardDTO boardDTO) {
        //check id
        checkID(boardDTO.getBoardWriter());

        Board board = requestConvert.convertEntity(boardDTO);

        int no = boardRepository.insertBoard(board);
        Board resultBoard = boardRepository.findByNo(no).get();

        return responseConvert.convertResponseDTO(resultBoard);

    }

    /**
     *
     * @param boardDTO
     * @return ResponseDeleteDTO
     */
    @Transactional
    public ResponseDeleteDTO deleteBoard(RequestDeleteDTO boardDTO) {
        //check valid
        validRequest(boardDTO);

        int ret = boardRepository.deleteByNo(boardDTO.getBoardNo());
        if (ret == 0) {
            throw new FailDeleteData();
        }
        return responseConvert.convertResponseDTO(ret);
    }

    private void validRequest(RequestDeleteDTO dto) {
        //check id
        checkID(dto.getBoardWriter());

        //check already delete
        Optional<Board> byNo = boardRepository.findByNo(dto.getBoardNo());
        Board board = byNo.get();
        if (board.isDelete()){
            throw new FailDeleteData();
        }
    }
    private void checkID(String boardWriterID) {
        if (userRepository.findById(boardWriterID).isEmpty()){
            throw new NotFoundUser();
        }
    }
}