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
import org.zkoss.util.media.AMedia;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;

/**
 *
 * @author My PC
 */
public class PreviewFileController extends BasicController<Window>
        implements Serializable{

    private Window win;

    private Iframe viewer;

    private File file;

    private String format;
    private String ctype;

    @Override
    public void doBeforeComposeChildren(Window comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.win = comp;
    }

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        this.init();
    }

    public void init() throws Exception{
        try {
            this.file = (File) this.arg.get(Constants.OBJECT);
            this.format = (String) this.arg.get(Constants.KEY);
            this.ctype = (String) this.arg.get(Constants.VALUE);

            if (Validator.isNotNull(this.file)) {

                AMedia media = new AMedia(this.file.getName(), this.format,
                        this.ctype, new FileInputStream(this.file));

                this.viewer.setContent(media);
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    private static final Logger _log =
            LogManager.getLogger(PreviewFileController.class);
}
