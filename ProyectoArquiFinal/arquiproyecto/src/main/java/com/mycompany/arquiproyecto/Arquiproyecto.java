package com.mycompany.arquiproyecto;

import java.util.List;

public class Arquiproyecto {

    public static void main(String[] args) {

        // 1. Leer texto
        String text = integrante1.LecturaEnTeclado();

        // 2. Convertir a binario
        int[] bits = integrante1.textoABinario(text);

        // 3. Dividir en bloques de 4 bits (Hamming 7,4)
        List<int[]> dataBlocks = integrante1.bloques4Bits(bits);

        // 4. Mostrar bloques (solo para verificar)
        System.out.println("Bloques listos para Hamming:");
        for (int[] bloque : dataBlocks) {
            for (int b : bloque) {
                System.out.print(b);
            }
            System.out.println();
        }
    }
}

