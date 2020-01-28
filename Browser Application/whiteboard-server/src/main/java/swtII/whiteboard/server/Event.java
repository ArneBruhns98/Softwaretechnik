package swtII.whiteboard.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents Events send from the server to the clients and vice versa, take a look at Type enum
 */
public class Event {
	public enum Type {
		CHAT_MESSAGE, USER_JOINED, USER_STATS, USER_LEFT;
	}

	private static AtomicInteger ID_GENERATOR = new AtomicInteger(0);

	private Type type;

	private final int id;

	private Content content;

	private final long timestamp;

	/**
	 *	Constructor for an Event
	 * @param type the type the event is of
	 * @param content content the event includes
	 */
	@JsonCreator
	public Event(@JsonProperty("type") Type type, @JsonProperty("content") Content content) {
		this.type = type;
		this.content = content;
		this.id = ID_GENERATOR.addAndGet(1);
		this.timestamp = System.currentTimeMillis();
	}

	// Getter and Setter
	// ----------------------------
	public Type getType() {
		return type;
	}

	public Content getContent() {
		return content;
	}

	@JsonIgnore
	public User getUser() {
		return getContent().getUser();
	}

	public int getId() {
		return id;
	}

	public long getTimestamp() {
		return timestamp;
	}
	// END Getter and Setter
	// ----------------------------

	/**
	 * Static methode to create an event from a type
	 * @param type Type to build event from
	 * @return Eventbuilder with specific type
	 */
	public static EventBuilder type(Type type) {
		return new EventBuilder().type(type);
	}
}