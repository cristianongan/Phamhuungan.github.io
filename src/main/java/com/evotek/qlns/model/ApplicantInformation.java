package com.evotek.qlns.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.zkoss.util.media.Media;


@Entity
@Table(name="applicant_information")
@SqlResultSetMappings({
	@SqlResultSetMapping(name = "applicantInformationMapping",classes = {
			@ConstructorResult(targetClass = ApplicantInformation.class,columns = {
					@ColumnResult(name = "id",type=Long.class),
					@ColumnResult(name = "name",type=String.class),
					@ColumnResult(name = "applyPositionId",type=Integer.class),
					@ColumnResult(name = "applyPositionName",type=String.class),
					@ColumnResult(name = "applyDate",type=Date.class),
					@ColumnResult(name = "status",type=String.class),
					@ColumnResult(name = "exp",type=String.class),
					@ColumnResult(name = "interviewResult",type=String.class),
			})
	})
})
public class ApplicantInformation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5L;
	private long id;
	private String name;
	private String mobilePhone;
	private ApplyPosition applyPosition;
	private String email;
	private Date applyDate;
	private String status;
	private String exp;
	private String jobInformationSource;
	private MediaCustom media;
	private String interviewResult;
	private String note;
	private String cvLink;
	public ApplicantInformation( String name, String mobilePhone, ApplyPosition applyPosition, String email,
			String exp, String jobInformationSource,Media media,String interviewResult,String note, String cvLink,Date applyDate) {
		this.name = name;
		this.mobilePhone = mobilePhone;
		this.applyPosition = applyPosition;
		this.email = email;
		this.exp = exp;
		this.jobInformationSource = jobInformationSource;
		this.applyDate=applyDate;
		if(media!=null)
			this.media = new MediaCustom(media);
		this.interviewResult=interviewResult;
		this.note=note;
		this.cvLink=cvLink;
	}
//	public ApplicantInformation(long id, String name, String mobilePhone, ApplyPosition applyPosition, String email,
//			Date applyDate, String status, String exp, String jobInformationSource,String interviewResult,String note, String cvLink,
//			Media media) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.mobilePhone = mobilePhone;
//		this.applyPosition = applyPosition;
//		this.email = email;
//		this.applyDate = applyDate;
//		this.status = status;
//		this.exp = exp;
//		this.jobInformationSource = jobInformationSource;
//		this.interviewResult=interviewResult;
//		this.note=note;
//		this.cvLink=cvLink;
//		this.media = new MediaCustom(media);
//	}
		
	public ApplicantInformation(long id, String name, int applyPositionId,String applyPositionName,Date applyDate, String status, String exp,
			String interviewResult) {
		super();
		this.id = id;
		this.name = name;
		this.applyPosition = new ApplyPosition(applyPositionId, applyPositionName);
		this.status = status;
		this.exp = exp;
		this.interviewResult = interviewResult;
		this.applyDate=applyDate;
		
	}
	
	public ApplicantInformation(long id, String name, String mobilePhone,int applyPositionId,String applyPositionName, String email,
			Date applyDate, String status, String exp, String jobInformationSource, long mediaId,String mediaType,String mediaName,
			byte[] mediaContent, 
			String interviewResult, String note, String cvLink) {
		super();
		this.id = id;
		this.name = name;
		this.mobilePhone = mobilePhone;
		this.applyPosition = new ApplyPosition(applyPositionId, applyPositionName);
		this.email = email;
		this.applyDate = applyDate;
		this.status = status;
		this.exp = exp;
		this.jobInformationSource = jobInformationSource;
		this.media = new MediaCustom(mediaId, mediaType, mediaName, mediaContent);
		this.interviewResult = interviewResult;
		this.note = note;
		this.cvLink = cvLink;
	}
	ApplicantInformation(){}
	@Id
	@Basic
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	@Column(name="full_name",length = 255,nullable = false)
	public String getName() {
		return name;
	}
	@Column(name="mobile_phone",length = 10)
	public String getMobilePhone() {
		return mobilePhone;
	}
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name="apply_position_id")
	public ApplyPosition getApplyPosition() {
		return applyPosition;
	}
	@Column(name="email",length=320)
	public String getEmail() {
		return email;
	}
	@Column(name="apply_date",nullable = false)
	public Date getApplyDate() {
		return applyDate;
	}
	@Column(name="status")
	public String getStatus() {
		return status;
	}
	@Column(name="exp")
	public String getExp() {
		return exp;
	}
	@Column(name="job_information_source")
	public String getJobInformationSource() {
		return jobInformationSource;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public void setApplyPosition(ApplyPosition applyPosition) {
		this.applyPosition = applyPosition;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	public void setJobInformationSource(String jobInformationSource) {
		this.jobInformationSource = jobInformationSource;
	}
	public void setMedia(MediaCustom media) {
		this.media = media;
	}
	public void setInterviewResult(String interviewResult) {
		this.interviewResult = interviewResult;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setCvLink(String cv_link) {
		this.cvLink = cv_link;
	}
	@Basic
	@OneToOne(fetch = FetchType.LAZY)
	@Cascade(value= {org.hibernate.annotations.CascadeType.ALL})
	@JoinColumn(name="media_id")
	public MediaCustom getMedia() {
		return media;
	}
	@Basic
	@Column(name="interview_result")
	public String getInterviewResult() {
		return interviewResult;
	}
	@Basic
	@Column(name="note",length = 2555)
	public String getNote() {
		return note;
	}
	@Basic
	@Column(name="cv_link",length = 2048)
	public String getCvLink() {
		return cvLink;
	}
	
	

}
