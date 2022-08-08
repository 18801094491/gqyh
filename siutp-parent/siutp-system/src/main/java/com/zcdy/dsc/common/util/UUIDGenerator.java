package com.zcdy.dsc.common.util;


import java.net.InetAddress;

/**
 * @author： Roberto
 * 创建时间：2020年2月25日 上午9:49:26
 * 描述: <p>uuid生成器</p>
 */
public class UUIDGenerator {
	/**
	 * @author：Roberto
	 * @create:2020年2月25日 上午9:49:02
	 * 描述:<p>产生一个32位的UUID</p>
	 */
	public static String generate() {
		return new StringBuilder(32).append(format(getIp())).append(
				format(getJvm())).append(format(getHiTime())).append(
				format(getLoTime())).append(format(getCount())).toString();
		
	}

	private static final int IP;
	
	static {
		int ipadd;
		try {
			ipadd = toInt(InetAddress.getLocalHost().getAddress());
		} catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
	}

	private static short counter = (short) 0;

	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

	private final static String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuilder buf = new StringBuilder("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	private final static String format(short shortval) {
		String formatted = Integer.toHexString(shortval);
		StringBuilder buf = new StringBuilder("0000");
		buf.replace(4 - formatted.length(), 4, formatted);
		return buf.toString();
	}

	private final static int getJvm() {
		return JVM;
	}

	private final static short getCount() {
		synchronized (UUIDGenerator.class) {
			if (counter < 0) {
				counter = 0;
			}
			return counter++;
		}
	}

	/**
	 * Unique in a local network
	 */
	private final static int getIp() {
		return IP;
	}

	/**
	 * Unique down to millisecond
	 */
	private final static short getHiTime() {
		return (short) (System.currentTimeMillis() >>> 32);
	}

	private final static int getLoTime() {
		return (int) System.currentTimeMillis();
	}

	private final static int toInt(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}

}
