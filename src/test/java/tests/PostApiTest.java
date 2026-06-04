package tests;

import base.BaseTest;
import clients.ApiClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@Feature("Posts Management")
public class PostApiTest extends BaseTest {
    private ApiClient apiClient;
    private Map<String, Object> body;

    @BeforeClass
    @Override
    public void setup() {
        super.setup();
        apiClient = new ApiClient();
    }

    @Test
    @Story("Obtener un post existente")
    @Description("Obtiene el post con ID 1 y valida sus campos")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPost() {
        obtenerPostConId(1);
    }

    @Step("Obtener post con ID {postId}")
    private void obtenerPostConId(int postId) {
        Response response = apiClient.get("/posts/" + postId);

        response.then()
                .statusCode(200)
                .body("userId", notNullValue())
                .body("id", equalTo(postId));
    }

    @Test
    @Story("Crear un nuevo post")
    @Description("Crea un nuevo post y valida que se retorna código 201")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreatePost() {
        crearNuevoPost();
        validarCreacionPost();
    }

    @Step("Crear un nuevo post")
    private void crearNuevoPost() {
        body = new HashMap<>();  // se guarda en variable global

        body.put("title", "Nuevo Post");
        body.put("body", "Contenido del test");
        body.put("userId", 1);
    }

    @Step("Validar que se ha creado correctamente el nuevo post")
    private void validarCreacionPost() {
        Response response = apiClient.postWithStatusCode("/posts", body, 201);

        response.then()
                .statusCode(201)
                .body("title", equalTo("Nuevo Post"))
                .body("body", equalTo("Contenido del test"))
                .body("userId", equalTo(1));
    }

    @Test
    @Story("Actualizar un post existente")
    @Description("Actualiza el post con ID 1 completamente (PUT)")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdatePost() {
        actualizarPostConId(1);
        validarActualizarPost(1);
    }

    private Response response;        // ✅ opcional pero recomendable

    @Step("Actualizar post con ID {postId}")
    private void actualizarPostConId(int postId) {
        body = new HashMap<>();

        body.put("id", postId);  // ✅ usar parámetro
        body.put("title", "Titulo Actualizado");
        body.put("body", "Contenido Actualizado");
        body.put("userId", 1);

        // ✅ Ejecutar el PUT aquí (mejor práctica)
        response = apiClient.put("/posts/" + postId, body);
    }

    @Step("Validar que el post fue actualizado correctamente")
    private void validarActualizarPost(int postId) {
        response.then()
                .statusCode(200)
                .body("id", equalTo(postId))
                .body("title", equalTo("Titulo Actualizado"))
                .body("body", equalTo("Contenido Actualizado"))
                .body("userId", equalTo(1));
    }

    @Test
    @Story("Eliminar un post existente")
    @Description("Elimina el post con ID 1 usando DELETE")
    public void testDeletePost() {
        validarEliminacionPost();
    }
        @Step("Validar que el post fue eliminado (status 200)")
        private void validarEliminacionPost () {
            Response response = apiClient.delete("/posts/1");
            response.then()
                    .statusCode(200);
        }
}

