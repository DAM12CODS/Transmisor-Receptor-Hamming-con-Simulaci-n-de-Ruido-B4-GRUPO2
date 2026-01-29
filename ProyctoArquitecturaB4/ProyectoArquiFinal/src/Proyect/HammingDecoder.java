package Proyect;
import java.util.ArrayList;
import java.util.List;

public class HammingDecoder {
	private ErrorCorrector corrector;
    private StatisticsReport stats;

    public HammingDecoder(StatisticsReport stats) {
        this.corrector = new ErrorCorrector();
        this.stats = stats;
    }

    public int[] decode(int[] block) {
        stats.registerBlock();

        int errorPosition = corrector.detectError(block);

        if (errorPosition != 0) {
            stats.registerDetected();
            boolean corrected = corrector.correct(block, errorPosition);

            if (corrected) {
                stats.registerCorrected();
            } else {
                stats.registerFatal();
            }
        }

        // Extraer bits de datos según el tamaño del bloque
        int[] dataBits = extractDataBits(block);
        return dataBits;
    }
    
    // Extrae los bits de datos de un bloque Hamming
    private int[] extractDataBits(int[] block) {
        if (block.length == 7) {
            // Hamming(7,4): datos en posiciones 3,5,6,7 (índices 2,4,5,6)
            return new int[] {
                block[2], block[4], block[5], block[6]
            };
        } else if (block.length == 11) {
            // Hamming(11,7): datos en posiciones 3,5,6,7,9,10,11 (índices 2,4,5,6,8,9,10)
            return new int[] {
                block[2], block[4], block[5], block[6], block[8], block[9], block[10]
            };
        } else {
            // Por defecto, retornar todos menos los bits de paridad
            return block;
        }
    }

    public List<int[]> decodeAll(List<int[]> blocks) {
        List<int[]> decoded = new ArrayList<>();
        for (int[] block : blocks) {
            decoded.add(decode(block));
        }
        return decoded;
    }
}
