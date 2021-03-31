package com.viettel.automl.repository.impl;

import com.viettel.automl.dto.object.ProjectDTO;
import com.viettel.automl.repository.ProjectRepositoryCustom;
import com.viettel.automl.util.DataUtil;
import com.viettel.automl.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<ProjectDTO> doSearch(ProjectDTO dto) {
		HashMap<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder("select ");
		sql.append("p.project_id projectId, ");
		sql.append("p.project_name projectName, ");
		sql.append("p.description description, ");
		sql.append("p.create_time createTime, ");
		sql.append("p.create_user createUser ");
		sql.append("from project p ");
		sql.append("where 1 = 1 ");

		if (Objects.nonNull(dto.getProjectName())) {
			sql.append("and lower(p.project_name) like :projectName ESCAPE '&' ");
			params.put("projectName", DataUtil.makeLikeParam(dto.getProjectName()));
		}

		if (Objects.nonNull(dto.getCreateUser())) {
			sql.append("and lower(p.create_user) like :createUser ESCAPE '&' ");
			params.put("createUser", DataUtil.makeLikeParam(dto.getCreateUser()));
		}

		if (Objects.nonNull(dto.getCreateFrom())) {
			sql.append("and p.create_time >= TO_DATE('" + dto.getCreateFrom() + " ', 'yyyy-mm-dd hh24:mi:ss') ");
		}

		if (Objects.nonNull(dto.getCreateTo())) {
			sql.append("and p.create_time <= TO_DATE(' " + dto.getCreateTo() + "', 'yyyy-mm-dd hh24:mi:ss')");
//            params.put("createTo", );
		}

		sql.append("Order by p.project_id desc ");

		Query query = em.createNativeQuery(sql.toString(), "doSearchProject");
		JpaUtil.setQueryParams(query, params);

		return query.getResultList();
	}
}
