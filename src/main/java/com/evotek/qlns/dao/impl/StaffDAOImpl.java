/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.evotek.qlns.dao.StaffDAO;
import com.evotek.qlns.model.ContractType;
import com.evotek.qlns.model.Department;
import com.evotek.qlns.model.Job;
import com.evotek.qlns.model.Staff;
import com.evotek.qlns.util.CharPool;
import com.evotek.qlns.util.DateUtil;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author PC
 */
public class StaffDAOImpl extends AbstractDAO<Staff> implements StaffDAO {

	private static final Logger _log = LogManager.getLogger(StaffDAOImpl.class);

	@Override
	public Staff getStaff(Long staffId) {
		Staff result = null;

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Staff> criteria = builder.createQuery(Staff.class);

			Root<Staff> root = criteria.from(Staff.class);

			root.fetch("department", JoinType.LEFT);
			root.fetch("job", JoinType.LEFT);
			root.fetch("contractType", JoinType.LEFT);

			criteria.select(root).where(builder.equal(root.get("staffId"), staffId));

			result = (Staff) session.createQuery(criteria).uniqueResult();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return result;
	}

	@Override
	public List<Staff> getStaff(String keyword, int firstResult, int maxResult, String orderByColumn,
			String orderByType) {
		List<Staff> results = new ArrayList<Staff>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Staff> criteria = builder.createQuery(Staff.class);

			Root<Staff> root = criteria.from(Staff.class);

			List<Predicate> predicates = getStaffPredicates(builder, root, keyword);

			criteria.select(root);

			if (Validator.isNotNull(predicates)) {
				criteria.where(builder.or(predicates.toArray(new Predicate[predicates.size()])));
			}

			Query<Staff> q = session.createQuery(criteria);

			if (firstResult >= 0 && maxResult > 0) {
				q.setFirstResult(firstResult);
				q.setMaxResults(maxResult);
			}

			if (Validator.isNotNull(orderByColumn)) {
				if (StringPool.ASC.equalsIgnoreCase(orderByType)) {
					criteria.orderBy(builder.asc(root.get(orderByColumn)));
				} else {
					criteria.orderBy(builder.desc(root.get(orderByColumn)));
				}

			} else {
				criteria.orderBy(builder.asc(root.get("staffName")));
			}

			results = q.getResultList();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	private List<Predicate> getStaffPredicates(CriteriaBuilder builder, Root<Staff> root, String keyword)
			throws Exception {
		List<Predicate> predicates = new ArrayList<>();

		try {
			Join<Staff, Department> departmentJoin = root.join("department", JoinType.LEFT);

			Join<Staff, Job> jobJoin = root.join("job", JoinType.LEFT);

			Join<Staff, ContractType> contractTypeJoin = root.join("contractType", JoinType.LEFT);

			if (Validator.isNotNull(keyword)) {
				predicates.add(builder.like(builder.lower(root.get("staffName")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(departmentJoin.get("deptName")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(jobJoin.get("jobTitle")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("permanentResidence")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("currentResidence")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("note")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(contractTypeJoin.get("contractTypeName")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("contractNumber")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("taxCode")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("insuranceBookNumber")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("paidPlace")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("levels")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("majors")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("college")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("identityCard")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("grantPlace")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("mobile")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("email")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
			}

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return predicates;
	}

	private List<Predicate> getStaffPredicates(CriteriaBuilder builder, Root<Staff> root, String staffName,
			Long yearOfBirth, Department dept, String email, Job job, String phone, String orderByColumn,
			String orderByType) throws Exception {
		List<Predicate> predicates = new ArrayList<>();

		try {
			Join<Staff, Department> departmentJoin = root.join("department", JoinType.LEFT);

			Join<Staff, Job> jobJoin = root.join("job", JoinType.LEFT);

			Join<Staff, ContractType> contractTypeJoin = root.join("contractType", JoinType.LEFT);

			if (Validator.isNotNull(staffName)) {
				predicates.add(builder.like(builder.lower(root.get("staffName")),
						QueryUtil.getFullStringParam(staffName, true), CharPool.EXCLAMATION));
			}

			if (Validator.isNotNull(yearOfBirth)) {
				predicates.add(builder.equal(builder.function("year", Long.class, root.get("dateOfBirth")), yearOfBirth));
			}

			if (Validator.isNotNull(dept) && Validator.isNotNull(dept.getDeptId())) {
				predicates.add(builder.equal(departmentJoin.get("deptId"), dept.getDeptId()));
			}

			if (Validator.isNotNull(email)) {
				predicates.add(builder.like(builder.lower(root.get("email")),
						QueryUtil.getFullStringParam(email, true), CharPool.EXCLAMATION));
			}

			if (Validator.isNotNull(job) && Validator.isNotNull(job.getJobId())) {
				predicates.add(builder.equal(jobJoin.get("jobId"), job.getJobId()));
			}

			if (Validator.isNotNull(phone)) {
				Disjunction disjunction = Restrictions.disjunction();

				disjunction.add(LikeCriterionMaker.ilike("mobile", phone, MatchMode.ANYWHERE));
				disjunction.add(LikeCriterionMaker.ilike("homePhone", phone, MatchMode.ANYWHERE));

				cri.add(disjunction);
			}

			if (Validator.isNotNull(orderByColumn)) {
				if (StringPool.ASC.equalsIgnoreCase(orderByType)) {
					cri.addOrder(Order.asc(orderByColumn));
				} else {
					cri.addOrder(Order.desc(orderByColumn));
				}

			} else {
				cri.addOrder(Order.asc("staffName"));
			}

			return cri;
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return predicates;
	}

	@Override
	public int getStaffCount(String keyword) {
		int result = 0;

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

			Root<Staff> root = criteria.from(Staff.class);

			List<Predicate> predicates = getStaffPredicates(builder, root, keyword);

			criteria.select(builder.count(root));

			if (Validator.isNotNull(predicates)) {
				criteria.where(builder.or(predicates.toArray(new Predicate[predicates.size()])));
			}

			Long count = (Long) session.createQuery(criteria).uniqueResult();

			if (count != null) {
				result = count.intValue();
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return result;
	}

	@Override
	public List<Staff> getStaff(String staffName, Long yearOfBirth, Department dept, String email, Job job,
			String phone, int firstResult, int maxResult, String orderByColumn, String orderByType) {
		List<Staff> results = new ArrayList<Staff>();

		try {
			Criteria cri = getStaffPredicates(staffName, yearOfBirth, dept, email, job, phone, orderByColumn,
					orderByType);

			if (firstResult >= 0 && maxResult > 0) {
				cri.setFirstResult(firstResult);
				cri.setMaxResults(maxResult);
			}

			results = cri.list();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@Override
	public int getStaffCount(String staffName, Long yearOfBirth, Department dept, String email, Job job, String phone) {
		int result = 0;

		try {
			Criteria cri = getStaffPredicates(staffName, yearOfBirth, dept, email, job, phone, null, null);

			cri.setProjection(Projections.rowCount());

			result = (Integer) cri.uniqueResult();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return result;
	}

	@Override
	public List<Staff> getStaffByIdList(List<Long> idList, int firstResult, int maxResult, String orderByColumn,
			String orderByType) {
		List<Staff> results = new ArrayList<Staff>();

		try {
			Criteria cri = getStaffByIdListQuery(idList, orderByColumn, orderByType);

			if (firstResult >= 0 && maxResult > 0) {
				cri.setFirstResult(firstResult);
				cri.setMaxResults(maxResult);
			}

			results = cri.list();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@Override
	public int getStaffCountByIdList(List<Long> idList) {
		int result = 0;

		try {
			Criteria cri = getStaffByIdListQuery(idList, null, null);

			cri.setProjection(Projections.rowCount());

			result = (Integer) cri.uniqueResult();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return result;
	}

	private Criteria getStaffByIdListQuery(List<Long> idList, String orderByColumn, String orderByType)
			throws Exception {
		try {
			Criteria cri = currentSession().createCriteria(Staff.class);

			cri.createAlias("department", "d", CriteriaSpecification.LEFT_JOIN);
			cri.createAlias("job", "j", CriteriaSpecification.LEFT_JOIN);
			cri.createAlias("contractType", "ct", CriteriaSpecification.LEFT_JOIN);

			if (Validator.isNotNull(idList)) {
				cri.add(Restrictions.in("d.deptId", idList));
			}

			if (Validator.isNotNull(orderByColumn)) {
				if (StringPool.ASC.equalsIgnoreCase(orderByType)) {
					cri.addOrder(Order.asc(orderByColumn));
				} else {
					cri.addOrder(Order.desc(orderByColumn));
				}

			} else {
				cri.addOrder(Order.asc("staffName"));
			}

			return cri;
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);

			throw ex;
		}
	}

	@Override
	public void deleteAllStaff(List<Staff> staffs) throws Exception {
		deleteAll(staffs);
	}

	@Override
	public List<Staff> getContractExpiredStaff() {
		List<Staff> results = new ArrayList<Staff>();

		try {
			Criteria cri = currentSession().createCriteria(Staff.class);

			cri.add(Restrictions.isNotNull("contractToDate"));
			cri.add(Restrictions.le("contractToDate",
					DateUtil.getDateAfter(StaticUtil.NOTIFY_CONTRACT_EXPIRED_BEFORE_DAY)));
			cri.add(Restrictions.eq("status", Values.STATUS_ACTIVE));

			results = cri.list();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@Override
	public List<Staff> getBirthDayNearlyStaff(int dayOfYear) {
		List<Staff> results = new ArrayList<Staff>();

		try {
			int _day = StaticUtil.NOTIFY_BIRTHDAY_BEFORE_DAY;

			Criteria cri = currentSession().createCriteria(Staff.class);

			cri.add(Restrictions.isNotNull("dateOfBirth"));
			cri.add(Restrictions.sqlRestriction(_buildBirthDayNearlyQuery(),
					new Object[] { dayOfYear + _day - 1, dayOfYear + _day + 1, dayOfYear, dayOfYear + _day },
					new Type[] { IntegerType.INSTANCE, IntegerType.INSTANCE, IntegerType.INSTANCE,
							IntegerType.INSTANCE }));
			cri.add(Restrictions.eq("status", Values.STATUS_ACTIVE));

			results = cri.list();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	private String _buildBirthDayNearlyQuery() {
		StringBuilder sb = new StringBuilder();

		sb.append("((MOD(YEAR(date_of_birth),4)=0 or MOD(YEAR(date_of_birth),400)=0)");
		sb.append(" and DAYOFYEAR(date_of_birth)>=? and DAYOFYEAR(date_of_birth)<=?)");
		sb.append(" or ((MOD(YEAR(date_of_birth),4)>0 and MOD(YEAR(date_of_birth),400)>0)");
		sb.append(" and DAYOFYEAR(date_of_birth)>=? and DAYOFYEAR(date_of_birth)<=?)");

		return sb.toString();
	}
}
