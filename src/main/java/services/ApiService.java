package services;

import dto.UserDTO;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiService {
    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    //private static final String USER_URL = BASE_URL + "/user";
    private static final String BASE_PATH = "/user";
    private static final String USER_NAME_PATH = BASE_PATH + "/{userName}";
    private static RequestSpecification spec;


    public ApiService() {
        spec = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .log().all();
    }

    public ValidatableResponse createUser(UserDTO user) {
        return given(spec)
                .basePath(BASE_PATH)
                .body(user)
                .when()
                .post()
                .then()
                .log().all();
    }

    public ValidatableResponse getUser(String userNameValue) {
        return given(spec)
                .basePath(BASE_PATH)
                .when()
                .get(userNameValue)
                .then()
                .log().all();
    }
    public ValidatableResponse deleteUser(String userNamevalue){
        return given(spec)
        .basePath(BASE_PATH)
                .when()
                .delete(userNamevalue)
                .then()
                .log().all();
    }

    public ValidatableResponse putUser(UserDTO user, String userNamevalue) {
        return given(spec)
                .basePath(BASE_PATH)
                .body(user)
                .when()
                .put(userNamevalue)
                .then()
                .log().all();
    }
}

