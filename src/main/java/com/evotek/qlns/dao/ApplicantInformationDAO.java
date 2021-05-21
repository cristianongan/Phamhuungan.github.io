package com.evotek.qlns.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.evotek.qlns.model.ApplicantInformation;
import com.evotek.qlns.model.ApplyPosition;

public interface ApplicantInformationDAO {
	List<ApplicantInformation> getList(Pageable pageable, String key,
			ApplyPosition applyPosition,String exp,Date startDate,Date endDate);
	long count( String key,
			ApplyPosition applyPosition,String exp,Date startDate,Date endDate);
	public void deleteOne(long id);
	public void deleteOne(ApplicantInformation applicantInformation);
	public void deleteMany(Collection<ApplicantInformation> list);
	public void updateOne(ApplicantInformation applicantInformation);
	public void saveOne(ApplicantInformation applicantInformation);
	ApplicantInformation getOne(long id);
}
