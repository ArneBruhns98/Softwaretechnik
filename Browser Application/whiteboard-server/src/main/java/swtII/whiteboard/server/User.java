package swtII.whiteboard.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class which represents a user by name may need to be extended to contain a WebSocketSession
 */
public class User {

	private String name;

	/**
	 * Static method for systemUser creation
	 * @return a new User who is systemuser
	 */
	public static User systemUser() {
		return new User("System");
	}

	/**
	 * For parsing purpose
	 * @param name namefield to create user from
	 */
	@JsonCreator
	public User(@JsonProperty("name") String name) {
		this.name = name;

	}

	// Getter and Setter if applicable
	public String getName() {
		return name;
	}

}