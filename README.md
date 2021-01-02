# Eclipse Cargo Tracker - Applied Domain-Driven Design Blueprints for Jakarta EE

## Overview

The project demonstrates how you can develop applications with Jakarta EE using widely adopted architectural best practices like Domain-Driven 
Design (DDD). The project is directly based on the well known 
original [Java DDD sample application](http://dddsample.sourceforge.net) 
developed by DDD pioneer Eric Evans' company Domain Language and the Swedish 
software consulting company Citerus. The cargo example actually comes from 
Eric Evans' seminal book on DDD. The original application is written in Spring,
Hibernate and Jetty whereas the application is built on Jakarta EE.

The application is an end-to-end system for keeping track of shipping cargo. It 
has several interfaces described in the following sections.

For further details on the project, please visit: https://eclipse-ee4j.github.io/cargotracker/.

![Cargo Tracker cover](cargo_tracker_cover.png)
 
## Getting Started

The [project website](https://eclipse-ee4j.github.io/cargotracker/) has detailed information on how to get started.

The simplest steps are the following (no IDE required):

* Get the project source code.
* Ensure you are running Java SE 8. The project by default uses Payara 4.1, which supports Java SE 8.
* Make sure JAVA_HOME is set.
* As long as you have Maven set up properly, navigate to the project source root and 
  type: `mvn package cargo:run`
* Go to http://localhost:8080/cargo-tracker

To set up in Eclipse, follow these steps:

* Set up [Java SE 8](https://www.azul.com/downloads/zulu-community/?version=java-8-lts), [the 2020-06 release of Eclipse for Enterprise Java Developers](https://www.eclipse.org/downloads/packages/release/2020-06/r/eclipse-ide-enterprise-java-developers) (this is the latest Eclipse IDE version that supports Java SE 8) and [Payara 4.1](https://repo1.maven.org/maven2/fish/payara/distributions/payara/4.1.2.181/payara-4.1.2.181.zip) (Payara 5 is not supported yet. Payara 4.1 only supports Java SE 8). You will also need to set up [Payara Tools](https://marketplace.eclipse.org/content/payara-tools) in Eclipse.
* Import this code in Eclipse as a Maven project, 
  Eclipse will do the rest for you. Proceed with clean/building the application.
* After the project is built (which will take a while the very first time as 
  Maven downloads dependencies), simply run it via Payara 4.

## Exploring the Application

After the application runs, it will be available at: 
http://localhost:8080/cargo-tracker/. Under the hood, the application uses a 
number of Jakarta EE (Java EE 7) features including JSF, CDI, EJB, JPA, JAX-RS, WebSocket, JSON Processing, Bean Validation and JMS.

There are several web interfaces, REST interfaces and a file system scanning
interface. It's probably best to start exploring the interfaces in the rough
order below.

The tracking interface let's you track the status of cargo and is
intended for the general public. Try entering a tracking ID like ABC123 (the 
application is pre-populated with some sample data).

The administrative interface is intended for the shipping company that manages
cargo. The landing page of the interface is a dashboard providing an overall 
view of registered cargo. You can book cargo using the booking interface.
One cargo is booked, you can route it. When you initiate a routing request,
the system will determine routes that might work for the cargo. Once you select
a route, the cargo will be ready to process handling events at the port. You can
also change the destination for cargo if needed or track cargo.

The Incident Logging interface is intended for port personnel registering what 
happened to cargo. The interface is primarily intended for mobile devices, but
you can use it via a desktop browser. The interface is accessible at this URL: http://localhost:8080/cargo-tracker/event-logger/index.xhtml. For convenience, you
could use a mobile emulator instead of an actual mobile device. Generally speaking cargo
goes through these events:

* It's received at the origin port.
* It's loaded and unloaded onto voyages on it's itinerary.
* It's claimed at it's destination port.
* It may go through customs at arbitrary points.

While filling out the event registration form, it's best to have the itinerary 
handy. You can access the itinerary for registered cargo via the admin interface. The cargo handling is done via Messaging for scalability. While using the incident logger, note that only the load 
and unload events require as associated voyage.

You should also explore the file system based bulk event registration interface. 
It reads files under /tmp/uploads. The files are just CSV files. A sample CSV
file is available under [src/test/resources/handling_events.csv](src/test/resources/handling_events.csv). The sample is already set up to match the remaining itinerary events for cargo ABC123. Just make sure to update the times in the first column of the sample CSV file to match the itinerary as well.

Sucessfully processed entries are archived under /tmp/archive. Any failed records are 
archived under /tmp/failed.

Don't worry about making mistakes. The application is intended to be fairly 
error tolerant. If you do come across issues, you should [report them](https://github.com/eclipse-ee4j/cargotracker/issues).

*All data entered is wiped upon application restart, so you can start from 
a blank slate easily if needed.*

You can also use the soapUI scripts included in the source code to explore the 
REST interfaces as well as the numerous unit tests covering the code base 
generally.

## Exploring the Code

As mentioned earlier, the real point of the application is demonstrating how to 
create well architected, effective Jakarta EE applications. To that end, once you 
have gotten some familiarity with the application functionality the next thing 
to do is to dig right into the code.

DDD is a key aspect of the architecture, so it's important to get at least a 
working understanding of DDD. As the name implies, Domain-Driven Design is an 
approach to software design and development that focuses on the core domain and 
domain logic.

For the most part, it's fine if you are new to Jakarta EE. As long as you have a
basic understanding of server-side applications, the code should be good enough to get started. For learning Jakarta EE further,
we have recommended a few links in the resources section of the project site. Of 
course, the ideal user of the project is someone who has a basic working 
understanding both Jakarta EE and DDD. Though it's not our goal to become a kitchen 
sink example for demonstrating the vast amount of APIs and features in Jakarta EE,
we do use a very representative set. You'll find that you'll learn a fair amount
by simply digging into the code to see how things are implemented.

## Exploring the Tests

Cargo Tracker's testing is done using JUnit and Arquillian. The Arquillian configuration
uses a [remote container](http://arquillian.org/arquillian-core/#_containers) (Payara 4.1). Therefore, to perform a test you will need to make sure
to have a container running. 

## Testing Locally with Payara
For testing locally you will first need to run a Payara 4.1 server.

You can do that with the following script:
```shell script
wget https://repo1.maven.org/maven2/fish/payara/distributions/payara/4.1.2.181/payara-4.1.2.181.zip
unzip payara-4.1.2.181.zip && cd payara41/bin
./asadmin start-domain
```

Now for running the tests: 
```shell script
mvn -Ppayara -DskipTests=false test
```

## Known Issues
* If you are running older versions of Payara, you will get a log message stating that SSL certificates have expired. This won't get in the way of functionality, but it will
  stop log messages from being printed to the IDE console. You can solve this issue by manually removing the expired certificates from the Payara domain, as 
  explained [here](https://github.com/payara/Payara/issues/3038).
* If you restart the application a few times, you will run into a bug causing a spurious deployment failure. While the problem can be annoying, it's harmless.
  Just re-run the application (make sure to completely shut down Payara first).
* Sometimes when the server is not shut down correctly or there is a locking/permissions issue, the Derby database that 
  the application uses get's corrupted, resulting in strange database errors. If 
  this occurs, you will need to stop the application and clean the database. You 
  can do this by simply removing \tmp\cargo-tracker-database from the file 
  system and restarting the application.
