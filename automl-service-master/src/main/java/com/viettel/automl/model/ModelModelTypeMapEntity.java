package com.viettel.automl.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "MODEL_MODEL_TYPE_MAP", schema = "AUTOML", catalog = "")
public class ModelModelTypeMapEntity {
	private Long modelModelTypeMapId;
	private Long modelId;
	private Long modelTypeId;

	@Id
	@Column(name = "MODEL_MODEL_TYPE_MAP_ID")
	@SequenceGenerator(name = "MODEL_MODEL_TYPE_MAP_SEQ", sequenceName = "MODEL_MODEL_TYPE_MAP_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MODEL_MODEL_TYPE_MAP_SEQ")
	public Long getModelModelTypeMapId() {
		return modelModelTypeMapId;
	}

	public void setModelModelTypeMapId(Long modelModelTypeMapId) {
		this.modelModelTypeMapId = modelModelTypeMapId;
	}

	@Basic
	@Column(name = "MODEL_ID")
	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	@Basic
	@Column(name = "MODEL_TYPE_ID")
	public Long getModelTypeId() {
		return modelTypeId;
	}

	public void setModelTypeId(Long modelTypeId) {
		this.modelTypeId = modelTypeId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ModelModelTypeMapEntity that = (ModelModelTypeMapEntity) o;
		return Objects.equals(modelModelTypeMapId, that.modelModelTypeMapId) && Objects.equals(modelId, that.modelId)
				&& Objects.equals(modelTypeId, that.modelTypeId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(modelModelTypeMapId, modelId, modelTypeId);
	}
}
