package app.document.entity;

import java.util.Date;

/**
 * 
 * @author WenZT
 * @Date 2017年9月9日
 */
public class ApiTest {
	
	private Long id;
	/** 操作人*/
	private String name;
	/** 标识*/
	private String code;
	/** 请求地址*/
	private String url;
	/** 请求方式*/
	private String type;
	/** 认证方式*/
	private String authType;
	/** 认证信息*/
	private String cookie;
	/** 请求参数*/
	private String params;
	/** 并发次数*/
	private Integer count;
	/** 创建时间*/
	private Date createDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	public ApiTest(){
		super();
	}
	
	public ApiTest(String name, String url, String type, String cookie, Integer count,String params,String code) {
		super();
		this.name = name;
		this.url = url;
		this.type = type;
		this.cookie = cookie;
		this.count = count;
		this.createDate = new Date();
		this.params = params;
		this.code = code;
	}
}
