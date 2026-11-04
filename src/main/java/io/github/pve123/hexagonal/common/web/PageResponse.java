package io.github.pve123.hexagonal.common.web;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 목록 응답의 공통 모양입니다. Page를 그대로 내보내면 스프링 내부 구조가 응답에 섞여서 따로 정했습니다.
 * page는 1부터 셉니다.
 */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber() + 1, page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
