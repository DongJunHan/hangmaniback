package com.project.hangmani.board.service;

import com.project.hangmani.board.model.dto.*;
import com.project.hangmani.board.model.entity.Board;
import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.convert.ResponseConvert;
import com.project.hangmani.exception.FailDeleteData;
import com.project.hangmani.exception.NotFoundException;
import com.project.hangmani.exception.NotFoundUser;
import com.project.hangmani.board.repository.BoardRepository;
import com.project.hangmani.user.repository.UserRepository;
import com.project.hangmani.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private ResponseConvert responseConvert;
    private final Util util;
    @Autowired
    public BoardService(BoardRepository boardRepository, UserRepository userRepository, PropertiesValues propertiesValues) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.util = new Util(propertiesValues);
        this.responseConvert = new ResponseConvert(propertiesValues);
    }

    /**
     *
     * @param boardDTO
     * @return ResponseBoardDTO
     */
    @Transactional
    public ResponseGetDTO add(RequestInsertDTO boardDTO) {
        //check id
        checkID(boardDTO.getBoardWriter());

        Board board = boardDTO.convertToEntity(boardDTO);
        int no = boardRepository.add(board);
        BoardDTO resultBoard = boardRepository.getByNo(no, 0, 1).get();

        return new ResponseGetDTO().convertToDTO(resultBoard);

    }

    public List<ResponseGetDTO> getList(RequestRangeDTO boardDTO) {

        List<BoardDTO> boards = boardRepository.getList(boardDTO);
        if (boards.size() == 0)
            throw new NotFoundException();

        return new ResponseGetDTO().convertToList(boards);
    }

    public List<ResponseGetDTO> getByWriter(RequestWriterDTO boardDTO) {
        List<BoardDTO> boards = boardRepository.getByWriter(RequestWriterDTO.builder()
                .boardWriter(boardDTO.getBoardWriter())
                .limit(boardDTO.getLimit())
                .offset(boardDTO.getOffset())
                .build());
        if (boards.size() == 0)
            throw new NotFoundException();

        return new ResponseGetDTO().convertToList(boards);
    }

    /**
     *
     * @param boardDTO
     * @return ResponseDeleteDTO
     */
    @Transactional
    public ResponseDTO delete(RequestDeleteDTO boardDTO) {
        //check valid
        checkDelete(boardDTO);

        int ret = boardRepository.delete(boardDTO.getBoardNo());
        if (ret == 0) {
            throw new FailDeleteData();
        }

        return ResponseDTO.builder()
                .rowNum(ret)
                .build();
    }

    private void checkDelete(RequestDeleteDTO dto) {
        //check id
        checkID(dto.getBoardWriter());

        //check already delete
        Optional<BoardDTO> byNo = boardRepository.getByNo(dto.getBoardNo(),0,1);
        BoardDTO board = byNo.get();
        if (board.getIsDelete()){
            throw new FailDeleteData();
        }
    }

    private void checkID(String boardWriterID) {
        if (userRepository.getById(boardWriterID).isEmpty()){
            throw new NotFoundUser();
        }
    }
}