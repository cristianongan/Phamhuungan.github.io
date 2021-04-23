/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "document")
public class Document implements Serializable, Comparable<Document> {

    private Long documentId;
    private Long userId;
    private String userName;
    private Date createDate;
    private Date modifiedDate;
    private String documentName;
    private String documentNumber;
    private String promulgationDept;
    private Date promulgationDate;
    private String description;
    private String content;
    private Long documentTypeId;
    private String typeName;
    private Set<FileEntry> files;
    @Column(name = "document_type_id", precision = 22, scale = 0)
    public Long getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    @Transient
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", length = 7)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    @Column(name = "document_name", length = 1000)
    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @Column(name = "document_number", length = 100)
    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date", length = 7)
    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "promulgation_date", length = 7)
    public Date getPromulgationDate() {
        return promulgationDate;
    }

    public void setPromulgationDate(Date promulgationDate) {
        this.promulgationDate = promulgationDate;
    }

    @Column(name = "promulgation_dept", length = 255)
    public String getPromulgationDept() {
        return promulgationDept;
    }

    public void setPromulgationDept(String promulgationDept) {
        this.promulgationDept = promulgationDept;
    }

    @Column(name = "user_id", precision = 22, scale = 0)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "user_name", length = 200)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public Set<FileEntry> getFiles() {
        return files;
    }

    public void setFiles(Set<FileEntry> files) {
        this.files = files;
    }

    public int compareTo(Document o) {
        if (documentId == null) {
            return -1;
        }

        if (o.documentId == null) {
            return 1;
        }

        return documentId.compareTo(o.documentId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Document other = (Document) obj;

        if (this.documentId != other.documentId
                && (this.documentId == null
                || !this.documentId.equals(other.documentId))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    public Document() {
    }

    public Document(Long documentId) {
        this.documentId = documentId;
    }
}
