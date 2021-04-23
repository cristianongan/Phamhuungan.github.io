/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.common.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

/**
 *
 * @author linhlh2
 */
public class AuthenticationFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler{

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if(exception instanceof SessionAuthenticationException){
            setDefaultFailureUrl("/login.zul?login_error=2");
        } else {
            setDefaultFailureUrl("/login.zul?login_error=1");
        }

        super.onAuthenticationFailure(request, response, exception);
    }

}
