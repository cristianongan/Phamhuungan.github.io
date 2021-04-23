/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.util;

import java.util.Collection;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.util.DigestUtils;

import com.evotek.qlns.model.Role;
import com.evotek.qlns.util.key.PermissionConstants;

/**
 *
 * @author linhlh2
 */
public class PermissionUtil {
    public static boolean isAdministrator(Collection<String> roles){
        return roles.contains(PermissionConstants.ROLE_ADMIN);
    }

    public static boolean isAdministrator(Role role){
        return PermissionConstants.ROLE_ADMIN.equals(role.getRoleName());
    }

    public static String encodePassword(String password, String userId){
        String token = DigestUtils.md5DigestAsHex(userId.getBytes());

        return new Md5PasswordEncoder().encodePassword(password, token);
    }

    public static String encodePassword(String password, Long userId){
        return encodePassword(password, userId.toString());
    }
    
    public static void main(String[] args) {
        System.err.println(PermissionUtil.encodePassword("123456a@", "100004"));
    }
}
