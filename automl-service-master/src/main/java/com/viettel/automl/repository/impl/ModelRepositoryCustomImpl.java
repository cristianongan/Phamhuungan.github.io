package com.viettel.automl.repository.impl;

import com.viettel.automl.dto.object.ModelDTO;
import com.viettel.automl.dto.object.ProjectDTO;
import com.viettel.automl.repository.ModelRepositoryCustom;
import com.viettel.automl.util.DataUtil;
import com.viettel.automl.util.JpaUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class ModelRepositoryCustomImpl implements ModelRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<ModelDTO> getRecent(Pageable pageable) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(
				"p.PROJECT_ID as projectId, p.PROJECT_NAME as projectName, m.MODEL_NAME modelName, m.MODEL_ID modelId, cf.TASK task, m.CREATE_TIME createTime ");
		sql.append("from PROJECT p ");
		sql.append("INNER JOIN CONFIG_FLOW cf ");
		sql.append("ON p.PROJECT_ID = cf.PROJECT_ID ");
		sql.append("Inner join MODEL m ");
		sql.append("on cf.MODEL_ID = m.MODEL_ID ");
		sql.append("where 1 = 1 ");

		Sort sort = pageable.getSort();
		if (sort.isSorted()) {
			StringJoiner joiner = new StringJoiner(",");
			sql.append("ORDER BY ");
			sort.get().forEach(s -> {
				joiner.add(s.getProperty() + " " + s.getDirection());
			});
			sql.append(joiner);
		}

		Query query = em.createNativeQuery(sql.toString(), "recentModelMapping")
				.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());

		List<ModelDTO> results = query.getResultList();
		return results;
	}

	@Override
	public List<ModelDTO> searchModel(ProjectDTO projectDTO) {
		HashMap<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder("select DISTINCT ");
		sql.append(
				"p.PROJECT_ID as projectId, p.PROJECT_NAME as projectName, m.MODEL_NAME modelName, m.MODEL_ID modelId,"
						+ " m.CREATE_TIME createTime , m.CREATE_USER createUser, m.best_model_type bestModelType, h.model_score modelScore ");
		sql.append("from PROJECT p ");
		sql.append("INNER JOIN CONFIG_FLOW cf ");
		sql.append("ON p.PROJECT_ID = cf.PROJECT_ID ");
		sql.append("Inner join MODEL m ");
		sql.append("on cf.MODEL_ID = m.MODEL_ID ");
		sql.append("Left join MODEL_MODEL_TYPE_MAP mtm ");
		sql.append("on mtm.MODEL_ID = m.MODEL_ID ");
		sql.append("Left join SUB_MODEL sm ");
		sql.append("on sm.MODEL_ID = m.MODEL_ID ");
		sql.append("Left join MODEL_TYPE mt ");
		sql.append("on mt.MODEL_TYPE_ID = mtm.MODEL_TYPE_ID ");
		sql.append("Left join HISTORY h ");
		sql.append("on h.MODEL_ID = m.MODEL_ID ");
		sql.append("where 1 = 1 ");

		if (!StringUtils.isEmpty(projectDTO.getModelName())) {
			sql.append("and lower(m.model_name) like :modelName ESCAPE '&' ");
			params.put("modelName", DataUtil.makeLikeParam(projectDTO.getModelName()));
		}
		if (!StringUtils.isEmpty(projectDTO.getCreateUser())) {
			sql.append("and lower(m.CREATE_USER) like :createUser ESCAPE '&' ");
			params.put("createUser", DataUtil.makeLikeParam(projectDTO.getCreateUser()));
		}

		if (!StringUtils.isEmpty(projectDTO.getProjectName())) {
			sql.append("and lower(p.project_name) like :projectName ESCAPE '&' ");
			params.put("projectName", DataUtil.makeLikeParam(projectDTO.getProjectName()));
		}

		if (Objects.nonNull(projectDTO.getModelType())) {
			sql.append("and mtm.MODEL_TYPE_ID = :modelType  ");
			params.put("modelType", projectDTO.getModelType());
		}

		if (Objects.nonNull(projectDTO.getTask())) {
			sql.append("and cf.TASK = :task  ");
			params.put("task", projectDTO.getTask());
		}

		if (Objects.nonNull(projectDTO.getModelMode())) {
			sql.append("and m.MODEL_MODE = :modelMode  ");
			params.put("modelMode", projectDTO.getModelMode());
		}

		if (Objects.nonNull(projectDTO.getTask())) {
			sql.append("and cf.TASK = :task  ");
			params.put("task", projectDTO.getTask());
		}

		if (Objects.nonNull(projectDTO.getProjectId())) {
			sql.append("and p.project_id = :projectId  ");
			params.put("projectId", projectDTO.getProjectId());
		}

		sql.append("Order by m.MODEL_ID desc");

		Query query = em.createNativeQuery(sql.toString(), "searchModelMapping");
		JpaUtil.setQueryParams(query, params);

		return query.getResultList();
	}

	@Override
	public List<ModelDTO> searchModelHaveModelType(ProjectDTO projectDTO) {
		HashMap<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder("select DISTINCT ");
		sql.append("m.MODEL_NAME modelName, m.MODEL_ID modelId,"
				+ " m.CREATE_TIME createTime , m.CREATE_USER createUser, m.best_model_type bestModelType, mt.model_type_name modelTypeName,"
				+ " h.model_score modelScore, p.project_name projectName ");
		sql.append("from MODEL m ");
		sql.append("INNER JOIN CONFIG_FLOW cf ");
		sql.append("ON cf.MODEL_ID = m.MODEL_ID ");
		sql.append("INNER JOIN Project p ");
		sql.append("ON p.PROJECT_ID = cf.PROJECT_ID ");
		sql.append("Left join MODEL_MODEL_TYPE_MAP mtm ");
		sql.append("on mtm.MODEL_ID = m.MODEL_ID ");
		sql.append("Left join SUB_MODEL sm ");
		sql.append("on sm.MODEL_ID = m.MODEL_ID ");
		sql.append("Left join MODEL_TYPE mt ");
		sql.append("on mt.MODEL_TYPE_ID = mtm.MODEL_TYPE_ID ");
		sql.append("Left join HISTORY h ");
		sql.append("on h.MODEL_ID = m.MODEL_ID and h.CURRENT_STATUS = 3 ");
		sql.append("where 1 = 1  and h.CURRENT_STATUS = 3 ");

		if (!StringUtils.isEmpty(projectDTO.getModelName())) {
			sql.append("and lower(m.model_name) like :modelName ESCAPE '&' ");
			params.put("modelName", DataUtil.makeLikeParam(projectDTO.getModelName()));
		}

		if (Objects.nonNull(projectDTO.getBestModelType())) {
			sql.append("and m.BEST_MODEL_TYPE = :bestModelType  ");
			params.put("bestModelType", projectDTO.getBestModelType());
		}

		if (Objects.nonNull(projectDTO.getModelMode())) {
			sql.append("and m.MODEL_MODE = :modelMode  ");
			params.put("modelMode", projectDTO.getModelMode());
		}

		if (Objects.nonNull(projectDTO.getModelType())) {
			sql.append("and mtm.MODEL_TYPE_ID = :modelType  ");
			params.put("modelType", projectDTO.getModelType());
		}

		if (Objects.nonNull(projectDTO.getProjectId())) {
			sql.append("and p.PROJECT_ID = :projectId  ");
			params.put("projectId", projectDTO.getProjectId());
		}

		sql.append("Order by m.MODEL_ID desc");

		Query query = em.createNativeQuery(sql.toString(), "searchModelHaveModelTypeMapping");
		JpaUtil.setQueryParams(query, params);

		return query.getResultList();
	}
}
