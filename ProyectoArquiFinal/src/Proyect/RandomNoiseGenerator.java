package Proyect;

import java.util.Random;

public class RandomNoiseGenerator {

    private Random random = new Random();

    public boolean shouldFlip(double probability) {
        return random.nextDouble() < probability;
    }
}
