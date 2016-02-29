# ElevatorSimulation
A simple program to simulate the operations of an elevator
## Building and Running the project
The project is built as a maven java  project for java 1.7. It can be built as follows:
```
mvn clean package
```
To the run the project please switch in to the target folder
```
ElevatorSimulation$ cd target
ElevatorSimulation$ java -jar elevator-simulation-1.0-SNAPSHOT.jar <simple | pickup_optimized | state_optimized>
## Example
ElevatorSimulation$ java -jar elevator-simulation-1.0-SNAPSHOT.jar simple
$elevator
```

## Sample Output
```
# The following operation picks up a person from floor 2 , adds the request to elevator 0, goes to floor 3 and finally quits.
# Note - This uses the simple elevator solution.

pjawahar:target/ (master) $ java -jar elevator-simulation-1.0-SNAPSHOT.jar simple    elevator$ pickup 2 1
elevator$ step
Step
elevator$ step
Step
elevator$ status
Elevator=0,CurrentFloor =2,Direction=0,Requests=
Elevator=1,CurrentFloor =1,Direction=0,Requests=
Elevator=2,CurrentFloor =1,Direction=0,Requests=
Elevator=3,CurrentFloor =1,Direction=0,Requests=
Elevator=4,CurrentFloor =1,Direction=0,Requests=
Elevator=5,CurrentFloor =1,Direction=0,Requests=
Elevator=6,CurrentFloor =1,Direction=0,Requests=
Elevator=7,CurrentFloor =1,Direction=0,Requests=
Elevator=8,CurrentFloor =1,Direction=0,Requests=
Elevator=9,CurrentFloor =1,Direction=0,Requests=
Elevator=10,CurrentFloor =1,Direction=0,Requests=
Elevator=11,CurrentFloor =1,Direction=0,Requests=
Elevator=12,CurrentFloor =1,Direction=0,Requests=
Elevator=13,CurrentFloor =1,Direction=0,Requests=
Elevator=14,CurrentFloor =1,Direction=0,Requests=
Elevator=15,CurrentFloor =1,Direction=0,Requests=

elevator$ goto 0 3
elevator$ step
Step
elevator$ status
Elevator=0,CurrentFloor =3,Direction=1,Requests=
Elevator=1,CurrentFloor =1,Direction=0,Requests=
Elevator=2,CurrentFloor =1,Direction=0,Requests=
Elevator=3,CurrentFloor =1,Direction=0,Requests=
Elevator=4,CurrentFloor =1,Direction=0,Requests=
Elevator=5,CurrentFloor =1,Direction=0,Requests=
Elevator=6,CurrentFloor =1,Direction=0,Requests=
Elevator=7,CurrentFloor =1,Direction=0,Requests=
Elevator=8,CurrentFloor =1,Direction=0,Requests=
Elevator=9,CurrentFloor =1,Direction=0,Requests=
Elevator=10,CurrentFloor =1,Direction=0,Requests=
Elevator=11,CurrentFloor =1,Direction=0,Requests=
Elevator=12,CurrentFloor =1,Direction=0,Requests=
Elevator=13,CurrentFloor =1,Direction=0,Requests=
Elevator=14,CurrentFloor =1,Direction=0,Requests=
Elevator=15,CurrentFloor =1,Direction=0,Requests=

elevator$ quit
```

## Solution Design
### Operations Supported
* pickup <floor_number> <direction> - request pickup from a floor in a direction
* status - current status of all elevators - Elevator=<elevator_id>,CurrentFloor =<current_floor>,Direction=<current_direction>,Requests=<pending_requests>
* goto <elevator_id><floorNumber> - person inside elevator , requesting to go to a particular floor
* step - make every elevator perform a unit of task
* quit - quit the program

I have presented 3 alternate solutions here. Once I finished the basic solution I had some time so I worked on 2 alternate solutions. All solutions assumes the number of elevators as 16. The crux of every solution is an `ElevatorManager` which manages all the elevators. Since I had only 16 elevators I implemented this as a simple List and could access it by the index. On a larger system having this as a Map would be beneficial. The `ElevatorManager` is an interface which contains the operations `status,pickup,goto,step`. The reason behind going for an interface is that the various operations could be implemented in multiple ways.

The elevator by itself is represented as a simple POJO class named `Elevator` maintaining the current state of the elevator. The Elevator class maintains `elevatorId, currentFloor,currentDirection,currentDestinationFloor,PendingRequests`. The requests coming into an elevator is maintained as a queue. The way this queue is interpreted depends on the type of elevator manager used. `Direction = {0->rest,1->up,-1->down}`

### Simple Elevator Manager
* Pickup - Requests are added to a particular elevators queue if it’s either at rest or it has the least load. 
* Step - The queue is polled for for more requests and based on the currentFloor and the destination floor the direction of the elevator is set. The direction of the elevator can change based on the requests in the queue. Once a destination is reached direction is set to 0 and the the next request is polled form the queue.
This solution is very simple though not optimal as the elevator goes up and down lets say from floor 1 to floor 50 and then floor 2 to 25.
* GoTo - The person is in the elevator and hence the destination floor is just added to the request queue.

### PickUp Optimized Elevator Manager
* Pickup - Requests are added to a particular elevators queue if the elevator is going in the same direction and at a distance least from the pickup floor. The currentFloor and currentDirection plays an important part in which elevator the floor goes to. Care is taken to ensure that the request is greater or less depending on the direction. E.g if request for going up from 5 th floor and there is an elevator at floor 6 and floor 4 going up the elevator at floor 4 will be chosen. Similarly for going down. If such a direction cannot be found the elevator manager falls back to picking an elevator with least load.
* Step - Same as above (Simple elevator manager)
With this solution there is a high probability that elements in the same direction can be grouped together. The only down side with this solution is that if the elevator is destined to go to floor and there is a pickup at floor 6 the elevator will skip. This lead to my next to my next solution.

### Update Optimized  Elevator Manager
* Pickup - Same as above(Pickup Optimized Elevator Manager)
* Step - After every step operation the queue is traversed to check if there is any request for the current floor. If found the entry is erased (i.e. it’s fulfilled). If there any more entries in the queue it’s verified if the direction needs to be changed. If there is atlas one entry in the current direction it’s served. If there is none in the current direction the direction is reversed and other requests are served.
This solution may lead to starvation if there is at least one new pick up in the current direction the requests in the other direction may not be served. With the pickup also being optimized in this solution such cases can be minimized. Or the pick up could be made much more smarter in a way similar to a cache avoiding hotspots.
Also this solution can use a fast sorted lookup data structure which I did not have time to implement.

## Alternate Solutions 

I considered solutions like having different types of elevator. A design that I have seen in some building in New York where users when requesting a pickup also have a choice to enter the destination. With that information we can come up with a design of having some elevators serving only particular floors. This would require storing more information in the elevator class. On hindering the Elevator class could have been an interface with different types of elevators extending from it.

Also things like max requests per elevator can also be used considering most elevators are limited by the total weight they can carry.
