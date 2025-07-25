package com.goodsple.features.auth.mapper;

import com.goodsple.features.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Optional;

// MyBatis에서 Mapper 인터페이스 = SQL 쿼리를 호출하는 Java 메서드와 DB 매핑을 연결하는 역할
@Mapper
public interface UserMapper {
    void createUser(User user);

    // 중복 체크해야 할 항목
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    User findByLoginId(String loginId);
    // 회원가입 직후 user의 userId 값을 포함한 전체 정보를 다시 가져오기 위해서
    // MyBatis에서 createUser(User user) 실행 시, userId는 DB에서 자동 생성
    // user 객체 안에 userId 값이 바로 들어오지 않기에
    // DB에 다시 조회해서 userId, role, gender 등 전체 정보까지 포함해서 가져오는 방식
    // createUser → DB에 INSERT → 다시 findByLoginId로 SELECT 구조

    User findByKakaoId(String kakaoId);

    /** 유저 ID로 사용자 정보를 조회합니다. */
    User findById(Long userId);

    // 이름,이메일 맞는 유저 조회
    boolean existsByNameAndEmail(@Param("name") String name, @Param("email") String email);

    // 아이디 찾기용 인증번호 저장
    void insertFindIdCode(@Param("email") String email,
                          @Param("code") String code,
                          @Param("expiresAt") LocalDateTime expiresAt);

    // 아이디 찾기: 이메일로 로그인 아이디 조회
    Optional<String> selectLoginIdByNameAndEmail(@Param("name") String name,
                                                 @Param("email") String email);


    // 비밀번호 재설정용 인증번호 저장
    void insertResetPasswordCode(@Param("email") String email,
                                 @Param("code") String code,
                                 @Param("expiresAt") LocalDateTime expiresAt);


    // 비밀번호 재설정 인증번호 검증
    Optional<String> selectResetPasswordCode(@Param("email") String email,
                                             @Param("code") String code,
                                             @Param("now") LocalDateTime now);

    // 아이디 + 이메일로 유저 조회
    Optional<User> selectByLoginIdAndEmail(@Param("loginId") String loginId,
                                           @Param("email") String email);

    // 유저 ID로 비밀번호 업데이트
    void updatePassword(@Param("userId") Long userId,
                        @Param("password") String password);

}
