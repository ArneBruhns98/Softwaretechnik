package Client;

import util.RoomAbtr;


public class Room extends RoomAbtr {

	private String name;
	private String password = "";
	private int maxUsers;
	private int currentUsers;
	private boolean full = false;
	private boolean passwordLocked;

	public boolean checkRole(String name) {
		return false;
	}

	public Room(String name, String password) {
		if (!name.isEmpty()) {
			this.name = name;
			if (password.isEmpty()) {
				this.passwordLocked = false;
			} else {
				setPassword(password);
				this.passwordLocked = true;
			}

			this.maxUsers = 35; // Magic Value für maxUser pro Room als Default
			this.currentUsers = 0;
		} else {
			throw new IllegalArgumentException("Es muss ein Raumname angegeben werden.");
		}
	}

	public void sendChat() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxUsers() {
		return maxUsers;
	}

	public int getCurrentUsers(){
		return this.currentUsers;
	}

	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}

	public boolean isFull() {
		return this.getMaxUsers() <= this.getCurrentUsers();
	}

	public void setFull(boolean full) {
		this.full = full;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.passwordLocked = true;
	}
	
	public boolean getPasswordLocket() {
		return this.passwordLocked;
	}
	
	public void addUser() {
		if (currentUsers == maxUsers) {
			this.full = true;
		}
		this.currentUsers++;
	}
	
	public void removeUser() {
		this.full = false;
		this.currentUsers--;
	}
}
