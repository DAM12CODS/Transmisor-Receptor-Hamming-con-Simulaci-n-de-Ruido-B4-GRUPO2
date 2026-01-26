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

        // Extraer bits de datos: posiciones 3,5,6,7
        return new int[] {
            block[2], block[4], block[5], block[6]
        };
    }

    public List<int[]> decodeAll(List<int[]> blocks) {
        List<int[]> decoded = new ArrayList<>();
        for (int[] block : blocks) {
            decoded.add(decode(block));
        }
        return decoded;
    }
}
