package com.ucar.smadmin.base.bd.vo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 版权声明： Copyright (c) 2008 ucarinc. All Rights Reserved.
 *
 * @author 何麒（qi.he@ucarinc.com）
 * @Version 1.0
 * @date 2018/11/1
 */
public class BannerVO implements Serializable{

    private static final long serialVersionUID = 3933335056779025681L;
    /**
     * 主键id
     */
    private Long id;

    /**广告名称**/
    @NotBlank(message = "广告名称不能为空")
    private String name;

    /**广告类型1首页**/
    @NotNull(message = "广告类型不能为空")
    private Integer type;

    /**是否前台显示，1：是，0:否**/
    @NotNull(message = "请选择是否显示！")
    private  Integer isShow;

    /**排序**/
    @NotNull(message = "请选择排序号！")
    private Integer sortNum;

    /**点击**/
    private Integer clicks;

    /**广告图片**/
    @NotBlank(message = "广告名称不能为空")
    private String imageUrl;

    /**广告地址**/
    private String url;

    /**描述**/
    private String description;

    /**
     * 创建人
     **/
    private Long createEmp;

    /**
     * 修改人
     **/
    private Long modifyEmp;


    @Override
    public String toString() {
        return "BannerVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", isShow=" + isShow +
                ", sortNum=" + sortNum +
                ", clicks=" + clicks +
                ", imageUrl='" + imageUrl + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", createEmp=" + createEmp +
                ", modifyEmp=" + modifyEmp +
                '}';
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public Long getCreateEmp() {
        return createEmp;
    }

    public void setCreateEmp(Long createEmp) {
        this.createEmp = createEmp;
    }

    public Long getModifyEmp() {
        return modifyEmp;
    }

    public void setModifyEmp(Long modifyEmp) {
        this.modifyEmp = modifyEmp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
