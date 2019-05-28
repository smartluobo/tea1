package com.ibay.tea.api.service.activity;

import com.ibay.tea.entity.TbActivity;
import com.ibay.tea.entity.TbActivityCouponsRecord;

public interface ApiActivityService {

    TbActivity getActivityInfo();

    TbActivityCouponsRecord extractPrize(String oppenId);

    void saveUserCouponsToDb(String oppenId, TbActivityCouponsRecord record);
}
