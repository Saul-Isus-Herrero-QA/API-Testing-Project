package tests;

import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.HeaderManager;
import java.util.Map;
import static org.testng.Assert.*;

// Test unitario que prueba la clase HeaderManager.
public class HeaderManagerTest {

    // Variable miembro para almacenar la instancia de HeaderManager a probar.
    private HeaderManager headerManager;

    @BeforeMethod
    public void setUp() {
        // Inicializa una nueva instancia de HeaderManager para cada test.
        headerManager = new HeaderManager();
    }

    @Test
    @Story("Verificar inicialización de headers por defecto")
    @Description("Verifica que los headers por defecto se inicialicen correctamente con Content-Type, User-Agent y Accept")
    @Severity(SeverityLevel.NORMAL)
    public void testDefaultHeadersInitialization() {
        Allure.step("Validar que el Content-Type sea 'application/json'");
        assertEquals(headerManager.getHeader("Content-Type"), "application/json");

        Allure.step("Validar que el User-Agent sea 'API-Testing-Framework/1.0'");
        assertEquals(headerManager.getHeader("User-Agent"), "API-Testing-Framework/1.0");

        Allure.step("Validar que Accept sea 'application/json'");
        assertEquals(headerManager.getHeader("Accept"), "application/json");

        Allure.step("Validar que el total de headers sea 3 (3 por defecto)");
        assertEquals(headerManager.getHeaderCount(), 3);
    }

    // TEST 2: Verifica que se pueda agregar un nuevo header
    @Test
    public void testAddHeader() {
        // Agrega un header personalizado con clave y valor.
        headerManager.addHeader("X-Custom-Header", "CustomValue");
        // Valida que el header se agregó correctamente.
        assertEquals(headerManager.getHeader("X-Custom-Header"), "CustomValue");
        // Valida que ahora hay 4 headers (3 por defecto + 1 nuevo).
        assertEquals(headerManager.getHeaderCount(), 4);
    }

    // TEST 3: Verifica que se puedan agregar múltiples headers a la vez
    @Test
    public void testAddMultipleHeaders() {
        // Agrega primer header personalizado.
        headerManager.addHeader("X-API-Key", "12345");
        // Agrega segundo header personalizado.
        headerManager.addHeader("X-Request-ID", "req-001");
        // Valida que el primer header se agregó.
        assertEquals(headerManager.getHeader("X-API-Key"), "12345");
        // Valida que el segundo header se agregó.
        assertEquals(headerManager.getHeader("X-Request-ID"), "req-001");
        // Valida que ahora hay 5 headers (3 por defecto + 2 nuevos).
        assertEquals(headerManager.getHeaderCount(), 5);
    }

