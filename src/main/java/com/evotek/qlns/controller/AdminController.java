/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeSet;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
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
@Controller
@Scope("prototype")
public class AdminController extends BasicController<Hlayout> implements Serializable {
	private static final long serialVersionUID = 8550094636173048300L;

	private A toggler;

	private Div sidebar;

	private Hlayout winAdmin;

	private Include contentLayout;

	private Map<String, Object> parameters;

	private Navbar navbar;

	private TreeSet<Category> items;

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

			@Override
			public void onEvent(Event t) throws Exception {
				navItem.setSelected(true);
			}
		});

		navItem.addEventListener(Events.ON_CLICK, new MenuSelectedEventListener(this.contentLayout,
				getPageSource(item.getFolderName(), item.getViewPage())));

		parentNode.appendChild(navItem);
	}

	@Override
	public void doAfterCompose(Hlayout comp) throws Exception {
		super.doAfterCompose(comp);

		this.init();
	}

	@Override
	public void doBeforeComposeChildren(Hlayout comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.winAdmin = comp;
	}

	private String getPageSource(String folder, String pageView) {
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

	public void init() {
		this.parameters = (Map<String, Object>) Sessions.getCurrent().getAttribute(Constants.MAP_PARAMETER);

		if (Validator.isNotNull(this.parameters)) {
			this.items = (TreeSet<Category>) this.parameters.get(Constants.MENU_ITEMS);
		}
	}

	public void onClick$toggler() {
		Include include = (Include) this.winAdmin.getParent();

		if (this.navbar.isCollapsed()) {
			this.sidebar.setSclass("sidebar");
			this.navbar.setCollapsed(false);
			this.toggler.setIconSclass("z-icon-angle-double-left");
			include.setSclass("bodylayout-max");
		} else {
			this.sidebar.setSclass("sidebar sidebar-min");
			this.navbar.setCollapsed(true);
			this.toggler.setIconSclass("z-icon-angle-double-right");
			include.setSclass("bodylayout-min");
		}
		// Force the hlayout contains sidebar to recalculate its size
		Clients.resize(this.sidebar.getRoot().query("#main"));
	}

	public void onCreate$navbar() {
		if (Validator.isNull(this.items)) {
			return;
		}

		for (Category item : this.items) {
			if (Validator.isNull(item)) {
				continue;
			}

			this.createNav(item, this.navbar);
		}
	}
}
