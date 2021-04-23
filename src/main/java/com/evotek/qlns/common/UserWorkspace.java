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
package com.evotek.qlns.common;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Window;

import com.evotek.qlns.policy.impl.UserPrincipalImpl;
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
public class UserWorkspace implements Serializable, DisposableBean {

	private static final long serialVersionUID = -3936210543827830197L;

	private static final Logger _log = LogManager.getLogger(UserWorkspace.class);

	private String userLanguage;
	private String browserType;
	/**
	 * Indicates that as mainMenu the TreeMenu is used, otherwise BarMenu.
	 *
	 * true = init.
	 */
	private boolean treeMenu = true;
	/**
	 * difference in the height between TreeMenu and BarMenu.
	 */
	private final int menuOffset = 32;
	/**
	 * Not used yet.
	 */
	private Set<String> grantedAuthoritySet = null;

	private List<String> roles = new ArrayList<String>();

	private UserPrincipalImpl userPrincipal;

	private Session session;

	private static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * Get a logged-in users WorkSpace which holds all necessary vars. <br>
	 *
	 * @return the users WorkSpace
	 * @deprecated
	 */
	public static UserWorkspace getInstance() {
		return SpringUtil.getBean("userWorkspace", UserWorkspace.class);
	}

	/**
	 * Default Constructor
	 */
	public UserWorkspace() {
		if (_log.isDebugEnabled()) {
			_log.debug("create new UserWorkspace [" + this + "]");
		}

		// init data
		session = Sessions.getCurrent();

		userPrincipal = (UserPrincipalImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		roles = (List<String>) userPrincipal.getRoles();

		if (!roles.contains(PermissionConstants.ROLE_ADMIN)) {
			getGrantedAuthoritySet();
		}

		// speed up the ModalDialogs while disabling the animation
		Window.setDefaultActionOnShow("");
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

		return grantedAuthoritySet.contains(rightName);
	}

	public boolean isAllowed(List<String> rightNames) {
		if (isAdministrator()) {
			return true;
		}

		return grantedAuthoritySet.containsAll(rightNames);
	}

	public boolean isAdministrator() {
		return PermissionUtil.isAdministrator(roles);
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

	@Override
	public void destroy() {
		this.grantedAuthoritySet = null;

		SecurityContextHolder.clearContext();

		if (_log.isDebugEnabled()) {
			_log.debug("destroy Workspace [" + this + "]");
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}

	public String getUserLanguage() {
		return this.userLanguage;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public String getBrowserType() {
		return this.browserType;
	}

	public int getMenuOffset() {

		int result = 0;

		if (isTreeMenu()) {
			result = 0;
		} else {
			result = menuOffset;
		}

		return result;
	}

	public void setTreeMenu(boolean treeMenu) {
		this.treeMenu = treeMenu;
	}

	public boolean isTreeMenu() {
		return treeMenu;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public UserPrincipalImpl getUserPrincipal() {
		return userPrincipal;
	}

	public void setUserPrincipal(UserPrincipalImpl userPrincipal) {
		this.userPrincipal = userPrincipal;
	}

	public Session currentSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
