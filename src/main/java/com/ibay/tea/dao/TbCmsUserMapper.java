package com.ibay.tea.dao;

import com.ibay.tea.entity.TbCmsUser;
import org.springframework.stereotype.Repository;

@Repository
public interface TbCmsUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbCmsUser record);

    TbCmsUser selectByPrimaryKey(Integer id);

    TbCmsUser findUserByLoginName(String loginName);
}