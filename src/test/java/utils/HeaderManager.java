package utils;

// Map (colección clave-valor) para importar los headers de las peticiones HTTP.
import java.util.Map;
// LinkedHashMap para mantener el orden de los nuevos headers.
import java.util.LinkedHashMap;

// Clase que gestiona todos los encabezados (headers) para las peticiones HTTP.
public class HeaderManager {

    // Mapa privado que almacena los encabezados como par clave-valor.
    private Map<String, String> headers;

    // Constructor de la clase HeaderManager.
    public HeaderManager() {
        // Inicialización del map de headers con LinkedHashMap para mantener el orden.
        this.headers = new LinkedHashMap<>();

        // Llamada a la función que establece los encabezados por defecto.
        initializeDefaultHeaders();
    }

    // Función privada que inicializa los encabezados por defecto para todas las peticiones.
    private void initializeDefaultHeaders() {
        // Se establece el tipo JSON (por defecto).
        this.headers.put("Content-Type", "application/json");

        // Agente de usuario para identificar la solicitud.
        this.headers.put("User-Agent", "API-Testing-Framework/1.0");

        // Establecer que se aceptan respuestas en formato JSON.
        this.headers.put("Accept", "application/json");
    }

    // Función pública para añadir un nuevo header al map.
    // Parámetros recibidos la clave (nombre del header) y el valor.
    public void addHeader(String key, String value) {
        // Validar que la clave no sea nula ni vacía.
        if (key == null || key.trim().isEmpty()) {
            // Se lanza una excepción si la clave no es válida.
            throw new IllegalArgumentException("El nombre del encabezado no puede ser nulo o vacío");
        }

        // Añadimos o actualizamos el header en el map.
        this.headers.put(key, value);
    }

    // Función pública que obtiene el valor de un encabezado específico.
    // Recibe como parámetro la clave (nombre del header).
    public String getHeader(String key) {
        // Retorna el valor asociado a la clave, o null si no existe.
        return this.headers.get(key);
    }

    // Función pública que obtiene todos los encabezados almacenados en el map.
    // Retorna un map con todos los pares clave-valor
    public Map<String, String> getAllHeaders() {
        // Retorna una copia del map de headers.
        return new LinkedHashMap<>(this.headers);
    }

    // Función pública para eliminar un determinado header del map.
    // Recibe como parámetro la clave (nombre del header a eliminar)
    public void removeHeader(String key) {
        // Elimina el encabezado del mapa usando su clave
        this.headers.remove(key);
    }

    // Función pública que verifica si un encabezado específico existe en el map.
    // Recibe como parámetro la clave (nombre del header).
    public boolean headerExists(String key) {
        // Retornar true si el encabezado existe, false en caso contrario.
        return this.headers.containsKey(key);
    }

    // Función pública que elimina todos los encabezados almacenados en el map.
    public void clearAllHeaders() {
        // Elimina todos los encabezados del map
        this.headers.clear();
    }

    // Función pública que reinicia los encabezados a sus valores por defecto.
    public void resetToDefaultHeaders() {
        // Limpia todos los encabezados actuales.
        this.headers.clear();

        // Reinicializa los encabezados por defecto.
        initializeDefaultHeaders();
    }

    // Función pública que devuelve una representación en cadena de los encabezados almacenados.
    @Override
    public String toString() {
        // Retorna una cadena con todos los pares clave-valor.
        return "HeaderManager{" +
                "headers=" + this.headers +
                '}';
    }

    // Función pública que devuelve el número total de encabezados almacenados en el map.
    public int getHeaderCount() {
        // Retorna el tamaño del mapa de encabezados
        return this.headers.size();
    }

    // Función pública que establece un encabezado de autenticación con token Bearer (el que lo posea puede
    // usar este token, así son los tokens tipo bearer).
    // Recibe como parámetro el token de autenticación.
    public void setAuthenticationHeader(String token) {
        // Valida que el token no sea nulo ni vacío
        if (token == null || token.trim().isEmpty()) {
            // Lanza una excepción si el token no es válido
            throw new IllegalArgumentException("El token de autenticación no puede ser nulo o vacío");
        }

        // Añade el encabezado de Autorización con el token Bearer
        this.headers.put("Authorization", "Bearer " + token);
    }

    // Función pública que establece un encabezado de autenticación básica (Basic Auth) para las peticiones HTTP.
    // Recibe como parámetros el usuario y la contraseña.
    public void setBasicAuthHeader(String username, String password) {
        // Validar que el usuario no sea nulo ni vacío.
        if (username == null || username.trim().isEmpty()) {
            // Lanzar excepción si el usuario no es válido
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío");
        }

        // Validamos que la contraseña no sea nula ni vacía.
        if (password == null || password.trim().isEmpty()) {
            // Lanzar excepción si la contraseña no es válida
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía");
        }

        // Se crea la cadena de usuario:contraseña.
        String credentials = username + ":" + password;

        // Codificar en Base64 las credenciales
        String encodedCredentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());

        // Añadimos el encabezado Authorization con autenticación básica
        this.headers.put("Authorization", "Basic " + encodedCredentials);
    }

    // Función pública que establece el tipo de contenido (Content-Type) para las peticiones HTTP.
    // Recibe como parámetro el tipo MIME de contenido.
    public void setContentType(String contentType) {
        // Validar que el tipo de contenido no sea nulo ni vacío
        if (contentType == null || contentType.trim().isEmpty()) {
            // Lanzar excepción si el tipo de contenido no es válido
            throw new IllegalArgumentException("El tipo de contenido no puede ser nulo o vacío");
        }

        // Actualizar el encabezado Content-Type con el nuevo tipo
        this.headers.put("Content-Type", contentType);
    }

    // Función pública que obtiene el valor del encabezado Content-Type actual.
    public String getContentType() {
        // Retorna el valor del encabezado Content-Type
        return this.headers.get("Content-Type");
    }

}
