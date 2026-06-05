package base; // paquete donde se coloca la clase base para tests

import io.restassured.RestAssured; // importa RestAssured para configurar baseURI y opciones
import org.testng.annotations.BeforeClass; // importa la anotación BeforeClass de TestNG
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import io.qameta.allure.Allure;

// Clase base que contiene configuración común para todas las pruebas
public class BaseTest {// declara la clase pública BaseTest
    @BeforeClass
    public void setup() {
        // lee propiedad o valor por defecto
        RestAssured.baseURI = System.getProperty("api.base", "https://jsonplaceholder.typicode.com"); // asigna la baseURI a RestAssured para todas las peticiones
        RestAssured.useRelaxedHTTPSValidation(); // permite validación SSL relajada para certificados self-signed
    }

    @BeforeMethod
    public void beforeEachTest() {
        // Código ejecutado antes de cada método de test
        // Ej: limpiar headers, resetear variables globales, inicializar mocks
    }

    @AfterMethod
    public void afterEachTest(org.testng.ITestResult result) {
        // Código ejecutado después de cada test
        // Ej: si falla, incluir información en Allure
        if (!result.isSuccess()) {
            // ejemplo: attach texto o logs
            Allure.addAttachment("failure-info", "Test " + result.getName() + " falló");
        }
    }

}

