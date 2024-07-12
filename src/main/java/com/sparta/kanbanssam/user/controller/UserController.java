package com.sparta.kanbanssam.user.controller;

import com.sparta.kanbanssam.user.dto.UserSignUpRequestDto;
import com.sparta.kanbanssam.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        log.info("찍힐까요!?");
        userService.signUp(requestDto);
        return ResponseEntity.ok("응답부 추후 수정");
    }



    //로그아웃 POST /users/logout

    //회원조회 GET /users
}