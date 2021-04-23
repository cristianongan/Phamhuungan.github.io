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
public class OpenDialogListener implements EventListener<Event>{

    private final String url;
    private final Component comp;
    private final Map<String, Object> parameters;

    public OpenDialogListener(String url, Component comp, Map<String, Object> parameters) {
        this.url = url;
        this.comp = comp;
        this.parameters = parameters;
    }

    public void onEvent(Event t) throws Exception {
        Window win = (Window) Executions.createComponents(
                url, comp, parameters);
        
        win.doModal();
    }

}
