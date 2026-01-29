package Proyect;

import java.util.List;

/**
 * Clase de pruebas automatizadas para el sistema Hamming
 * Ejecuta diferentes escenarios de prueba
 */
public class HammingTest {
    
    static class TestResult {
        String testName;
        boolean passed;
        String message;
        
        TestResult(String name, boolean passed, String message) {
            this.testName = name;
            this.passed = passed;
            this.message = message;
        }
        
        void print() {
            String status = passed ? "PASÓ" : "FALLÓ";
            System.out.println("[" + status + "] " + testName + ": " + message);
        }
    }
    
    static int passedTests = 0;
    static int totalTests = 0;
    
    public static void main(String[] args) {
        System.out.println("=== PRUEBAS AUTOMATIZADAS DEL SISTEMA HAMMING ===\n");
        
        testTextConversion();
        testBinaryConverter();
        testHammingEncoding74();
        testHammingEncoding117();
        testNoiseChannel();
        testErrorCorrection();
        testCompleteTransmission();
        
        printSummary();
    }
    
    static void testTextConversion() {
        System.out.println("\n--- PRUEBA 1: Conversión de Texto ---");
        
        BinaryConverter converter = new BinaryConverter();
        String text = "AB";
        
        int[] bits = convertTextToBits(text);
        String expected = "0100000101000010"; // ASCII: A=65, B=66
        
        StringBuilder bitString = new StringBuilder();
        for (int bit : bits) {
            bitString.append(bit);
        }
        
        boolean passed = bitString.toString().equals(expected);
        reportTest("Conversión texto a binario", passed, 
            "Entrada: '" + text + "' -> " + bitString.toString());
    }
    
    static void testBinaryConverter() {
        System.out.println("\n--- PRUEBA 2: Convertidor Binario ---");
        
        BinaryConverter converter = new BinaryConverter();
        String originalText = "Hola";
        
        List<int[]> blocks = converter.convertTextToBlocks(originalText, 4);
        String recovered = converter.convertBlocksToText(blocks);
        
        boolean passed = recovered.contains("H") && recovered.contains("o") && 
                        recovered.contains("l") && recovered.contains("a");
        reportTest("Recuperación de texto desde bloques", passed, 
            "Original: '" + originalText + "' -> Recuperado: '" + recovered + "'");
    }
    
    static void testHammingEncoding74() {
        System.out.println("\n--- PRUEBA 3: Codificación Hamming(7,4) ---");
        
        Hamming encoder = new Hamming();
        int[] testData = {1, 0, 1, 0}; // 4 bits
        
        List<int[]> blocks = new java.util.ArrayList<>();
        blocks.add(testData);
        
        List<int[]> encoded = encoder.encode(blocks);
        
        boolean passed = encoded.size() == 1 && encoded.get(0).length == 7;
        reportTest("Codificación Hamming(7,4)", passed,
            "Entrada: 4 bits -> Salida: " + encoded.get(0).length + " bits");
    }
    
    static void testHammingEncoding117() {
        System.out.println("\n--- PRUEBA 4: Codificación Hamming(11,7) ---");
        
        Hamming encoder = new Hamming();
        int[] testData = {1, 0, 1, 0, 1, 0, 1}; // 7 bits
        
        List<int[]> blocks = new java.util.ArrayList<>();
        blocks.add(testData);
        
        List<int[]> encoded = encoder.encode(blocks);
        
        boolean passed = encoded.size() == 1 && encoded.get(0).length == 11;
        reportTest("Codificación Hamming(11,7)", passed,
            "Entrada: 7 bits -> Salida: " + encoded.get(0).length + " bits");
    }
    
    static void testNoiseChannel() {
        System.out.println("\n--- PRUEBA 5: Canal de Ruido ---");
        
        // Bloque sin alterar
        int[] block = {1, 0, 1, 1, 0, 1, 0};
        java.util.List<int[]> blocks = new java.util.ArrayList<>();
        blocks.add(block.clone());
        
        // Aplicar ruido con probabilidad alta
        NoiseChannel channel = new NoiseChannel(0.5);
        java.util.List<int[]> noisyBlocks = channel.transmitAll(blocks);
        
        int differences = 0;
        for (int i = 0; i < block.length; i++) {
            if (block[i] != noisyBlocks.get(0)[i]) {
                differences++;
            }
        }
        
        boolean passed = differences > 0;
        reportTest("Inyección de ruido", passed,
            "Probabilidad: 50% -> Bits alterados: " + differences);
    }
    
    static void testErrorCorrection() {
        System.out.println("\n--- PRUEBA 6: Corrección de Errores ---");
        
        Hamming encoder = new Hamming();
        ErrorCorrector corrector = new ErrorCorrector();
        
        int[] data = {1, 0, 1, 0};
        java.util.List<int[]> blocks = new java.util.ArrayList<>();
        blocks.add(data);
        
        java.util.List<int[]> encoded = encoder.encode(blocks);
        int[] block = encoded.get(0);
        
        // Introducir error en posición 3
        int[] corrupted = block.clone();
        corrupted[2] ^= 1;
        
        // Detectar error
        int errorPosition = corrector.detectError(corrupted);
        
        // Corregir
        corrector.correct(corrupted, errorPosition);
        
        boolean passed = java.util.Arrays.equals(block, corrupted);
        reportTest("Detección y corrección de error", passed,
            "Error en posición: " + errorPosition + " -> Corregido: " + passed);
    }
    
    static void testCompleteTransmission() {
        System.out.println("\n--- PRUEBA 7: Transmisión Completa ---");
        
        BinaryConverter converter = new BinaryConverter();
        String message = "Prueba";
        
        java.util.List<int[]> dataBlocks = converter.convertTextToBlocks(message, 4);
        
        Hamming encoder = new Hamming();
        java.util.List<int[]> encodedBlocks = encoder.encode(dataBlocks);
        
        NoiseChannel channel = new NoiseChannel(0.05);
        java.util.List<int[]> noisyBlocks = channel.transmitAll(encodedBlocks);
        
        StatisticsReport stats = new StatisticsReport();
        HammingDecoder decoder = new HammingDecoder(stats);
        java.util.List<int[]> decodedBlocks = decoder.decodeAll(noisyBlocks);
        
        String recovered = converter.convertBlocksToText(decodedBlocks);
        
        boolean passed = recovered.length() > 0;
        reportTest("Transmisión completa", passed,
            "Original: '" + message + "' -> Recuperado: '" + recovered + "'");
    }
    
    static void reportTest(String testName, boolean passed, String message) {
        totalTests++;
        if (passed) passedTests++;
        
        TestResult result = new TestResult(testName, passed, message);
        result.print();
    }
    
    static void printSummary() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("RESUMEN DE PRUEBAS");
        System.out.println("=".repeat(50));
        System.out.println("Pruebas pasadas: " + passedTests + "/" + totalTests);
        System.out.println("Tasa de éxito: " + 
            String.format("%.2f%%", (passedTests * 100.0) / totalTests));
        System.out.println("=".repeat(50));
    }
    
    static int[] convertTextToBits(String text) {
        StringBuilder bin = new StringBuilder();
        for (char c : text.toCharArray()) {
            bin.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }
        int[] bits = new int[bin.length()];
        for (int i = 0; i < bin.length(); i++) {
            bits[i] = bin.charAt(i) - '0';
        }
        return bits;
    }
}
