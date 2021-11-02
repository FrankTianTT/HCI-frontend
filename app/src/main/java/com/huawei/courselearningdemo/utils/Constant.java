package com.huawei.courselearningdemo.utils;

public class Constant {
    // login code
    public static final int REQUEST_CODE_SIGN_IN = 8888;
    // buy code
    public static final int REQUEST_CODE_BUY = 6666;
    // server host url
    public static final String HOST = "http://121.5.161.245:8081/";
    // 当前应用开通的华为应用内支付公钥
    public static final String IAP_PUBLIC_KEY = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAtffauJMwskVv8vV4bmW0Mi6S+mZpRdqSyfMzLB11JMq8TmZkfeWQHJccwJmDu9Cp3MPEAyAlTWQHIX15AkuseyKZqnbbAbQC4NsmoRbuVi7S4Dx681xpdJP5eg+zKXffnL+YFw2/KU1P7xl25IaISg+5KLT8fjDezJte56+CgCTvHWq495BTDwvz9hGK7kGZXS7V2rJk/0Hjol1IhuPE9WJGgm5YbaJk+78hp6LHE8idZOTUmtJsXPvsVW1PEIdztr47gpS0ieyu8NLCga3Ye0Q5RHEuvrzkeF/AJrsGBJsn7JM7N7buTj5D8+ZW0sSOnvnt9eXdPpIiKO62Vyd6cVFoCC7hpLx6VfJbkiloxzZiNqsyw3A69Kd494kqVFzM1cJu8KgYZaGPVatlf0JXoVZVZcfrLjLrUNjtrXMMQM84nre6DmUtoplgSEryA8XlpLzwjjTf0NjMTrM5YGEfwHI8UC+NVzmEPP4Q8fDaz0vzwN0tVx27pmTsDJSg5pUZAgMBAAE=";
    // 订单已完成交易（支付并已发货）的状态码
    public static final Integer ORDER_STATUS_SUCCESS = 1;
    // 订单尚未支付的状态码
    public static final Integer ORDER_STATUS_UNPAID = 2;
    // 订单已支付等待发货的状态码
    public static final Integer ORDER_STATUS_WAIT = 3;
    // 订单已取消的状态码
    public static final Integer ORDER_STATUS_CANCEL = 4;
    // AppGalleryConnect里配置的应用内商品ID
    public static final String PRODUCT_ID_FOR_10_DIAMONDS = "test_commodity_202012_01";
    // 请求操作成功返回码 - 用于ResultVO
    public static final Integer REQUEST_SUCCESS = 1;
    // 请求操作失败返回码 - 用于ResultVO
    public static final Integer REQUEST_FAIL = 0;
}
