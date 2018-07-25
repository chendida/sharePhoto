package com.zq.dynamicphoto.mylive.bean;

import com.zq.dynamicphoto.bean.BaseModel;

import java.io.Serializable;

/**
 * 直播房间 t_live_room
 * @author mayn
 * 2018-04-10 
 */
public class LiveRoom extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7833836534409665555L;
	
	//直播标题
	private String title;
	//直播封面
	private String cover;
	//直播间创建时间
	private String createTime;
	//最后一次开播时间
	private String openLiveTime;
	//最后一次开播结束时间
	private String endLiveTime;
	//相册用户ID
	private Integer userId;
	//直播账号ID
	private Integer liveId;
	//开播类型 1直播
	private Integer liveType;
	//是否开播 0未开播 1开播
	private Integer isOpen;
	//点赞数
	private Integer admireCount;
	//观看总人数
	private Integer totalWatchNum;

	private String phone;

	private String wx;

	private String content;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWx() {
		return wx;
	}

	public void setWx(String wx) {
		this.wx = wx;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LiveRoom() {
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreteTime() {
		return createTime;
	}

	public void setCreteTime(String creteTime) {
		this.createTime = creteTime;
	}

	public String getOpenLiveTime() {
		return openLiveTime;
	}

	public void setOpenLiveTime(String openLiveTime) {
		this.openLiveTime = openLiveTime;
	}

	public String getEndLiveTime() {
		return endLiveTime;
	}

	public void setEndLiveTime(String endLiveTime) {
		this.endLiveTime = endLiveTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getLiveId() {
		return liveId;
	}

	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}

	public Integer getLiveType() {
		return liveType;
	}

	public void setLiveType(Integer liveType) {
		this.liveType = liveType;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public Integer getAdmireCount() {
		return admireCount;
	}

	public void setAdmireCount(Integer admireCount) {
		this.admireCount = admireCount;
	}

	public Integer getTotalWatchNum() {
		return totalWatchNum;
	}

	public void setTotalWatchNum(Integer totalWatchNum) {
		this.totalWatchNum = totalWatchNum;
	}
	
	
}
