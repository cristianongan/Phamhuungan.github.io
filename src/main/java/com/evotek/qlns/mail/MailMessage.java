package com.evotek.qlns.mail;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;

/**
 * <a href="MailMessage.java.html"><b><i>View Source</i></b></a>
 */
public class MailMessage implements Serializable {

    public MailMessage() {
    }

    public MailMessage(
            InternetAddress from, String subject, String body,
            boolean htmlFormat) {

        this(from, null, subject, body, htmlFormat);
    }

    public MailMessage(
            InternetAddress from, InternetAddress to, String subject, String body,
            boolean htmlFormat) {

        _from = from;

        if (to != null) {
            _to = new InternetAddress[]{to};
        } else {
            _to = new InternetAddress[0];
        }

        _subject = subject;
        _body = body;
        _htmlFormat = htmlFormat;
    }

    public void addAttachment(File attachment) {
        if (attachment != null) {
            _attachments.add(attachment);
        }
    }

    public File[] getAttachments() {
        return _attachments.toArray(new File[_attachments.size()]);
    }

    public InternetAddress[] getBCC() {
        return _bcc;
    }

    public String getBody() {
        return _body;
    }

    public InternetAddress[] getBulkAddresses() {
        return _bulkAddresses;
    }

    public InternetAddress[] getCC() {
        return _cc;
    }

    public InternetAddress getFrom() {
        return _from;
    }

    public boolean getHTMLFormat() {
        return _htmlFormat;
    }

    public String getInReplyTo() {
        return _inReplyTo;
    }

    public String getMessageId() {
        return _messageId;
    }

    public InternetAddress[] getReplyTo() {
        return _replyTo;
    }

    public SMTPAccount getSMTPAccount() {
        return _smtpAccount;
    }

    public String getSubject() {
        return _subject;
    }

    public InternetAddress[] getTo() {
        return _to;
    }

    public boolean isHTMLFormat() {
        return _htmlFormat;
    }

    public void setBCC(InternetAddress bcc) {
        _bcc = new InternetAddress[]{bcc};
    }

    public void setBCC(InternetAddress[] bcc) {
        _bcc = bcc;
    }

    public void setBody(String body) {
        _body = body;
    }

    public void setBulkAddresses(InternetAddress[] bulkAddresses) {
        _bulkAddresses = bulkAddresses;
    }

    public void setCC(InternetAddress cc) {
        _cc = new InternetAddress[]{cc};
    }

    public void setCC(InternetAddress[] cc) {
        _cc = cc;
    }

    public void setFrom(InternetAddress from) {
        _from = from;
    }

    public void setHTMLFormat(boolean htmlFormat) {
        _htmlFormat = htmlFormat;
    }

    public void setInReplyTo(String inReplyTo) {
        _inReplyTo = inReplyTo;
    }

    public void setMessageId(String messageId) {
        _messageId = messageId;
    }

    public void setReplyTo(InternetAddress[] replyTo) {
        _replyTo = replyTo;
    }

    public void setSMTPAccount(SMTPAccount account) {
        _smtpAccount = account;
    }

    public void setSubject(String subject) {
        _subject = subject;
    }

    public void setTo(InternetAddress to) {
        _to = new InternetAddress[]{to};
    }

    public void setTo(InternetAddress[] to) {
        _to = to;
    }

    private InternetAddress _from;
    private InternetAddress[] _to;
    private InternetAddress[] _cc;
    private InternetAddress[] _bcc;
    private InternetAddress[] _bulkAddresses;
    private String _subject;
    private String _body;
    private boolean _htmlFormat;
    private InternetAddress[] _replyTo;
    private String _messageId;
    private String _inReplyTo;
    private List<File> _attachments = new ArrayList<File>();
    private SMTPAccount _smtpAccount;

}
