package com.ibay.tea.api.service.map;

import com.ibay.tea.entity.TbStore;

import java.util.List;
import java.util.Map;

public interface ApiMapService {


    TbStore selectStore(List<TbStore> storeList, Map<String, String> params);

    List<Object> getAddressList(Map<String, String> params);
}
