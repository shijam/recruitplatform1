/**
 * 文件名：MD5Wrapper
 * 文件描述：MD5消息摘要算法
 * @author Jonze
 * @version 1.0.0
 */
package flytv.qaonline.http;

import java.security.MessageDigest;

/** 
 * 此类用于MD5相关的加解密操作
 *  MD5签名与验签
 *	@author Jonze
 */
public class MD5 {
	private static final String ENCODING = "utf-8";// 编码方式
	private static final String MD5_INSTANCE = "MD5";
	
    public static String md5Password(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};       
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
	/**
	 * MD5签名
	 * 
	 * @param plainText
	 *            待加密字符串
	 * @return 返回加密后的字符串 并用BASE64编码 BASE64(MD5(src))
	 * @throws Exception
	 */
	public static String encryptMD5ToString(String plainText) throws Exception {// MD5加密
		if(plainText == null){
			return null;
		}
		MessageDigest md5 = MessageDigest.getInstance(MD5_INSTANCE);
		md5.update(plainText.getBytes(ENCODING));
		byte[] digestBytes = md5.digest();
		String md5Digest = Base64.byteArrayToBase64(digestBytes);
		return md5Digest;
	}
	
	/**
	 * MD5加密
	 * 
	 * @param plainText
	 *            待加密字符串
	 * @return 返回加密后的字节数组
	 * @throws Exception
	 */
	public static byte[] encryptMD5ToBytes(String plainText) throws Exception {// MD5加密
		if(plainText == null){
			return null;
		}
		MessageDigest md5 = MessageDigest.getInstance(MD5_INSTANCE);
		md5.update(plainText.getBytes(ENCODING));
		return md5.digest();
	}

	/**
	 * MD5验证
	 * 
	 * @param message
	 *            明文
	 * @param MD5Code
	 *            MD5加密的数据
	 * @return true为验证通过，false为未通过
	 * @throws Exception
	 */
	public static boolean verifyMD5(String message, String sign)// MD5验证
			throws Exception {
		if(message == null || sign == null){
			return false;
		}
		MessageDigest md5 = MessageDigest.getInstance(MD5_INSTANCE);
		md5.update(message.getBytes(ENCODING));
		byte[] digestBytes = md5.digest();
		String md5Digest = Base64.byteArrayToBase64(digestBytes);
		return md5Digest.trim().equals(sign.trim()) ? true : false;
	}

}
