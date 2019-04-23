package com.wqb.service.documents.impl;

import com.wqb.common.UUIDUtils;
import com.wqb.dao.documents.TBasicDocumentsConfigMapper;
import com.wqb.model.TBasicSubjectParent;
import com.wqb.service.documents.TBasicDocumentsConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Transactional
@Service("tBasicDocumentsConfigService")
public class TBasicDocumentsConfigServiceImpl implements TBasicDocumentsConfigService {

    @Autowired
    TBasicDocumentsConfigMapper tBasicDocumentsConfigMapper;

    @Override
    public Map<String, Object> insertDocumentsConfig(List<Map<String, Object>> list, Map<String, String> map) {
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

                tBasicDocumentsConfigMapper.insertDocumentsConfig(subParent);
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

}
