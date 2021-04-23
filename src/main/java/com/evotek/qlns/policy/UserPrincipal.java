/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.policy;

import java.util.Collection;

import com.evotek.qlns.model.User;

/**
 *
 * @author linhlh2
 */
public interface UserPrincipal {
    public Long getUserId();

    public String getUserName();

    public User getUser();

    public Collection<String> getRoles();
}