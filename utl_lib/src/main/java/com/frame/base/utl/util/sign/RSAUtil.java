package com.frame.base.utl.util.sign;

import com.frame.base.utl.log.DebugLog;

import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

/**
 * RSA 工具类。提供加密
 *
 * @author YANGQIYUN on 2015/7/28
 */
public class RSAUtil {

  public static RSAPublicKey pubKey;

  /**
   * 生成公钥
   */
  public static RSAPublicKey generateRSAPublicKey(String spubKey) {
    if (spubKey != null && !spubKey.equals("")) {
      BufferedReader br = new BufferedReader(new StringReader(spubKey));

      try {
        String readstr = br.readLine();
        BigInteger modulus = new BigInteger(readstr);

        readstr = br.readLine();
        BigInteger exponent = new BigInteger(readstr);

        KeyFactory fact = KeyFactory.getInstance("RSA", "BC");
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, exponent);
        pubKey = (RSAPublicKey) fact.generatePublic(pubKeySpec);
      } catch (Exception e) {
        DebugLog.d("RSAUtil", "生成公钥错误！");
      }
    }
    return pubKey;
  }

  public static String encrypt(String inputStr) throws Exception {
    SecureRandom rand = new FixedSecureRandom();
    Cipher cipher = Cipher.getInstance("RSA", "BC");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey, rand);
    byte[] out = cipher.doFinal(inputStr.getBytes());
    return HexUtil.bytesToHexString(out);
  }
}