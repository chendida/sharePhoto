package com.zq.dynamicphoto.mylive.bean;

import com.zq.dynamicphoto.bean.BaseModel;

import java.io.Serializable;
import java.util.List;

public class ChargeType extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = -6366333954835921907L;

	private Integer id;
	
	//是否显示
	private Integer isShow;
	
	//第三方计费Code
	private Integer code;
	
	//支付名称
	private String payName;
	
	//税率
	private String poundage;
	
	//计费点
	private String feeCode;
	
	private List<String> feeCodeList;
	//回调URL
	private String notifyUrl;
	
	//排序
	private String indexs;
	
	//描述
	private String description;
	
	//包名
	private String packageName;
	
	private String giveAway;
	
	private List<String> giveAwayList;
	
	private Integer scale;
	
	private String newgiveAway;
	
	private List<String> newgiveAwayList;

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public List<String> getFeeCodeList() {
		return feeCodeList;
	}

	public void setFeeCodeList(List<String> feeCodeList) {
		this.feeCodeList = feeCodeList;
	}

	public String getGiveAway() {
		return giveAway;
	}

	public void setGiveAway(String giveAway) {
		this.giveAway = giveAway;
	}

	public List<String> getGiveAwayList() {
		return giveAwayList;
	}

	public void setGiveAwayList(List<String> giveAwayList) {
		this.giveAwayList = giveAwayList;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPoundage() {
		return poundage;
	}

	public void setPoundage(String poundage) {
		this.poundage = poundage;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getIndexs() {
		return indexs;
	}

	public void setIndexs(String indexs) {
		this.indexs = indexs;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNewgiveAway() {
		return newgiveAway;
	}

	public void setNewgiveAway(String newgiveAway) {
		this.newgiveAway = newgiveAway;
	}

	public List<String> getNewgiveAwayList() {
		return newgiveAwayList;
	}

	public void setNewgiveAwayList(List<String> newgiveAwayList) {
		this.newgiveAwayList = newgiveAwayList;
	}
	
}
