package Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class ModelLobby {

	private List<Room> rooms;
	private List<Room> filteredRooms;


	public ModelLobby() {
		this.rooms = new LinkedList<Room>();
	}

	public void createRoom(String name, String password) {
		if (!name.startsWith(" ") && !name.endsWith(" ")) {
			if (name.length() < 25) {
				if (name.length() >= 4) {
					if (null == getRoomInList(name)) {
						this.rooms.add(new Room(name, password));
					} else {
						throw new IllegalArgumentException(
								"Der Raumname muss eindeutig sein, es gibt bereits einen Raum mit diesem Namen.");
					}
				} else {
					throw new IllegalArgumentException("Der Name muss aus mindestens 4 Buchstaben bestehen.");
				}
			} else {
				throw new IllegalArgumentException("Der Name darf aus maximal 25 Zeichen bestehen.");
			}
		} else {
			throw new IllegalArgumentException("Der Name darf nicht mit einem Leerzeichen anfangen oder aufhören.");
		}
	}

	/**
	 * beitreten eines Raumes
	 * @param roomname
	 * @param passwort
	 * @return
	 */
	public void joinRoom(String roomname, String passwort) throws IllegalArgumentException {
		Room room = getRoomInList(roomname);
		if (!(room == null)) {
			if (passwort.equals(room.getPassword())) {
				if (!room.isFull()) {

					// TODO needs to be implemented; or not

				} else {
					throw new IllegalArgumentException("Der Raum hat seine maximale Teilnehmeranzahl erreicht.");
				}
			} else {
				throw new IllegalArgumentException("Das Passwort ist leider falsch.");
			}
		} else {
			throw new IllegalArgumentException(
					"Der Raum scheint nicht mehr zu existieren. Erstelle doch einen eigenen oder tritt einem anderen bei.");
		}
	}

	/**
	 * gibt eine Liste an Raeumen basierend auf Suchanfrage zurueck
	 * 
	 * @param name
	 * @return
	 */
	public void searchRoom(String name) {
		List<Room> out = new ArrayList<Room>();
		if (!(null == name)) {
			for (Room room : this.rooms) {
				if (room.getName().contains(name)) {
					out.add(room);
				}
			}
		}
		this.filteredRooms = out;
	}

	/**
	 * synchronisiert die rooms Liste
	 * @param rooms
	 */
	public void syncRoom(List<Room> rooms) {
		this.rooms = rooms;
	}

	/**
	 * gibt einen Raum aus der rooms Liste zurueck
	 * @param name
	 * @return
	 */
	public Room getRoomInList(String name) {
		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).getName().equals(name)) {
				return rooms.get(i);
			}
		}
		return null;
	}

	public List<Room> getFilteredRooms() {
		return this.filteredRooms;
	}

	public List<Room> getRooms() {
		return this.rooms;
	}

	public void sortRoomsList(int i) { // i = 1 Username desc 2 Username asc 3 Amountuser desc 4 amountuser asc
		switch (i) {
		case 1:
			Collections.sort(rooms, (a, b) -> b.getName().compareTo(a.getName()));
			break;
		case 2:
			Collections.sort(rooms, (a, b) -> b.getName().compareTo(a.getName()));
			Collections.reverse(rooms);
			break;
		case 3:
			Collections.sort(rooms, (a, b) -> ((Integer) b.getCurrentUsers()).compareTo(a.getCurrentUsers()));
			break;
		case 4:
			Collections.sort(rooms, (a, b) -> ((Integer) b.getCurrentUsers()).compareTo(a.getCurrentUsers()));
			Collections.reverse(rooms);
			break;
		}
	}
}
