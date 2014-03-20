package game3d.mapping;

import game3d.socketserver.model.DeviceSocketChannel;

public class User extends DeviceSocketChannel.DeviceInfo {
	private String name;
	private int currentRoom;
	private int currentTankType;
	private Tank currentTank;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(int currentRoom) {
		this.currentRoom = currentRoom;
	}

	public int getCurrentTankType() {
		return currentTankType;
	}

	public void setCurrentTankType(int currentTankType) {
		this.currentTankType = currentTankType;
	}

	public Tank getCurrentTank() {
		return currentTank;
	}

	public void setCurrentTank(Tank currentTank) {
		this.currentTank = currentTank;
	}
}
