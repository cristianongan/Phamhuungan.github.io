package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.model.SimpleModel;
import com.evotek.qlns.model.User;
import com.evotek.qlns.service.UserService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;
import com.evotek.qlns.util.key.ZkKeys;

/**
 *
 * @author LinhLH
 */
@Controller
@Scope("prototype")
public class AddEditUserController extends BasicController<Window> implements Serializable {

	private static final long serialVersionUID = -2959880949068841736L;

	private static final Logger _log = LogManager.getLogger(AddEditUserController.class);

	@Autowired
	private UserService userService;

	private A btnClearGender;

	private Combobox cbGender;

	private Datebox dbBirthday;

	private Div winParent;

	private Textbox tbAddress;
	private Textbox tbBirthplace;
	private Textbox tbEmail;
	private Textbox tbFirstName;
	private Textbox tbLastName;
	private Textbox tbMiddleName;
	private Textbox tbMobile;
	private Textbox tbPhone;
	private Textbox tbUserName;

	private User user;

	private Window winUpdateUser;

	private boolean isManager;
//    private String passwordEncode = StringPool.BLANK;

	private Map<String, Object> _createParameterMap(boolean update) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.PARENT_WINDOW, this.winParent);
		parameters.put(Constants.OBJECT, this.user);
		parameters.put(Constants.SECOND_OBJECT, !update || this.isManager);

		return parameters;
	}

	/*
	 * Hàm fill dữ liệu vào form khi thực hiện cập nhật
	 */
	private void _setEditForm() {
		try {
			this.tbUserName.setValue(this.user.getUserName());
			this.tbEmail.setValue(this.user.getEmail());
			this.tbFirstName.setValue(this.user.getFirstName());
			this.tbMiddleName.setValue(this.user.getMiddleName());
			this.tbLastName.setValue(this.user.getLastName());
			this.tbBirthplace.setValue(this.user.getBirthPlace());
			this.tbAddress.setValue(this.user.getAddress());
			this.tbPhone.setValue(this.user.getPhone());
			this.tbMobile.setValue(this.user.getMobile());
			this.dbBirthday.setValue(this.user.getDateOfBirth());
		} catch (WrongValueException ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	private boolean _validate(String userName, String email, String firstName, String middleName, String lastName,
			String birthPlace, String address, String phone, String mobile) {

		Long userId = Validator.isNull(this.user) ? null : this.user.getUserId();

		if (!_validateUserName(userName, userId)) {

			return false;
		}

		if (!_validateEmail(email, userId)) {
			return false;
		}

		// Ho
		if (Validator.isNull(firstName)) {
			this.tbFirstName.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.FIRST_NAME)));

			return false;
		}

		if (firstName.length() > Values.SHORT_LENGTH) {
			this.tbFirstName.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.FIRST_NAME), Values.SHORT_LENGTH));

			return false;
		}

		// Ten
		if (Validator.isNotNull(lastName) && lastName.length() > Values.SHORT_LENGTH) {
			this.tbLastName.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.LAST_NAME), Values.SHORT_LENGTH));

			return false;
		}

		if (Validator.isNotNull(middleName) && middleName.length() > Values.SHORT_LENGTH) {
			this.tbMiddleName.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.MIDDLE_NAME), Values.SHORT_LENGTH));

			return false;
		}
		// Noi sinh
		if (Validator.isNotNull(birthPlace) && birthPlace.length() > Values.MEDIUM_LENGTH) {
			this.tbBirthplace.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.PLACE_OF_BIRTH), Values.MEDIUM_LENGTH));

			return false;
		}

		// Dia chi
		if (Validator.isNotNull(address) && address.length() > Values.MEDIUM_LENGTH) {
			this.tbAddress.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.ADDRESS), Values.MEDIUM_LENGTH));

			return false;
		}

		// telephone
		if (Validator.isNotNull(phone) && !Validator.isPhoneNumber(phone)) {
			this.tbPhone.setErrorMessage(Values.getFormatInvalidMsg(Labels.getLabel(LanguageKeys.PHONE)));

			return false;
		}

		// cellphone
		if (Validator.isNotNull(mobile) && !Validator.isPhoneNumber(mobile)) {
			this.tbPhone.setErrorMessage(Values.getFormatInvalidMsg(Labels.getLabel(LanguageKeys.MOBILE)));

			return false;
		}

