package utils;

import io.restassured.response.Response;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.time.LocalDateTime;

// Clase que gestiona todas las operaciones relacionadas con cookies en las pruebas de API
public class CookiesManager {

    // Mapa privado que almacena todas las cookies como pares clave-valor (nombre de cookie -> valor de cookie)
    private Map<String, String> cookies;
    // Mapa privado que almacena la fecha de expiración de cada cookie (nombre de cookie -> fecha de expiración)
    private Map<String, LocalDateTime> cookieExpirations;

    // Constructor de la clase CookiesManager que inicializa los mapas de cookies
    public CookiesManager() {
        this.cookies = new LinkedHashMap<>();
        this.cookieExpirations = new HashMap<>();
    }

    // Método público para agregar una cookie manualmente al almacén de cookies
    // Recibe como parámetro el nombre de la cookie y su valor
    public void addCookie(String cookieName, String cookieValue) {
        // Valida que el nombre de la cookie no sea nulo ni vacío
        if (cookieName == null || cookieName.trim().isEmpty()) {
            // Lanza una excepción si el nombre de la cookie no es válido
            throw new IllegalArgumentException("El nombre de la cookie no puede ser nulo o vacío");
        }
        // Valida que el valor de la cookie no sea nulo ni vacío
        if (cookieValue == null || cookieValue.trim().isEmpty()) {
            // Lanza una excepción si el valor de la cookie no es válido
            throw new IllegalArgumentException("El valor de la cookie no puede ser nulo o vacío");
        }
        // Agrega o actualiza la cookie en el mapa de cookies
        this.cookies.put(cookieName, cookieValue);
    }

    // Método público para extraer y almacenar todas las cookies de una respuesta HTTP
    // Recibe como parámetro un objeto Response de RestAssured que contiene las cookies de la respuesta
    public void addCookiesFromResponse(Response response) {
        // Valida que la respuesta no sea nula
        if (response == null) {
            // Lanza una excepción si la respuesta es nula
            throw new IllegalArgumentException("La respuesta no puede ser nula");
        }

        // Extrae todas las cookies de la respuesta HTTP usando el método cookies() de RestAssured
        Map<String, String> responseCookies = response.getCookies();

        // Verifica que la respuesta contenga cookies
        if (responseCookies != null && !responseCookies.isEmpty()) {
            // Itera sobre cada cookie extraída de la respuesta
            for (Map.Entry<String, String> entry : responseCookies.entrySet()) {
                // Obtiene el nombre de la cookie del par clave-valor
                String cookieName = entry.getKey();
                // Obtiene el valor de la cookie del par clave-valor
                String cookieValue = entry.getValue();
                // Agrega la cookie al almacén de cookies del administrador
                this.cookies.put(cookieName, cookieValue);
            }
        }
    }

    // Método público para obtener el valor de una cookie específica
    // Recibe como parámetro el nombre de la cookie que se desea obtener
    public String getCookie(String cookieName) {
        // Valida que el nombre de la cookie no sea nulo ni vacío
        if (cookieName == null || cookieName.trim().isEmpty()) {
            // Lanza una excepción si el nombre de la cookie no es válido
            throw new IllegalArgumentException("El nombre de la cookie no puede ser nulo o vacío");
        }

        // Retorna el valor de la cookie si existe, o null si no existe
        return this.cookies.get(cookieName);
    }

    // Método público para obtener todas las cookies almacenadas
    // Retorna un Map con todos los pares clave-valor de cookies
    public Map<String, String> getAllCookies() {
        // Retorna una copia del mapa de cookies para evitar modificaciones externas
        return new LinkedHashMap<>(this.cookies);
    }

    // Método público para eliminar una cookie específica del almacén
    // Recibe como parámetro el nombre de la cookie que se desea eliminar
    public void removeCookie(String cookieName) {
        // Valida que el nombre de la cookie no sea nulo ni vacío
        if (cookieName == null || cookieName.trim().isEmpty()) {
            // Lanza una excepción si el nombre de la cookie no es válido
            throw new IllegalArgumentException("El nombre de la cookie no puede ser nulo o vacío");
        }
        // Elimina la cookie del mapa de cookies usando su nombre
        this.cookies.remove(cookieName);
        // También elimina la fecha de expiración asociada a esta cookie
        this.cookieExpirations.remove(cookieName);
    }

