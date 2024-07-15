package com.sparta.kanbanssam.column.controller;

import com.sparta.kanbanssam.card.dto.CardResponseDto;
import com.sparta.kanbanssam.column.dto.ColumnRequestDto;
import com.sparta.kanbanssam.column.dto.ColumnResponseDto;
import com.sparta.kanbanssam.column.entity.Columns;
import com.sparta.kanbanssam.column.service.ColumnService;
import com.sparta.kanbanssam.common.authorization.AuthorizationService;
import com.sparta.kanbanssam.common.enums.ErrorType;
import com.sparta.kanbanssam.common.exception.CustomException;
import com.sparta.kanbanssam.security.UserDetailsImpl;
import com.sparta.kanbanssam.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;
    private final AuthorizationService authorizationService;

    /**
     * 컬럼 생성
     * @param boardId 보드 ID
     * @param requestDto 컬럼 생성 정보
     * @return 컬럼 정보
     */
    @ResponseBody
    @PostMapping("/boards/{boardId}/columns")
    public ResponseEntity<?> createColumn(
            @PathVariable Long boardId,
            @RequestBody ColumnRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        // 로그인된 사용자 정보 가져오기
        User user = userDetails.getUser();

        // 권한 확인 후 컬럼 생성
        ColumnResponseDto responseDto = columnService.createColum(boardId, requestDto, user);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 컬럼 수정
     * @param columnId 컬럼 ID
     * @param requestDto 컬럼 수정 정보
     * @return 카드 정보
     */
    @ResponseBody
    @PutMapping("/columns/{columnId}")
    public ResponseEntity<?> updateColumn(
            @PathVariable Long columnId,
            @Valid @RequestBody ColumnRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // 로그인된 사용자 정보 가져오기
        User user = userDetails.getUser();

        // 권한 확인 후 컬럼 생성
        ColumnResponseDto responseDto = columnService.updateColumn(columnId, requestDto, user);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 컬럼 삭제
     * @param columnId 컬럼 ID
     */
    @ResponseBody
    @DeleteMapping("/columns/{columnId}")
    public void deleteColumn(
            @PathVariable Long columnId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        // 로그인된 사용자 정보 가져오기
        User user = userDetails.getUser();

        // 권한 확인 후 컬럼 삭제
        columnService.deleteColumn(columnId, user);

    }

    /**
     * 컬럼 순서 이동
     * @param boardId 보드 ID
     * @param columnIdList 컬럼 ID 목록
     */
    @ResponseBody
    @PutMapping("/boards/{boardId}/columns/orders")
    public void updateColumnOrders(
            @PathVariable Long boardId,
            @RequestBody List<Long> columnIdList,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails == null) {
            System.out.println("UserDetails is null");
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }

        // 수신된 데이터 로그 출력
        System.out.println("Received columnIdList: " + columnIdList);

        // 로그로 유저 정보 출력
        System.out.println("User authenticated: " + userDetails.getUsername());
        // 로그인된 사용자 정보 가져오기
        User user = userDetails.getUser();
        columnService.updateColumnOrders(boardId, columnIdList, user);
    }





}
