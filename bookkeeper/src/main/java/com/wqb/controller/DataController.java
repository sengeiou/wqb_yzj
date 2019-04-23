package com.wqb.controller;

import com.alibaba.fastjson.JSONObject;
import com.wqb.common.BusinessException;
import com.wqb.dao.user.UserDao;
import com.wqb.httpClient.HttpClientUtil;
import com.wqb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/data")
public class DataController extends BaseController {
    @Value("${wqb_url}")
    private String url;
    @Value("${wqb_charset}")
    private String charset;
    @Autowired
    private UserDao userDao;
    private HttpClientUtil httpClientUtil = new HttpClientUtil();

    @RequestMapping("/dataHandle")
    @ResponseBody
    Map<String, Object> dataHandle() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<User> userList = userDao.queryAllUser();
            for (int i = 0; i < userList.size(); i++) {
                User user = userList.get(i);
                String loginUser = user.getLoginUser();
                String id = user.getId();
                if (loginUser != null && !"".equals(loginUser)) {
                    if (id != null && !"".equals(id)) {
                        String httpOrgCreateTest = url + "Customer/Test";
                        Map<String, String> createMap1 = new HashMap<String, String>();
                        createMap1.put("mobile", loginUser);
                        String httpOrgCreateTestRtn = null;
                        try {
                            httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest, createMap1, charset);
                            Map<?, ?> maps = (Map<?, ?>) JSONObject.parse(httpOrgCreateTestRtn);
                            Map<?, ?> map1 = (Map<?, ?>) maps.get("data");
                            if (map1 != null) {
                                String ID = map1.get("Id").toString();
                                String mobile = map1.get("Mobile").toString();
                                if (ID != null && !"".equals(ID)) {
                                    if ((id == null || "".equals(id)) || !ID.equals(id)) {
                                        // 把商城ID 更新到对应的财税系统ID
                                        Map<String, Object> param = new HashMap<String, Object>();
                                        param.put("id", ID);
                                        param.put("mobile", mobile);
                                        userDao.updCsID(param);
                                    }
                                }
                            } else {
                                // 根据手机号查不到用户
                                Map<String, String> createMap2 = new HashMap<String, String>();
                                createMap1.put("id", id);
                                httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest, createMap2, charset);
                                Map<?, ?> maps1 = (Map<?, ?>) JSONObject.parse(httpOrgCreateTestRtn);
                                Map<?, ?> map2 = (Map<?, ?>) maps1.get("data");
                                if (map2 != null) {
                                    String ID = map2.get("Id").toString();
                                    String mobile = map2.get("Mobile").toString();
                                    if (mobile != null && !"".equals(mobile)) {
                                        if ((loginUser == null || "".equals(loginUser)) || !loginUser.equals(mobile)) {
                                            // 把商城手机号更新到财税
                                            Map<String, Object> param = new HashMap<String, Object>();
                                            param.put("id", ID);
                                            param.put("mobile", mobile);
                                            userDao.updCsMobile(param);
                                        }
                                    }
                                } else {
                                    // 删除用户(谨慎操作)

                                }
                            }
                        } catch (Exception e) {
                            throw new BusinessException("调用商城登录接口异常!");
                        }
                    }
                }
            }
            result.put("success", "true");
            result.put("message", "商城和财税用户数据维护成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", "false");
            result.put("message", "商城和财税用户数据维护异常!");
            return result;
        }
    }
}
