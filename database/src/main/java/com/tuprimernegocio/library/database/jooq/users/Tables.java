/*
 * This file is generated by jOOQ.
 */
package com.tuprimernegocio.library.database.jooq.users;


import com.tuprimernegocio.library.database.jooq.users.tables.AccountConfiguration;
import com.tuprimernegocio.library.database.jooq.users.tables.Address;
import com.tuprimernegocio.library.database.jooq.users.tables.AdminRoles;
import com.tuprimernegocio.library.database.jooq.users.tables.PersonalData;
import com.tuprimernegocio.library.database.jooq.users.tables.Phones;
import com.tuprimernegocio.library.database.jooq.users.tables.UserProfilepicture;
import com.tuprimernegocio.library.database.jooq.users.tables.UserRoles;
import com.tuprimernegocio.library.database.jooq.users.tables.Users;

import javax.annotation.processing.Generated;


/**
 * Convenience access to all tables in users
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>users.account_configuration</code>.
     */
    public static final AccountConfiguration ACCOUNT_CONFIGURATION = AccountConfiguration.ACCOUNT_CONFIGURATION;

    /**
     * The table <code>users.address</code>.
     */
    public static final Address ADDRESS = Address.ADDRESS;

    /**
     * The table <code>users.admin_roles</code>.
     */
    public static final AdminRoles ADMIN_ROLES = AdminRoles.ADMIN_ROLES;

    /**
     * The table <code>users.personal_data</code>.
     */
    public static final PersonalData PERSONAL_DATA = PersonalData.PERSONAL_DATA;

    /**
     * The table <code>users.phones</code>.
     */
    public static final Phones PHONES = Phones.PHONES;

    /**
     * The table <code>users.users</code>.
     */
    public static final Users USERS_ = Users.USERS_;

    /**
     * The table <code>users.user_profilePicture</code>.
     */
    public static final UserProfilepicture USER_PROFILEPICTURE = UserProfilepicture.USER_PROFILEPICTURE;

    /**
     * The table <code>users.user_roles</code>.
     */
    public static final UserRoles USER_ROLES = UserRoles.USER_ROLES;
}