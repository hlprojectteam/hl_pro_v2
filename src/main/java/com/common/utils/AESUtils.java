package com.common.utils;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESUtils {

	private static final Logger logger = LoggerFactory.getLogger(AESUtils.class);  
	
	public static void main(String[] args) throws IOException {
		String content = "谁123";
		String password = "12345678";  
		//加密  
		logger.info("加密前：" + content);  
		byte[] encryptResult = encrypt(content, password);  
		BASE64Encoder encoder = new BASE64Encoder();
		String str = encoder.encode(encryptResult);//传输前BASE64编码
		logger.info("传输密文字符串"+str);
		//解密  
		BASE64Decoder decoder = new BASE64Decoder(); 
		byte[] decryptResult = decrypt(decoder.decodeBuffer(str),password);  
		logger.info("解密后：" + new String(decryptResult));  
	}
    /**
     * 加密
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return
     */
    public static byte[] encrypt(String content, String password) {
    	byte[] result = null;
        try {           
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            result = cipher.doFinal(byteContent);// 加密
        } catch (Exception e) {
        	logger.error("AESUtils", e);
        }
        return result;
    }
    
    /**解密
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] content, String password) {
    	byte[] result = null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");            
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            result = cipher.doFinal(content);// 加密
        } catch (Exception e) {
        	logger.error("AESUtils", e);
        }
        return result;
    }
}
