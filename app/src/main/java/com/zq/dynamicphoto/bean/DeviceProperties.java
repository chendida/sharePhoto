/*
 * Copyright (C) 2012 Guangzhou CooguoSoft Co.,Ltd.
 * cn.douwan.sdk.entityDeviceProperties.java
 */
package com.zq.dynamicphoto.bean;

import java.io.Serializable;

/**
 * @Description: 设备环境参数
 * @author Jerry   @date 2012-8-21 上午09:58:49 
 * @version 1.0 
 * @JDK  1.6
 */

public class DeviceProperties implements Serializable {

	private static final long serialVersionUID = -7578544222928872751L;
	/**
	 * 1.0.1
	 * 手机系统版本
	 */
	public String sdkVersion = "0";
	/**	
	 * 1.0.1
	 * 手机型号
	 */
	public String product;
	/**
	 * 1.0.1
	 * Sim卡序列号
	 */
	public String imsi;
	/**
	 * 1.0.1
	 * 手机序列号
	 */
	public String imei;
	/**
	 * 1.0.1
	 * 应用版本名称
	 */
	public String versionName;
	/**
	 * 1.0.1
	 * 应用版本号
	 */
	public int versionCode;
	/**
	 * 1.0.1
	 * 手机屏幕密码
	 */
	public int phoneMode;
	/**
	 * 	1.0.1
	 * 手机屏幕宽度
	 */
	public int displayScreenWidth;
	/**
	 * 1.0.1
	 * 手机屏幕高度
	 */
	public int displayScreenHeight;
	/**
	 * 1.0.1
	 * 经度
	 */
	public double latitude;
	/**
	 * 1.0.1
	 * 纬度
	 */
	public double longitude;
	/**
	 * 1.0.1
	 * 地域（省、市、县/区）
	 */
	public String area;
	/**
	 * 1.0.1
	 * 网络类型
	 */
	public String networkInfo;
	/**
	 * 1.0.1
	 * 后台自动生成唯一标识
	 */
	public String douGameId;
	
	public String channelId;  //渠道ID
	
	public String packageName;  //包名
	
	public String version;
	
	public String deviceParams;  //自定义手机标识
	
	public String currentImsi; //当前手机卡imsi
	
	/** t 是否需要快充数据，1-不需要；0-需要，默认0 **/
	public int isfastPay=0;
	
	/**
	 * 不用登录 充值1 正常 0
	 */
	public int noLoginPay = 0;
	public String iccId;           //客户端识别地域的标示
	public String telecomsdk;      //客户端识别地域的标示
	public String mac;             //手机客户端mac地址
	public String productNameFlag;  //产品改名标示：1、快乐大赢家
	public String sdkDate;  //sdk编辑日期
	
	public static final int NoLoginPay = 1;
	public static final int LoginPay = 0;
	
	//图片验证码
	public String imageCode;
	
	public String keyCode;
	
	//accessToken
	public String accessToken;//从微信获取的token

	private Integer userId;
	
	//QQ对应的appId
	public String appId;
	
	//微信对应的openId
	public String openId;
	
	//提取产品的URL
	public String url;
	
	//商户Code
	public String merchantsCode;
	//登录会话Key
	public String key;
	//登录时间戳
	public String textTime;


	private Integer liveType;//0表示推荐，1表示最新

	private Integer clientType;//1表示安卓

	private Integer appType;//1表示男，2表示女

	public void setAppType(Integer appType) {
		this.appType = appType;
	}

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

	public Integer getLiveType() {
		return liveType;
	}

	public void setLiveType(Integer liveType) {
		this.liveType = liveType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}
	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public int getPhoneMode() {
		return phoneMode;
	}
	public void setPhoneMode(int phoneMode) {
		this.phoneMode = phoneMode;
	}
	public int getDisplayScreenWidth() {
		return displayScreenWidth;
	}
	public void setDisplayScreenWidth(int displayScreenWidth) {
		this.displayScreenWidth = displayScreenWidth;
	}
	public int getDisplayScreenHeight() {
		return displayScreenHeight;
	}
	public void setDisplayScreenHeight(int displayScreenHeight) {
		this.displayScreenHeight = displayScreenHeight;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getNetworkInfo() {
		return networkInfo;
	}
	public void setNetworkInfo(String networkInfo) {
		this.networkInfo = networkInfo;
	}
	public String getDouGameId() {
		return douGameId;
	}
	public void setDouGameId(String douGameId) {
		this.douGameId = douGameId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDeviceParams() {
		return deviceParams;
	}
	public void setDeviceParams(String deviceParams) {
		this.deviceParams = deviceParams;
	}
	public String getCurrentImsi() {
		return currentImsi;
	}
	public void setCurrentImsi(String currentImsi) {
		this.currentImsi = currentImsi;
	}
	public int getIsfastPay() {
		return isfastPay;
	}
	public void setIsfastPay(int isfastPay) {
		this.isfastPay = isfastPay;
	}
	public int getNoLoginPay() {
		return noLoginPay;
	}
	public void setNoLoginPay(int noLoginPay) {
		this.noLoginPay = noLoginPay;
	}
	public String getIccId() {
		return iccId;
	}
	public void setIccId(String iccId) {
		this.iccId = iccId;
	}
	public String getTelecomsdk() {
		return telecomsdk;
	}
	public void setTelecomsdk(String telecomsdk) {
		this.telecomsdk = telecomsdk;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getProductNameFlag() {
		return productNameFlag;
	}
	public void setProductNameFlag(String productNameFlag) {
		this.productNameFlag = productNameFlag;
	}
	public String getSdkDate() {
		return sdkDate;
	}
	public void setSdkDate(String sdkDate) {
		this.sdkDate = sdkDate;
	}
	public String getImageCode() {
		return imageCode;
	}
	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}
	public String getKeyCode() {
		return keyCode;
	}
	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMerchantsCode() {
		return merchantsCode;
	}
	public void setMerchantsCode(String merchantsCode) {
		this.merchantsCode = merchantsCode;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTextTime() {
		return textTime;
	}
	public void setTextTime(String textTime) {
		this.textTime = textTime;
	}
	@Override
	public String toString() {
		return "dr[area=" + area + ",channelId=" + channelId
				+ ",densityDpi=" + phoneMode + ",deviceParams="
				+ deviceParams + ",displayScreenHeight=" + displayScreenHeight
				+ ",displayScreenWidth=" + displayScreenWidth + ",douGameId="
				+ douGameId + ",imei=" + imei + ",imsi=" + imsi
				+ ",currentImsi=" + currentImsi
				+ ",latitude=" + latitude + ",longitude=" + longitude
				+ ",networkInfo=" + networkInfo + ",packageName="
				+ packageName + ",product=" + product + ",sdkVersion="
				+ sdkVersion + ",version=" + version + ",versionCode="
				+ versionCode + ",versionName=" + versionName + ",isfastPay="+ isfastPay
				+ ",iccId="+ iccId+ ",telecomsdk="+ telecomsdk+ ",mac="+ mac +",productNameFlag"+productNameFlag+"]";
	}

}
