package com.sparta.kanbanssam.user.controller;

import com.sparta.kanbanssam.security.UserDetailsImpl;
import com.sparta.kanbanssam.user.dto.EmailRequestDto;
import com.sparta.kanbanssam.user.dto.UserResponseDto;
import com.sparta.kanbanssam.user.dto.UserSignUpRequestDto;
import com.sparta.kanbanssam.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 로그인 뷰
    @GetMapping("/view/login-page")
    public String loginPage() {
        return "/user/login";
    }

    // 회원가입 뷰
    @GetMapping("/view/signup")
    public String signupPage() {
        return "/user/signup";
    }

    /**
     * 회원가입 API
     * @param requestDto 회원가입 내용
     * @return ResponseEntity: 응답 코드, 응답 메시지
     */
    @PostMapping("")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        userService.signUp(requestDto);
        return ResponseEntity.ok("회원가입 완료");
    }

    /**
     * 회원조회 API
//     * @param requestDto 초대할 사용자 이메일, userDetails
     * @return userResponseDto 초대할 사용자 이메일, 이름
     */
    @GetMapping
    public ResponseEntity<?> getUsers(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      String email) {
        UserResponseDto userResponseDto = userService.getUsersByEmail(userDetails, email);
        return ResponseEntity.ok(userResponseDto);
    }

}
