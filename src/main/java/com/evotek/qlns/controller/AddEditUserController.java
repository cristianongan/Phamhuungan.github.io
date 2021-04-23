package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
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
public class AddEditUserController extends BasicController<Window>
        implements Serializable {

    private Window winUpdateUser;
    
    private Div winParent;

    private Textbox tbUserName;
    private Textbox tbEmail;
    private Textbox tbFirstName;
    private Textbox tbMiddleName;
    private Textbox tbLastName;
    private Textbox tbBirthplace;
    private Textbox tbAddress;
    private Textbox tbPhone;
    private Textbox tbMobile;

    private Combobox cbGender;

    private A btnClearGender;
    
    private Datebox dbBirthday;

    private User user;

//    private String passwordEncode = StringPool.BLANK;

    private boolean isManager;

    @Override
    public void doBeforeComposeChildren(Window win) throws Exception {
        super.doBeforeComposeChildren(win);

        this.winUpdateUser = win;
    }

    @Override
    public void doAfterCompose(Window win) throws Exception {
        super.doAfterCompose(win);

        //init data
        this.initData();
    }


    public void initData() {
        winParent = (Div) arg.get(Constants.PARENT_WINDOW);

        user = (User) arg.get(Constants.OBJECT);

        isManager = GetterUtil.getBooleanValue(arg.get(Constants.SECOND_OBJECT),
                false);

        if (Validator.isNotNull(user)) {
            winUpdateUser.setTitle((String) arg.get(Constants.TITLE));

            this._setEditForm();

            tbUserName.setReadonly(true);
        } else {
            final String staticDomain = StaticUtil.DEFAULT_EMAIL_DOMAIN;
            
            tbEmail.setValue(staticDomain);
            
            tbUserName.addEventListener(Events.ON_CHANGING, new EventListener<Event>() {

                public void onEvent(Event t) throws Exception {
                    tbEmail.setValue(tbUserName.getValue()+staticDomain);
                }
            });
        }

        this.onCreateGender();
    }

    public void onCreateGender() {
        List<SimpleModel> genders =
                userService.getGenderType();
        cbGender.setModel(new ListModelList<SimpleModel>(genders));
    }

    public void onAfterRender$cbGender() {
        if (Validator.isNotNull(user)
                && Validator.isNotNull(user.getGender())) {
                cbGender.setSelectedIndex(user.getGender().intValue());
        }
    }
    
    public void onSelect$cbGender() {
        btnClearGender.setVisible(true);
    }
    
    public void onClick$btnClearGender() {
        cbGender.setSelectedIndex(-1);
        btnClearGender.setVisible(false);
    }
    /*
     * Hàm fill dữ liệu vào form khi thực hiện cập nhật
     */
    private void _setEditForm() {
        try {
            tbUserName.setValue(user.getUserName());
            tbEmail.setValue(user.getEmail());
            tbFirstName.setValue(user.getFirstName());
            tbMiddleName.setValue(user.getMiddleName());
            tbLastName.setValue(user.getLastName());
            tbBirthplace.setValue(user.getBirthPlace());
            tbAddress.setValue(user.getAddress());
            tbPhone.setValue(user.getPhone());
            tbMobile.setValue(user.getMobile());
            dbBirthday.setValue(user.getDateOfBirth());
        } catch (WrongValueException ex) {
            _log.error(ex.getMessage(), ex);
        }
    }
    //event
    public void onClick$btnCancel() {
        winUpdateUser.detach();
    }

    public void onClick$btnSave() {
        save(false);
    }

    public void onClick$btnSaveAndContinue() {
        save(true);
    }

    private void save(final boolean _continue){
        boolean update = true;

        try {
            String userName = GetterUtil.getString(tbUserName.getValue());
            String email = GetterUtil.getString(tbEmail.getValue());
            String firstName = GetterUtil.getString(tbFirstName.getValue());
            String middleName = GetterUtil.getString(tbMiddleName.getValue());
            String lastName = GetterUtil.getString(tbLastName.getValue());
            Long gender = ComponentUtil.getComboboxValue(cbGender);
            Date birthday = dbBirthday.getValue();
            String birthPlace = GetterUtil.getString(tbBirthplace.getValue());
            String address = GetterUtil.getString(tbAddress.getValue());
            String phone = GetterUtil.getString(tbPhone.getValue());
            String mobile = GetterUtil.getString(tbMobile.getValue());

            if (this._validate(userName, email, firstName, middleName, lastName,
                    birthPlace, address, phone, mobile)) {
                if (Validator.isNull(user)) {
                    update = false;

                    user = new User();

                    user.setCreateDate(new Date());
                    user.setStatus(Values.STATUS_NOT_READY);
                    user.setUserName(userName);
                }

                user.setModifiedDate(new Date());
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setMiddleName(middleName);
                user.setLastName(lastName);
                user.setGender(gender);
                user.setDateOfBirth(birthday);
                user.setBirthPlace(birthPlace);
                user.setAddress(address);
                user.setPhone(phone);
                user.setMobile(mobile);                
                
                this.saveUser(update, _continue);
            }
        } catch (WrongValueException ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    private void saveUser(boolean update, boolean _continue) {
        try {
            userService.saveOrUpdate(user);

            //set default password
            if(!update){
                userService.createPassword(user);
            }

            winUpdateUser.detach();

            if(_continue){
                Events.sendEvent(ZkKeys.ON_LOAD_DATA_AND_REOPEN, winParent,
                    null);
            } else {
                Events.sendEvent(ZkKeys.ON_LOAD_DATA, winParent,
                    _createParameterMap(update));
            }

            ComponentUtil.createSuccessMessageBox(ComponentUtil.getSuccessKey(update));
            
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            Messagebox.show(Labels.getLabel(
                    ComponentUtil.getFailKey(update)));
        }
    }

    private Map<String, Object> _createParameterMap(boolean update){
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(Constants.PARENT_WINDOW, winParent);
        parameters.put(Constants.OBJECT, user);
        parameters.put(Constants.SECOND_OBJECT, !update||isManager);

        return parameters;
    }

    private boolean _validate(String userName, String email, String firstName,
            String middleName, String lastName, String birthPlace, String address,
            String phone, String mobile){

        Long userId = Validator.isNull(user) ? null : user.getUserId();

        if(!_validateUserName(userName, userId)){

            return false;
        }

        if(!_validateEmail(email, userId)){
            return false;
        }

        //Ho
        if (Validator.isNull(firstName)) {
            tbFirstName.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.FIRST_NAME)));

            return false;
        }

        if (firstName.length() > Values.SHORT_LENGTH) {
            tbFirstName.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.FIRST_NAME), Values.SHORT_LENGTH));

            return false;
        }

        //Ten
        if (Validator.isNotNull(lastName)
                && lastName.length() > Values.SHORT_LENGTH) {
            tbLastName.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.LAST_NAME),
                    Values.SHORT_LENGTH));

            return false;
        }
        
        if (Validator.isNotNull(middleName)
                && middleName.length() > Values.SHORT_LENGTH) {
            tbMiddleName.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.MIDDLE_NAME),
                    Values.SHORT_LENGTH));

            return false;
        }
        //Noi sinh
        if (Validator.isNotNull(birthPlace)
                && birthPlace.length() > Values.MEDIUM_LENGTH) {
            tbBirthplace.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.PLACE_OF_BIRTH),
                    Values.MEDIUM_LENGTH));

            return false;
        }

        //Dia chi
        if (Validator.isNotNull(address)
                && address.length() > Values.MEDIUM_LENGTH) {
            tbAddress.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.ADDRESS), Values.MEDIUM_LENGTH));

            return false;
        }

        //telephone
        if (Validator.isNotNull(phone)
                && !Validator.isPhoneNumber(phone)) {
            tbPhone.setErrorMessage(Values.getFormatInvalidMsg(
                    Labels.getLabel(LanguageKeys.PHONE)));

            return false;
        }

        //cellphone
        if (Validator.isNotNull(mobile)
                && !Validator.isPhoneNumber(mobile)) {
            tbPhone.setErrorMessage(Values.getFormatInvalidMsg(
                    Labels.getLabel(LanguageKeys.MOBILE)));

            return false;
        }

