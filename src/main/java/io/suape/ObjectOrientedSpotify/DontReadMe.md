# How we think about creating a java backend

## Elements for Making a Cohesive Spring Boot Backend 

## The important ones

### Domain

The Domain package is the heart of business entities. It represents the core concepts of an app in an object-oriented way.

> Entities such as `User`, `Playlist`, `Track` embody the business model. These classes should be designed to encapsulate business logic and rules, making your application maintainable and scalable.

You can think of the domain as declaring the objects that we are going to use in the application.

### Service

The Service layer contains the application's business logic.

It is sequential and defines the set of available operations of the app.

Its main job is to coordinate the application's response in each operation, acting as a bridge between the Repository and the Controller layers.

> Services like `UserService` handle complex business operations, ensuring that your controllers remain lean and focused only on handling HTTP requests and responses.

### Domain vs Service

The Domain contains information and functionality related to what it means to be a User. 
It should map conceptually onto something that exists physically in the real world or a clearly defined concept in the problem space.

The Service contains information and functionality related to performing atomic units of work. 
It should map conceptually onto tasks that are performed on or by members of the Domain model. 
A single atomic task performed by clicking a button in the application generally involves many members of the Domain working together.

### Repository

The Repository layer abstracts the data access logic from the business logic.

> It is responsible for data retrieval, storage, and update operations. By defining interfaces like `UserRepository`, the application adheres to the Dependency Inversion Principle, making it easier to unit test and maintain.

The repository should allow the seamless communication with the database; however, the frontend should not know how the information is stored.

The repository implementation that is injected will handle all the methods, like inserting, but it could be inserting in different places or using different databases.

### Resource

Resources are essentially the controllers in the MVC pattern, defining the endpoints of any RESTful API.

> `UserResource` would expose CRUD operations for `User` entities. This layer is key in defining the API's contract and ensuring it aligns with REST principles and best practices.

In essence, the frontend or just users will communicate with the backend by using Controller endpoints.

These endpoints should always call some function in the Service layer to perform operations

> Resources never perform atomic tasks or business logic; they ask for it to be performed

## Adjacent and Necessary, but not part of the real Domain Driven Design Model

### Configuration

This package centralizes all configuration-related classes, enabling a single point of reference for application settings.

> It includes classes like `SecurityConfig`, which configure application security, setting up authentication and authorization mechanisms. It’s essential for managing various environments (development, staging, production) and handling application-wide settings like CORS or database configurations.

### Principal in Domain 

A `Principal` is a service like entity, that can be placed in the `Domain` layer for security verifications.

In spring, an `AuthenticationManager` will ask an `AuthenticationProvider` to provide authentication for something.

The something needs to be authenticated in some way, and that way will use a `Principal`.

> All that `Principal` does is implement a `DetailsService` that takes in a `Domain Entity` and returns things like authorizations, credentials, and boolean traits.

For example, UserDetails is returned by the UserDetailsService. The DaoAuthenticationProvider validates the UserDetails and then returns an Authentication that has a principal that is the UserDetails returned by the configured UserDetailsService.

**UserDetailsService = UserPrincipal**

### DTO (Data Transfer Objects)

DTOs serve as a layer of abstraction between the internal representation of data and what is presented to or received from external interfaces.

> `UserDTO` and other DTOs ensure that sensitive information is not exposed to the client. `DTOMapper` is crucial for converting domain models to DTOs and vice versa, ensuring loose coupling and single responsibility principles.

You would not want to send a `User` class which contains a `password` field to the client side. 

### Enums

Enums provide a type-safe way to handle a fixed set of constants.



### Form

Forms are specialized DTOs used for validating and transporting user input.

> Classes like `LoginForm` ensure that user input is valid before it even reaches the business logic, significantly reducing the risk of invalid data affecting the system.

### Handler

Handlers in this context manage exceptional or specific scenarios in the application.

Spring has a few security interfaces and we can send a 403 error message by implementing access denied, for example.

> `CustomAccessDeniedHandler` and `CustomAuthenticationEntryPoint` are examples that might manage security exceptions, providing custom responses or behaviors when access is denied or when authentication is required.

### Infrastructure

This package includes classes that provide a bridge between your application and external systems or services.

### OOSExceptions (Object Oriented Spotify Exceptions)

Custom exceptions help in precise error handling and providing more context when exceptions occur.

> `APIException` could be tailored to provide detailed information about failures in API calls. This practice aids in debugging and improves the application's reliability by handling known error conditions gracefully.

### Queries

This package can contain custom query definitions, usually for complex database operations.

This aids in code organization, and the re-usability of query logic.

### RowMapper

RowMapper implementations convert rows from a ResultSet into domain objects.

