package com.ibay.tea.dao;

import com.ibay.tea.entity.TbOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface TbOrderMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(TbOrder record);

    TbOrder selectByPrimaryKey(String orderId);

    void updateCompleteMessageSendStatus(String orderId, int makeCompleteSendStatus);

    void updateCloseMessageSendStatus(String orderId, int closeSendStatus);

}