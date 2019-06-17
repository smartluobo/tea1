package com.ibay.tea.dao;

import com.ibay.tea.entity.TbStoreGoods;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TbStoreGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbStoreGoods record);

    int insertSelective(TbStoreGoods record);

    TbStoreGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbStoreGoods record);

    int updateByPrimaryKey(TbStoreGoods record);

    List<TbStoreGoods> findRecordByCondition(Map<String, Object> condition);
}