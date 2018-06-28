package com.anlijiu.example.domain.objects;

import com.google.auto.value.AutoValue;

import io.reactivex.annotations.Nullable;

/**
 * Class that represents a Card in the domain layer.
 */
@AutoValue
public abstract class Card {

    public abstract long id();

    @Nullable
    public abstract String coverImageUrl();

    @Nullable
    public abstract String title();

    @Nullable
    public abstract String targetUrl();

    public static Card create(long id, String coverImageUrl, String title, String targetUrl) {
        return builder()
                .id(id)
                .coverImageUrl(coverImageUrl)
                .title(title)
                .targetUrl(targetUrl)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_Card.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder coverImageUrl(String coverImageUrl);

        public abstract Builder title(String title);

        public abstract Builder targetUrl(String targetUrl);

        public abstract Card build();
    }
}
