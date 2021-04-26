/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;

import com.evotek.qlns.dao.DocumentDAO;
import com.evotek.qlns.model.Document;
import com.evotek.qlns.util.CharPool;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author LinhLH
 */
public class DocumentDAOImpl extends AbstractDAO<Document> implements DocumentDAO {

	private static final Logger _log = LogManager.getLogger(DocumentDAOImpl.class);

	@Override
	public void delete(Collection<Document> documents) {
		deleteAll(documents);
	}

	@Override
	public List<Document> getAll() {
		return findAll(Document.class);
	}

	private NativeQuery<?> getDocumentAdvQuery(String documentContent, String documentNumber, Long documentType,
			String department, Date fromDate, Date toDate, String orderByColumn, String orderByType, boolean count)
			throws Exception {
		try {
			Session session = getCurrentSession();

			StringBuilder sb = new StringBuilder();

			if (count) {
				sb.append("select count(*)");
			} else {
				sb.append(" select a.document_id as documentId, a.user_id as ");
				sb.append(" userId, a.user_name as userName, a.create_date as ");
				sb.append(" createDate, a.modified_date as modifiedDate, ");
				sb.append(" a.document_name as documentContent, a.document_number as ");
				sb.append(" documentNumber, a.promulgation_dept as promulgationDept,");
				sb.append(" a.promulgation_date as promulgationDate,");
				sb.append(" a.document_type_id as documentTypeId, a.description as description, ");
				sb.append(" a.content as content, b.type_name as typeName ");
			}

			sb.append(" from document a join document_type b on a.document_type_id = b.document_type_id where 1=1 ");

			List<Object> params = new ArrayList<Object>();

			if (Validator.isNotNull(documentNumber)) {
				sb.append(" and lower(a.document_number) like ? ");

				params.add(QueryUtil.getFullStringParam(documentNumber.toLowerCase()));
			}

			if (Validator.isNotNull(documentContent)) {
				sb.append(" and lower(a.content) like ?");

				params.add(QueryUtil.getFullStringParam(documentContent.toLowerCase()));
			}

			if (Validator.isNotNull(documentType)) {
				sb.append(" and a.document_type_id = ? ");

				params.add(documentType);
			}

			if (Validator.isNotNull(department)) {
				sb.append(" and lower(a.promulgation_dept) like ? ");

				params.add(QueryUtil.getFullStringParam(department.toLowerCase()));
			}

			if (Validator.isNotNull(fromDate)) {
				sb.append(" and a.promulgation_date >= ?");

				params.add(fromDate);
			}

			if (Validator.isNotNull(toDate)) {
				sb.append(" and a.promulgation_date <= ?");

				params.add(toDate);
			}

			if (Validator.isNotNull(orderByColumn) && Validator.isNotNull(orderByType)) {
				sb.append(QueryUtil.addOrder(Document.class, orderByColumn, orderByType));
			} else {
				sb.append(" order by a.promulgation_date desc, a.document_number desc");
			}

			NativeQuery<?> sql = session.createSQLQuery(sb.toString());

			if (!count) {
				sql.addScalar("documentId", LongType.INSTANCE);
				sql.addScalar("userId", LongType.INSTANCE);
				sql.addScalar("userName", StringType.INSTANCE);
				sql.addScalar("createDate", TimestampType.INSTANCE);
				sql.addScalar("modifiedDate", TimestampType.INSTANCE);
				sql.addScalar("content", StringType.INSTANCE);
				sql.addScalar("documentNumber", StringType.INSTANCE);
				sql.addScalar("promulgationDept", StringType.INSTANCE);
				sql.addScalar("promulgationDate", DateType.INSTANCE);
				sql.addScalar("documentTypeId", LongType.INSTANCE);
				sql.addScalar("description", StringType.INSTANCE);
				sql.addScalar("content", StringType.INSTANCE);
				sql.addScalar("typeName", StringType.INSTANCE);
			}

			for (int i = 0; i < params.size(); i++) {
				sql.setParameter(i, params.get(i));
			}

			return sql;
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);

			throw ex;
		}
	}

	private NativeQuery<?> getDocumentBasicQuery(String textSearch, String orderByColumn, String orderByType,
			boolean count) throws Exception {
		try {
			Session session = getCurrentSession();

			StringBuilder sb = new StringBuilder();

			if (count) {
				sb.append("select count(*)");
			} else {
				sb.append(" select a.document_id as documentId, a.user_id as ");
				sb.append(" userId, a.user_name as userName, a.create_date as ");
				sb.append(" createDate, a.modified_date as modifiedDate, ");
				sb.append(" a.document_name as documentContent, a.document_number as ");
				sb.append(" documentNumber, a.promulgation_dept as promulgationDept,");
				sb.append(" a.promulgation_date as promulgationDate,");
				sb.append(" a.document_type_id as documentTypeId, a.description as description, ");
				sb.append(" a.content as content, b.type_name as typeName ");
			}

			sb.append(
					" from document a left join document_type b on a.document_type_id = b.document_type_id where 1=1 ");

			List<Object> params = new ArrayList<Object>();

			if (Validator.isNotNull(textSearch)) {
				sb.append(" and (lower(a.content) like ? ");
				sb.append(" or lower(a.document_number) like ? ");
				sb.append(" or lower(a.promulgation_dept) like ? ");
				sb.append(" or lower(b.type_name) like ?)");

				params.add(QueryUtil.getFullStringParam(textSearch.toLowerCase()));
				params.add(QueryUtil.getFullStringParam(textSearch.toLowerCase()));
				params.add(QueryUtil.getFullStringParam(textSearch.toLowerCase()));
				params.add(QueryUtil.getFullStringParam(textSearch.toLowerCase()));
			}

			if (Validator.isNotNull(orderByColumn) && Validator.isNotNull(orderByType)) {
				sb.append(QueryUtil.addOrder(Document.class, orderByColumn, orderByType));
			} else {
				sb.append(" order by a.promulgation_date desc, a.document_number desc");
			}

			NativeQuery<?> sql = session.createSQLQuery(sb.toString());

			if (!count) {
				sql.addScalar("documentId", LongType.INSTANCE);
				sql.addScalar("userId", LongType.INSTANCE);
				sql.addScalar("userName", StringType.INSTANCE);
				sql.addScalar("createDate", TimestampType.INSTANCE);
				sql.addScalar("modifiedDate", TimestampType.INSTANCE);
				sql.addScalar("content", StringType.INSTANCE);
				sql.addScalar("documentNumber", StringType.INSTANCE);
				sql.addScalar("promulgationDept", StringType.INSTANCE);
				sql.addScalar("promulgationDate", DateType.INSTANCE);
				sql.addScalar("documentTypeId", LongType.INSTANCE);
				sql.addScalar("description", StringType.INSTANCE);
				sql.addScalar("content", StringType.INSTANCE);
				sql.addScalar("typeName", StringType.INSTANCE);
			}

			for (int i = 0; i < params.size(); i++) {
				sql.setParameter(i, params.get(i));
			}

			return sql;
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);

			throw ex;
		}
	}

	@Override
	public List<Document> getDocumentByI_N_C(Long documentId, String documentNumber, String content) {
		List<Document> results = new ArrayList<Document>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Document> criteria = builder.createQuery(Document.class);

			Root<Document> root = criteria.from(Document.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(builder.like(builder.lower(root.get("documentNumber")),
					QueryUtil.getFullStringParam(documentNumber, true), CharPool.EXCLAMATION));

			predicates.add(builder.like(builder.lower(root.get("content")), QueryUtil.getFullStringParam(content, true),
					CharPool.EXCLAMATION));

			if (Validator.isNotNull(documentId)) {
				predicates.add(builder.notEqual(root.get("documentId"), documentId));

			}

			criteria.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

			results = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}

	@Override
	public int getDocumentByIdListCount(List<Long> idList) {
		int result = 0;

		try {
			NativeQuery<?> q = getDocumentByIdListQuery(idList, null, null, true);

			result = ((BigInteger) q.uniqueResult()).intValue();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return result;
	}

	private NativeQuery<?> getDocumentByIdListQuery(List<Long> idList, String orderByColumn, String orderByType,
			boolean count) throws Exception {
		try {
			Session session = getCurrentSession();

			StringBuilder sb = new StringBuilder();

			if (count) {
				sb.append("select count(*)");
			} else {
				sb.append(" select a.document_id as documentId, a.user_id as ");
				sb.append(" userId, a.user_name as userName, a.create_date as ");
				sb.append(" createDate, a.modified_date as modifiedDate, ");
				sb.append(" a.document_name as documentContent, a.document_number as ");
				sb.append(" documentNumber, a.promulgation_dept as promulgationDept,");
				sb.append(" a.promulgation_date as promulgationDate,");
				sb.append(" a.document_type_id as documentTypeId, a.description as description, ");
				sb.append(" a.content as content, b.type_name as typeName ");
			}

			sb.append(
					" from document a left join document_type b on a.document_type_id = b.document_type_id where 1=1 ");

			boolean addParam = false;

			if (!idList.isEmpty()) {
				if (Validator.isNull(idList.get(0))) {
					sb.append(" and a.document_type_id is null");
				} else {
					sb.append(" and a.document_type_id in (:idList)");

					addParam = true;
				}
			}

			if (Validator.isNotNull(orderByColumn) && Validator.isNotNull(orderByType)) {
				sb.append(QueryUtil.addOrder(Document.class, orderByColumn, orderByType));
			} else {
				sb.append(" order by a.promulgation_date desc, a.document_number desc");
			}

			System.out.println(sb.toString());

			NativeQuery<?> sql = session.createNativeQuery(sb.toString());

			if (!count) {
				sql.addScalar("documentId", LongType.INSTANCE);
				sql.addScalar("userId", LongType.INSTANCE);
				sql.addScalar("userName", StringType.INSTANCE);
				sql.addScalar("createDate", TimestampType.INSTANCE);
				sql.addScalar("modifiedDate", TimestampType.INSTANCE);
				sql.addScalar("content", StringType.INSTANCE);
				sql.addScalar("documentNumber", StringType.INSTANCE);
				sql.addScalar("promulgationDept", StringType.INSTANCE);
				sql.addScalar("promulgationDate", DateType.INSTANCE);
				sql.addScalar("documentTypeId", LongType.INSTANCE);
				sql.addScalar("description", StringType.INSTANCE);
				sql.addScalar("content", StringType.INSTANCE);
				sql.addScalar("typeName", StringType.INSTANCE);
			}

			if (addParam) {
				sql.setParameterList("idList", idList);
			}

			return sql;
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);

			throw ex;
		}
	}

	@Override
	public List<Document> getDocumentByN(String documentNumber) {
		List<Document> results = new ArrayList<Document>();

		try {
			Session session = getCurrentSession();

			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Document> criteria = builder.createQuery(Document.class);

			Root<Document> root = criteria.from(Document.class);

			Predicate documentNumberPre = builder.like(builder.lower(root.get("documentNumber")),
					QueryUtil.getFullStringParam(documentNumber, true), CharPool.EXCLAMATION);

			criteria.select(root).where(documentNumberPre);

			results = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}

		return results;
	}

	@Override
	public int getDocumentCountAdv(String documentContent, String documentNumber, Long documentType, String department,
			Date fromDate, Date toDate) {
		int result = 0;

		try {
			NativeQuery<?> q = getDocumentAdvQuery(documentContent, documentNumber, documentType, department, fromDate,
					toDate, null, null, true);

			result = ((BigInteger) q.uniqueResult()).intValue();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return result;
	}

	@Override
	public int getDocumentCountBasic(String textSearch) {
		int result = 0;

		try {
			NativeQuery<?> q = getDocumentBasicQuery(textSearch, null, null, true);

			result = ((BigInteger) q.uniqueResult()).intValue();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getDocumentListAdv(String documentContent, String documentNumber, Long documentType,
			String department, Date fromDate, Date toDate, int firstResult, int maxResult, String orderByColumn,
			String orderByType) {
		List<Document> results = new ArrayList<Document>();

		try {
			NativeQuery<?> q = getDocumentAdvQuery(documentContent, documentNumber, documentType, department, fromDate,
					toDate, orderByColumn, orderByType, false);

			if (firstResult >= 0 && maxResult > 0) {
				q.setFirstResult(firstResult);
				q.setMaxResults(maxResult);
			}

			q.addSynchronizedEntityClass(Document.class);

			results = (List<Document>) q.list();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getDocumentListBasic(String textSearch, int firstResult, int maxResult, String orderByColumn,
			String orderByType) {
		List<Document> results = new ArrayList<Document>();

		try {
			NativeQuery<?> q = getDocumentBasicQuery(textSearch, orderByColumn, orderByType, false);

			if (firstResult >= 0 && maxResult > 0) {
				q.setFirstResult(firstResult);
				q.setMaxResults(maxResult);
			}

			q.addSynchronizedEntityClass(Document.class);

			results = (List<Document>) q.list();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getDocumentListByIdList(List<Long> idList, int firstResult, int maxResult,
			String orderByColumn, String orderByType) {
		List<Document> results = new ArrayList<Document>();

		try {
			NativeQuery<?> q = getDocumentByIdListQuery(idList, orderByColumn, orderByType, false);

			if (firstResult >= 0 && maxResult > 0) {
				q.setFirstResult(firstResult);
				q.setMaxResults(maxResult);
			}

			q.addSynchronizedEntityClass(Document.class);

			results = (List<Document>) q.list();

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	@Override
	public void saveOrUpdate(Collection<Document> documents) {
		saveOrUpdateAll(documents);
	}
}
