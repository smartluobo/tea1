package com.ibay.tea.api.service.address;

import com.ibay.tea.entity.TbApiUserAddress;

import java.util.List;

public interface ApiAddressService {

    List<TbApiUserAddress> findUserAddressByOppenId(String oppenId);

    TbApiUserAddress findUserAddressById(int id);

    void insertApiUserAddress(TbApiUserAddress tbApiUserAddress);
}
