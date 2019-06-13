package com.ibay.tea.dao;

import com.ibay.tea.entity.TbStore;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbStoreMapper {

    TbStore selectByPrimaryKey(Integer id);

    List<TbStore> findAll();

}