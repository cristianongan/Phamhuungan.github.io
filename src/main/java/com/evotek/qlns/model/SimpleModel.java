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

    private Object value;
    private String label;
    private Long id;
    private Long parentId;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public SimpleModel(Object value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public SimpleModel(Long id) {
        this.id = id;
    }
    
    public SimpleModel(String label, Long id) {
        this.label = label;
        this.id = id;
    }
    
    public SimpleModel() {}
}
