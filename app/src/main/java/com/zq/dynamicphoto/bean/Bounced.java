package com.zq.dynamicphoto.bean;

import java.io.Serializable;

public class Bounced extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6634559736847528218L;
	
	private Integer id;
	//弹框图片
	private String bouncedImg;
	//弹框文字
	private String bouncedContent;
	//弹框按钮文字
	private String btnVal;
	//弹框类型1普通
	private Integer bouncedType;//1普通，2版本更新，3详情
	//是否显示
	private Integer isShow;

	private String versionCode;//最新版本号

	private String bouncedURL;//点击按钮跳转的H5链接

	private String htmlURL;//展示的弹窗信息链接

	private String title;

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

	public String getHtmlURL() {
		return htmlURL;
	}

	public void setHtmlURL(String htmlURL) {
		this.htmlURL = htmlURL;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getBouncedURL() {
		return bouncedURL;
	}

	public void setBouncedURL(String bouncedURL) {
		this.bouncedURL = bouncedURL;
	}

	public Bounced() {
		// TODO Auto-generated constructor stub
	}

	public String getBouncedImg() {
		return bouncedImg;
	}

	public void setBouncedImg(String bouncedImg) {
		this.bouncedImg = bouncedImg;
	}

	public String getBouncedContent() {
		return bouncedContent;
	}

	public void setBouncedContent(String bouncedContent) {
		this.bouncedContent = bouncedContent;
	}

	public String getBtnVal() {
		return btnVal;
	}

	public void setBtnVal(String btnVal) {
		this.btnVal = btnVal;
	}

	public Integer getBouncedType() {
		return bouncedType;
	}

	public void setBouncedType(Integer bouncedType) {
		this.bouncedType = bouncedType;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
}
