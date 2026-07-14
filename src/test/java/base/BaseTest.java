package base; // paquete donde se coloca la clase base para tests. En esta clase se definen los métodos transversales para el resto de tests.

import io.restassured.RestAssured; // Importa RestAssured para configurar baseURI, etc.
import org.testng.ITestResult;
import org.testng.annotations.BeforeClass; // Importa la anotación BeforeClass de TestNG
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import io.qameta.allure.Allure; // Importa la librería de Allure que sirve para mostrar reportes de los tests ejecutados.
import utils.CookiesManager; // Importa la utilidad de la gestión de cookies.
import clients.ApiClient; // Importa la clase reutilizable API client.


// Clase Base contiene la configuración común para todos los tests.
public class BaseTest {
    // Instancia de Cookies Manager compartida para manejar cookies en todos los tests de esta clase.
    protected CookiesManager cookiesManager;
    // Instancia de API Client compartida también por todos los tests de esta clase.
    protected ApiClient apiClient;
    @BeforeClass
    public void setup(){
        //Se asigna la URL por defecto o base para todas las peticiones.
        RestAssured.baseURI = System.getProperty("api.base", "https://jsonplaceholder.typicode.com");
        /** Se usa para desactivar la validación de certificados SSL/HTTPS, permitiendo que RestAssured acepte conexiones seguras
         incluso si el certificado es inválido, autofirmado o no confiable. */
        RestAssured.useRelaxedHTTPSValidation();

        // Crea una nueva instancia de Cookies Manager.
        this.cookiesManager = new CookiesManager();

        // Crea una nueva instancia de Api Client.
        this.apiClient = new ApiClient();

        // Crea un nuevo ApiClient e inyecta la dependencia del Cookies Manager.
        this.apiClient = new ApiClient(this.cookiesManager);

    }

    @BeforeMethod
    public void beforeEachTest(){
        // Las cookies se preservan entre tests.
        // Esta línea siguiente sirve para que cada test empiece con un estado limpio de cookies.
        this.cookiesManager.clearAllCookies();
    }

    @AfterMethod
    public void afterEachTest(ITestResult result) {
        // Adjunta la información de las cookies al reporte de Allure
        Allure.addAttachment(
                "Stored Cookies",
                this.cookiesManager.toString()
        );
        if (!result.isSuccess()) {
            // Ejemplo de texto o logs
            Allure.addAttachment(
                    "failure-info",
                    "Test " + result.getName() + " failed"
            );
        }
    }
}
