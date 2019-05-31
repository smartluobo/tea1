package com.ibay.tea.api.service.address.impl;

import com.ibay.tea.api.service.address.ApiAddressService;
import com.ibay.tea.dao.TbApiUserAddressMapper;
import com.ibay.tea.entity.TbApiUserAddress;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApiAddressServiceImpl implements ApiAddressService {

    @Resource
    private TbApiUserAddressMapper tbApiUserAddressMapper;


    @Override
    public List<TbApiUserAddress> findUserAddressByOppenId(String oppenId) {
        return tbApiUserAddressMapper.findUserAddressByOppenId(oppenId);
    }

    @Override
    public TbApiUserAddress findUserAddressById(int id) {
        return tbApiUserAddressMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insertApiUserAddress(TbApiUserAddress tbApiUserAddress) {
        tbApiUserAddressMapper.insert(tbApiUserAddress);
    }
}
