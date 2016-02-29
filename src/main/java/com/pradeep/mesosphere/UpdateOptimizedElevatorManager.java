package com.pradeep.mesosphere;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class UpdateOptimizedElevatorManager implements ElevatorManager {
    private List<Elevator> elevators;

    public UpdateOptimizedElevatorManager(int numElevators) {
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

            // Update current floor
            elevator.setCurrentFloor(elevator.getCurrentFloor() + elevator.getDirection());

            // Check if current floor is in the queue
            // Can be improved with a hash set
            if (elevator.getRequests().contains(elevator.getCurrentFloor())) {
                // remove the request
                elevator.getRequests().remove(elevator.getCurrentFloor());
            }

            // Set direction
            if (elevator.getRequests().size() == 0) {
                elevator.setDirection(0);
            } else {
                // Check if there are more requests in the current direction
                // Else switch direction
                if (checkSwitchDirection(elevator)) {
                    elevator.setDirection(elevator.getDirection() * -1);
                }
            }

        }
    }

    /**
     * Checks if there any more destination floors which are > currentFloor
     * Can be optimized if there
     * @param elevator
     * @return
     */
    private boolean checkSwitchDirection(Elevator elevator) {
        Iterator<Integer> requestsIterator = elevator.getRequests().iterator();
        while (requestsIterator.hasNext()) {
            // There is at least one request in the current direction
            if (requestsIterator.next() > elevator.getCurrentFloor()) {
                return false;
            }
        }
        return true;
    }
}
