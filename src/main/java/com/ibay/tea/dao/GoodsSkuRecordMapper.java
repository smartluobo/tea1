package com.ibay.tea.dao;

import com.ibay.tea.entity.GoodsSkuRecord;

public interface GoodsSkuRecordMapper {
    int insert(GoodsSkuRecord record);

    void deleteGoodsSkuRecordByPrimaryKey(Integer id);

}