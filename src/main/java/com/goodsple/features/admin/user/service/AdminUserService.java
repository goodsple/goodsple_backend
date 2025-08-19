package com.goodsple.features.admin.user.service;

import com.goodsple.features.admin.user.dto.AdminUpdateUserRequest;
import com.goodsple.features.admin.user.dto.AdminUserDetail;
import com.goodsple.features.admin.user.dto.AdminUserSearchCond;
import com.goodsple.features.admin.user.dto.AdminUserSummary;
import com.goodsple.features.admin.user.mapper.AdminUserMapper;
import com.goodsple.features.auth.enums.Role;
import com.goodsple.features.auth.enums.SuspensionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본은 읽기 트랜잭션
public class AdminUserService {
    private final AdminUserMapper adminUserMapper;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private static final Set<String> ALLOWED_QUERY_TYPES = Set.of("loginId", "nickname", "userId");
    private static final Set<String> ALLOWED_STATUSES = Set.of("active", "suspended", "withdrawn");
    private static final Set<String> ALLOWED_ROLES    = Set.of("user", "admin");

    /** 목록 조회: 읽기 전용 그대로 사용 */
    public List<AdminUserSummary> list(AdminUserSearchCond cond) {
        AdminUserSearchCond fixed = normalize(cond);     // 기본값/검증/트림
        System.out.println("[ADMIN] list fixed=" + fixed);
        int offset = fixed.getPage() * fixed.getSize();   // ← 계산
        return adminUserMapper.selectAdminUsers(fixed, offset);
    }

    /** 총 개수 */
    public long count(AdminUserSearchCond cond) {
        // count는 page/size 필요 없지만, null로 인한 NPE를 피하려 동일 normalize 사용
        AdminUserSearchCond fixed = normalize(cond);
        System.out.println("[ADMIN] count fixed=" + fixed);
        return adminUserMapper.countAdminUsers(fixed);
    }

    /** 상세 */
    public AdminUserDetail detail(Long userId) {
        AdminUserDetail detail = adminUserMapper.selectAdminUserDetail(userId);
        if (detail == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자");
        }
        return detail;
    }

    /** 상태/권한 변경: 쓰기이므로 읽기전용 해제 (둘 다 nullable) */
    @Transactional  // 메서드에서 readOnly=false로 override
    public void update(Long userId, AdminUpdateUserRequest req) {
        Role role = req.getRole();
        SuspensionStatus status = req.getStatus();

        // 변경 사항 없으면 종료
        if (role == null && status == null) return;

        // Enum을 그대로 넘겨서 TypeHandler가 DB enum로 매핑하도록
        adminUserMapper.adminUpdateUserStatus(userId, role, status);
    }

    /** (탈퇴 상태에서만) 실제 삭제: 쓰기 트랜잭션 */
    @Transactional
    public void hardDelete(Long userId) {
        int rows = adminUserMapper.deleteUserIfWithdrawn(userId);
        if (rows == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "탈퇴 상태에서만 삭제할 수 있습니다.");
        }
    }

    // ---------- 내부 유틸 ----------

    private static String trimLower(String s) {
        if (s == null) return null;
        String v = s.trim();
        return v.isEmpty() ? null : v.toLowerCase();
    }

    /** 검색 조건 정규화: null 기본값 채우기, 허용값 검증, 안전한 범위 강제 */
    private static List<String> normalizeList(List<String> src, Set<String> allowed) {
        if (src == null || src.isEmpty()) return null;
        List<String> out = new java.util.ArrayList<>();
        for (String it : src) {
            if (it == null) continue;
            for (String token : it.split(",")) { // CSV도 허용
                String v = token.trim().toLowerCase();
                if (!v.isEmpty() && allowed.contains(v)) out.add(v);
            }
        }
        return out.isEmpty() ? null : out;
    }

    private AdminUserSearchCond normalize(AdminUserSearchCond c) {
        if (c == null) {
            return AdminUserSearchCond.builder()
                    .page(DEFAULT_PAGE)
                    .size(DEFAULT_SIZE)
                    .build();
        }

        int page = (c.getPage() == null || c.getPage() < 0) ? DEFAULT_PAGE : c.getPage();
        int size = (c.getSize() == null) ? DEFAULT_SIZE : Math.min(Math.max(c.getSize(), 1), MAX_PAGE_SIZE);

        String qt = (c.getQueryType() == null || !ALLOWED_QUERY_TYPES.contains(c.getQueryType()))
                ? "nickname" : c.getQueryType();

        String q = (c.getQuery() == null) ? null : c.getQuery().trim();
        if (q != null && q.isEmpty()) q = null;

        Long userId = null;
        if ("userId".equals(qt) && q != null && q.matches("^\\d+$")) {
            try {
                userId = Long.valueOf(q);
            } catch (NumberFormatException ignore) {}
        }

        LocalDate from = c.getJoinedFrom();
        LocalDate to   = c.getJoinedTo();
        if (from != null && to != null && from.isAfter(to)) {
            LocalDate tmp = from; from = to; to = tmp;
        }

        // roles/statuses: 반복키/CSV 모두 정규화 + 소문자 + 허용값만
        List<String> roles    = normalizeList(c.getRoles(),    ALLOWED_ROLES);
        List<String> statuses = normalizeList(c.getStatuses(), ALLOWED_STATUSES);

        return AdminUserSearchCond.builder()
                .queryType(qt)
                .query(q)
                .userId(userId)
                .roles(roles)           // 소문자, 허용값만 남김(없으면 null)
                .statuses(statuses)     // 소문자, 허용값만 남김(없으면 null)
                .joinedFrom(from)
                .joinedTo(to)
                .page(page)
                .size(size)
                .build();
    }
}
