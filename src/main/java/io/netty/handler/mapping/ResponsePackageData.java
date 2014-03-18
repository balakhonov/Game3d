package io.netty.handler.mapping;

public class ResponsePackageData {

	private int resultCode;
	private String warningMessage ="";

	public ResponsePackageData() {
		// no code
	}

	public ResponsePackageData(int resultCode) {
		this.resultCode = resultCode;
	}

	public ResponsePackageData(int resultCode, String message) {
		this.resultCode = resultCode;
		this.warningMessage = message;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(", resultCode: " + resultCode);
		sb.append(", message: " + warningMessage);
		return sb.toString();
	}
}
