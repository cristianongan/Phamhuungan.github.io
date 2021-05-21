package com.evotek.qlns.dao;

import java.util.List;

import com.evotek.qlns.model.ApplyPosition;

public interface ApplyPositionDAO {
	List<ApplyPosition> getAll();
	void addOne(ApplyPosition applyPosition);
}
