package com.yd.telescope.common.util;

import com.yd.telescope.common.enums.ResultEnums;
import com.yd.telescope.common.exception.EncryException;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryUtils {

    /**利用MD5进行加密 base64的md5加密
     * @param str  待加密的字符串
     * @return  加密后的字符串
     * @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException
     */
    public static String EncoderByMd5ForBase64(String str) throws EncryException {
        try {
            //确定计算方法
            MessageDigest md5=MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            //加密后的字符串
            String newStr=base64en.encode(md5.digest(str.getBytes("utf-8")));
            return newStr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryException(ResultEnums.MD5_ERROR);
        }
    }

    /**
     * 转成16进制的md5加密
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String EncoderByMd5ForHex(String str) throws EncryException {
        try {
            //确定计算方法
            MessageDigest md5=MessageDigest.getInstance("MD5");
            //加密后的字符串
            String newStr = new BigInteger(1, md5.digest(str.getBytes("utf-8"))).toString(16);
            return newStr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryException(ResultEnums.MD5_ERROR);
        }
    }
}
