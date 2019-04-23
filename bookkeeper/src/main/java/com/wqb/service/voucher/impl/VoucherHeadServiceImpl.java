package com.wqb.service.voucher.impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.subBook.SubBookDao;
import com.wqb.dao.user.UserDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.model.VoucherHead;
import com.wqb.service.voucher.VoucherHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("voucherHeadService")
public class VoucherHeadServiceImpl implements VoucherHeadService {
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    UserDao userDao;
    @Autowired
    SubBookDao subBookDao;

    // 查询最大凭证号
    public Integer queryMaxVouchNO(String accountID, String period) throws BusinessException {
        return voucherHeadDao.queryMaxVouchNO(accountID, period);
    }

    @Override
    public Integer insertVouchHead(VoucherHead vouchHead) throws BusinessException {
        return voucherHeadDao.insertVouchHead(vouchHead);
    }

    // 断号整理凭证号重新制作
    @Override
    public void updatevoucherNo(Map<String, Object> param) throws BusinessException {
        // 根据accountID，userID,创建時間範圍，查询全部凭证
        try {
            long s1 = System.currentTimeMillis();
            List<VoucherHead> list = voucherHeadDao.queryVHByUserIdAndVouchID(param);
            Map<String, String> map = new HashMap<>();
            Map<String, Object> upBookMap = new HashMap<>();
            if (list != null && list.size() > 0) {
                Collections.sort(list, new Comparator<VoucherHead>() {
                    @Override
                    public int compare(VoucherHead paramT1, VoucherHead paramT2) {
                        return paramT1.getVoucherNO().compareTo(paramT2.getVoucherNO());
                    }
                });

                List<Integer> arr = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    VoucherHead vh = list.get(i);
                    arr.add(vh.getVoucherNO());
                }
                System.out.println(arr);

                for (int i = 0; i < list.size(); i++) {
                    VoucherHead vh = list.get(i);
                    String vouchID = vh.getVouchID();

                    //旧的凭证号
                    Integer old_voucherNO = vh.getVoucherNO();
                    // 新凭证号
                    int new_voucherNO = i + 1;
                    if (old_voucherNO == new_voucherNO) {
                        System.out.println();
                    } else {
                        upBookMap.put("voucherNo", new_voucherNO);
                        upBookMap.put("vouchID", vouchID);
                        //更新明细账凭证号
                        int upSubvoucherNo = subBookDao.upSubvoucherNo(upBookMap);
                    }
                    // 更新凭证主表凭证号
                    map.put("voucherNo", String.valueOf(new_voucherNO));
                    map.put("vouchID", vouchID);
                    voucherHeadDao.updatevoucherNo(map);
                }
            }
            long s2 = System.currentTimeMillis();
            // StringUtil.jsDate("计算 凭证断号整理时间", s1, s2);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    // 获取凭证最大号自增1
    @Override
    public Integer getMaxVoucherNo(Map<String, Object> param) throws BusinessException {
        return voucherHeadDao.getMaxVoucherNo(param);
    }

    @Override
    public List<VoucherHead> queryInvAndBankVou(Map<String, Object> param) throws BusinessException {

        return voucherHeadDao.queryInvAndBankVou(param);
    }

    //lch 凭证汇总
    @Override
    public List<TBasicSubjectMessage> queryVouSummary(Map<String, Object> param) throws BusinessException {
        List<TBasicSubjectMessage> list = voucherHeadDao.queryVouSummary(param);
        return list;

    }

    //查询凭证总数
    @Override
    public int queryCountVouch(Map<String, Object> param) throws BusinessException {
        int num = voucherHeadDao.queryCountVouch(param);
        return num;
    }

    //查询凭证总数--如果有凭证不能初始化科目余额表
    @Override
    public int queryCountVouch2(Map<String, Object> param) throws BusinessException {
        int num = voucherHeadDao.queryCountVouch2(param);
        return num;
    }
}
