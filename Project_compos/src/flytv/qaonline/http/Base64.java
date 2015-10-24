/**
 * 文件名：Base64
 * 文件描述：Base64编码/转码
 * @author Jonze
 * @version 1.0.0
 */
package flytv.qaonline.http;

/**
 * Base64转换类：提供Base64与其他类型之间的相互转换
 *	@author Jonze
 */
public class Base64 {
	 final static String encodingChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	/**
	 * Translates the specified byte array into a Base64 string as per
	 * Preferences.put(byte[]).
	 */
	public static String byteArrayToBase64(byte[] a) {
		return byteArrayToBase64(a, false);
	}

	/**
	 * Translates the specified byte array into an "alternate representation"
	 * Base64 string. This non-standard variant uses an alphabet that does not
	 * contain the uppercase alphabetic characters, which makes it suitable for
	 * use in situations where case-folding occurs.
	 */
	public static String byteArrayToAltBase64(byte[] a) {
		return byteArrayToBase64(a, true);
	}

	private static String byteArrayToBase64(byte[] a, boolean alternate) {
		int aLen = a.length;
		int numFullGroups = aLen / 3;
		int numBytesInPartialGroup = aLen - 3 * numFullGroups;
		int resultLen = 4 * ((aLen + 2) / 3);
		StringBuffer result = new StringBuffer(resultLen);
		char[] intToAlpha = (alternate ? intToAltBase64 : intToBase64);

		// Translate all full groups from byte array elements to Base64
		int inCursor = 0;
		for (int i = 0; i < numFullGroups; i++) {
			int byte0 = a[inCursor++] & 0xff;
			int byte1 = a[inCursor++] & 0xff;
			int byte2 = a[inCursor++] & 0xff;
			result.append(intToAlpha[byte0 >> 2]);
			result.append(intToAlpha[(byte0 << 4) & 0x3f | (byte1 >> 4)]);
			result.append(intToAlpha[(byte1 << 2) & 0x3f | (byte2 >> 6)]);
			result.append(intToAlpha[byte2 & 0x3f]);
		}

		// Translate partial group if present
		if (numBytesInPartialGroup != 0) {
			int byte0 = a[inCursor++] & 0xff;
			result.append(intToAlpha[byte0 >> 2]);
			if (numBytesInPartialGroup == 1) {
				result.append(intToAlpha[(byte0 << 4) & 0x3f]);
				result.append("==");
			} else {
				// assert numBytesInPartialGroup == 2;
				int byte1 = a[inCursor++] & 0xff;
				result.append(intToAlpha[(byte0 << 4) & 0x3f | (byte1 >> 4)]);
				result.append(intToAlpha[(byte1 << 2) & 0x3f]);
				result.append('=');
			}
		}
		// assert inCursor == a.length;
		// assert result.length() == resultLen;
		return result.toString();
	}

