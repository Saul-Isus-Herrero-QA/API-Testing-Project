package tests; // paquete donde estarán los tests

import base.BaseTest; // importa la clase base con la configuración común
import io.qameta.allure.Description; // reporting allure
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test; // importa la anotación Test de TestNG

import static io.restassured.RestAssured.*; // importa métodos estáticos de RestAssured para given/when/then
import static org.hamcrest.Matchers.*; // importa Matchers de Hamcrest para aserciones

// Clase de prueba para verificar que la API responde correctamente
@Feature("Health Check")
public class ApiHealthTest extends BaseTest { // la clase extiende BaseTest para heredar configuración

    @Test // Se marca cómo test.
    public void healthCheck() { // nombre del caso de prueba que verifica el endpoint /get
        given() // inicia la construcción de la petición con RestAssured
                .log().all() // registra toda la petición en consola para debugging
                .when() // indica la parte de ejecución de la petición
                .get("/posts/1") // realiza un GET al recurso /get relativo a baseURI
                .then() // inicia la parte de validación de la respuesta
                .log().all() // registra toda la respuesta en consola para debugging
                .statusCode(200) // verifica que el código HTTP sea 200 OK
                // Se valida que los campos devueltos por el endpoint (URL específica de una API donde nos conectamos) /posts/1:
                .body("userId", notNullValue()) // Verificamos que el userId exista (no sea nulo).
                .body("id", equalTo(1)) // Confirmamos que nos devolvió exactamente el ID que solicitamos (1).
                .body("title", notNullValue()) // Verificamos que contenga un título.
                .body("body", notNullValue()); // Verificamos que contenga un cuerpo de texto.
    }
}