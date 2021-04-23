/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * @author linhlh2
 */
public class RandomStringGeneratorUtil {

    public static enum Mode {

        MODE_ALPHA_UPPER_CASE,
        MODE_ALPHA_LOWER_CASE,
        MODE_NUMERIC,
        MODE_SPECIAL_CHARACTER
    }
    
    private static final String ALPHA_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    private static final String ALPHA_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    
    private static final String NUMERIC = "0123456789";
    
    private static final String SPECIAL_CHARACTER = "!@#$%^&*_=+-/";
    
    private static final int DEFAULT_LENGTH = 8;
    
    public static String generate(){
        return generate(DEFAULT_LENGTH);
    }
    
    public static String generate(int length){
        return generate(length, 
                new Mode[]{
                    Mode.MODE_ALPHA_UPPER_CASE,
                    Mode.MODE_ALPHA_LOWER_CASE,
                    Mode.MODE_NUMERIC,
                    Mode.MODE_SPECIAL_CHARACTER
                });
    }
    
    public static String generate(int length, Mode[] modes) {
        StringBuilder characterGroup = new StringBuilder();
        
        StringBuilder sb = new StringBuilder();
        
        if(modes == null || modes.length == 0){
            characterGroup.append(ALPHA_LOWER_CASE);
        } else {
            for (Mode mode : modes) {
                switch (mode) {
                    case MODE_ALPHA_UPPER_CASE:
                        characterGroup.append(ALPHA_UPPER_CASE);

                        break;

                    case MODE_ALPHA_LOWER_CASE:
                        characterGroup.append(ALPHA_LOWER_CASE);

                        break;
                    case MODE_NUMERIC:
                        characterGroup.append(NUMERIC);

                        break;
                    case MODE_SPECIAL_CHARACTER:
                        characterGroup.append(SPECIAL_CHARACTER);

                        break;
                    default:
                        characterGroup.append(ALPHA_UPPER_CASE);

                        break;
                }
            }
        }
        
        Random random = new SecureRandom();
        
        for (int i = 0; i < length; i++) {
            double index = random.nextDouble() * characterGroup.length();
            
            sb.append(characterGroup.charAt((int) index));
        }
        
        return sb.toString();
    }
}
