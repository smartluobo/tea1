package com.ibay.tea.api.service.pay;

import com.ibay.tea.entity.TbOrder;

public interface ApiPayService {
    void createPayOrderToWechat(TbOrder tbOrder) throws Exception;
}
