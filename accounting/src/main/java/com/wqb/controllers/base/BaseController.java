package com.wqb.controllers.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wqb.services.base.BaseService;
import com.wqb.supports.util.ResponseUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Shoven
 * @since 2018-11-09
 */
public abstract class BaseController<T> {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    private BaseService<T> service;

    /**
     * 查询所有记录
     *
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity selectAll(T entity) {
        List<T> list = service.list(entity);

        if (CollectionUtils.isEmpty(list)) {
            return ResponseUtils.ok("暂无数据！");
        }

        return ResponseUtils.ok("获取数据成功！", list);
    }


    /**
     * 查询分页记录
     *
     * @param page 分页对象
     * @param entity 实体类
     * @return
     */
    @GetMapping
    public ResponseEntity selectPage(Page page, T entity){
        IPage pageData = service.page(page, entity);
        if (page.getTotal() == 0) {
            return ResponseUtils.ok("暂无数据！", pageData);
        }
        return ResponseUtils.ok(pageData);
    }

    /**
     * 根据主键id查找一条记录
     *
     * @param id 主键
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity selectOne(@PathVariable Serializable id) {
        T t = service.getById(id);
        if (t == null) {
            return ResponseUtils.notFound("暂无数据！");
        }
        return ResponseUtils.ok("获取数据成功！", t);
    }

    /**
     * 添加记录
     *
     * @param entity 实体类
     * @return
     */
    @PostMapping
    public ResponseEntity insert(@Valid T entity) {
        if (!service.save(entity)) {
            return ResponseUtils.unprocesable("添加失败！", entity);
        }
        return ResponseUtils.created("添加成功！", entity);
    }


    /**
     * 根据主键id更新记录
     *
     * @param entity 实体类
     * @return
     */
    @PutMapping
    public ResponseEntity update(@Valid T entity) {
        if (!service.updateById(entity)) {
            return ResponseUtils.unprocesable("更新失败！", entity);
        }
        return ResponseUtils.created("更新成功！", entity);
    }


    /**
     * 根据id删除记录
     *
     * @param id 主键id
     * @param ids 主键id字符串 (ids=11,22,33)
     * @return
     */
    @DeleteMapping
    public ResponseEntity delete(@RequestParam(value = "id", required = false) Serializable id,
                                 @RequestParam(value = "ids", required = false) String ids) {

        if (id != null) {
            if (!service.removeById(id)) {
                return ResponseUtils.unprocesable("删除失败！");
            }
            return ResponseUtils.noContent();
        }

        if (StringUtils.isNotEmpty(ids)) {
            String[] arr = StringUtils.split(ids, ",");
            if (!service.removeByIds(Arrays.asList(arr))) {
                return ResponseUtils.unprocesable("批量删除失败！");
            }
            return ResponseUtils.noContent();
        }

        return ResponseUtils.unprocesable("没有要删除的记录！");
    }

}
