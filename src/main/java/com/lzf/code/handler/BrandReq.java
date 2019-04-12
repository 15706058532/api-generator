package com.lzf.code.handler;

import java.io.Serializable;

/**
 * 写点注释
 * <br/>
 * Created in 2019-03-17 16:04:01
 * <br/>
 *
 * @author Li Zhenrfeng
 */
public class BrandReq implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键 
	 */
	private Long id;

	/**
	 * 名称 
	 */
	private String name;

	/**
	 * 描述 
	 */
	private String description;

	/**
	 * 图片URI 
	 */
	private String imgUri;

	/**
	 * 品牌网址 
	 */
	private String webSite;

	/**
	 * 排序:最大最排前 
	 */
	private Integer sort;

	/**
	 * 排序:最大最排前 开始值
	 */
	private Integer sortStart;

	/**
	 * 排序:最大最排前 结束值
	 */
	private Integer sortEnd;

	/**
	 * 是否可见 1:可见 0:不可见 
	 */
	private Integer isDisplay;

	/**
	 * 是否可见 1:可见 0:不可见 开始值
	 */
	private Integer isDisplayStart;

	/**
	 * 是否可见 1:可见 0:不可见 结束值
	 */
	private Integer isDisplayEnd;

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

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setImgUri(String imgUri) {
		this.imgUri = imgUri == null ? null : imgUri.trim();
	}

	public String getImgUri() {
		return imgUri;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite == null ? null : webSite.trim();
	}

	public String getWebSite() {
		return webSite;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSortStart(Integer sortStart) {
		this.sortStart = sortStart;
	}

	public Integer getSortStart() {
		return sortStart;
	}

	public void setSortEnd(Integer sortEnd) {
		this.sortEnd = sortEnd;
	}

	public Integer getSortEnd() {
		return sortEnd;
	}

	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}

	public Integer getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplayStart(Integer isDisplayStart) {
		this.isDisplayStart = isDisplayStart;
	}

	public Integer getIsDisplayStart() {
		return isDisplayStart;
	}

	public void setIsDisplayEnd(Integer isDisplayEnd) {
		this.isDisplayEnd = isDisplayEnd;
	}

	public Integer getIsDisplayEnd() {
		return isDisplayEnd;
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
		return "BrandReq{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", imgUri='" + imgUri + '\'' +
				", webSite='" + webSite + '\'' +
				", sort=" + sort +
				", isDisplay=" + isDisplay +
				", page=" + page +
				", pageSize=" + pageSize +
				", orderByClause=" + orderByClause +
				"}";
	}
}