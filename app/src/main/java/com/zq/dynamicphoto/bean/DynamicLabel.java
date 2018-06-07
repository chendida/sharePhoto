package com.zq.dynamicphoto.bean;

import java.io.Serializable;

public class DynamicLabel extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = 9053729664394861384L;

	private Integer id;
	
	private String labeltext;
	
	private Integer isShow;
	
	private Integer userId;
	
	private String createTime;

	private Integer dynamicId;

	private Integer countNum;     //标签下面动态数

	public Integer getCountNum() {
		return countNum;
	}

	public void setCountNum(Integer countNum) {
		this.countNum = countNum;
	}

	public Integer getDynamicId() {
		return dynamicId;
	}

	public void setDynamicId(Integer dynamicId) {
		this.dynamicId = dynamicId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabeltext() {
		return labeltext;
	}

	public void setLabeltext(String labeltext) {
		this.labeltext = labeltext;
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

}
