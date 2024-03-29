/*
 * This file is generated by jOOQ.
 */
package com.tuprimernegocio.library.database.jooq.users.tables;


import com.tuprimernegocio.library.database.jooq.users.Indexes;
import com.tuprimernegocio.library.database.jooq.users.Keys;
import com.tuprimernegocio.library.database.jooq.users.Users;
import com.tuprimernegocio.library.database.jooq.users.tables.records.PersonalDataRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


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
public class PersonalData extends TableImpl<PersonalDataRecord> {

    private static final long serialVersionUID = 1876325576;

    /**
     * The reference instance of <code>users.personal_data</code>
     */
    public static final PersonalData PERSONAL_DATA = new PersonalData();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PersonalDataRecord> getRecordType() {
        return PersonalDataRecord.class;
    }

    /**
     * The column <code>users.personal_data.user_id</code>.
     */
    public final TableField<PersonalDataRecord, Integer> USER_ID = createField(DSL.name("user_id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>users.personal_data.full_name</code>.
     */
    public final TableField<PersonalDataRecord, String> FULL_NAME = createField(DSL.name("full_name"), org.jooq.impl.SQLDataType.VARCHAR(255).defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * Create a <code>users.personal_data</code> table reference
     */
    public PersonalData() {
        this(DSL.name("personal_data"), null);
    }

    /**
     * Create an aliased <code>users.personal_data</code> table reference
     */
    public PersonalData(String alias) {
        this(DSL.name(alias), PERSONAL_DATA);
    }

    /**
     * Create an aliased <code>users.personal_data</code> table reference
     */
    public PersonalData(Name alias) {
        this(alias, PERSONAL_DATA);
    }

    private PersonalData(Name alias, Table<PersonalDataRecord> aliased) {
        this(alias, aliased, null);
    }

    private PersonalData(Name alias, Table<PersonalDataRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> PersonalData(Table<O> child, ForeignKey<O, PersonalDataRecord> key) {
        super(child, key, PERSONAL_DATA);
    }

    @Override
    public Schema getSchema() {
        return Users.USERS;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PERSONAL_DATA_PRIMARY);
    }

    @Override
    public UniqueKey<PersonalDataRecord> getPrimaryKey() {
        return Keys.KEY_PERSONAL_DATA_PRIMARY;
    }

    @Override
    public List<UniqueKey<PersonalDataRecord>> getKeys() {
        return Arrays.<UniqueKey<PersonalDataRecord>>asList(Keys.KEY_PERSONAL_DATA_PRIMARY);
    }

    @Override
    public List<ForeignKey<PersonalDataRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PersonalDataRecord, ?>>asList(Keys.PERSONAL_DATA_IBFK_1);
    }

    public com.tuprimernegocio.library.database.jooq.users.tables.Users users() {
        return new com.tuprimernegocio.library.database.jooq.users.tables.Users(this, Keys.PERSONAL_DATA_IBFK_1);
    }

    @Override
    public PersonalData as(String alias) {
        return new PersonalData(DSL.name(alias), this);
    }

    @Override
    public PersonalData as(Name alias) {
        return new PersonalData(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PersonalData rename(String name) {
        return new PersonalData(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PersonalData rename(Name name) {
        return new PersonalData(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
