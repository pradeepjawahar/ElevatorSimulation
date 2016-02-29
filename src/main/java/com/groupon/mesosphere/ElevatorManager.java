package com.groupon.mesosphere;

/**
 * An Interface to manage the various operations of an elevator
 */
public interface ElevatorManager {

    /**
     *
     * @return
     */
    public String getStatus();

    /**
     *
     * @param floor
     * @param direction
     */
    public void requestPickup(int floor, int direction);

    /**
     *
     * @param elevatorId
     * @param floor
     */
    public void gotoFloor(int elevatorId, int floor);

    /**
     *
     */
    public void step();

}
