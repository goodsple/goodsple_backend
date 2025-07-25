package com.goodsple.features.auth.service;

import com.goodsple.features.auth.dto.request.LoginRequest;
import com.goodsple.features.auth.dto.request.SignUpRequest;
import com.goodsple.features.auth.dto.response.SignUpResponse;
import com.goodsple.features.auth.dto.response.TokenResponse;
import com.goodsple.features.auth.dto.response.UserProfile;
import com.goodsple.features.auth.entity.EmailVerification;
import com.goodsple.features.auth.entity.User;
import com.goodsple.features.auth.enums.CheckType;
import com.goodsple.features.auth.enums.Role;
import com.goodsple.features.auth.mapper.EmailVerificationMapper;
import com.goodsple.features.auth.mapper.UserMapper;
import com.goodsple.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
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
    private final EmailService emailService;
    private final EmailVerificationMapper emailVerificationMapper;
    private final EmailVerificationService emailVerificationService;

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
    // 1) 인증번호 요청
    public void requestFindIdCode(String email) {
        // 1. 이메일이 가입된 회원 이메일인지 확인
        if (!userMapper.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "가입되지 않은 이메일입니다."
            );
        }
        // 2. 코드 생성, DB 저장, 메일 전송
        String code = RandomStringUtils.randomNumeric(6); // 6자리 숫자 인증코드 생성
        LocalDateTime now = LocalDateTime.now();

        // DB 저장
        EmailVerification record = EmailVerification.builder()
                .email(email)
                .code(code)
                .createdAt(now)
                .expiresAt(now.plusMinutes(3))
                .used(false)
                .build();
        emailVerificationMapper.insert(record);

        // 메일 발송
        emailService.sendVerificationEmail(email, code,"find-id"); // fromAddress 자동 사용
    }

    // 2) 인증번호 검증 & 사용 처리
    private void verifyCode(String email, String code) {
        emailVerificationMapper.findByEmailAndCode(email, code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "❗인증번호가 일치하지 않거나 만료되었습니다."
                ));
        emailVerificationMapper.updateUsed(email, code);
    }

    // 3) 아이디 찾기
    public String findLoginId(String name, String email, String code) {
        // 코드 검증 후
        verifyCode(email, code);

        // 검증 통과하면 로그인 아이디를 꺼내서 리턴
        return userMapper.selectLoginIdByNameAndEmail(name, email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "일치하는 회원이 없습니다."
                ));
    }

    // 비밀번호 찾기 - 인증번호 전송
    public void requestResetPasswordCode(String loginId, String email) {
        // 아이디와 이메일이 일치하는 회원 확인
        User user = userMapper.selectByLoginIdAndEmail(loginId, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 회원이 없습니다."));

        // 인증번호 생성 및 저장
        String code = emailVerificationService.createAndSaveCode(email);

        // 이메일 발송
        emailService.sendVerificationEmail(email, code,"reset-password");
    }

    // 비밀번호 찾기 - 인증번호 검증
    public void verifyResetPasswordCode(String email, String code) {
        emailVerificationService.verifyCode(email, code);
    }

    // 비밀번호 찾기 - 새 비밀번호 설정
    public void resetPassword(String loginId, String newPassword) {
        // 1. 사용자 존재 여부 확인
        User user = userMapper.findByLoginId(loginId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.");
        }

        // 2. 기존 비밀번호와 동일한지 확인
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "기존 비밀번호와 동일한 비밀번호는 사용할 수 없습니다.");
        }

        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 4. DB 업데이트
        userMapper.updatePassword(user.getUserId(), encodedPassword);
    }
}


