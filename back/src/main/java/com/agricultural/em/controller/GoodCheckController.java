package com.agricultural.em.controller;

import com.agricultural.em.annotation.Authority;
import com.agricultural.em.common.Result;
import com.agricultural.em.constants.Constants;
import com.agricultural.em.entity.*;
import com.agricultural.em.service.GoodCheckService;
import com.agricultural.em.service.GoodService;
import com.agricultural.em.service.GoodStandardCheckService;
import com.agricultural.em.service.StandardService;
import com.agricultural.em.utils.CommonUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/good/check")
public class GoodCheckController {

    @Autowired
    private GoodCheckService goodCheckService;

    @Autowired
    private GoodStandardCheckService goodStandardCheckService;

    @Autowired
    private GoodService goodService;

    @Autowired
    private StandardService standardService;


//    // @Authority(AuthorityType.requireAuthority)
    @PostMapping
    public Result save(@RequestBody GoodCheck goodCheck) {
        // 在商品审核列表新增的时候，默认是未审核的状态
        goodCheck.setStatus("待审核");
        goodCheck.setCreateTime(CommonUtils.getCurrentTime());
        System.out.println(goodCheck);

        boolean save = goodCheckService.saveOrUpdate(goodCheck);
        return Result.success(goodCheck);

    }

//    // @Authority(AuthorityType.requireAuthority)
@PostMapping("/update")
    public Result update(@RequestBody GoodCheck goodCheck) {
        // 根据传入的数据进行商品的修改
        System.out.println(goodCheck);
        //
        if (goodCheck.getStatus().equals("已通过")){
            goodCheckService.updateById(goodCheck);
            // 保存数据到good表中
            Good good = new Good();
            good.setName(goodCheck.getName());
            good.setDescription(goodCheck.getDescription());
            good.setImgs(goodCheck.getImgs());
            good.setCreateTime(CommonUtils.getCurrentTime());
            good.setDiscount(1.0);
            good.setSaleMoney(BigDecimal.valueOf(0));
            good.setSales(0);
            good.setCheckId(goodCheck.getId());
            good.setIsDelete(false);
            good.setRecommend(true);
            good.setCategoryId(goodCheck.getCategoryId());

            goodService.save(good);

            Integer id = good.getId();

            // 根据checkId来获取标准表相关的数据
            List<GoodStandardCheck> standards = goodCheckService.getStandard(goodCheck.getId());
            for (GoodStandardCheck goodStandardCheck : standards) {
                Standard standard = new Standard();
                standard.setGoodId(id);
                standard.setPrice(BigDecimal.valueOf(goodStandardCheck.getPrice()));
                standard.setStore(goodStandardCheck.getStore());
                standard.setValue(goodStandardCheck.getValue());
                standardService.save(standard);

            }

        }
        // 另外一种就是未通过
        else if (goodCheck.getStatus().equals("未通过")){
            goodCheckService.updateById(goodCheck);
        }
        else {}

        return Result.success();

    }

    @GetMapping("/standard/{id}")
    public Result getStandard(@PathVariable int id) {
        return Result.success(goodCheckService.getStandard(id));
    }

    @PostMapping("/standard")
    public Result saveStandard(@RequestBody List<GoodStandardCheck> standards, @RequestParam int checkId) {
        //先删除全部旧记录
        goodStandardCheckService.deleteAll(checkId);
        //然后插入新记录
        for (GoodStandardCheck standard : standards) {
            standard.setGoodCheckId(checkId);
            if(!goodStandardCheckService.save(standard)){
                return Result.error(Constants.CODE_500,"保存失败");
            }
        }
        return Result.success();
    }

    //删除商品的规格信息
//    // @Authority(AuthorityType.requireAuthority)
    @DeleteMapping("/standard")
    public Result delStandard(@RequestBody GoodStandardCheck standard) {
        boolean delete = goodStandardCheckService.delete(standard);
        if(delete) {
            return Result.success();
        }else {
            return Result.error(Constants.CODE_500,"删除失败");
        }
    }


    // 分页获取审核列表
    //查询
    @GetMapping("/page")
    public Result selectPage(@RequestParam int pageNum,
                             @RequestParam int pageSize,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) Integer userId
                             ){

        IPage<GoodCheck> myFileIPage = goodCheckService.selectPage(pageNum, pageSize,name,userId);
        return Result.success(myFileIPage);
    }

    // 删除
//    // @Authority(AuthorityType.requireAuthority)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {

        return Result.success(goodCheckService.removeById(id));
    }


}
