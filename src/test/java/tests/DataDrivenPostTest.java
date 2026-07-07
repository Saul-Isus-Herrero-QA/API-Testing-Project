package tests;

import base.BaseTest;
import clients.ApiClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.CSVDataProvider;

import static org.hamcrest.Matchers.*;

// Test data-driven: crea posts usando los datos del CSV, este tag es para poder hacer api testing.
@Feature("Posts Management - Data Driven")
public class DataDrivenPostTest extends BaseTest {
    // Crea una variable de tipo ApiClient
    private ApiClient apiClient;

    @BeforeClass
    public void setup() {
        super.setup(); // configura RestAssured desde BaseTest
        apiClient = new ApiClient(); // nueva instancia del cliente reusable
    }

    // Usa DataProvider definido en utils.CSVDataProvider
    @Test(dataProvider = "postsCsv", dataProviderClass = CSVDataProvider.class)
    @Story("Crear posts desde CSV")
    @Description("Crea posts usando los datos del archivo CSV")
    @Severity(SeverityLevel.NORMAL)
    public void createPostsDataDriven(String title, String body, int userId) {
        // Construye JSON de petición
        String json = String.format("{\"title\":\"%s\",\"body\":\"%s\",\"userId\":%d}", title, body, userId);

        // Llama al ApiClient que encapsula RestAssured
        Response response = apiClient.postWithStatusCode("/posts", json, 201); // verifica 201 dentro del client

        // Adjunta la respuesta en Allure para revisión en el reporte
        Allure.addAttachment("response-body", response.getBody().asString());
        
        // Log del caso de prueba ejecutado
        Allure.step("POST /posts con título: " + title);

        // Asserts en la respuesta de la petición de POST
        response.then()
                .body("title", equalTo(title))
                .body("body", equalTo(body))
                .body("userId", equalTo(userId));
    }
}