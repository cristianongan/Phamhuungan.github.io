package com.evotek.qlns.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.evotek.qlns.dao.ApplyPositionDAO;
import com.evotek.qlns.model.ApplyPosition;

@Service
public class ApplyPositionServiceImpl implements com.evotek.qlns.service.ApplyPositionService{
	private static List<ApplyPosition> positionList;
	static {
		positionList = new ArrayList<ApplyPosition>();
	}
	private ApplyPositionDAO applyPositionDAO;
	
	
	public ApplyPositionServiceImpl(ApplyPositionDAO applyPositionDAO) {
		super();
		this.applyPositionDAO = applyPositionDAO;
		refresh();
	}

	@Override
	public List<ApplyPosition> getAll() {
		return positionList;
	}

	@Override
	public ApplyPosition getOneByID(long id) {
		for(int i=0 ; i< positionList.size();i++) {
			if(positionList.get(i).getId()==id) {
				return positionList.get(i);
			}
		}
		return null;
	}

	@Override
	public ApplyPosition getOneByName(String name) {
		for(int i=0 ; i< positionList.size();i++) {
			if(positionList.get(i).getName().equals(name)) {
				return positionList.get(i);
			}
		}
		return null;
	}
	private void refresh() {
		this.applyPositionDAO.getAll().forEach(c-> positionList.add(c));
	}

	@Override
	public void addOne(ApplyPosition applyPosition) {
		applyPositionDAO.addOne(applyPosition);
		refresh();
	}

}
