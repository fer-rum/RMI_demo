RMI_demo
========

A small demonstration of how to implement Remote Method Invocation in Java.

In the src-folder, you find three parts:

* client : the RMI-client, who is calling methods on the server.
* shared : the interface that is common to client and server and used in the communication.
* server : the RMI-server, who is offering methods to be called.

How to use:
-----------

Start the servers main-Method. It will look for a RMI registry server or start one if needed. Then it will register its services.
Start the client. It looks up the registry server and attempts to call the methods offered by the server.
Some of the methods on the server take longer to process then others. They will be scheduled in a thread pool with one thread per request. The results therefor may come out of order.
When done the client will exit.
You can terminate the RMI-server and the RMI-registry server by killing their processes.
