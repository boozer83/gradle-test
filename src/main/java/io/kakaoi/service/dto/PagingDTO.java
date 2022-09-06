package io.kakaoi.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(name = "Paging")
public class PagingDTO<T> {

    @Schema(description = "응답 데이터")
    private List<T> content;

    @Schema(description = "전체 갯수")
    private long size;

    @Schema(description = "요청 limit")
    private int limit;

    @Schema(description = "현재 페이지 index (0..n)")
    private long offset;

    public PagingDTO(Page<T> page) {
        this.content = page.getContent();
        this.size = page.getTotalElements();
        this.limit = page.getSize();
        this.offset = page.getPageable().getOffset();
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
