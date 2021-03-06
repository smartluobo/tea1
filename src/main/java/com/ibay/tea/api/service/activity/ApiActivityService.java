package com.ibay.tea.api.service.activity;

import com.ibay.tea.entity.TbActivity;
import com.ibay.tea.entity.TbActivityCouponsRecord;
import com.ibay.tea.entity.TbUserCoupons;

public interface ApiActivityService {

    TbActivity getTodayActivity();

    TbActivityCouponsRecord extractPrize(String oppenId);

    void saveUserCouponsToDb(TbUserCoupons tbUserCoupons);

    TbUserCoupons buildUserCoupons(String oppenId, TbActivityCouponsRecord record);

    int checkActivityStatus(TbActivity activityInfo);
}
