
package com.mycompany.arquiproyecto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class integrante1 {
    
    //----------------------------Entrada para la lectura de datos------------------------------
    //funcion para la lectura de datos entrantes 
    public static String LecturaEnTeclado(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el valor");
        return sc.nextLine();
    }
    //funcion para la lectura de todo tipo de datos 
    public static byte[] readFile(String ruta) throws IOException {
        return Files.readAllBytes(Paths.get(ruta));
    }
    
    //----------------------------Conversion a Binarios-----------------------------------------
    //Lo vuelve de bytes a ASCII y asi a binarios 
    public static int[] textoABinario(String texto) {
        StringBuilder bin = new StringBuilder();
        for (char c : texto.toCharArray()) {
            String b = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            bin.append(b);
        }
    
    //
        int[] bits = new int[bin.length()];
        for (int i = 0; i < bin.length(); i++) {
            bits[i] = bin.charAt(i) - '0';
        }
        return bits;
    }

    public static int[] bytesABinario(byte[] datos) {
        StringBuilder bin = new StringBuilder();
        for (byte b : datos) {
            String s = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            bin.append(s);
        }

        int[] bits = new int[bin.length()];
        for (int i = 0; i < bin.length(); i++) {
            bits[i] = bin.charAt(i) - '0';
        }
        return bits;
    }
    
    //---------------------------Division de Bloques----------------------------------------------
    public static List<int[]> bloques4Bits(int[] bits) {
        List<int[]> bloques = new ArrayList<>();

        for (int i = 0; i < bits.length; i += 4) {
            int[] bloque = new int[4];
            for (int j = 0; j < 4; j++) {
                if (i + j < bits.length)
                    bloque[j] = bits[i + j];
                else
                    bloque[j] = 0; // padding
            }
            bloques.add(bloque);
        }
        return bloques;
    }

    public static List<int[]> bloques7Bits(int[] bits) {
        List<int[]> bloques = new ArrayList<>();

        for (int i = 0; i < bits.length; i += 7) {
            int[] bloque = new int[7];
            for (int j = 0; j < 7; j++) {
                if (i + j < bits.length)
                    bloque[j] = bits[i + j];
                else
                    bloque[j] = 0;
            }
            bloques.add(bloque);
        }
        return bloques;
    }
}


   


