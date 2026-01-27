package Proyect;

import java.util.ArrayList;
import java.util.List;

public class NoiseChannel {

    private double errorProbability;
    private RandomNoiseGenerator noiseGenerator;
    private int alteredBits = 0;

    public NoiseChannel(double errorProbability) {
        this.errorProbability = errorProbability;
        this.noiseGenerator = new RandomNoiseGenerator();
    }

    public List<int[]> transmitAll(List<int[]> encodedBlocks) {
        List<int[]> noisyBlocks = new ArrayList<>();

        for (int[] block : encodedBlocks) {
            noisyBlocks.add(transmit(block));
        }

        System.out.println("Bits alterados en el canal: " + alteredBits);
        return noisyBlocks;
    }

    private int[] transmit(int[] block) {
        int[] noisyBlock = block.clone();

        for (int i = 0; i < noisyBlock.length; i++) {
            if (noiseGenerator.shouldFlip(errorProbability)) {
                noisyBlock[i] ^= 1; // bit flip
                alteredBits++;
            }
        }
        return noisyBlock;
    }
}
