package com.ibay.tea.entity;

import java.util.Date;

public class Carousel {
    private Integer id;

    private String imageUrl;

    private Integer carouselType;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public Integer getCarouselType() {
        return carouselType;
    }

    public void setCarouselType(Integer carouselType) {
        this.carouselType = carouselType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Carousel{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", carouselType=" + carouselType +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}