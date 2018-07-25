package com.zq.dynamicphoto.mylive.bean;

public class Charge {
	
	private Integer userId;
	
	private String realName;
	
	private Integer ChargeTypeId;//6
	
	private String paymentName;
	
	private Integer money;
	
	private String orderId;

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
