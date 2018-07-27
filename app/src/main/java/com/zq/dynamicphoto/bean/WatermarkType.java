package com.zq.dynamicphoto.bean;

import java.io.Serializable;
import java.util.List;

public class WatermarkType extends BaseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2765769969948430520L;
	/**
	 * t_watermark_type
	 */

	private Integer watermarkType;//水印图片类型
	private String watermarkName;//水印图片类型名称
	
	private List<Watermark> watermarkList;
	
	
	
	public List<Watermark> getWatermarkList() {
		return watermarkList;
	}
	public void setWatermarkList(List<Watermark> watermarkList) {
		this.watermarkList = watermarkList;
	}
	public String getWatermarkName() {
		return watermarkName;
	}
	public void setWatermarkName(String watermarkName) {
		this.watermarkName = watermarkName;
	}
	public Integer getWatermarkType() {
		return watermarkType;
	}
	public void setWatermarkType(Integer watermarkType) {
		this.watermarkType = watermarkType;
	}
	
	
}
