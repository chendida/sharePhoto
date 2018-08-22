package com.zq.dynamicphoto.bean;

import java.io.Serializable;
import java.util.List;

public class UserInfo extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = -6342527486090816795L;

	private Integer userId;
	
	private String realName;//微信昵称
	
	private String lastReqTime;
	
	private Integer sex;
	
	private String address;
	
	private String birthday	;
	
	private String phoneMode;
	
	private String photoURL;//相册网址

	private String mobile;//电话号码

	private String wx;//微信号
	
	private String wxQRCode;//二维码
	
	private String introduce;//相册介绍

	private String userLogo;

	private Integer totalDynamicNum;		//总动态数

	private Integer addDynamicNum;			//新增动态数（自己新增动态总和）

	private String remarkName;//昵称

	private String url;//相册地址
	private Integer isVip;//1表示是vip,0表示非vip
	private String vip;//vip到期时间

	private List<DynamicLabel> dynamicLabels;

	private String bgImage;
	private Integer coin;//余额

	private Integer num;//订单数量


	public Integer getIsVip() {
		return isVip;
	}

	public void setIsVip(Integer isVip) {
		this.isVip = isVip;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getCoin() {
		return coin;
	}

	public void setCoin(Integer coin) {
		this.coin = coin;
	}
	public String getBgImage() {
		return bgImage;
	}

	public void setBgImage(String bgImage) {
		this.bgImage = bgImage;
	}

	public List<DynamicLabel> getDynamicLabels() {
		return dynamicLabels;
	}

	public void setDynamicLabels(List<DynamicLabel> dynamicLabels) {
		this.dynamicLabels = dynamicLabels;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getAddDynamicNum() {
		return addDynamicNum;
	}

	public void setAddDynamicNum(Integer addDynamicNum) {
		this.addDynamicNum = addDynamicNum;
	}

	public Integer getTotalDynamicNum() {
		return totalDynamicNum;
	}

	public void setTotalDynamicNum(Integer totalDynamicNum) {
		this.totalDynamicNum = totalDynamicNum;
	}

	public String getUserLogo() {
		return userLogo;
	}

	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLastReqTime() {
		return lastReqTime;
	}

	public void setLastReqTime(String lastReqTime) {
		this.lastReqTime = lastReqTime;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhoneMode() {
		return phoneMode;
	}

	public void setPhoneMode(String phoneMode) {
		this.phoneMode = phoneMode;
	}

	public String getPhotoURL() {
		return photoURL;
	}

	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	public String getWx() {
		return wx;
	}

	public void setWx(String wx) {
		this.wx = wx;
	}

	public String getWxQRCode() {
		return wxQRCode;
	}

	public void setWxQRCode(String wxQRCode) {
		this.wxQRCode = wxQRCode;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

}
