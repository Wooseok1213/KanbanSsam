package com.sparta.kanbanssam.board.controller;


import com.sparta.kanbanssam.board.dto.*;
import com.sparta.kanbanssam.board.entity.Board;
import com.sparta.kanbanssam.board.entity.Guest;
import com.sparta.kanbanssam.board.service.BoardService;
import com.sparta.kanbanssam.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardservice;
    private final BoardService boardService;

    @ResponseBody
    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody @Valid BoardRequestDto requestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardResponseDto responseDto = boardservice.createBoard(requestDto, userDetails.getUser());
        return ResponseEntity.ok(responseDto);
    }

    //   보드 전체 조회
    @GetMapping
    public ResponseEntity<?> findAllBoard(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BoardResponseDto> responseDto = boardservice.findAllBoard(userDetails.getUser());
        return ResponseEntity.ok(responseDto);
    }

    //보드 선택 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<?> finaBoard(@PathVariable Long boardId,
                                       Guest guest,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Board board = boardservice.findBoard(boardId, userDetails.getUser(), guest);
        BoardResponseDto responseDto = new BoardResponseDto(board);
        return ResponseEntity.ok(responseDto);
    }

    //    보드 초대
    @PostMapping("/{boardId}/invite/{userId}")
    public ResponseEntity<?> userInviteToBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long userId,
            @PathVariable Long boardId) {
        Board board = boardService.inviteGuest(userDetails.getUser(), userId, boardId);
        BoardInviteResponseDto responseDto = new BoardInviteResponseDto(board);

        return ResponseEntity.ok(responseDto);
    }


    //상품수정
    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateBoard(@RequestBody BoardUpdateRequestDto requestDto,
                                         @PathVariable Long boardId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Board board = boardservice.updateBoard(boardId, requestDto, userDetails.getUser());
        BoardUpdateResponseDto reponseDto = new BoardUpdateResponseDto(board);

        return ResponseEntity.ok(reponseDto);
    }

    @GetMapping("/boardList")
    public String boardListView(Model model) {
        return "/board/boardList";
    }

    @GetMapping("/boardTest")
    public String boardTestView(Model model) {
        return "/board/boardTest";
    }

    //보드 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable Long boardId) {
        boardService.deleteBoard(boardId, userDetails.getUser());
        return ResponseEntity.ok().body("보드가 삭제되었습니다.");
    }
}
