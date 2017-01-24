/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.helper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author jngue
 */
public class helper {
    public helper() {
            // TODO Auto-generated constructor stub
    }

    public static final String DEFAULT_ENCODING = "UTF-8";
    private static final BASE64Encoder ENC = new BASE64Encoder();
    private static final BASE64Decoder DEC = new BASE64Decoder();

    public static String encode(String text) {
        try {
            String rez = ENC.encode(text.getBytes(DEFAULT_ENCODING));
            return rez;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }// base64encode

    public static String decode(String text) {
        try {
            return new String(DEC.decodeBuffer(text), DEFAULT_ENCODING);
        } catch (IOException e) {
            return e.getMessage();
        }

    }// base64decode
}
