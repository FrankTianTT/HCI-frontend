package com.huawei.courselearningdemo.utils;

public class Constant {
    // login code
    public static final int REQUEST_CODE_SIGN_IN = 8888;
    // buy code
    public static final int REQUEST_CODE_BUY = 6666;
    // server host url
    public static final String HOST = "http://121.5.161.245:8081/";
    // 当前应用开通的华为应用内支付公钥
    public static final String IAP_PUBLIC_KEY = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAiuJYGIlmzYN6OBo449TgLzrRB34wUbqmM3IcD0mRwcVG/Y4DI4mm0YPd+/qasUaoApnGu13CbRBvWe5BlaOqnN0efSE+ni8pIe3gNEm1quL1Gx3kDiwdiLek5Ts+qWQs51hcQTRZ6tjzReGzOxK/Svqn58/RM0lsn1qdkyGLczaoXjkpc5HaiGAbCjg83opdmO3/7e44ApcI8WGrBoljTY4Z5CNikeaQVwuYJ5EjSSQwBmeeZ/cVIxmmhIvN5nUVBupEd5+YEvqwsae+D9AvKb+B4lH5TYxwe6WaWygAux4BXS8gOAzsvb480/drDVy9yQ2DUAMQjgJqZ0AFCfA66NtdCatASHhGwwzMSTrWZiWUz2QS7LoxM1INYMBZF1KSSLyXpAaqXiZkPK+Xmj4bNUq6nz7JxOckrqfpPealcSuB54GBm2MFl0EA8HAV5GSykO/9YznQDnjUO4MN8AopQMeZBwttGPeFdtO0y8MG994nHIL7id+MwW0Dj4vtrmDzAgMBAAE=";
    // 订单已完成交易（支付并已发货）的状态码
    public static final Integer ORDER_STATUS_SUCCESS = 1;
    // 订单尚未支付的状态码
    public static final Integer ORDER_STATUS_UNPAID = 2;
    // 订单已支付等待发货的状态码
    public static final Integer ORDER_STATUS_WAIT = 3;
    // 订单已取消的状态码
    public static final Integer ORDER_STATUS_CANCEL = 4;
    // AppGalleryConnect里配置的应用内商品ID
    public static final String PRODUCT_ID_FOR_10_DIAMONDS = "10zhaun";
    public static final String PRODUCT_ID_FOR_1_DIAMONDS = "1zhuan";
    // 请求操作成功返回码 - 用于ResultVO
    public static final Integer REQUEST_SUCCESS = 1;
    // 请求操作失败返回码 - 用于ResultVO
    public static final Integer REQUEST_FAIL = 0;
}
