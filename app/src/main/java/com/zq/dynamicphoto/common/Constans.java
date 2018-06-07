package com.zq.dynamicphoto.common;

/**
 * Created by Administrator on 2018/6/7.
 */

public class Constans {
    //微信登录参数
    public static String APP_ID = "wx5d4d593bf265cfd0";
    public static String AppSecret = "2dbf2af3115076315ec09f6d9b4c4337";

    public static final int SDK_APPID = 1400087984;
    //public static String Base_Url = "http://192.168.1.178:8080/photo/interface/";

    //public static String HTML_Url = "http://redshoping.cn/";
    public static String HTML_Url = "http://192.168.1.178:8080/photo/";
    public static String Base_Url = "http://redshoping.cn/interface/";
    //public static String Base_Url = "http://www.gongxiangxiangce.cn/interface/";

    private static volatile Constans instance;

    public static Constans getInstance(){
        if (null == instance) {
            synchronized (Constans.class) {
                if (null == instance) {
                    instance = new Constans();
                }
            }
        }
        return instance;
    }
}
