package game3d.socketserver.model;

public interface DeviceSocketChannel {

	public boolean isAuthorized();


	public void setAuthorized(boolean authorized);


	public DeviceInfo getDeviceInfo();


	public void setDeviceInfo(DeviceInfo di);

	/**
	 * @author yuri
	 */
	public static class DeviceInfo {
		protected int id;
		private String sessionId;
		private boolean active = false;


		public String getSessionId() {
			return sessionId;
		}


		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public boolean isActive() {
			return active;
		}


		public void setActive(boolean active) {
			this.active = active;
		}


		@Override
		public String toString() {
			return String.format("DeviceInfo [ID:%s,SessionId:%s,IsActive:%s]", id, sessionId, active);
		}
	}
}
