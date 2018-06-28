package com.anlijiu.example.data.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * app item(shortcut or widget)
 */
@Entity
public class ItemInfoEntity {

    @Id
    long id;
    long prevId;
    long nextId;
    String title;
    String intent;
    int container;
    int itemType;
    int appWidgetId = -1;
    int iconPackage;
    String iconResource;
    byte[] icon;
    String appWidgetProvider;
    int modified;
    int restored;
    int profileId;
    int rank;
    int options;
    int screen;

    public ItemInfoEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPrevId() {
        return prevId;
    }

    public void setPrevId(long prevId) {
        this.prevId = prevId;
    }

    public long getNextId() {
        return nextId;
    }

    public void setNextId(long nextId) {
        this.nextId = nextId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public int getContainer() {
        return container;
    }

    public void setContainer(int container) {
        this.container = container;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getAppWidgetId() {
        return appWidgetId;
    }

    public void setAppWidgetId(int appWidgetId) {
        this.appWidgetId = appWidgetId;
    }

    public int getIconPackage() {
        return iconPackage;
    }

    public void setIconPackage(int iconPackage) {
        this.iconPackage = iconPackage;
    }

    public String getIconResource() {
        return iconResource;
    }

    public void setIconResource(String iconResource) {
        this.iconResource = iconResource;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getAppWidgetProvider() {
        return appWidgetProvider;
    }

    public void setAppWidgetProvider(String appWidgetProvider) {
        this.appWidgetProvider = appWidgetProvider;
    }

    public int getModified() {
        return modified;
    }

    public void setModified(int modified) {
        this.modified = modified;
    }

    public int getRestored() {
        return restored;
    }

    public void setRestored(int restored) {
        this.restored = restored;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getOptions() {
        return options;
    }

    public void setOptions(int options) {
        this.options = options;
    }

    public int getScreen() {
        return screen;
    }

    public void setScreen(int screen) {
        this.screen = screen;
    }

}