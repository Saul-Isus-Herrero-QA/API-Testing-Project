package clients; // paquete de los clientes de API

import io.restassured.http.ContentType; // importa tipos de contenido para peticiones
import io.restassured.response.Response; // importa la clase Response de RestAssured
import utils.ConfigReader; // importa el lector de configuración
import static io.restassured.RestAssured.*; // importa métodos estáticos de RestAssured
import utils.CookiesManager; // Importa la clase CookieManager para gestionar cookies
import io.restassured.specification.RequestSpecification; // Importa la especificación RequestSpecification para construcción de peticiones
import java.util.Map;

// Clase cliente que encapsula métodos reutilizables para realizar peticiones HTTP
//Saúl Isús Herrero.
// 3 de julio del 2026.
// Entorno : API de pruebas https://jsonplaceholder.typicode.com
public class ApiClient {
    // Variable privada que almacena la instancia de CookiesManager para gestionar cookies entre peticiones
    private CookiesManager cookiesManager;

    // Constructor de la clase ApiClient que recibe un CookiesManager como parámetro
    // Permite la inyección de dependencia del gestor de cookies
    public ApiClient(CookiesManager cookiesManager) {
        // Asigna el gestor de cookies recibido a la variable de instancia
        this.cookiesManager = cookiesManager;
    }

    public ApiClient() {

    }

    /** Método privado auxiliar que aplica las cookies almacenadas a una especificación de petición
     * Recibe como parámetro una RequestSpecification sin las cookies aplicadas aún
     * Retorna la RequestSpecification con todas las cookies del administrador aplicadas
     */
    private RequestSpecification applyStoredCookies(RequestSpecification requestSpec) {
        // Verifica si hay un gestor de cookies disponible
        if (this.cookiesManager != null) {
            // Obtiene todas las cookies almacenadas en el gestor
            Map<String, String> storedCookies = this.cookiesManager.getAllCookies();

            // Itera sobre cada cookie almacenada
            for (Map.Entry<String, String> entry : storedCookies.entrySet()) {
                // Obtiene el nombre de la cookie del par clave-valor
                String cookieName = entry.getKey();
                // Obtiene el valor de la cookie del par clave-valor
                String cookieValue = entry.getValue();
                // Aplica la cookie a la especificación de la petición
                requestSpec = requestSpec.cookie(cookieName, cookieValue);
            }
        }

        // Retorna la especificación con todas las cookies aplicadas
        return requestSpec;
    }

    /** Método privado auxiliar que extrae las cookies de una respuesta y las almacena en el gestor
     * Recibe como parámetro un objeto Response que contiene las cookies
     */
    private void storeCookiesFromResponse(Response response) {
        // Verifica si hay un gestor de cookies disponible
        if (this.cookiesManager != null && response != null) {
            // Instruye al gestor de cookies que extraiga y almacene las cookies de la respuesta
            this.cookiesManager.addCookiesFromResponse(response);
        }
    }

    /** Método público para realizar una petición GET a un endpoint específico
    * Recibe como parámetro la ruta del endpoint a consultar
    * Retorna un objeto Response con la respuesta completa del servidor */
    public Response get(String endpoint) {
        // Inicia la construcción de la petición con given()
        RequestSpecification requestSpec = given();

        // Aplica todas las cookies almacenadas a la petición antes de enviarla
        requestSpec = applyStoredCookies(requestSpec);

        // Continúa la construcción de la petición registrando todos los detalles
        Response response = requestSpec
                .log().all() // Registra la petición completa en consola
                .contentType(ContentType.JSON) // Especifica que se espera un JSON como respuesta
                .when() // Indica la sección de ejecución
                .get(endpoint) // Realiza GET al endpoint especificado
                .then() // Inicia la sección de validación
                .log().all() // Registra la respuesta completa en consola
                .extract().response(); // Extrae y retorna la respuesta como objeto Response

        // Almacena cualquier cookie que haya venido en la respuesta
        storeCookiesFromResponse(response);

        // Retorna la respuesta al método que hizo la llamada
        return response;
    }

    /** Método público para realizar una petición GET con validación de código de estado
     * Recibe como parámetro el endpoint a consultar y el código de estado esperado
     * Retorna un objeto Response con la respuesta completa del servidor */
    public Response getWithStatusCode(String endpoint, int statusCode) {
        // Inicia la construcción de la petición con given()
        RequestSpecification requestSpec = given();

        // Aplica todas las cookies almacenadas a la petición
        requestSpec = applyStoredCookies(requestSpec);

        // Continúa la construcción realizando la petición GET
        Response response = requestSpec
                .log().all() // Registra la petición en consola
                .when() // Indica la sección de ejecución
                .get(endpoint) // Realiza GET al endpoint
                .then() // Inicia la sección de validación
                .log().all() // Registra la respuesta
                .statusCode(statusCode) // Valida que el código de estado sea el esperado
                .extract().response(); // Extrae la respuesta

        // Almacena cualquier cookie que haya venido en la respuesta
        storeCookiesFromResponse(response);

        // Retorna la respuesta al método que hizo la llamada
        return response;
    }

