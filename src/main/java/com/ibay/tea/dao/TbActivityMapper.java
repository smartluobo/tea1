package com.ibay.tea.dao;

import com.ibay.tea.entity.TbActivity;
import org.springframework.stereotype.Repository;

@Repository
public interface TbActivityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbActivity record);

    int insertSelective(TbActivity record);

    TbActivity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbActivity record);

    int updateByPrimaryKey(TbActivity record);
}