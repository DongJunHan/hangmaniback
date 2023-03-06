package com.project.hangmani.service;

import com.project.hangmani.convert.ResponseConvert;
import com.project.hangmani.domain.Board;
import com.project.hangmani.dto.BoardDTO.RequestBoardDTO;
import com.project.hangmani.dto.BoardDTO.RequestDeleteDTO;
import com.project.hangmani.dto.BoardDTO.ResponseBoardDTO;
import com.project.hangmani.dto.BoardDTO.ResponseDeleteDTO;
import com.project.hangmani.exception.NotFoundUser;
import com.project.hangmani.repository.BoardRepository;
import com.project.hangmani.repository.UserRepository;
import com.project.hangmani.convert.RequestConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final RequestConvert requestConvert;
    private final ResponseConvert responseConvert;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.requestConvert = new RequestConvert();
        this.responseConvert = new ResponseConvert();
    }

    /**
     *
     * @param boardDTO
     * @return
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
     * @return
     */
    @Transactional
    public ResponseDeleteDTO deleteBoard(RequestDeleteDTO boardDTO) {
        //check id
        checkID(boardDTO.getBoardWriter());

        int ret = boardRepository.deleteBoard(boardDTO.getBoardNo());
        return responseConvert.convertResponseDTO(ret);
    }
    private void checkID(String boardWriter) {
        if (userRepository.findById(boardWriter).isEmpty()){
            throw new NotFoundUser();
        }
    }

}