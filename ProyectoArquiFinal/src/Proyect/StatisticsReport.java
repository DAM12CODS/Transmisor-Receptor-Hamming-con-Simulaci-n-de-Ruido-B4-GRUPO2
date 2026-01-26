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
	        System.out.println("=== REPORTE DE TRANSMISIÓN ===");
	        System.out.println("Bloques totales: " + totalBlocks);
	        System.out.println("Errores detectados: " + detectedErrors);
	        System.out.println("Errores corregidos: " + correctedErrors);
	        System.out.println("Errores fatales: " + fatalErrors);
	    }
}
