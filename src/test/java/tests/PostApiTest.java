package tests;

import base.BaseTest;
import clients.ApiClient;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class PostApiTest extends BaseTest {

    private ApiClient apiClient;

    @BeforeClass
    @Override
    public void setup() {
        super.setup();
        apiClient = new ApiClient();
    }

    @Test
    public void testGetPost() {

        Response response = apiClient.get("/posts/1");

        response.then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", notNullValue())
                .body("body", notNullValue());
    }

    @Test
    public void testCreatePost() {

        // ✅ JSON usando Map (mejor práctica)
        Map<String, Object> body = new HashMap<>();
        body.put("title", "Nuevo Post");
        body.put("body", "Contenido del test");
        body.put("userId", 1);

        Response response = apiClient.postWithStatusCode("/posts", body, 201);

        response.then()
                .statusCode(201)
                .body("title", equalTo("Nuevo Post"))
                .body("body", equalTo("Contenido del test"))
                .body("userId", equalTo(1));
    }

    @Test
    public void testUpdatePost() {

        // ✅ Map también para PUT
        Map<String, Object> body = new HashMap<>();
        body.put("id", 1);
        body.put("title", "Titulo Actualizado");
        body.put("body", "Contenido Actualizado");
        body.put("userId", 1);

        Response response = apiClient.put("/posts/1", body);

        response.then()
                .statusCode(200)
                .body("title", equalTo("Titulo Actualizado"));
    }

    @Test
    public void testDeletePost() {

        Response response = apiClient.delete("/posts/1");

        response.then()
                .statusCode(200);
    }
}