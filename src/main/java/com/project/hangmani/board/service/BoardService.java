package com.project.hangmani.board.service;

import com.project.hangmani.board.model.dto.*;
import com.project.hangmani.board.model.entity.Board;
import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.exception.FailDeleteData;
import com.project.hangmani.exception.NotFoundException;
import com.project.hangmani.exception.NotFoundUser;
import com.project.hangmani.board.repository.BoardRepository;
import com.project.hangmani.file.model.dto.AttachmentDTO;
import com.project.hangmani.file.service.FileService;
import com.project.hangmani.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final PropertiesValues propertiesValues;

    /**
     *
     * @param boardDTO
     * @return ResponseBoardDTO
     */
    @Transactional
    public ResponseGetDTO insert(RequestInsertDTO boardDTO) throws IOException {
        //check id
        checkID(boardDTO.getBoardWriter());

        Board board = boardDTO.convertToEntity();
        int no = boardRepository.insert(board);
        BoardDTO resultBoard = boardRepository.getByNo(no, 0, 1).get();
        //attachFiles save
        List<AttachmentDTO> attachFiles = fileService.saveAttachment(boardDTO.convertToDTO());
        //attachFile db insert
        int ret = boardRepository.insertAttachFiles(attachFiles);
        if (ret > 0) {
            String uploadDir = propertiesValues.getUploadDir();
            //TODO setter 없으니 builder로 빌드해야하는데 따로 만들어서 ResponseGetDTO변환 과정을 거쳐야할듯
//            resultBoard.setFiles(
//                    attachFiles.stream()
//                            .map(elem -> {
//                                elem.setOriginalFileName(uploadDir + elem.getOriginalFileName());
//                                elem.setSavedFileName(uploadDir + elem.getSavedFileName());
//                                return elem;
//                            })
//                            .toList()
//            );
        }
        return ResponseGetDTO.builder().build();

    }

    public List<ResponseGetDTO> getList(RequestRangeDTO boardDTO) {

        List<BoardDTO> boards = boardRepository.getList(boardDTO);
        if (boards.size() == 0)
            throw new NotFoundException();

        return List.of(ResponseGetDTO.builder().build());//new ResponseGetDTO().convertToList(boards);
    }

    public List<ResponseGetDTO> getByWriter(RequestWriterDTO boardDTO) {
        List<BoardDTO> boards = boardRepository.getByWriter(RequestWriterDTO.builder()
                .boardWriter(boardDTO.getBoardWriter())
                .limit(boardDTO.getLimit())
                .offset(boardDTO.getOffset())
                .build());
        if (boards.size() == 0)
            throw new NotFoundException();
        //TODO 변경해야함
        return List.of(ResponseGetDTO.builder().build());
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