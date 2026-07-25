package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.LocalDateTime;
import utils.CookiesManager;

public class CookiesManagerTest {

    // Instancia de Cookies Manager usada en cada test
    private CookiesManager cookiesManager;

    @BeforeMethod
    public void setup() {
        // Creamos una nueva instancia de Cookies Manager.
        cookiesManager = new CookiesManager();
    }

    @Test
    public void AddCookie() {

        // añade una cookie al manager.
        cookiesManager.addCookie("SESSION_ID", "ABC123");

        // Verifica que la cookie existe
        Assert.assertTrue(
                cookiesManager.hasCookie("SESSION_ID"),
                "Cookie debe existir después de ser añadida"
        );

        // Verificar el valor almacenado.
        Assert.assertEquals(
                cookiesManager.getCookie("SESSION_ID"),
                "ABC123"
        );
    }

    // Verifica que el valor de una cookie pueda ser recuperado.
    @Test
    public void recuperarValorCookie() {

        cookiesManager.addCookie("TOKEN", "XYZ456");
        // Recupera el valor de una cookie.
        String cookieValue = cookiesManager.getCookie("TOKEN");

        // Verificamos el valor de una cookie.
        Assert.assertEquals(
                cookieValue,
                "XYZ456"
        );
    }

    // Verificamos que todas las cookies se retornan correctamente
    @Test
    public void RetornarTodasLasCookies() {

        // Añade cookie
        cookiesManager.addCookie("COOKIE_1", "VALUE_1");

        // Añade segunda cookie
        cookiesManager.addCookie("COOKIE_2", "VALUE_2");

        // Verifica el contador de cookies
        Assert.assertEquals(
                cookiesManager.getAllCookies().size(),
                2
        );
    }

    // Verifica que una cookie puede ser eliminada.
    @Test
    public void eliminarCookie() {

        // Añade cookie
        cookiesManager.addCookie("TEMP_COOKIE", "TEMP_VALUE");

        // Eliminar cookie
        cookiesManager.removeCookie("TEMP_COOKIE");

        // Verifica que la cookie ya no existe
        Assert.assertFalse(
                cookiesManager.hasCookie("TEMP_COOKIE"),
                "La cookie no debería existir después de ser eliminada"
        );
    }

    // Verificar que el manejo de la expiración de una cookie funciona correctamente.
    @Test
    public void detectarCookieExpirada() {

        // Añade cookie
        cookiesManager.addCookie("AUTH_COOKIE", "AUTH_VALUE");

        // Setea su tiempo de expiración pasado
        cookiesManager.setCookieExpiration(
                "AUTH_COOKIE",
                LocalDateTime.now().minusMinutes(5)
        );

        // Verifica que la cookie se reporta cómo expirada
        Assert.assertTrue(
                cookiesManager.isCookieExpired("AUTH_COOKIE"),
                "La cookie debe ser marcada como expirada"
        );
    }
}
