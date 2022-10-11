Accompanying code for my blog [jaokim.github.io](https://jaokim.github.io/).

## Introduction
The code here tries to mimic a relatively likely production system. The mimiced system is supposed contain a plethora of bugs, anti-patterns, memory hogs, oddities, performance issues and in general be less than ideal. It serves as a sandbox, or playground to demonstrate how to troubleshoot various issues.

Disclaimer: This code contains both subtle and not so subtle bugs -- that is its purpose.

Some motivations behind the project:
* reproducible executions; requests to the application are created from actual log files
* create life-like JFR event examples, to not only create "TestEvent" and the likes
* has different web application servers (Micronaut, Jetty, will add WLS) in order to test where f.i. JFR VM properties are set

### Overview

```mermaid
flowchart TD
  nf([netflow_data logs]) --- Client
  WebClient --> appserver
  CliClient --> backend
  subgraph client
    Client --> WebClient
    Client --> CliClient
  end
  subgraph container
  subgraph appserver
    JettyServer
    MicronautServer
  end
  appserver --> backend
  subgraph backend
    BusinessLogic --- bd[(Backend)]
  end
  end
  style container fill:#c3a64724
  click Client "https://github.com/jaokim/inside-java-dumpster/tree/main/Client"
  click nf "https://github.com/jaokim/inside-java-dumpster/tree/main/Client"
  click WebClient "https://github.com/jaokim/inside-java-dumpster/tree/main/WebClient"
  click CliClient "https://github.com/jaokim/inside-java-dumpster/tree/main/CliClient"
  click BusinessLogic "https://github.com/jaokim/inside-java-dumpster/tree/main/BusinessLogic"
  click bd "https://github.com/jaokim/inside-java-dumpster/tree/main/Backend"
  click MicronautServer "https://github.com/jaokim/inside-java-dumpster/tree/main/MicronautServer"
  click JettyServer "https://github.com/jaokim/inside-java-dumpster/tree/main/JettyServer"
```

## An example test-run
Preferable you'd want a remote machine to run the server JVM on, but it works with an all local setup.
* You need atleast JDK17.

Check out all the code and build using Maven (I might supply pre-built binairies at some point).
Start two terminals (has been tested on cygwin). 

In terminal 1, start the Micronaut Webserver
```
sh ./runmicronaut.sh
```

In terminal 2, start JConsole and connnect to the Micronaut JVM
```
jconsole &
```
Then, start the webclient script to create network requests.
```
sh runwebclient.sh
```

The runwebclient script first starts a delayed JFR recording for 60 seconds, and then produces network request for atleaset 60 seconds.
When done, the webclient should stop, and you should have a JFR recording to analyze (or a crashlog!).

### Excercise 1
Find out which service is the slowest, and why. When you've found the likely culprit, set it as non-buggy in JConsole, and do another test run.


---
If you're looking for code for my [old blog posts at inside.java](https://inside.java/u/JoakimNordstrom/), go look in the [tag inside.java](https://github.com/jaokim/inside-java-dumpster/tree/inside.java)


