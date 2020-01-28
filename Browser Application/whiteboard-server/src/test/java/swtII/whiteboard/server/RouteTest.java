package swtII.whiteboard.server;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
class RouteTest {

	  @Autowired
	  private WebTestClient webTestClient;

	  @Test
	  public void testEmptyRoomListIsEmpty() {

	    webTestClient
	      .get().uri("/rooms")
	      .accept(MediaType.APPLICATION_JSON)
	      .exchange()
	      .expectStatus().isOk()
	      .expectBody(String.class).isEqualTo("[]");
}
	  
	  @Test
	  public void testNotImplementedRouteIsNotFound() {
		    webTestClient
		      .get().uri("/example2")
		      .accept(MediaType.APPLICATION_JSON)
		      .exchange()
		      .expectStatus().isNotFound();
	  }
	  
	  @Test
	  public void testIndexHtmlResponse() {
		    webTestClient
		      .get().uri("/")
			  .accept(MediaType.APPLICATION_JSON)
		      .exchange()
		      .expectStatus().isOk();
	  }

	@Test
	public void testmainjsResponse() {
		webTestClient
				.get().uri("/main.js")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk();
	}
	@Test
	public void testfaviconicoResponse() {
		webTestClient
				.get().uri("/favicon.ico")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk();
	}
	@Test
	public void teststylescssResponse() {
		webTestClient
				.get().uri("/styles.css")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk();
	}
	@Test
	public void testCreateRoom(){
        String test1 = "{\"name\":\"test1\",\"password\":\"123\",\"maxUsers\":\"6\"}";
		String test2 = "{\"name\":\"test2\",\"password\":\"1234\",\"maxUsers\":\"7\"}";

		//POST test1 : isCreated()
		webTestClient
				.post().uri("/rooms")
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(test1))
				.exchange()
				.expectStatus().isCreated()
				.expectBody(String.class).isEqualTo("Ihr Raum wurde erfolgreich angelegt.");

		//GET : isOk(), isEqualTo(test1)
		webTestClient
	            .get().uri("/rooms")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("[{\"name\":\"test1\",\"maxUsers\":6,\"currentUsers\":0," +
				"\"full\":false,\"passwordLocked\":true}]");

		//POST test1 : Status = 409
		webTestClient
				.post().uri("/rooms")
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(test1))
				.exchange()
				.expectStatus().isEqualTo(409)
				.expectBody(String.class).isEqualTo("Es existiert bereits ein Raum mit diesem Namen.");

		//GET : isOk(), isEqualTo(test1)
		webTestClient
				.get().uri("/rooms")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("[{\"name\":\"test1\",\"maxUsers\":6,\"currentUsers\":0," +
				"\"full\":false,\"passwordLocked\":true}]");

		//POST test2 : isCreated()
		webTestClient
				.post().uri("/rooms")
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(test2))
				.exchange()
				.expectStatus().isCreated()
				.expectBody(String.class).isEqualTo("Ihr Raum wurde erfolgreich angelegt.");

		//GET : isOk(), isEqualTo(test1,test2)
		webTestClient
				.get().uri("/rooms")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("[{\"name\":\"test1\",\"maxUsers\":6,\"currentUsers\":0,\"full\":false" +
				",\"passwordLocked\":true},{\"name\":\"test2\",\"maxUsers\":7,\"currentUsers\":0,\"full\":false,\"passwordLocked\":true}]");
	}

	}