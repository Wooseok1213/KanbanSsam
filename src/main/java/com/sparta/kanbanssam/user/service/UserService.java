package com.sparta.kanbanssam.user.service;

import com.sparta.kanbanssam.common.enums.ErrorType;
import com.sparta.kanbanssam.common.exception.CustomException;
import com.sparta.kanbanssam.security.UserDetailsImpl;
import com.sparta.kanbanssam.user.dto.UserResponseDto;
import com.sparta.kanbanssam.user.dto.UserSignUpRequestDto;
import com.sparta.kanbanssam.user.entity.User;
import com.sparta.kanbanssam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(UserSignUpRequestDto requestDto) {
        String accountId = requestDto.getAccountId();
        String email = requestDto.getEmail();

        // 아이디 중복 검사
        userRepository.findByAccountId(accountId).ifPresent(u -> {
            throw new CustomException(ErrorType.DUPLICATE_ACCOUNT_ID);
        });

        // 이메일 중복 검사
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new CustomException(ErrorType.DUPLICATE_EMAIL);
        });

        // 비밀번호 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());

        // User DB에 저장
        User user = User.builder()
                .email(email)
                .name(requestDto.getName())
                .accountId(accountId)
                .password(password)
                .build();

        userRepository.save(user);

        //클라이언트에 성공 메시지와 상태코드를 반환한다.

    }

    //유저조회

    public UserResponseDto getUsersByEmail(UserDetailsImpl userDetails, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        if (user.getEmail().equals(userDetails.getUser().getEmail())) {
            throw new CustomException(ErrorType.INVALID_USER);
        }

        return new UserResponseDto(user.getName(), user.getEmail());
    }
}
