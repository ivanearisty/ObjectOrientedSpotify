
![Architecture](ResearchResources/Architechture.drawio.png)

We will be following a DDD model. Knowledge is based on these articles:

- https://en.wikipedia.org/wiki/Multitier_architecture
- https://en.wikipedia.org/wiki/Business_logic
- https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/infrastructure-persistence-layer-design
- https://en.wikipedia.org/wiki/Domain-driven_design
- https://softwareengineering.stackexchange.com/questions/330428/ddd-repositories-in-application-or-domain-service
- https://softwareengineering.stackexchange.com/questions/323686/what-layer-should-contain-interactions-with-external-or-remote-resources-which-a
- https://medium.com/@enocklubowa/why-you-need-to-use-dtos-in-your-rest-api-d9d6d7be5450#:~:text=The%20Domain%20layer%20defines%20the,by%20the%20underlying%20Domain%20object.
- https://ademcatamak.medium.com/layers-in-ddd-projects-bd492aa2b8aa
- https://www.geeksforgeeks.org/application-layer-in-osi-model/
- https://wiki.c2.com/?EntityBeansAsDomainObjects
## The four layers
1. **Presentation**  
The Presentation Layer serves as the interface for interactions with external systems, acting as the gateway for real user inputs that affect the domain. It accepts requests and shapes responses, facilitating communication between the system and users.   
In our project, this layer uses Controllers for HTTP interactions. All of our Angular components live here.  
Contains our `Resource` package.


2. **Application**:  
   Mediates between the various user interface components on a GUI screen and translates the messages that they understand into messages understood by the objects in the domain model.
   The application layer must only coordinate tasks and must not hold or define any domain state (domain model). It delegates the execution of business rules to the domain model classes themselves (aggregate roots and domain entities), which will ultimately update the data within those domain entities.  
   Contains our `Service` package.


3. **Domain**:
This is the core of the application. It is the layer where all business rules related to the problem to be solved are included. In this layer; entities, value objects, aggregates, factories and interfaces will take place.  
This layer must completely ignore data persistence details. These persistence tasks should be performed by the infrastructure layer. Therefore, this layer should not take direct dependencies on the infrastructure, which means that Beans in the domain layer will usually be entities.  
**Contains multiple packages:**
   - Domain: represents core business entities.
   - DTO: Contains DTO classes to transfer data between layers. 
   - Enums: Contains enum classes, which may represent domain-specific constants. 
   - Exceptions: Contains custom exceptions used in the project, which are part of the domain's error-handling logic.
   - Queries: Contains SQL queries as strings (which are typically used for data access).


4. **Infrastructure**:  
The infrastructure layer is how the data that is initially held in domain entities (in memory) is persisted in databases or another persistent store. It also includes methods for the domain layer to call for data access.  
This layer will be the layer that accesses external services such as database, messaging systems and email services.  
   **Contains multiple packages:**
   - Repository: Contains classes that are responsible for data access and bridge the gap between the domain and the database.
   - Infrastructure: Contains connections to external APIs like SpotifyAPI, which aligns with accessing external services. 
   - Queries: Contains SQL queries (strings) for data access. While queries are also found in the Domain Layer, they are closely associated with data persistence, making them part of the Infrastructure Layer.