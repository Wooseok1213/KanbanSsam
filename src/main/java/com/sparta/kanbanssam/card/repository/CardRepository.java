package com.sparta.kanbanssam.card.repository;

import com.sparta.kanbanssam.card.entity.Card;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryQuery {
    Long countAllByColumnsId(Long columnId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Card c WHERE c.id = :cardId")
    Optional<Card> findByIdWithPessimisticLock(@Param("cardId") Long cardId);

}
