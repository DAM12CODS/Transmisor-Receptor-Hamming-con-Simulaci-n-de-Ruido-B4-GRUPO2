package Proyect;

import java.util.ArrayList;
import java.util.List;

public class BinaryConverter {

    private int actualBitCount = 0;  // Almacena cantidad exacta de bits
    
    // TEXTO → BLOQUES (4 bits o 7 bits)
    public List<int[]> convertTextToBlocks(String text, int blockSize) {
        int[] bits = convertTextToBinary(text);
        this.actualBitCount = bits.length;
        return splitIntoBlocks(bits, blockSize);
    }
    
    // TEXTO → BLOQUES (por defecto 4 bits)
    public List<int[]> convertTextToBlocks(String text) {
        return convertTextToBlocks(text, 4);
    }

    // BYTES → BLOQUES (4 bits o 7 bits)
    public List<int[]> convertBytesToBlocks(byte[] data, int blockSize) {
        int[] bits = convertBytesToBinary(data);
        this.actualBitCount = bits.length;
        return splitIntoBlocks(bits, blockSize);
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
            if (value >= 32 && value < 127) {
                result.append((char) value);
            }
        }
        return result.toString();
    }
    
    // BLOQUES → BYTES
    public byte[] convertBlocksToBytes(List<int[]> blocks) {
        List<Integer> bitsList = new ArrayList<>();
        
        for (int[] block : blocks) {
            for (int bit : block) {
                bitsList.add(bit);
            }
        }
        
        // Usar actualBitCount si está disponible, sino calcular
        int bitsToUse = (actualBitCount > 0) ? actualBitCount : bitsList.size();
        byte[] result = new byte[(bitsToUse + 7) / 8];
        
        for (int i = 0; i < bitsToUse; i += 8) {
            int value = 0;
            for (int j = 0; j < 8 && i + j < bitsToUse; j++) {
                if (i + j < bitsList.size()) {
                    value = (value << 1) | bitsList.get(i + j);
                } else {
                    value = value << 1;
                }
            }
            result[i / 8] = (byte) value;
        }
        return result;
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
    
    // BYTES → BINARIO
    private int[] convertBytesToBinary(byte[] data) {
        int[] bits = new int[data.length * 8];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 8; j++) {
                bits[i * 8 + j] = (data[i] >> (7 - j)) & 1;
            }
        }
        return bits;
    }

    // DIVISIÓN EN BLOQUES DE N BITS
    private List<int[]> splitIntoBlocks(int[] bits, int blockSize) {
        List<int[]> blocks = new ArrayList<>();

        for (int i = 0; i < bits.length; i += blockSize) {
            int[] block = new int[blockSize];
            for (int j = 0; j < blockSize; j++) {
                if (i + j < bits.length)
                    block[j] = bits[i + j];
                else
                    block[j] = 0; // padding
            }
            blocks.add(block);
        }
        return blocks;
    }
    
    // Obtiene la cantidad exacta de bits original
    public int getActualBitCount() {
        return actualBitCount;
    }
    
    // Establece la cantidad de bits original (para recuperación)
    public void setActualBitCount(int count) {
        this.actualBitCount = count;
    }
}
