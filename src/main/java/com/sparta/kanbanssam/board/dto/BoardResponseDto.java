package com.sparta.kanbanssam.board.dto;

import com.sparta.kanbanssam.board.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponseDto {
    private Long boardId; // 보드 ID
    private String name; // 보드이름
    private String introduction; // 보드 소개,내용

    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.name = board.getName();
        this.introduction = board.getIntroduction();
    }

}
