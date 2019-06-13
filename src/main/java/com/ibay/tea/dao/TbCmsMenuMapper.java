package com.ibay.tea.dao;

import com.ibay.tea.entity.TbCmsMenu;

public interface TbCmsMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbCmsMenu record);

    int insertSelective(TbCmsMenu record);

    TbCmsMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbCmsMenu record);

    int updateByPrimaryKey(TbCmsMenu record);
}