package com.wqb.dao.receipt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wqb.common.BusinessException;
import com.wqb.common.StringUtil;
import com.wqb.common.SubjectUtils;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.documents.TBasicDocumentsMapper;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.service.documents.TBasicDocumentsService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import com.wqb.service.vat.VatService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Repository
public class ReceiptDao {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    public List<Receipt> selectSalesList(String accountId, String period) {
        SqlSession sqlsession = null;
        try {
            sqlsession = sqlSessionFactory.openSession();

            Receipt receipt = new Receipt();
            receipt.setAccountId(accountId);
            receipt.setAccountPeriod(period);

            List<Receipt> list = sqlsession.selectList("selectSalesList", receipt);
            if (list != null) {
                return list;
            }
            return Collections.emptyList();
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            if (sqlsession != null) {
                sqlsession.close();
            }
        }

    }

}
