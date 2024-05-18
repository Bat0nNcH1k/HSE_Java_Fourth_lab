package org.example;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class RequestGeneration extends Thread {

    private final BlockingQueue<Request> requests;
    private final int maxFloor;
    private final int minFloor;
    private final int generationFrequencyInMillis;
    private boolean isGenerationRunning = true;

    public RequestGeneration(BlockingQueue<Request> requests, int minFloor, int maxFloor, int generationFrequencyInMillis) {
        this.requests = requests;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.generationFrequencyInMillis = generationFrequencyInMillis;
    }

    @Override
    public void run() {
        try {
            Random random = new Random();
            while (this.isGenerationRunning) {
                requests.add(new Request(random.nextInt(minFloor, maxFloor),random.nextInt(minFloor, maxFloor)));
                Thread.sleep(generationFrequencyInMillis);
            }
        } catch (InterruptedException exception) {
            System.out.println("Request generation went wrong");
        }
    }

    public void stopRequestGeneration() {
        this.isGenerationRunning = false;
    }
}