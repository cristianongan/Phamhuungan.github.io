/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
@Controller
public class LoginController extends GenericForwardComposer implements Serializable {

	private static final long serialVersionUID = 1368611560949L;

	@Autowired
	private UserService userService;

	private Textbox captcha;
	private Div container;

	private Captcha cpa;

	private org.zkoss.zul.Div divVerify;

	private boolean requireCaptcha = StaticUtil.LOGIN_POLICY_REQUIRE_VERIFY_PRIVATE_LOGIN;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {
			Executions.sendRedirect("/index.zul");
		} else {
			this.container.setVisible(true);

			if (!this.requireCaptcha) {
				this.divVerify.getParent().removeChild(this.divVerify);
			} else {
				this.cpa.setLength(StaticUtil.LOGIN_POLICY_CAPTCHA_LENGTH);

				this.divVerify.setVisible(true);
			}
		}
	}

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		String ip = Executions.getCurrent().getRemoteAddr();

		if (!this.requireCaptcha && Validator.isIPAddress(ip)) {
			this.requireCaptcha = this.userService.isIpAdrRequireCaptcha(ip);
		}
	}

	public void onClick$btnReCaptcha() {
		this.cpa.randomValue();
	}

	public void onOK() {
		if (this.requireCaptcha && (!this.cpa.getValue().equals(this.captcha.getValue()))) {
			Executions.sendRedirect("/login.zul?login_error=3");
		} else {
			Clients.submitForm("f");
		}
	}

}
