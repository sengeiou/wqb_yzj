package com.wqb.commons;

import java.io.*;
import java.util.Properties;

public class PropertiesUtil {

    /**
    * @author Ben
    * @date 2019-04-02
    * @Description read properties file
    * @param filePath, key
    * @return java.lang.String
    */
    public static String getProperties(String filePath, String key) throws Exception {
        Properties props = new Properties();//使用Properties类来加载属性文件
        FileInputStream iFile = new FileInputStream(filePath);
        props.load(iFile);

        String value = props.getProperty(key);
        iFile.close();
        return value;
    }

    /**
     * write properties file
     *
     * @param filePath file path
     * @throws IOException
     */
    public static void setProperties (String filePath, String pKey, String pValue) throws IOException {
        ///保存属性到b.properties文件
        Properties props = new Properties();

        props.load(new FileReader(filePath));
        props.setProperty(pKey, pValue);
        //store(OutputStream,comments):store(输
        // 出流，注释)
        FileOutputStream oFile = new FileOutputStream(filePath);//true表示追加打开
        props.store(oFile, "Set " + pKey);
        oFile.close();
    }
}
