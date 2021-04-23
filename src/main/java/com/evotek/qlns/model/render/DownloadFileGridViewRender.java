/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model.render;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.evotek.qlns.event.DownloadFileEntryListener;
import com.evotek.qlns.model.FileEntry;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author linhlh2
 */
public class DownloadFileGridViewRender <Component> implements
        RowRenderer<FileEntry>{

    @Override
	public void render(Row row, FileEntry entry, int index) throws Exception {
        row.appendChild(new Label(entry.getName()));

        A rm = new A();

        rm.setImage(ComponentUtil.DOWNLOAD_ICON);

        rm.setTooltiptext(Labels.getLabel(LanguageKeys.DOWNLOAD));

        rm.addEventListener(Events.ON_CLICK,
                new DownloadFileEntryListener(entry));

        row.appendChild(rm);

    }

}
