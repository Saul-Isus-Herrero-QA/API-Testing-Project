package clients; // paquete de los clientes de API

import io.restassured.http.ContentType; // importa tipos de contenido para peticiones
import io.restassured.response.Response; // importa la clase Response de RestAssured
import utils.ConfigReader; // importa el lector de configuración

import static io.restassured.RestAssured.*; // importa métodos estáticos de RestAssured

// Clase cliente que encapsula métodos reutilizables para realizar peticiones HTTP
public class ApiClient { // declara la clase pública ApiClient
    public Response get(String endpoint) { // recibe la ruta del endpoint
        return given() // inicia la construcción de petición
                .log().all() // registra la petición completa en consola
                .contentType(ContentType.JSON) // especifica que se espera respuesta JSON
                .when() // indica la sección de ejecución
                .get(endpoint) // realiza GET al endpoint especificado
                .then() // inicia la sección de validación
                .log().all() // registra la respuesta completa en consola
                .extract().response(); // extrae y retorna la respuesta como objeto Response
    }
    public Response getWithStatusCode(String endpoint, int expectedStatus) { // recibe endpoint y código esperado
        return given() // inicia la construcción de petición
                .log().all() // registra la petición completa en consola
                .when() // indica la sección de ejecución
                .get(endpoint) // realiza GET al endpoint
                .then() // inicia la sección de validación
                .log().all() // registra la respuesta en consola
                .statusCode(expectedStatus) // valida que el código de estado sea el esperado
                .extract().response(); // extrae y retorna la respuesta
    }
    public Response post(String endpoint, String body) { // recibe endpoint y cuerpo en formato JSON string
        return given() // inicia la construcción de petición
                .log().all() // registra la petición en consola
                .contentType(ContentType.JSON) // especifica que el cuerpo es JSON
                .body(body) // asigna el cuerpo a la petición
                .when() // indica la sección de ejecución
                .post(endpoint) // realiza POST al endpoint
                .then() // inicia la sección de validación
                .log().all() // registra la respuesta en consola
                .extract().response(); // extrae y retorna la respuesta
    }
    public Response postWithStatusCode(String endpoint, Object body, int expectedStatus) { // recibe endpoint, cuerpo y código esperado
        return given() // inicia la construcción de petición
                .log().all() // registra la petición en consola
                .contentType(ContentType.JSON) // especifica que es JSON
                .body(body) // asigna el cuerpo
                .when() // indica la sección de ejecución
                .post(endpoint) // realiza POST
                .then() // inicia validación
                .log().all() // registra respuesta
                .statusCode(expectedStatus) // valida código de estado
                .extract().response(); // extrae y retorna
    }
    public Response put(String endpoint, Object body) { // recibe endpoint y cuerpo JSON
        return given() // inicia construcción de petición
                .log().all() // registra petición
                .contentType(ContentType.JSON) // especifica contenido JSON
                .body(body) // asigna cuerpo
                .when() // ejecución
                .put(endpoint) // realiza PUT
                .then() // validación
                .log().all() // registra respuesta
                .extract().response(); // extrae respuesta
    }
    public Response patch(String endpoint, Object body) { // recibe endpoint y cuerpo JSON
        return given() // inicia construcción
                .log().all() // registra petición
                .contentType(ContentType.JSON) // especifica JSON
                .body(body) // asigna cuerpo
                .when() // ejecución
                .patch(endpoint) // realiza PATCH
                .then() // validación
                .log().all() // registra respuesta
                .extract().response(); // extrae respuesta
    }
    public Response delete(String endpoint) { // recibe el endpoint a eliminar
        return given() // inicia construcción
                .log().all() // registra petición
                .when() // ejecución
                .delete(endpoint) // realiza DELETE
                .then() // validación
                .log().all() // registra respuesta
                .extract().response(); // extrae respuesta
    }

}
