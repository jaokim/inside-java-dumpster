# Introduction
The code here tries to mimic a relatively likely production system. The mimiced system is supposed contain a plethora of bugs, anti-patterns, memory hogs, oddities, performance issues and in general be less than ideal. It serves as a sandbox, or playground to demonstrate how to troubleshoot various issues.

> This project implements a fake business application, that gets request, and produces results.

```mermaid
sequenceDiagram
    actor User
    participant Computer
    participant WebAppServer
    participant BusinessApplication
    participant Backend
    User-->Computer: Action
    Computer->>WebAppServer: Do request
    note right of WebAppServer: JVM
    rect rgb(200,200,200,0.1)
      par Produces JFR event
        WebAppServer->>BusinessApplication: Invoke business logic
      and 
        opt Crashes
          BusinessApplication->>Backend: Do business
        end
        Backend->>BusinessApplication: Do other business
        BusinessApplication->>WebAppServer: Response
      end
      
    end
    WebAppServer->>Computer: Result
```

:heavy_exclamation_mark: Disclaimer: This code contains both subtle and not so subtle bugs -- that is its purpose.

## Motivations and objective
* produce JFR events, to learn JMC and try out stuff
* life-like env. to set JFR properties and arguments to get away from simple examples like:
     `java -XX:StartFlightRecording Example.java`
* reproducible executions; requests to the application are created from actual log files
* recreate bugs or weird behaviour
* has different web application servers (Micronaut, Jetty, will add WLS) in order to test where f.i. JFR VM properties are set

## Possible scenarios
:bomb: crash "randomly" for learning purposes -- not just `java Crash.java` 

:watch: find memory leaks, bottlenecks, etc

:bar_chart: create life-like JFR event examples, to not only create "TestEvent" and the likes

:chart_with_upwards_trend: using JConsole to remote JVM via ssh tunneling

:vhs: JFR remote recording stream to containerized servers

:coffee: create multi-JDK environments (JDK8 combined with JDK17)

# Overview

```mermaid
flowchart TD
  nf([netflow_data logs]) --- Client
  WebClient --> appserver
  CliClient --> backend
  subgraph client [Client Components]
    Client --> WebClient
    Client --> CliClient
  end
  subgraph container [Server Components]
  subgraph appserver [Application Servers]
    JettyServer
    MicronautServer
  end
  appserver --> backend
  subgraph backend [Backend]
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

The diagram above gives a context for most of the components involved. There is however also the [OutsideDumpster](https://github.com/jaokim/inside-java-dumpster/tree/main/OutsideDumpster) which is included in each of the server components. This holds classes specific for the sandbox, and is used to aid in creating buggy behavior and disable/enable it in "production".

# An example test-run

```mermaid
flowchart LR
  subgraph Client [Client terminal]
    WebClient["webclient http://jvm/business/"]
    jcmd["jcmd JFR.start"]
    JConsole
  end
  subgraph Server [Server terminal]
    subgraph JVM
      ApplicationServer
      JFR
    end
  end
  WebClient -->|request| ApplicationServer
  jcmd -->|record| JFR
  JConsole -->|control| JVM
  style JVM fill:#e0e0e9dd
```

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


