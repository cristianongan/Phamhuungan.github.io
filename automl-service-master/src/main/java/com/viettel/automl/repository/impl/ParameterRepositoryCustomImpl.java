package com.viettel.automl.repository.impl;

import com.viettel.automl.dto.object.ParameterDTO;
import com.viettel.automl.repository.ParameterRepositoryCustom;
import com.viettel.automl.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ParameterRepositoryCustomImpl implements ParameterRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<ParameterDTO> doSearch(ParameterDTO dto) {
		HashMap<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder("select p.parameter_id parameterId, ");
		sql.append("p.parameter_name parameterName, ");
		sql.append("p.parameter_type parameterType, ");
		sql.append("p.sub_model_id subModelId, ");
		sql.append("p.model_type_id modelTypeId, ");
		sql.append("p.data_type dataType ");
		sql.append("from PARAMETER p ");
		sql.append("where 1 = 1 ");

		if (Objects.nonNull(dto.getModelTypeId())) {
			sql.append("and p.model_type_id = :modelTypeId ");
			params.put("modelTypeId", dto.getModelTypeId());
		}

		if (Objects.nonNull(dto.getSubModelId())) {
			sql.append("and p.sub_model_id = :subModelId ");
			params.put("subModelId", dto.getSubModelId());
		}

		Query query = em.createNativeQuery(sql.toString(), "searchParamMapping");
		JpaUtil.setQueryParams(query, params);

		return query.getResultList();
	}
}
