package com.agricultural.em.controller;

import com.agricultural.em.common.Result;
import com.agricultural.em.utils.BaseApi;
import com.agricultural.em.annotation.Authority;
import com.agricultural.em.entity.AuthorityType;
import com.agricultural.em.entity.Category;
import com.agricultural.em.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    /*
    查询
    */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        return Result.success(categoryService.getById(id));
    }

    @GetMapping
    public Result findAll() {
        List<Category> list = categoryService.list();
        return Result.success(list);
    }

    /*
    保存
    */
    @PostMapping
    public Result save(@RequestBody Category category) {
        categoryService.saveOrUpdate(category);
        return Result.success();
    }

    /**
     *  新增下级分类 + 上下级分类关联
     *
     * @param category 下级分类
     * @return 结果
     */
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Category category) {
        categoryService.add(category);
        return BaseApi.success();
    }

    // @Authority(AuthorityType.requireAuthority)
    @PutMapping
    public Result update(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.success();
    }


    /**
     * 删除分类
     *
     * @param id id
     * @return 结果
     */
    // @Authority(AuthorityType.requireAuthority)
    @GetMapping("/delete")
    public Map<String, Object> delete(@RequestParam("id") Long id) {
        return categoryService.delete(id);
    }





}
