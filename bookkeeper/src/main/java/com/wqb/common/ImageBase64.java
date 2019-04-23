package com.wqb.common;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class ImageBase64 {
    /**
     * 将图片转化为base64编码
     *
     * @param imgSrcPath 生成64编码的图片路径
     * @return base64编码后的二进制文件
     */
    public static String getImageStr(String imgSrcPath) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgSrcPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * 将字符串转码为图片
     *
     * @param imgStr        图片字符串
     * @param imgCreatePath 生成图片路径
     */
    public static boolean generateImage(String imgStr, String imgCreatePath) {
        if (imgStr == null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] data = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < data.length; i++) {
                if (data[i] < 0) {
                    data[i] += 256;
                }
            }
            OutputStream os = new FileOutputStream(imgCreatePath);
            os.write(data);
            os.flush();
            os.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

}
