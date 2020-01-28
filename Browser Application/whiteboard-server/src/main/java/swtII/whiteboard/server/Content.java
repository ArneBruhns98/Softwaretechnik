package swtII.whiteboard.server;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Class which represents the content of a event
 */
public class Content {

	private User user;

	private Map<String, Object> properties = new HashMap<>();

	/**
	 * Constructor for Content
	 * @param user user who created content
	 * @param properties content itself
	 */
	public Content(User user, Map<String, Object> properties) {
		this(user);
		this.properties = properties;
	}

	@JsonCreator
	private Content(@JsonProperty("user") User user) {
		this.user = user;
	}

	/**
	 * @return the User the content was created by
	 */
	public User getUser() {
		return user;
	}

	@JsonAnySetter
	private void setProperties(String name, Object value) {
		properties.put(name, value);
	}

	@JsonAnyGetter
	private Map<String, Object> getProperties() {
		return properties;
	}

}