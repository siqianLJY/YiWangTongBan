package com.synjones.cpucard;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DesECBUtil {  
	
	private static final String TripeDes = "DESede/ECB/NoPadding"; 
	private static final String Des = "DES/ECB/NoPadding"; 
    /** 
     * 加密数据 
     * @param encryptKey
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptDES(byte encryptKey[],byte encryptbytes[] ) throws Exception {  
        SecretKeySpec key = new SecretKeySpec(getKey(encryptKey), "DES");  
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");  
        cipher.init(Cipher.ENCRYPT_MODE, key);  
        byte[] encryptedData = cipher.doFinal(encryptbytes);  
        return encryptedData;  
    }  
    
    public static byte[] encryptTripleDES(byte encryptKey[],byte encryptbytes[] ) throws Exception {  
        SecretKeySpec key = new SecretKeySpec(getTripleKey(encryptKey), "DESede");  
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");  
        cipher.init(Cipher.ENCRYPT_MODE, key);  
        byte[] encryptedData = cipher.doFinal(encryptbytes);  
        return encryptedData;  
    } 
      
    /** 
     * 自定义一个key 
     */
    public static byte[] getKey(byte keyByte[]) {  
        Key key = null;  
        key = new SecretKeySpec(keyByte, "DES");  
        return key.getEncoded();  
    }  
    public static byte[] getTripleKey(byte keyByte[]) {  
        Key key = null;  
        key = new SecretKeySpec(keyByte, "DESede");  
        return key.getEncoded();  
    } 
      
    /*** 
     * 解密数据 
     * @param decryptKey
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptDES( byte decryptKey[],byte decryptbytes[]) throws Exception {  
        SecretKeySpec key = new SecretKeySpec(getKey(decryptKey), "DES");  
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");  
        cipher.init(Cipher.DECRYPT_MODE, key);  
        byte decryptedData[] = cipher.doFinal(decryptbytes);  
        return decryptedData;  
    }  
    
    public static byte[] decryptTripleDES( byte decryptKey[],byte decryptbytes[]) throws Exception {  
        SecretKeySpec key = new SecretKeySpec(getTripleKey(decryptKey), "DESede");  
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");  
        cipher.init(Cipher.DECRYPT_MODE, key);  
        byte decryptedData[] = cipher.doFinal(decryptbytes);  
        return decryptedData;  
    }  
}
