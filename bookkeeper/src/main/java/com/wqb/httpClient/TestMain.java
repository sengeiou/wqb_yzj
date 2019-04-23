package com.wqb.httpClient;

import java.util.HashMap;
import java.util.Map;

//对接口进行测试
public class TestMain {
    //	正式环境
//	private String url = "http://api.wqbol.com/";
    // 测试环境
    private String url = "http://api.wqbol.net/";
    String Token = "4BCED7FC56C8B6E9926EB57638123C888704DBF4C56A4AEA9616CCA4770A801C9597678B94FF7D58111EE06DE5ED46DE27169D049012CB816D3365619E0B4928E609240682FD763B77D86A6D19E15C5347D1781DB33B25573B215106DCE5EE5A160B4B725BEA94B5FEC9EDE19F14FD09A83ACAB7CE9FE3EBE27F65927BCF359E931391685FB21773CE2F06BB3D0061482471B1C3EC3514EADE9E621C1A902336";

    private String charset = "utf-8";
    private HttpClientUtil httpClientUtil = null;

    public TestMain() {
        httpClientUtil = new HttpClientUtil();
    }

    public void test() {
        String httpOrgCreateTest = url + "/Customer/GetCustomerInfo";
        Map<String, String> createMap = new HashMap<String, String>();
        createMap.put("authuser", "*****");
        createMap.put("authpass", "*****");
        createMap.put("orgkey", "****");
        createMap.put("orgname", "****");
        createMap.put("Token", Token);
        String httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest, createMap, charset);
        System.out.println("result:" + httpOrgCreateTestRtn);
    }

    public static void main(String[] args) {
        TestMain main = new TestMain();
        main.test();
    }
}
