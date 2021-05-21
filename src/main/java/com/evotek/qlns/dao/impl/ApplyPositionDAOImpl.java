package com.evotek.qlns.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.evotek.qlns.dao.ApplyPositionDAO;
import com.evotek.qlns.model.ApplyPosition;

@Repository
@Transactional
public class ApplyPositionDAOImpl extends AbstractDAO<ApplyPosition> implements ApplyPositionDAO{
	private static final Logger _log = LogManager.getLogger(CategoryDAOImpl.class);
	
	@Override
	public List<ApplyPosition> getAll() {
		return findAll(ApplyPosition.class);
	}

	@Override
	public void addOne(ApplyPosition applyPosition) {
		save(applyPosition);
	}
	

}
