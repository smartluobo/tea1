package com.ibay.tea.dao;

import com.ibay.tea.entity.TbPrinter;
import org.springframework.stereotype.Repository;

@Repository
public interface TbPrinterMapper {
    int insert(TbPrinter record);

    int insertSelective(TbPrinter record);
}