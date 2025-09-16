package com.agricultural.em.service;

import com.agricultural.em.entity.GoodStandardCheck;
import com.agricultural.em.mapper.GoodStandardCheckMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GoodStandardCheckService extends ServiceImpl<GoodStandardCheckMapper, GoodStandardCheck> {
    @Resource
    private GoodStandardCheckMapper goodStandardCheckMapper;

    public boolean delete(GoodStandardCheck standard) {
        QueryWrapper<GoodStandardCheck> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("good_check_id",standard.getGoodCheckId());
        queryWrapper.eq("value",standard.getValue());
        return remove(queryWrapper);
    }


    public void deleteAll(int checkId) {
        QueryWrapper<GoodStandardCheck> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("good_check_id",checkId);
        remove(queryWrapper);
    }
}
