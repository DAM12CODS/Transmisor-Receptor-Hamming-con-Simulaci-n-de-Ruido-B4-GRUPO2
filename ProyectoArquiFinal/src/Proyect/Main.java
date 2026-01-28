package Proyect;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== SISTEMA DE TRANSMISIÓN HAMMING CON SIMULACIÓN DE RUIDO ===");
        System.out.println();
        
        // Menú de entrada
        System.out.println("Seleccione tipo de entrada:");
        System.out.println("1. Texto");
        System.out.println("2. Archivo de imagen");
        System.out.println("3. Archivo de audio");
        System.out.print("Opción: ");
        int inputType = 1;
        try {
            inputType = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida, usando texto por defecto");
        }
        
        String message = "";
        byte[] fileData = null;
        
        switch(inputType) {
            case 1:
                System.out.print("Ingrese el mensaje de texto: ");
                message = scanner.nextLine();
                break;
            case 2:
                System.out.print("Ingrese ruta del archivo de imagen: ");
                String imagePath = scanner.nextLine();
                try {
                    java.io.File imgFile = new java.io.File(imagePath);
                    if (imgFile.exists()) {
                        fileData = new byte[(int) imgFile.length()];
                        java.io.FileInputStream fis = new java.io.FileInputStream(imgFile);
                        fis.read(fileData);
                        fis.close();
                    }
                } catch (Exception e) {
                    System.out.println("Error al leer archivo. Usando texto por defecto.");
                    message = "Imagen no encontrada";
                }
                break;
            case 3:
                System.out.print("Ingrese ruta del archivo de audio: ");
                String audioPath = scanner.nextLine();
                try {
                    java.io.File audioFile = new java.io.File(audioPath);
                    if (audioFile.exists()) {
                        fileData = new byte[(int) audioFile.length()];
                        java.io.FileInputStream fis = new java.io.FileInputStream(audioFile);
                        fis.read(fileData);
                        fis.close();
                    }
                } catch (Exception e) {
                    System.out.println("Error al leer archivo. Usando texto por defecto.");
                    message = "Audio no encontrado";
                }
                break;
            default:
                message = "Opción inválida";
        }
        
        // Configurar probabilidad de ruido
        System.out.print("Ingrese probabilidad de error (0.0 - 1.0, ej: 0.05): ");
        double errorProbability = 0.05;
        try {
            errorProbability = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Usando valor por defecto: 0.05");
        }
        
        // Seleccionar tipo Hamming
        System.out.println("Seleccione tipo de codificación Hamming:");
        System.out.println("1. Hamming(7,4)");
        System.out.println("2. Hamming(11,7)");
        System.out.print("Opción: ");
        int hammingType = 1;
        try {
            hammingType = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Usando Hamming(7,4) por defecto");
        }
        
        scanner.close();
        
        // Convertir mensaje o archivo a bloques
        BinaryConverter converter = new BinaryConverter();
        List<int[]> dataBlocks;
        
        if (fileData != null) {
            if (hammingType == 2) {
                dataBlocks = converter.convertBytesToBlocks(fileData, 7);
            } else {
                dataBlocks = converter.convertBytesToBlocks(fileData, 4);
            }
        } else {
            if (hammingType == 2) {
                dataBlocks = converter.convertTextToBlocks(message, 7);
            } else {
                dataBlocks = converter.convertTextToBlocks(message, 4);
            }
        }
        
        // Codificación
        Hamming encoder = new Hamming();
        List<int[]> encodedBlocks = encoder.encode(dataBlocks);
        
        System.out.println();
        System.out.println("Bloques de datos originales: " + dataBlocks.size());
        System.out.println("Bloques codificados (Hamming): " + encodedBlocks.size());
        System.out.println("Probabilidad de error en el canal: " + (errorProbability * 100) + "%");
        System.out.println();
        
        // Canal de ruido
        NoiseChannel channel = new NoiseChannel(errorProbability);
        List<int[]> noisyBlocks = channel.transmitAll(encodedBlocks);
        
        // Decodificación
        StatisticsReport stats = new StatisticsReport();
        HammingDecoder decoder = new HammingDecoder(stats);
        List<int[]> decodedBlocks = decoder.decodeAll(noisyBlocks);
        
        // Recuperar mensaje
        String receivedMessage = "";
        if (fileData != null) {
            byte[] receivedData = converter.convertBlocksToBytes(decodedBlocks);
            receivedMessage = "[Archivo recuperado - " + receivedData.length + " bytes]";
        } else {
            receivedMessage = converter.convertBlocksToText(decodedBlocks);
        }
        
        // Mostrar resultados
        System.out.println("=== RESULTADOS DE TRANSMISIÓN ===");
        System.out.println("Mensaje original    : " + (message.length() > 50 ? message.substring(0, 50) + "..." : message));
        System.out.println("Mensaje recibido    : " + receivedMessage);
        System.out.println();
        stats.printReport();
    }
}
