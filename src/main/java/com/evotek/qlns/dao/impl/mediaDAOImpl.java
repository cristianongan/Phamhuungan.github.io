package com.evotek.qlns.dao.impl;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.evotek.qlns.dao.MediaDAO;
import com.evotek.qlns.model.MediaCustom;

@Repository
@Transactional
public class mediaDAOImpl extends AbstractDAO<MediaCustom> implements MediaDAO{

	@Override
	public MediaCustom getOne(long id) {
		
		return findById(MediaCustom.class, id);
	}

}
