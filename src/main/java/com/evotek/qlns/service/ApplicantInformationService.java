package com.evotek.qlns.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.evotek.qlns.model.ApplicantInformation;
import com.evotek.qlns.model.ApplyPosition;

public interface ApplicantInformationService {
	public void addApplicant(ApplicantInformation applicantInformation);
	public Page<ApplicantInformation> getListByPage(Pageable pageable, String key,
			ApplyPosition applyPosition,String exp,Date startDate,Date endDate);
	public ApplicantInformation getApplicantInformation(long id);
	public void updateApplicantInformation(ApplicantInformation applicantInformation);
	public void deleteApplicantInformation(ApplicantInformation applicantInformation);
	public void deleteManyApplicantInformation(Collection<ApplicantInformation> list);
}
