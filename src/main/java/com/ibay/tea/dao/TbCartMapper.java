package com.ibay.tea.dao;

import com.ibay.tea.entity.TbCart;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbCart record);

    TbCart selectByPrimaryKey(Integer id);

    List<TbCart> findCartGoodsListByOppenId(String oppenId);

    List<TbCart> findCartItemByIds(List<String> strings);

}