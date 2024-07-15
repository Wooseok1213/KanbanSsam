package com.sparta.kanbanssam.user.entity;

import com.sparta.kanbanssam.card.entity.Card;
import com.sparta.kanbanssam.comment.entity.Comment;
import com.sparta.kanbanssam.common.entity.Timestamped;
import com.sparta.kanbanssam.common.enums.ErrorType;
import com.sparta.kanbanssam.common.enums.UserRole;
import com.sparta.kanbanssam.common.enums.UserStatus;
import com.sparta.kanbanssam.common.exception.CustomException;
import jakarta.persistence.*;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name="user")
@NoArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    private String accountId;
    private String password;
    private String email;
    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Setter
    private String refreshToken;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Card> cardList;



    @Builder
    public User(String accountId, String password, String email, String name) {
        this.accountId = accountId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.userStatus = UserStatus.UNAUTHORIZED;
        this.userRole = UserRole.USER;
    }


    public boolean validateRefreshToken(String refreshToken) {
        return this.refreshToken != null && this.refreshToken.equals(refreshToken);
    }

    public void checkUserRole() {
        if (!UserRole.MANAGER.equals(this.userRole)) {
            throw new CustomException(ErrorType.USER_NOT_AUTHORIZATION);
        }
    }
}