    /**
     * Método público para verificar si una cookie específica existe en el almacén
     * Recibe como parámetro el nombre de la cookie a verificar
     *
     * @param cookieName el nombre de la cookie a verificar
     * @return true si la cookie existe, false en caso contrario
     */
    public boolean hasCookie(String cookieName) {
        // Valida que el nombre de la cookie no sea nulo ni vacío
        if (cookieName == null || cookieName.trim().isEmpty()) {
            // Lanza una excepción si el nombre de la cookie no es válido
            throw new IllegalArgumentException("El nombre de la cookie no puede ser nulo o vacío");
        }
        // Retorna true si la cookie existe en el mapa, false en caso contrario
        return this.cookies.containsKey(cookieName);
    }

    // Método público para eliminar todas las cookies almacenadas en el administrador
    public void clearAllCookies() {
        // Limpia completamente el mapa de cookies eliminando todas las entradas
        this.cookies.clear();
        // Limpia completamente el mapa de fechas de expiración eliminando todas las entradas
        this.cookieExpirations.clear();
    }

    /**
     * Método público para obtener el número total de cookies almacenadas
     *
     * @return un int con la cantidad de cookies actualmente almacenadas
     */
    public int getCookieCount() {
        // Retorna el tamaño actual del mapa de cookies
        return this.cookies.size();
    }

    /**
     * Método público para establecer una fecha de expiración para una cookie específica
     * Recibe como parámetro el nombre de la cookie y la fecha de expiración
     *
     * @param cookieName     el nombre de la cookie
     * @param expirationTime la fecha de expiración
     */
    public void setCookieExpiration(String cookieName, LocalDateTime expirationTime) {
        // Valida que el nombre de la cookie no sea nulo ni vacío
        if (cookieName == null || cookieName.trim().isEmpty()) {
            // Lanza una excepción si el nombre de la cookie no es válido
            throw new IllegalArgumentException("El nombre de la cookie no puede ser nulo o vacío");
        }

        // Valida que la fecha de expiración no sea nula
        if (expirationTime == null) {
            // Lanza una excepción si la fecha de expiración es nula
            throw new IllegalArgumentException("La fecha de expiración no puede ser nula");
        }

        // Verifica si la cookie existe en el almacén
        if (!this.cookies.containsKey(cookieName)) {
            // Lanza una excepción si se intenta establecer expiración a una cookie que no existe
            throw new IllegalArgumentException("La cookie '" + cookieName + "' no existe en el almacén");
        }

        // Almacena la fecha de expiración para la cookie especificada
        this.cookieExpirations.put(cookieName, expirationTime);
    }

    /**
     * Método público para obtener la fecha de expiración de una cookie específica
     * Recibe como parámetro el nombre de la cookie
     *
     * @param cookieName el nombre de la cookie
     * @return la fecha de expiración de la cookie, o null si no tiene expiración definida
     */
    public LocalDateTime getCookieExpiration(String cookieName) {
        // Valida que el nombre de la cookie no sea nulo ni vacío
        if (cookieName == null || cookieName.trim().isEmpty()) {
            // Lanza una excepción si el nombre de la cookie no es válido
            throw new IllegalArgumentException("El nombre de la cookie no puede ser nulo o vacío");
        }

        // Retorna la fecha de expiración de la cookie, o null si no tiene expiración definida
        return this.cookieExpirations.get(cookieName);
    }

    /**
     * Método público para verificar si una cookie ha expirado comparando con la fecha actual
     * Recibe como parámetro el nombre de la cookie a verificar
     */
    public boolean isCookieExpired(String cookieName) {
        // Valida que el nombre de la cookie no sea nulo ni vacío
        if (cookieName == null || cookieName.trim().isEmpty()) {
            // Lanza una excepción si el nombre de la cookie no es válido
            throw new IllegalArgumentException("El nombre de la cookie no puede ser nulo o vacío");
        }
        // Obtiene la fecha de expiración de la cookie especificada
        LocalDateTime expirationTime = this.cookieExpirations.get(cookieName);

        // Si no hay fecha de expiración definida, la cookie no está expirada
        if (expirationTime == null) {
            // Retorna false indicando que la cookie no está expirada
            return false;
        }
        // Compara la fecha de expiración con la fecha y hora actual
        // Si la fecha de expiración es anterior a ahora, la cookie está expirada
        return expirationTime.isBefore(LocalDateTime.now());
    }

    /**
     * Método público que devuelve una representación en cadena de todas las cookies almacenadas
     * @return un String con información formateada de todas las cookies
     */
    @Override
    public String toString() {
        // Retorna una cadena formateada con el nombre de la clase y el contenido del mapa de cookies
        return "CookieManager{" +
                "cookies=" + this.cookies +
                ", expirations=" + this.cookieExpirations +
                '}';
    }

}




