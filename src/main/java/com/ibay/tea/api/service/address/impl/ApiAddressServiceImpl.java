package com.ibay.tea.api.service.address.impl;

import com.ibay.tea.api.service.address.ApiAddressService;
import com.ibay.tea.dao.TbApiUserAddressMapper;
import com.ibay.tea.entity.TbApiUserAddress;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ApiAddressServiceImpl implements ApiAddressService {

    @Resource
    private TbApiUserAddressMapper tbApiUserAddressMapper;


    @Override
    public List<TbApiUserAddress> findUserAddressByOppenId(String oppenId,String storeId) {
        return tbApiUserAddressMapper.findUserAddressByOppenId(oppenId,storeId);
    }

    @Override
    public TbApiUserAddress findUserAddressById(int id) {
        return tbApiUserAddressMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insertApiUserAddress(TbApiUserAddress tbApiUserAddress) {
        tbApiUserAddressMapper.insert(tbApiUserAddress);
    }

    @Override
    public void updateUserAddress(TbApiUserAddress tbApiUserAddress) {
        TbApiUserAddress dbAddress = tbApiUserAddressMapper.selectByPrimaryKey(tbApiUserAddress.getId());
        if (dbAddress == null){
            return;
        }
        tbApiUserAddressMapper.deleteByPrimaryKey(tbApiUserAddress.getId());

        tbApiUserAddressMapper.saveUpdateApiUserAddress(tbApiUserAddress);

    }

    @Override
    public void deleteApiUserAddress(Map<String, String> params) {
        tbApiUserAddressMapper.deleteApiUserAddress(params);
    }
}
