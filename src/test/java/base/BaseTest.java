package base; // paquete donde se coloca la clase base para tests

import io.restassured.RestAssured; // importa RestAssured para configurar baseURI y opciones
import org.testng.annotations.BeforeClass; // importa la anotación BeforeClass de TestNG
import utils.ConfigReader; // importa el lector de configuración

// Clase base que contiene configuración común para todas las pruebas
public class BaseTest { // declara la clase pública BaseTest

    @BeforeClass
    public void setup() {
        // lee propiedad o valor por defecto
        RestAssured.baseURI = System.getProperty("api.base", "https://jsonplaceholder.typicode.com"); // asigna la baseURI a RestAssured para todas las peticiones
        // RestAssured.port podría configurarse si se requiere (ejemplo comentado)
        // String portProp = System.getProperty(\"api.port\"); // obtiene propiedad de puerto si existe
        // if (portProp!= null) RestAssured.port = Integer.parseInt(portProp); // parsea y asigna el puerto
        RestAssured.useRelaxedHTTPSValidation(); // permite validación SSL relajada para certificados self-signed
    }

}