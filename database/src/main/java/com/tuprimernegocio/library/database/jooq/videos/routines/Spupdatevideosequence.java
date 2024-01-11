/*
 * This file is generated by jOOQ.
 */
package com.tuprimernegocio.library.database.jooq.videos.routines;


import com.tuprimernegocio.library.database.jooq.videos.Videos;

import javax.annotation.processing.Generated;

import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.Internal;


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
public class Spupdatevideosequence extends AbstractRoutine<java.lang.Void> {

    private static final long serialVersionUID = -473541930;

    /**
     * The parameter <code>videos.spUpdateVideoSequence.p_section_id</code>.
     */
    public static final Parameter<Integer> P_SECTION_ID = Internal.createParameter("p_section_id", org.jooq.impl.SQLDataType.INTEGER, false, false);

    /**
     * The parameter <code>videos.spUpdateVideoSequence.p_video_id</code>.
     */
    public static final Parameter<Integer> P_VIDEO_ID = Internal.createParameter("p_video_id", org.jooq.impl.SQLDataType.INTEGER, false, false);

    /**
     * The parameter <code>videos.spUpdateVideoSequence.p_new_sequence_number</code>.
     */
    public static final Parameter<Integer> P_NEW_SEQUENCE_NUMBER = Internal.createParameter("p_new_sequence_number", org.jooq.impl.SQLDataType.INTEGER, false, false);

    /**
     * The parameter <code>videos.spUpdateVideoSequence.p_admin_id</code>.
     */
    public static final Parameter<Integer> P_ADMIN_ID = Internal.createParameter("p_admin_id", org.jooq.impl.SQLDataType.INTEGER, false, false);

    /**
     * Create a new routine call instance
     */
    public Spupdatevideosequence() {
        super("spUpdateVideoSequence", Videos.VIDEOS);

        addInParameter(P_SECTION_ID);
        addInParameter(P_VIDEO_ID);
        addInParameter(P_NEW_SEQUENCE_NUMBER);
        addInParameter(P_ADMIN_ID);
    }

    /**
     * Set the <code>p_section_id</code> parameter IN value to the routine
     */
    public void setPSectionId(Integer value) {
        setValue(P_SECTION_ID, value);
    }

    /**
     * Set the <code>p_video_id</code> parameter IN value to the routine
     */
    public void setPVideoId(Integer value) {
        setValue(P_VIDEO_ID, value);
    }

    /**
     * Set the <code>p_new_sequence_number</code> parameter IN value to the routine
     */
    public void setPNewSequenceNumber(Integer value) {
        setValue(P_NEW_SEQUENCE_NUMBER, value);
    }

    /**
     * Set the <code>p_admin_id</code> parameter IN value to the routine
     */
    public void setPAdminId(Integer value) {
        setValue(P_ADMIN_ID, value);
    }
}