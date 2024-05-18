package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Elevator implements Runnable {

    private final BlockingQueue<Request> requests;
    private Request currentRequest = new Request(0, 1);
    private ElevatorDirection elevatorDirection = ElevatorDirection.Up;
    private int currentFloor = 0;
    private final int number;
    private boolean isFree = true;

    public Elevator(BlockingQueue<Request> requests, int number) {
        this.requests = requests;
        this.number = number;
    }

    @Override
    public void run() {
        if (this.currentRequest.floorstart()< this.currentFloor){
            this.elevatorDirection = ElevatorDirection.Down;
        }
        else{
            this.elevatorDirection = ElevatorDirection.Up;
        }
        System.out.println("Elevator " + this.number + " started to move from the " + this.currentFloor + " floor to the " + this.currentRequest.floorstart() + " floor");
        try {
            List<Integer> possiblePassingRequestsFloors = new ArrayList<>();
            while (this.currentFloor != this.currentRequest.floorstart()){
                System.out.println("Elevator " + this.number + " on the " + this.currentFloor + " floor now");
                this.currentFloor += this.elevatorDirection == ElevatorDirection.Up ? 1 : -1;
                Thread.sleep(1000);
            }
            System.out.println("Elevator " + this.number + " took passenger on " + this.currentFloor + " floor");
            if (this.currentRequest.floorfinish()< this.currentFloor){
                this.elevatorDirection = ElevatorDirection.Down;
            }
            else{
                this.elevatorDirection = ElevatorDirection.Up;
            }
            while (this.currentFloor != this.currentRequest.floorfinish()) {
                if (this.elevatorDirection == ElevatorDirection.Up) {
                    possiblePassingRequestsFloors.addAll(requests.stream().filter(request -> this.currentFloor < request.floorfinish()
                            && this.currentRequest.floorfinish() > request.floorfinish()).map(Request::floorfinish).toList());
                } else {
                    possiblePassingRequestsFloors.addAll(requests.stream().filter(request -> this.currentFloor > request.floorfinish()
                            && this.currentRequest.floorfinish() < request.floorfinish()).map(Request::floorfinish).toList());
                }
                if (possiblePassingRequestsFloors.contains(this.currentFloor)) {
                    System.out.println("Elevator " + this.number + " took passing request on the " + this.currentFloor + " floor");
                    this.requests.removeAll(this.requests.stream().filter(request -> request.floorfinish() == this.currentFloor).toList());
                    possiblePassingRequestsFloors.removeAll(possiblePassingRequestsFloors.stream().filter(possiblePassingRequestsFloor -> possiblePassingRequestsFloor == this.currentFloor).toList());
                }
                System.out.println("Elevator " + this.number + " on the " + this.currentFloor + " floor now");
                this.currentFloor += this.elevatorDirection == ElevatorDirection.Up ? 1 : -1;
                Thread.sleep(1000);
            }
            System.out.println("Elevator " + this.number + " unloaded all passengers on " + this.currentFloor + " floor");
            System.out.println("Elevator " + this.number + " is currently free");
            this.isFree = true;
            Thread.sleep(1000);
        } catch (Exception exception) {
            System.out.println("Elevator running went wrong");
        }
    }

    public void setCurrentRequest(Request currentRequest) {
        this.currentRequest = currentRequest;
    }

    public boolean isFree() {
        return this.isFree;
    }

    public void setIsFree(boolean isFree) {
        this.isFree = isFree;
    }

    public int getCurrentFloor() {
        return this.currentFloor;
    }
}