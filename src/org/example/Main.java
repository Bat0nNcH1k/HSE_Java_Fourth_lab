package org.example;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BlockingQueue<Request> requests = new LinkedBlockingQueue<>();

        System.out.println("Please enter the lowest floor:");
        int minFloor = scanner.nextInt();
        System.out.println("Please enter the highest floor:");
        int maxFloor = scanner.nextInt();
        System.out.println("Please enter frequency of request generation:");
        int generationFrequencyInMillis = scanner.nextInt();
        System.out.println("Please enter number of elevators:");
        int numberOfElevators = scanner.nextInt();

        RequestGeneration requestGeneration = new RequestGeneration(requests, minFloor, maxFloor, generationFrequencyInMillis);
        requestGeneration.start();

        ElevatorsManager elevatorsManager = new ElevatorsManager(requests, numberOfElevators);
        elevatorsManager.start();

        scanner.next();
        requestGeneration.stopRequestGeneration();
        elevatorsManager.stopElevators();
        System.out.println("Shutoff is in process");
    }
}