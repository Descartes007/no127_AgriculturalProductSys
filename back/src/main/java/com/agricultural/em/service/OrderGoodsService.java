package com.agricultural.em.service;

import com.agricultural.em.mapper.OrderGoodsMapper;
import com.agricultural.em.entity.OrderGoods;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderGoodsService extends ServiceImpl<OrderGoodsMapper, OrderGoods> {

    @Resource
    private OrderGoodsMapper orderGoodsMapper;

}
