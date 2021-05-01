/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.util;

import java.util.Collection;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.evotek.qlns.model.Role;
import com.evotek.qlns.util.key.PermissionConstants;

/**
 *
 * @author linhlh2
 */
public class PermissionUtil {
	public static String encodePassword(String password) {

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11);

		return bCryptPasswordEncoder.encode(password);
	}

	public static boolean isAdministrator(Collection<String> roles) {
		return roles.contains(PermissionConstants.ROLE_ADMIN);
	}

	public static boolean isAdministrator(Role role) {
		return PermissionConstants.ROLE_ADMIN.equals(role.getRoleName());
	}

	public static void main(String[] args) {
		System.err.println(PermissionUtil.encodePassword("123456a@"));
	}
}
