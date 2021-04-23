/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.event;

import java.util.Map;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Include;

import com.evotek.qlns.util.key.Constants;

/**
 *
 * @author linhlh2
 */
public class MenuSelectedEventListener implements EventListener<Event>{

    private Include bodyLayout;
    private String src;
    private Map<String, Object> parameters;

    public MenuSelectedEventListener(Include bodyLayout, String src) {
        this.bodyLayout = bodyLayout;
        this.src = src;
    }

    public MenuSelectedEventListener(Include bodyLayout,
            String src, Map<String, Object> parameters) {
        this.bodyLayout = bodyLayout;
        this.src = src;
        this.parameters = parameters;
    }

    public void onEvent(Event t) throws Exception {
        this.setSelectedMenu(src, parameters);
    }

    private void setSelectedMenu(String url, Map<String, Object> parameters) {
        Sessions.getCurrent().setAttribute(Constants.MAP_PARAMETER, parameters);

        bodyLayout.setSrc(url);

        bodyLayout.invalidate();
    }
}