    // TEST 4: Verifica que lance excepción si la clave es null.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddHeaderWithNullKey() {
        // Intenta agregar un header con clave null.
        // Lanza una IllegalArgumentException automáticamente.
        headerManager.addHeader(null, "value");
    }

    // TEST 5: Verifica que lance excepción si la clave es vacía.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddHeaderWithEmptyKey() {
        // Intenta agregar un header con clave vacía ("").
        // Lanza IllegalArgumentException automáticamente.
        headerManager.addHeader("", "value");
    }

    // TEST 6: Verifica que lance excepción si la clave contiene solo espacios.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddHeaderWithBlankKey() {
        // Intentar agregar un header con clave de solo espacios ("   ").
        // Lanza IllegalArgumentException automáticamente.
        headerManager.addHeader("   ", "value");
    }

    // TEST 7: Verifica que sobrescribir un header existente funciona.
    @Test
    public void testAddHeaderOverwrite() {
        // Sobrescribe el header Content-Type con un nuevo valor.
        headerManager.addHeader("Content-Type", "text/xml");
        // Valida que el valor fue actualizado
        assertEquals(headerManager.getHeader("Content-Type"), "text/xml");
    }

    // TEST 8: Verifica que se puede obtener el valor de un header específico.
    @Test
    public void testGetHeader() {
        // Obtiene el valor del header Content-Type.
        String value = headerManager.getHeader("Content-Type");
        // Valida que sea igual a "application/json"
        assertEquals(value, "application/json");
    }

    // TEST 9: Verifica que obtiene un header inexistente retorna null.
    @Test
    public void testGetNonExistentHeader() {
        // Intenta obtener un header que no existe.
        String value = headerManager.getHeader("X-Non-Existent");
        // Valida que retorna null.
        assertNull(value);
    }

    // TEST 10: Verifica que se obtienen todos los headers en un Map.
    @Test
    public void testGetAllHeaders() {
        // Agrega un header personalizado.
        headerManager.addHeader("X-Custom", "value");
        // Obtiene todos los headers en un Map
        Map<String, String> allHeaders = headerManager.getAllHeaders();
        // Valida que hay 4 headers en total (3 por defecto + 1 nuevo).
        assertEquals(allHeaders.size(), 4);
        // Valida que el Map contiene el header Content-Type.
        assertTrue(allHeaders.containsKey("Content-Type"));
        // Valida que contiene User-Agent.
        assertTrue(allHeaders.containsKey("User-Agent"));
        // Valida que contiene Accept.
        assertTrue(allHeaders.containsKey("Accept"));
        // Valida que contiene el header personalizado.
        assertTrue(allHeaders.containsKey("X-Custom"));
    }

    // TEST 11: Verifica que getAllHeaders() retorna una copia, no la referencia original.
    @Test
    public void testGetAllHeadersIsACopy() {
        // Obtiene todos los headers.
        Map<String, String> allHeaders = headerManager.getAllHeaders();
        // Intenta agregar un header al Map retornado.
        allHeaders.put("X-Temp", "temp");
        // Valida que el header NO se agregó a la instancia real.
        assertFalse(headerManager.headerExists("X-Temp"));
    }

    // TEST 12: Verifica que se puede eliminar un header específico.
    @Test
    public void testRemoveHeader() {
        // Agrega un header personalizado.
        headerManager.addHeader("X-Custom", "value");
        // Valida que ahora hay 4 headers.
        assertEquals(headerManager.getHeaderCount(), 4);
        // Elimina el header personalizado.
        headerManager.removeHeader("X-Custom");
        // Valida que el header fue eliminado (retorna null).
        assertNull(headerManager.getHeader("X-Custom"));
        // Valida que ahora hay 3 headers nuevamente.
        assertEquals(headerManager.getHeaderCount(), 3);
    }

    // TEST 13: Verifica que eliminar un header inexistente no causa error.
    @Test
    public void testRemoveNonExistentHeader() {
        // Intenta eliminar un header que no existe.
        headerManager.removeHeader("X-Non-Existent");
        // Valida que el contador sigue siendo 3 (sin cambios).
        assertEquals(headerManager.getHeaderCount(), 3);
    }

    // TEST 14: Verifica que se puede verificar si un header existe.
    @Test
    public void testHeaderExists() {
        // Valida que Content-Type existe.
        assertTrue(headerManager.headerExists("Content-Type"));
        // Valida que User-Agent existe.
        assertTrue(headerManager.headerExists("User-Agent"));
        // Valida que Accept existe.
        assertTrue(headerManager.headerExists("Accept"));
    }

    // TEST 15: Verifica que se retorna false si un header no existe
    @Test
    public void testHeaderNotExists() {
        // Valida que un header inexistente retorna false.
        assertFalse(headerManager.headerExists("X-Non-Existent"));
    }

    // TEST 16: Verifica que se pueden limpiar todos los headers
    @Test
    public void testClearAllHeaders() {
        // Agrega un header personalizado.
        headerManager.addHeader("X-Custom", "value");
        // Valida que ahora hay 4 headers.
        assertEquals(headerManager.getHeaderCount(), 4);
        // Limpia todos los headers.
        headerManager.clearAllHeaders();
        // Valida que no hay headers.
        assertEquals(headerManager.getHeaderCount(), 0);
        // Valida que los headers por defecto fueron eliminados.
        assertNull(headerManager.getHeader("Content-Type"));
    }

    // TEST 17: Verifica que se pueden reiniciar los headers a sus valores por defecto
    @Test
    public void testResetToDefaultHeaders() {
        // Agrega un header personalizado.
        headerManager.addHeader("X-Custom", "value");
        // Elimina el header User-Agent
        headerManager.removeHeader("User-Agent");
        // Limpia todos los headers.
        headerManager.clearAllHeaders();
        // Valida que no hay headers.
        assertEquals(headerManager.getHeaderCount(), 0);
        // Reinicializa los headers por defecto.
        headerManager.resetToDefaultHeaders();
        // Valida que volvemos a tener 3 headers.
        assertEquals(headerManager.getHeaderCount(), 3);
        // Valida que Content-Type tiene su valor por defecto.
        assertEquals(headerManager.getHeader("Content-Type"), "application/json");
        // Valida que User-Agent tiene su valor por defecto.
        assertEquals(headerManager.getHeader("User-Agent"), "API-Testing-Framework/1.0");
        // Valida que Accept tiene su valor por defecto.
        assertEquals(headerManager.getHeader("Accept"), "application/json");
    }

    // TEST 18: Verifica que la función toString() retorna una representación válida,
    @Test
    public void testToString() {
        // Obtiene la representación en string del objeto,
        String result = headerManager.toString();
        // Valida que contiene "HeaderManager",
        assertTrue(result.contains("HeaderManager"));
        // Valida que contiene la palabra "headers".
        assertTrue(result.contains("headers"));
    }

    // TEST 19: Verifica que se retorna el contador correcto de headers.
    @Test
    public void testGetHeaderCount() {
        // Valida que inicialmente hay 3 headers.
        assertEquals(headerManager.getHeaderCount(), 3);
        // Agrega un header personalizado.
        headerManager.addHeader("X-Header-1", "value1");
        // Validar que ahora hay 4 headers.
        assertEquals(headerManager.getHeaderCount(), 4);
        // Agregar otro header personalizado.
        headerManager.addHeader("X-Header-2", "value2");
        // Validar que ahora hay 5 headers.
        assertEquals(headerManager.getHeaderCount(), 5);
    }

    // TEST 20: Verifica que se puede agregar autenticación con token Bearer.
    @Test
    public void testSetAuthenticationHeader() {
        // Establece un header de autenticación con token Bearer (al portador).
        headerManager.setAuthenticationHeader("mytoken123");
        // Validar que el header Authorization tiene el formato correcto.
        assertEquals(headerManager.getHeader("Authorization"), "Bearer mytoken123");
    }

    // TEST 21: Verifica que lance excepción si el token es null.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetAuthenticationHeaderWithNullToken() {
        // Intenta establecer token null
        // Debería lanzar IllegalArgumentException automática.
        headerManager.setAuthenticationHeader(null);
    }

    // TEST 22: Verifica que lance excepción si el token es vacío.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetAuthenticationHeaderWithEmptyToken() {
        // Intenta establecer token vacío.
        // Debería lanzar IllegalArgumentException automáticamente.
        headerManager.setAuthenticationHeader("");
    }

    // TEST 23: Verifica que lance excepción si el token contiene solo espacios.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetAuthenticationHeaderWithBlankToken() {
        // Intenta establecer token con solo espacios.
        // Debería lanzar IllegalArgumentException automáticamente.
        headerManager.setAuthenticationHeader("   ");
    }

    // TEST 24: Verifica que se puede agregar autenticación básica (Basic Auth).
    @Test
    public void testSetBasicAuthHeader() {
        // Establece autenticación básica con usuario y contraseña.
        headerManager.setBasicAuthHeader("user", "password");
        // Obtiene el header de Authorization.
        String authHeader = headerManager.getHeader("Authorization");
        // Valida que empieza con "Basic "
        assertTrue(authHeader.startsWith("Basic "));
        // Valida que las credenciales están codificadas en Base64 correctamente
        assertEquals(authHeader, "Basic " + java.util.Base64.getEncoder().encodeToString("user:password".getBytes()));
    }

    // TEST 25: Verifica que se puede establecer autenticación básica con diferentes credenciales
    @Test
    public void testSetBasicAuthHeaderWithDifferentCredentials() {
        // Establecer autenticación básica con usuario admin y contraseña passwordseguro123
        headerManager.setBasicAuthHeader("admin", "passwordseguro123");
        // Obtener el header Authorization
        String authHeader = headerManager.getHeader("Authorization");
        // Validar que el encoding en Base64 es correcto.
        assertEquals(authHeader, "Basic " + java.util.Base64.getEncoder().encodeToString("admin:passwordseguro123".getBytes()));
    }

    // TEST 26: Verifica que lance excepción si el usuario es null.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetBasicAuthHeaderWithNullUsername() {
        // Intentar establecer autenticación básica con usuario null.
        // Debe lanzar IllegalArgumentException automáticamente.
        headerManager.setBasicAuthHeader(null, "password");
    }

    // TEST 27: Verifica que lance excepción si el usuario es vacío
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetBasicAuthHeaderWithEmptyUsername() {
        // Intentar establecer autenticación básica con usuario vacío.
        // Debe lanzar IllegalArgumentException automáticamente.
        headerManager.setBasicAuthHeader("", "password");
    }

    // TEST 28: Verifica que lance excepción si el usuario contiene solo espacios.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetBasicAuthHeaderWithBlankUsername() {
        // Intentar establecer autenticación básica con usuario de solo espacios.
        // Debe lanzar IllegalArgumentException automáticamente.
        headerManager.setBasicAuthHeader("   ", "password");
    }

    // TEST 29: Verifica que lance excepción si la contraseña es null.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetBasicAuthHeaderWithNullPassword() {
        // Intentar establecer autenticación básica con contraseña null.
        // Debe lanzar IllegalArgumentException automáticamente.
        headerManager.setBasicAuthHeader("user", null);
    }

    // TEST 30: Verifica que lance excepción si la contraseña es vacía.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetBasicAuthHeaderWithEmptyPassword() {
        // Intentar establecer autenticación básica con contraseña vacía.
        // Debe lanzar IllegalArgumentException automáticamente.
        headerManager.setBasicAuthHeader("user", "");
    }

    // TEST 31: Verifica que lance excepción si la contraseña contiene solo espacios.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetBasicAuthHeaderWithBlankPassword() {
        // Intentar establecer autenticación básica con contraseña de solo espacios.
        // Debe lanzar IllegalArgumentException automáticamente.
        headerManager.setBasicAuthHeader("user", "   ");
    }

    // TEST 32: Verifica que se puede cambiar el Content-Type.
    @Test
    public void testSetContentType() {
        // Cambiar el Content-Type a "text/xml".
        headerManager.setContentType("text/xml");
        // Validar que el cambio fue aplicado.
        assertEquals(headerManager.getContentType(), "text/xml");
    }

    // TEST 33: Verifica que se puede cambiar el Content-Type múltiples veces.
    @Test
    public void testSetContentTypeMultipleTimes() {
        // Cambiar Content-Type a "text/xml".
        headerManager.setContentType("text/xml");
        // Validar que el cambio fue aplicado.
        assertEquals(headerManager.getContentType(), "text/xml");
        // Cambiar Content-Type a otro tipo MIME.
        headerManager.setContentType("application/x-www-form-urlencoded");
        // Validar que el nuevo cambio fue aplicado.
        assertEquals(headerManager.getContentType(), "application/x-www-form-urlencoded");
    }

    // TEST 34: Verifica que lance excepción si Content-Type es null.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetContentTypeWithNull() {
        // Intentar establecer Content-Type a null.
        // Debe lanzar IllegalArgumentException automáticamente.
        headerManager.setContentType(null);
    }

    // TEST 35: Verifica que lance excepción si Content-Type es vacío.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetContentTypeWithEmpty() {
        // Intentar establecer Content-Type vacío.
        // Debe lanzar IllegalArgumentException automáticamente.
        headerManager.setContentType("");
    }

    // TEST 36: Verifica que lance excepción si Content-Type contiene solo espacios.
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetContentTypeWithBlank() {
        // Intentar establecer Content-Type con solo espacios.
        // Debe lanzar IllegalArgumentException automáticamente.
        headerManager.setContentType("   ");
    }

    // TEST 37: Verifica que se puede obtener el Content-Type actual.
    @Test
    public void testGetContentType() {
        // Obtener el Content-Type actual.
        String contentType = headerManager.getContentType();
        // Validar que es el valor por defecto.
        assertEquals(contentType, "application/json");
    }

    // TEST 38: Verifica que se obtiene el Content-Type después de cambiar
    @Test
    public void testGetContentTypeAfterChange() {
        // Cambiar el Content-Type a "text/plain"
        headerManager.setContentType("text/plain");
        // Validar que retorna el nuevo valor
        assertEquals(headerManager.getContentType(), "text/plain");
    }

}