> `UserRowMapper` maps SQL results to the `User` entity, ensuring a clean separation between the database schema and the domain model.

This structured approach to package organization helps maintain a clear separation of concerns, making your Spring Boot application easier to understand, maintain, and scale.

## How we go about creating Functionality

All the heavy lifting should happen in the repository.

Say we wanted to implement a text message functionality for a `User`:
1. First step would be to declare an `HTTPResponse` in the `Resource` of `User`
2. Second, the `userResource` should communicate with the `userService` and say to the service:
    > `userService.sendTextMessage(user)`: where userService is injected to Resource 
3. The `userService` should cascade down the heavy lifting to the `Repository`
    > `userRepository.sendTextMessage(user)`: where userRepository is injected to Service
5. Finally, the `userRepository` will implement the actual method by communicating with infrastructure and doing complicated logic that achieves such functionality

This procedure allows for the utilization of different repositories by the business entities.

Remember, they don't have to know how they end up getting a text message, they only care that they did and that they get a standardized response.

So, to recap: 
1. Resource is the access point and communicates with the internet
2. Service is business logic of what needs to happen sequentially
3. Repository is relatively dumb and only implements functionality of real code  

## Annotations with Spring Boot

In web application development, particularly in Java-based frameworks like Spring Boot, there are several common annotations used to simplify and streamline the process. These annotations play a critical role in handling requests, injecting dependencies, configuring beans, and more. Here are some of the most commonly used annotations in the context of web applications:

### Spring MVC and REST Annotations

#### @Controller
- Marks a class as a web controller, capable of handling requests. Used in Spring MVC for web pages.

#### @RestController
- A specialized version of `@Controller` for RESTful web services. It includes `@ResponseBody` to indicate that the return value of the methods should be bound to the web response body.

#### @RequestMapping
- Maps HTTP requests to handler methods of MVC and REST controllers. It can be configured with specific HTTP methods, URIs, and other request parameters.

#### @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PatchMapping
- Specializations of `@RequestMapping` for specific HTTP methods. They simplify the mapping of HTTP GET, POST, PUT, DELETE, and PATCH requests to specific handler methods.

#### @ResponseBody
- Indicates that the return value of a method should be used as the response body directly, commonly used in REST APIs.

#### @RequestBody
- Binds the HTTP request body to a method parameter, typically used for deserializing incoming JSON or XML data.

#### @PathVariable
- Used to extract values from the URL. It’s commonly used in RESTful web services to handle dynamic URIs.

#### @RequestParam
- Binds request parameters to a method parameter in your controller. Useful for handling query parameters in URLs.

#### @RequestHeader
- Allows a method parameter to be bound to an HTTP header.

### Spring Boot Configuration and Dependency Injection Annotations

