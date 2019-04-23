package com.wqb.service.subject.impl;

import com.wqb.common.UUIDUtils;
import com.wqb.dao.subject.TBasicSubjectParentMapper;
import com.wqb.model.Account;
import com.wqb.model.TBasicSubjectParent;
import com.wqb.model.User;
import com.wqb.service.subject.TBasicSubjectParentService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("TBasicSubjectParentService")
public class TBasicSubjectParentServiceImpl implements TBasicSubjectParentService {
    @Autowired
    TBasicSubjectParentMapper tBasicSubjectParentMapper;

    @Override
    public Map<String, Object> insertSubParent(List<Map<String, Object>> list, Map<String, String> map) {
        Map<String, Object> result = new HashMap<String, Object>();
        System.out.println(new Date().getTime());
        result.put("code", -1);
        //定义科目表对象
        TBasicSubjectParent subParent = new TBasicSubjectParent();
        try {
            //循环遍历每一行excel表数据
            for (int i = 0; i < list.size(); i++)//从第一行开始读
            {
                int j = i + 2; //获取真实行数
                subParent = new TBasicSubjectParent();

                Map<String, Object> subParentMap = list.get(i);//获取一行科目表数据
                subParent.setPkSubPareenId(UUIDUtils.getUUID()); //设置主键
                subParent.setMender(map.get("userID")); //设置修改者

                String subCode = subParentMap.get("map0").toString().trim(); //科目代码
                if (null == subCode) subCode = "";
                subParent.setSubCode(subCode.split("\\.")[0]); // 截取字符串xx.xx  包括点后面所有的

                Object subName = subParentMap.get("map1"); //科目名称
                if (null == subName) subName = "";
                subParent.setSubName(subName.toString().trim());

                String debitCreditDirection = subParentMap.get("map2").toString().trim(); //借贷方向
                subParent.setDebitCreditDirection(debitCreditDirection);

                String category = subParentMap.get("map3").toString().trim();//类别(一、资产类二、负债类　三、共同类　四、所有者权益类五、成本类六、损益类)
                if (null == category) category = "";
                subParent.setCategory(category.split("\\.")[0]); // 截取字符串xx.xx  包括点后面所有的

                subParent.setAccountingStandard("新会计准则");//设置会计准则

                String timestamp = String.valueOf(new Date().getTime());
                subParent.setUpdateTimestamp(timestamp); //更新 时间（时间戳）

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				sdf.parse(source)

                if (StringUtils.isBlank(list.get(0).get("map0").toString().trim()))
                    return result; // 如果第一行的科目代码为空时 返回错误信息

                tBasicSubjectParentMapper.insertSubParent(subParent);
                result.put("result", "导入失败，必填字段有空，请检查第" + j + "行，状态的属性。");
            }

            System.out.println(new Date().getTime());
            result.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("result", "导入完成");
        return result;
    }

    @Override
    public Map<String, Object> querySubParent(HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            param.put("userID", user.getUserID());
            param.put("accountID", account.getAccountID());
            List<TBasicSubjectParent> list = tBasicSubjectParentMapper.querySubParent(param);
            result.put("list", list);
            result.put("message", "success");
        } catch (Exception e) {
            result.put("message", "fail");
            return result;
        }
        return result;
    }

    @Override
    public List<TBasicSubjectParent> querySubParentList(HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<TBasicSubjectParent> list = null;
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            param.put("userID", user.getUserID());
            param.put("accountID", account.getAccountID());
            list = tBasicSubjectParentMapper.querySubParent(param);
        } catch (Exception e) {
            return list;
        }
        return list;
    }
}
