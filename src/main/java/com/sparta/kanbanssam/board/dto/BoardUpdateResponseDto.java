package com.sparta.kanbanssam.board.dto;

import com.sparta.kanbanssam.board.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardUpdateResponseDto {

    private String name; // 수정한 보드제목
    private String introduction; // 수정한 보드내용


    public BoardUpdateResponseDto(Board board) {
        this.name = board.getName();
        this.introduction = board.getIntroduction();
    }
}
