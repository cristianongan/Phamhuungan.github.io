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
import org.zkoss.zul.Window;

import com.evotek.qlns.event.DownloadFileEntryListener;
import com.evotek.qlns.model.FileEntry;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.ZkKeys;

/**
 *
 * @author linhlh2
 */
public class DownloadFileGridRender<Component> implements RowRenderer<FileEntry> {

	private Window win;

	public DownloadFileGridRender(Window win) {
		this.win = win;
	}

	@Override
	public void render(Row row, FileEntry entry, int index) throws Exception {
		row.appendChild(new Label(entry.getName()));

		A rm = new A();

		rm.setImage(Constants.Icon.DOWNLOAD);

		rm.setTooltiptext(Labels.getLabel(LanguageKeys.DOWNLOAD));

		rm.addEventListener(Events.ON_CLICK, new DownloadFileEntryListener(entry));

		row.appendChild(rm);

		A rm1 = new A();
		rm1.setImage(Constants.Icon.RESOURCE);

		rm1.setTooltiptext(Labels.getLabel(LanguageKeys.PREVIEW));

		rm1.addForward(Events.ON_CLICK, this.win, ZkKeys.ON_PREVIEW_DATA, entry);

		row.appendChild(rm1);

		row.setStyle(Constants.STYLE_NO_PADDING);
	}

}
