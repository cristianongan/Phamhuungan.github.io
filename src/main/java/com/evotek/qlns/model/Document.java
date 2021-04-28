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

	private static final long serialVersionUID = -1196841363460803909L;

	private String content;
	private Date createDate;
	private String description;
	private Long documentId;
	private String documentName;
	private String documentNumber;
	private Long documentTypeId;
	private Set<FileEntry> files;
	private Date modifiedDate;
	private Date promulgationDate;
	private String promulgationDept;
	private String typeName;
	private Long userId;
	private String userName;

	public Document() {
	}

	public Document(Long documentId) {
		this.documentId = documentId;
	}

	@Override
	public int compareTo(Document o) {
		if (this.documentId == null) {
			return -1;
		}

		if (o.documentId == null) {
			return 1;
		}

		return this.documentId.compareTo(o.documentId);
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
				&& (this.documentId == null || !this.documentId.equals(other.documentId))) {
			return false;
		}

		return true;
	}

	@Column(name = "content")
	public String getContent() {
		return this.content;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "document_id", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getDocumentId() {
		return this.documentId;
	}

	@Column(name = "document_name", length = 1000)
	public String getDocumentName() {
		return this.documentName;
	}

	@Column(name = "document_number", length = 100)
	public String getDocumentNumber() {
		return this.documentNumber;
	}

	@Column(name = "document_type_id", precision = 22, scale = 0)
	public Long getDocumentTypeId() {
		return this.documentTypeId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
	@LazyCollection(LazyCollectionOption.EXTRA)
	public Set<FileEntry> getFiles() {
		return this.files;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date", length = 7)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "promulgation_date", length = 7)
	public Date getPromulgationDate() {
		return this.promulgationDate;
	}

	@Column(name = "promulgation_dept", length = 255)
	public String getPromulgationDept() {
		return this.promulgationDept;
	}

	@Transient
	public String getTypeName() {
		return this.typeName;
	}

	@Column(name = "user_id", precision = 22, scale = 0)
	public Long getUserId() {
		return this.userId;
	}

	@Column(name = "user_name", length = 200)
	public String getUserName() {
		return this.userName;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		return hash;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public void setFiles(Set<FileEntry> files) {
		this.files = files;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setPromulgationDate(Date promulgationDate) {
		this.promulgationDate = promulgationDate;
	}

	public void setPromulgationDept(String promulgationDept) {
		this.promulgationDept = promulgationDept;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
