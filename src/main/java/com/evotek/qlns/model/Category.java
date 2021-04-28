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
@Table(name = "category")
public class Category implements Serializable, Comparable<Category> {

	private static final long serialVersionUID = -2595220162480903278L;

	private Long categoryId;
	private Date createDate;
	private String description;
	private String folderName;
	private String icon;
	private Long immune;
	private String languageKey;
	private Date modifiedDate;
	private Long parentId;
	private Long status;
	private Long type;
	private Long userId;
	private String userName;
	private String viewPage;
	private Double weight;

	public Category() {
	}

	public Category(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public int compareTo(Category o) {
		if (this.weight == null) {
			return -1;
		}

		if (o.weight == null) {
			return 1;
		}

		return this.weight.compareTo(o.weight) == 0 ? this.categoryId.compareTo(o.categoryId)
				: this.weight.compareTo(o.weight);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		final Category other = (Category) obj;

		if (this.categoryId != other.categoryId
				&& (this.categoryId == null || !this.categoryId.equals(other.categoryId))) {
			return false;
		}

		return true;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getCategoryId() {
		return this.categoryId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	@Column(name = "description", length = 1000)
	public String getDescription() {
		return this.description;
	}

	@Column(name = "folder_name", length = 75)
	public String getFolderName() {
		return this.folderName;
	}

	@Column(name = "icon", length = 255)
	public String getIcon() {
		return this.icon;
	}

	@Column(name = "immune", precision = 1, scale = 0)
	public Long getImmune() {
		return this.immune;
	}

	@Column(name = "language_key", length = 255)
	public String getLanguageKey() {
		return this.languageKey;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date", length = 7)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	@Column(name = "parent_id", precision = 22, scale = 0)
	public Long getParentId() {
		return this.parentId;
	}

	@Column(name = "status", precision = 22, scale = 0)
	public Long getStatus() {
		return this.status;
	}

	@Column(name = "type", precision = 1, scale = 0)
	public Long getType() {
		return this.type;
	}

	@Column(name = "user_id", precision = 22, scale = 0)
	public Long getUserId() {
		return this.userId;
	}

	@Column(name = "user_name", length = 200)
	public String getUserName() {
		return this.userName;
	}

	@Column(name = "view_page", length = 75)
	public String getViewPage() {
		return this.viewPage;
	}

	@Column(name = "weight", precision = 4, scale = 2)
	public Double getWeight() {
		return this.weight;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		return hash;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setImmune(Long immune) {
		this.immune = immune;
	}

	public void setLanguageKey(String languageKey) {
		this.languageKey = languageKey;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
}
