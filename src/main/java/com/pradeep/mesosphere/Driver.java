package com.pradeep.mesosphere;

import java.util.Scanner;

public class Driver {

    private static final String QUIT_PROGRAM = "quit";
    private static final int MAX_ELEVATORS = 16;

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Required parameter elevator manager type missing.");
            System.out.println("SIMPLE,PICKUP_OPTIMIZED,STATE_OPTIMIZED");
            System.exit(1);
        }

        ElevatorManager elevatorManager = ElevatorManagerFactory.getElevatorManager(args[0], MAX_ELEVATORS);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("elevator$ ");
            String input = scanner.nextLine();
            if (input.contains(QUIT_PROGRAM)) {
                System.exit(0);
            } else if (input.contains("status")) {
                System.out.println(elevatorManager.getStatus());
            } else if (input.contains("step")) {
                elevatorManager.step();
                System.out.println("Step");
            } else if (input.contains("pickup")) {
                String[] parts = input.split(" ");
                if (parts.length < 3) {
                    System.out.println("Pick operation needs 2 arguments pickup <floorNumber> <direction>");
                } else {
                    // TODO: Validate integer type for arguments
                    elevatorManager.requestPickup(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                }
            } else if (input.contains("goto")) {
                String[] parts = input.split(" ");
                if (parts.length < 3) {
                    System.out.println("Goto needs 2 arguments pickup <elevatorNumber> <destination>");
                } else {
                    // TODO: Validate integer type for arguments
                    elevatorManager.gotoFloor(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                }
            } else {
                System.out.println("Unknown command = " + input);
                System.out.println("Valid commands are status,step,pickup,goto");
            }
        }
    }
}
