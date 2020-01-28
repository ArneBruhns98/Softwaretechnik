package swtII.whiteboard.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to build events from
 */
public class EventBuilder {
	private Event.Type type;
	private ContentBuilder contentBuilder = new ContentBuilder();

	/**
	 * Constructor to create an EventBuilder from a specific type
	 * @param type type to create the builder or
	 * @return an EventBuilder to further build the event
	 */
	public EventBuilder type(Event.Type type) {
		this.type = type;
		return this;
	}

	/**
	 * Creates a ContentBuilder to use if Event contains a payload
	 * @return a ContentBuilder to further build the content
	 */
	public ContentBuilder withPayload() {
		return contentBuilder;
	}

	private Event buildEvent(Content content) {
		return new Event(type, content);
	}

	protected class ContentBuilder {

		private String alias;
		private Map<String, Object> properties = new HashMap<>();

		/**
		 * Constructor to create a ContentBuilder by username
		 * @param alias username
		 * @return a new Contentbuilder
		 */
		public ContentBuilder userAlias(String alias) {
			this.alias = alias;
			return this;
		}

		/**
		 * Sets the username for this ContentBuilder to the username the content is build from
		 * @param user user to get the username from
		 * @return this Contentbuilder for further building
		 */
		public ContentBuilder user(User user) {
			this.alias = user.getName();
			return this;
		}

		/**
		 * Sets the username for this ContentBuilder to the systemuser, to create payload from systemuser
		 * @return this Contentbuilder for further building
		 */
		public ContentBuilder systemUser() {
			user(User.systemUser());
			return this;
		}

		/**
		 * Sets properties of the content which is to build
		 * @param property single entries from maps by String key
		 * @param value single object to map to String property
		 * @return this Contentbuilder for further building
		 */
		public ContentBuilder property(String property, Object value) {
			properties.put(property, value);
			return this;
		}

		/**
		 * Finally builds an event from a user and content by that user
		 * @return an event which was build
		 */
		public Event build() {
			return buildEvent(new Content(new User(contentBuilder.alias), properties));
		}
	}
}