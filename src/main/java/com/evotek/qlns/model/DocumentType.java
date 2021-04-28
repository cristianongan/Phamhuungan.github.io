/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author MRHOT
 */
@Entity
@Table(name = "document_type")
public class DocumentType implements Serializable, Comparable<DocumentType> {

	private List<DocumentType> childDocumentTypes = new ArrayList<DocumentType>();
	private Date createDate;
	private String description;
	private Long documentTypeId;
	private String icon;
	private Date modifiedDate;
	private Long ordinal;
	private DocumentType parentDocumentType;
	private Long status;
	private String typeName;
	private Long userId;
	private String userName;

	public DocumentType() {
	}

	public DocumentType(Long documentTypeId, String typeName, String icon) {
		this.documentTypeId = documentTypeId;
		this.typeName = typeName;
		this.icon = icon;
	}

	@Override
	public int compareTo(DocumentType o) {
		if (this.ordinal == null) {
			return 1;
		}

		if (o.ordinal == null) {
			return -1;
		}

		return this.ordinal.compareTo(o.ordinal);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		final DocumentType other = (DocumentType) obj;

		if (this.documentTypeId != other.documentTypeId
				&& (this.documentTypeId == null || !this.documentTypeId.equals(other.documentTypeId))) {
			return false;
		}

		return true;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentDocumentType", cascade = CascadeType.ALL)
	@Cascade({ org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.REFRESH })
//    @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN, 
//        org.hibernate.annotations.CascadeType.SAVE_UPDATE, 
//        org.hibernate.annotations.CascadeType.REFRESH})
	@LazyCollection(LazyCollectionOption.EXTRA)
//    @IndexColumn(name = "ordinal", base = 0)
	@Fetch(FetchMode.SELECT)
	public List<DocumentType> getChildDocumentTypes() {
		return this.childDocumentTypes;
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
	@Column(name = "document_type_id", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getDocumentTypeId() {
		return this.documentTypeId;
	}

	@Column(name = "icon", length = 75)
	public String getIcon() {
		return this.icon;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date", length = 7)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	@Column(name = "ordinal", precision = 22, scale = 0)
	public Long getOrdinal() {
		return this.ordinal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	public DocumentType getParentDocumentType() {
		return this.parentDocumentType;
	}

	@Column(name = "status", precision = 22, scale = 0)
	public Long getStatus() {
		return this.status;
	}

	@Column(name = "type_name", length = 255)
	public String getTypeName() {
		return this.typeName;
	}

	@Column(name = "user_id", precision = 22, scale = 0)
	public Long getUserId() {
		return this.userId;
	}

	@Column(name = "user_name", length = 75)
	public String getUserName() {
		return this.userName;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		return hash;
	}

	public void setChildDocumentTypes(List<DocumentType> childDocumentTypes) {
		this.childDocumentTypes = childDocumentTypes;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setOrdinal(Long ordinal) {
		this.ordinal = ordinal;
	}

	public void setParentDocumentType(DocumentType parentDocumentType) {
		this.parentDocumentType = parentDocumentType;
	}

	public void setStatus(Long status) {
		this.status = status;
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
