package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public abstract class IamAbstractPage<T> {

    private final long offset;

    private final long size;

    private final long total;

    public IamAbstractPage(
        Long offset,
        Long size,
        Long total
    ) {
        this.offset = offset == null ? 0 : offset;
        this.size = size == null ? 0 : size;
        this.total = total == null ? 0 : total;
    }

    public long getOffset() {
        return offset;
    }

    public long getSize() {
        return size;
    }

    public long getTotal() {
        return total;
    }

    @JsonIgnore
    public abstract List<T> getElements();

}
