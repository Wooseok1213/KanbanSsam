package com.sparta.kanbanssam.board.entity;


import com.sparta.kanbanssam.board.dto.BoardUpdateRequestDto;
import com.sparta.kanbanssam.card.entity.Card;
import com.sparta.kanbanssam.column.entity.Columns;
import com.sparta.kanbanssam.common.enums.ErrorType;
import com.sparta.kanbanssam.common.enums.UserRole;
import com.sparta.kanbanssam.common.exception.CustomException;
import com.sparta.kanbanssam.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table (name = "Board")
public class Board {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String introduction;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Columns> columnsList;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Guest> guestList;

    @Builder
    public Board(Long id, User user, String name, String introduction) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.introduction = introduction;
    }

    public void updateBoard(BoardUpdateRequestDto requestDto) {
        this.name = requestDto.getName();
        this.introduction = requestDto.getIntroduction();
    }

    public void validateAuthority(User user) {
        if (!this.user.getId().equals(user.getId()) && !UserRole.MANAGER.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.BOARD_ACCESS_FORBIDDEN);
        }
    }

}
