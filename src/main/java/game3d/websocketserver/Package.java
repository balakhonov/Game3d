package game3d.websocketserver;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;


public class Package implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	public String type;
	public Object data;


	public Package(String type, Serializable data) {
		this.type = type;
		this.data = data;
	}


	public Package(String type, Map<String, ? extends Serializable> data) {
		this.type = type;
		this.data = data;
	}


	public Package(String type, Collection<? extends Serializable> data) {
		this.type = type;
		this.data = data;
	}


	// public Package(String type, Map<String, Object> data) {
	// this.type = type;
	// this.data = data;
	// }

	protected void setId(String id) {
		this.id = id;
	}


	protected String getId() {
		return id;
	}
}
