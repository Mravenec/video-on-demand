/*
 * This file is generated by jOOQ.
 */
package com.tuprimernegocio.library.database.jooq.email_config.tables.pojos;


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
public class EmailConfig implements Serializable {

    private static final long serialVersionUID = 1817993764;

    private Integer id;
    private byte[]  host;
    private byte[]  port;
    private byte[]  username;
    private byte[]  password;
    private Byte    smtpAuth;
    private Byte    starttlsEnabled;

    public EmailConfig() {}

    public EmailConfig(EmailConfig value) {
        this.id = value.id;
        this.host = value.host;
        this.port = value.port;
        this.username = value.username;
        this.password = value.password;
        this.smtpAuth = value.smtpAuth;
        this.starttlsEnabled = value.starttlsEnabled;
    }

    public EmailConfig(
        Integer id,
        byte[]  host,
        byte[]  port,
        byte[]  username,
        byte[]  password,
        Byte    smtpAuth,
        Byte    starttlsEnabled
    ) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.smtpAuth = smtpAuth;
        this.starttlsEnabled = starttlsEnabled;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getHost() {
        return this.host;
    }

    public void setHost(byte... host) {
        this.host = host;
    }

    public byte[] getPort() {
        return this.port;
    }

    public void setPort(byte... port) {
        this.port = port;
    }

    public byte[] getUsername() {
        return this.username;
    }

    public void setUsername(byte... username) {
        this.username = username;
    }

    public byte[] getPassword() {
        return this.password;
    }

    public void setPassword(byte... password) {
        this.password = password;
    }

    public Byte getSmtpAuth() {
        return this.smtpAuth;
    }

    public void setSmtpAuth(Byte smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public Byte getStarttlsEnabled() {
        return this.starttlsEnabled;
    }

    public void setStarttlsEnabled(Byte starttlsEnabled) {
        this.starttlsEnabled = starttlsEnabled;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("EmailConfig (");

        sb.append(id);
        sb.append(", ").append("[binary...]");
        sb.append(", ").append("[binary...]");
        sb.append(", ").append("[binary...]");
        sb.append(", ").append("[binary...]");
        sb.append(", ").append(smtpAuth);
        sb.append(", ").append(starttlsEnabled);

        sb.append(")");
        return sb.toString();
    }
}