//        if(!this._validatePassword()){
//            return false;
//        }

        return true;
    }

    private boolean _validateUserName(String userName, Long userId){
        //Ten dang nhap
        if (Validator.isNull(userName)) {
            tbUserName.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.USER_LOGIN_NAME)));

            return false;
        }

        //check length tên
        if (userName.length() > Values.SHORT_LENGTH) {
            tbUserName.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.USER_LOGIN_NAME),
                    Values.SHORT_LENGTH));

            return false;
        }

        if (userName.length() < Values.MIN_NAME_LENGTH) {
            tbUserName.setErrorMessage(Values.getMinLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.USER_LOGIN_NAME),
                    Values.MIN_NAME_LENGTH));

            return false;
        }

        if(!Validator.isVariableName(userName)){
            tbUserName.setErrorMessage(Values.getFormatInvalidMsg(
                    Labels.getLabel(LanguageKeys.USER_LOGIN_NAME),
                    Values.USER_NAME_PATTERN));

            return false;
        }

        if(userService.isUserNameExits(userId, userName)){
            tbUserName.setErrorMessage(Values.getDuplicateMsg(
                    Labels.getLabel(LanguageKeys.USER_LOGIN_NAME)));

            return false;
        }

        return true;
    }

    private boolean _validateEmail(String email, Long userId){
        //email
        if (Validator.isNull(email)) {
            tbEmail.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.EMAIL)));

            return false;
        }

        if (email.length() > Values.SHORT_LENGTH) {
            tbEmail.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.EMAIL), Values.SHORT_LENGTH));

            return false;
        }

        if(!Validator.isEmailAddress(email)){
            tbEmail.setErrorMessage(Values.getFormatInvalidMsg(
                    Labels.getLabel(LanguageKeys.EMAIL)));

            return false;
        }

        if(userService.isEmailExits(userId, email)){
            tbEmail.setErrorMessage(Values.getDuplicateMsg(
                    Labels.getLabel(LanguageKeys.EMAIL)));

            return false;
        }

        return true;
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

    //get set service
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

    private static final Logger _log =
            LogManager.getLogger(AddEditUserController.class);
}
