Active Monitors
Active Monitors:

This project has the aim to develop a synchronization mechanism in Java. Using only the synchronization methods provided by Java like wait() and notify(), I implement a 'randezvous' mechanism simulating the behavior of system based on the Message Passing Model: the Server task provides three method entrycall(),accept() and entryret() to the Client task that wants to use shared resources. It is also allowed, when more than one Clients are blocked waiting for use a resource, an allocation strategy different from the one provided by Java : the Client is choose according a FIFO policy or a priority one.
