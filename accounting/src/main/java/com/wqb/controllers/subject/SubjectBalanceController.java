package com.wqb.controllers.subject;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.subject.SubjectBalance;
import com.wqb.services.subject.SubjectBalanceService;
import com.wqb.supports.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * EXCEL科目档案表 前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@RestController
@RequestMapping("/subjectBalance")
public class SubjectBalanceController extends BaseController<SubjectBalance> {

}
