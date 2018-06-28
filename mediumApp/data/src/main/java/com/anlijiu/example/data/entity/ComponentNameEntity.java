package com.anlijiu.example.data.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by anlijiu on 18-3-27.
 */


@Entity
public class ComponentNameEntity {
    @Id
    long id;
    String pkg;
    String cls;

    public ComponentNameEntity() {
    }

    public ComponentNameEntity(String pkg, String cls) {
        this.pkg = pkg;
        this.cls = cls;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    @Override
    public String toString() {
        return "ComponentNameEntity{" +
                "id=" + id +
                ", pkg='" + pkg + '\'' +
                ", cls='" + cls + '\'' +
                '}';
    }
}
