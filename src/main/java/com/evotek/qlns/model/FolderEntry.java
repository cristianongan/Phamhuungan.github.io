package com.evotek.qlns.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "folder_entry")
public class FolderEntry implements Serializable {

	private Date createDate;
	private String description;
	private Long fileCount;
	private Long folderId;
	private Date modifiedDate;
	private String name;
	private Long parentFolderId;
	private Long userId;
	private String userName;

	public FolderEntry() {
	}

	public FolderEntry(Long folderId) {
		this.folderId = folderId;
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

	@Column(name = "file_count", precision = 22, scale = 0)
	public Long getFileCount() {
		return this.fileCount;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "folder_id", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getFolderId() {
		return this.folderId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date", length = 7)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	@Column(name = "name", length = 200)
	public String getName() {
		return this.name;
	}

	@Column(name = "parent_folder_id", precision = 22, scale = 0)
	public Long getParentFolderId() {
		return this.parentFolderId;
	}

	@Column(name = "user_id", precision = 22, scale = 0)
	public Long getUserId() {
		return this.userId;
	}

	@Column(name = "user_name", length = 200)
	public String getUserName() {
		return this.userName;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFileCount(Long fileCount) {
		this.fileCount = fileCount;
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

	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
