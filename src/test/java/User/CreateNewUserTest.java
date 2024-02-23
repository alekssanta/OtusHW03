package User;

import dto.UserDTO;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import services.ApiService;

import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.equalTo;
import static utils.RandomUtil.getRandomNum;

public class CreateNewUserTest {
    static int id = getRandomNum(100, 200);
    static String userName = "userName_" + getRandomNum(10, 99);
    static String firstName = "firstName_" + getRandomNum(10, 99);
    static String lastName = "lastName_" + getRandomNum(10, 99);
    static String password = "password_" + getRandomNum(10, 99);
    static String email = "mail" + getRandomNum(10, 99) + ".com";
    static String phone = valueOf(getRandomNum(891200000, 891299999));
    String CREATE_PUT_DELETE_USER_JSON = "json/create_put_delete_user.json";
    String GET_USER_JSON = "json/get_user.json";
    ApiService apiUser = new ApiService();

    @Test
    /* 1) создаем юзера с мин набором полей (id, username)
    проверяем ответ (200, код message = id),json схему
    2) гет созданного юзера
    проверка ответа (200, id, username) json схемы
     3) пут юзера
     проверка ответа (200, код message = id) json схемы
     4)  гет измененного юзера
    проверка ответа (200, id, username и остальных полей) json схемы
     4) удаляем юзера
        проверка ответа (200, код message = id) json схемы
        5) гет удаленного юзера
        проверка ответ( 404, "message": "User not found")
     */
    public void createAndDeleteUser() {
        UserDTO userDTO = UserDTO.builder()
                .id(id)
                .username(userName)
                .build();

        apiUser.createUser(userDTO)
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(valueOf(id)))
                .body("code", equalTo(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(CREATE_PUT_DELETE_USER_JSON));

        try {
            apiUser.getUser(userName)
                    .statusCode(HttpStatus.SC_OK)
                    .body("id", equalTo(id))
                    .body("username", equalTo(userName))
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(GET_USER_JSON));

            UserDTO modifiedUserDTO = UserDTO.builder()
                    .id(id)
                    .username(userName)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password(password)
                    .phone(phone)
                    .userStatus(id)
                    .build();

            apiUser.putUser(modifiedUserDTO, userName)
                    .statusCode(HttpStatus.SC_OK)
                    .body("code", equalTo(200))
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(CREATE_PUT_DELETE_USER_JSON));

            apiUser.getUser(userName)
                    .statusCode(HttpStatus.SC_OK)
                    .body("id", equalTo(id))
                    .body("username", equalTo(userName))
                    .body("firstName", equalTo(firstName))
                    .body("lastName", equalTo(lastName))
                    .body("email", equalTo(email))
                    .body("password", equalTo(password))
                    .body("phone", equalTo(phone))
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(GET_USER_JSON));

        } finally {
            apiUser.deleteUser(userName)
                    .statusCode(HttpStatus.SC_OK)
                    .body("message", equalTo(userName))
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(CREATE_PUT_DELETE_USER_JSON));

            apiUser.getUser(userName)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body("message", equalTo("User not found"));
        }
    }

    @Test
        /* 1) создаем юзера с полным набором полей
    проверяем ответ (200, код message = id),json схему
    2) гет созданного юзера
    проверка ответа (200, все поля) json схемы
     4) удаляем юзера
        проверка ответа (200, код message = id) json схемы */
    public void createUserWithAllFields() {
        UserDTO userDTO = UserDTO.builder()
                .id(id)
                .username(userName)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .phone(phone)
                .userStatus(id)
                .build();
        apiUser.createUser(userDTO)
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(valueOf(id)))
                .body("code", equalTo(200));
        try {
            apiUser.getUser(userName)
                    .statusCode(HttpStatus.SC_OK)
                    .body("id", equalTo(id))
                    .body("username", equalTo(userName))
                    .body("firstName", equalTo(firstName))
                    .body("lastName", equalTo(lastName))
                    .body("email", equalTo(email))
                    .body("password", equalTo(password))
                    .body("phone", equalTo(phone));
        } finally {
            apiUser.deleteUser(userName)
                    .statusCode(HttpStatus.SC_OK)
                    .body("message", equalTo(userName));
        }
    }

    @Test
    /* создаем юзера с пустым полем
    проверям что юзер создался методом гет
    (баг, 200 при создании юзера, хотя метод гет не поддерживает пустое поле
     */
    public void createNegativeUser() {
        UserDTO userDTO = UserDTO.builder()
                .username("")
                .build();

        apiUser.createUser(userDTO)
                .statusCode(HttpStatus.SC_OK)
                .body("code", equalTo(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(CREATE_PUT_DELETE_USER_JSON));
        apiUser.getUser("")
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }
}
