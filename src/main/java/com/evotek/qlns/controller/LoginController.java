/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Captcha;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

import com.evotek.qlns.service.UserService;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.Validator;

/**
 *
 * @author linhlh2
 */
@Controller("loginController")
@Scope("prototype")
public class LoginController extends GenericForwardComposer<Component> implements Serializable {

	private static final long serialVersionUID = 1368611560949L;

	private static boolean requireCaptcha = StaticUtil.LOGIN_POLICY_REQUIRE_VERIFY_PRIVATE_LOGIN;

	@Autowired
	private UserService userService;

	private Captcha cpa;

	private Div divVerify;

	private org.zkoss.zhtml.Div container;

	private Textbox captcha;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {
			Executions.sendRedirect("/index");
		} else {
			this.container.setVisible(true);

			if (!this.requireCaptcha) {
				Component parent = this.divVerify.getParent();

				if (parent != null) {
					parent.removeChild(this.divVerify);
				} else {
					this.divVerify.setVisible(false);
				}

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
			Executions.sendRedirect("/login?login_error=3");
		} else {
			Clients.submitForm("f");
		}
	}

}
