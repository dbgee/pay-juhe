package com.kk.payjuhe.utils;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlipayUtils {
    static String ALIPAY_PUBLIC_KEY;
    static String CHARSET;
    static String SIGN_TYPE;
    static String APP_ID;
    static String PRIVATE_KEY;

    static {
         ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjl+L13RCoBNXKtjCjwPtUr/5uvTAoHo6TBprkpx/wFv+Br1xbdc0o2BgrWlEtodUbP8MfTKL4zf0WQHoJnOJFm5lB1xeJy625PXdKslrFnltqxwtpF4B6FplishihQMc2Iqqlz6/6XMA1w0RatWhElxqesuDDN1eTaBlmybzyj21w/5rVYoYtCx5paYpsS1Y2GzXSF4Asvc33hbih1rWz90zgHPws2J/pcOE4qZ2IEH1XpxORWsN9EsXXBhav2S/JV2Ccyhf0DcSMBWgPsmgXCAgkv5CYDq0ZJy9NcZ33MZzYS04QhGCa/9nqWxYix3hYM5h9+WhxtC+s5cVY39aDQIDAQAB";
         CHARSET="utf-8";
         SIGN_TYPE="RSA2";
         APP_ID="填写自己的 app id";
         PRIVATE_KEY="填写自己的私钥 private key";
    }

    public static String queryOrderStatus(String id){
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipay.com/gateway.do",
                APP_ID,
                PRIVATE_KEY,
                "json",
                CHARSET,
                ALIPAY_PUBLIC_KEY,
                SIGN_TYPE);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", id);
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        if(response.isSuccess()){
            log.info("查单成功");
            return response.getMsg()+response.getTotalAmount();

        } else {
            log.info("查单失败，请确认是否存在此订单");
            return "未支付成功或不存在此订单:"+response.getCode();

        }
    }
    public static String getOrderForm(String id){

        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do\n");
        alipayConfig.setAppId(APP_ID);
        alipayConfig.setPrivateKey(PRIVATE_KEY);
        alipayConfig.setFormat("json");
        alipayConfig.setCharset(CHARSET);
        alipayConfig.setAlipayPublicKey(ALIPAY_PUBLIC_KEY);
        alipayConfig.setSignType(SIGN_TYPE);
        AlipayClient alipayClient = null;
        try {
            alipayClient = new DefaultAlipayClient(alipayConfig);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl("");
        request.setReturnUrl("");
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", id);
        bizContent.put("total_amount", 0.01);
        bizContent.put("subject", "测试商品");
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        bizContent.put("qr_pay_mode",1);

        request.setBizContent(bizContent.toString());
        AlipayTradePagePayResponse response;
        try {
            response = alipayClient.pageExecute(request);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        if(response.isSuccess()){
            System.out.println("调用成功,order id="+id);
            log.info("下单成功:{}",id);
            return response.getBody();

        } else {
            String code=response.getCode();
            log.info("下单成功:{}",code);
            return "<p>下单失败："+code+"</p>";
        }
    }
}
