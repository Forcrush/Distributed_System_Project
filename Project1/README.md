# Multithread Server-Client Dictionary Service

This is a project based on Server-Client architecture to implement multi-thread server to handle concurrent requests from several clients to query or operate a dictionary file on server.

More details about this project and the code can be found in [Assignment1.pdf]() and [Report.pdf]()

There are two ways to execute this project (Need parameters to run main function):

- Open `DictionaryMultithreadSC` in IntelliJ IDEA, run the DictionaryServer.java with running parameters in form of `<prot> <dictinoary-file>` such as `2029 dic.txt` and the DictionaryClient.java with running parameters in the form of `<server-address> <port>` such as `localhost 2029`.

- Run the `jar` package in `Jar Package as` in cmd/shell as `java –jar DictionaryServer.jar <port> <dictionary-file>` and `java –jar DictionaryClient.jar <server-address> <server-port>`
