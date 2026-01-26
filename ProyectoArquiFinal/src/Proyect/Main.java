package Proyect;

import java.util.List;

public class Main {
/*Prueba repositorio*/
    public static void main(String[] args) {

        String message = "Hola Mundo";

        BinaryConverter converter = new BinaryConverter();
        List<int[]> dataBlocks = converter.convertTextToBlocks(message);

        Hamming encoder = new Hamming();
        List<int[]> encodedBlocks = encoder.encodeAll(dataBlocks);

        double noiseProbability = 0.05;
        RandomNoiseGenerator noise = new RandomNoiseGenerator(noiseProbability);
        List<int[]> noisyBlocks = noise.applyNoise(encodedBlocks);

        StatisticsReport stats = new StatisticsReport();
        HammingDecoder decoder = new HammingDecoder(stats);
        List<int[]> decodedBlocks = decoder.decodeAll(noisyBlocks);

        String receivedMessage = converter.convertBlocksToText(decodedBlocks);

        System.out.println("Mensaje original : " + message);
        System.out.println("Mensaje recibido : " + receivedMessage);
        System.out.println();
        stats.printReport();
    }
}