#### @SpringBootApplication
- Used to mark the main class of a Spring Boot application. It combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan` annotations.

#### @Bean
- Indicates that a method produces a bean to be managed by the Spring container.

#### @Autowired
- Marks a constructor, field, or setter method to be autowired by Spring's dependency injection facilities.

#### @Component
- Marks a class as a Spring component. It’s a generic stereotype for any Spring-managed component. `@Service`, `@Repository`, and `@Controller` are specializations of `@Component`.

#### @Service
- Indicates that a class is a service component. It’s used to mark service layer classes.

#### @Repository
- Marks a class as a repository, which is an abstraction of data access and storage.

#### @Configuration
- Indicates that a class declares one or more `@Bean` methods and may be processed by the Spring container.

#### @Profile
- Indicates that a component is eligible for registration when one or more specified profiles are active.

### Spring Security Annotations

#### @EnableWebSecurity
- Used to enable Spring Security’s web security support and provide the Spring MVC integration.

#### @PreAuthorize / @PostAuthorize
- Used for method security. `@PreAuthorize` is used to check authorization before entering a method, whereas `@PostAuthorize` is used after the method execution.

### Validation Annotations

#### @Valid
- Used to activate validation for a particular parameter, typically used on a request body in a controller.

#### @NotNull, @NotEmpty, @Size, etc.
- Annotations from the Java Bean Validation API used to enforce constraints on your model attributes.

These annotations are integral in developing web applications, providing a declarative way to define the behavior of various components and handling the HTTP request-response lifecycle. They are key to the efficiency and readability of the code in modern Java-based web development.

## Annotations with Lombok

Lombok is a Java library that plugs into your editor and build tools, spicing up your Java. It reduces boilerplate code in Java applications, allowing developers to focus more on the business logic rather than mundane, repetitive tasks. Below are some key annotations from Lombok and their use cases:

### @Data
- This annotation generates all the boilerplate that is normally associated with simple POJOs (Plain Old Java Objects): getters for all fields, setters for all non-final fields, `toString`, `equals`, and `hashCode` implementations. It simplifies the model/entity class definitions significantly.

### @SuperBuilder
- `@SuperBuilder` is an advanced version of `@Builder`, providing an automatic implementation of the builder pattern for classes and their superclasses. It is particularly useful in complex inheritance hierarchies and ensures type-safe object construction.

### @NoArgsConstructor
- This annotation generates a constructor with no parameters. It is particularly useful for entities and POJOs as many libraries and frameworks (like JPA, Jackson, etc.) require a default constructor for operation.

### @RequiredArgsConstructor
- Generates a constructor with 1 parameter for each field that requires special handling. It mainly applies to fields that are final and non-initialized. This annotation ensures that mandatory properties are set during object creation.

### @Getter and @Setter
- `@Getter` and `@Setter` generate the getter and setter methods for your fields, respectively. They can be applied at the class level or individually on fields.

### @ToString
- `@ToString` generates an implementation of the `toString` method. It can include or exclude certain fields and can be customized to show field names or use a custom format.

### @EqualsAndHashCode
- This annotation generates both `equals` and `hashCode` methods based on the fields of the class. It is useful for objects that need to be compared based on their values.

### @AllArgsConstructor
- `@AllArgsConstructor` generates a constructor with one parameter for each field in your class. This is particularly useful when you want to create an object by setting all its fields at once.

### @Value
- `@Value` is used for immutable objects and combines the functionalities of `@Getter`, `@ToString`, `@EqualsAndHashCode`, `@AllArgsConstructor`, and `@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)`. It is ideal for creating immutable classes.

### @Builder
- `@Builder` implements the Builder pattern for object construction. This annotation is especially useful for classes with many fields, providing a clean and user-friendly way to create objects.

### @Slf4j, @Log4j, @Log4j2, @Log
- These annotations auto-generate a logger field for the class. `@Slf4j` is for the SLF4J logger, while `@Log4j` and `@Log4j2` are for Log4j and Log4j2 respectively. `@Log` is for Java Util Logging.

### @NonNull
- `@NonNull` can be used on a field or parameter to have Lombok automatically generate a null-check statement. It is useful for methods and constructors where null values are not acceptable.

### @Synchronized
- `@Synchronized` is a safer alternative to the synchronized keyword in Java. It synchronizes on a private field, reducing the risk of accidental deadlocks.

### @Cleanup
- `@Cleanup` ensures that resources are automatically closed after use. It can be applied to variables to automatically call their `close` method at the end of the scope.

### @With
- `@With` generates `withAttributeName` methods for each field in the class. This method returns a clone of the object with the specified field changed, which is useful for creating modified copies of immutable objects.

These annotations from Lombok significantly improve code clarity and reduce the amount of repetitive code, enabling developers to focus more on the business logic rather than the boilerplate code.
are used to map web requests onto specific handler classes and/or handler methods. They are essential in defining the routes and methods (GET, POST, etc.) for RESTful services.

## Spring Security

### Filters

Every request that comes into the app will come in to spring security filters. 

Filters will parse the request and before they reach the controller methods they will need to be authorized by the controller methods.

High level, filters secure the application by preventing requests from reaching controllers.

### Authentication Manager

Manager figures out, for each request, which authentication provider it needs to give a specific request to.

It basically goes to authentication providers and asks them if they can support the authentication that was provided to them.

Default: `ProviderManager`

### Authentication Provider 

Receives information from the manager and sends information to the User Detail Service to authenticate any specific user.

Default `DoaAuthenticationProvider`

### User detail service

Authenticates a specific user by checking the request against actual user data, like session cookies or passwords.

We usually change this so that Spring Security can authenticate against the users that we actually have in the app.

Default `InMemoryUserDetailsManager`

### Tokens

We will give users a refresh and access token so that users can log into the frontend and send information to the backed and get authenticated

### Security Filter Chain 

We usually use the username password authentication filter to authenticate a user, 
we usually want to make sure that we place our filters before this one.

> The order of the filters is important, spring security has a standard ordering

> There is a lot of implementing classes for a lot of different protocols that handle exceptions like JWT and 0AUTH and such.

In our app we started with custom authentication, but hoping to get to 0auth


```java
class Example{
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    http.csrf().disable().cors().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.authorizeHttpRequests().requestMatchers(PUBLIC_URLS).permitAll();
    //NOTE: if user is not enabled or locked this will explode
    http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customAuthenticationEntryPoint);
    http.authorizeHttpRequests().anyRequest().authenticated();
    return http.build();
    }
}
```
> HttpSecurity is similar to Spring Security's XML <http> element in the namespace configuration. It allows configuring web based security for specific http requests. By default it will be applied to all requests, but can be restricted using #requestMatcher(RequestMatcher) or other similar methods.

