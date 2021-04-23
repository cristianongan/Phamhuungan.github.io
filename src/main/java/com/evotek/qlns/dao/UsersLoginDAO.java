/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.dao;

import java.util.List;

import com.evotek.qlns.model.UserLogin;

/**
 *
 * @author linhlh2
 */
public interface UsersLoginDAO {

    public int countByIp(String ip);

    public List<UserLogin> getUsersLogin(String ip);

    public void delete(List<UserLogin> userLogins);

    public void saveOrUpdate(UserLogin loginLog);

}
