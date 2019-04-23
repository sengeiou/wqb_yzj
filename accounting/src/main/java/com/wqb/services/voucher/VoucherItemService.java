package com.wqb.services.voucher;

import com.wqb.commons.dto.Menu;
import com.wqb.commons.dto.MenuTree;
import com.wqb.domains.voucher.VoucherItem;
import com.wqb.services.base.BaseService;

import java.util.List;
import java.util.function.Predicate;

/**
 * <p>
 * 生成凭证的项目表  服务类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
public interface VoucherItemService extends BaseService<VoucherItem> {

    /**
     * 生成菜单树
     *
     * @return
     */
    List<Menu> prepareMenuTreeFormCache();


    void deleteMenusCache();

    /**
     * 查询所有生成凭证的项目
     *
     * @return
     */
    List<VoucherItem> selectAllFormCache();

    void deleteListCache();

    /**
     * 通过主键查询生成凭证的项目
     *
     * @return
     */
    VoucherItem selectOneByIdFormCache(Integer id);
}
