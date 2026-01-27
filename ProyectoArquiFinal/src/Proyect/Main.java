package Proyect;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        String message = "Hola Mundo Que tal";

        BinaryConverter converter = new BinaryConverter();
        List<int[]> dataBlocks = converter.convertTextToBlocks(message);

        Hamming encoder = new Hamming();
        List<int[]> encodedBlocks = encoder.encode(dataBlocks);

        // 🔹 CANAL DE RUIDO (NO RandomNoiseGenerator)
        NoiseChannel channel = new NoiseChannel(0.05);
        List<int[]> noisyBlocks = channel.transmitAll(encodedBlocks);

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
