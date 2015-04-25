package tools;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class TwittiKeyGenerator {
	
	
	
	public static String generateKey (String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		StringBuilder msgBuf= new StringBuilder();
		msgBuf.append(key);
		msgBuf.append(System.currentTimeMillis());
		String message= msgBuf.toString();
				
				
		byte[] bytesOfMessage = null;
		bytesOfMessage = message.getBytes("UTF-8");

		MessageDigest md = null;
		md = MessageDigest.getInstance("MD5");

		byte[] thedigest = md.digest(bytesOfMessage);

		BigInteger bigInt = new BigInteger(1,thedigest);
		String str = bigInt.toString(16);

		return str;
	}
}
