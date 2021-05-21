package com.evotek.qlns.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evotek.qlns.dao.ApplicantInformationDAO;
import com.evotek.qlns.model.ApplicantInformation;
import com.evotek.qlns.model.ApplyPosition;

@Service
public class ApplicantInformationServiceImpl implements com.evotek.qlns.service.ApplicantInformationService{
	private ApplicantInformationDAO applicantInformationDAO;
	
	
	public ApplicantInformationServiceImpl(ApplicantInformationDAO applicantInformationDAO) {
		super();
		this.applicantInformationDAO = applicantInformationDAO;
	}

	@Override
	public void addApplicant(ApplicantInformation applicantInformation) {
		applicantInformationDAO.saveOne(applicantInformation);
	}

	@Override
	public Page<ApplicantInformation> getListByPage(Pageable pageable, String key, ApplyPosition applyPosition,
			String exp, Date startDate, Date endDate) {
		long count = applicantInformationDAO.count(key, applyPosition, exp, startDate, endDate);
		List<ApplicantInformation> content = applicantInformationDAO.getList(pageable, key, applyPosition, exp, startDate, endDate);
		Page<ApplicantInformation> result = new PageImpl<ApplicantInformation>(content,pageable,count);
		return result;
	}


	@Override
	public ApplicantInformation getApplicantInformation(long id) {
		return applicantInformationDAO.getOne(id);
	}

	@Override
	public void updateApplicantInformation(ApplicantInformation applicantInformation) {
		applicantInformationDAO.updateOne(applicantInformation);
	}

	@Override
	public void deleteApplicantInformation(ApplicantInformation applicantInformation) {
		applicantInformationDAO.deleteOne(applicantInformation);
	}

	@Override
	public void deleteManyApplicantInformation(Collection<ApplicantInformation> list) {
		applicantInformationDAO.deleteMany(list);
	}




}
