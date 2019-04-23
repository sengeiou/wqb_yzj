package com.wqb.services.impl.tax;

import com.wqb.commons.PropertiesUtil;
import com.wqb.commons.emun.PriorityServ;
import com.wqb.commons.emun.ResultStatus;
import com.wqb.commons.vo.TaxVO;
import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.domains.tax.Ticket;
import com.wqb.services.tax.TaxService;
import com.wqb.supports.exceptions.BizException;
import com.wqb.supports.util.SpringContextUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Author: Ben
 * @Date: 2019-04-04
 * @Description: 接口方法工厂
 */

@Service
public class TaxFactory {
    private static final String CONFIG_PATH = new File("src/main/resources/config.properties").getPath();

    public Ticket getVatTicket(TaxVO taxVO, User user, Account account) {
        Ticket ticket = null;
        int priority=1;
        int retryCount=1;
        try {
            priority = Integer.valueOf(PropertiesUtil.getProperties(CONFIG_PATH, "priority"));
            retryCount = Integer.valueOf(PropertiesUtil.getProperties(CONFIG_PATH, "retryCount"));
            SortedMap<Integer, PriorityServ> map = new TreeMap<>();
            for (PriorityServ serv : PriorityServ.values()) {//先按priority排序
                map.put(serv.getPriority(), serv);
            }
            for (PriorityServ serv : map.values()) {
                if (Objects.equals(serv.getPriority(),priority) && serv.getRetryCount() > retryCount) {
                    TaxService tax = SpringContextUtils.getBean(serv.getBeanName(), TaxService.class);
                    if (StringUtils.isNotBlank(taxVO.getTicketId()))
                        ticket = tax.getVatTicke(taxVO.getTicketId(),user,account);
                    else if (ObjectUtils.allNotNull(taxVO.getImageStream(),user,account))
                        ticket = tax.getVatTicke(taxVO.getImageStream(),user,account);
                    else if (ObjectUtils.anyNotNull(taxVO.getTicketImage()))
                        ticket = tax.getVatTicke(taxVO.getTicketImage(),user,account);
                    if (ticket.getResultStatus() != ResultStatus.SUCCESS) {
                        retryCount++;
                        PropertiesUtil.setProperties(CONFIG_PATH, "retryCount", String.valueOf(retryCount));
                    } else {
                        break;
                    }
                } else {
                    priority++;
                    PropertiesUtil.setProperties(CONFIG_PATH, "priority", String.valueOf(priority));
                }
            }
            if (priority > map.values().size()) {
                PropertiesUtil.setProperties(CONFIG_PATH, "priority", "1");
                PropertiesUtil.setProperties(CONFIG_PATH, "retryCount", "1");
            }
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            retryCount++;
            try {
                PropertiesUtil.setProperties(CONFIG_PATH, "retryCount", String.valueOf(retryCount));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            throw new BizException(String.format("发票识别失败！"));
        }
        return ticket;
    }
}
