package com.wqb.services.impl.tax;

import com.wqb.commons.vo.TaxVO;
import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.supports.exceptions.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @Author: Ben
 * @Date: 2019-04-04
 * @Description: 接口方案优先级控制和方法调度
 */
@Service
public class TaxProvider {

    private static final String CONFIG_PATH = new File("src/main/resources/config.properties").getPath();

    @Autowired
    private TaxFactory taxFactory;

    public Object getTaxInfo(TaxVO taxVO, User user, Account account) {
        Method method = ReflectionUtils.findMethod(taxFactory.getClass(), taxVO.getAction().getCode(), TaxVO.class,User.class,Account.class);
        if (method==null) {
            throw new BizException(String.format("找不到方法：%s",taxVO.getAction().getCode()));
        }
        return ReflectionUtils.invokeMethod(method, taxFactory, taxVO,user,account);
    }

}
