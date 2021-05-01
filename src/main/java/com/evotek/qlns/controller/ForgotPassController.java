/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

import com.evotek.qlns.model.User;
import com.evotek.qlns.service.UserService;
import com.evotek.qlns.util.EncryptUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author LinhLH
 */
@Controller
@Scope("prototype")
public class ForgotPassController extends BasicController<Div> implements Serializable {

	private static final long serialVersionUID = 1368611560950L;

	@Autowired
	private UserService userService;

	private Div forgotPassWin;

	private Textbox tbEmailRetrive;
	private Textbox tbVerify;

	private User user;

	private boolean _validate(String email) {
		// email
		if (Validator.isNull(email)) {
			this.tbEmailRetrive.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.EMAIL)));

			return false;
		}

		if (email.length() > Values.SHORT_LENGTH) {
			this.tbEmailRetrive.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.EMAIL), Values.SHORT_LENGTH));

			return false;
		}

		if (!Validator.isEmailAddress(email)) {
			this.tbEmailRetrive.setErrorMessage(Values.getFormatInvalidMsg(Labels.getLabel(LanguageKeys.EMAIL)));

			return false;
		}

		this.user = this.userService.getUserByEmail(null, email);

		if (Validator.isNull(this.user)) {
			this.tbEmailRetrive.setErrorMessage(Values.getNotExistMsg(Labels.getLabel(LanguageKeys.EMAIL)));

			return false;
		}

		return true;
	}

	private boolean _validateCode(String verifyCode) {
		if (Validator.isNull(verifyCode)) {
			this.tbVerify.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.VERIFY_CODE)));

			return false;
		}
		// not completed
		String _code = new EncryptUtil().decrypt(verifyCode);

		return true;
	}

	@Override
	public void doAfterCompose(Div comp) throws Exception {
		super.doAfterCompose(comp);
	}

	@Override
	public void doBeforeComposeChildren(Div comp) throws Exception {
		super.doBeforeComposeChildren(comp);

		this.forgotPassWin = comp;
	}

	public void onOK() {
		String email = GetterUtil.getString(this.tbEmailRetrive.getValue());

		if (_validate(email)) {
			Clients.evalJavaScript("showRetriveEmailResult('.retrive-success')");

			this.userService.addVerifyResetPwd(this.user);
		}
	}

	public void onVerify() {
		String verifyCode = GetterUtil.getString(this.tbVerify.getValue());

		if (_validateCode(verifyCode)) {

		}
	}

}
