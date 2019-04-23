package com.wqb.controller.inventory;

import com.wqb.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 进销存(库存控制层)
 *
 * @author zhushuyuan
 */
@Controller
@RequestMapping("/inventory")
public class InventoryController extends BaseController {
    @RequestMapping("/queryInventory")
    @ResponseBody
    Map<String, Object> queryInventory(String beginTime, String endTime, String keyWords, String curPage) {
        Map<String, Object> result = new HashMap<String, Object>();

        return result;
    }
}
