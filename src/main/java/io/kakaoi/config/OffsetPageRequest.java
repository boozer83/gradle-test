package io.kakaoi.config;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Objects;

/**
 * offset 기반의 Pageable
 */
public class OffsetPageRequest implements Pageable, Serializable {

    private static final long serialVersionUID = -25822477129613575L;

    private final int limit;
    private final long offset;
    private final Sort sort;

    /**
     * 새로운 {@link OffsetPageRequest} 객체를 생성하는 정적 메서드
     *
     * @param offset 0부터 시작하는 offset
     * @param limit  한번에 보여지는 최대 갯수
     * @param sort   정렬 기준
     * @return 생성한 {@link OffsetPageRequest}
     */
    public static OffsetPageRequest of(long offset, int limit, Sort sort) {
        return new OffsetPageRequest(offset, limit, sort);
    }

    /**
     * 새로운 {@link OffsetPageRequest} 객체를 생성하는 정적 메서드
     *
     * @param offset 0부터 시작하는 offset
     * @param limit  한번에 보여지는 최대 갯수
     * @param direction  정렬 순서
     * @param properties 정렬 기준 프로퍼티(컬럼)
     * @return 생성한 {@link OffsetPageRequest}
     */
    public static OffsetPageRequest of(long offset, int limit, Sort.Direction direction, String... properties) {
        return new OffsetPageRequest(offset, limit, direction, properties);
    }

    /**
     * 새로운 {@link OffsetPageRequest} 객체를 생성하는 정적 메서드. 정렬되지 않는 상태로 생성.
     *
     * @param offset 0부터 시작하는 offset
     * @param limit  한번에 보여지는 최대 갯수
     * @return 생성한 {@link OffsetPageRequest}
     */
    public static OffsetPageRequest of(long offset, int limit) {
        return new OffsetPageRequest(offset, limit);
    }

    /**
     * {@link OffsetPageRequest} 객체 생성자
     *
     * @param offset 0부터 시작하는 offset
     * @param limit  한번에 보여지는 최대 갯수
     * @param sort   정렬 기준
     */
    public OffsetPageRequest(long offset, int limit, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero!");
        }

        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one!");
        }
        this.limit = limit;
        this.offset = offset;
        if (sort == null) {
            this.sort = Sort.unsorted();
        }
        else {
            this.sort = sort;
        }
    }

    /**
     * {@link OffsetPageRequest} 객체 생성자
     *
     * @param offset 0부터 시작하는 offset
     * @param limit  한번에 보여지는 최대 갯수
     * @param direction  정렬 순서
     * @param properties 정렬 기준 프로퍼티(컬럼)
     */
    public OffsetPageRequest(long offset, int limit, Sort.Direction direction, String... properties) {
        this(offset, limit, Sort.by(direction, properties));
    }

    /**
     * {@link OffsetPageRequest} 객체 생성자. 정렬되지 않는 상태로 생성.
     *
     * @param offset 0부터 시작하는 offset
     * @param limit  한번에 보여지는 최대 갯수
     */
    public OffsetPageRequest(long offset, int limit) {
        this(offset, limit, Sort.unsorted());
    }

    @Override
    public int getPageNumber() {
        return Math.toIntExact(offset / limit);
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetPageRequest(getOffset() + getPageSize(), getPageSize(), getSort());
    }

    public OffsetPageRequest previous() {
        return hasPrevious() ? new OffsetPageRequest(getOffset() - getPageSize(), getPageSize(), getSort()) : this;
    }


    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new OffsetPageRequest(0, getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OffsetPageRequest that = (OffsetPageRequest) o;
        return limit == that.limit && offset == that.offset && sort.equals(that.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, offset, sort);
    }

    @Override
    public String toString() {
        return "OffsetPageRequest{" +
            "limit=" + limit +
            ", offset=" + offset +
            ", sort=" + sort +
            '}';
    }
}
