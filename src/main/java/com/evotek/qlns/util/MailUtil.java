/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.util;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.evotek.qlns.mail.Account;
import com.evotek.qlns.util.key.Constants;

/**
 *
 * @author linhlh2
 */
public class MailUtil {

    public static final String[] LINK_REGEXP = {
        "([^]_a-z0-9-=\"'/])"
        + "((https?|ftp|gopher|news|telnet)://|www\\.)"
        + "([^ \\r\\n\\(\\)\\*\\^\\$!`\"'\\|\\[\\]\\{\\};<>\\.]*)"
        + "((\\.[^ \\r\\n\\(\\)\\*\\^\\$!`\"'\\|\\[\\]\\{\\};<>\\.]+)*)",
        "<a href=\"www\\."
    };

    public static String[] LINK_REPLACEMENT = {
        "$1<a href=\"$2$4$5\" target=\"_blank\">$2$4$5</a>",
        "<a href=\"http://www."
    };

    public static Pattern[] LINK_PATTERN = {
        Pattern.compile(LINK_REGEXP[0]),
        Pattern.compile(LINK_REGEXP[1])
    };

    public static String addQuote(String text, boolean htmlFormat) {
        if (text == null) {
            return StringPool.BLANK;
        }

        if (htmlFormat) {
            StringBuilder sb = new StringBuilder();

            sb.append("<blockquote style=\"border-left: #C4C4C4 2px solid; ");
            sb.append("margin-left: 5px; padding-left: 5px;\">");
            sb.append("<div>");
            sb.append(text);
            sb.append("</div>");

            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder(text.length() + 10);

            sb.append(">");

            int x = 0;
            int y = 0;

            while (true) {
                y = text.indexOf(10, x);

                if (y != -1) {
                    sb.append(text.substring(x, y + 1));
                    sb.append(">");

                    x = y + 1;
                } else {
                    sb.append(text.substring(x, text.length()));

                    break;
                }
            }

            return sb.toString();
        }
    }

    public static String formatPlainText(String text) {
        if (text == null) {
            return null;
        }

        text = StringUtil.wrap(text);
        text = HtmlUtil.escape(text);

        Matcher matcher = LINK_PATTERN[0].matcher(text);
        text = matcher.replaceAll(LINK_REPLACEMENT[0]);

        matcher = LINK_PATTERN[1].matcher(text);
        text = matcher.replaceAll(LINK_REPLACEMENT[1]);

        return text;
    }

