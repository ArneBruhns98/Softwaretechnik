package Client;

import Client.ModelLobby;
import Client.Room;
import Client.ViewLobby;


public class Main {

	public static void main(String[] args) {
		ModelLobby model = new ModelLobby();
		// Test Data
		for (int i = 0; i < 10; i++) {
			model.createRoom("Test" + i, "2");
		}
		for (int i = 10; i < 20; i++) {
			model.createRoom("Test" + i, "");
		}
		
		
		for (Room room : model.getRooms()) {
			for (int i = 0; i < (Math.random() * 10); i++) {
				room.addUser();
			}
		}
		
		
		ViewLobby view = new ViewLobby(model);


	}

}