//        if(!this._validatePassword()){
//            return false;
//        }

		return true;
	}

	private boolean _validateEmail(String email, Long userId) {
		// email
		if (Validator.isNull(email)) {
			this.tbEmail.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.EMAIL)));

			return false;
		}

		if (email.length() > Values.SHORT_LENGTH) {
			this.tbEmail.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.EMAIL), Values.SHORT_LENGTH));

			return false;
		}

		if (!Validator.isEmailAddress(email)) {
			this.tbEmail.setErrorMessage(Values.getFormatInvalidMsg(Labels.getLabel(LanguageKeys.EMAIL)));

			return false;
		}

		if (this.userService.isEmailExits(userId, email)) {
			this.tbEmail.setErrorMessage(Values.getDuplicateMsg(Labels.getLabel(LanguageKeys.EMAIL)));

			return false;
		}

		return true;
	}

	private boolean _validateUserName(String userName, Long userId) {
		// Ten dang nhap
		if (Validator.isNull(userName)) {
			this.tbUserName.setErrorMessage(Values.getRequiredInputMsg(Labels.getLabel(LanguageKeys.USER_LOGIN_NAME)));

			return false;
		}

		// check length tên
		if (userName.length() > Values.SHORT_LENGTH) {
			this.tbUserName.setErrorMessage(
					Values.getMaxLengthInvalidMsg(Labels.getLabel(LanguageKeys.USER_LOGIN_NAME), Values.SHORT_LENGTH));

			return false;
		}

		if (userName.length() < Values.MIN_NAME_LENGTH) {
			this.tbUserName.setErrorMessage(Values.getMinLengthInvalidMsg(Labels.getLabel(LanguageKeys.USER_LOGIN_NAME),
					Values.MIN_NAME_LENGTH));

			return false;
		}

		if (!Validator.isVariableName(userName)) {
			this.tbUserName.setErrorMessage(Values.getFormatInvalidMsg(Labels.getLabel(LanguageKeys.USER_LOGIN_NAME),
					Values.USER_NAME_PATTERN));

			return false;
		}

		if (this.userService.isUserNameExits(userId, userName)) {
			this.tbUserName.setErrorMessage(Values.getDuplicateMsg(Labels.getLabel(LanguageKeys.USER_LOGIN_NAME)));

			return false;
		}

		return true;
	}

	@Override
	public void doAfterCompose(Window win) throws Exception {
		super.doAfterCompose(win);

		// init data
		this.initData();
	}

	@Override
	public void doBeforeComposeChildren(Window win) throws Exception {
		super.doBeforeComposeChildren(win);

		this.winUpdateUser = win;
	}

	public void initData() {
		this.winParent = (Div) this.arg.get(Constants.PARENT_WINDOW);

		this.user = (User) this.arg.get(Constants.OBJECT);

		this.isManager = GetterUtil.getBooleanValue(this.arg.get(Constants.SECOND_OBJECT), false);

		if (Validator.isNotNull(this.user)) {
			this.winUpdateUser.setTitle((String) this.arg.get(Constants.TITLE));

			this._setEditForm();

			this.tbUserName.setReadonly(true);
		} else {
			final String staticDomain = StaticUtil.DEFAULT_EMAIL_DOMAIN;

			this.tbEmail.setValue(staticDomain);

			this.tbUserName.addEventListener(Events.ON_CHANGING, new EventListener<Event>() {

				@Override
				public void onEvent(Event t) throws Exception {
					AddEditUserController.this.tbEmail
							.setValue(AddEditUserController.this.tbUserName.getValue() + staticDomain);
				}
			});
		}

		this.onCreateGender();
	}

	public void onAfterRender$cbGender() {
		if (Validator.isNotNull(this.user) && Validator.isNotNull(this.user.getGender())) {
			this.cbGender.setSelectedIndex(this.user.getGender().intValue());
		}
	}

	// event
	public void onClick$btnCancel() {
		this.winUpdateUser.detach();
	}

	public void onClick$btnClearGender() {
		this.cbGender.setSelectedIndex(-1);
		this.btnClearGender.setVisible(false);
	}

	public void onClick$btnSave() {
		save(false);
	}

	public void onClick$btnSaveAndContinue() {
		save(true);
	}

	public void onCreateGender() {
		List<SimpleModel> genders = this.userService.getGenderType();
		this.cbGender.setModel(new ListModelList<SimpleModel>(genders));
	}

