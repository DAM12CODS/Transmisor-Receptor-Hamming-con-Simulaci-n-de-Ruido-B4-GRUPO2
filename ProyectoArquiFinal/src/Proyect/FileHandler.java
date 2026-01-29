package Proyect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandler {
    
    /**
     * Lee un archivo y devuelve sus bytes
     * Soporta: imágenes (PNG, JPG, BMP), audio (WAV, MP3, FLAC), y otros formatos
     * @param filePath Ruta del archivo
     * @return Array de bytes del archivo, o null si hay error
     */
    public static byte[] readFile(String filePath) {
        File file = new File(filePath);
        
        if (!file.exists()) {
            System.err.println("Archivo no encontrado: " + filePath);
            return null;
        }
        
        if (!file.isFile()) {
            System.err.println("La ruta no es un archivo: " + filePath);
            return null;
        }
        
        if (file.length() > 10_000_000) { // Límite de 10MB para evitar problemas de memoria
            System.err.println("Archivo demasiado grande (máximo 10MB): " + file.length() + " bytes");
            return null;
        }
        
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            System.out.println("Archivo leído exitosamente: " + file.getAbsolutePath() + 
                             " (" + file.length() + " bytes)");
            return data;
        } catch (IOException e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Escribe bytes a un archivo
     * @param filePath Ruta destino
     * @param data Array de bytes a escribir
     * @return true si fue exitoso, false en caso contrario
     */
    public static boolean writeFile(String filePath, byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
            System.out.println("Archivo escrito exitosamente: " + filePath);
            return true;
        } catch (IOException e) {
            System.err.println("Error al escribir archivo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene información sobre un archivo
     * @param filePath Ruta del archivo
     * @return String con información del archivo
     */
    public static String getFileInfo(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return "Archivo: " + file.getName() + 
                   ", Tamaño: " + file.length() + " bytes, " +
                   "Tipo: " + getFileType(filePath);
        }
        return "Archivo no encontrado";
    }
    
    /**
     * Determina el tipo de archivo según su extensión
     * @param filePath Ruta del archivo
     * @return Tipo de archivo (imagen, audio, otro)
     */
    public static String getFileType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
        
        // Tipos de imagen
        if (extension.matches("png|jpg|jpeg|bmp|gif|tiff")) {
            return "Imagen (" + extension.toUpperCase() + ")";
        }
        
        // Tipos de audio
        if (extension.matches("mp3|wav|flac|aac|ogg|m4a")) {
            return "Audio (" + extension.toUpperCase() + ")";
        }
        
        // Tipos de video
        if (extension.matches("mp4|avi|mov|flv|wmv|webm")) {
            return "Video (" + extension.toUpperCase() + ")";
        }
        
        // Tipos de texto
        if (extension.matches("txt|pdf|doc|docx|json|xml")) {
            return "Documento (" + extension.toUpperCase() + ")";
        }
        
        return "Archivo desconocido";
    }
}
