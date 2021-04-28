/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.event;

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Window;

/**
 *
 * @author linhlh2
 */
public class OpenDialogListener implements EventListener<Event> {

	private final Component comp;
	private final Map<String, Object> parameters;
	private final String url;

	public OpenDialogListener(String url, Component comp, Map<String, Object> parameters) {
		this.url = url;
		this.comp = comp;
		this.parameters = parameters;
	}

	@Override
	public void onEvent(Event t) throws Exception {
		Window win = (Window) Executions.createComponents(this.url, this.comp, this.parameters);

		win.doModal();
	}

}
