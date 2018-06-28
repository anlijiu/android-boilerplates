package com.anlijiu.example.domain.objects;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ItemSortData {
    public static final long INVALID_ID = -1;
    public static final long DEFAULT_ID = 0;

    public abstract long id();

    public abstract long prevId();

    public abstract long nextId();

    public static ItemSortData create(long id, long prevId, long nextId) {
        return builder()
                .id(id)
                .prevId(prevId)
                .nextId(nextId)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ItemSortData.Builder()
                .id(0)
                .prevId(0)
                .nextId(0);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder prevId(long prevId);

        public abstract Builder nextId(long nextId);

        public abstract ItemSortData build();
    }
}
