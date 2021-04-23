/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.model.render;

import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.evotek.qlns.model.FileEntry;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
/**
 *
 * @author linhlh2
 */
public class OldFileGridRender<Component> implements RowRenderer<FileEntry>{

    private List<FileEntry> deleteFiles;

    public OldFileGridRender(List<FileEntry> deleteFiles) {
        this.deleteFiles = deleteFiles;
    }
    
    @Override
	public void render(final Row row, final FileEntry entry, int index)
            throws Exception {
        row.appendChild(new Label(entry.getName()));

        Button button = new Button();
        
        button.setTooltiptext(Labels.getLabel(LanguageKeys.DELETE));
        button.setIconSclass(Constants.Z_ICON_TRASH_O);
        button.setSclass(Constants.RED);

        button.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

            @Override
			public void onEvent(Event event) throws Exception {
                OldFileGridRender.this.deleteFiles.add(entry);

                row.getParent().removeChild(row);
            }
        });

        row.appendChild(button);

        row.setStyle(Constants.STYLE_NO_PADDING);
    }
}
