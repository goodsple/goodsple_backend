package com.goodsple.features.admin.report.service.impl;

import com.goodsple.features.admin.report.dto.*;
import com.goodsple.features.admin.report.mapper.AdminReportMapper;
import com.goodsple.features.admin.report.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*; // ★ Objects, Set 등

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportServiceImpl implements AdminReportService {

    private final AdminReportMapper mapper;

    @Override
    public AdminReportList getReportList(AdminReportSearchCond cond) {
        int page = cond.getPage() == null ? 0 : cond.getPage();
        int size = cond.getSize() == null ? 20 : cond.getSize();
        cond.setPage(page);
        cond.setSize(size);

        // 날짜 역전 보정
        if (cond.getCreatedFrom() != null && cond.getCreatedTo() != null
                && cond.getCreatedFrom().isAfter(cond.getCreatedTo())) {
            LocalDate tmp = cond.getCreatedFrom();
            cond.setCreatedFrom(cond.getCreatedTo());
            cond.setCreatedTo(tmp);
        }

        // 타입/상태 소문자 정규화 (+ MESSAGE -> chat_message 보정)
        if (cond.getTargetTypes() != null) {
            var expanded = new LinkedHashSet<String>(); // 중복 제거 + 순서 보존
            for (String s : cond.getTargetTypes()) {
                if (s == null || s.isBlank()) continue;
                String v = s.trim().toLowerCase();
                switch (v) {
                    case "message":
                        // 메시지 필터 선택 시, chat(메시지 신고) + user(프로필/회원 신고) 모두 포함
                        expanded.add("chat");
                        expanded.add("user");
                        break;
                    case "chat":
                    case "chat_message": // 혹시 옛 값이 올 수도 있으니 안전망
                        expanded.add("chat");
                        expanded.add("user");
                        break;
                    default:
                        expanded.add(v);
                }
            }
            cond.setTargetTypes(new java.util.ArrayList<>(expanded));
        }

        if (cond.getStatuses() != null) {
            cond.setStatuses(
                    cond.getStatuses().stream()
                            .filter(s -> s != null && !s.isBlank())
                            .map(String::toLowerCase)
                            .toList()
            );
        }

        // ★ actions 정규화 (프론트에서 오는 조치 필터)
        if (cond.getActions() != null) {
            cond.setActions(
                    cond.getActions().stream()
                            .filter(Objects::nonNull)
                            .map(String::trim)
                            .map(String::toLowerCase)
                            .filter(s -> Set.of("warning","rejected","suspend_3d","permanent_ban").contains(s))
                            .distinct()
                            .toList()
            );
        }

        // ★ actions가 비어있을 때만 keyword로 조치 유추 (actionSearch)
        cond.setActionSearch(null);
        boolean hasActions = cond.getActions() != null && !cond.getActions().isEmpty();
        if (!hasActions && cond.getKeyword() != null) {
            String k = cond.getKeyword().trim().toLowerCase();
            if (!k.isBlank()) {
                if (k.contains("warning") || k.contains("경고")) {
                    cond.setActionSearch("warning");
                } else if (k.contains("dismiss") || k.contains("기각") || k.contains("rejected")) {
                    cond.setActionSearch("rejected");
                } else if (k.contains("3일") || k.contains("3d")) {
                    cond.setActionSearch("suspend_3d");
                } else if (k.contains("영구") || k.contains("perm") || k.contains("permanent")) {
                    cond.setActionSearch("permanent_ban");
                }
            }
        }

        // 키워드가 숫자면 ID 검색에 사용(신고ID/타겟ID/신고자/피신고자)
        cond.setKeywordAsLong(null);
        if (cond.getKeyword() != null) {
            String k = cond.getKeyword().trim();
            if (!k.isBlank()) {
                try { cond.setKeywordAsLong(Long.parseLong(k)); }
                catch (NumberFormatException ignore) { /* not a number */ }
            }
        }

        long total = mapper.countReportList(cond);
        List<AdminReportListItem> items = total > 0
                ? mapper.selectReportList(cond)
                : Collections.emptyList();

        return AdminReportList.builder()
                .items(items)
                .total(total)
                .page(page)
                .size(size)
                .build();
    }

    @Override
    public AdminReportDetail getReportDetail(Long reportId) {
        AdminReportDetail detail = mapper.selectReportDetail(reportId);
        if (detail == null) {
            throw new NoSuchElementException("Report not found: " + reportId);
        }
        detail.setReasons(mapper.selectReasonsByReportId(reportId));
        return detail;
    }

    @Override
    @Transactional
    public void updateStatus(Long reportId, AdminReportStatusUpdate request) {
        // (선택) 상태 유효성 간단 체크
        if (request.getStatus() == null ||
                !Set.of("pending","processing","resolved","rejected")
                        .contains(request.getStatus().toLowerCase())) {
            throw new IllegalArgumentException("Invalid status: " + request.getStatus());
        }

        String status = request.getStatus().toLowerCase(); // 정규화
        String actionTaken = request.getActionTaken();      // 대문자여도 OK (XML에서 lower 처리)

        int updated = mapper.updateReportStatus(reportId, status, actionTaken);
        if (updated == 0) {
            throw new NoSuchElementException("Report not found: " + reportId);
        }
    }
}
