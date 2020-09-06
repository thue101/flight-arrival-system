## Over view of the Project

#### Flight Arrival System Project

######It's a Spring Boot project that takes a conversion over configuration approach to Spring application which means we will have minimal setup as it makes it easy to get our application off and running.
######Using embedded MongoDB that stores lists of flights and arrival times. The Embedded database is ideal for testing, and it does not require you to set up an external database. The data is queried using Spring Data JPA.

Rather than querying a database directly, the application code communicates with a repository interface. This interface will have an implementation with data access code.

The repository interface provides with an interoperable data access abstraction.

Spring data generates all the boilerplate for the system.

Through the Paging And Sorting Repository we use the basic CRUD operation.

Advanced queries are derived. Paging and sorting are abstracted.

A transaction method is created and verified by triggering a rollback

