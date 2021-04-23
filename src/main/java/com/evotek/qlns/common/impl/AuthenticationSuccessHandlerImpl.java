/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.common.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.evotek.qlns.model.User;
import com.evotek.qlns.policy.impl.UserPrincipalImpl;
import com.evotek.qlns.service.UserService;
import com.evotek.qlns.util.SecurityUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
public class AuthenticationSuccessHandlerImpl extends SimpleUrlAuthenticationSuccessHandler{

	@Autowired
	private UserService userService;
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        UserPrincipalImpl userPrincipalm = SecurityUtil.getUserPrincipalImpl();

        User user = userPrincipalm.getUser();
        
        if(Values.STATUS_NOT_READY.equals(user.getStatus())){
            user.setStatus(Values.STATUS_ACTIVE);

            this.userService.saveOrUpdate(user);
        }

        //remove login failure log
        String ip = request.getRemoteAddr();

        if(Validator.isIPAddress(ip)){
        	this.userService.remove(ip);
        }

        setDefaultTargetUrl("/index.zul");

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
