package com.evotek.qlns.dao.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.evotek.qlns.dao.ApplicantInformationDAO;
import com.evotek.qlns.model.ApplicantInformation;
import com.evotek.qlns.model.ApplyPosition;

@Repository
@Transactional
public class ApplicantInformationDAOImpl extends AbstractDAO<ApplicantInformation> implements ApplicantInformationDAO{
	private static final Logger _log = LogManager.getLogger(DocumentDAOImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ApplicantInformation> getList(Pageable pageable, String key, ApplyPosition applyPosition,
			String exp,Date startDate,Date endDate) {
		//init
		Session session = getCurrentSession();
		StringBuilder sql =new StringBuilder("select c.id id,c.full_name name,"
				+ "c.apply_position_id applyPositionId,a.name applyPositionName, "
				+ "c.apply_date applyDate,c.status status,c.exp exp,"
				+ "c.interview_result interviewResult"
				+ " from applicant_information c left join apply_position a on c.apply_position_id = a.id");
		StringBuilder condition = new StringBuilder();
		HashMap<String, Object> parameter =new HashMap<String, Object>();
		//check filter
		if(applyPosition!= null) {
			condition.append(" c.apply_position_id = :id");
			parameter.put("id", applyPosition.getId());
		}
		if(exp!= null && !exp.equals("")) {
			if(condition.length()>0) condition.append(" and");
			condition.append(" c.exp = :exp");
			parameter.put("exp", exp);
		}
		//check key
		if(key!=null && !key.equals("")) {
			if(condition.length()>0) condition.append(" and");
			condition.append(" (c.full_name like :key or c.email like :key or c.mobile_phone like :key)");
			parameter.put("key","%"+key+"%");
		}
		//check date
		if(startDate!=null && endDate !=null) {
			if(condition.length()>0) condition.append(" and");
			condition.append(" c.apply_date between :startDate and :endDate");
			parameter.put("startDate", startDate);
			parameter.put("endDate", endDate);
			
		}
		//end
		if(condition.length()>0)
			condition.insert(0, " where");
		sql.append(condition);
		sql.append(" order by(c.id) desc");
		Query query = session.createNativeQuery(sql.toString(), "applicantInformationMapping");
		query.setFirstResult((int) pageable.getOffset());
		query.setMaxResults((int)  pageable.getPageSize());
		for(Entry<String, Object> e: parameter.entrySet()) {
			query.setParameter(e.getKey(), e.getValue());
		}
		return query.getResultList();
	}

	@Override
	public long count( String key, ApplyPosition applyPosition, String exp,Date startDate,Date endDate) {
		//init
		Session session = getCurrentSession();
		StringBuilder sql =new StringBuilder("select count(c.id)"
				+ " from applicant_information c left join apply_position a on c.apply_position_id = a.id");
		StringBuilder condition = new StringBuilder();
		HashMap<String, Object> parameter =new HashMap<String, Object>();
		//check filter
		if(applyPosition!= null) {
			condition.append(" c.apply_position_id = :id");
			parameter.put("id", applyPosition.getId());
		}
		if(exp!= null && !exp.equals("")) {
			if(condition.length()>0) condition.append(" and");
			condition.append(" c.exp = :exp");
			parameter.put("exp", exp);
		}
		//check key
		if(key!=null && !key.equals("")) {
			if(condition.length()>0) condition.append(" and");
			condition.append(" c.full_name like :key or c.email like :key or c.mobile_phone like :key");
			parameter.put("key","%"+key+"%");
		}
		//check date
		if(startDate!=null && endDate !=null) {
			if(condition.length()>0) condition.append(" and");
			condition.append(" c.apply_date between :startDate and :endDate");
			parameter.put("startDate", startDate);
			parameter.put("endDate", endDate);
					
		}
		if(condition.length()>0)
			condition.insert(0, " where");
		sql.append(condition);
		Query query = session.createNativeQuery(sql.toString());
		for(Entry<String, Object> e: parameter.entrySet()) {
			query.setParameter(e.getKey(), e.getValue());
		}
		return ((BigInteger) query.getSingleResult()).longValue();
	}

	@Override
	public void deleteOne(long id) {
		deleteById(ApplicantInformation.class, id);
	}

	@Override
	public void deleteOne(ApplicantInformation applicantInformation) {
		delete(applicantInformation);
	}

	@Override
	public void deleteMany(Collection<ApplicantInformation> list) {
		deleteAll(list);
	}

	@Override
	public void updateOne(ApplicantInformation applicantInformation) {
		update(applicantInformation);
	}

	@Override
	public ApplicantInformation getOne(long id) {
		return findById(ApplicantInformation.class, id);
	}

	@Override
	public void saveOne(ApplicantInformation applicantInformation) {
		save(applicantInformation);
	}

}
