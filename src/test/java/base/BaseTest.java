package base; // paquete donde se coloca la clase base para tests. En esta clase se definen los métodos transversales para el resto de tests.

import io.restassured.RestAssured; // Importa RestAssured para configurar baseURI, etc.
import org.testng.annotations.BeforeClass; // Importa la anotación BeforeClass de TestNG
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import io.qameta.allure.Allure; // Importa la librería de Allure que sirve para mostrar reportes de los tests ejecutados.

// Clase Base contiene la configuración común para todos los tests.
public class BaseTest {
    @BeforeClass
    public void setup(){
        //Se asigna la URL por defecto o base para todas las peticiones.
        RestAssured.baseURI = System.getProperty("api.base", "https://jsonplaceholder.typicode.com");
        /** Se usa para desactivar la validación de certificados SSL/HTTPS, permitiendo que RestAssured acepte conexiones seguras
         incluso si el certificado es inválido, autofirmado o no confiable. */
        RestAssured.useRelaxedHTTPSValidation();
    }

    @BeforeMethod
    public void beforeEachTest(){
        // Código que se ejecuta antes de cada test, por ejemplo, para iniciar sesión o configurar datos necesarios.
    }

    @AfterMethod
    public void afterEachTest(org.testng.ITestResult result){
        // Código que se ejecuta después de cada test, por ejemplo, para limpiar datos o cerrar sesiones.
        // Aquí si falla, se incluye información de Allure.
        if (!result.isSuccess()) {
            // Ejeomplo de texto o logs
            Allure.addAttachment("failure-info", "Test " + result.getName() + "failed");
        }
    }
}
