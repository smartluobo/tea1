package com.ibay.tea.dao;

import com.ibay.tea.entity.TbStore;
import org.springframework.stereotype.Repository;

@Repository
public interface TbStoreMapper {

    TbStore selectByPrimaryKey(Integer id);
}