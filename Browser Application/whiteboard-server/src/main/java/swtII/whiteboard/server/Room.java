package swtII.whiteboard.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

/**
 * Class which represents a single room
 * Contains the asynch publisher and subscriber to handle events
 */
public class Room {

	private String name;
	private String password = "";
	private int maxUsers;
	private int currentUsers;
	private boolean full = false;
	private boolean passwordLocked;

	private UnicastProcessor<Event> eventProcessor = UnicastProcessor.create();
	private Flux<Event> events = eventProcessor.replay(25).autoConnect();
	private ObjectMapper mapper = new ObjectMapper();

	private List<User> users = new ArrayList<User>();


	/**
	 * Constructor for a room.
	 * @param name name for the room, important for ws connections
	 * @param password password the room is protected with, empty if unprotected
	 * @param maxUsers maximum number of users a new room may hold
	 */
	@JsonCreator
	public Room(@JsonProperty("name") String name, @JsonProperty("password") String password,
				@JsonProperty("maxUsers") int maxUsers) {
		this.name = name;
		this.password = password.isEmpty() ? "" : password;
		this.passwordLocked = this.password == "" ? false : true;
		this.maxUsers = maxUsers;
	}

	// Getter and Setter
	// some are ignored while parsing to json
	// --------------------------------------------
	@JsonIgnore
	public UnicastProcessor<Event> getEventProcessor() {
		return eventProcessor;
	}

	@JsonIgnore
	public Flux<Event> getEvents() {
		return events;
	}

	public void setEventProcessor(UnicastProcessor<Event> eventProcessor) {
		this.eventProcessor = eventProcessor;
	}

	public void setEvents(Flux<Event> events) {
		this.events = events;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}


	public String getName() {
		return name;
	}

	public int getMaxUsers() {
		return maxUsers;
	}

	public int getCurrentUsers() {
		return currentUsers;
	}

	public boolean isFull() {
		return full;
	}

	public boolean isPasswordLocked() {
		return passwordLocked;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}

	public void setCurrentUsers(int currentUsers) {
		this.currentUsers = currentUsers;
	}

	public void setFull(boolean full) {
		this.full = full;
	}

	public void setPasswordLocked(boolean passwordLocked) {
		this.passwordLocked = passwordLocked;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	// END Getter and Setter
	// --------------------------------------------

	private Event toEvent(String json) {
		try {
			return mapper.readValue(json, Event.class);
		} catch (IOException e) {
			throw new RuntimeException("Invalid JSON:" + json, e);
		}
	}

	private String toJSON(Event event) {
		try {
			return mapper.writeValueAsString(event);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
