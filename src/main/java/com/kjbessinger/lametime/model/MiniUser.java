package com.kjbessinger.lametime.model;

import java.util.UUID;

/**
 * Created by kjbes on 4/28/2017.
 */
public class MiniUser {
    private String uuid;
    private String name;
    private String picture;

    public MiniUser(User user) {
        this.name = user.getName();
        this.picture = user.getPicture();
        this.uuid = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
