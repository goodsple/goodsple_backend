package com.goodsple.features.auth.service;

import com.goodsple.features.auth.dto.request.LoginRequest;
import com.goodsple.features.auth.dto.request.SignUpRequest;
import com.goodsple.features.user.dto.request.UpdateUserProfile;
import com.goodsple.features.auth.dto.response.SignUpResponse;
import com.goodsple.features.auth.dto.response.TokenResponse;
import com.goodsple.features.user.dto.request.UserInfo;
import com.goodsple.features.user.dto.response.UserProfile;
import com.goodsple.features.auth.entity.User;
import com.goodsple.features.auth.enums.CheckType;
import com.goodsple.features.auth.enums.Role;
import com.goodsple.features.auth.mapper.UserMapper;
import com.goodsple.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtProvider;
    private final EmailService emailService;
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

        if (user == null) {
            log.warn("사용자 없음: {}", loginRequest.getLoginId());
        } else {
            log.debug("사용자 존재: loginId={}, rawPwd={}, encodedPwd={}, matches={}",
                    user.getLoginId(),
                    loginRequest.getPassword(),
                    user.getPassword(),
                    passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()));
        }
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


    // 아이디 찾기 인증번호 발급
    // 1) 인증번호 요청
    public void requestFindIdCode(String name, String email) {
        if (!userMapper.existsByNameAndEmail(name, email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일치하는 회원이 없습니다.");
        }
        String code = emailVerificationService.createAndSaveCode(email,"find-id" );
        emailService.sendVerificationEmail(email, code, "find-id");
    }


    // 아이디 찾기 - 인증번호 검증 후 아이디 조회
    /**
     * 1) purpose="find-id" 로 검증
     * 2) 검증 통과 시 아이디 조회 후 반환
     */
    public String findLoginId(String name, String email, String code) {
        // 검증은 여기서 딱 한 번만
        emailVerificationService.verifyCode(email, code, "find-id");

        return userMapper
                .selectLoginIdByNameAndEmail(name, email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "일치하는 회원이 없습니다."
                ));
    }


    // 비밀번호 찾기 - 인증번호 발급
    public void requestResetPasswordCode(String loginId, String email) {
        userMapper.selectByLoginIdAndEmail(loginId, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 회원이 없습니다."));
        String code = emailVerificationService.createAndSaveCode(email, "reset-password");
        emailService.sendVerificationEmail(email, code, "reset-password");
    }

    // 비밀번호 찾기 - 인증번호 검증
    public void verifyResetPasswordCode(String email, String code) {
        emailVerificationService.verifyCode(email, code, "reset-password");
    }

    // 비밀번호 찾기 - 새 비밀번호 설정
    public void resetPassword(String loginId, String newPassword) {
        User user = userMapper.findByLoginId(loginId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "기존 비밀번호와 동일한 비밀번호는 사용할 수 없습니다.");
        }
        userMapper.updatePassword(user.getUserId(), passwordEncoder.encode(newPassword));
    }

    /**
     * 이름+이메일로 회원 존재 여부 확인
     */
    public boolean existsByNameAndEmail(String name, String email) {
        return userMapper.existsByNameAndEmail(name, email);
    }

    /**
     * 로그인ID+이메일로 회원 존재 여부 확인
     */
    public boolean existsByLoginIdAndEmail(String loginId, String email) {
        return userMapper
                .selectByLoginIdAndEmail(loginId, email)
                .isPresent();
    }

    /**
     * 현재 로그인된 사용자 프로필 정보 조회
     */
    @Transactional(readOnly = true)
    public UserProfile getProfile(Long userId) {
        User u = userMapper.selectMyProfile(userId);
        return UserProfile.builder()
                .userId(u.getUserId())
                .loginId(u.getLoginId())
                .nickname(u.getNickname())
                .name(u.getName())
                .email(u.getEmail())
                .phoneNumber(u.getPhoneNumber())
                .birthDate(u.getBirthDate())
                .gender(u.getGender())
                .profileImageUrl(u.getProfileImage())
                .loginType(u.getLoginType().name())
                .build();
    }

    /**
     * 내 프로필 수정
     */
    @Transactional
    public void updateMyProfile(Long userId, UpdateUserProfile upreq) {
        User u = userMapper.selectMyProfile(userId);

        if (upreq.getProfileImageUrl() != null)
            u.setProfileImage(upreq.getProfileImageUrl());
        if (upreq.getName() != null) u.setName(upreq.getName());
        if (upreq.getNickname() != null) u.setNickname(upreq.getNickname());
        if (upreq.getPhoneNumber() != null) u.setPhoneNumber(upreq.getPhoneNumber());
        // 비밀번호 변경: currentPassword + newPassword 모두 있어야 변경
        if (upreq.getCurrentPassword() != null && upreq.getNewPassword() != null) {
            if (!passwordEncoder.matches(upreq.getCurrentPassword(), u.getPassword())) {
                // 400 Bad Request 로 클라이언트에 메시지 전송
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "현재 비밀번호가 일치하지 않습니다."
                );
            }
            if (passwordEncoder.matches(upreq.getNewPassword(), u.getPassword())) {
                throw new IllegalArgumentException("새 비밀번호는 현재 비밀번호와 달라야 합니다.");
            }
            u.setPassword(passwordEncoder.encode(upreq.getNewPassword()));
        }
        // 이메일은 LOCAL 유저만 수정 가능
        if ("LOCAL".equals(u.getLoginType().name()) && upreq.getEmail() != null) {
            u.setEmail(upreq.getEmail());
        }

        userMapper.updateMyProfile(u);
    }

    /**
     * 내 계정 삭제(탈퇴)
     */
    @Transactional
    public void deleteMyProfile(Long userId) {
        userMapper.deleteMyProfile(userId);
    }

    /**
     * 마이페이지용 닉네임 + 프로필 이미지 조회
     */
    @Transactional(readOnly = true)
    public UserInfo getUserInfo(Long userId) {
        UserInfo info = userMapper.selectUserInfo(userId);
        if (info == null) {
            throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. id=" + userId);
        }
        return info;
    }

}


