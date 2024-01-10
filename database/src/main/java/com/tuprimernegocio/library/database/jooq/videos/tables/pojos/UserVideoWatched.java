/*
 * This file is generated by jOOQ.
 */
package com.tuprimernegocio.library.database.jooq.videos.tables.pojos;


import java.io.Serializable;

import javax.annotation.processing.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UserVideoWatched implements Serializable {

    private static final long serialVersionUID = 1746305060;

    private Integer userId;
    private Integer videoId;
    private Byte    watched;

    public UserVideoWatched() {}

    public UserVideoWatched(UserVideoWatched value) {
        this.userId = value.userId;
        this.videoId = value.videoId;
        this.watched = value.watched;
    }

    public UserVideoWatched(
        Integer userId,
        Integer videoId,
        Byte    watched
    ) {
        this.userId = userId;
        this.videoId = videoId;
        this.watched = watched;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVideoId() {
        return this.videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Byte getWatched() {
        return this.watched;
    }

    public void setWatched(Byte watched) {
        this.watched = watched;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("UserVideoWatched (");

        sb.append(userId);
        sb.append(", ").append(videoId);
        sb.append(", ").append(watched);

        sb.append(")");
        return sb.toString();
    }
}
