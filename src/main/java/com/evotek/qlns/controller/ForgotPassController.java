/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.controller;

import java.io.Serializable;

import org.zkoss.spring.SpringUtil;
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
 * @author My PC
 */
public class ForgotPassController extends BasicController<Div>
        implements Serializable {

    private static final long serialVersionUID = 1368611560950L;
    
    private Div forgotPassWin;
    
    private Textbox tbEmailRetrive;
    private Textbox tbVerify;
    
    private User user;
    
    @Override
    public void doBeforeComposeChildren(Div comp) throws Exception {
        super.doBeforeComposeChildren(comp);
        
        this.forgotPassWin = comp;
    }

    @Override
    public void doAfterCompose(Div comp) throws Exception {
        super.doAfterCompose(comp); 
    }
    
    public void onOK(){
        String email = GetterUtil.getString(tbEmailRetrive.getValue());
        
        if(_validate(email)){
            Clients.evalJavaScript("showRetriveEmailResult('.retrive-success')");
            
            userService.addVerifyResetPwd(user);
        }
    }
    
    public void onVerify(){
        String verifyCode = GetterUtil.getString(tbVerify.getValue());
        
        if(_validateCode(verifyCode)){
            
        }
    }
    
    private boolean _validate(String email){
        //email
        if (Validator.isNull(email)) {
            tbEmailRetrive.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.EMAIL)));

            return false;
        }

        if (email.length() > Values.SHORT_LENGTH) {
            tbEmailRetrive.setErrorMessage(Values.getMaxLengthInvalidMsg(
                    Labels.getLabel(LanguageKeys.EMAIL), Values.SHORT_LENGTH));

            return false;
        }

        if(!Validator.isEmailAddress(email)){
            tbEmailRetrive.setErrorMessage(Values.getFormatInvalidMsg(
                    Labels.getLabel(LanguageKeys.EMAIL)));

            return false;
        }
        
        user = userService.getUserByEmail(null, email);
        
        if(Validator.isNull(user)){
            tbEmailRetrive.setErrorMessage(Values.getNotExistMsg(
                    Labels.getLabel(LanguageKeys.EMAIL)));

            return false;
        }
        
        return true;
    }
    
    private boolean _validateCode(String verifyCode){
        if (Validator.isNull(verifyCode)) {
            tbVerify.setErrorMessage(Values.getRequiredInputMsg(
                    Labels.getLabel(LanguageKeys.VERIFY_CODE)));

            return false;
        }
        //not completed
        String _code = new EncryptUtil().decrypt(verifyCode);
        
        return true;
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
