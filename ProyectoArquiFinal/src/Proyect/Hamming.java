package Proyect;
import java.util.ArrayList;
import java.util.List; 
public class Hamming {

    /**
     * Método principal solicitado.
     * Procesa una lista de bloques de datos y devuelve bloques codificados.
     */
    public List<int[]> encode(List<int[]> BloquesDeDatos) {
        List<int[]> Bloquescodificados = new ArrayList<>();

        for (int[] BloquedeDato : BloquesDeDatos) {
            try {
                int[] Bloquecodificado = InsertarDatos(BloquedeDato);
                Bloquescodificados.add(Bloquecodificado);
            } catch (IllegalArgumentException e) {
                System.err.println("Error procesando bloque: " + e.getMessage());
            }
        }

        return Bloquescodificados;
    }

    /**
     * Prepara un bloque individual insertando datos y calculando paridad.
     */
    private int[] InsertarDatos(int[] data) {
        int m = data.length;
        int r = 0; // Número de bits de paridad
        int n = 0; // Longitud total

        // Determinamos si es Hamming(7,4) o Hamming(11,7)
        if (m == 4) {
            r = 3;
            n = 7;
        } else if (m == 7) {
            r = 4;
            n = 11;
        } else {
            throw new IllegalArgumentException("Longitud de datos no soportada. Use 4 bits (7,4) o 7 bits (11,7).");
        }

        int[] Bloquecodificado = new int[n];
        int dataIndex = 0;

        // Inserción de bits de datos:
        // Recorremos las posiciones de 1 a n. Si NO es potencia de 2, ponemos un bit de datos.
        for (int i = 1; i <= n; i++) {
            // Chequeamos si i es potencia de 2 (ej: 1, 2, 4, 8)
            if ((i & (i - 1)) == 0) {
                // Es posición de paridad, temporalmente ponemos 0
                Bloquecodificado[i - 1] = 0;
            } else {
                // Es posición de datos
                Bloquecodificado[i - 1] = data[dataIndex];
                dataIndex++;
            }
        }

        // Llamamos al calculador de paridad para llenar los huecos
        return Paridad.calcularParidad(Bloquecodificado);
    }
}
