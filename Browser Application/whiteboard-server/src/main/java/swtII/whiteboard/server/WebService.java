package swtII.whiteboard.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Entry Point for Spring Application
 */
@SpringBootApplication
public class WebService {

	/**
	 * Roomhandler across all connections
	 */
	RoomHandler roomHandler = new RoomHandler();

	/**
	 * Mainmethod invocation
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(WebService.class, args);
	}

	/**
	 * Routingfunctions to specific content
	 * - / 		via GET => index.html
	 * - /rooms	via GET => list of all rooms in json
	 * - /rooms via POST => create new Room and add to list from Roomhandler
	 * - /rooms/{name} via GET => returns a single room by name
	 * @return a RouterFunction<ServerResponse> corresponding the methode invoked
	 */
	@Bean
	public RouterFunction<ServerResponse> routes() {
		return RouterFunctions
				.route(GET("/"),
						request -> ServerResponse.ok()
								.body(BodyInserters.fromResource(new ClassPathResource("static/index.html"))))
				.andRoute(GET("/rooms"), roomHandler::all)
				.andRoute(POST("/rooms").and(RequestPredicates.accept(APPLICATION_JSON)), roomHandler::createRoom)
				.andRoute(GET("/rooms/{name}").and(RequestPredicates.accept(APPLICATION_JSON)),
						roomHandler::getRoomByName);

	}

	/**
	 * Mapping Bean for websockethandling, URI contains variable name which maps to the websocket of
	 * specific room.
	 * @return a HandlerMapping
	 */
	@Bean
	public HandlerMapping webSocketMapping() {
		Map<String, Object> map = new HashMap<>();
		map.put("/rooms/{name}/ws", new RoomSocketHandler(roomHandler));
		SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
		simpleUrlHandlerMapping.setUrlMap(map);

		// Without the order things break :-/
		simpleUrlHandlerMapping.setOrder(10);
		return simpleUrlHandlerMapping;
	}

	/**
	 * A WebSocketHandlerAdapter Bean
	 * @return a WebSOcketHandlerAdapter
	 */
	@Bean
	public WebSocketHandlerAdapter handlerAdapter() {
		return new WebSocketHandlerAdapter();
	}
}