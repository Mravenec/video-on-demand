/*
 * This file is generated by jOOQ.
 */
package com.tuprimernegocio.library.database.jooq.manual_video_access.tables.records;


import com.tuprimernegocio.library.database.jooq.manual_video_access.tables.VideoAccesses;

import java.sql.Timestamp;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
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
public class VideoAccessesRecord extends UpdatableRecordImpl<VideoAccessesRecord> implements Record6<Integer, String, Byte, Integer, Timestamp, Timestamp> {

    private static final long serialVersionUID = -170678036;

    /**
     * Setter for <code>manual_video_access.video_accesses.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>manual_video_access.video_accesses.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>manual_video_access.video_accesses.email</code>.
     */
    public void setEmail(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>manual_video_access.video_accesses.email</code>.
     */
    public String getEmail() {
        return (String) get(1);
    }

    /**
     * Setter for <code>manual_video_access.video_accesses.is_active</code>.
     */
    public void setIsActive(Byte value) {
        set(2, value);
    }

    /**
     * Getter for <code>manual_video_access.video_accesses.is_active</code>.
     */
    public Byte getIsActive() {
        return (Byte) get(2);
    }

    /**
     * Setter for <code>manual_video_access.video_accesses.admin_id</code>.
     */
    public void setAdminId(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>manual_video_access.video_accesses.admin_id</code>.
     */
    public Integer getAdminId() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>manual_video_access.video_accesses.created_at</code>.
     */
    public void setCreatedAt(Timestamp value) {
        set(4, value);
    }

    /**
     * Getter for <code>manual_video_access.video_accesses.created_at</code>.
     */
    public Timestamp getCreatedAt() {
        return (Timestamp) get(4);
    }

    /**
     * Setter for <code>manual_video_access.video_accesses.updated_at</code>.
     */
    public void setUpdatedAt(Timestamp value) {
        set(5, value);
    }

    /**
     * Getter for <code>manual_video_access.video_accesses.updated_at</code>.
     */
    public Timestamp getUpdatedAt() {
        return (Timestamp) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<Integer, String, Byte, Integer, Timestamp, Timestamp> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<Integer, String, Byte, Integer, Timestamp, Timestamp> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return VideoAccesses.VIDEO_ACCESSES.ID;
    }

    @Override
    public Field<String> field2() {
        return VideoAccesses.VIDEO_ACCESSES.EMAIL;
    }

    @Override
    public Field<Byte> field3() {
        return VideoAccesses.VIDEO_ACCESSES.IS_ACTIVE;
    }

    @Override
    public Field<Integer> field4() {
        return VideoAccesses.VIDEO_ACCESSES.ADMIN_ID;
    }

    @Override
    public Field<Timestamp> field5() {
        return VideoAccesses.VIDEO_ACCESSES.CREATED_AT;
    }

    @Override
    public Field<Timestamp> field6() {
        return VideoAccesses.VIDEO_ACCESSES.UPDATED_AT;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getEmail();
    }

    @Override
    public Byte component3() {
        return getIsActive();
    }

    @Override
    public Integer component4() {
        return getAdminId();
    }

    @Override
    public Timestamp component5() {
        return getCreatedAt();
    }

    @Override
    public Timestamp component6() {
        return getUpdatedAt();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getEmail();
    }

    @Override
    public Byte value3() {
        return getIsActive();
    }

    @Override
    public Integer value4() {
        return getAdminId();
    }

    @Override
    public Timestamp value5() {
        return getCreatedAt();
    }

    @Override
    public Timestamp value6() {
        return getUpdatedAt();
    }

    @Override
    public VideoAccessesRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public VideoAccessesRecord value2(String value) {
        setEmail(value);
        return this;
    }

    @Override
    public VideoAccessesRecord value3(Byte value) {
        setIsActive(value);
        return this;
    }

    @Override
    public VideoAccessesRecord value4(Integer value) {
        setAdminId(value);
        return this;
    }

    @Override
    public VideoAccessesRecord value5(Timestamp value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public VideoAccessesRecord value6(Timestamp value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    public VideoAccessesRecord values(Integer value1, String value2, Byte value3, Integer value4, Timestamp value5, Timestamp value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached VideoAccessesRecord
     */
    public VideoAccessesRecord() {
        super(VideoAccesses.VIDEO_ACCESSES);
    }

    /**
     * Create a detached, initialised VideoAccessesRecord
     */
    public VideoAccessesRecord(Integer id, String email, Byte isActive, Integer adminId, Timestamp createdAt, Timestamp updatedAt) {
        super(VideoAccesses.VIDEO_ACCESSES);

        set(0, id);
        set(1, email);
        set(2, isActive);
        set(3, adminId);
        set(4, createdAt);
        set(5, updatedAt);
    }
}
