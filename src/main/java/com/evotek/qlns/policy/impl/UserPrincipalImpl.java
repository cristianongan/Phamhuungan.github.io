/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.policy.impl;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.DigestUtils;

import com.evotek.qlns.model.User;
import com.evotek.qlns.policy.UserPrincipal;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author linhlh2
 */
public class UserPrincipalImpl extends org.springframework.security.core.userdetails.User
		implements UserPrincipal, Serializable {

	private static final long serialVersionUID = 7682359879431168931L;
	// Token for En-/decrypting the users password
	private String userToken;
	// The user ID
	private Long userId;
	// The user name
	private String userName;
	// The user object
	private User user;
	// Role
	private Collection<String> roles;

	/**
	 * Constructor
	 *
	 * @param username
	 * @param password
	 * @param enabled
	 * @param accountNonExpired
	 * @param credentialsNonExpired
	 * @param accountNonLocked
	 * @param authorities
	 * @throws IllegalArgumentException
	 */
	public UserPrincipalImpl(User user, Collection<String> roles, Collection<GrantedAuthority> grantedAuthorities)
			throws IllegalArgumentException {

		super(user.getUserName(), user.getPassword(), true, true, true, true, grantedAuthorities);
//        super(user.getUserName(), StaticUtil.getDefaultPassword(), user.isActive(),
//                true, true, true, grantedAuthorities);

		this.userId = user.getUserId();
		this.userName = user.getUserName();
		this.userToken = Validator.isNotNull(userId) ? DigestUtils.md5DigestAsHex(userId.toString().getBytes())
				: StringPool.BLANK;
		this.user = user;
		this.roles = roles;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public Long getUserId() {
		return this.userId;
	}

	public User getUser() {
		return this.user;
	}

	public String getUserName() {
		return this.userName;
	}

	public Collection<String> getRoles() {
		return this.roles;
	}
}
