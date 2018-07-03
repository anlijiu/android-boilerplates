package com.anlijiu.example.domain.objects;

import com.google.auto.value.AutoValue;

import io.reactivex.annotations.Nullable;

@AutoValue
public abstract class Repo {
    public abstract String name();

    public static Repo create(String name) {
        return builder()
                .name(name)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_Repo.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder name(String name);

        public abstract Repo build();
    }
}
