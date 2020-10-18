# Layers

The Cargo Tracker application is layered as illustrated by the following picture.

![](.gitbook/assets/layers.jpg)

As you can see, there are three vertical layers: interfaces, application and domain, each supported by different kinds of infrastructure. In the application, the [package naming](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/main/java/org/eclipse/cargotracker) reflects these layers.

## Interfaces

This layer holds everything that interacts with other systems \(including humans :-\)\), such as REST web services, WebSocket endpoints, web UIs, external messaging end-points and batch processes. It handles interpretation, input validation and translation of incoming data. It also handles serialization of outgoing data, such as HTML, JSON or XML across HTTP to web browsers or web service clients, or DTO classes and distributed facade interfaces for remote clients.

The [org.eclipse.cargotracker.interfaces](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/main/java/org/eclipse/cargotracker/interfaces) package holds all the interface classes for the application. We have two Web UI interfaces - one for [booking](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/main/java/org/eclipse/cargotracker/interfaces/booking) and the other for [tracking](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/main/java/org/eclipse/cargotracker/interfaces/tracking) as well one [REST interface](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/main/java/org/eclipse/cargotracker/interfaces/handling/rest/HandlingReportService.java) and one [scheduled file processor](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/main/java/org/eclipse/cargotracker/interfaces/handling/file/UploadDirectoryScanner.java) for reporting handling events. The web interfaces are written in HTML/JavaScript/Jakarta Faces, the REST interface uses Jakarta REST while the file processor uses @Scheduled/Jakarta Batch. Modern web application frameworks naturally enforce MVC style separation of concerns, so there is not that much heavy lifting you will need to do. In addition, you can enforce a facade layer if necessary \(e.g. your UI team is sufficiently separated from the application services team or you wish to remain strictly agnostic of the UI layer\). Otherwise, you can use simple adapters to adapt domain classes to your UI view. We demonstrate both techniques in the application.

Other than Jakarta Faces, REST and Enterprise Beans, other Jakarta EE technologies typically used in the interface layer are Jakarta EE Security, CDI, Bean Validation, JSON Processing/JSON Binding/XML Binding \(for serialization\), Batch, Messaging and WebSocket.

## Application

The application layer is responsible for driving the workflow of the application, matching the business use cases at hand. These operations are interface-independent and can be both synchronous or asynchronous. This layer is well suited for spanning transactions, high-level logging and security.

Note, the application layer is very thin in terms of domain logic - it merely coordinates the domain layer objects to perform the actual work. This is very different from traditional tiered architectures that tend to look more procedural with a lot of business logic in the application layer.

The [org.eclpse.cargotracker.application](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/main/java/org/eclipse/cargotracker/application) package contains all the application classes. The application layer is typically implemented using Enterprise Beans or CDI beans. Other Jakarta EE APIs likely to be used in this layer are Jakarta EE Security, Interceptors, Transactions \(in very rare situations where you would like to manage transactions yourself\) and Concurrency.

## Domain

The domain layer is the heart of the software, and this is where the interesting stuff happens. There is one package per aggregate and to each aggregate belongs entities, value objects, domain events, a repository interface and sometimes factories \(see the Characterization section for details\). The aggregate roots are [Cargo](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/main/java/org/eclipse/cargotracker/domain/model/cargo/Cargo.java), [HandlingEvent](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/main/java/org/eclipse/cargotracker/domain/model/handling/HandlingEvent.java), [Location](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/main/java/org/eclipse/cargotracker/domain/model/location/Location.java) and [Voyage](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/main/java/org/eclipse/cargotracker/domain/model/voyage/Voyage.java).

The core of the business logic, such as determining whether a handling event should be registered and how the delivery of a cargo is affected by handling, belongs here. The structure and naming of aggregates, classes and methods in the domain layer should follow the real world as closely as possible, and you should be able to explain to a business domain expert how this part of the software works by drawing a few simple diagrams and using the actual class and method names of the source code. Note that it is not uncommon for some domain classes to simply model data and not contain any business logic.

The domain layer is usually implemented primarily using JPA and CDI.

## Infrastructure

In addition to the three vertical layers, there is also the infrastructure. As the picture shows, it supports all of the three layers in different ways, sometimes facilitating communication between the layers. In the most common case the infrastructure layer consists of Repository implementations interacting with a database. In simple terms, the infrastructure consists of everything that exists independently of our application: external/third-party resources, persistence/database engines, messaging back-ends, legacy systems, and so on.

All of the infrastructure classes are in the [org.eclipse.cargotracker.infrastructure](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/main/java/org/eclipse/cargotracker/infrastructure) package. We use CDI, JPA, JMS and the JAX-RS client API in this layer. The typical application will mostly have JPA EntityManager code in this layer encapsulated with thin CDI bean Repository classes. Other Java EE APIs commonly used in this layer includes EJB clients, WebSocket clients and JAX-WS clients.

## Are Layers Mandatory?

Very few things in DDD are mandatory, layering is no exception. You should use layers if you believe they add value in keeping your application maintainable. Indeed even if you do choose to use layers, you do not need to use them everywhere in your application as an absolute requirement. If you look closely in the application as well as the opening image in this section, it is perfectly OK to skip layers per use case. For example, it is quite common for the Interface layer to skip the Application layer and communicate directly with the Domain layer. In many other cases, having a layer will clearly add value by separating concerns, improving testability, reducing coupling and enhancing semantic clarity. A helpful example of this is the [BookingService](https://github.com/eclipse-ee4j/cargotracker/tree/master/src/main/java/org/eclipse/cargotracker/application/internal/DefaultBookingService.java) implementation.
