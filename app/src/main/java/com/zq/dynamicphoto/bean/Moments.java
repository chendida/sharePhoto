package com.zq.dynamicphoto.bean;

import java.io.Serializable;
import java.util.List;

public class Moments extends BaseModel implements Serializable {
	private static final long serialVersionUID = 6982762957136613957L;

	private Integer id;
	
	private String title;//内容
	
	private String bgImage;//背景图片url
	
	private String dynamicIds;//动态id,中间用逗号分隔
	
	private Integer isShow;

	private Integer userId;//用户id
	
	private String createTime;//创建时间

	private String forwardLogo;//小图标

	private String signature;//个性签名

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getForwardLogo() {
		return forwardLogo;
	}

	public void setForwardLogo(String forwardLogo) {
		this.forwardLogo = forwardLogo;
	}

	private List<Dynamic> dynamicList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBgImage() {
		return bgImage;
	}

	public void setBgImage(String bgImage) {
		this.bgImage = bgImage;
	}

	public String getDynamicIds() {
		return dynamicIds;
	}

	public void setDynamicIds(String dynamicIds) {
		this.dynamicIds = dynamicIds;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<Dynamic> getDynamicList() {
		return dynamicList;
	}

	public void setDynamicList(List<Dynamic> dynamicList) {
		this.dynamicList = dynamicList;
	}
}
