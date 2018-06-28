package com.anlijiu.example.domain.objects;

import com.google.auto.value.AutoValue;

import io.reactivex.annotations.Nullable;

/**
 * app icon display mode & widget display mode
 */
@AutoValue
public abstract class ItemInfo {

    public abstract long id();
    public abstract long prevId();
    public abstract long nextId();

    @Nullable
    public abstract String title();
    @Nullable
    public abstract String intent();
    public abstract int container();
    public abstract int screen();
    public abstract int itemType();
    public abstract int appWidgetId();
    public abstract int iconPackage();
    @Nullable
    public abstract String iconResource();
    @SuppressWarnings("mutable")
    @Nullable
    public abstract byte[] icon();
    @Nullable
    public abstract String appWidgetProvider();
    public abstract int modified();
    public abstract int restored();
    public abstract int profileId();
    public abstract int rank();
    public abstract int options();

    public static ItemInfo create(long id, long prevId, long nextId, String title, String intent, int container, int screen, int itemType, int appWidgetId, int iconPackage, String iconResource, byte[] icon, String appWidgetProvider, int modified, int restored, int profileId, int rank, int options) {
        return builder()
                .id(id)
                .prevId(prevId)
                .nextId(nextId)
                .title(title)
                .intent(intent)
                .container(container)
                .screen(screen)
                .itemType(itemType)
                .appWidgetId(appWidgetId)
                .iconPackage(iconPackage)
                .iconResource(iconResource)
                .icon(icon)
                .appWidgetProvider(appWidgetProvider)
                .modified(modified)
                .restored(restored)
                .profileId(profileId)
                .rank(rank)
                .options(options)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ItemInfo.Builder()
                .id(0)
                .prevId(0)
                .nextId(0)
                .container(0)
                .screen(0)
                .itemType(0)
                .appWidgetId(-1)
                .iconPackage(0)
                .modified(0)
                .restored(0)
                .profileId(0)
                .rank(0)
                .options(0);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder prevId(long prevId);

        public abstract Builder nextId(long nextId);

        public abstract Builder title(String title);

        public abstract Builder intent(String intent);

        public abstract Builder container(int container);

        public abstract Builder screen(int screen);

        public abstract Builder itemType(int itemType);

        public abstract Builder appWidgetId(int appWidgetId);

        public abstract Builder iconPackage(int iconPackage);

        public abstract Builder iconResource(String iconResource);

        public abstract Builder icon(byte[] icon);

        public abstract Builder appWidgetProvider(String appWidgetProvider);

        public abstract Builder modified(int modified);

        public abstract Builder restored(int restored);

        public abstract Builder profileId(int profileId);

        public abstract Builder rank(int rank);

        public abstract Builder options(int options);

        public abstract ItemInfo build();
    }
}