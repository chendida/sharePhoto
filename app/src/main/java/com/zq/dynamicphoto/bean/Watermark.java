package com.zq.dynamicphoto.bean;

import java.io.Serializable;
import java.util.List;

public class Watermark extends BaseModel implements Serializable{

	/**
	 * t_watermark_type
	 */
	private static final long serialVersionUID = -5154286641690205391L;


	private String watermarkId;//水印图片id
	private String watermarkUrl;//水印图片地址
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
	public Integer getWatermarkType() {
		return watermarkType;
	}
	public void setWatermarkType(Integer watermarkType) {
		this.watermarkType = watermarkType;
	}


}
