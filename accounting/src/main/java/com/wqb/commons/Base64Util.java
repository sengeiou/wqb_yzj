package com.wqb.commons;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Base64Util {
	/**
	 * 解码
	 * 
	 * @param requestString
	 * @return
	 * @throws IOException
	 */
	public static byte[] base64Decoder(String requestString) throws IOException {
		return new BASE64Decoder().decodeBuffer(requestString);
	}

	/*
	 * 编码
	 */
	public static String base64encoder(byte[] bytes) throws IOException {
		BASE64Encoder enc = new BASE64Encoder();
		String encStr = enc.encode(bytes);
		return encStr;
	}

	// 加密
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	// 解密
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String getImgStr(String imgFile) throws IOException {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64encoder(data);
	}
}
