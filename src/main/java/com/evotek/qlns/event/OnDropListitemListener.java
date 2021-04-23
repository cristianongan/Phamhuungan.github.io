/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.event;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listitem;

/**
 *
 * @author linhlh2
 */
public class OnDropListitemListener implements EventListener<DropEvent>{
    private Listitem targetItem;

    public OnDropListitemListener(Listitem targetItem) {
        this.targetItem = targetItem;
    }

    @Override
	public void onEvent(DropEvent event) throws Exception {
        Component dragged = event.getDragged();

	this.targetItem.getParent().insertBefore(dragged, this.targetItem);
    }
}
