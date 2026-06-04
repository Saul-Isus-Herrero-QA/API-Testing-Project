package tests;

import base.BaseTest;
import clients.ApiClient;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.CSVDataProvider;

import static org.hamcrest.Matchers.*;

// Test data-driven: crea posts usando los datos del CSV
public class DataDrivenPostTest extends BaseTest {

    private ApiClient apiClient;

    @BeforeClass
    public void setup() {
        super.setup(); // configura RestAssured desde BaseTest
        apiClient = new ApiClient(); // instancia del cliente reusable
    }

    // Usa DataProvider definido en utils.CSVDataProvider
    @Test(dataProvider = "postsCsv", dataProviderClass = CSVDataProvider.class)
    public void createPostsDataDriven(String title, String body, int userId) {
        // Construye JSON de petición
        String json = String.format("{\"title\":\"%s\",\"body\":\"%s\",\"userId\":%d}", title, body, userId);

        // Llama al ApiClient que encapsula RestAssured
        Response response = apiClient.postWithStatusCode("/posts", json, 201); // valida 201 dentro del client

        // Adjunta la respuesta en Allure para revisión en el reporte
        Allure.addAttachment("response-body", response.getBody().asString());

        // Aserciones adicionales (por si no hicimos validación del código dentro del client)
        response.then()
                .body("title", equalTo(title))
                .body("body", equalTo(body))
                .body("userId", equalTo(userId));
    }
}