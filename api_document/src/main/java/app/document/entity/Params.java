package app.document.entity;
/**
 * api参数model
 * @author WenZT
 * @Date 2017年8月31日
 */
public class Params {
	private Long id;
	private String paramsName;
	private String paramsDiscription;
	private String paramsType;
	private String exampleParams;
	private String isRequired;
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
	
	public String getParamsDiscription() {
		return paramsDiscription;
	}
	public void setParamsDiscription(String paramsDiscription) {
		this.paramsDiscription = paramsDiscription;
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
	
	public String getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}
	public Params(String paramsName, String paramsType, String exampleParams, String isRequired,String paramsDiscription) {
		super();
		this.paramsName = paramsName;
		this.paramsType = paramsType;
		this.exampleParams = exampleParams;
		this.isRequired = isRequired;
		this.paramsDiscription = paramsDiscription;
	}
	public Params() {
		super();
	}
}
