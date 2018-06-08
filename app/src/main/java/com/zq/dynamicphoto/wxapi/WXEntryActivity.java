package com.zq.dynamicphoto.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.zq.dynamicphoto.MyApplication;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.ui.LoginActivity;
import com.zq.dynamicphoto.utils.HttpURLConnectionUtil;
import org.json.JSONException;
import org.json.JSONObject;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
    private static final String TAG = "WXEntryActivity";
    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.mWxApi.handleIntent(getIntent(),this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {

	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	// app发送消息给微信，处理返回消息的回调
	@Override
	public void onResp(BaseResp resp) {
		Log.i("loadData","onResp");
		Log.i("loadData","错误码 : " + resp.errCode + "");
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				if (RETURN_MSG_TYPE_SHARE == resp.getType())
					Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
                finish();
				break;
			case BaseResp.ErrCode.ERR_OK:
				switch (resp.getType()) {
					case RETURN_MSG_TYPE_LOGIN:
						//拿到了微信返回的code,立马再去请求access_token
						String code = ((SendAuth.Resp) resp).code;
						//就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
						getWeixinToken(code);
						break;

					case RETURN_MSG_TYPE_SHARE:
						Toast.makeText(this, "微信分享成功", Toast.LENGTH_SHORT).show();
						finish();
						break;
				}
				break;
		}
	}

	/**
	 * 获取微信登录的token和json
	 * @param code
	 */
	public void getWeixinToken(String code) {
		final String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ Constans.APP_ID + "&secret="
		+ Constans.AppSecret + "&code=" + code + "&grant_type=authorization_code";
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String s = HttpURLConnectionUtil.get(url, 10000);
					JSONObject jsonObject = new JSONObject(s);
					String token = jsonObject.optString("access_token").toString();
					String openId = jsonObject.optString("openid").toString();
					LoginActivity.token = token;
					LoginActivity.openId = openId;
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}