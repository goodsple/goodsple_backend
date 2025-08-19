package com.goodsple.features.admin.user.mapper;

import com.goodsple.features.admin.user.dto.AdminUserDetail;
import com.goodsple.features.admin.user.dto.AdminUserSearchCond;
import com.goodsple.features.admin.user.dto.AdminUserSummary;
import com.goodsple.features.auth.enums.Role;
import com.goodsple.features.auth.enums.SuspensionStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminUserMapper {

    /** 목록 조회 (필터 + 페이징) */
    List<AdminUserSummary> selectAdminUsers(@Param("cond") AdminUserSearchCond cond,
                                            @Param("offset") int offset);

    /** 목록 총 개수 */
    long countAdminUsers(@Param("cond") AdminUserSearchCond cond);

    /** 상세 조회(모달) */
    AdminUserDetail selectAdminUserDetail(@Param("userId") Long userId);

    /** 상태/권한 변경 (둘 다 nullable: 변경하는 값만 전달) */
    int adminUpdateUserStatus(@Param("userId") Long userId,
                              @Param("role") Role role,
                              @Param("status") SuspensionStatus status);

    /** (탈퇴 상태에서만) 실제 삭제 */
    int deleteUserIfWithdrawn(@Param("userId") Long userId);
}
