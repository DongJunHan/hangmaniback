package com.project.hangmani.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

public class BoardDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestBoardDTO {
        @NotBlank(message = "boardTitle insert")
        private String boardTitle;
        @NotNull(message = "boardContent insert")
        private String boardContent;
        @NotNull(message = "boardWriter insert")
        private String boardWriter;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RequestDeleteDTO {
        @NotNull(message = "please boardNo insert")
        private int boardNo;
        @NotNull(message = "please boardWriter insert")
        private String boardWriter;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseBoardDTO {
        private int boardNo;
        private String boardTitle;
        private String boardContent;
        private String boardWriter;
        private Date createAt;
        private Date updateAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseDeleteDTO {
        private int rowNum;
    }

}
