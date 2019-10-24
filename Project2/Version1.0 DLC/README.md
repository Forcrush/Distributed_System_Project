# Distributed_System_Project2_DLC -- Independent ChatBox
 
## Environment 

- `IntelliJ IDEA / cmd`
- `JDK 12.0`

## Independent ChatBox

- Implement of one server-multiple clients chatbox

- To mitigate the throughput of data, using file position to send message from server to clients

## RUN (In cmd)

- locate in `.../src`

- run `javac -encoding utf-8 *.java` (compile)

- run `java ChatboxServer <port> <dic_path>` such as `java ChatboxServer 2029 dic.txt`

- run `java ChatboxClient <ip> <port> <username>` such as `java ChatboxClient localhost 2029 client1`