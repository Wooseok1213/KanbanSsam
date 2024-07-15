package com.sparta.kanbanssam.card.service;

import com.sparta.kanbanssam.board.entity.Board;
import com.sparta.kanbanssam.board.repository.BoardRepository;
import com.sparta.kanbanssam.card.dto.CardListByColumnsResponseDto;
import com.sparta.kanbanssam.card.dto.CardListByUserResponseDto;
import com.sparta.kanbanssam.card.dto.CardRequestDto;
import com.sparta.kanbanssam.card.dto.CardResponseDto;
import com.sparta.kanbanssam.card.entity.Card;
import com.sparta.kanbanssam.card.repository.CardRepository;
import com.sparta.kanbanssam.column.entity.Columns;
import com.sparta.kanbanssam.column.repository.ColumnRepository;
import com.sparta.kanbanssam.common.enums.ErrorType;
import com.sparta.kanbanssam.common.exception.CustomException;
import com.sparta.kanbanssam.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;

    /**
     * 카드 생성
     * @param columnId 컬럼 ID
     * @param requestDto 카드 생성 정보
     * @param user 회원 정보
     * @return 카드 정보
     */
    @Transactional
    public CardResponseDto createCard(Long columnId, CardRequestDto requestDto, User user) {
        Columns columns = columnRepository.findById(columnId).orElseThrow(
                ()-> new CustomException(ErrorType.COLUMN_NOT_FOUND));

        // todo : 해당 보드에 초대된 사용자인지 확인 필요

        Long cardCnt = cardRepository.countAllByColumnsId(columnId);

        Card card = Card.builder()
                .user(user)
                .columns(columns)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .responsiblePerson(requestDto.getResponsiblePerson())
                .deadline(requestDto.getDeadline())
                .orders(cardCnt + 1)
                .build();

        Card saveCard = cardRepository.save(card);

        return new CardResponseDto(saveCard);
    }

    /**
     * 카드 수정
     * @param cardId 카드 ID
     * @param requestDto 카드 수정 정보
     * @param user 회원 정보
     * @return 카드 정보
     */
    @Transactional
    public CardResponseDto updateCard(Long cardId, CardRequestDto requestDto, User user) {
        Card card = findCard(cardId);

        // 카드 작성자 또는 매니저가 아닐 경우 예외처리
        card.validateAuthority(user);
        card.update(requestDto);

        return new CardResponseDto(card);
    }

    /**
     * 카드 삭제
     * @param cardId 카드 ID
     * @param user 회원 정보
     */
    public void deleteCard(Long cardId, User user) {
        Card card = findCard(cardId);
        card.validateAuthority(user);
        cardRepository.delete(card);
    }

    /**
     * 카드 순서 및 컬럼 이동
     * <p>
     *     순서만 이동하거나 컬럼까지 이동할 수 있음
     * </p>
     * @param columnId 컬럼 ID
     * @param cardIdList 이동한 컬럼의 카드 ID 순번 목록
     */
    @Transactional
    public void updateCardOrders(Long columnId, List<Long> cardIdList) {
        Columns columns = columnRepository.findByIdWithPessimisticLock(columnId)
                .orElseThrow(()-> new CustomException(ErrorType.COLUMN_NOT_FOUND));

        for (int i = 1; i <= cardIdList.size(); i++) {
            Card card = findCard(cardIdList.get(i - 1));
            card.updateOrders((long) i, columns);
            cardRepository.save(card);
        }
    }

    /**
     * 카드 단일 조회
     * @param cardId 카드 ID
     * @return 카드 정보
     */
    public CardResponseDto getCard(Long cardId) {
        return new CardResponseDto(findCard(cardId));
    }

    /**
     * 보드 별 카드 전체 목록 조회
     * @param boardId 보드 ID
     * @return 카드 목록
     */
    public List<CardResponseDto> getCardListByBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new CustomException(ErrorType.NOT_FOUND_BOARD));

        return cardRepository.getCardListByBoard(boardId);
    }

    /**
     * 단일 컬럼 별 카드 목록 조회
     * @param columnId 컬럼 ID
     * @return 카드 목록
     */
    public List<CardResponseDto> getCardList(Long columnId) {
        Columns columns = columnRepository.findById(columnId).orElseThrow(
                ()-> new CustomException(ErrorType.COLUMN_NOT_FOUND));

        return cardRepository.getCardListAtColumn(columnId);
    }

    /**
     * 전체 컬럼 별 카드 목록 조회
     * @param boardId 보드 ID
     * @return 카드 목록
     */
    public List<CardListByColumnsResponseDto> getCardListByColumn(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new CustomException(ErrorType.NOT_FOUND_BOARD));

        return cardRepository.getCardListByColumnAtBoard(board);
    }

    /**
     * 사용자 별 카드 목록 조회
     * @param boardId 보드 ID
     * @return 카드 목록
     */
    public List<CardListByUserResponseDto> getCardListByUser(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new CustomException(ErrorType.NOT_FOUND_BOARD));

        return cardRepository.getCardListByUserAtBoard(board);
    }

    /**
     * Id로 Card 엔티티 찾기
     * @param cardId 카드 ID
     * @return 카드 정보
     */
    public Card findCard(Long cardId) {
        return cardRepository.findByIdWithPessimisticLock(cardId)
                .orElseThrow(()-> new CustomException(ErrorType.CARD_NOT_FOUND));
    }
}
