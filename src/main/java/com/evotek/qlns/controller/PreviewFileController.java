/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.media.AMedia;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;

/**
 *
 * @author LinhLH
 */
@Controller
@Scope("prototype")
public class PreviewFileController extends BasicController<Window> implements Serializable {

	private static final long serialVersionUID = 8095093259223337416L;

	private static final Logger _log = LogManager.getLogger(PreviewFileController.class);

	private Iframe viewer;

	private File file;

	private String ctype;
	private String format;

	private Window win;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		this.init();
	}

	@Override
	public void doBeforeComposeChildren(Window comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.win = comp;
	}

	public void init() throws Exception {
		try {
			this.file = (File) this.arg.get(Constants.Attr.OBJECT);
			this.format = (String) this.arg.get(Constants.Attr.KEY);
			this.ctype = (String) this.arg.get(Constants.Attr.VALUE);

			if (Validator.isNotNull(this.file)) {

				AMedia media = new AMedia(this.file.getName(), this.format, this.ctype, new FileInputStream(this.file));

				this.viewer.setContent(media);
			}
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}
}
