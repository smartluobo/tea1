package com.ibay.tea.dao;

import com.ibay.tea.entity.TbActivity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbActivityMapper {
    int deleteByPrimaryKey(Integer id);

    TbActivity selectByPrimaryKey(Integer id);

    List<TbActivity> findAll();

}