    /** Método público para realizar una petición POST con un cuerpo JSON y validación de estado
    * Recibe como parámetro el endpoint, el cuerpo JSON y el código de estado esperado
    * Retorna un objeto Response con la respuesta completa del servidor */
    public Response postWithStatusCode(String endpoint, Object body, int expectedStatus) {
        // Inicia la construcción de la petición con given()
        RequestSpecification requestSpec = given();

        // Aplica todas las cookies almacenadas a la petición antes de enviarla
        requestSpec = applyStoredCookies(requestSpec);

        // Continúa la construcción de la petición agregando el cuerpo y registrando detalles
        Response response = requestSpec
                .log().all() // Registra la petición en consola
                .contentType(ContentType.JSON) // Especifica que es un JSON
                .body(body) // Asigna el cuerpo JSON a la petición
                .when() // Indica la sección de ejecución
                .post(endpoint) // Realiza POST al endpoint
                .then() // Inicia validación
                .log().all() // Registra respuesta
                .statusCode(expectedStatus) // Valida código de estado esperado
                .extract().response(); // Extrae y retorna la respuesta

        // Almacena cualquier cookie que haya venido en la respuesta
        storeCookiesFromResponse(response);

        // Retorna la respuesta al método que hizo la llamada
        return response;
    }

    /** Método público para realizar una petición POST simple con un cuerpo JSON
    * Recibe como parámetro el endpoint y el cuerpo JSON a enviar
    * Retorna un objeto Response con la respuesta completa del servidor */
    public Response post(String endpoint, Object body) {
        // Inicia la construcción de la petición con given()
        RequestSpecification requestSpec = given();

        // Aplica todas las cookies almacenadas a la petición
        requestSpec = applyStoredCookies(requestSpec);

        // Continúa la construcción de la petición
        Response response = requestSpec
                .log().all() // Registra la petición en consola
                .contentType(ContentType.JSON) // Especifica que es un JSON
                .body(body) // Asigna el cuerpo JSON
                .when() // Indica la sección de ejecución
                .post(endpoint) // Realiza POST al endpoint
                .then() // Inicia validación
                .log().all() // Registra respuesta
                .extract().response(); // Extrae y retorna la respuesta

        // Almacena cualquier cookie que haya venido en la respuesta
        storeCookiesFromResponse(response);

        // Retorna la respuesta al método que hizo la llamada
        return response;
    }

    /** Método público para realizar una petición PUT que actualiza un recurso completo
    * Recibe como parámetro el endpoint y el cuerpo JSON del nuevo recurso
    * Retorna un objeto Response con la respuesta completa del servidor */
    public Response put(String endpoint, Object body) {
        // Inicia la construcción de la petición con given()
        RequestSpecification requestSpec = given();

        // Aplica todas las cookies almacenadas a la petición
        requestSpec = applyStoredCookies(requestSpec);

        // Continúa la construcción de la petición
        Response response = requestSpec
                .log().all() // Registra petición
                .contentType(ContentType.JSON) // Especifica contenido en formato JSON
                .body(body) // Asigna cuerpo
                .when() // Indica ejecución
                .put(endpoint) // Realiza PUT
                .then() // Inicia validación
                .log().all() // Registra respuesta
                .extract().response(); // Extrae respuesta

        // Almacena cualquier cookie que haya venido en la respuesta
        storeCookiesFromResponse(response);

        // Retorna la respuesta al método que hizo la llamada
        return response;
    }

    /** Método público para realizar una petición PATCH que actualiza un recurso parcialmente
    * Recibe como parámetro el endpoint y el cuerpo JSON con los cambios parciales
    * Retorna un objeto Response con la respuesta completa del servidor */
    public Response patch(String endpoint, Object body) {
        // Inicia la construcción de la petición con given()
        RequestSpecification requestSpec = given();

        // Aplica todas las cookies almacenadas a la petición
        requestSpec = applyStoredCookies(requestSpec);

        // Continúa la construcción de la petición
        Response response = requestSpec
                .log().all() // Registra petición
                .contentType(ContentType.JSON) // Especifica el JSON
                .body(body) // Asigna cuerpo
                .when() // Indica ejecución
                .patch(endpoint) // Realiza PATCH que es actualización parcial de un registro
                .then() // Inicia validación
                .log().all() // Registra respuesta
                .extract().response(); // Extrae respuesta

        // Almacena cualquier cookie que haya venido en la respuesta
        storeCookiesFromResponse(response);

        // Retorna la respuesta al método que hizo la llamada
        return response;
    }

    /** Método público para realizar una petición DELETE que elimina un recurso
    * Recibe como parámetro el endpoint del recurso a eliminar
    * Retorna un objeto Response con la respuesta completa del servidor */
    public Response delete(String endpoint) {
        // Inicia la construcción de la petición con given()
        RequestSpecification requestSpec = given();

        // Aplica todas las cookies almacenadas a la petición
        requestSpec = applyStoredCookies(requestSpec);

        // Continúa la construcción de la petición
        Response response = requestSpec
                .log().all() // Registra petición
                .when() // Indica ejecución
                .delete(endpoint) // Realiza DELETE
                .then() // Inicia validación
                .log().all() // Registra respuesta
                .extract().response(); // Extrae respuesta

        // Almacena cualquier cookie que haya venido en la respuesta
        storeCookiesFromResponse(response);

        // Retorna la respuesta al método que hizo la llamada
        return response;
    }

    /** Método público para obtener la instancia del gestor de cookies de este cliente
    * Retorna el CookieManager asociado a esta instancia de ApiClient */
    public CookiesManager getCookieManager() {
        // Retorna el gestor de cookies de esta instancia
        return this.cookiesManager;
    }
}