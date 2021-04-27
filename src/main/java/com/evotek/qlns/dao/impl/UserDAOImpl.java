package com.evotek.qlns.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import com.evotek.qlns.dao.UserDAO;
import com.evotek.qlns.model.User;
import com.evotek.qlns.util.CharPool;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author hungnt1 LinhLH2 fixed
 */
public class UserDAOImpl extends AbstractDAO<User> implements UserDAO {

	@Override
	public List<User> getListUsers() {
		List<User> results = new ArrayList<User>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<User> criteria = builder.createQuery(User.class);

			Root<User> root = criteria.from(User.class);

			criteria.select(root).where(builder.equal(root.get("status"), Values.STATUS_ACTIVE));

			results = session.createQuery(criteria).getResultList();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@Override
	public User getNewUser() {
		return new User();
	}

	@Override
	public int getCountAllUsers() throws Exception {
		int result = 0;

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

			Root<User> root = criteria.from(User.class);

			criteria.select(builder.count(root)).where(builder.equal(root.get("status"), Values.STATUS_DEACTIVE));

			Long count = (Long) session.createQuery(criteria).uniqueResult();

			if (count != null) {
				result = count.intValue();
			}
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return result;
	}

	@Override
	public List<User> getAllUsers() throws Exception {
		List<User> results = new ArrayList<User>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<User> criteria = builder.createQuery(User.class);

			Root<User> root = criteria.from(User.class);

			criteria.select(root).where(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			results = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}

	@Override
	public User getUserById(Long userId) {
		User user = null;

		try {
			user = findById(User.class, userId);

			if (Values.STATUS_DEACTIVE.equals(user.getStatus())) {
				return null;
			}
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return user;
	}

	@Override
	public User getUserByUserName(String userName) throws Exception {
		User user = null;

		try {
			if (Validator.isNull(userName)) {
				return user;
			}

			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<User> criteria = builder.createQuery(User.class);

			Root<User> root = criteria.from(User.class);

			criteria.select(root).where(
					builder.equal(builder.lower(root.get("userName")), QueryUtil.getStringParam(userName, true)),
					builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			List<User> results = session.createQuery(criteria).getResultList();

			if (!results.isEmpty()) {
				user = results.get(0);
			}

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return user;
	}

	@Override
	public List<User> getUsersLikeUserName(String value) throws Exception {
		List<User> results = new ArrayList<User>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<User> criteria = builder.createQuery(User.class);

			Root<User> root = criteria.from(User.class);

			List<Predicate> predicates = new ArrayList<>();

			if (Validator.isNotNull(value)) {
				predicates.add(builder.like(builder.lower(root.get("userName")),
						QueryUtil.getFullStringParam(value, true), CharPool.EXCLAMATION));
			}

			predicates.add(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			criteria.select(root).where(builder.or(predicates.toArray(new Predicate[predicates.size()])));

			results = session.createQuery(criteria).getResultList();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@Override
	public List<User> getUsersLikeLastname(String value) throws Exception {
		List<User> results = new ArrayList<User>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<User> criteria = builder.createQuery(User.class);

			Root<User> root = criteria.from(User.class);

			List<Predicate> predicates = new ArrayList<>();

			if (Validator.isNotNull(value)) {
				predicates.add(builder.like(builder.lower(root.get("lastName")),
						QueryUtil.getFullStringParam(value, true), CharPool.EXCLAMATION));
			}

			predicates.add(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			criteria.select(root).where(builder.or(predicates.toArray(new Predicate[predicates.size()])));

			results = session.createQuery(criteria).getResultList();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@Override
	public List<User> getUsersLikeEmail(String value) throws Exception {
		List<User> results = new ArrayList<User>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<User> criteria = builder.createQuery(User.class);

			Root<User> root = criteria.from(User.class);

			List<Predicate> predicates = new ArrayList<>();

			if (Validator.isNotNull(value)) {
				predicates.add(builder.like(builder.lower(root.get("email")),
						QueryUtil.getFullStringParam(value, true), CharPool.EXCLAMATION));
			}

			predicates.add(builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));

			criteria.select(root).where(builder.or(predicates.toArray(new Predicate[predicates.size()])));

			results = session.createQuery(criteria).getResultList();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

//    public List<User> getUsers(String userName, String email, Long gender,
//            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
//            String mobile, String account, Long status, int firstResult,
//            int maxResult, String orderByColumn, String orderByType) {
//        List<User> users = new ArrayList<User>();
//
//        try {
//            SQLQuery sql = getUsersQuery(userName, email, gender, birthPlace,
//                    birthdayFrom, birthdayTo, phone, mobile, account, status,
//                    orderByColumn, orderByType, false);
//
//            if (firstResult >= 0 && maxResult > 0) {
//                sql.setFirstResult(firstResult);
//                sql.setMaxResults(maxResult);
//            }
//
//            sql.setResultTransformer(Transformers.aliasToBean(User.class));
//
//            users = (List<User>) sql.list();
//        } catch (Exception ex) {
//            _log.error(ex.getMessage(), ex);
//        }
//
//        return users;
//    }
//
//    public int getUsersCount(String userName, String email, Long gender,
//            String birthPlace, Date birthdayFrom, Date birthdayTo, String phone,
//            String mobile, String account, Long status) {
//
//        int result = 0;
//
//        try {
//            SQLQuery sql = getUsersQuery(userName, email, gender, birthPlace,
//                    birthdayFrom, birthdayTo, phone, mobile, account, status,
//                    null, null, true);
//
//            result = ((BigInteger) sql.uniqueResult()).intValue();
//        } catch (Exception ex) {
//            _log.error(ex.getMessage(), ex);
//        }
//
//        return result;
//    }
//
//    public List<User> getUsers(String keyword, int firstResult, int maxResult,
//            String orderByColumn, String orderByType) {
//        List<User> users = new ArrayList<User>();
//
//        try {
//            SQLQuery sql = getUsersQuery(keyword, orderByColumn, orderByType,
//                    false);
//
//            if (firstResult >= 0 && maxResult > 0) {
//                sql.setFirstResult(firstResult);
//                sql.setMaxResults(maxResult);
//            }
//
//            sql.setResultTransformer(Transformers.aliasToBean(User.class));
//
//            users = (List<User>) sql.list();
//        } catch (Exception ex) {
//            _log.error(ex.getMessage(), ex);
//        }
//
//        return users;
//    }
//
//    public int getUsersCount(String keyword) {
//        int result = 0;
//
//        try {
//            SQLQuery sql = getUsersQuery(keyword, null, null, true);
//
//            result = ((BigInteger) sql.uniqueResult()).intValue();
//        } catch (Exception ex) {
//            _log.error(ex.getMessage(), ex);
//        }
//
//        return result;
//    }

	@Override
	public List<User> getUsers(String userName, String email, Long gender, String birthPlace, Date birthdayFrom,
			Date birthdayTo, String phone, String mobile, String account, Long status, int firstResult, int maxResult,
			String orderByColumn, String orderByType) {
		List<User> users = new ArrayList<User>();

		try {
			Criteria cri = getUsersCriteria(userName, email, gender, birthPlace, birthdayFrom, birthdayTo, phone,
					mobile, account, status, orderByColumn, orderByType);

			if (firstResult >= 0 && maxResult > 0) {
				cri.setFirstResult(firstResult);
				cri.setMaxResults(maxResult);
			}

			users = cri.list();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return users;
	}

	@Override
	public int getUsersCount(String userName, String email, Long gender, String birthPlace, Date birthdayFrom,
			Date birthdayTo, String phone, String mobile, String account, Long status) {

		int result = 0;

		try {
			Criteria cri = getUsersCriteria(userName, email, gender, birthPlace, birthdayFrom, birthdayTo, phone,
					mobile, account, status, null, null);

			cri.setProjection(Projections.rowCount());

			result = ((Integer) cri.uniqueResult()).intValue();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return result;
	}

	@Override
	public List<User> getUsers(String keyword, int firstResult, int maxResult, String orderByColumn,
			String orderByType) {
		List<User> results = new ArrayList<User>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<User> criteria = builder.createQuery(User.class);

			Root<User> root = criteria.from(User.class);
			
			Predicate predicate = getUsersPredicate(builder, root, keyword);
			
			criteria.select(root);

			if (Validator.isNotNull(predicate)) {
				criteria.where(predicate);
			}
			
			Query<User> q = session.createQuery(criteria);

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
				criteria.orderBy(builder.asc(root.get("modifiedDate")));
			}
			
			results = q.getResultList();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@Override
	public int getUsersCount(String keyword) {
		int result = 0;

		try {
			Criteria cri = getUsersCriteria(keyword, null, null);

			cri.setProjection(Projections.rowCount());

			result = (Integer) cri.uniqueResult();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return result;
	}

	private Predicate getUsersPredicate(CriteriaBuilder builder, Root<User> root, String keyword)
			throws Exception {
		Predicate totalPre = null;
		
		try {
			List<Predicate> predicates = new ArrayList<>();
			
			if (Validator.isNotNull(keyword)) {
				predicates.add(builder.like(builder.lower(root.get("firstName")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("middleName")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("lastName")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("email")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("birthPlace")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("phone")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("mobile")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("userName")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
				predicates.add(builder.like(builder.lower(root.get("address")),
						QueryUtil.getFullStringParam(keyword, true), CharPool.EXCLAMATION));
			}
			
			if (!predicates.isEmpty()) {
				Predicate keywordPre = builder.or(predicates.toArray(new Predicate[predicates.size()]));
				
				totalPre = builder.and(keywordPre, builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE));
			} else {
				totalPre = builder.notEqual(root.get("status"), Values.STATUS_DEACTIVE);
			}
			
			
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
		
		return totalPre;
	}

	private Criteria getUsersCriteria(String userName, String email, Long gender, String birthPlace, Date birthdayFrom,
			Date birthdayTo, String phone, String mobile, String account, Long status, String orderByColumn,
			String orderByType) throws Exception {
		try {
			Criteria cri = currentSession().createCriteria(User.class);

			Conjunction conjunction = Restrictions.conjunction();

			if (Validator.isNotNull(userName)) {
				Disjunction disjunction = Restrictions.disjunction();

				disjunction.add(LikeCriterionMaker.ilike("firstName", userName, MatchMode.ANYWHERE));
				disjunction.add(LikeCriterionMaker.ilike("middleName", userName, MatchMode.ANYWHERE));
				disjunction.add(LikeCriterionMaker.ilike("lastName", userName, MatchMode.ANYWHERE));

				conjunction.add(disjunction);
			}

			if (Validator.isNotNull(email)) {
				conjunction.add(LikeCriterionMaker.ilike("email", email, MatchMode.ANYWHERE));
			}

			if (Validator.isNotNull(gender)) {
				conjunction.add(Restrictions.eq("gender", gender));
			}

			if (Validator.isNotNull(birthPlace)) {
				conjunction.add(LikeCriterionMaker.ilike("birthPlace", birthPlace, MatchMode.ANYWHERE));
			}

			if (Validator.isNotNull(birthdayFrom)) {
				conjunction.add(Restrictions.ge("dateOfBirth", birthdayFrom));
			}

			if (Validator.isNotNull(birthdayTo)) {
				conjunction.add(Restrictions.le("dateOfBirth", birthdayTo));
			}

			if (Validator.isNotNull(phone)) {
				conjunction.add(LikeCriterionMaker.ilike("phone", phone, MatchMode.ANYWHERE));
			}

			if (Validator.isNotNull(mobile)) {
				conjunction.add(LikeCriterionMaker.ilike("mobile", mobile, MatchMode.ANYWHERE));
			}

			if (Validator.isNotNull(account)) {
				conjunction.add(LikeCriterionMaker.ilike("account", account, MatchMode.ANYWHERE));
			}

			if (Validator.isNotNull(status)) {
				conjunction.add(Restrictions.eq("status", status));
			} else {
				conjunction.add(Restrictions.ne("status", Values.STATUS_DEACTIVE));
			}

			cri.add(conjunction);

			if (Validator.isNotNull(orderByColumn)) {
				if (StringPool.ASC.equalsIgnoreCase(orderByType)) {
					cri.addOrder(Order.asc(orderByColumn));
				} else {
					cri.addOrder(Order.desc(orderByColumn));
				}

			} else {
				cri.addOrder(Order.desc("modifiedDate"));
			}

			return cri;
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);

			throw ex;
		}
	}

//	public SQLQuery getUsersQuery(String userName, String email, Long gender, String birthPlace, Date birthdayFrom,
//			Date birthdayTo, String phone, String mobile, String account, Long status, String orderByColumn,
//			String orderByType, boolean count) throws Exception {
//		try {
//			Session session = currentSession();
//
//			StringBuilder sb = new StringBuilder();
//
//			List params = new ArrayList();
//
//			if (count) {
//				sb.append("select count(*)");
//			} else {
//				sb.append(" SELECT a.user_id as userId, a.user_name as userName, ");
//				sb.append(" a.password as password, a.email as email, a.phone ");
//				sb.append(" as phone, a.mobile as mobile, a.gender as ");
//				sb.append(" gender, a.date_of_birth as dateOfBirth, a.birth_place ");
//				sb.append(" as birthPlace, a.address as address, ");
//				sb.append(" a.create_date as createDate, a.modified_date as ");
//				sb.append(" modifiedDate, a.first_name as firstName, a.last_name as ");
//				sb.append(" lastName, a.status as status, a.description as ");
//				sb.append(" description");
//
//			}
//
//			sb.append(" FROM user_ a");
//			sb.append(" where 1 = 1 ");
//
//			if (Validator.isNotNull(userName)) {
//				sb.append(" and lower(a.first_name) like ? escape '\\' or lower(a.last_name) like ? escape '\\'");
//
//				params.add(QueryUtil.getFullStringParam(userName.toLowerCase()));
//				params.add(QueryUtil.getFullStringParam(userName.toLowerCase()));
//			}
//
//			if (Validator.isNotNull(email)) {
//				sb.append(" and lower(a.email) like ? escape '\\'");
//
//				params.add(QueryUtil.getFullStringParam(email.toLowerCase()));
//			}
//
//			if (Validator.isNotNull(gender)) {
//				sb.append(" and a.gender = ? ");
//
//				params.add(gender);
//			}
//
//			if (Validator.isNotNull(birthPlace)) {
//				sb.append(" and lower(a.birth_place) like ? escape '\\'");
//
//				params.add(QueryUtil.getFullStringParam(birthPlace.toLowerCase()));
//			}
//
//			if (Validator.isNotNull(birthdayFrom)) {
//				sb.append(" and a.date_of_birth >= ?");
//
//				params.add(birthdayFrom);
//			}
//
//			if (Validator.isNotNull(birthdayTo)) {
//				sb.append(" and a.date_of_birth <= ?");
//
//				params.add(birthdayTo);
//			}
//
//			if (Validator.isNotNull(phone)) {
//				sb.append(" and lower(a.phone) like ? escape '\\'");
//
//				params.add(QueryUtil.getFullStringParam(mobile.toLowerCase()));
//				params.add(QueryUtil.getFullStringParam(mobile.toLowerCase()));
//			}
//
//			if (Validator.isNotNull(mobile)) {
//				sb.append(" and lower(a.mobile) like ? escape '\\'");
//
//				params.add(QueryUtil.getFullStringParam(mobile.toLowerCase()));
//				params.add(QueryUtil.getFullStringParam(mobile.toLowerCase()));
//			}
//
//			if (Validator.isNotNull(account)) {
//				sb.append(" and lower(a.user_name) like ? escape '\\'");
//
//				params.add(QueryUtil.getFullStringParam(account.toLowerCase()));
//			}
//
//			if (Validator.isNotNull(status)) {
//				sb.append(" and a.status = ? ");
//
//				params.add(status);
//			} else {
//				sb.append(" and a.status != 0 ");
//			}
//
//			if (Validator.isNotNull(orderByColumn) && Validator.isNotNull(orderByType)) {
//				sb.append(QueryUtil.addOrder(User.class, orderByColumn, orderByType));
//			} else {
//				sb.append(" order by a.modified_date desc");
//			}
//
//			SQLQuery sql = session.createSQLQuery(sb.toString());
//
//			if (!count) {
//				sql.addScalar("userId", LongType.INSTANCE);
//				sql.addScalar("userName", StringType.INSTANCE);
//				sql.addScalar("password", StringType.INSTANCE);
//				sql.addScalar("email", StringType.INSTANCE);
//				sql.addScalar("phone", StringType.INSTANCE);
//				sql.addScalar("mobile", StringType.INSTANCE);
//				sql.addScalar("gender", LongType.INSTANCE);
//				sql.addScalar("dateOfBirth", DateType.INSTANCE);
//				sql.addScalar("birthPlace", StringType.INSTANCE);
//				sql.addScalar("address", StringType.INSTANCE);
//				sql.addScalar("createDate", DateType.INSTANCE);
//				sql.addScalar("modifiedDate", DateType.INSTANCE);
//				sql.addScalar("firstName", StringType.INSTANCE);
//				sql.addScalar("lastName", StringType.INSTANCE);
//				sql.addScalar("status", LongType.INSTANCE);
//				sql.addScalar("description", StringType.INSTANCE);
//			}
//
//			for (int i = 0; i < params.size(); i++) {
//				sql.setParameter(i, params.get(i));
//			}
//
//			return sql;
//
//		} catch (Exception ex) {
//			_log.error(ex.getMessage(), ex);
//
//			throw ex;
//		}
//	}
//
//	public SQLQuery getUsersQuery(String keyword, String orderByColumn, String orderByType, boolean count)
//			throws Exception {
//		try {
//			Session session = currentSession();
//
//			StringBuilder sb = new StringBuilder();
//
//			List params = new ArrayList();
//
//			if (count) {
//				sb.append("select count(*)");
//			} else {
//				sb.append(" SELECT a.user_id as userId, a.user_name as userName, ");
//				sb.append(" a.password as password, a.email as email, a.phone ");
//				sb.append(" as phone, a.mobile as mobile, a.gender as ");
//				sb.append(" gender, a.date_of_birth as dateOfBirth, a.birth_place ");
//				sb.append(" as birthPlace, a.address as address, ");
//				sb.append(" a.create_date as createDate, a.modified_date as ");
//				sb.append(" modifiedDate, a.first_name as firstName, a.last_name as ");
//				sb.append(" lastName, a.status as status, a.description as ");
//				sb.append(" description");
//			}
//
//			sb.append(" FROM user_ a");
//			sb.append(" where 1 = 1 and a.status != 0");
//
//			if (Validator.isNotNull(keyword)) {
//				String _keyword = QueryUtil.getFullStringParam(keyword.toLowerCase());
//
//				sb.append(" and (lower(a.first_name) like ? escape '\\'");
//				sb.append(" or lower(a.last_name) like ? escape '\\'");
//				sb.append(" or lower(a.email) like ? escape '\\'");
//				sb.append(" or lower(a.birth_place) like ? escape '\\'");
//				sb.append(" or lower(a.mobile) like ? escape '\\'");
//				sb.append(" or lower(a.phone) like ? escape '\\'");
//				sb.append(" or lower(a.user_name) like ? escape '\\'");
//				sb.append(")");
//
//				params.add(_keyword);
//				params.add(_keyword);
//				params.add(_keyword);
//				params.add(_keyword);
//				params.add(_keyword);
//				params.add(_keyword);
//				params.add(_keyword);
//			}
//
//			if (Validator.isNotNull(orderByColumn) && Validator.isNotNull(orderByType)) {
//				sb.append(QueryUtil.addOrder(User.class, orderByColumn, orderByType));
//			} else {
//				sb.append(" order by a.modified_date desc");
//			}
//
//			SQLQuery sql = session.createSQLQuery(sb.toString());
//
//			if (!count) {
//				sql.addScalar("userId", LongType.INSTANCE);
//				sql.addScalar("userName", StringType.INSTANCE);
//				sql.addScalar("password", StringType.INSTANCE);
//				sql.addScalar("email", StringType.INSTANCE);
//				sql.addScalar("phone", StringType.INSTANCE);
//				sql.addScalar("mobile", StringType.INSTANCE);
//				sql.addScalar("gender", LongType.INSTANCE);
//				sql.addScalar("dateOfBirth", DateType.INSTANCE);
//				sql.addScalar("birthPlace", StringType.INSTANCE);
//				sql.addScalar("address", StringType.INSTANCE);
//				sql.addScalar("createDate", DateType.INSTANCE);
//				sql.addScalar("modifiedDate", DateType.INSTANCE);
//				sql.addScalar("firstName", StringType.INSTANCE);
//				sql.addScalar("lastName", StringType.INSTANCE);
//				sql.addScalar("status", LongType.INSTANCE);
//				sql.addScalar("description", StringType.INSTANCE);
//			}
//
//			for (int i = 0; i < params.size(); i++) {
//				sql.setParameter(i, params.get(i));
//			}
//
//			return sql;
//
//		} catch (Exception ex) {
//			_log.error(ex.getMessage(), ex);
//
//			throw ex;
//		}
//	}

	@Override
	public List<User> getUsersByI_UN(Long userId, String userName) {
		List<User> results = new ArrayList<User>();

		try {
			Criteria cri = currentSession().createCriteria(User.class);

			cri.add(LikeCriterionMaker.ilike("userName", userName, MatchMode.EXACT));

			if (Validator.isNotNull(userId)) {
				cri.add(Restrictions.ne("userId", userId));
			}

			results = cri.list();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}

	@Override
	public List<User> getUsersByI_E(Long userId, String email) {
		List<User> results = new ArrayList<User>();

		try {
			Criteria cri = currentSession().createCriteria(User.class);

			cri.add(LikeCriterionMaker.ilike("email", email, MatchMode.EXACT));

			if (Validator.isNotNull(userId)) {
				cri.add(Restrictions.ne("userId", userId));
			}

			results = cri.list();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}

	private static final Logger _log = LogManager.getLogger(UserDAOImpl.class);

	@Override
	public Map<String, Long> createMapUser() {
		Map<String, Long> map = new HashMap<String, Long>();
		Session session = null;
		try {
			session = currentSession();
			Criteria criteria = session.createCriteria(User.class);
			List<User> results = criteria.list();
			for (User user : results) {
				map.put(user.getUserName().toLowerCase(), user.getUserId());
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		} finally {
			return map;
		}
	}
}
