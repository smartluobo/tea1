package com.ibay.tea.dao;

import com.ibay.tea.entity.TbApiUserAddress;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TbApiUserAddressMapper {

    int deleteByPrimaryKey(int id);

    int insert(TbApiUserAddress record);

    TbApiUserAddress selectByPrimaryKey(int id);

    List<TbApiUserAddress> findUserAddressByOppenId(String oppenId);

    void saveUpdateApiUserAddress(TbApiUserAddress tbApiUserAddress);

    void deleteApiUserAddress(Map<String, String> params);

}