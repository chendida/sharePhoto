package com.zq.dynamicphoto.mylive.bean;

import com.zq.dynamicphoto.bean.BaseModel;
import com.zq.dynamicphoto.bean.Dynamic;
import com.zq.dynamicphoto.bean.UserInfo;

import java.io.Serializable;
import java.util.List;

public class ChargeOrder extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4363763791532773480L;
	
	//订单Id
	private String orderId;
	//用户Id
	private Integer userId;
	//手机号
	private String mobile;
	//地址
	private String address;
	//订单创建时间
	private String createTime;
	//订单发货时间
	private String shipmentsTime;
	//订单完成时间
	private String resultTime;
	//订单状态，1提交订单2确认订单3订单发货4订单完成-1订单失败
	private Integer orderStatus;//1表示待确认，2表示已确认，3表示已发货，4表示已取消
	//用户姓名
	private String userName;
	//购买数量
	private Integer buyNum;
	//动态ID
	private Integer dynamicId;
	//优惠券（暂不考虑）
	private Integer ticketId;
	
	//用户留言
	private String liuyan;

	private Integer newLiveId;
	
	
	private List<UserInfo> userInfo;
	
	private List<Dynamic> dynamic;

	private String dynamicImg;

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDynamicImg() {
		return dynamicImg;
	}

	public void setDynamicImg(String dynamicImg) {
		this.dynamicImg = dynamicImg;
	}

	public ChargeOrder() {
		// TODO Auto-generated constructor stub
	}

	public Integer getNewLiveId() {
		return newLiveId;
	}

	public void setNewLiveId(Integer newLiveId) {
		this.newLiveId = newLiveId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getMoblie() {
		return mobile;
	}

	public void setMoblie(String moblie) {
		this.mobile = moblie;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getShipments() {
		return shipmentsTime;
	}

	public void setShipments(String shipments) {
		this.shipmentsTime = shipments;
	}

	public String getResultTime() {
		return resultTime;
	}

	public void setResultTime(String resultTime) {
		this.resultTime = resultTime;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}

	public Integer getDynamicId() {
		return dynamicId;
	}

	public void setDynamicId(Integer dynamicId) {
		this.dynamicId = dynamicId;
	}

	public Integer getTicketId() {
		return ticketId;
	}

	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}

	public String getShipmentsTime() {
		return shipmentsTime;
	}

	public void setShipmentsTime(String shipmentsTime) {
		this.shipmentsTime = shipmentsTime;
	}

	public List<UserInfo> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(List<UserInfo> userInfo) {
		this.userInfo = userInfo;
	}

	public List<Dynamic> getDynamic() {
		return dynamic;
	}

	public void setDynamic(List<Dynamic> dynamic) {
		this.dynamic = dynamic;
	}

	public String getMessage() {
		return liuyan;
	}

	public void setMessage(String message) {
		this.liuyan = message;
	}
	 
		
}
