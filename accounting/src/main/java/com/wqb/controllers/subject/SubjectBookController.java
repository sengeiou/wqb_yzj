package com.wqb.controllers.subject;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.subject.SubjectBook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-25
 */
@RestController
@RequestMapping("/subjectBook")
public class SubjectBookController extends BaseController<SubjectBook> {

}
