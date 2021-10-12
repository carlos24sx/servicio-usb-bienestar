package co.edu.unisimon.bienestar.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import co.edu.unisimon.bienestar.configuration.exception.InconsistencyException;

/**
 * @author William Torres
 * @version 1.0
 */
public final class MapUtil {

    public static final String EXCEPTION = "La cadena de texto no puede ser convertida a un objeto de tipo 'Map'.";

    private MapUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Contruye un mapa a partir de un array de cadenas de texto
     *
     * @param data Array de cadenas de texto. Formato requerido: [key1,value1,key2,value2]
     * */
    public static Map<String, String> getMap(String... data) {
        if (data.length % 2 != 0) throw new InconsistencyException(EXCEPTION);
        Map<String, String> parameters = new HashMap<>();
        for (int i = 0; i < data.length; i += 2) {
            parameters.put(data[i], data[i + 1]);
        }
        return parameters;
    }

    /**
     * Contruye un mapa a partir de una cadena texto
     *
     * @param data Cadena de texto. Formato requerido: key1,value1,key2,value2
     * */
    public static Map<String, Object> getMap(String data) {
        return Arrays.stream(data.split(","))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));
    }


}
