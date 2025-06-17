//package tech.best;
//
//import io.restassured.builder.RequestSpecBuilder;
//import io.restassured.specification.RequestSpecification;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import tech.best.controller.PlainMessage;
//
//import static io.restassured.RestAssured.given;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.awaitility.Awaitility.await;
//
//@SpringBootTest(
//        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
//)
//@Import(ContainerConfig.class)
//public class SignMessageSpringBootTest {
//
//  protected RequestSpecification requestSpecification;
//
//  @LocalServerPort
//  int port;
//
//  @BeforeEach
//  void setup() {
//    requestSpecification = new RequestSpecBuilder()
//            .setPort(port)
//            .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//            .build();
//  }
//
//  @DisplayName("when requesting to sign a text then it is signed")
//  @Test
//  void signText() {
//    var response = given(requestSpecification)
//            .when().body(new PlainMessage("Hello"))
//            .post("/messages");
//
//    response.then().statusCode(HttpStatus.ACCEPTED.value());
//
//    String location = response.then().extract().header(HttpHeaders.LOCATION);
//    String messageId = location.replace("/messages/", "");
//
//    await().untilAsserted(() -> {
//      // This way we extract the signature and check it manually
//      String signature = given(requestSpecification)
//              .when()
//              .get("/messages/{messageId}", messageId)
//              .then()
//              .extract()
//              .path("signature");
//      assertThat(signature).isNotNull();
//    });
//  }
//
//}