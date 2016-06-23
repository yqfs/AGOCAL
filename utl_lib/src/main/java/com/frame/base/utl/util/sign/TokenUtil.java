package com.frame.base.utl.util.sign;


import android.util.Log;


import com.frame.base.utl.application.BaseApplication;
import com.frame.base.utl.util.local.PhoneInfo;

import org.apache.commons.codec.binary.Base64;


/**
 * TOKEN工具
 *
 * @author YANGQIYUN 2013-10-30
 */
public class TokenUtil {

    /**
     * 加密TOKEN
     *
     * @param token 未加密的TOKEN
     * @return
     */
    public static String encodeToken(String token) {
        String encodedToken;
        try {
            byte[] encoded = Base64.encodeBase64(
                DESede.encryptMode(PhoneInfo.getImsi(BaseApplication.getContext()).getBytes(), token.getBytes(DESede.ISO88591)));
            encodedToken = new String(encoded, DESede.ISO88591);
        } catch (Exception e) {
            Log.e(TokenUtil.class.getName(), "加密TOKEN失败", e);
            encodedToken = token;
        }
        return encodedToken;
    }

    /**
     * 解密TOKEN
     *
     * @param token 被加密的TOKEN
     * @return
     */
    public static String decodeToken(String token) {
        String decodedToken;
        try {
            byte[] srcBytes = DESede.decryptMode(PhoneInfo.getImsi(BaseApplication.getContext()).getBytes(),
                    Base64.decodeBase64(token.getBytes(DESede.ISO88591)));
            decodedToken = new String(srcBytes, DESede.ISO88591);
        } catch (Exception e) {
            Log.e("AutoLoginConnectorHelper", "解密token失败", e);
            decodedToken = token;
        }
        return decodedToken;
    }
}
