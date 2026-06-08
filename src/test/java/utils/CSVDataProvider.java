package utils;

import org.testng.annotations.DataProvider;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Clase utilitaria que proporciona datos desde un archivo CSV para DataProvider de TestNG
public class CSVDataProvider {

    // DataProvider con nombre "postsCsv" que puede usarse desde tests
    @DataProvider(name = "postsCsv")
    public static Object[][] postsCsv() throws Exception {
        // Ruta relativa al proyecto del archivo CSV con casos de prueba
        Path path = Paths.get("src/test/resources/data/posts.csv");
        List<String> lines = Files.readAllLines(path); // lee todas las líneas del CSV
        List<Object[]> data = new ArrayList<>(); // lista donde acumulamos arrays para TestNG

        for (String line : lines) { // se itera cada línea
            line = line.trim(); // limpiamos espacios
            if (line.isEmpty() || line.startsWith("#")) { // ignoramos cabeceras o líneas comentadas
                continue;
            }
            // Separa por coma en tres campos: title, body, userId
            String[] parts = line.split(",", -1);
            String title = parts.length > 0 ? parts[0].trim() : "";
            String body = parts.length > 1 ? parts[1].trim() : "";
            int userId = parts.length > 2 && !parts[2].trim().isEmpty() ? Integer.parseInt(parts[2].trim()) : 1;
            data.add(new Object[]{ title, body, userId }); // añade el caso de prueba
        }

        // Convierte la lista a Object[][] requerido por TestNG
        return data.toArray(new Object[0][]);
    }
}