	/**
	 * This array is a lookup table that translates 6-bit positive integer index
	 * values into their "Base64 Alphabet" equivalents as specified in Table 1
	 * of RFC 2045.
	 */
	private static final char intToBase64[] = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '+', '/' };

	/**
	 * This array is a lookup table that translates 6-bit positive integer index
	 * values into their "Alternate Base64 Alphabet" equivalents. This is NOT
	 * the real Base64 Alphabet as per in Table 1 of RFC 2045. This alternate
	 * alphabet does not use the capital letters. It is designed for use in
	 * environments where "case folding" occurs.
	 */
	private static final char intToAltBase64[] = { '!', '"', '#', '$', '%',
			'&', '\'', '(', ')', ',', '-', '.', ':', ';', '<', '>', '@', '[',
			']', '^', '`', '_', '{', '|', '}', '~', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '+', '?' };

	/**
	 * Translates the specified Base64 string (as per Preferences.get(byte[]))
	 * into a byte array.
	 * 
	 * @throw IllegalArgumentException if <tt>s</tt> is not a valid Base64
	 *        string.
	 */
	public static byte[] base64ToByteArray(String s) {
		return base64ToByteArray(s, false);
	}

	/**
	 * Translates the specified "alternate representation" Base64 string into a
	 * byte array.
	 * 
	 * @throw IllegalArgumentException or ArrayOutOfBoundsException if
	 *        <tt>s</tt> is not a valid alternate representation Base64 string.
	 */
	static byte[] altBase64ToByteArray(String s) {
		return base64ToByteArray(s, true);
	}

	private static byte[] base64ToByteArray(String s, boolean alternate) {
		byte[] alphaToInt = (alternate ? altBase64ToInt : base64ToInt);
		int sLen = s.length();
		int numGroups = sLen / 4;
		if (4 * numGroups != sLen)
			throw new IllegalArgumentException(
					"String length must be a multiple of four.");
		int missingBytesInLastGroup = 0;
		int numFullGroups = numGroups;
		if (sLen != 0) {
			if (s.charAt(sLen - 1) == '=') {
				missingBytesInLastGroup++;
				numFullGroups--;
			}
			if (s.charAt(sLen - 2) == '=')
				missingBytesInLastGroup++;
		}
		byte[] result = new byte[3 * numGroups - missingBytesInLastGroup];

		// Translate all full groups from base64 to byte array elements
		int inCursor = 0, outCursor = 0;
		for (int i = 0; i < numFullGroups; i++) {
			int ch0 = base64toInt(s.charAt(inCursor++), alphaToInt);
			int ch1 = base64toInt(s.charAt(inCursor++), alphaToInt);
			int ch2 = base64toInt(s.charAt(inCursor++), alphaToInt);
			int ch3 = base64toInt(s.charAt(inCursor++), alphaToInt);
			result[outCursor++] = (byte) ((ch0 << 2) | (ch1 >> 4));
			result[outCursor++] = (byte) ((ch1 << 4) | (ch2 >> 2));
			result[outCursor++] = (byte) ((ch2 << 6) | ch3);
		}

		// Translate partial group, if present
		if (missingBytesInLastGroup != 0) {
			int ch0 = base64toInt(s.charAt(inCursor++), alphaToInt);
			int ch1 = base64toInt(s.charAt(inCursor++), alphaToInt);
			result[outCursor++] = (byte) ((ch0 << 2) | (ch1 >> 4));

			if (missingBytesInLastGroup == 1) {
				int ch2 = base64toInt(s.charAt(inCursor++), alphaToInt);
				result[outCursor++] = (byte) ((ch1 << 4) | (ch2 >> 2));
			}
		}
		// assert inCursor == s.length()-missingBytesInLastGroup;
		// assert outCursor == result.length;
		return result;
	}

	/**
	 * Translates the specified character, which is assumed to be in the
	 * "Base 64 Alphabet" into its equivalent 6-bit positive integer.
	 * 
	 * @throw IllegalArgumentException or ArrayOutOfBoundsException if c is not
	 *        in the Base64 Alphabet.
	 */
	private static int base64toInt(char c, byte[] alphaToInt) {
		int result = alphaToInt[c];
		if (result < 0)
			throw new IllegalArgumentException("Illegal character " + c);
		return result;
	}

	/**
	 * This array is a lookup table that translates unicode characters drawn
	 * from the "Base64 Alphabet" (as specified in Table 1 of RFC 2045) into
	 * their 6-bit positive integer equivalents. Characters that are not in the
	 * Base64 alphabet but fall within the bounds of the array are translated to
	 * -1.
	 */
	private static final byte base64ToInt[] = { -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1,
			-1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
			13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1,
			-1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
			41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };

	/**
	 * This array is the analogue of base64ToInt, but for the nonstandard
	 * variant that avoids the use of uppercase alphabetic characters.
	 */
	private static final byte altBase64ToInt[] = { -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1,
			62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13,
			14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18,
			19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
			40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 22, 23, 24, 25 };

    /**
     * Returns the base 64 encoded equivalent of a supplied string.
     * @param source the string to encode
     */
    public static String encode( String source ) {
        char[] sourceBytes = getPaddedBytes( source );
        int numGroups = (sourceBytes.length + 2) / 3;
        char[] targetBytes = new char[4];
        char[] target = new char[ 4 * numGroups ];

        for (int group = 0; group < numGroups; group++) {
            convert3To4( sourceBytes, group*3, targetBytes );
            for (int i = 0; i < targetBytes.length; i++) {
                target[ i + 4*group ] = encodingChar.charAt( targetBytes[i] );
            }
        }

        int numPadBytes = sourceBytes.length - source.length();

        for (int i = target.length-numPadBytes; i < target.length; i++) target[i] = '=';
        return new String( target );
    }


    private static char[] getPaddedBytes( String source ) {
        char[] converted = source.toCharArray();
        int requiredLength = 3 * ((converted.length+2) /3);
        char[] result = new char[ requiredLength ];
        System.arraycopy( converted, 0, result, 0, converted.length );
        return result;
    }


    private static void convert3To4( char[] source, int sourceIndex, char[] target ) {
        target[0] = (char) ( source[ sourceIndex ] >>> 2);
        target[1] = (char) (((source[ sourceIndex   ] & 0x03) << 4) | (source[ sourceIndex+1 ] >>> 4));
        target[2] = (char) (((source[ sourceIndex+1 ] & 0x0f) << 2) | (source[ sourceIndex+2 ] >>> 6));
        target[3] = (char) (  source[ sourceIndex+2 ] & 0x3f);
    }


    /**
     * Returns the plaintext equivalent of a base 64-encoded string.
     * @param source a base 64 string (which must have a multiple of 4 characters)
     */
    public static String decode( String source ) {
        if (source.length()%4 != 0) throw new RuntimeException( "valid Base64 codes have a multiple of 4 characters" );
        int numGroups = source.length() / 4;
        int numExtraBytes = source.endsWith( "==" ) ? 2 : (source.endsWith( "=" ) ? 1 : 0);
        byte[] targetBytes = new byte[ 3*numGroups ];
        byte[] sourceBytes = new byte[4];
        for (int group = 0; group < numGroups; group++) {
            for (int i = 0; i < sourceBytes.length; i++) {
                sourceBytes[i] = (byte) Math.max( 0, encodingChar.indexOf( source.charAt( 4*group+i ) ) );
            }
            convert4To3( sourceBytes, targetBytes, group*3 );
        }
        return new String( targetBytes, 0, targetBytes.length - numExtraBytes );
    }

    private static void convert4To3( byte[] source, byte[] target, int targetIndex ) {
        target[ targetIndex  ]  = (byte) (( source[0] << 2) | (source[1] >>> 4));
        target[ targetIndex+1 ] = (byte) (((source[1] & 0x0f) << 4) | (source[2] >>> 2));
        target[ targetIndex+2 ] = (byte) (((source[2] & 0x03) << 6) | (source[3]));
    }
}
