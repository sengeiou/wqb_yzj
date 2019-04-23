package com.wqb.common;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileDownload {

    public static void filedownload(final HttpServletResponse response, String filePath, String fileName) throws IOException {
        byte[] data = toByteArray(filePath);
        fileName = URLEncoder.encode(fileName, "utf-8");
        response.reset();
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + fileName + "\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(data);
        ops.flush();
        ops.close();
        response.flushBuffer();
    }

    /**
     * 转化二进制流
     *
     * @param filePath
     * @return byte[]
     * @throws IOException
     */
    public static byte[] toByteArray(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }

        FileChannel channel = null;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
            channel = fis.getChannel();   //创建文件读取渠道
            ByteBuffer bf = ByteBuffer.allocate((int) channel.size());  //分配子节缓冲区
            while ((channel.read(bf)) > 0) {
                //读取缓冲区内容
            }
            return bf.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
