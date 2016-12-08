package com.example.improvedbaseadapter;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
/**
 * Android 3DES加密工具类
 */
public class AndroidDes3Util {

    // 密钥 长度不得小于24
       private final static String secretKey = "123456789012345678901234" ;
       // 向量 可有可无 终端后台也要约定
       private final static String iv = "01234567" ;
       // 加解密统一使用的编码方式
       private final static String encoding = "utf-8" ;
        public static final String DESEDE_CBC_PKCS5_PADDING = "desede/CBC/PKCS5Padding";
        public static final String DESEDE = "desede";

    /**
       * 3DES加密
       *
       * @param plainText
       *            普通文本
       * @return
       * @throws Exception
       */
       public static String encode(String plainText) throws Exception {
            Key deskey = null ;
            DESedeKeySpec spec = new DESedeKeySpec(secretKey .getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(DESEDE);
            deskey = keyfactory.generateSecret(spec);
 
            Cipher cipher = Cipher.getInstance(DESEDE_CBC_PKCS5_PADDING);
            IvParameterSpec ips = new IvParameterSpec( iv.getBytes());
            cipher.init(Cipher. ENCRYPT_MODE , deskey, ips);
             byte [] encryptData = cipher.doFinal(plainText.getBytes(encoding ));
             return Base64.encode(encryptData  );
      }
 
       /**
       * 3DES解密
       *
       * @param encryptText
       *            加密文本
       * @return
       * @throws Exception
       */
       public static String decode(String encryptText) throws Exception {
            Key deskey = null ;
            DESedeKeySpec spec = new DESedeKeySpec( secretKey.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(DESEDE);
            deskey = keyfactory. generateSecret(spec);
            Cipher cipher = Cipher.getInstance( DESEDE_CBC_PKCS5_PADDING );
            IvParameterSpec ips = new IvParameterSpec( iv.getBytes());
            cipher. init(Cipher. DECRYPT_MODE, deskey, ips);
 
             byte [] decryptData = cipher.doFinal(Base64. decode(encryptText   ));
 
             return new String (decryptData, encoding);
      }
}