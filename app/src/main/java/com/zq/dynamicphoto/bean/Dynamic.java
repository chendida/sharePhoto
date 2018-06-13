package com.zq.dynamicphoto.bean;

import java.io.Serializable;
import java.util.List;

public class Dynamic extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = 7004539148279382026L;

	private Integer id;
	
	private String title;//搜索动态时传的关键字
	
	private Integer parentId;//所转发的动态id
	
	private String content;//内容
	
	private Integer rank;
	
	private Integer dynamicType;//动态类型1图文2视频3链接

	private String createTime;
	
	private String updateTime;//时间
	
	private Integer likeNum;
	
	private Integer isOpen;//1所有人可见，2仅自己可见
	
	private Integer userId;//用户id(发布人)
	
	private Integer labelId;

	private Integer momentId;//朋友圈id

	private List<DynamicLabel> dynamicLabels;

	private List<LabelRelation> labelRelations;

	private String beginTime;//开始时间

	private String endTime;//结束时间

	private Integer height;
	private Integer width;

	private Integer isCheck;

	private String promoteImg;

	private Integer liveId;

	public Integer getLiveId() {
		return liveId;
	}

	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}

	public String getPromoteImg() {
		return promoteImg;
	}

	public void setPromoteImg(String promoteImg) {
		this.promoteImg = promoteImg;
	}

	public Integer getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<LabelRelation> getLabelRelations() {
		return labelRelations;
	}

	public void setLabelRelations(List<LabelRelation> labelRelations) {
		this.labelRelations = labelRelations;
	}

	public List<DynamicLabel> getDynamicLabels() {
		return dynamicLabels;
	}

	public void setDynamicLabels(List<DynamicLabel> dynamicLabels) {
		this.dynamicLabels = dynamicLabels;
	}

	public Integer getMomentId() {
		return momentId;
	}

	public void setMomentId(Integer momentId) {
		this.momentId = momentId;
	}

	private List<DynamicPhoto> dynamicPhotos;//动态中的图片集合

	private List<DynamicVideo> dynamicVideos;//动态中的视频集合

	public List<DynamicVideo> getDynamicVideos() {
		return dynamicVideos;
	}

	public void setDynamicVideos(List<DynamicVideo> dynamicVideos) {
		this.dynamicVideos = dynamicVideos;
	}

	private User user;

	private UserInfo userInfo;

	private Integer isMoments;

	public Integer getIsMoments() {
		return isMoments;
	}

	public void setIsMoments(Integer isMoments) {
		this.isMoments = isMoments;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	///////////////////////////临时字段//////////////////////
	
	private Integer isForward = 0;          //是否转发1转发0未转发,2已分享
	
	private Integer iDynamicId = 0;			//映射自己的动态ID
	
	private String forwardTime;				//转发时间
	
	private Integer isBathSave = 0;			//是否可以批量保存
	
	private String userIds;					//userId集合

	public List<DynamicPhoto> getDynamicPhotos() {
		return dynamicPhotos;
	}

	public void setDynamicPhotos(List<DynamicPhoto> dynamicPhotos) {
		this.dynamicPhotos = dynamicPhotos;
	}

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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getDynamicType() {
		return dynamicType;
	}

	public void setDynamicType(Integer dynamicType) {
		this.dynamicType = dynamicType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getLabelId() {
		return labelId;
	}

	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}

	public Integer getIsForward() {
		return isForward;
	}

	public void setIsForward(Integer isForward) {
		this.isForward = isForward;
	}

	public Integer getiDynamicId() {
		return iDynamicId;
	}

	public void setiDynamicId(Integer iDynamicId) {
		this.iDynamicId = iDynamicId;
	}

	public String getForwardTime() {
		return forwardTime;
	}

	public void setForwardTime(String forwardTime) {
		this.forwardTime = forwardTime;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public Integer getIsBathSave() {
		return isBathSave;
	}

	public void setIsBathSave(Integer isBathSave) {
		this.isBathSave = isBathSave;
	}
	
}
