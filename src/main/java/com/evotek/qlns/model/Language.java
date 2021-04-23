/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author linhlh2
 */
public class Language implements Serializable {

    private static final long serialVersionUID = -3863392491172579819L;
    private int id;
    private String lanLocale;
    private String lanText;

    public Language() {
    }

    public Language(int id, String lanLocale, String lanText) {
        this.id = id;
        this.lanLocale = lanLocale;
        this.lanText = lanText;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setLanLocale(String lanLocale) {
        this.lanLocale = lanLocale;
    }

    public String getLanLocale() {
        return this.lanLocale;
    }

    public void setLanText(String lanText) {
        this.lanText = lanText;
    }

    public String getLanText() {
        return this.lanText;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    public boolean equals(Language language) {
        return getId() == language.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Language) {
            Language language = (Language) obj;
            return equals(language);
        }

        return false;
    }

    @Override
	public String toString() {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }
}
