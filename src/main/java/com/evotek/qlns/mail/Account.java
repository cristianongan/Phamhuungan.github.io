/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.mail;

import java.io.Serializable;

import com.evotek.qlns.util.Validator;

/**
 *
 * @author linhlh2
 */
public abstract class Account implements Serializable {

    public static final String PROTOCOL_IMAP = "imap";

    public static final String PROTOCOL_IMAPS = "imaps";

    public static final String PROTOCOL_POP = "pop3";

    public static final String PROTOCOL_POPS = "pop3s";

    public static final String PROTOCOL_SMTP = "smtp";

    public static final String PROTOCOL_SMTPS = "smtps";

    public static final int PORT_IMAP = 143;

    public static final int PORT_IMAPS = 993;

    public static final int PORT_POP = 110;

    public static final int PORT_POPS = 995;

    public static final int PORT_SMTP = 25;

    public static final int PORT_SMTPS = 465;

    public static Account getInstance(String protocol) {
        return getInstance(protocol, 0);
    }

    public static Account getInstance(String protocol, int port) {
        Account account = null;

        if (protocol.startsWith(PROTOCOL_IMAP)) {
            boolean secure = false;
            int defaultPort = PORT_IMAP;

            if (protocol.endsWith("s")) {
                secure = true;
                defaultPort = PORT_IMAPS;
            }

            if (port <= 0) {
                port = defaultPort;
            }

            account = new IMAPAccount(protocol, secure, port);
        } else if (protocol.startsWith(PROTOCOL_POP)) {
            boolean secure = false;
            int defaultPort = PORT_POP;

            if (protocol.endsWith("s")) {
                secure = true;
                defaultPort = PORT_POPS;
            }

            if (port <= 0) {
                port = defaultPort;
            }

            account = new POPAccount(protocol, secure, port);
        } else {
            boolean secure = false;
            int defaultPort = PORT_SMTP;

            if (protocol.endsWith("s")) {
                secure = true;
                defaultPort = PORT_SMTPS;
            }

            if (port <= 0) {
                port = defaultPort;
            }

            account = new SMTPAccount(protocol, secure, port);
        }

        return account;
    }

    public String getHost() {
        return this._host;
    }

    public String getPassword() {
        return this._password;
    }

    public int getPort() {
        return this._port;
    }

    public String getProtocol() {
        return this._protocol;
    }

    public String getUser() {
        return this._user;
    }

    public boolean isRequiresAuthentication() {
        if (Validator.isNotNull(this._user)
                && Validator.isNotNull(this._password)) {

            return true;
        } else {
            return false;
        }
    }

    public boolean isSecure() {
        return this._secure;
    }

    public void setHost(String host) {
        this._host = host;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public void setPort(int port) {
        this._port = port;
    }

    public void setUser(String user) {
        this._user = user;
    }

    protected Account(String protocol, boolean secure, int port) {
        this._protocol = protocol;
        this._secure = secure;
        this._port = port;
    }

    private String _host;
    private String _password;
    private int _port;
    private String _protocol;
    private boolean _secure;
    private String _user;

}
