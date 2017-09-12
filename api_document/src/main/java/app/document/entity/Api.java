package app.document.entity;

import java.util.List;

/**
 * api主体
 * @author WenZT
 * @Date 2017年8月31日
 */
public class Api {

    private Long id;
    private String code;
    private String discription;
    private String name;
    private String type;
    private String project;
    private String uri;
    private String result;
    private String author;
    private String authType;
    private String exampleUri;
    private List<Params> params;
    
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
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getExampleUri() {
		return exampleUri;
	}
	public void setExampleUri(String exampleUri) {
		this.exampleUri = exampleUri;
	}
	
	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public List<Params> getParams() {
		return params;
	}
	public void setParams(List<Params> params) {
		this.params = params;
	}
}
