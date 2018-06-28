package com.anlijiu.example.data.entity;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Card Entity from business Operation push.
 */
@Entity
public class CardEntity {

    @Id
    long id;

    String coverImageUrl;
    String title;
    String targetUrl;

    public CardEntity() {
    }

    public CardEntity(long id, String coverImageUrl, String title, String targetUrl) {
        this.id = id;
        this.coverImageUrl = coverImageUrl;
        this.title = title;
        this.targetUrl = targetUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

}
