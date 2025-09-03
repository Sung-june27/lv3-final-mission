package finalmission.helper;

import finalmission.member.dto.request.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

public class TestHelper {

    public static String login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("auth/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        return response.getCookie("token");
    }
}
