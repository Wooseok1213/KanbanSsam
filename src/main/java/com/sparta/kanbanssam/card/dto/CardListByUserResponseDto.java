package com.sparta.kanbanssam.card.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CardListByUserResponseDto {

    private Long guestId;
    private String guestName;
    private List<CardResponseDto> cardList;

    @Builder
    public CardListByUserResponseDto(Long guestId, String guestName, List<CardResponseDto> cardList) {
        this.guestId = guestId;
        this.guestName = guestName;
        this.cardList = cardList;
    }
}
