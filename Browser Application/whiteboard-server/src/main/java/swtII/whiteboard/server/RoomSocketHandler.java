package swtII.whiteboard.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.io.IOException;
import java.util.Optional;

import static swtII.whiteboard.server.Event.Type.USER_LEFT;

/**
 * Class which represents single client to server connections and how to handle them,
 * by subscribing to roomspecific publishers, also maps incoming and outgoing events from and to json
 */
public class RoomSocketHandler implements WebSocketHandler {

	private ObjectMapper mapper;
	private RoomHandler roomHandler;

	/**
	 * Constructor for RoomSocketHandler
	 * @param roomhandler
	 */
	public RoomSocketHandler(RoomHandler roomhandler) {
		this.mapper = new ObjectMapper();
		this.roomHandler = roomhandler;
	}

	/**
	 * Overriden methode to handle single WebSocketSession connections from clients, and subscribes
	 * to the publisher and eventsource of the room, which includes mapping to json
	 * @param session Session which is to handle
	 * @return a asynch Mono
	 */
	@Override
	public Mono<Void> handle(WebSocketSession session) {
		String room = session.getHandshakeInfo().getUri().toString().split("/")[4];
		Room roomToJoin = roomHandler.getRoomEntityByName(room);

		WebSocketMessageSubscriber subscriber = new WebSocketMessageSubscriber(roomToJoin.getEventProcessor());
		session.receive().map(WebSocketMessage::getPayloadAsText).map(this::toEvent).subscribe(subscriber::onNext,
				subscriber::onError, subscriber::onComplete);
		return session.send(roomToJoin.getEvents().map(this::toJSON).map(session::textMessage));
	}

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

	private static class WebSocketMessageSubscriber {
		private UnicastProcessor<Event> eventPublisher;
		private Optional<Event> lastReceivedEvent = Optional.empty();

		/**
		 * Constructor for a WebSocketMessageSubscriber
		 * @param eventPublisher Publisher to subscribe to
		 */
		public WebSocketMessageSubscriber(UnicastProcessor<Event> eventPublisher) {
			this.eventPublisher = eventPublisher;
		}

		/**
		 * Handle next events
		 * @param event event to handle
		 */
		public void onNext(Event event) {
			lastReceivedEvent = Optional.of(event);
			eventPublisher.onNext(event);
		}

		/**
		 * Errorhandling
		 * @param error error to handle
		 */
		public void onError(Throwable error) {
			// TODO log error
			error.printStackTrace();
		}

		/**
		 * Handles connection closing, e.g. USER_LEFT currently implemented
		 */
		public void onComplete() {

			lastReceivedEvent.ifPresent(
					event -> eventPublisher.onNext(Event.type(USER_LEFT).withPayload().user(event.getUser()).build()));
		}

	}
}