/*
 * This file is generated by jOOQ.
 */
package com.tuprimernegocio.library.database.jooq.manual_video_access;


import com.tuprimernegocio.library.database.jooq.manual_video_access.tables.VideoAccesses;
import com.tuprimernegocio.library.database.jooq.manual_video_access.tables.records.VideoAccessesRecord;

import javax.annotation.processing.Generated;

import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>manual_video_access</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<VideoAccessesRecord, Integer> IDENTITY_VIDEO_ACCESSES = Identities0.IDENTITY_VIDEO_ACCESSES;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<VideoAccessesRecord> KEY_VIDEO_ACCESSES_PRIMARY = UniqueKeys0.KEY_VIDEO_ACCESSES_PRIMARY;
    public static final UniqueKey<VideoAccessesRecord> KEY_VIDEO_ACCESSES_EMAIL = UniqueKeys0.KEY_VIDEO_ACCESSES_EMAIL;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<VideoAccessesRecord, Integer> IDENTITY_VIDEO_ACCESSES = Internal.createIdentity(VideoAccesses.VIDEO_ACCESSES, VideoAccesses.VIDEO_ACCESSES.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<VideoAccessesRecord> KEY_VIDEO_ACCESSES_PRIMARY = Internal.createUniqueKey(VideoAccesses.VIDEO_ACCESSES, "KEY_video_accesses_PRIMARY", VideoAccesses.VIDEO_ACCESSES.ID);
        public static final UniqueKey<VideoAccessesRecord> KEY_VIDEO_ACCESSES_EMAIL = Internal.createUniqueKey(VideoAccesses.VIDEO_ACCESSES, "KEY_video_accesses_email", VideoAccesses.VIDEO_ACCESSES.EMAIL);
    }
}