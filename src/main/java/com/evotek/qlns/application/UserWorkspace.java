/**
 * Copyright 2010 the original author or authors.
 * 
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package com.evotek.qlns.application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Window;

import com.evotek.qlns.security.policy.impl.UserPrincipalImpl;
import com.evotek.qlns.util.PermissionUtil;
import com.evotek.qlns.util.key.PermissionConstants;

/**
 * Workspace for the user. One workspace per userSession. <br>
 * <br>
 * Every logged in user have his own workspace. <br>
 * Here are stored several properties for the user. <br>
 * <br>
 * 1. Access the rights that the user have. <br>
 * 2. The office for that the user are logged in. <br>
 * 
 * @author bbruhns
 * @author Stephan Gerth
 * 
 */
//@Component("userWorkspace")
//@Scope(value = "session",  proxyMode = ScopedProxyMode.INTERFACES)
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserWorkspace implements Serializable, DisposableBean {

	private static final Logger _log = LogManager.getLogger(UserWorkspace.class);

	private static final long serialVersionUID = -3936210543827830197L;

	private static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private String browserType;
	/**
	 * Not used yet.
	 */
	private Set<String> grantedAuthoritySet = null;
	/**
	 * difference in the height between TreeMenu and BarMenu.
	 */
	private final int menuOffset = 32;

	private List<String> roles = new ArrayList<String>();

	private Session session;

	/**
	 * Indicates that as mainMenu the TreeMenu is used, otherwise BarMenu.
	 *
	 * true = init.
	 */
	private boolean treeMenu = true;

	private UserPrincipalImpl userPrincipal;

	/**
	 * Default Constructor
	 */
	public UserWorkspace() {
		if (_log.isDebugEnabled()) {
			_log.debug("create new UserWorkspace [" + this + "]");
		}

		// init data
		this.session = Sessions.getCurrent();

		Object ob = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (ob instanceof UserPrincipalImpl) {
			this.userPrincipal = (UserPrincipalImpl) ob;

			this.roles = (List<String>) this.userPrincipal.getRoles();

			if (!this.roles.contains(PermissionConstants.ROLE_ADMIN)) {
				getGrantedAuthoritySet();
			}
		}

		// speed up the ModalDialogs while disabling the animation
		Window.setDefaultActionOnShow("");
	}

	public Session currentSession() {
		return this.session;
	}

	@Override
	public void destroy() {
		this.grantedAuthoritySet = null;

		SecurityContextHolder.clearContext();

		if (_log.isDebugEnabled()) {
			_log.debug("destroy Workspace [" + this + "]");
		}
	}

	/**
	 * Logout with the spring-security logout action-URL.<br>
	 * Therefore we make a sendRedirect() to the logout uri we <br>
	 * have configured in the spring-config.br>
	 */
	public void doLogout() {
		destroy();

		/* ++++++ Kills the Http session ++++++ */
		// HttpSession s = (HttpSession)
		// Sessions.getCurrent().getNativeSession();
		// s.invalidate();
		/* ++++++ Kills the zk session +++++ */
		// Sessions.getCurrent().invalidate();

		Executions.sendRedirect("/j_spring_logout");
	}

	public String getBrowserType() {
		return this.browserType;
	}

	/**
	 * Copied the grantedAuthorities to a Set of strings <br>
	 * for a faster searching in it.
	 *
	 * @return String set of GrantedAuthorities (rightNames)
	 */
	private Set<String> getGrantedAuthoritySet() {

		if (this.grantedAuthoritySet == null) {

			final Collection<? extends GrantedAuthority> list = getAuthentication().getAuthorities();

			this.grantedAuthoritySet = new HashSet<String>(list.size());

			for (final GrantedAuthority grantedAuthority : list) {
				this.grantedAuthoritySet.add(grantedAuthority.getAuthority());
			}
		}

		return this.grantedAuthoritySet;
	}

	public int getMenuOffset() {

		int result = 0;

		if (isTreeMenu()) {
			result = 0;
		} else {
			result = this.menuOffset;
		}

		return result;
	}

	public List<String> getRoles() {
		return this.roles;
	}

	public Properties getUserLanguageProperty() {

		// // TODO only for testing. we must get the language from
		// // the users table filed
		// userLanguageProperty =
		// ApplicationWorkspace.getInstance().getPropEnglish();
		// userLanguageProperty =
		// ApplicationWorkspace.getInstance().getPropGerman();
		//
		// return userLanguageProperty;
		return null;
	}

	public UserPrincipalImpl getUserPrincipal() {
		return this.userPrincipal;
	}

	public boolean isAdministrator() {
		return PermissionUtil.isAdministrator(this.roles);
	}

	public boolean isAllowed(List<String> rightNames) {
		if (isAdministrator()) {
			return true;
		}

		return this.grantedAuthoritySet.containsAll(rightNames);
	}

	/**
	 * Checks if a right is in the <b>granted rights</b> that the logged in user
	 * have. <br>
	 *
	 * @param rightName
	 * @return true, if the right is in the granted user rights.<br>
	 *         false, if the right is not granted to the user.<br>
	 */
	public boolean isAllowed(String rightName) {
		if (isAdministrator()) {
			return true;
		}

		return this.grantedAuthoritySet.contains(rightName);
	}

	public boolean isTreeMenu() {
		return this.treeMenu;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void setTreeMenu(boolean treeMenu) {
		this.treeMenu = treeMenu;
	}

}
