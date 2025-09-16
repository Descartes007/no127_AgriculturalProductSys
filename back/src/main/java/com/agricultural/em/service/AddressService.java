package com.agricultural.em.service;

import com.agricultural.em.mapper.AddressMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.agricultural.em.entity.Address;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressService extends ServiceImpl<AddressMapper, Address> {

    @Resource
    private AddressMapper addressMapper;

    public List<Address> findAllById(Long id) {
        QueryWrapper<Address> listQueryWrapper = new QueryWrapper<>();
        listQueryWrapper.eq("user_id",id);
        return list(listQueryWrapper);
    }
}
