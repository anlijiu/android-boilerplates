package com.anlijiu.example.domain.objects;


import com.google.auto.value.AutoValue;

import io.reactivex.annotations.Nullable;

@AutoValue
public abstract class SimpleItem {
    public abstract long id();
    @Nullable
    public abstract String name();

    public static SimpleItem create(long id, String name) {
        return builder()
                .id(id)
                .name(name)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_SimpleItem.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract SimpleItem build();
    }
}