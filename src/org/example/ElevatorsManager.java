package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ElevatorsManager extends Thread {

    private final BlockingQueue<Request> requests;
    private final List<Elevator> elevators = new ArrayList<>();
    private final List<Thread> elevatorsThreads = new ArrayList<>();
    private boolean areElevatorsRunning = true;

    public ElevatorsManager(BlockingQueue<Request> requests, int numberOfElevators) {
        this.requests = requests;
        for (int i = 0; i < numberOfElevators; ++i) {
            this.elevators.add(new Elevator(requests, i + 1));
            this.elevatorsThreads.add(new Thread(this.elevators.get(i)));
        }
    }

    @Override
    public void run() {
        try {
            while (this.areElevatorsRunning) {
                if (this.requests.isEmpty()) {
                    Thread.sleep(300);
                    continue;
                }
                if (isAtLeastOneElevatorFree()) {
                    Request request = requests.poll();
                    if (request != null) {
                        int fastestElevatorIndex = getFastestElevatorIndex(request);
                        Elevator fastestElevator = elevators.get(fastestElevatorIndex);
                        fastestElevator.setCurrentRequest(request);
                        fastestElevator.setIsFree(false);
                        elevatorsThreads.set(fastestElevatorIndex, new Thread(fastestElevator));
                        elevatorsThreads.get(fastestElevatorIndex).start();
                    }
                }
            }
            for (Thread elevatorThread : elevatorsThreads) {
                elevatorThread.join();
            }
        } catch (InterruptedException exception) {
            System.out.println("ElevatorManager went wrong");
        }
    }

    private int getFastestElevatorIndex(Request request) {
        int fastestElevatorPathToRequest = Integer.MAX_VALUE;
        int fastestElevatorIndex = 0;
        for (int i = 0; i < this.elevators.size(); ++i) {
            if (!this.elevators.get(i).isFree()) {
                continue;
            }
            if (fastestElevatorPathToRequest > Math.abs(elevators.get(i).getCurrentFloor() - request.floorstart())) {
                fastestElevatorPathToRequest = Math.abs(elevators.get(i).getCurrentFloor() - request.floorstart());
                fastestElevatorIndex = i;
            }
        }
        return fastestElevatorIndex;
    }

    private boolean isAtLeastOneElevatorFree() {
        for (Elevator elevator : elevators) {
            if (elevator.isFree()) {
                return true;
            }
        }
        return false;
    }

    public void stopElevators() {
        this.areElevatorsRunning = false;
    }
}