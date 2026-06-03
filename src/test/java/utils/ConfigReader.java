package utils; // paquete para las utilidades de configuración

import java.io.FileInputStream; // importa para leer archivos desde el disco
import java.io.IOException; // importa excepción para errores de I/O
import java.util.Properties; // importa la clase Properties para manejar archivos .properties

// Clase utilidad para leer configuraciones desde archivo config.properties
public class ConfigReader { // declara la clase pública ConfigReader

    // declara una variable estática de Properties que será compartida por todas las instancias
    private static Properties properties; // variable para almacenar las propiedades leídas

    static { // bloque estático que se ejecuta una sola vez al cargar la clase
        try { // inicia bloque de manejo de excepciones
            FileInputStream fileInputStream = new FileInputStream("src/test/resources/config.properties"); // abre el archivo config.properties
                    properties = new Properties(); // crea una nueva instancia de Properties
            properties.load(fileInputStream); // carga las propiedades desde el archivo abierto
            fileInputStream.close(); // cierra el stream del archivo
        } catch (IOException e) { // captura cualquier error de lectura del archivo
            System.err.println("Error al cargar config.properties: " + e.getMessage()); // imprime el error en consola
                    e.printStackTrace(); // imprime la pila de excepciones para debugging
        } // fin del bloque catch
    }
    public static String getProperty(String key) { // recibe el nombre de la propiedad a obtener
        return properties.getProperty(key); // retorna el valor de la propiedad, o null si no existe
    }

    public static String getProperty(String key, String defaultValue) { // recibe clave y valor por defecto
        return properties.getProperty(key, defaultValue); // retorna el valor de la propiedad o el valor por defecto si no existe
    }

}