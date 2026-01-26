package Proyect;

public class Paridad {

	    /**
	     * Calcula y rellena los bits de paridad para un bloque dado.
	     * Funciona para cualquier longitud de Hamming (7,4), (11,7), etc.
	     * * @param encodedBlock El bloque con los datos ya insertados y los huecos de paridad en 0.
	     * @return El bloque con los bits de paridad calculados.
	     */
	 public static int[] calcularParidad(int[] bloque) {
	    int n = bloque.length;

	        // Iteramos sobre las posiciones de potencia de 2 (1, 2, 4, 8...)
	        // Nota: i es la posición en base-1 (1, 2, 4...), pero el acceso al array es i-1.
	        for (int i = 1; i < n; i = i * 2) {
	            int parity = 0;

	            // Revisamos todos los bits del bloque
	            for (int j = 1; j <= n; j++) {
	                // Si la posición j tiene el bit i encendido en su binario (operación AND)
	                // significa que este bit j es cubierto por la paridad i.
	                if ((j & i) != 0) {
	                    // Omitimos la posición del propio bit de paridad que estamos calculando
	                    if (j != i) {
	                        parity = parity ^ bloque[j - 1]; // Suma XOR
	                    }
	                }
	            }
	            // Asignamos el resultado en la posición del bit de paridad (índice i-1)
	            bloque[i - 1] = parity;
	        }
	        return bloque;
	    }
	}

