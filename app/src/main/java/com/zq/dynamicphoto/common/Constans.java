package com.zq.dynamicphoto.common;

/**
 * Created by Administrator on 2018/6/7.
 */

public class Constans {
    public static final float  DEFAULT_PIXEL              = 1280;
    public static final float  DEFAULT_HEIGHT              = 1280;
    public static final float  DEFAULT_WIDTH              = 720;
    //微信登录参数
    public static String APP_ID = "wx5d4d593bf265cfd0";
    public static String AppSecret = "2dbf2af3115076315ec09f6d9b4c4337";

    public static final int MAX_PIC_NUM = 9;//最多选择的图片数量
    public static final int MAX_VIDEO_NUM = 1;//最多选择的视频数量
    public static final int MAX_VIDEO_TIME = 11;//选择视频的时长不能超过10秒
    public static final int ADD_DYNAMIC = 1;//新增动态
    public static final int EDIT_DYNAMIC = 2;//编辑动态
    public static final int REPEAT_DYNAMIC = 3;//转发动态

    //本地保存数据字段
    public static String USERID = "userId";
    public static String USERLOGO = "userLogo";
    public static String BGIMAGE = "bgImage";
    public static String PHOTOURL = "photoUrl";
    public static String REMARKNAME = "remarkName";
    public static String ISBIND = "isBind";
    public static String ISLOGIN = "isLogin";
    public static String UNIONID = "unionId";
    public static String DYNAMIC = "dynamic";
    public static String FORWORDURL = "forwordUrl";
    public static String AGREEMENT = "agreement";
    public static String HTML = "htmlUrl";
    public static String HTML_TITLE = "title";
    public static String FLAG = "flag";
    public static String MOMENTS = "moments";
    public static String MOMENTS_ID = "momentsId";
    public static String VIDEO_URL = "videoUrl";
    public static String IMAGEBUCKET = "imageBucket";
    public static String SELECT_LIST = "bucketList";
    public static String NEW_LIVE_ROOM = "newLiveRoom";
    public static String USERINFO = "userInfo";
    public static String ISANCHOR = "isAnchor";
    public static String LISTENER = "listener";
    public static String WATERMARKID = "watermarkId";
    public static String WATERMARKID_5008 = "5008";
    public static String WATERMARKID_5010 = "5010";
    public static String WATERMARKID_5015 = "5015";
    public static String WATERMARKID_5016 = "5016";
    public static String WATERMARKID_5017 = "5017";
    public static String WATERMARKID_5018 = "5018";
    public static String WATERMARKID_5019 = "5019";
    public static String WATERMARKID_5020 = "5020";
    public static String WATERMARKID_5021 = "5021";
    public static String WATERMARKID_5022 = "5022";
    public static String WATERMARKID_5023 = "5023";
    public static String WATERMARKID_5003 = "5003";
    public static String WATERMARKID_5004 = "5004";
    public static String WATERMARKID_5005 = "5005";
    public static String WATERMARKID_5006 = "5006";
    public static String WATERMARKID_5007 = "5007";
    public static String WATERMARKID_5011 = "5011";
    public static String WATERMARKID_5012 = "5012";
    public static String WATERMARKID_5013 = "5013";

    public static String WATERMARKID_6001 = "6001";
    public static String WATERMARKID_6002 = "6002";
    public static String WATERMARKID_6003 = "6003";
    public static String WATERMARKID_6004 = "6004";
    public static String WATERMARKID_6005 = "6005";
    public static String WATERMARKID_6006 = "6006";
    public static String WATERMARKID_6007 = "6007";
    public static String WATERMARKID_6008 = "6008";
    public static String WATERMARKID_6009 = "6009";
    public static String WATERMARKID_6010 = "6010";
    public static String WATERMARKID_6011 = "6011";

    public static String WATERMARKID_7004 = "7004";
    public static String DRAWABLE = "drawable";
    public static Integer REQUEST_OK = 0;
    public static int REQUEST_CODE = 1001;
    public static int RESULT_CODE = 1008;
    public static String AVATAR_CHANGE = "avatar_change";
    public static String AVATAR_PATH = "avatar_path";
    public static String FRAME_TYPE = "frame_type";

    public static Integer REQUEST_LOIGN_ERROR = -4;//登录时手机号或密码错误

    public static final int SDK_APPID = 1400087984;
    public static String Base_Url = "http://192.168.1.7:8080/photo/interface/";

    public static String HTML_Url = "http://redshoping.cn/";
    //public static String HTML_Url = "http://192.168.1.184:8080/photo/";
    //public static String Base_Url = "http://redshoping.cn/interface/";
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
