package io.netty.handler.mapping;

public class RequestPackageWrapper {

	private String command;
	private String data;

	public RequestPackageWrapper(String command, String data) {
		if (command == null || command.trim().isEmpty()) {
			throw new IllegalArgumentException("Command(" + command + ") should not be null or empty!");
		}
		if (data == null) {
			throw new IllegalArgumentException("data(" + data + ") should not be null!");
		}

		this.command = command;
		this.data = data;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "command: " + command + ", data: " + data;
	}
}
