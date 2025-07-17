package com.goodsple.features.auth.mapper;

import com.goodsple.features.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;

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
}
