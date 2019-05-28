package com.ibay.tea.dao;

import com.ibay.tea.entity.TbApiUser;
import org.springframework.stereotype.Repository;

@Repository
public interface TbApiUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbApiUser record);

    TbApiUser selectByPrimaryKey(Integer id);

    TbApiUser findApiUserByOppenId(String oppenId);
}