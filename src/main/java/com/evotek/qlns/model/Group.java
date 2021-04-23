package com.evotek.qlns.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "group_")
public class Group implements Serializable {

    private Long categoryId;
    private Long groupId;
    private Long userId;
    private String description;
    private String userName;
    private String groupName;
    private Date createDate;
    private Long status;
    private Date modifiedDate;
    private Set<Role> roles = new HashSet<Role>(0);
    private Set<Right> rights = new HashSet<Right>(0);

    @Column(name = "category_id", precision = 22, scale = 0)
    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

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

    @Column(name = "user_name", length = 200)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "group_name", length = 200)
    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", length = 7)
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "group_right", joinColumns = {
        @JoinColumn(name = "group_id", nullable = false, updatable = false)
    }, inverseJoinColumns = {
        @JoinColumn(name = "right_id", nullable = false, updatable = false)
    })
//    @LazyCollection(LazyCollectionOption.EXTRA)
    @Fetch(FetchMode.SELECT)
    public Set<Right> getRights() {
        return this.rights;
    }

    public void setRights(Set<Right> rights) {
        this.rights = rights;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Group() {
    }

    public Group(Long groupsId) {
        this.groupId = groupsId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Group other = (Group) obj;
        
        if (this.groupId != other.groupId
                && (this.groupId == null
                || !this.groupId.equals(other.groupId))) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
}
