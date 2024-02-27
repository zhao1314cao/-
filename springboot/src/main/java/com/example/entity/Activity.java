package com.example.entity;

import java.io.Serializable;

public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;
    /** ID */
    private Integer id;
    /** 活动名称 */
    private String name;
    /** 活动简介 */
    private String descr;
    /** 开始时间 */
    private String start;
    /** 结束时间 */
    private String end;
    /** 活动形式 */
    private String form;
    /** 活动地址 */
    private String address;
    /** 主办方 */
    private String host;
    /** 浏览量 */
    private Integer readCount;
    private String content;
    private String cover;
    /**是否结束*/
    private Boolean isEnd;
    /**是否报名*/
    private Boolean isSign;
    /**收藏量*/
    private Integer collectCount;
    /**点赞量*/
    private Integer likesCount;
    /**是否点过赞*/
    private Boolean isLike;
    /**是否被收藏*/
    private Boolean isCollect;

    public Boolean getIsLike() {
        return this.isLike;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    public Boolean getIsCollect() {
        return this.isCollect;
    }

    public void setIsCollect(Boolean isCollect) {
        this.isCollect = isCollect;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Boolean getIsSign() {
        return this.isSign;
    }

    public void setIsSign(Boolean isSign) {
        this.isSign = isSign;
    }

    public void setIsEnd(Boolean isEnd) {
        this.isEnd = isEnd;
    }
    public Boolean getIsEnd() {
        return this.isEnd;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


}
