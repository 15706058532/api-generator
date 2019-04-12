package com.lzf.code.handler;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 16:04:01
 * <br/>
 *
 * @author Li Zhenrfeng
 */
public class BuyerReq implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键 
	 */
	private Long id;

	/**
	 * 用户名 
	 */
	private String username;

	/**
	 * 密码 
	 */
	private String password;

	/**
	 * 性别 0:保密  1:男  2:女 
	 */
	private Integer gender;

	/**
	 * 性别 0:保密  1:男  2:女 开始值
	 */
	private Integer genderStart;

	/**
	 * 性别 0:保密  1:男  2:女 结束值
	 */
	private Integer genderEnd;

	/**
	 * 邮箱 
	 */
	private String email;

	/**
	 * 真实名字 
	 */
	private String realName;

	/**
	 * 注册时间 
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerTime;

	/**
	 * 注册时间 开始值
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerTimeStart;

	/**
	 * 注册时间 结束值
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerTimeEnd;

	/**
	 * 省编码 
	 */
	private String provinceCode;

	/**
	 * 省名称 
	 */
	private String provinceName;

	/**
	 * 市编码 
	 */
	private String cityCode;

	/**
	 * 市名称 
	 */
	private String cityName;

	/**
	 * 县编码 
	 */
	private String townCode;

	/**
	 * 县名称 
	 */
	private String townName;

	/**
	 * 地址 
	 */
	private String addr;

	/**
	 * 是否已删除:0:未,1:删除了 
	 */
	private Integer isDel;

	/**
	 * 是否已删除:0:未,1:删除了 开始值
	 */
	private Integer isDelStart;

	/**
	 * 是否已删除:0:未,1:删除了 结束值
	 */
	private Integer isDelEnd;

	/**
	 * 当前页数 
	 */
	private Integer page;

	/**
	 * 每页条数 
	 */
	private Integer pageSize;

	/**
	 * 排序字段及规则 
	 */
	private Integer orderByClause;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGenderStart(Integer genderStart) {
		this.genderStart = genderStart;
	}

	public Integer getGenderStart() {
		return genderStart;
	}

	public void setGenderEnd(Integer genderEnd) {
		this.genderEnd = genderEnd;
	}

	public Integer getGenderEnd() {
		return genderEnd;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setRealName(String realName) {
		this.realName = realName == null ? null : realName.trim();
	}

	public String getRealName() {
		return realName;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTimeStart(Date registerTimeStart) {
		this.registerTimeStart = registerTimeStart;
	}

	public Date getRegisterTimeStart() {
		return registerTimeStart;
	}

	public void setRegisterTimeEnd(Date registerTimeEnd) {
		this.registerTimeEnd = registerTimeEnd;
	}

	public Date getRegisterTimeEnd() {
		return registerTimeEnd;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode == null ? null : provinceCode.trim();
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName == null ? null : provinceName.trim();
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode == null ? null : cityCode.trim();
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName == null ? null : cityName.trim();
	}

	public String getCityName() {
		return cityName;
	}

	public void setTownCode(String townCode) {
		this.townCode = townCode == null ? null : townCode.trim();
	}

	public String getTownCode() {
		return townCode;
	}

	public void setTownName(String townName) {
		this.townName = townName == null ? null : townName.trim();
	}

	public String getTownName() {
		return townName;
	}

	public void setAddr(String addr) {
		this.addr = addr == null ? null : addr.trim();
	}

	public String getAddr() {
		return addr;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDelStart(Integer isDelStart) {
		this.isDelStart = isDelStart;
	}

	public Integer getIsDelStart() {
		return isDelStart;
	}

	public void setIsDelEnd(Integer isDelEnd) {
		this.isDelEnd = isDelEnd;
	}

	public Integer getIsDelEnd() {
		return isDelEnd;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPage() {
		return page;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setOrderByClause(Integer orderByClause) {
		this.orderByClause = orderByClause;
	}

	public Integer getOrderByClause() {
		return orderByClause;
	}

	@Override
	public String toString() {
		return "BuyerReq{" +
				"id=" + id +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", gender=" + gender +
				", email='" + email + '\'' +
				", realName='" + realName + '\'' +
				", registerTime=" + registerTime +
				", provinceCode='" + provinceCode + '\'' +
				", provinceName='" + provinceName + '\'' +
				", cityCode='" + cityCode + '\'' +
				", cityName='" + cityName + '\'' +
				", townCode='" + townCode + '\'' +
				", townName='" + townName + '\'' +
				", addr='" + addr + '\'' +
				", isDel=" + isDel +
				", page=" + page +
				", pageSize=" + pageSize +
				", orderByClause=" + orderByClause +
				"}";
	}
}