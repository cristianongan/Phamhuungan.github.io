/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeSet;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Navbar;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Include;

import com.evotek.qlns.event.MenuSelectedEventListener;
import com.evotek.qlns.model.Category;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;

/**
 *
 * @author linhlh2
 */
public class AdminController extends BasicController<Hlayout>
        implements Serializable{
    private Hlayout winAdmin;
    
    private Navbar navbar;
    
    private Div sidebar;
    
    private A toggler;
    
    private Include contentLayout;
    
    private Map<String, Object> parameters;
    
    private TreeSet<Category> items;
    @Override
    public void doBeforeComposeChildren(Hlayout comp) throws Exception {
        super.doBeforeComposeChildren(comp);
        
        this.winAdmin = comp;
    }
    
    @Override
    public void doAfterCompose(Hlayout comp) throws Exception {
        super.doAfterCompose(comp);

        this.init();
    }
    
    public void init() {
        parameters = (Map<String, Object>) Sessions.getCurrent().
                getAttribute(Constants.MAP_PARAMETER);
        
        if(Validator.isNotNull(parameters)){
            items = (TreeSet<Category>) parameters.get(Constants.MENU_ITEMS);
        }
    }
    
    public void onCreate$navbar() {
        if(Validator.isNull(items)){
            return;
        }

        for (Category item : items) {
            if (Validator.isNull(item)) {
                continue;
            }

            this.createNav(item, navbar);
        }
    }
    
    private void createNav(Category item, Component parentNode) {
        final Navitem navItem = new Navitem();

        navItem.setLabel(Labels.getLabel(item.getLanguageKey()));

        if (Validator.isNotNull(item.getIcon())) {
            navItem.setIconSclass(item.getIcon());
        } else {
            navItem.setIconSclass("z-icon-angle-double-right");
        }

        navItem.setId(item.getFolderName());
        
        navItem.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

            public void onEvent(Event t) throws Exception {
                navItem.setSelected(true);
            }
        });

        navItem.addEventListener(Events.ON_CLICK, new MenuSelectedEventListener(
                contentLayout, getPageSource(item.getFolderName(), item.getViewPage())));

        parentNode.appendChild(navItem);
    }
    
    public void onClick$toggler() {
        Include include = (Include) winAdmin.getParent();
        
        if (navbar.isCollapsed()) {
            sidebar.setSclass("sidebar");
            navbar.setCollapsed(false);
            toggler.setIconSclass("z-icon-angle-double-left");
            include.setSclass("bodylayout-max");
        } else {
            sidebar.setSclass("sidebar sidebar-min");
            navbar.setCollapsed(true);
            toggler.setIconSclass("z-icon-angle-double-right");
            include.setSclass("bodylayout-min");
        }
        // Force the hlayout contains sidebar to recalculate its size
        Clients.resize(sidebar.getRoot().query("#main"));
    }
    
    private String getPageSource(String folder, String pageView){
        StringBuilder sb = new StringBuilder(4);

        sb.append(Constants.CATEGORY_FOLDER);

        if (Validator.isNotNull(folder)) {
            sb.append(folder);
            sb.append(StringPool.FORWARD_SLASH);
        }

        sb.append(pageView);
        sb.append(StringPool.ZUL);

        return sb.toString();
    }
}
