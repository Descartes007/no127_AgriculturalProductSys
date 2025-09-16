package com.agricultural.em.service;

import com.agricultural.em.constants.Constants;
import com.agricultural.em.entity.GoodCheck;
import com.agricultural.em.entity.GoodStandard;
import com.agricultural.em.entity.GoodStandardCheck;
import com.agricultural.em.entity.MyFile;
import com.agricultural.em.exception.ServiceException;
import com.agricultural.em.mapper.GoodCheckMapper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.xml.fastinfoset.stax.events.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodCheckService extends ServiceImpl<GoodCheckMapper, GoodCheck> {

    // 首先不需要关注规格，在保存了以后，才关注规格
    @Autowired
    private GoodCheckMapper goodCheckMapper;


    public IPage<GoodCheck> selectPage(int pageNum, int pageSize, String name, Integer userId) {
        IPage<GoodCheck> filesPage = new Page<>(pageNum, pageSize);
        QueryWrapper<GoodCheck> filesQueryWrapper = new QueryWrapper<>();
        if(!Util.isEmptyString(name)){
            filesQueryWrapper.like("name",name);
        }
        if(userId!=-1){
            filesQueryWrapper.eq("user_id",userId);
        }
        filesQueryWrapper.orderByDesc("id");

        return page(filesPage, filesQueryWrapper);

    }

    public  List<GoodStandardCheck> getStandard(int id) {
        List<GoodStandardCheck> standards = goodCheckMapper.getStandardById(id);
        if(standards.size()==0){
            throw new ServiceException(Constants.NO_RESULT,"无结果");
        }
        return standards;

    }
}
