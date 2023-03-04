package com.project.hangmani.dto;

import com.project.hangmani.domain.Board;
import com.project.hangmani.enums.ResponseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class BoardDTO {
    @Getter
    public static class RequestBoardDTO {
        @NotBlank
        private String title;
        @NotNull
        private String content;
        @NotNull
        private String boardWriter;

        public RequestBoardDTO() {
        }

        public RequestBoardDTO(String title, String content, String boardWriter) {
            this.title = title;
            this.content = content;
            this.boardWriter = boardWriter;
        }
    }
    @Getter
    public static class ResponseBoardDTO {
            private Board board;
            private Integer status;
            private String message;

            public ResponseBoardDTO() {
            }

            public ResponseBoardDTO(Board board, Integer status, String message) {
                this.board = board;
                this.status = status;
                this.message = message;
            }
    }
}
