package com.goodsple.features.auth.service;

import com.goodsple.features.auth.dto.request.LoginRequest;
import com.goodsple.features.auth.dto.request.SignUpRequest;
import com.goodsple.features.auth.dto.response.SignUpResponse;
import com.goodsple.features.auth.dto.response.TokenResponse;
import com.goodsple.features.auth.dto.response.UserProfile;
import com.goodsple.features.auth.entity.User;
import com.goodsple.features.auth.enums.CheckType;
import com.goodsple.features.auth.enums.Role;
import com.goodsple.features.auth.mapper.UserMapper;
import com.goodsple.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.coyote.BadRequestException;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

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
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
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

    /**
     * 현재 로그인된 사용자 프로필 정보 조회
     */
    public UserProfile getProfile(Long userId) {
        User u = userMapper.findById(userId);
        return UserProfile.builder()
                .loginId(u.getLoginId())
                .nickname(u.getNickname())
                .name(u.getName())
                .email(u.getEmail())
                .phoneNumber(u.getPhoneNumber())
                .birthDate(u.getBirthDate())
                .gender(u.getGender())
                .profileImageUrl(u.getProfileImage())  // 로컬 가입 시 저장된 URL 또는 카카오 프로필 URL
                .build();
    }

    // 아이디 찾기 인증번호 발급
    public void requestFindIdCode(String email) {
        String code = RandomStringUtils.randomNumeric(6); // 6자리 숫자 인증코드 생성
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(3); // 3분 뒤 만료 설정

        // 인증번호 DB에 저장
        userMapper.insertFindIdCode(email, code, expiresAt);

        // 이메일 전송 로직 (생략)
        // 예: 인증번호를 이메일로 전송하는 부분 구현
    }

    // 아이디 찾기 인증번호 검증
    public boolean validateFindIdCode(String email, String code) {
        LocalDateTime now = LocalDateTime.now();
        return userMapper.selectFindIdCode(email, code, now).isPresent();
    }

    // 아이디 찾기: 이메일로 로그인 아이디 조회
    public String findLoginId(String name, String email, String code) {
        // 인증번호 검증
        boolean valid = validateFindIdCode(email, code);
        if (!valid) {
            // ResponseStatusException을 사용하여 BAD_REQUEST 상태 코드와 메시지를 반환
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "유효하지 않은 인증번호입니다."
            );
        }

        // 아이디 조회
        String loginId = userMapper.selectLoginIdByNameAndEmail(name, email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "일치하는 회원이 없습니다."
                ));

        // 아이디를 암호화하여 반환
        return passwordEncoder.encode(loginId); // 암호화된 아이디 반환
    }
}


