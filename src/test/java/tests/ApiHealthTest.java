package tests;

import base.BaseTest;
import clients.ApiClient; // importa ApiClient para encapsular RestAssured y ejecutar peticiones
import io.qameta.allure.*; // importa todas las anotaciones de Allure para reportes
import io.restassured.response.Response; // importa Response para capturar respuesta de la petición
import org.testng.annotations.BeforeClass; // importa para setup de la clase
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

// Clase de prueba para verificar que la API responde correctamente con pasos en Allure
@Feature("Health Check")
public class ApiHealthTest extends BaseTest {

    // Crea una variable de tipo ApiClient para reutilizar en los tests
    private ApiClient apiClient;

    // Método setup que se ejecuta antes de cada clase
    @BeforeClass
    public void setup() {
        super.setup(); // configura RestAssured desde BaseTest
        apiClient = new ApiClient(); // nueva instancia del cliente reusable
    }

    @Test // Se marca cómo test
    @Story("Verificar endpoint GET /posts/1") // Allure.
    @Description("Verifica que el endpoint /posts/1 responda correctamente con código 200 y campos válidos") // Descripción detallada
    @Severity(SeverityLevel.NORMAL) // Nivel de severidad del test
    public void healthCheck() {
        // Paso 1: Realizar GET al endpoint /posts/1
        Allure.step("Realizar GET al endpoint /posts/1");
        Response response = apiClient.getWithStatusCode("/posts/1", 200); // ejecuta GET verificando 200 en el client

        // Paso 2: Adjuntar el body de la respuesta al reporte de Allure para revisión
        Allure.addAttachment("response-body", response.getBody().asString()); // adjunta la respuesta en Allure

        // Paso 3: Log del caso de prueba ejecutado
        Allure.step("Validar que el GET retorna código 200"); // registra el paso en Allure

        // Paso 4: Validar status code 200
        response.then() // inicia la parte de validación de la respuesta
                .statusCode(200); // verifica que el código HTTP sea 200 correcto

        // Paso 5: Log de validación de campos
        Allure.step("Validar que los campos de respuesta no son nulos"); // registra el paso en Allure

        // Paso 6: Validaciones de campos devueltos por el endpoint
        response.then() // inicia la parte de validación de la respuesta
                .body("userId", notNullValue()) // Verificamos que el userId exista (no sea nulo)
                .body("id", equalTo(1)) // Confirmamos que nos devolvió exactamente el ID que solicitamos (1)
                .body("title", notNullValue()) // Verificamos que contenga un título
                .body("body", notNullValue()); // Verificamos que contenga un cuerpo de texto
    }
}
