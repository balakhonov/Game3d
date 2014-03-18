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
		private String tooken;
		private int vehicleId;
		private int companyId;
		private int tripId;
		private boolean active = false;


		public int getVehicleId() {
			return vehicleId;
		}


		public void setVehicleId(int vehicleId) {
			if (vehicleId < 1) {
				throw new IllegalArgumentException("Unit ID should not be < 1");
			}

			this.vehicleId = vehicleId;
		}


		public int getCompanyId() {
			return companyId;
		}


		public void setCompanyId(int companyId) {
			if (companyId < 1) {
				throw new IllegalArgumentException("Company ID should not be < 1");
			}

			this.companyId = companyId;
		}


		public int getTripId() {
			return tripId;
		}


		public void setTripId(int tripId) {
			this.tripId = tripId;
		}


		public String getTooken() {
			return tooken;
		}


		public void setTooken(String tooken) {
			this.tooken = tooken;
		}


		public boolean isActive() {
			return active;
		}


		public void setActive(boolean active) {
			this.active = active;
		}


		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(", Tooken: " + tooken);
			sb.append(", Company ID: " + companyId);
			sb.append(", Vehicle ID: " + vehicleId);
			sb.append(", Trip ID: " + tripId);
			sb.append(", Is active: " + active);

			return sb.toString();
		}
	}
}
