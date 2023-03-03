package com.project.hangmani.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BoardInsertDTO {
    @NotBlank
    private String title;
    @NotNull
    private String content;
    @NotNull
    private String boardWriter;

    public BoardInsertDTO() {
    }

    public BoardInsertDTO(String title, String content, String boardWriter) {
        this.title = title;
        this.content = content;
        this.boardWriter = boardWriter;
    }
}
