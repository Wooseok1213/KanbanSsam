package com.sparta.kanbanssam.column.service;

import com.sparta.kanbanssam.board.entity.Board;
import com.sparta.kanbanssam.board.repository.BoardRepository;
import com.sparta.kanbanssam.card.entity.Card;
import com.sparta.kanbanssam.column.dto.ColumnRequestDto;
import com.sparta.kanbanssam.column.dto.ColumnResponseDto;
import com.sparta.kanbanssam.column.entity.Columns;
import com.sparta.kanbanssam.column.repository.ColumnRepository;
import com.sparta.kanbanssam.common.enums.ErrorType;
import com.sparta.kanbanssam.common.exception.CustomException;
import com.sparta.kanbanssam.common.authorization.AuthorizationService;
import com.sparta.kanbanssam.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final AuthorizationService authorizationService;
    private final BoardRepository boardRepository;

    /**
     * 컬럼 생성
     * @param boardId 보드 ID
     * @param requestDto 컬럼 생성 정보
     * @param user 회원 정보
     * @return 컬럼 정보
     */
    @Transactional
    public ColumnResponseDto createColum(Long boardId, ColumnRequestDto requestDto, User user) {

        // 요청한 사용자가 해당 보드의 관리자임을 확인
        Board board = authorizationService.validateUserIsBoardManager(boardId, user);

        // 동일한 이름의 컬럼이 이미 존재하는지 확인
        if (columnRepository.existsByBoardIdAndName(board.getId(), requestDto.getName())) {
            throw new CustomException(ErrorType.COLUMN_ALREADY_EXISTS);
        }

        // 해당 보드에 존재하는 컬럼 총 개수
        Long columnCnt = columnRepository.countAllByBoardId(boardId);

        Columns column = Columns.builder()
                .board(board)
                .name(requestDto.getName())
                .orders(columnCnt + 1)
                .build();

        Columns saveColumn = columnRepository.save(column);

        return new ColumnResponseDto(saveColumn);
    }


    /**
     * 컬럼 수정
     * @param columnId 컬럼 ID
     * @param requestDto 컬럼 수정 정보
     * @param user 회원 정보
     * @return 컬럼 정보
     */
    @Transactional
    public ColumnResponseDto updateColumn(Long columnId, ColumnRequestDto requestDto, User user) {

        Columns column = getColumn(columnId);

        // 컬럼 작성자가 아닐 경우 예외처리(보드 매니저가 아닐경우)
        Board board =authorizationService.validateUserIsBoardManager(column.getBoard().getId(), user);

        // 동일한 이름의 컬럼이 이미 존재하는지 확인
        if (columnRepository.existsByBoardIdAndName(board.getId(), requestDto.getName())) {
            throw new CustomException(ErrorType.COLUMN_ALREADY_EXISTS);
        }

        column.update(requestDto);

        return new ColumnResponseDto(column);
    }

    /**
     * 컬럼 삭제
     * @param columnId 컬럼 ID
     * @param user 회원 정보
     */
    @Transactional
    public void deleteColumn(Long columnId, User user) {
        Columns column = getColumn(columnId);

        // 컬럼 작성자가 아닐 경우 예외처리(보드 매니저가 아닐경우)
        authorizationService.validateUserIsBoardManager(column.getBoard().getId(), user);
        columnRepository.delete(column);
    }

    /**
     * 컬럼 순서 수정
     * @param boardId 보드 ID
     * @param columnIdList 이동한 보드의 컬럼ID 순번 목록
     * @param user 회원정보
     */
    @Transactional
    public void updateColumnOrders(Long boardId, List<Long> columnIdList, User user) {

        // 컬럼 작성자가 아닐 경우 예외처리(보드 매니저가 아닐경우)
        Board board =authorizationService.validateUserIsBoardManager(boardId, user);

        for (int i = 1; i <= columnIdList.size(); i++) {
            Columns column = getColumn(columnIdList.get(i-1));
            column.updateOrders((long) i, board);
            columnRepository.save(column);
        }
    }

    /**
     * Id로 Columns 엔티티 찾기
     * @param columnId 컬럼 ID
     * @return 컬럼 정보
     */
    private Columns getColumn(Long columnId) {
        return columnRepository.findById(columnId)
                .orElseThrow(()-> new CustomException(ErrorType.COLUMN_NOT_FOUND));
    }

}
