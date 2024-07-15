package com.sparta.kanbanssam.card.controller;

import com.sparta.kanbanssam.card.dto.CardListByColumnsResponseDto;
import com.sparta.kanbanssam.card.dto.CardListByUserResponseDto;
import com.sparta.kanbanssam.card.dto.CardRequestDto;
import com.sparta.kanbanssam.card.dto.CardResponseDto;
import com.sparta.kanbanssam.card.service.CardService;
import com.sparta.kanbanssam.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    /**
     * 카드 생성
     * @param columnId 컬럼 ID
     * @param requestDto 카드 생성 정보
     * @return 카드 정보
     */
    @ResponseBody
    @PostMapping("/columns/{columnId}/cards")
    public ResponseEntity<?> createCard(
            @PathVariable Long columnId,
            @RequestBody @Valid CardRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CardResponseDto responseDto = cardService.createCard(columnId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 카드 수정
     * @param cardId 카드 ID
     * @param requestDto 카드 수정 정보
     * @return 카드 정보
     */
    @ResponseBody
    @PutMapping("/cards/{cardId}")
    public ResponseEntity<?> updateCard(
            @PathVariable Long cardId,
            @Valid @RequestBody CardRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CardResponseDto responseDto = cardService.updateCard(cardId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 카드 삭제
     * @param cardId 카드 ID
     */
    @ResponseBody
    @DeleteMapping("/cards/{cardId}")
    public void deleteCard(
            @PathVariable Long cardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cardService.deleteCard(cardId, userDetails.getUser());
    }

    /**
     * 카드 순서 및 컬럼 이동
     * <p>
     *     순서만 이동하거나 컬럼까지 이동할 수 있음
     * </p>
     * @param columnId 컬럼 ID
     * @param cardIdList 카드 ID 목록
     */
    @ResponseBody
    @PutMapping("/columns/{columnId}/cards/orders")
    public void updateCardOrders(@PathVariable Long columnId, @RequestBody List<Long> cardIdList) {
        cardService.updateCardOrders(columnId, cardIdList);
    }

    /**
     * 카드 단일 조회
     * @param cardId 카드 ID
     * @return 카드 정보
     */
    @ResponseBody
    @GetMapping("/api/cards/{cardId}")
    public ResponseEntity<?> getCard(
            @PathVariable Long cardId) {
        CardResponseDto responseDto = cardService.getCard(cardId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 보드 별 카드 전체 목록 조회
     * @param boardId 보드 ID
     * @return 카드 목록
     */
    @ResponseBody
    @GetMapping("/boards/{boardId}/cards")
    public ResponseEntity<?> getCardListByBoard(
            @PathVariable Long boardId) {

        List<CardResponseDto> responseDtos = cardService.getCardListByBoard(boardId);
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * 단일 컬럼 카드 목록 조회
     * @param columnId 컬럼 ID
     * @return 카드 목록
     */
    @ResponseBody
    @GetMapping("/columns/{columnId}/cards")
    public ResponseEntity<?> getCardByColumn(
            @PathVariable Long columnId) {
        List<CardResponseDto> cardList = cardService.getCardList(columnId);
        return ResponseEntity.ok(cardList);
    }

    /**
     * 전체 컬럼 별 카드 목록 조회
     * @param boardId 보드 ID
     * @return 컬럼 별 카드 목록
     */
    @ResponseBody
    @GetMapping("/boards/{boardId}/cards/byColumns")
    public ResponseEntity<?> getCardListByColumn(
            @PathVariable Long boardId) {
        List<CardListByColumnsResponseDto> responseDto = cardService.getCardListByColumn(boardId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 사용자 별 카드 목록 조회
     * @param boardId 보드 ID
     * @return 카드 목록
     */
    @ResponseBody
    @GetMapping("/boards/{boardId}/cards/byUser")
    public ResponseEntity<?> getCardListByUser(@PathVariable Long boardId) {
        List<CardListByUserResponseDto> responseDtos = cardService.getCardListByUser(boardId);
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * view
     */

    /**
     * 보드 전체 컬럼 별 카드 목록 View
     * @return card.html
     */
    @GetMapping("/boards/{boardId}/columns/cardList")
    public String cardListView(@PathVariable Long boardId, Model model) {
        List<CardListByColumnsResponseDto> cardListByColumn = cardService.getCardListByColumn(boardId);
        model.addAttribute("cardListByColumn", cardListByColumn);
        model.addAttribute("boardId", boardId);

        List<CardListByUserResponseDto> cardListByUser = cardService.getCardListByUser(boardId);
        model.addAttribute("cardListByUser", cardListByUser);

        return "/card/cardList";
    }

    /**
     * 카드 상세 View
     * @param cardId 카드 ID
     * @return cardDetail.html
     */
    @GetMapping("/cards/{cardId}")
    public String cardDetail(@PathVariable Long cardId, Model model) {
        CardResponseDto responseDto = cardService.getCard(cardId);
        model.addAttribute("card", responseDto);
        return "/card/cardDetail";
    }
}
