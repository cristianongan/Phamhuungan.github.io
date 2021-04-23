package com.evotek.qlns.mail;

/**
 * <a href="SMTPAccount.java.html"><b><i>View Source</i></b></a>
 *
 * @author Thiago Moreira
 *
 */
public class SMTPAccount extends Account {

    protected SMTPAccount(String protocol, boolean secure, int port) {
        super(protocol, secure, port);
    }

}
