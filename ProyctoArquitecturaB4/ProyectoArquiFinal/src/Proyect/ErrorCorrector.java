package Proyect;

public class ErrorCorrector {

	// Posiciones (0-indexadas) que afectan a cada bit de paridad
    private static final int[][] Paridad = {
        {0, 2, 4, 6}, // p1
        {1, 2, 5, 6}, // p2
        {3, 4, 5, 6}  // p4
    };

    public int detectError(int[] block) {
        int errorPosition = 0;

        for (int i = 0; i < Paridad.length; i++) {
            int parity = 0;
            for (int pos : Paridad[i]) {
                parity ^= block[pos];
            }
            errorPosition += parity * (1 << i);
        }

        return errorPosition;
    }

    public boolean correct(int[] block, int errorPosition) {
        if (errorPosition >= 1 && errorPosition <= block.length) {
            block[errorPosition - 1] ^= 1;
            return true;
        }
        return false;
    }
}
