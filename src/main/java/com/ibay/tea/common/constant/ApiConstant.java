package com.ibay.tea.common.constant;

public class ApiConstant {

    //常规活动标识
    public static final int ACTIVITY_TYPE_NORMAL = 1;
    //节假日活动标识
    public static final int ACTIVITY_TYPE_HOLIDAY = 2;
    //全场折扣体现在商品价格上的活动类型
    public static final int ACTIVITY_TYPE_FULL = 3;

    //活动未开始
    public static final int ACTIVITY_STATUS_NOT_START = 0;
    //活动进行中
    public static final int ACTIVITY_STATUS_STARTING = 1;
    //活动已经结束
    public static final int ACTIVITY_STATUS_END = 2;

    //优惠券未使用
    public static final int USER_COUPONS_STATUS_NO_USE = 0;
    //优惠券锁定中
    public static final int USER_COUPONS_STATUS_LOCK = 1;
    //优惠券已使用
    public static final int USER_COUPONS_STATUS_USED = 2;

    //折扣券
    public static final int USER_COUPONS_TYPE_RATIO = 1;
    //满减券
    public static final int USER_COUPONS_TYPE_FULL_REDUCE = 2;
    //团购券
    public static final int USER_COUPONS_TYPE_GROUP = 3;
    //免费券
    public static final int USER_COUPONS_TYPE_FREE = 4;
    //通用券
    public static final int USER_COUPONS_TYPE_GENERAL = 5;

    //订单提取方式 自取
    public static final int ORDER_TAKE_WAY_SELF_GET = 0;

    //订单提取方式 派送
    public static final int ORDER_TAKE_WAY_SEND = 1;

    //订单外送价格
    public static final int ORDER_SEND_PRICE = 2;

    //订单状态 未支付
    public static final int ORDER_STATUS_NO_PAY = 0;
    //已支付
    public static final int ORDER_STATUS_PAYED = 1;
    //已完成
    public static final int ORDER_STATUS_COMPLETE = 2;
    //已关闭
    public static final int ORDER_STATUS_CLOSED = 3;

    public static final String WECHAT_LOGIN_TYPE_SK = "SK";
    public static final String WECHAT_LOGIN_TYPE_DT = "DT";









}
