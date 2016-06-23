package com.frame.base.utl.util.sign;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * DES加密算法
 * cangfei.hgy
 */
public class DESede {
    public static final String ISO88591 = "ISO-8859-1";
    private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish

    /**
     * 加密
     *
     * @param    key        加密键
     * @param    data    加密内容
     * @return 加密后数据
     */
    public static byte[] encryptMode(byte key[], byte data[]) {
        try {
            byte[] k = new byte[24];

            if (key.length >= 24) {
                System.arraycopy(key, 0, k, 0, 24);
            } else {
                System.arraycopy(key, 0, k, 0, key.length);
            }

            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance(Algorithm);
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(Algorithm);
            c.init(Cipher.ENCRYPT_MODE, ky);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 解密
     *
     * @param    key        解密键
     * @param    data    解密内容
     * @return 解密后数据
     */
    public static byte[] decryptMode(byte key[], byte data[]) {
        try {
            //        	data = Base64.decodeBase64(data);
            byte[] k = new byte[24];
            if (key.length >= 24) {
                System.arraycopy(key, 0, k, 0, 24);
            } else {
                System.arraycopy(key, 0, k, 0, key.length);
            }
            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance(Algorithm);
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(Algorithm);
            c.init(Cipher.DECRYPT_MODE, ky);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}