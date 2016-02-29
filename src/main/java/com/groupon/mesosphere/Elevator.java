package com.groupon.mesosphere;


import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

/**
 * Elevator class representing the the state of an elevator.
 * The elevator starts from floor 1.
 * Requests is a queue containing destination floor.
 * Requests can either be added as a consequence of 'pickup' or 'goto'.
 */
public class Elevator {
    private static final String STATE_DELIMITER = ",";
    private int id;
    // Current floor the elevator is in.
    private int currentFloor;
    // The destination floor the elevator is moving to.
    private int currentDestinationFloor;

    // Current direction 1-> up, -1 down, 0 -> rest
    private int direction;

    // Other queued requests
    private Queue<Integer> requests;

    public Elevator(int id) {
        this.id = id;
        this.currentFloor = 1;
        // 0 denotes that there is no destination floor
        this.currentDestinationFloor = 0;
        // at rest
        this.direction = 0;
        this.requests = new ArrayDeque<Integer>();
    }
    /**
     * Get the state of the elevator. State is represented as
     * 'elevatorID-CurrentFloor-CurrentDestinationFloor-Queued'
     * @return String representing status.
     */
    public String getStatus() {
        StringBuilder sbStatus = new StringBuilder();
        sbStatus.append("Elevator=").append(id).append(STATE_DELIMITER)
                .append("CurrentFloor =").append(currentFloor).append(STATE_DELIMITER)
                .append("Direction=").append(direction).append(STATE_DELIMITER)
                .append("Requests=").append(serializeRequests());
        return sbStatus.toString();
    }

    /**
     * Checks if all the requests in the queue have been completed.
     * @return true -> requests completed, false -> more to complete.
     */
    public boolean requestsCompleted() {
        return (requests.size() == 0 && direction == 0);
    }

    private String serializeRequests() {
        StringBuilder sbRequests = new StringBuilder();
        Iterator<Integer> requestsIterator = requests.iterator();
        while (requestsIterator.hasNext()) {
            sbRequests.append(requestsIterator.next())
                      .append(",");
        }
        return sbRequests.toString();
    }

    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getCurrentDestinationFloor() {
        return currentDestinationFloor;
    }

    public Queue<Integer> getRequests() {
        return requests;
    }

    public int getDirection() {
        return direction;
    }

    public void setCurrentDestinationFloor(int currentDestinationFloor) {
        this.currentDestinationFloor = currentDestinationFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
