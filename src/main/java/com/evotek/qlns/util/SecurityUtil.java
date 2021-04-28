/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Sessions;

import com.evotek.qlns.policy.impl.UserPrincipalImpl;

/**
 *
 * @author linhlh2
 */
public class SecurityUtil {
	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static Collection<? extends GrantedAuthority> getGrantedAuthorities() {
		return getAuthentication().getAuthorities();
	}

	public static ServletContext getServletContext() {
		return Sessions.getCurrent().getWebApp().getServletContext();
	}

	public static UserPrincipalImpl getUserPrincipalImpl() {
		return (UserPrincipalImpl) getAuthentication().getPrincipal();
	}

	public static boolean isAllGranted(String authorities) {
		if (null == authorities || "".equals(authorities)) {
			return false;
		}

		if (PermissionUtil.isAdministrator(getUserPrincipalImpl().getRoles())) {
			return true;
		}

		final Collection<? extends GrantedAuthority> granted = getGrantedAuthorities();

		boolean isAllGranted = granted.containsAll(parseAuthorities(authorities));

		return isAllGranted;
	}

	public static boolean isAnyGranted(String authorities) {
		if (null == authorities || "".equals(authorities)) {
			return false;
		}

		if (PermissionUtil.isAdministrator(getUserPrincipalImpl().getRoles())) {
			return true;
		}

		final Collection<? extends GrantedAuthority> granted = getGrantedAuthorities();

		final Set grantedCopy = retainAll(granted, parseAuthorities(authorities));

		return !grantedCopy.isEmpty();
	}

	public static boolean isNoneGranted(String authorities) {
		if (null == authorities || "".equals(authorities)) {
			return false;
		}

		if (PermissionUtil.isAdministrator(getUserPrincipalImpl().getRoles())) {
			return false;
		}

		final Collection<? extends GrantedAuthority> granted = getGrantedAuthorities();

		final Set grantedCopy = retainAll(granted, parseAuthorities(authorities));

		return grantedCopy.isEmpty();
	}

	private static Collection<GrantedAuthority> parseAuthorities(String authorizationsString) {
		final ArrayList<GrantedAuthority> required = new ArrayList<GrantedAuthority>();

		final String[] rights = authorizationsString.split(",");

		for (int i = 0; i < rights.length; i++) {
			String right = rights[i].trim();

			required.add(new SimpleGrantedAuthority(right));
		}

		return required;
	}

	private static Set retainAll(final Collection<? extends GrantedAuthority> granted,
			final Collection<? extends GrantedAuthority> required) {
		Set<String> grantedRoles = toRoles(granted);
		Set<String> requiredRoles = toRoles(required);

		grantedRoles.retainAll(requiredRoles);

		return toAuthorities(grantedRoles, granted);
	}

	private static Set<GrantedAuthority> toAuthorities(Set<String> grantedRights,
			Collection<? extends GrantedAuthority> granted) {
		Set<GrantedAuthority> target = new HashSet<GrantedAuthority>();

		for (String right : grantedRights) {
			for (GrantedAuthority authority : granted) {

				if (authority.getAuthority().equals(right)) {
					target.add(authority);
					break;
				}
			}
		}

		return target;
	}

	private static Set<String> toRoles(Collection<? extends GrantedAuthority> authorities) {
		final Set<String> target = new HashSet<String>();

		for (GrantedAuthority au : authorities) {

			if (null == au.getAuthority()) {
				throw new IllegalArgumentException("Cannot process GrantedAuthority objects which return "
						+ "null from getAuthority() - attempting to process " + au.toString());
			}

			target.add(au.getAuthority());
		}

		return target;
	}
}
