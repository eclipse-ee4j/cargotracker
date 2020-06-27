# Java EE and DDD

As the name implies, Domain-Driven Design is an approach to software design and development that focuses on the core domain and domain logic. The domain is implemented through a careful focus on traditional OOAD \(Object Oriented Analysis and Design\) and modeling the real world problem the software is trying to solve as closely as possible.

The basic building blocks of the domain are entities, value objects, aggregates, services, repositories and factories. The [Characterization](characterization.md) section overviews how these concepts are implemented in the application using Java EE. Logical layers partitioning distinct concerns are super-imposed on the core concept of the domain. These layers generally consist of the UI/interface layer, the application layer, the domain layer \(of course!\) and the infrastructure layer respectively. The [Layers](layers.md) section explains the architectural layers in the application and how they relate to various Java EE APIs.

![](.gitbook/assets/ddd-diagram.png)

