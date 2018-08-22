package com.zq.dynamicphoto.mylive.bean;

public class Charge {
	
	private Integer userId;
	
	private String realName;
	
	private Integer ChargeTypeId;//6直播钻石计费点获取，7vip会员计费点获取
	
	private String paymentName;
	
	private Integer money;
	
	private String orderId;

	private Integer chargeMode;//1表示直播充值，2表示vip半年会员，3表示vip年度会员

	public Integer getChargeMode() {
		return chargeMode;
	}

	public void setChargeMode(Integer chargeMode) {
		this.chargeMode = chargeMode;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
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

	public Integer getChargeTypeId() {
		return ChargeTypeId;
	}

	public void setChargeTypeId(Integer chargeTypeId) {
		ChargeTypeId = chargeTypeId;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
