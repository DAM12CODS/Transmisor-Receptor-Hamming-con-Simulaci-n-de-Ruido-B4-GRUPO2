package Proyect;

public class StatisticsReport {
	
    private int totalBlocks;
    private int detectedErrors;
    private int correctedErrors;
    private int fatalErrors;

    public void registerBlock() {
        totalBlocks++;
    }

    public void registerDetected() {
        detectedErrors++;
    }

    public void registerCorrected() {
        correctedErrors++;
    }

    public void registerFatal() {
        fatalErrors++;
    }

    public void printReport() {
        System.out.println("=== REPORTE DE ESTADÍSTICAS DE TRANSMISIÓN ===");
        System.out.println();
        System.out.println("Bloques totales procesados       : " + totalBlocks + 
                         " bloques");
        System.out.println("Errores detectados              : " + detectedErrors + 
                         " (" + (totalBlocks > 0 ? String.format("%.2f%%", (detectedErrors * 100.0) / totalBlocks) : "N/A") + ")");
        System.out.println("Errores corregidos              : " + correctedErrors + 
                         " (" + (detectedErrors > 0 ? String.format("%.2f%%", (correctedErrors * 100.0) / detectedErrors) : "N/A") + ")");
        System.out.println("Errores fatales (no corregibles): " + fatalErrors + 
                         " (" + (detectedErrors > 0 ? String.format("%.2f%%", (fatalErrors * 100.0) / detectedErrors) : "N/A") + ")");
        
        double integrityRate = totalBlocks > 0 ? ((totalBlocks - fatalErrors) * 100.0) / totalBlocks : 0;
        System.out.println();
        System.out.println("Tasa de integridad de datos     : " + String.format("%.2f%%", integrityRate));
    }
}