    public static Session currentSession() {

        if (_session != null) {
            return _session;
        }
        try {
            String pop3Host = StaticUtil.MAIL_SESSION_MAIL_POP3_HOST;
            String pop3Password = StaticUtil.MAIL_SESSION_MAIL_POP3_PASSWORD;
            int pop3Port = StaticUtil.MAIL_SESSION_MAIL_POP3_PORT;
            String pop3User = StaticUtil.MAIL_SESSION_MAIL_POP3_USER;
            String smtpHost = StaticUtil.MAIL_SESSION_MAIL_SMTP_HOST;
            final String smtpPassword = StaticUtil.MAIL_SESSION_MAIL_SMTP_PASSWORD;
            int smtpPort = StaticUtil.MAIL_SESSION_MAIL_SMTP_PORT;
            final String smtpUser = StaticUtil.MAIL_SESSION_MAIL_SMTP_USER;
            String storeProtocol = StaticUtil.MAIL_SESSION_MAIL_STORE_PROTOCOL;
            String transportProtocol = StaticUtil.MAIL_SESSION_MAIL_TRANSPORT_PROTOCOL;
            String smtpAuthType = StaticUtil.MAIL_SESSION_MAIL_SMTP_AUTH_TYPE;

            Properties properties = new Properties();

            // Incoming
            if (!storeProtocol.equals(Account.PROTOCOL_POPS)) {
                storeProtocol = Account.PROTOCOL_POP;
            }

            properties.setProperty("mail.store.protocol", storeProtocol);

            String storePrefix = "mail." + storeProtocol + ".";

            properties.setProperty(storePrefix + "host", pop3Host);
            properties.setProperty(storePrefix + "password", pop3Password);
            properties.setProperty(storePrefix + "port", String.valueOf(pop3Port));
            properties.setProperty(storePrefix + "user", pop3User);

            // Outgoing
            if (!transportProtocol.equals(Account.PROTOCOL_SMTPS)) {
                transportProtocol = Account.PROTOCOL_SMTP;
            }

            properties.setProperty("mail.transport.protocol", transportProtocol);

            String transportPrefix = "mail." + transportProtocol + ".";

            boolean smtpAuth = false;

            if (Validator.isNotNull(smtpPassword)
                    || Validator.isNotNull(smtpUser)) {

                smtpAuth = true;
            }

            properties.setProperty(
                    transportPrefix + "auth", String.valueOf(smtpAuth));
            properties.setProperty(transportPrefix + "host", smtpHost);
            properties.setProperty(transportPrefix + "password", smtpPassword);
            properties.setProperty(
                    transportPrefix + "port", String.valueOf(smtpPort));
            properties.setProperty(transportPrefix + "user", smtpUser);
            
            if(smtpAuth){
                if(Constants.Email.SSL.equalsIgnoreCase(smtpAuthType)){
                    properties.setProperty(transportPrefix + "socketFactory.port", 
                            String.valueOf(smtpPort));
                    properties.setProperty(transportPrefix + "socketFactory.class", 
                            "javax.net.ssl.SSLSocketFactory");
                } else {
                    //TLS
                    properties.setProperty(transportPrefix + "starttls.enable", 
                            String.valueOf(smtpAuth));
                }
            }
            
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUser, smtpPassword);
                }
            };
            
            _session = Session.getInstance(properties, auth);
        } catch (Exception ex) {
            _log.error(ex);
        }

        return _session;
    }

    public static void sendEmail(Address fromEmail, Address[] toEmails, 
            RecipientType type, String subject, String body, boolean htmlFormat) {
        try {
            MimeMessage msg = getNewMessage();

            msg.setFrom(fromEmail);
            msg.setRecipients(type, toEmails);
            msg.setSubject(subject, "UTF-8");

            msg.setSentDate(new Date());
            
            BodyPart bodyPart = new MimeBodyPart();
            
            if (htmlFormat) {
                bodyPart.setContent(body, Constants.ContentType.TEXT_HTML + "; charset=UTF-8");
            } else {
                body = formatPlainText(body);
                
                bodyPart.setText(body);
            }

            Multipart multipart = (Multipart) msg.getContent();
            multipart.removeBodyPart(0);
            multipart.addBodyPart(bodyPart, 0);

            Transport.send(msg);
        } catch (MessagingException ex) {
            _log.error(ex);
        } catch (IOException ex) {
            _log.error(ex);
        }
    }
    
    public static void sendEmail(Address fromEmail, Address[] toEmails, 
            String subject, String body, boolean htmlFormat) {
        sendEmail(fromEmail, toEmails, RecipientType.TO, subject, body, htmlFormat);
    }
    
    public static void sendEmail(String fromEmail, String[] toEmails, 
            RecipientType type, String subject, String body, boolean htmlFormat){
        try {
            InternetAddress fromAddress = new InternetAddress(fromEmail);
            
            InternetAddress[] recipients = new InternetAddress[toEmails.length];
            
            for(int i=0; i< toEmails.length; i++){
                recipients[i] = new InternetAddress(toEmails[i]);
            }
            
            sendEmail(fromAddress, recipients, type, subject, body, htmlFormat);
        } catch (AddressException ex) {
            _log.error(ex);
        }
    }
    
    public static void sendEmail(String fromEmail, String[] toEmails, 
            String subject, String body, boolean htmlFormat){
        sendEmail(fromEmail, toEmails, RecipientType.TO, subject, body, htmlFormat);
    }
    
    public static void sendEmail(String[] toEmails, 
            String subject, String body, boolean htmlFormat){
        sendEmail(StaticUtil.MAIL_SESSION_MAIL_FROM_DEFAULT, toEmails, subject, 
                body, htmlFormat);
    }
    
    public static void sendEmail(String toEmail, 
            String subject, String body, boolean htmlFormat){
        sendEmail(StaticUtil.MAIL_SESSION_MAIL_FROM_DEFAULT, new String[]{toEmail}, 
                subject, body, htmlFormat);
    }
    
    public static void sendTemplateEmail(String toEmail, String emailId, 
            Map<String, String> params){
        try {
            Node tempNode = _xmlUtil.getNodeByXpath(_doc, "//entry[@id='"+emailId+"']");
            
            Element e = (Element) tempNode;
            
            if(tempNode!=null){
                String subject = _xmlUtil.getElementContent(e, "./subject").trim();
                String body = _xmlUtil.getElementContent(e, "./body").trim();
                
                for(Map.Entry<String, String> entry: params.entrySet()){
                    body = body.replace(entry.getKey(), entry.getValue());
                }
                
                sendEmail(toEmail, subject, body, true);
            }
        } catch (Exception ex) {
            _log.error(ex);
        }
    }
    
    public static void sendUserCreateEmail(String toEmail, String userName, 
            String password, String fullName, String verifyUrl){
        Map<String, String> params = new HashMap<String, String>();
        
        if(Validator.isNotNull(fullName)){
            params.put("$[FULL_NAME]", fullName);
        } else {
            params.put("$[FULL_NAME]", userName);
        }
        
        params.put("$[HOST_ADDRESS]", hostAddress);
        params.put("$[USER_NAME]", userName);
        params.put("$[PASSWORD]", password);
        params.put("$[VERIFY_URL]", verifyUrl);
        
        sendTemplateEmail(toEmail, Constants.Email.USER_CREATE, params);
    }
    
    public static void sendUserRegisterEmail(String toEmail, String userName, 
            String password, String fullName, String verifyUrl){
        Map<String, String> params = new HashMap<String, String>();
        
        if(Validator.isNotNull(fullName)){
            params.put("$[FULL_NAME]", fullName);
        } else {
            params.put("$[FULL_NAME]", userName);
        }
        
        params.put("$[HOST_ADDRESS]", hostAddress);
        params.put("$[USER_NAME]", userName);
        params.put("$[PASSWORD]", password);
        params.put("$[VERIFY_URL]", verifyUrl);
        
        sendTemplateEmail(toEmail, Constants.Email.USER_REGISTER, params);
    }
    
    public static void sendPwdResetEmail(String toEmail, String userName, 
            String password, String fullName){
        Map<String, String> params = new HashMap<String, String>();
        
        if(Validator.isNotNull(fullName)){
            params.put("$[FULL_NAME]", fullName);
        } else {
            params.put("$[FULL_NAME]", userName);
        }
        
        params.put("$[HOST_ADDRESS]", hostAddress);
        params.put("$[USER_NAME]", userName);
        params.put("$[PASSWORD]", password);
        
        sendTemplateEmail(toEmail, Constants.Email.RESET_PASSWORD, params);
    }
    
    public static void sendVerifyResetPwd(String toEmail, String userName, 
            String fullName, String verifyCode){        
        Map<String, String> params = new HashMap<String, String>();
        
        if(Validator.isNotNull(fullName)){
            params.put("$[FULL_NAME]", fullName);
        } else {
            params.put("$[FULL_NAME]", userName);
        }
        
        params.put("$[HOST_ADDRESS]", hostAddress);
        params.put("$[USER_NAME]", userName);
        params.put("$[VERIFY_CODE]", verifyCode);
        
        sendTemplateEmail(toEmail, Constants.Email.VERIFY_RESET_PASSWORD, params);
    }
    
    public static void sendVerifyResetPwd(String toEmail, String userName, 
            String fullName){        
        Map<String, String> params = new HashMap<String, String>();
        
        if(Validator.isNotNull(fullName)){
            params.put("$[FULL_NAME]", fullName);
        } else {
            params.put("$[FULL_NAME]", userName);
        }
        
        params.put("$[HOST_ADDRESS]", hostAddress);
        params.put("$[USER_NAME]", userName);
        
        sendTemplateEmail(toEmail, Constants.Email.VERIFY_RESET_PASSWORD_USER_DEACTIVE, 
                params);
    }
    
    public static MimeMessage getNewMessage() throws MessagingException {
        try {
            MimeMessage msg = new MimeMessage(currentSession());

            msg.setSubject("");

            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText("");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);

            msg.setContent(multipart);

            return msg;
        } catch (MessagingException e) {
            throw e;
        } 
    }
    
    private static Session _session;

    private static final Document _doc;
    
    private static final XmlDomUtils _xmlUtil = new XmlDomUtils();
    
    private static String hostAddress;
    
    static {
        _doc = _xmlUtil.read(
                MailUtil.class.getResourceAsStream("../mail/template.xml"));
        
        hostAddress = GetterUtil.getServerUrl();
    }
    
//    public static void main(String[] args) {
//        String toEmail = "linhlh2@viettel.com.vn";
//        
//        Map<String, String> params = new HashMap<String, String>();
//        
//        params.put("$[FULL_NAME]", "Lê Linh");
//        params.put("$[HOST_ADDRESS]", "http://localhost:8080");
//        params.put("$[USER_NAME]", "linhlh2");
//        params.put("$[PASSWORD]", "123456a@");
//        params.put("$[VERIFY_URL]", "http://localhost:8080");
//        
//        sendTemplateEmail(toEmail, "USER_CREATE", 
//                params);
//    }
    
    private static final Logger _log = LogManager.getLogger(MailUtil.class);
}
