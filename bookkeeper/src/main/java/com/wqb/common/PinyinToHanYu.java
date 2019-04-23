package com.wqb.common;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class PinyinToHanYu {

    private static Properties unicodeToHanyuPinyinTable = new Properties();
    private Resource configLocation;
	/*public pinyinToHanYu() throws Exception {
		unicodeToHanyuPinyinTable.load(new BufferedInputStream(pinyin2.class.getResourceAsStream("/config/unicode_to_hanyu_pinyin.txt")));
	}*/

    @SuppressWarnings({"unused", "static-access"})
    public void setConfigLocation(Resource configLocation) throws IOException {
        this.configLocation = configLocation;
        this.unicodeToHanyuPinyinTable.load(configLocation.getInputStream());
	/*	boolean exists = configLocation.exists();
		String filename = configLocation.getFilename();
		InputStream inputStream = configLocation.getInputStream();
		URI uri = configLocation.getURI();
		System.out.println();*/
    }

    public Resource getConfigLocation() throws IOException {
        return this.configLocation;
    }

    @SuppressWarnings("static-access")
    public String getPinYin(String ss) throws Exception {
        //String ss = "a深圳微期宝有限公司";
        if (StringUtil.isEmpty(ss)) {
            return null;
        }
        ss = ss.trim().replace(" ", "");
        StringBuffer bf = new StringBuffer();
        char[] charArray = ss.toCharArray();
        try {
            //这个汉字对应的拼音
            for (int i = 0; i < charArray.length; i++) {
                char cc = charArray[i];
                int m = cc;
                if (cc > 127) {
                    String str1 = Integer.toHexString(m).toUpperCase(); // 返回 整数参数所表示的值以十六进制
                    String str2 = this.unicodeToHanyuPinyinTable.getProperty(str1); //查找对应的拼音
                    if ((null != str2) && (!(str2.equals("(none0)"))) && (str2.startsWith("(")) && (str2.endsWith(")"))) {
                        int k1 = str2.indexOf("(");
                        String str3 = str2.substring(++k1, str2.lastIndexOf(")"));
                        String[] arr3 = str3.split(",");
                        char charAt = arr3[0].charAt(0);  //z
                        bf.append(charAt);
                    } else {
                        //str2 == null  //没有找到汉字对应的拼音
                        bf.append(cc);
                    }
                } else if (cc >= 48 && cc <= 57) {  //str.matches("[0-9]+")) {// 如果字符是数字,取数字
                    //ASCII 48-57
                    bf.append(cc);
                } else if (String.valueOf(cc).matches("[a-zA-Z]+")) {
                    // 如果字符是字母,取字母)
                    bf.append(cc);
                } else {// 否则不转换
                    bf.append(cc);//如果是标点符号的话，带着
                }
            }
            return bf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("PinyinToHanYu--" + ss);
        }

    }

}
