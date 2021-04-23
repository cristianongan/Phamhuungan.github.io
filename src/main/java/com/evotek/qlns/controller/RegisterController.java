/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Captcha;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Textbox;

import com.evotek.qlns.model.User;
import com.evotek.qlns.service.UserService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author My PC
 */
public class RegisterController extends BasicController<Div>
        implements Serializable {

    private static final long serialVersionUID = 1368611560951L;
    
    private Div registerWin;
    
    private Captcha cpaRegister;

    private Textbox tbUserName;
    private Textbox tbEmail;
    private Textbox tbPassword;
    private Textbox tbConfirmPassword;
    private Textbox captchaRegister;
    
    private Html htmlSuccess;
    private Html htmlError;
    
    private Checkbox chkAccept;
    
    private User user;

    @Override
    public void doBeforeComposeChildren(Div comp) throws Exception {
        super.doBeforeComposeChildren(comp);
        
        this.registerWin = comp;
    }

    @Override
    public void doAfterCompose(Div comp) throws Exception {
        super.doAfterCompose(comp); 
    }
    
    public void onClick$btnReCaptchaRegister(){
        cpaRegister.randomValue();
    }
    
    public void onOK() {
        String userName = GetterUtil.getString(tbUserName.getValue());
        String email = GetterUtil.getString(tbEmail.getValue());
        String password = GetterUtil.getString(tbPassword.getValue());
        String confirmPassword = GetterUtil.getString(tbConfirmPassword.getValue());
        String captcha = captchaRegister.getValue();
        
        if(!_validateUserLimited()){
            
        } else if(_validate(userName, email, password, confirmPassword, captcha)){
            user = new User();
            
            user.setCreateDate(new Date());
            user.setStatus(Values.STATUS_NOT_READY);
            user.setUserName(userName);
            user.setModifiedDate(new Date());
            user.setEmail(email);
            
            this.saveUser(user);
        }
    }
    
    private void saveUser(User user) {
        try {
            userService.saveOrUpdate(user);

            //set default password
            userService.createPassword(user);

            showResultMsg(Result.SUCCESS);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);

            showResultMsg(Result.ERROR);
        }
        
        cpaRegister.randomValue();
    }
    
    private boolean _validateUserLimited(){
        return true;
    }
    
    private boolean _validate(String userName, String email, String password, 
            String confirmPassword, String captcha) {
        if(!_validateUserName(userName)){

            return false;
        }

        if(!_validateEmail(email)){
            return false;
        }
        
        if(!this._validatePassword(password, confirmPassword)){
            return false;
        }
        
        if(!cpaRegister.getValue().equals(captcha)){
            captchaRegister.setErrorMessage(
                    Labels.getLabel(LanguageKeys.CAPTCHA_NOT_MATCH));
            
//            cpaRegister.randomValue();
            
            return false;
        }
        
        if(!chkAccept.isChecked()){
            ComponentUtil.createErrorMessageBox(
                    LanguageKeys.YOU_MUST_ACCEPT_USER_AGREEMENT);
            
            return false;
        }
        
        return true;
    }
    
    private boolean _validateUserName(String userName){
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

        if(userService.isUserNameExits(null, userName)){
            tbUserName.setErrorMessage(Values.getDuplicateMsg(
                    Labels.getLabel(LanguageKeys.USER_LOGIN_NAME)));

            return false;
        }

        return true;
    }
    
    private boolean _validateEmail(String email){
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

        if(userService.isEmailExits(null, email)){
            tbEmail.setErrorMessage(Values.getDuplicateMsg(
                    Labels.getLabel(LanguageKeys.EMAIL)));

            return false;
        }

        return true;
    }

    private boolean _validatePassword(String password, String confirmPassword) {
        if (Validator.isNull(password)) {
            tbPassword.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.PASSWORD)));

            return false;
        }

        if (!confirmPassword.equals(password)) {
            tbConfirmPassword.setErrorMessage(Values.getNotSameMsg(
                    Labels.getLabel(LanguageKeys.CONFIRM_PASSWORD)));

            return false;
        }

        if (password.length() < pwdMinlength) {
            tbPassword.setErrorMessage(Values.getMinLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.PASSWORD), pwdMinlength));

            return false;
        }

        if (password.length() > pwdMaxlength) {
            tbPassword.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.PASSWORD), pwdMaxlength));

            return false;
        }

        boolean forceLowerLetter = StaticUtil.PASSWORD_POLICY_FORCE_LOWERCASE_LETTER;
        boolean forceUpperLetter = StaticUtil.PASSWORD_POLICY_FORCE_UPPERCASE_LETTER;
        boolean forceForceDigit = StaticUtil.PASSWORD_POLICY_FORCE_DIGIT;
        boolean forceForceSymbol = StaticUtil.PASSWORD_POLICY_FORCE_SYMBOL;
        String symbolRange = StaticUtil.PASSWORD_POLICY_CHARSET_SYMBOL;

        Matcher matcher = this._getPasswordPatter(forceLowerLetter,
                forceUpperLetter, forceForceDigit, forceForceSymbol,
                symbolRange).matcher(password);

        if (!matcher.matches()) {
            tbPassword.setErrorMessage(Values.getPwdNotMatch(
                    Labels.getLabel(LanguageKeys.PASSWORD), forceLowerLetter,
                    forceUpperLetter, forceForceDigit, forceForceSymbol,
                    symbolRange));

            return false;
        }

        return true;
    }

    private Pattern _getPasswordPatter(boolean forceLowerLetter,
            boolean forceUpperLetter, boolean forceForceDigit,
            boolean forceForceSymbol, String symbolRange) {
        StringBuilder sb = new StringBuilder();

        sb.append(StringPool.OPEN_PARENTHESIS);

        if (forceLowerLetter) {
            sb.append("(?=.*[a-z])");
        }

        if (forceUpperLetter) {
            sb.append("(?=.*[A-Z])");
        }

        if (forceForceDigit) {
            sb.append("(?=.*\\d)");
        }

        if (forceForceSymbol && symbolRange.length() > 0) {
            sb.append("(?=.*[");
            sb.append(symbolRange);
            sb.append("])");
        }

        sb.append(StringPool.PERIOD);
        sb.append(StringPool.OPEN_CURLY_BRACE);
        sb.append(pwdMinlength);
        sb.append(StringPool.COMMA);
        sb.append(pwdMaxlength);
        sb.append(StringPool.CLOSE_CURLY_BRACE);
        sb.append(StringPool.CLOSE_PARENTHESIS);

        return Pattern.compile(sb.toString());
    }
    
    private void showResultMsg(Result result){
        
        switch(result){
            case SUCCESS:
                htmlSuccess.setContent(Labels.getLabel(
                        LanguageKeys.REGISTER_SUCCESS, new Object[]{user.getEmail()}));
                
                Clients.evalJavaScript("showRegisterResult('.register-success')");
                
                break;
            case ERROR:
                htmlError.setContent(Labels.getLabel(LanguageKeys.REGISTER_FAIL));
                
                Clients.evalJavaScript("showRegisterResult('.register-fail')");
                
                break;
            default:
                htmlError.setContent(Labels.getLabel(LanguageKeys.REGISTER_LIMITED));
                
                Clients.evalJavaScript("showRegisterResult('.register-fail')");
                
                break;
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
    
    private static final int pwdMinlength  = StaticUtil.PASSWORD_POLICY_MIN_LENGTH;
    private static final int pwdMaxlength  = StaticUtil.PASSWORD_POLICY_MAX_LENGTH;
    
    public enum Result {
        SUCCESS,
        ERROR,
        WARNING
    }
    
    private static final Logger _log =
            LogManager.getLogger(RegisterController.class);
}
