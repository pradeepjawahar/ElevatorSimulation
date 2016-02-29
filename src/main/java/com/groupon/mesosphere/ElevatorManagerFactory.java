package com.groupon.mesosphere;

public class ElevatorManagerFactory {

    private static enum ElevatorManagerType {
        SIMPLE,
        PICKUP_OPTIMIZED,
        UPDATE_OPTIMIZED
    }

    public static ElevatorManager getElevatorManager(String type, int numElevators) {
        ElevatorManagerType elevatorManagerType = null;
        try {
           elevatorManagerType =  ElevatorManagerType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ie) {
            System.out.println("Required parameter elevator manager incorrect.");
            System.out.println("SIMPLE,PICKUP_OPTIMIZED,STATE_OPTIMIZED");
            System.exit(1);
        }

        switch (elevatorManagerType) {
            case SIMPLE:
                return new SimpleElevatorManager(numElevators);
            case PICKUP_OPTIMIZED:
                return new PickupOptimizedElevatorManager(numElevators);
            case UPDATE_OPTIMIZED:
                return new UpdateOptimizedElevatorManager(numElevators);
            default:
                return null;
        }

    }
}
