package com.aiolos.library.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    /**
     * 主键
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Long id;

    /**
     * 长期授权字符串
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码，暂未定哪种方式登录
     */
    private String password;

    /**
     * 用户头像，小头像
     */
    @Column(name = "head_portrait")
    private String headPortrait;

    /**
     * 用户头像，大头像
     */
    @Column(name = "head_portrait_big")
    private String headPortraitBig;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 是否是超级管理员
     */
    private Boolean root;

    /**
     * 状态: 0: 已删除, 1: 已激活, 2: 已冻结
     */
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 角色
     */
    private String role;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取长期授权字符串
     *
     * @return open_id - 长期授权字符串
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置长期授权字符串
     *
     * @param openId 长期授权字符串
     */
    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    /**
     * 获取昵称
     *
     * @return nickname - 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取性别
     *
     * @return sex - 性别
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置性别
     *
     * @param sex 性别
     */
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    /**
     * 获取手机号
     *
     * @return phone - 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取密码，暂未定哪种方式登录
     *
     * @return password - 密码，暂未定哪种方式登录
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码，暂未定哪种方式登录
     *
     * @param password 密码，暂未定哪种方式登录
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取用户头像，小头像
     *
     * @return head_portrait - 用户头像，小头像
     */
    public String getHeadPortrait() {
        return headPortrait;
    }

    /**
     * 设置用户头像，小头像
     *
     * @param headPortrait 用户头像，小头像
     */
    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait == null ? null : headPortrait.trim();
    }

    /**
     * 获取用户头像，大头像
     *
     * @return head_portrait_big - 用户头像，大头像
     */
    public String getHeadPortraitBig() {
        return headPortraitBig;
    }

    /**
     * 设置用户头像，大头像
     *
     * @param headPortraitBig 用户头像，大头像
     */
    public void setHeadPortraitBig(String headPortraitBig) {
        this.headPortraitBig = headPortraitBig == null ? null : headPortraitBig.trim();
    }

    /**
     * 获取省份
     *
     * @return province - 省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省份
     *
     * @param province 省份
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * 获取城市
     *
     * @return city - 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置城市
     *
     * @param city 城市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * 获取区县
     *
     * @return district - 区县
     */
    public String getDistrict() {
        return district;
    }

    /**
     * 设置区县
     *
     * @param district 区县
     */
    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    /**
     * 获取生日
     *
     * @return birthday - 生日
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 设置生日
     *
     * @param birthday 生日
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取是否是超级管理员
     *
     * @return root - 是否是超级管理员
     */
    public Boolean getRoot() {
        return root;
    }

    /**
     * 设置是否是超级管理员
     *
     * @param root 是否是超级管理员
     */
    public void setRoot(Boolean root) {
        this.root = root;
    }

    /**
     * 获取状态: 0: 已删除, 1: 已激活, 2: 已冻结
     *
     * @return status - 状态: 0: 已删除, 1: 已激活, 2: 已冻结
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态: 0: 已删除, 1: 已激活, 2: 已冻结
     *
     * @param status 状态: 0: 已删除, 1: 已激活, 2: 已冻结
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取修改时间
     *
     * @return gmt_modified - 修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置修改时间
     *
     * @param gmtModified 修改时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * 获取角色
     *
     * @return role - 角色
     */
    public String getRole() {
        return role;
    }

    /**
     * 设置角色
     *
     * @param role 角色
     */
    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", openId=").append(openId);
        sb.append(", nickname=").append(nickname);
        sb.append(", name=").append(name);
        sb.append(", sex=").append(sex);
        sb.append(", phone=").append(phone);
        sb.append(", password=").append(password);
        sb.append(", headPortrait=").append(headPortrait);
        sb.append(", headPortraitBig=").append(headPortraitBig);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", district=").append(district);
        sb.append(", birthday=").append(birthday);
        sb.append(", root=").append(root);
        sb.append(", status=").append(status);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", role=").append(role);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}