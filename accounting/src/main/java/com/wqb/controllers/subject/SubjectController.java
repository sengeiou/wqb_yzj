package com.wqb.controllers.subject;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 记账科目表 前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@RestController
@RequestMapping("/subject")
public class SubjectController extends BaseController<Subject> {

}
