package com.viettel.automl.model;

import com.viettel.automl.dto.object.ModelDTO;
import com.viettel.automl.dto.object.ProjectDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "PROJECT", schema = "AUTOML", catalog = "")
@SqlResultSetMappings(value = {
		@SqlResultSetMapping(name = "recentModelMapping", classes = {
				@ConstructorResult(targetClass = ModelDTO.class, columns = {
						@ColumnResult(name = "projectId", type = Long.class),
						@ColumnResult(name = "projectName", type = String.class),
						@ColumnResult(name = "modelId", type = Long.class),
						@ColumnResult(name = "modelName", type = String.class),
						@ColumnResult(name = "task", type = String.class),
						@ColumnResult(name = "createTime", type = Instant.class) }) }),
		@SqlResultSetMapping(name = "doSearchProject", classes = {
				@ConstructorResult(targetClass = ProjectDTO.class, columns = {
						@ColumnResult(name = "projectId", type = Long.class),
						@ColumnResult(name = "projectName", type = String.class),
						@ColumnResult(name = "description", type = String.class),
						@ColumnResult(name = "createTime", type = Instant.class),
						@ColumnResult(name = "createUser", type = String.class) }) }),
		@SqlResultSetMapping(name = "searchModelMapping", classes = {
				@ConstructorResult(targetClass = ModelDTO.class, columns = {
						@ColumnResult(name = "projectId", type = Long.class),
						@ColumnResult(name = "projectName", type = String.class),
						@ColumnResult(name = "modelId", type = Long.class),
						@ColumnResult(name = "modelName", type = String.class),
//                                @ColumnResult(name = "task", type = String.class),
						@ColumnResult(name = "createTime", type = Instant.class),
						@ColumnResult(name = "createUser", type = String.class),
						@ColumnResult(name = "bestModelType", type = String.class),
//                                @ColumnResult(name = "modelTypeName", type = String.class),
						@ColumnResult(name = "modelScore", type = String.class) }) }),
		@SqlResultSetMapping(name = "searchModelHaveModelTypeMapping", classes = {
				@ConstructorResult(targetClass = ModelDTO.class, columns = {
						@ColumnResult(name = "modelId", type = Long.class),
						@ColumnResult(name = "modelName", type = String.class),
						@ColumnResult(name = "createTime", type = Instant.class),
						@ColumnResult(name = "createUser", type = String.class),
						@ColumnResult(name = "bestModelType", type = String.class),
						@ColumnResult(name = "modelTypeName", type = String.class),
						@ColumnResult(name = "modelScore", type = String.class),
						@ColumnResult(name = "projectName", type = String.class) }) })

})
public class ProjectEntity implements Serializable {
	private Long projectId;
	private String projectName;
	private String description;
	private Instant createTime;
	private String createUser;

	@Id
	@Column(name = "PROJECT_ID")
	@SequenceGenerator(name = "PROJECT_SEQ", sequenceName = "PROJECT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_SEQ")
	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	@Basic
	@Column(name = "PROJECT_NAME")
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Basic
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic
	@Column(name = "CREATE_TIME")
	public Instant getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Instant createTime) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProjectEntity that = (ProjectEntity) o;
		return Objects.equals(projectId, that.projectId) && Objects.equals(projectName, that.projectName)
				&& Objects.equals(description, that.description) && Objects.equals(createTime, that.createTime)
				&& Objects.equals(createUser, that.createUser);
	}

	@Override
	public int hashCode() {
		return Objects.hash(projectId, projectName, description, createTime, createUser);
	}
}
