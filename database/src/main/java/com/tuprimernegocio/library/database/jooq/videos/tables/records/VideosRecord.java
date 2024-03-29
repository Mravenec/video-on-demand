/*
 * This file is generated by jOOQ.
 */
package com.tuprimernegocio.library.database.jooq.videos.tables.records;


import com.tuprimernegocio.library.database.jooq.videos.tables.Videos;

import java.sql.Timestamp;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;


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
public class VideosRecord extends UpdatableRecordImpl<VideosRecord> implements Record9<Integer, String, String, String, Integer, Byte, Integer, Integer, Timestamp> {

    private static final long serialVersionUID = -490170313;

    /**
     * Setter for <code>videos.videos.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>videos.videos.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>videos.videos.title</code>.
     */
    public void setTitle(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>videos.videos.title</code>.
     */
    public String getTitle() {
        return (String) get(1);
    }

    /**
     * Setter for <code>videos.videos.url</code>.
     */
    public void setUrl(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>videos.videos.url</code>.
     */
    public String getUrl() {
        return (String) get(2);
    }

    /**
     * Setter for <code>videos.videos.content</code>.
     */
    public void setContent(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>videos.videos.content</code>.
     */
    public String getContent() {
        return (String) get(3);
    }

    /**
     * Setter for <code>videos.videos.sequence_number</code>.
     */
    public void setSequenceNumber(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>videos.videos.sequence_number</code>.
     */
    public Integer getSequenceNumber() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>videos.videos.is_active</code>.
     */
    public void setIsActive(Byte value) {
        set(5, value);
    }

    /**
     * Getter for <code>videos.videos.is_active</code>.
     */
    public Byte getIsActive() {
        return (Byte) get(5);
    }

    /**
     * Setter for <code>videos.videos.section_id</code>.
     */
    public void setSectionId(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>videos.videos.section_id</code>.
     */
    public Integer getSectionId() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>videos.videos.added_by</code>.
     */
    public void setAddedBy(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>videos.videos.added_by</code>.
     */
    public Integer getAddedBy() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>videos.videos.created_at</code>.
     */
    public void setCreatedAt(Timestamp value) {
        set(8, value);
    }

    /**
     * Getter for <code>videos.videos.created_at</code>.
     */
    public Timestamp getCreatedAt() {
        return (Timestamp) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<Integer, String, String, String, Integer, Byte, Integer, Integer, Timestamp> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<Integer, String, String, String, Integer, Byte, Integer, Integer, Timestamp> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Videos.VIDEOS_.ID;
    }

    @Override
    public Field<String> field2() {
        return Videos.VIDEOS_.TITLE;
    }

    @Override
    public Field<String> field3() {
        return Videos.VIDEOS_.URL;
    }

    @Override
    public Field<String> field4() {
        return Videos.VIDEOS_.CONTENT;
    }

    @Override
    public Field<Integer> field5() {
        return Videos.VIDEOS_.SEQUENCE_NUMBER;
    }

    @Override
    public Field<Byte> field6() {
        return Videos.VIDEOS_.IS_ACTIVE;
    }

    @Override
    public Field<Integer> field7() {
        return Videos.VIDEOS_.SECTION_ID;
    }

    @Override
    public Field<Integer> field8() {
        return Videos.VIDEOS_.ADDED_BY;
    }

    @Override
    public Field<Timestamp> field9() {
        return Videos.VIDEOS_.CREATED_AT;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getTitle();
    }

    @Override
    public String component3() {
        return getUrl();
    }

    @Override
    public String component4() {
        return getContent();
    }

    @Override
    public Integer component5() {
        return getSequenceNumber();
    }

    @Override
    public Byte component6() {
        return getIsActive();
    }

    @Override
    public Integer component7() {
        return getSectionId();
    }

    @Override
    public Integer component8() {
        return getAddedBy();
    }

    @Override
    public Timestamp component9() {
        return getCreatedAt();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getTitle();
    }

    @Override
    public String value3() {
        return getUrl();
    }

    @Override
    public String value4() {
        return getContent();
    }

    @Override
    public Integer value5() {
        return getSequenceNumber();
    }

    @Override
    public Byte value6() {
        return getIsActive();
    }

    @Override
    public Integer value7() {
        return getSectionId();
    }

    @Override
    public Integer value8() {
        return getAddedBy();
    }

    @Override
    public Timestamp value9() {
        return getCreatedAt();
    }

    @Override
    public VideosRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public VideosRecord value2(String value) {
        setTitle(value);
        return this;
    }

    @Override
    public VideosRecord value3(String value) {
        setUrl(value);
        return this;
    }

    @Override
    public VideosRecord value4(String value) {
        setContent(value);
        return this;
    }

    @Override
    public VideosRecord value5(Integer value) {
        setSequenceNumber(value);
        return this;
    }

    @Override
    public VideosRecord value6(Byte value) {
        setIsActive(value);
        return this;
    }

    @Override
    public VideosRecord value7(Integer value) {
        setSectionId(value);
        return this;
    }

    @Override
    public VideosRecord value8(Integer value) {
        setAddedBy(value);
        return this;
    }

    @Override
    public VideosRecord value9(Timestamp value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public VideosRecord values(Integer value1, String value2, String value3, String value4, Integer value5, Byte value6, Integer value7, Integer value8, Timestamp value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached VideosRecord
     */
    public VideosRecord() {
        super(Videos.VIDEOS_);
    }

    /**
     * Create a detached, initialised VideosRecord
     */
    public VideosRecord(Integer id, String title, String url, String content, Integer sequenceNumber, Byte isActive, Integer sectionId, Integer addedBy, Timestamp createdAt) {
        super(Videos.VIDEOS_);

        set(0, id);
        set(1, title);
        set(2, url);
        set(3, content);
        set(4, sequenceNumber);
        set(5, isActive);
        set(6, sectionId);
        set(7, addedBy);
        set(8, createdAt);
    }
}
