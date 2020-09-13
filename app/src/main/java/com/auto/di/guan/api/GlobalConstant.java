package com.auto.di.guan.api;

public class GlobalConstant {
    public static final String SERVER_ERROR="网络信号差或网络连接不可用,请稍后重试！";

    //http商户请求接口签名字符串(公共请求参数)
    public static final String MERCHANT_SECRET = "e607ac9d931cd646e3f765503e5307a4";
    //http支付请求接口签名字符串(公共请求参数)
    public static final String PAYMENT_SECRET = "32596ac746536b0369e2ec2d1de65a0c";


    //mqtt消息体,事件类型,1=初始化,2=增量数据
    public static final String EVENT_TYPE_INIT ="1";
    public static final String EVENT_TYPE_PUSH ="2";


    //mqtt消息体,消息类型,1=分类,2=商品,3=订单,4=交接班
    public static final String MESSAGE_TYPE_CATEGORY = "1";
    public static final String MESSAGE_TYPE_GOODS = "2";
    public static final String MESSAGE_TYPE_ORDERS="3";
    public static final String MESSAGE_TYPE_HANDOVER="4";

    //微信扫码枪
    public static final Integer WECHAT_SCAN_PAY=1;
    //支付宝扫码枪
    public static final Integer ALIPAY_SCAN_PAY=2;
    //银行卡记账
    public static final Integer BANK_PAY_TYPE=3;
    //现金收款
    public static final Integer CASH_PAY_TYPE=4;
    //微信记账
    public static final Integer WECHAT_PAY_TYPE=5;
    //支付宝记账
    public static final Integer ALIPAY_PAY_TYPE=6;
    //云闪付扫码
    public static final Integer UNIONPAY_SCAN_PAY=7;
    //云闪付记账
    public static final Integer UNIONPAY_BOOKKOOP_PAY=8;
    //刷脸收款
    public static final Integer BRUSH_PAY=9;


    //订单类型1=收款,1=支付成功
    public static final Integer RECEIVABLES_ORDER_TYPE=1;
    public static final Integer PAY_STATUS_SUCCESS =1;

    //订单状态,2=退款,1=退款成功
    public static final Integer REFUND_ORDER_TYPE=2;
    public static final Integer REFUND_STATUS_SUCCESS=1;

    //发送mqtt消息定义客户端类型android=9(硬编码)
    public static final int client=9;

    /**
     *  优惠的类型
     */
    //会员优惠
    public static final int COUPON_TYPE_MEMBER = 0;
    //券码核销
    public static final int COUPON_TYPE_REVOKE = 1;
    //抵扣金额
    public static final int COUPON_TYPE_DEDUCTION = 2;
    //全单折扣
    public static final int COUPON_TYPE_DISCOUNT = 3;
}
