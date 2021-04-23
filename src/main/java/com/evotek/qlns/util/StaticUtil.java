/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.util;

import com.evotek.qlns.util.key.PropsKeys;

/**
 *
 * @author linhlh2
 */
public class StaticUtil {

    /**
     * Login policy*
     */
    public static boolean LOGIN_POLICY_REQUIRE_VERIFY_PRIVATE_LOGIN
            = GetterUtil.get(PropsUtil.get(
                            PropsKeys.LOGIN_POLICY_REQUIRE_VERIFY_PRIVATE_LOGIN, true), true);

    public static int LOGIN_POLICY_LIMIT_FAILURE_TIME
            = GetterUtil.get(PropsUtil.get(
                            PropsKeys.LOGIN_POLICY_LIMIT_FAILURE_TIME, true), 5);

    public static int LOGIN_POLICY_CAPTCHA_LENGTH
            = GetterUtil.get(PropsUtil.get(
                            PropsKeys.LOGIN_POLICY_CAPTCHA_LENGTH, true), 6);

    /**
     * Password policy*
     */
    public static boolean PASSWORD_POLICY_FORCE_LOWERCASE_LETTER
            = GetterUtil.get(PropsUtil.get(
                            PropsKeys.PASSWORD_POLICY_FORCE_LOWERCASE_LETTER, true), true);

    public static boolean PASSWORD_POLICY_FORCE_UPPERCASE_LETTER
            = GetterUtil.get(PropsUtil.get(
                            PropsKeys.PASSWORD_POLICY_FORCE_UPPERCASE_LETTER, true), true);

    public static boolean PASSWORD_POLICY_FORCE_DIGIT
            = GetterUtil.get(PropsUtil.get(
                            PropsKeys.PASSWORD_POLICY_FORCE_DIGIT, true), true);

    public static boolean PASSWORD_POLICY_FORCE_SYMBOL
            = GetterUtil.get(PropsUtil.get(
                            PropsKeys.PASSWORD_POLICY_FORCE_SYMBOL, true), true);

    public static String PASSWORD_POLICY_CHARSET_SYMBOL
            = PropsUtil.get(PropsKeys.PASSWORD_POLICY_CHARSET_SYMBOL, true);

    public static int PASSWORD_POLICY_MIN_LENGTH
            = GetterUtil.get(
                    PropsUtil.get(PropsKeys.PASSWORD_POLICY_MIN_LENGTH, true), 8);

    public static int PASSWORD_POLICY_MAX_LENGTH
            = GetterUtil.get(
                    PropsUtil.get(PropsKeys.PASSWORD_POLICY_MAX_LENGTH, true), 15);

    public static String PASSWORD_POLICY_DEFAULT_PASSWORD
            = GetterUtil.get(PropsUtil.get(PropsKeys.PASSWORD_POLICY_DEFAULT_PASSWORD, true),
                    GetterUtil.DEFAULT_PASSWORD);

    public static String SYSTEM_STORE_FILE_DIR
            = PropsUtil.get(PropsKeys.SYSTEM_STORE_FILE_DIR, true);
    //openshift
//          = System.getenv("OPENSHIFT_DATA_DIR");

    public static Long ATTACH_FILE_UPLOAD_ALLOW_MAX_SIZE
            = GetterUtil.getLong(PropsUtil.get(
                            PropsKeys.ATTACH_FILE_UPLOAD_ALLOW_MAX_SIZE, true));

    public static String TEMP_DIR = PropsUtil.get(PropsKeys.TEMP_DIR);

    public static String[] ATTACH_FILE_UPLOAD_ALLOW_EXTENSIONS
            = PropsUtil.getArray(
                    PropsKeys.ATTACH_FILE_UPLOAD_ALLOW_EXTENSION, true);

    public static String ATTACH_FILE_UPLOAD_ALLOW_EXTENSION
            = PropsUtil.get(
                    PropsKeys.ATTACH_FILE_UPLOAD_ALLOW_EXTENSION, true);

    public static String[] MENU_TYPE
            = PropsUtil.getArray(PropsKeys.MENU_TYPE, true);

    public static String[] MENU_RIGHT_TYPE
            = PropsUtil.getArray(PropsKeys.MENU_RIGHT_TYPE, true);

    public static String FILE_UPLOAD_DIR
            = PropsUtil.get(PropsKeys.FILE_UPLOAD_DIR, true);

    public static String DEFAULT_EMAIL_DOMAIN
            = PropsUtil.get(PropsKeys.DEFAULT_EMAIL_DOMAIN, true);

    public static final int NOTIFY_CONTRACT_EXPIRED_BEFORE_DAY
            = GetterUtil.get(
                    PropsUtil.get(PropsKeys.NOTIFY_CONTRACT_EXPIRED_BEFORE_DAY, true), 7);

    public static final int NOTIFY_BIRTHDAY_BEFORE_DAY
            = GetterUtil.get(
                    PropsUtil.get(PropsKeys.NOTIFY_BIRTHDAY_BEFORE_DAY, true), 2);

    //mail
    public static String MAIL_SESSION_MAIL_POP3_HOST
            = PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_POP3_HOST, false);

    public static String MAIL_SESSION_MAIL_POP3_PASSWORD
            = PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_POP3_PASSWORD, false);

    public static int MAIL_SESSION_MAIL_POP3_PORT
            = GetterUtil.get(
                    PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_POP3_PORT, false), 110);

    public static String MAIL_SESSION_MAIL_POP3_USER
            = PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_POP3_USER, false);

    public static String MAIL_SESSION_MAIL_SMTP_HOST
            = PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_SMTP_HOST, false);

    public static String MAIL_SESSION_MAIL_SMTP_PASSWORD
            = PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_SMTP_PASSWORD, false);

    public static int MAIL_SESSION_MAIL_SMTP_PORT
            = GetterUtil.get(
                    PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_SMTP_PORT, false), 25);

    public static String MAIL_SESSION_MAIL_SMTP_USER
            = PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_SMTP_USER, false);

    public static String MAIL_SESSION_MAIL_STORE_PROTOCOL
            = PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_STORE_PROTOCOL, false);

    public static String MAIL_SESSION_MAIL_TRANSPORT_PROTOCOL
            = PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_TRANSPORT_PROTOCOL, false);

    public static String MAIL_SESSION_MAIL_FROM_DEFAULT
            = PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_FROM_DEFAULT, false);

    public static String MAIL_SESSION_MAIL_SMTP_AUTH_TYPE
            = PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_SMTP_AUTH_TYPE, false);

    //verify
    public static int VERIFY_ACTIVE_USER_AVAIABLE_TIME
            = GetterUtil.get(PropsUtil.get(PropsKeys.VERIFY_ACTIVE_USER_AVAIABLE_TIME, false), 30);
    
    public static int VERIFY_RESET_PASSWORD_AVAIABLE_TIME
            = GetterUtil.get(PropsUtil.get(PropsKeys.VERIFY_RESET_PASSWORD_AVAIABLE_TIME, false), 1);
}
