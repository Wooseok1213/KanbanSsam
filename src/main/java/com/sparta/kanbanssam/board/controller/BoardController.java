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
//@RequestMapping("/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardservice;

    @ResponseBody
    @PostMapping("/boards")
    public ResponseEntity<?> createBoard(@RequestBody @Valid BoardRequestDto requestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardResponseDto responseDto = boardservice.createBoard(requestDto, userDetails.getUser());
        return ResponseEntity.ok(responseDto);
    }

    //   보드 전체 조회
    @ResponseBody
    @GetMapping("/boards")
    public ResponseEntity<?> findAllBoard(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BoardResponseDto> responseDto = boardservice.findAllBoard(userDetails.getUser());
        return ResponseEntity.ok(responseDto);
    }

    //보드 선택 조회
    @ResponseBody
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<?> finaBoard(@PathVariable Long boardId,
                                       Guest guest,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Board board = boardservice.findBoard(boardId, userDetails.getUser(), guest);
        BoardResponseDto responseDto = new BoardResponseDto(board);
        return ResponseEntity.ok(responseDto);
    }

    // 게스트의 보드 목록 조회
    @ResponseBody
    @GetMapping("/guests")
    public ResponseEntity<?> findAllBoardsByGuest(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardByGuestResponseDto boardList = boardservice.findAllBoardsByGuest(userDetails.getUser());
        return ResponseEntity.ok(boardList);
    }

    // 보드 초대
    @ResponseBody
    @PostMapping("/boards/{boardId}/invite")
    public ResponseEntity<?> userInviteToBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody List<String> emailList,
            @PathVariable Long boardId) {
        Board board = boardservice.inviteGuest(userDetails.getUser(), boardId, emailList);
        BoardInviteResponseDto responseDto = new BoardInviteResponseDto(board);

        return ResponseEntity.ok(responseDto);
        }

    //상품수정
    @ResponseBody
    @PutMapping("/boards/{boardId}")
    public ResponseEntity<?> updateBoard(
            @RequestBody @Valid BoardUpdateRequestDto requestDto,
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Board board = boardservice.updateBoard(boardId, requestDto, userDetails.getUser());
        BoardUpdateResponseDto reponseDto = new BoardUpdateResponseDto(board);

        return ResponseEntity.ok(reponseDto);
    }

    //보드 삭제
    @ResponseBody
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<?> deleteBoard (@AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId){
        boardservice.deleteBoard(boardId, userDetails.getUser());
        return ResponseEntity.ok().body("보드가 삭제되었습니다.");
    }

    @GetMapping("/boards/boardList")
    public String boardListView(Model model) {
        return "/board/boardList";
    }

    @GetMapping("/boards/boardTest")
    public String boardTestView(Model model) {
        return "/board/boardTest";
    }
}
