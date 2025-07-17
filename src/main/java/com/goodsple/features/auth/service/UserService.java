package com.goodsple.features.auth.service;

import com.goodsple.features.auth.dto.request.LoginRequest;
import com.goodsple.features.auth.dto.request.SignUpRequest;
import com.goodsple.features.auth.dto.response.SignUpResponse;
import com.goodsple.features.auth.dto.response.TokenResponse;
import com.goodsple.features.auth.entity.User;
import com.goodsple.features.auth.enums.CheckType;
import com.goodsple.features.auth.enums.Role;
import com.goodsple.features.auth.mapper.UserMapper;
import com.goodsple.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtProvider;

    // 회원가입 응답
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        validateDuplicate(signUpRequest);

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        User user = User.builder()
                .loginId(signUpRequest.getLoginId())
                .password(encodedPassword)
                .name(signUpRequest.getName())
                .nickname(signUpRequest.getNickname())
                .email(signUpRequest.getEmail())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .birthDate(signUpRequest.getBirthDate())
                .gender(signUpRequest.getGender())
                .role(Role.USER) // 기본값 USER로 설정
                .build();

        userMapper.createUser(user);
        // toBuilder() → DTO처럼 필드 구조가 완전히 같은 경우 일부 값만 수정할 때.
        // builder() → 처음부터 새로 만드는 경우, 다른 타입 간 변환, 필드 추가/삭제 필요할 때.

        User createUser = userMapper.findByLoginId(user.getLoginId());

        return SignUpResponse.builder()
                .userId(createUser.getUserId())
                .loginId(createUser.getLoginId())
                .name(createUser.getName())
                .nickname(createUser.getNickname())
                .email(createUser.getEmail())
                .gender(createUser.getGender())
                .role(createUser.getRole())
                .build();
    }

    // 중복체크
    private void validateDuplicate(SignUpRequest signUpRequest) {
        if (userMapper.existsByLoginId(signUpRequest.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 로그인 아이디입니다.");
        }
        if (userMapper.existsByNickname(signUpRequest.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        if (userMapper.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (userMapper.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("이미 사용 중인 휴대폰 번호입니다.");
        }
    }

    // checkType과 useMapper 맞추기위해서
    public boolean isAvailable(CheckType checkType, String value) {
        return switch (checkType) {
            case LOGIN_ID -> !userMapper.existsByLoginId(value);
            case NICKNAME -> !userMapper.existsByNickname(value);
            case EMAIL -> !userMapper.existsByEmail(value);
            case PHONE_NUMBER -> !userMapper.existsByPhoneNumber(value);
        };
    }

    public TokenResponse login(LoginRequest loginRequest) {
        // 사용자 조회
        User user = userMapper.findByLoginId(loginRequest.getLoginId());
        // 비밀번호 검증
        if(user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "아이디 또는 비밀번호가 올바르지 않습니다."
            );
        }
        // 토근 발급
        String at = jwtProvider.createAccessToken(user.getUserId(), user.getRole().name());
        String rt = jwtProvider.createRefreshToken(user.getUserId(), user.getRole().name());

        // 응답 dto 빌드
        return TokenResponse.builder()
                .accessToken(at)
                .refreshToken(rt)
                .build();
    }

}

