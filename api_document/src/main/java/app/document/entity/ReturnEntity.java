package app.document.entity;

public class ReturnEntity {
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public static ReturnEntity succeed(Object data){
		return new ReturnEntity(data);
	}

	public ReturnEntity(Object data) {
		super();
		this.data = data;
	}

	public ReturnEntity() {
		super();
	}
}
