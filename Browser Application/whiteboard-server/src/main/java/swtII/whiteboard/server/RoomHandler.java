package swtII.whiteboard.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;

/**
 * RoomHandler Component which handles and creates responses to AJAX requests
 */
@Component
public class RoomHandler {

	private List<Room> rooms = new ArrayList<Room>();
	
	/**
	 * Methode to return a list of all rooms in json
	 * @param request request to handle
	 * @return an asynch which completes on response complete
	 */
	public Mono<ServerResponse> all(ServerRequest request) {
		if (!rooms.isEmpty()) {
			Flux<Room> room = Flux.fromIterable(rooms);
			return ok().contentType(APPLICATION_JSON).body(room, Room.class);
		} else {
			return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromObject(new ArrayList<Room>()));
			// return noContent().build();
		}
	}

	/**
	 * Methode to create a room serverside by request body which contains a room in json
	 * @param request request to handle
	 * @return an asynch which completes on response complete
	 */
	public Mono<ServerResponse> createRoom(ServerRequest request) {
		return request.bodyToMono(Room.class)
				.flatMap(r -> {
					if(isInList(r.getName())){
						return ServerResponse.status(409)
								.body(BodyInserters.fromObject("Es existiert bereits ein Raum mit diesem Namen."));
					}
					else {
						this.rooms.add(r);
						return ServerResponse.created(request.uri())
								.body(BodyInserters.fromObject("Ihr Raum wurde erfolgreich angelegt."));
					}
				});
	}


	// Needs refactoring
	/**
	 * Methode to return a room by name given in URI from request
	 * @param request request to handle
	 * @return an asynch which completes on response complete
	 */
	public Mono<ServerResponse> getRoomByName(ServerRequest request) {
		List<Room> tempRooms = rooms.stream().filter(x -> x.getName().equals(request.pathVariable("name"))).limit(1)
				.collect(Collectors.toList());
		Room toReturn = tempRooms.size() > 0 ? tempRooms.get(0) : null;
		if (!rooms.isEmpty() && toReturn != null) {
			Mono<Room> room = Mono.just(toReturn);
			return room.then(ok().body(BodyInserters.fromObject(toReturn)));
		} else {
			return Mono.empty().then(notFound().build());
		}
	}

	/**
	 * TODO:
	 * Helperfunction which gets a Room from the list of rooms
	 * @param name name to find the room by
	 * @return the entity of a room
	 */
	public Room getRoomEntityByName(String name) {
		for (Room r : rooms) {
			if (r.getName().equals(name)) {
				return r;
			}

		}
		return new Room("test", "test", 3);

	}

	/**
	 * Getter for the list of rooms
	 * @return a list of rooms
	 */
	public List<Room> getRooms() {
		return rooms;
	}
	
	/**
	 * check if room exists
	 * @param name of the room
	 * @return
	 */
	private boolean isInList(String name) {
		for(Room r : rooms) {
			if(r.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
}