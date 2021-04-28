package com.evotek.qlns.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "file_entry")
public class FileEntry implements Serializable {

	private static final long serialVersionUID = -7216283817240576540L;

	private Date createDate;
	private String description;
	private Document document;
	private Long fileId;
	private Long folderId;
	private Date modifiedDate;
	private String name;
	private Long size;
	private Long userId;
	private String userName;

	public FileEntry() {
	}

	public FileEntry(Long fileId) {
		this.fileId = fileId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		final FileEntry other = (FileEntry) obj;

		if (this.fileId != other.fileId && (this.fileId == null || !this.fileId.equals(other.fileId))) {
			return false;
		}

		return true;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	@Column(name = "description", length = 2000)
	public String getDescription() {
		return this.description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "document_id")
	public Document getDocument() {
		return this.document;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_id", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getFileId() {
		return this.fileId;
	}

	@Column(name = "folder_id", precision = 22, scale = 0)
	public Long getFolderId() {
		return this.folderId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date", length = 7)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	@Column(name = "name", length = 255)
	public String getName() {
		return this.name;
	}

	@Column(name = "size_", precision = 22, scale = 0)
	public Long getSize() {
		return this.size;
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
		int hash = 5;
		return hash;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