//    private boolean _validatePassword(){
//        if(Validator.isNotNull(user)){
//            String currentPassword = tbCurrentPassword.getValue();
//            String newPassword = tbNewPassword.getValue();
//            String confirmPassword = tbConfirmPassword.getValue();
//
//            if(Validator.isNotNull(newPassword)
//                    || Validator.isNotNull(confirmPassword)
//                    || (!isManager && Validator.isNotNull(currentPassword))){
//                if(!confirmPassword.equals(newPassword)){
//                    tbConfirmPassword.setErrorMessage(Values.getNotSameMsg(
//                            Labels.getLabel(LanguageKeys.CONFIRM_PASSWORD)));
//
//                    return false;
//                }
//
//                if(newPassword.length()< pwdMinlength){
//                    tbNewPassword.setErrorMessage(Values.getMinLengthInvalidMsg(
//                            Labels.getLabel(LanguageKeys.PASSWORD), pwdMinlength));
//
//                    return false;
//                }
//
//                if(newPassword.length()> pwdMaxlength){
//                    tbNewPassword.setErrorMessage(Values.getMaxLengthInvalidMsg(
//                            Labels.getLabel(LanguageKeys.PASSWORD), pwdMaxlength));
//
//                    return false;
//                }
//
//                boolean forceLowerLetter = StaticUtil.pwdForceLowerLetter();
//                boolean forceUpperLetter = StaticUtil.pwdForceUpperLetter();
//                boolean forceForceDigit = StaticUtil.pwdForceDigit();
//                boolean forceForceSymbol = StaticUtil.pwdForceDigit();
//                String symbolRange = StaticUtil.getPwdCharsetSymbol();
//
//                Matcher matcher = this._getPasswordPatter(forceLowerLetter,
//                        forceUpperLetter, forceForceDigit, forceForceSymbol,
//                        symbolRange).matcher(newPassword);
//
//                if (!matcher.matches()) {
//                    tbNewPassword.setErrorMessage(Values.getPwdNotMatch(
//                            Labels.getLabel(LanguageKeys.PASSWORD), forceLowerLetter,
//                            forceUpperLetter, forceForceDigit, forceForceSymbol,
//                            symbolRange));
//
//                    return false;
//                }
//            }
//
//            if(!isManager){
//                if(newPassword.contains(currentPassword)){
//                    tbNewPassword.setErrorMessage(Labels.getLabel(
//                            LanguageKeys.MESSAGE_NEW_PASSWORD_DO_NOT_CONTAIN_OLD_PASSWORD));
//
//                    return false;
//                }
//
//                String currentPasswordEncode = PermissionUtil.encodePassword(
//                        currentPassword, user.getUserId());
//
//                if(!currentPasswordEncode.equals(user.getPassword())){
//                    tbCurrentPassword.setErrorMessage(Values.getNotSameMsg(
//                            Labels.getLabel(LanguageKeys.CURRENT_PASSWORD)));
//
//                    return false;
//                }
//            }
//
//            if(Validator.isNotNull(newPassword)){
//                passwordEncode = PermissionUtil.encodePassword(
//                        newPassword, user.getUserId());
//            }
//        }
//
//        return true;
//    }
//
//    private Pattern _getPasswordPatter(boolean forceLowerLetter,
//            boolean forceUpperLetter, boolean forceForceDigit,
//            boolean forceForceSymbol, String symbolRange){
//        StringBuilder sb = new StringBuilder();
//
//        sb.append(StringPool.OPEN_PARENTHESIS);
//
//        if(forceLowerLetter){
//            sb.append("(?=.*[a-z])");
//        }
//
//        if(forceUpperLetter){
//            sb.append("(?=.*[A-Z])");
//        }
//
//        if(forceForceDigit){
//            sb.append("(?=.*\\d)");
//        }
//
//        if(forceForceSymbol && symbolRange.length()>0){
//            sb.append("(?=.*[");
//            sb.append(symbolRange);
//            sb.append("])");
//        }
//
//        sb.append(StringPool.PERIOD);
//        sb.append(StringPool.OPEN_CURLY_BRACE);
//        sb.append(pwdMinlength);
//        sb.append(StringPool.COMMA);
//        sb.append(pwdMaxlength);
//        sb.append(StringPool.CLOSE_CURLY_BRACE);
//        sb.append(StringPool.CLOSE_PARENTHESIS);
//
//        return Pattern.compile(sb.toString());
//    }

	public void onSelect$cbGender() {
		this.btnClearGender.setVisible(true);
	}

	private void save(final boolean _continue) {
		boolean update = true;

		try {
			String userName = GetterUtil.getString(this.tbUserName.getValue());
			String email = GetterUtil.getString(this.tbEmail.getValue());
			String firstName = GetterUtil.getString(this.tbFirstName.getValue());
			String middleName = GetterUtil.getString(this.tbMiddleName.getValue());
			String lastName = GetterUtil.getString(this.tbLastName.getValue());
			Long gender = ComponentUtil.getComboboxValue(this.cbGender);
			Date birthday = this.dbBirthday.getValue();
			String birthPlace = GetterUtil.getString(this.tbBirthplace.getValue());
			String address = GetterUtil.getString(this.tbAddress.getValue());
			String phone = GetterUtil.getString(this.tbPhone.getValue());
			String mobile = GetterUtil.getString(this.tbMobile.getValue());

			if (this._validate(userName, email, firstName, middleName, lastName, birthPlace, address, phone, mobile)) {
				if (Validator.isNull(this.user)) {
					update = false;

					this.user = new User();

					this.user.setCreateDate(new Date());
					this.user.setStatus(Values.STATUS_NOT_READY);
					this.user.setUserName(userName);
				}

				this.user.setModifiedDate(new Date());
				this.user.setEmail(email);
				this.user.setFirstName(firstName);
				this.user.setMiddleName(middleName);
				this.user.setLastName(lastName);
				this.user.setGender(gender);
				this.user.setDateOfBirth(birthday);
				this.user.setBirthPlace(birthPlace);
				this.user.setAddress(address);
				this.user.setPhone(phone);
				this.user.setMobile(mobile);

				this.saveUser(update, _continue);
			}
		} catch (WrongValueException ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	private void saveUser(boolean update, boolean _continue) {
		try {
			this.userService.saveOrUpdate(this.user);

			// set default password
			if (!update) {
				this.userService.createPassword(this.user);
			}

			this.winUpdateUser.detach();

			if (_continue) {
				Events.sendEvent(ZkKeys.ON_LOAD_DATA_AND_REOPEN, this.winParent, null);
			} else {
				Events.sendEvent(ZkKeys.ON_LOAD_DATA, this.winParent, _createParameterMap(update));
			}

			ComponentUtil.createSuccessMessageBox(ComponentUtil.getSuccessKey(update));

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);

			Messagebox.show(Labels.getLabel(ComponentUtil.getFailKey(update)));
		}
	}
}
