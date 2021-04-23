/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.evotek.qlns.util;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 *
 * @author linhlh2
 */
public class EncryptUtil {

    private final TextEncryptor encryptor;
    
    private static final byte[] _salt = new byte[]{
        57, 53 , 50, 53, 54, 102, 55, 53, 100, 97, 100, 53, 48, 52, 49, 98};
    private static final byte[] _password = new byte[]{
        103, 71, 115, 69, 89, 100, 119, 99, 116, 120, 86, 68, 75, 76, 87, 115, 
        72, 121, 82, 70, 68, 71, 55, 105, 66, 110, 81, 104, 80, 49, 98, 104
    };
    
    public EncryptUtil() {
        encryptor = Encryptors.queryableText(new String(_password), 
                new String(_salt));
    }
    
    public EncryptUtil(TextEncryptor encryptor) {
        this.encryptor = encryptor;
    }
    
    public String encrypt(String str){
        return encryptor.encrypt(str);
    }
    
    public String decrypt(String str) {
        return encryptor.decrypt(str);
    }
}
