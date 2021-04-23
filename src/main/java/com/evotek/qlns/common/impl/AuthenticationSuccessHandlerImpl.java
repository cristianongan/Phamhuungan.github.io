/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.common.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.zkoss.spring.SpringUtil;

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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        UserPrincipalImpl userPrincipalm = SecurityUtil.getUserPrincipalImpl();

        User user = userPrincipalm.getUser();

//        if(Validator.isNull(user.getUserId())){
//            UserToken userToken = userService.getUserToken();

//            userService.createUser(user, userToken);
            //get role tu vsa token
            //tao moi user
            //insert role cho user
//        }
        
        if(Values.STATUS_NOT_READY.equals(user.getStatus())){
            user.setStatus(Values.STATUS_ACTIVE);

            userService.saveOrUpdate(user);
        }

        //remove login failure log
        String ip = request.getRemoteAddr();

        if(Validator.isIPAddress(ip)){
            userService.remove(ip);
        }

        setDefaultTargetUrl("/index.zul");

        super.onAuthenticationSuccess(request, response, authentication);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    public UserService getUserService() {
        if (this.userService == null) {
            this.userService = (UserService)
                    SpringUtil.getBean("userService");
            setUserService(this.userService);
        }

        return this.userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private transient UserService userService;
}
