/*
 * This file is generated by jOOQ.
 */
package com.tuprimernegocio.library.database.jooq.videos.tables.pojos;


import java.io.Serializable;
import java.sql.Timestamp;

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
public class Videos implements Serializable {

    private static final long serialVersionUID = -1796245557;

    private Integer   id;
    private String    title;
    private String    url;
    private String    content;
    private Integer   sequenceNumber;
    private Byte      isActive;
    private Integer   sectionId;
    private Integer   addedBy;
    private Timestamp createdAt;

    public Videos() {}

    public Videos(Videos value) {
        this.id = value.id;
        this.title = value.title;
        this.url = value.url;
        this.content = value.content;
        this.sequenceNumber = value.sequenceNumber;
        this.isActive = value.isActive;
        this.sectionId = value.sectionId;
        this.addedBy = value.addedBy;
        this.createdAt = value.createdAt;
    }

    public Videos(
        Integer   id,
        String    title,
        String    url,
        String    content,
        Integer   sequenceNumber,
        Byte      isActive,
        Integer   sectionId,
        Integer   addedBy,
        Timestamp createdAt
    ) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.content = content;
        this.sequenceNumber = sequenceNumber;
        this.isActive = isActive;
        this.sectionId = sectionId;
        this.addedBy = addedBy;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Byte getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }

    public Integer getSectionId() {
        return this.sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getAddedBy() {
        return this.addedBy;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Videos (");

        sb.append(id);
        sb.append(", ").append(title);
        sb.append(", ").append(url);
        sb.append(", ").append(content);
        sb.append(", ").append(sequenceNumber);
        sb.append(", ").append(isActive);
        sb.append(", ").append(sectionId);
        sb.append(", ").append(addedBy);
        sb.append(", ").append(createdAt);

        sb.append(")");
        return sb.toString();
    }
}
