package com.zq.dynamicphoto.bean;

import java.io.Serializable;

public class UserWatermark extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1783389117947480100L;
	private String watermarkId;
	private String watermarkUrl;
	private Integer userId;
	private Integer watermarkType;//1水印，2文字
	
	
	public Integer getWatermarkType() {
		return watermarkType;
	}
	public void setWatermarkType(Integer watermarkType) {
		this.watermarkType = watermarkType;
	}
	public String getWatermarkId() {
		return watermarkId;
	}
	public void setWatermarkId(String watermarkId) {
		this.watermarkId = watermarkId;
	}
	public String getWatermarkUrl() {
		return watermarkUrl;
	}
	public void setWatermarkUrl(String watermarkUrl) {
		this.watermarkUrl = watermarkUrl;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
