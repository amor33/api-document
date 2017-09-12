package app.document.entity;

import java.util.Date;
/**
 * 
 * @author WenZT
 * @Date 2017年9月9日
 */
public class ApiTestDetail {
	private Long id;
	/** 主表id*/
	private Long mainId;
	/** 标识*/
	private String name;
	/** 请求地址*/
	private String url;
	/** 请求方式*/
	private String type;
	/** 认证信息*/
	private String cookie;
	/** 并发次数*/
	private Integer count;
	/** 请求参数*/
	private String params;
	/** 创建时间*/
	private Date createDate;
	/** 开始时间*/
	private Date start;
	/** 结束时间*/
	private Date end;
	/** 请求状态*/
	private Integer status;
	/** 响应时长*/
	private Integer time;
	/** 返回结果*/
	private String result;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMainId() {
		return mainId;
	}
	public void setMainId(Long mainId) {
		this.mainId = mainId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public ApiTestDetail(){
		super();
	}
	public ApiTestDetail(Long mainId, Date start, Date end, Integer status, String result) {
		super();
		this.mainId = mainId;
		this.start = start;
		this.end = end;
		this.status = status;
		this.result = result;
	}
}
