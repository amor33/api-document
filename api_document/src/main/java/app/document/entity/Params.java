package app.document.entity;
/**
 * api参数model
 * @author WenZT
 * @Date 2017年8月31日
 */
public class Params {
	private Long id;
	private String paramsName;
	private String discription;
	private String paramsType;
	private String exampleParams;
	private Long apiId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getParamsName() {
		return paramsName;
	}
	public void setParamsName(String paramsName) {
		this.paramsName = paramsName;
	}
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	public String getParamsType() {
		return paramsType;
	}
	public void setParamsType(String paramsType) {
		this.paramsType = paramsType;
	}
	public String getExampleParams() {
		return exampleParams;
	}
	public void setExampleParams(String exampleParams) {
		this.exampleParams = exampleParams;
	}
	public Long getApiId() {
		return apiId;
	}
	public void setApiId(Long apiId) {
		this.apiId = apiId;
	}
	public Params(String paramsName, String paramsType, String exampleParams) {
		super();
		this.paramsName = paramsName;
		this.paramsType = paramsType;
		this.exampleParams = exampleParams;
	}
}
