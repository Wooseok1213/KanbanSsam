package com.sparta.kanbanssam.board.repository;

import com.sparta.kanbanssam.board.entity.Board;
import com.sparta.kanbanssam.board.entity.Guest;
import com.sparta.kanbanssam.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    boolean existsByUserId(Long inviteUserId);

    List<Guest> findAllByUser(User user);
}
