/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.model;

import java.io.Serializable;

/**
 *
 * @author linhlh2
 */
public class SimpleModel implements Serializable {

	private Long id;
	private String label;
	private Long parentId;
	private Object value;

	public SimpleModel() {
	}

	public SimpleModel(Long id) {
		this.id = id;
	}

	public SimpleModel(Object value, String label) {
		this.value = value;
		this.label = label;
	}

	public SimpleModel(String label, Long id) {
		this.label = label;
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public String getLabel() {
		return this.label;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public Object getValue() {
		return this.value;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
