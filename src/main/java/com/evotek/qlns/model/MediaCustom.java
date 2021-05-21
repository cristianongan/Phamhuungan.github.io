package com.evotek.qlns.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.zkoss.util.media.Media;

@Entity
@Table(name="media")
public class MediaCustom implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	private long id;
	private String nameFile;
	private String contentType;
	private byte[] content;
	public MediaCustom(long id, String nameFile, String contentType, byte[] content) {
		super();
		this.id = id;
		this.nameFile = nameFile;
		this.contentType = contentType;
		this.content = content;
	}
	public MediaCustom(Media media) {
		this.nameFile=media.getName();
		this.contentType=media.getContentType();
		this.content=media.getByteData();
	}
	@Id
	@Basic
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	public long getId() {
		return id;
	}
	@Basic
	@Column(name="nameFile",nullable = false)
	public String getNameFile() {
		return nameFile;
	}
	@Basic
	@Column(name="contentType",nullable = false)
	public String getContentType() {
		return contentType;
	}
	@Basic
	@Lob
	@Column(name="content",nullable = false)
	public byte[] getContent() {
		return content;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
}
