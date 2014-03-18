package game3d.websocketserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class SerializableList extends ArrayList<Object> implements Serializable {
	private static final long serialVersionUID = 1L;

	public SerializableList(Collection<? extends Serializable> c) {
		super(c);
	}
}
