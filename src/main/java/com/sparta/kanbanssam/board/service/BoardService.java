package com.sparta.kanbanssam.board.service;

import com.sparta.kanbanssam.board.dto.BoardRequestDto;
import com.sparta.kanbanssam.board.dto.BoardResponseDto;
import com.sparta.kanbanssam.board.dto.BoardUpdateRequestDto;
import com.sparta.kanbanssam.board.entity.Board;
import com.sparta.kanbanssam.board.entity.Guest;
import com.sparta.kanbanssam.board.repository.BoardRepository;
import com.sparta.kanbanssam.board.repository.GuestRepository;
import com.sparta.kanbanssam.common.enums.ErrorType;
import com.sparta.kanbanssam.common.enums.UserRole;
import com.sparta.kanbanssam.common.exception.CustomException;
import com.sparta.kanbanssam.user.entity.User;
import com.sparta.kanbanssam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor


public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final GuestRepository guestRepository;

    @Transactional
    //유저가 있는지 확인 후 없다면 Exception
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
        user = userRepository.findByAccountId(user.getAccountId()).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_USER));

        //유저 Role이 매니저인지 검증하는 로직
        checkUserRole(user);

        Board board = Board.builder()
                .name(requestDto.getName())
                .introduction(requestDto.getIntroduction())
                .user(user)
                .build();

        boardRepository.save(board);

        return new BoardResponseDto(board);
    }

    //해당 유저 보드 전체 조회
    @Transactional
    public List<BoardResponseDto> findAllBoard(User user) {
        List<Board> boardList = boardRepository.findAllByUserId(user.getId());

        if (boardList.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND_USER_BOARD);
        }
        return boardList.stream()
                .map(BoardResponseDto::new)
                .toList();
    }

    //보드 선택 조회
    public Board findBoard(Long boardId, User user, Guest guest) {
        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_BOARD));

        if (!board.getUser().getId().equals(user.getId()) || !board.getUser().getId().equals(guest.getUser().getId())) {
            throw new CustomException(ErrorType.BOARD_ACCESS_FORBIDDEN);
        }
        return board;
    }

    // 보드수정로직
    @Transactional
    public Board updateBoard(Long boardId, BoardUpdateRequestDto requestDto, User user) {
        //보드 ID 검색 후 Null 값이라면 Exception 발생
        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_BOARD));
        //유저 Role이 매니저인지 검증하는 로직
        checkUserRole(user);
        //보드에서 가져온 ID 와 User 의 ID가 다르다면 Exception 발생
        if (!board.getUser().getAccountId().equals(user.getAccountId())) {
            new CustomException(ErrorType.BOARD_ACCESS_FORBIDDEN);
        }
        // 수정
        board.updateBoard(requestDto);
        return board;
    }

    //보드 삭제
    @Transactional
    public void deleteBoard(Long boardId, User user) {

        // board 의 ID가 없다면 Exception 발생
        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_BOARD));
        //userRole이 MANAGER인지 검증
        checkUserRole(user);
        if (!board.getUser().getAccountId().equals(user.getAccountId())) {
            throw new CustomException(ErrorType.BOARD_ACCESS_FORBIDDEN);
        }
        boardRepository.delete(board);
    }

    //UserRole 이 Manager 인지 검증하는 로직
    private void checkUserRole(User user) {
        if (!user.getUserRole().equals(UserRole.MANAGER)) {
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }
    }

    // 유저 아이디 검사
    private void checkUserId(User user, Board board) {
        if (!(user.getId().equals(board.getUser().getId()))) {
            throw new CustomException(ErrorType.NOT_FOUND_USER);
        }
    }
}




























//    //보드초대
//    public Board inviteUserToBoard(Long boardId, BoardInviteRequsetDto boardInviteRequsetDto, User user) {
//        Board board = boardRepository.findById(boardId).orElseThrow(()
//                -> new CustomException(ErrorType.NOT_FOUND_BOARD));
//        //매니저인지 검증
//        checkUserRole(user);
//        userService.getUsersByEmail(invite.inviteUser());
//
//        invite.inviteUser(boardInviteRequsetDto);
//    }


