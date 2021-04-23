/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zhtml.Div;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Captcha;
import org.zkoss.zul.Textbox;

import com.evotek.qlns.service.UserService;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author linhlh2
 */
public class LoginController extends GenericForwardComposer
        implements Serializable {

    private static final long serialVersionUID = 1368611560949L;

    private Div container;
    private org.zkoss.zul.Div divVerify;

    private Captcha cpa;

    private Textbox captcha;

    private boolean requireCaptcha = StaticUtil.LOGIN_POLICY_REQUIRE_VERIFY_PRIVATE_LOGIN;

    @Override
    public void doBeforeComposeChildren(Component comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        String ip = Executions.getCurrent().getRemoteAddr();

        if(!requireCaptcha && Validator.isIPAddress(ip)){
            requireCaptcha = userService.isIpAdrRequireCaptcha(ip);
        }
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        Authentication auth = SecurityContextHolder.getContext().
                getAuthentication();

        if(!(auth instanceof AnonymousAuthenticationToken)){
            Executions.sendRedirect("/index.zul");
        } else {
            container.setVisible(true);

            if(!requireCaptcha){
                divVerify.getParent().removeChild(divVerify);
            } else {
                cpa.setLength(StaticUtil.LOGIN_POLICY_CAPTCHA_LENGTH);

                divVerify.setVisible(true);
            }
        }
    }

    public void onClick$btnReCaptcha(){
        cpa.randomValue();
    }

    public void onOK(){
        if(requireCaptcha
                && (!cpa.getValue().equals(captcha.getValue()))){
            Executions.sendRedirect("/login.zul?login_error=3");
        } else {
            Clients.submitForm("f");
        }
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
