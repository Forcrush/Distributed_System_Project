# Distributed_System_Project2
 
## Environment 

- `IntelliJ IDEA / cmd`
- `JDK 12.0`

## Version 3.0

- Based on socket communication

- Server & Multiple Client structure

- Commnication based on `ObjectOutputStream/ObjectInputStream`


## Issues

- shapes still lag on one step

## RUN (In cmd)

- locate in `.../src`

- run `javac -encoding utf-8 *.java` (compile)

- run `java CreateWhiteBoard <address> <port> <servername>` such as `java CreateWhiteBoard localhost 9999 Server`

- run `java CreateWhiteBoard <address> <port> <clientname>` such as `java JoinWhiteBoard localhost 9999 Client1`