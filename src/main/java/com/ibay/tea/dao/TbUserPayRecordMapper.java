package com.ibay.tea.dao;

import com.ibay.tea.entity.TbUserPayRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface TbUserPayRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbUserPayRecord record);

    TbUserPayRecord selectByPrimaryKey(Integer id);
}