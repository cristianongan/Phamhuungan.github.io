package com.evotek.qlns.service;


import java.util.List;

import com.evotek.qlns.model.ApplyPosition;

public interface ApplyPositionService {
	public List<ApplyPosition> getAll();
	public ApplyPosition getOneByID(long id);
	public ApplyPosition getOneByName(String name);
	public void addOne(ApplyPosition applyPosition);
	
}
