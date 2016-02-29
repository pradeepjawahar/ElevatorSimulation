package com.groupon.mesosphere;

import java.util.ArrayList;
import java.util.List;

public class PickupOptimizedElevatorManager implements ElevatorManager {

    private List<Elevator> elevators;

    public PickupOptimizedElevatorManager(int numElevators) {
        elevators = new ArrayList<Elevator>(numElevators);
        for (int i = 0; i < numElevators; i++) {
            elevators.add(new Elevator(i));
        }
    }

    /**
     * @return
     */
    @Override
    public String getStatus() {
        StringBuilder sbStatus = new StringBuilder();
        for (Elevator elevator : elevators) {
            sbStatus.append(elevator.getStatus())
                    .append("\n");
        }
        return sbStatus.toString();
    }

    /**
     * Pick the elevator with the least number of requests
     *
     * @param floor
     * @param direction
     */
    @Override
    public void requestPickup(int floor, int direction) {
        // Get an elevator traveling in the same direction and is the closest to the destination floor
        Elevator minElevator = null;
        int minDiff = Integer.MAX_VALUE;
        for (Elevator elevator : elevators) {
            if (elevator.getDirection() == direction) {
                if ((direction == -1 && floor < elevator.getCurrentFloor()) &&
                        (direction == 1 && floor > elevator.getCurrentFloor())) {
                    int floorDiff = Math.abs(floor - elevator.getCurrentFloor());
                    if (floorDiff < minDiff) {
                        minDiff = floorDiff;
                        minElevator = elevator;
                    }
                }
            }
        }

        // All elevators are at rest, direction = 0
        if (minElevator == null) {
            // Pick an elevator with least load
            minElevator = elevators.get(0);
            for (Elevator elevator : elevators) {
                if (elevator.getRequests().size() < minElevator.getRequests().size()) {
                    minElevator = elevator;
                }
            }
            // add request
            minElevator.getRequests().offer(floor);
        }
    }

    /**
     * Pick the elevator with least number of requests.
     *
     * @param elevatorId
     * @param floor
     */
    @Override
    public void gotoFloor(int elevatorId, int floor) {
        if (elevatorId > elevators.size() - 1) {
            System.out.println("Incorrect request. Elevator = " + elevatorId + " is not a valid elevator");
        }
        elevators.get(elevatorId).getRequests().offer(floor);
    }

    /**
     *
     */
    @Override
    public void step() {
        // Update state of each elevator
        for (Elevator elevator : elevators) {
            if (elevator.requestsCompleted())
                continue;
            // Mark previous request completed if done.
            if (elevator.getCurrentFloor() == elevator.getCurrentDestinationFloor()) {
                elevator.setDirection(0);
            }

            if (elevator.getRequests().size() > 0 && elevator.getDirection() == 0) {
                elevator.setCurrentDestinationFloor(elevator.getRequests().poll());
                // The next request could be to the current floor.
                if (elevator.getCurrentFloor() == elevator.getCurrentDestinationFloor()) {
                    elevator.setDirection(0);
                } else {
                    int direction = (elevator.getCurrentFloor() < elevator.getCurrentDestinationFloor()) ? 1 : -1;
                    elevator.setDirection(direction);
                }
            }

            // Update the floor only if requests not completed
            if (!elevator.requestsCompleted()) {
                elevator.setCurrentFloor(elevator.getCurrentFloor() + elevator.getDirection());
            }
        }
    }

}
