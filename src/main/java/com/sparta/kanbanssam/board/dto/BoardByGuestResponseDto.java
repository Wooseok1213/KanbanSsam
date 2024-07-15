package com.sparta.kanbanssam.board.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class BoardByGuestResponseDto {

    private String userName;
    private List<BoardResponseDto> boardList;

    public BoardByGuestResponseDto(String userName, List<BoardResponseDto> boardList) {
        this.userName = userName;
        this.boardList = boardList;
    }
}
