package com.project.hangmani.service;

import com.project.hangmani.domain.Board;
import com.project.hangmani.dto.BoardDTO;
import com.project.hangmani.dto.BoardDTO.ResponseBoardDTO;
import com.project.hangmani.enums.ResponseStatus;
import com.project.hangmani.exception.NotFoundUser;
import com.project.hangmani.repository.BoardRepository;
import com.project.hangmani.repository.UserRepository;
import com.project.hangmani.convert.RequestConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final RequestConvert requestConvert;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.requestConvert = new RequestConvert();
    }

    /**
     *
     * @param boardDTO
     * @return
     */
    @Transactional
    public ResponseBoardDTO createBoard(BoardDTO.RequestBoardDTO boardDTO) {
        //check id
        if (userRepository.findById(boardDTO.getBoardWriter()).isEmpty()){
            String message = "존재하지 않는 사용자 입니다.";
            throw new NotFoundUser(message);
//            return new ResponseBoardDTO(null, HttpStatusCode.valueOf(404), message);
        }

        Board board = requestConvert.boardInsertDTOToBoard(boardDTO);

        int no = boardRepository.insertBoard(board);
        Board resultBoard = boardRepository.findByNo(no).get();
        return new ResponseBoardDTO(resultBoard, ResponseStatus.OK.getCode(), "success");

    }
}