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

            if (scanner.hasNextLine()) {
                String inputStr = scanner.nextLine().trim();
                if (!inputStr.isEmpty()) {
                    inputType = Integer.parseInt(inputStr);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida, usando texto por defecto");
        } catch (Exception e) {
            System.out.println("Error de entrada, usando texto");
        }
        
        String message = "";
        byte[] fileData = null;
        String originalFileName = "";
        
        switch(inputType) {
            case 1:
                System.out.print("Ingrese el mensaje de texto: ");
                message = scanner.nextLine();
                break;
            case 2:
                System.out.print("Ingrese ruta del archivo de imagen: ");
                String imagePath = scanner.nextLine();
                originalFileName = imagePath;
                try {
                    java.io.File imgFile = new java.io.File(imagePath);
                    if (imgFile.exists()) {
                        fileData = new byte[(int) imgFile.length()];
                        java.io.FileInputStream fis = new java.io.FileInputStream(imgFile);
                        int bytesRead = fis.read(fileData);
                        fis.close();
                        if (bytesRead != fileData.length) {
                            System.out.println("Advertencia: Se leyeron " + bytesRead + " bytes de " + fileData.length);
                        }
                    } else {
                        System.out.println("Archivo no encontrado: " + imagePath);
                        message = "Imagen no encontrada";
                    }
                } catch (Exception e) {
                    System.out.println("Error al leer archivo: " + e.getMessage());
                    message = "Imagen no encontrada";
                }
                break;
            case 3:
                System.out.print("Ingrese ruta del archivo de audio: ");
                String audioPath = scanner.nextLine();
                originalFileName = audioPath;
                try {
                    java.io.File audioFile = new java.io.File(audioPath);
                    if (audioFile.exists()) {
                        fileData = new byte[(int) audioFile.length()];
                        java.io.FileInputStream fis = new java.io.FileInputStream(audioFile);
                        int bytesRead = fis.read(fileData);
                        fis.close();
                        if (bytesRead != fileData.length) {
                            System.out.println("Advertencia: Se leyeron " + bytesRead + " bytes de " + fileData.length);
                        }
                    } else {
                        System.out.println("Archivo no encontrado: " + audioPath);
                        message = "Audio no encontrado";
                    }
                } catch (Exception e) {
                    System.out.println("Error al leer archivo: " + e.getMessage());
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
            if (scanner.hasNextLine()) {
                String errorInput = scanner.nextLine().trim();
                if (!errorInput.isEmpty()) {
                    errorProbability = Double.parseDouble(errorInput);
                    if (errorProbability < 0 || errorProbability > 1) {
                        errorProbability = 0.05;
                        System.out.println("Rango inválido. Usando 0.05");
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Usando valor por defecto: 0.05");
        } catch (Exception e) {
            System.out.println("Error de entrada. Usando 0.05");
        }
        
        // Seleccionar tipo Hamming
        System.out.println("Seleccione tipo de codificación Hamming:");
        System.out.println("1. Hamming(7,4)");
        System.out.println("2. Hamming(11,7)");
        System.out.print("Opción: ");
        int hammingType = 1;
        try {
            if (scanner.hasNextLine()) {
                String hammingInput = scanner.nextLine().trim();
                if (!hammingInput.isEmpty()) {
                    hammingType = Integer.parseInt(hammingInput);
                    if (hammingType != 2) {
                        hammingType = 1;
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida. Usando Hamming(7,4)");
            hammingType = 1;
        } catch (Exception e) {
            System.out.println("Error. Usando Hamming(7,4)");
            hammingType = 1;
        }
        
        scanner.close();
        
        // Convertir mensaje o archivo a bloques
        BinaryConverter converter = new BinaryConverter();
        List<int[]> dataBlocks;
        int originalBitCount = 0;  // Guardar cantidad exacta de bits
        
        if (fileData != null) {
            if (hammingType == 2) {
                dataBlocks = converter.convertBytesToBlocks(fileData, 7);
            } else {
                dataBlocks = converter.convertBytesToBlocks(fileData, 4);
            }
            originalBitCount = converter.getActualBitCount();  // Guardar antes de codificar
        } else {
            if (hammingType == 2) {
                dataBlocks = converter.convertTextToBlocks(message, 7);
            } else {
                dataBlocks = converter.convertTextToBlocks(message, 4);
            }
            originalBitCount = converter.getActualBitCount();
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
            // Restaurar el contador de bits original
            converter.setActualBitCount(originalBitCount);
            byte[] receivedData = converter.convertBlocksToBytes(decodedBlocks);
            receivedMessage = "[Archivo recuperado - " + receivedData.length + " bytes]";
            
            // Crear carpeta de resultados si no existe
            String carpetaResultados = "archivos_recuperados";
            java.io.File carpeta = new java.io.File(carpetaResultados);
            if (!carpeta.exists()) {
                carpeta.mkdir();
            }
            
            // Guardar archivo recuperado con la extensión correcta
            String fileExtension = getFileExtension(originalFileName);
            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String outputFileName = carpetaResultados + "/archivo_recuperado_" + timestamp + fileExtension;
            
            try {
                java.io.FileOutputStream fos = new java.io.FileOutputStream(outputFileName);
                fos.write(receivedData);
                fos.close();
                System.out.println("✓ Archivo guardado en: " + outputFileName);
                System.out.println("  Carpeta: " + new java.io.File(carpetaResultados).getAbsolutePath());
                System.out.println("  Original: " + new java.io.File(originalFileName).getName());
            } catch (Exception e) {
                System.out.println("⚠ No se pudo guardar el archivo: " + e.getMessage());
            }
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
    
    // Obtiene la extensión del archivo
    private static String getFileExtension(String filePath) {
        if (filePath.contains(".")) {
            return filePath.substring(filePath.lastIndexOf("."));
        }
        return ".bin";
    }
}
