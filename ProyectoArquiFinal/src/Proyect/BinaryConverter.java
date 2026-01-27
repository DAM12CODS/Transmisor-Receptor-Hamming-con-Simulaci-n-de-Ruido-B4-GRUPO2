package Proyect;

import java.util.ArrayList;
import java.util.List;

public class BinaryConverter {

    // TEXTO → BLOQUES DE 4 BITS
    public List<int[]> convertTextToBlocks(String text) {
        int[] bits = convertTextToBinary(text);
        return splitInto4BitBlocks(bits);
    }

    // BLOQUES → TEXTO
    public String convertBlocksToText(List<int[]> blocks) {
        List<Integer> bitsList = new ArrayList<>();

        for (int[] block : blocks) {
            for (int bit : block) {
                bitsList.add(bit);
            }
        }

        // Convertimos a bytes (8 bits)
        StringBuilder result = new StringBuilder();
        for (int i = 0; i + 7 < bitsList.size(); i += 8) {
            int value = 0;
            for (int j = 0; j < 8; j++) {
                value = (value << 1) | bitsList.get(i + j);
            }
            result.append((char) value);
        }
        return result.toString();
    }

    // TEXTO → BINARIO
    private int[] convertTextToBinary(String texto) {
        StringBuilder bin = new StringBuilder();

        for (char c : texto.toCharArray()) {
            String b = String.format("%8s",
                    Integer.toBinaryString(c)).replace(' ', '0');
            bin.append(b);
        }

        int[] bits = new int[bin.length()];
        for (int i = 0; i < bin.length(); i++) {
            bits[i] = bin.charAt(i) - '0';
        }
        return bits;
    }

    // DIVISIÓN EN BLOQUES DE 4 BITS (Hamming 7,4)
    private List<int[]> splitInto4BitBlocks(int[] bits) {
        List<int[]> blocks = new ArrayList<>();

        for (int i = 0; i < bits.length; i += 4) {
            int[] block = new int[4];
            for (int j = 0; j < 4; j++) {
                if (i + j < bits.length)
                    block[j] = bits[i + j];
                else
                    block[j] = 0; // padding
            }
            blocks.add(block);
        }
        return blocks;
    }
}
