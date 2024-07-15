package com.sparta.kanbanssam.card.repository;

import com.sparta.kanbanssam.board.entity.Board;
import com.sparta.kanbanssam.card.dto.CardListByColumnsResponseDto;
import com.sparta.kanbanssam.card.dto.CardListByUserResponseDto;
import com.sparta.kanbanssam.card.dto.CardResponseDto;

import java.util.List;

public interface CardRepositoryQuery {

    List<CardListByColumnsResponseDto> getCardListByColumnAtBoard(Board board);

    List<CardListByUserResponseDto> getCardListByUserAtBoard(Board board);

    List<CardResponseDto> getCardListAtColumn(Long columnId);

    List<CardResponseDto> getCardListByBoard(Long boardId);
}
