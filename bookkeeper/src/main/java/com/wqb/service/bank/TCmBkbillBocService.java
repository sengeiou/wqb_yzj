package com.wqb.service.bank;

import com.wqb.common.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface TCmBkbillBocService {

    Map<String, Object> uploadTCmBkbillBoc(MultipartFile updateBkillBocFile, HttpSession session);

    Map<String, Object> queryTCmBkbillBocList(HttpSession session) throws BusinessException;

    Map<String, Object> deleteTCmbkbillBocAll(HttpSession session) throws BusinessException;

    Map<String, Object> deleteTCmBkbillBocByBkbillBoc(HttpSession session, String pkBkbillBoc) throws BusinessException;

}
