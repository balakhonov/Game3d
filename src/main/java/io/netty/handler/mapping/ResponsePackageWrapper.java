package io.netty.handler.mapping;

public class ResponsePackageWrapper<C> extends ResponsePackageData {

	private C data;

	public ResponsePackageWrapper() {
		// no code
	}

	public ResponsePackageWrapper(int resultCode, C data) {
		super(resultCode);
		this.data = data;
	}

	public ResponsePackageWrapper(int resultCode, String message) {
		super(resultCode, message);
	}

	public ResponsePackageWrapper(int resultCode, String message, C data) {
		super(resultCode, message);
		this.data = data;
	}

	public C getData() {
		return data;
	}

	public void setData(C data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(", data: " + data);
		return sb.toString();
	}
}
