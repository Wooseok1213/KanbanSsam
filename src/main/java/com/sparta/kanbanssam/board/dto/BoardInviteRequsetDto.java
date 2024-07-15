package com.sparta.kanbanssam.board.dto;

import com.sparta.kanbanssam.board.entity.Board;
import com.sparta.kanbanssam.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class BoardInviteRequsetDto {

    private String email;
    private String name;

    private BoardInviteRequsetDto(User user, Board board) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
