package swtII.whiteboard.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Test for WebSocketconnections by sending a message and comparing the answer to
 * the send message.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
public class WebSocketTest {


	/**
	 * Queue that blocks is used for storing the answer
	 */
	static BlockingQueue<String> blockingQueue;
	/**
	 * Connectionmanager to handle the connection
	 */
	static WebSocketConnectionManager cManager;
	/**
	 * Message which will be send to the server
	 */
	static String messageToSend;

	/**
	 * Setup method to prepare the Queue and connection.
	 * Establishes the connection and prepares the message that will be send
	 */
	@BeforeAll
	public static void setup() {
		blockingQueue = new LinkedBlockingDeque<>();
		cManager =  new WebSocketConnectionManager(new StandardWebSocketClient(), new RoomWebSocketHandler(), "ws://localhost:8080/rooms/hallo/ws");
		cManager.start();
		messageToSend = "42";
	}

	/**
	 * Test to compare a send message to the answer
	 * @throws Exception
	 */
	@Test
	public void shouldReceiveAMessageFromTheServer() throws Exception {
		JsonElement root = new JsonParser().parse(blockingQueue.poll(10, SECONDS));
		assertEquals(messageToSend, root.getAsJsonObject().get("content").getAsJsonObject().get("properties").getAsJsonObject().get("message").getAsString());
		cManager.stop();
	}

	/**
	 * Class to handle events from the connection
	 */
	static class RoomWebSocketHandler extends TextWebSocketHandler {

		/**
		 * Method to handle incoming messages
		 * @param session to use
		 * @param message text message that was received from the server
		 */
		@Override
		public void handleTextMessage(WebSocketSession session, TextMessage message) {
			System.out.println("Message Received [" + message.getPayload() + "]");
			blockingQueue.add(message.getPayload());
		}

		/**
		 * Method that is run after an connection has been established
		 * @param session to use
		 * @throws Exception if connection is interrupted
		 */
		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			System.out.println("Connected");
			String payload = "{\"content\":{\"user\":{\"name\":\"anonymous\"},\"properties\":{\"message\":" + messageToSend + "}}, \"type\": \"CHAT_MESSAGE\"}";
			System.out.println("Sending [" + payload + "]");
			session.sendMessage(new TextMessage(payload));
		}

		/**
		 * Method that is called if an error occurs
		 * @param session to use
		 * @param exception exception to handle
		 */
		@Override
		public void handleTransportError(WebSocketSession session, Throwable exception) {
			System.out.println("Transport Error " + exception.getMessage());
		}

		/**
		 * Method that is called after a connection has been closed
		 * @param session to use
		 * @param status status with which the connection closed
		 */
		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
			System.out.println("Connection Closed [" + status.getReason() + "]");
		}
	}
}
