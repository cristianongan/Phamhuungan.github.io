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

        this._from = from;

        if (to != null) {
            this._to = new InternetAddress[]{to};
        } else {
            this._to = new InternetAddress[0];
        }

        this._subject = subject;
        this._body = body;
        this._htmlFormat = htmlFormat;
    }

    public void addAttachment(File attachment) {
        if (attachment != null) {
            this._attachments.add(attachment);
        }
    }

    public File[] getAttachments() {
        return this._attachments.toArray(new File[this._attachments.size()]);
    }

    public InternetAddress[] getBCC() {
        return this._bcc;
    }

    public String getBody() {
        return this._body;
    }

    public InternetAddress[] getBulkAddresses() {
        return this._bulkAddresses;
    }

    public InternetAddress[] getCC() {
        return this._cc;
    }

    public InternetAddress getFrom() {
        return this._from;
    }

    public boolean getHTMLFormat() {
        return this._htmlFormat;
    }

    public String getInReplyTo() {
        return this._inReplyTo;
    }

    public String getMessageId() {
        return this._messageId;
    }

    public InternetAddress[] getReplyTo() {
        return this._replyTo;
    }

    public SMTPAccount getSMTPAccount() {
        return this._smtpAccount;
    }

    public String getSubject() {
        return this._subject;
    }

    public InternetAddress[] getTo() {
        return this._to;
    }

    public boolean isHTMLFormat() {
        return this._htmlFormat;
    }

    public void setBCC(InternetAddress bcc) {
        this._bcc = new InternetAddress[]{bcc};
    }

    public void setBCC(InternetAddress[] bcc) {
        this._bcc = bcc;
    }

    public void setBody(String body) {
        this._body = body;
    }

    public void setBulkAddresses(InternetAddress[] bulkAddresses) {
        this._bulkAddresses = bulkAddresses;
    }

    public void setCC(InternetAddress cc) {
        this._cc = new InternetAddress[]{cc};
    }

    public void setCC(InternetAddress[] cc) {
        this._cc = cc;
    }

    public void setFrom(InternetAddress from) {
        this._from = from;
    }

    public void setHTMLFormat(boolean htmlFormat) {
        this._htmlFormat = htmlFormat;
    }

    public void setInReplyTo(String inReplyTo) {
        this._inReplyTo = inReplyTo;
    }

    public void setMessageId(String messageId) {
        this._messageId = messageId;
    }

    public void setReplyTo(InternetAddress[] replyTo) {
        this._replyTo = replyTo;
    }

    public void setSMTPAccount(SMTPAccount account) {
        this._smtpAccount = account;
    }

    public void setSubject(String subject) {
        this._subject = subject;
    }

    public void setTo(InternetAddress to) {
        this._to = new InternetAddress[]{to};
    }

    public void setTo(InternetAddress[] to) {
        this._to = to;
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
