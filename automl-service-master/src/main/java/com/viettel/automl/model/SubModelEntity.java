package com.viettel.automl.model;

import javax.persistence.*;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(name = "SUB_MODEL", schema = "AUTOML", catalog = "")
public class SubModelEntity {
	private Long subModelId;
	private String subModelName;
	private Long modelTypeId;
	private Time createTime;
	private String createUser;
	private Long modelId;

	@Id
	@Column(name = "SUB_MODEL_ID")
	@SequenceGenerator(name = "SUB_MODEL_SEQ", sequenceName = "SUB_MODEL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUB_MODEL_SEQ")
	public Long getSubModelId() {
		return subModelId;
	}

	public void setSubModelId(Long subModelId) {
		this.subModelId = subModelId;
	}

	@Basic
	@Column(name = "SUB_MODEL_NAME")
	public String getSubModelName() {
		return subModelName;
	}

	public void setSubModelName(String subModelName) {
		this.subModelName = subModelName;
	}

	@Basic
	@Column(name = "MODEL_TYPE_ID")
	public Long getModelTypeId() {
		return modelTypeId;
	}

	public void setModelTypeId(Long modelTypeId) {
		this.modelTypeId = modelTypeId;
	}

	@Basic
	@Column(name = "CREATE_TIME")
	public Time getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Time createTime) {
		this.createTime = createTime;
	}

	@Basic
	@Column(name = "CREATE_USER")
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Basic
	@Column(name = "MODEL_ID")
	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SubModelEntity that = (SubModelEntity) o;
		return Objects.equals(subModelId, that.subModelId) && Objects.equals(subModelName, that.subModelName)
				&& Objects.equals(modelTypeId, that.modelTypeId) && Objects.equals(createTime, that.createTime)
				&& Objects.equals(createUser, that.createUser) && Objects.equals(modelId, that.modelId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(subModelId, subModelName, modelTypeId, createTime, createUser, modelId);
	}
}
