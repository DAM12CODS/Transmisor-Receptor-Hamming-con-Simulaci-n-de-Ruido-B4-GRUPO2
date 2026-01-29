package Proyect;

import java.io.Serializable;

/**
 * Almacena metadatos del archivo para recuperación exacta
 */
public class FileMetadata implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public int originalBitCount;  // Cantidad exacta de bits originales
    public int dataSize;          // Tamaño del archivo en bytes
    
    public FileMetadata(int originalBitCount, int dataSize) {
        this.originalBitCount = originalBitCount;
        this.dataSize = dataSize;
    }
}
