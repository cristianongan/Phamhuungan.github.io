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
	
	private Long userId;
    private String description;
    private Long status;
    private Date modifiedDate;
    private String icon;
    private String folderName;
    private String languageKey;
    private String viewPage;
    private String userName;
    private Long categoryId;
    private Date createDate;
    private Long parentId;
    private Long type;
    private Double weight;
    private Long immune;

    @Column(name = "user_id", precision = 22, scale = 0)
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "description", length = 1000)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "status", precision = 22, scale = 0)
    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date", length = 7)
    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Column(name = "icon", length = 255)
    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column(name = "folder_name", length = 75)
    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Column(name = "language_key", length = 255)
    public String getLanguageKey() {
        return this.languageKey;
    }

    public void setLanguageKey(String languageKey) {
        this.languageKey = languageKey;
    }

    @Column(name = "view_page", length = 75)
    public String getViewPage() {
        return this.viewPage;
    }

    public void setViewPage(String viewPage) {
        this.viewPage = viewPage;
    }

    @Column(name = "user_name", length = 200)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", length = 7)
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "parent_id", precision = 22, scale = 0)
    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Column(name = "type", precision = 1, scale = 0)
    public Long getType() {
        return this.type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Column(name = "weight", precision = 4, scale = 2)
    public Double getWeight() {
        return this.weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Column(name = "immune", precision = 1, scale = 0)
    public Long getImmune() {
        return this.immune;
    }

    public void setImmune(Long immune) {
        this.immune = immune;
    }

    @Override
	public int compareTo(Category o) {
        if(this.weight==null) {
            return -1;
        }

        if(o.weight==null) {
            return 1;
        }

        return this.weight.compareTo(o.weight)==0 ? this.categoryId.compareTo(o.categoryId) :
            this.weight.compareTo(o.weight);
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
                && (this.categoryId == null
                || !this.categoryId.equals(other.categoryId))) {
            return false;
        }

        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    public Category() {
    }

    public Category(Long categoryId) {
        this.categoryId = categoryId;
    }
}
