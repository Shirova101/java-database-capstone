# Schema Architecture

## Section 1: Architecture Summary

The Smart Clinic Management System follows a three-tier web application architecture built using Spring Boot. The architecture separates responsibilities into presentation, application, and data layers to improve maintainability, scalability, and deployment flexibility.

The presentation layer supports both server-rendered pages and REST-based integrations. Thymeleaf templates are used for Admin and Doctor dashboards, while REST APIs are exposed for modules such as appointments, patient dashboards, and patient records.

The application layer contains controllers, services, and business logic. Controllers receive requests and delegate processing to services, which enforce validation and business rules before interacting with repositories.

The data layer uses two databases for different storage needs. MySQL stores structured relational data including patients, doctors, appointments, and administrators using Spring Data JPA. MongoDB stores flexible document-based prescription records using Spring Data MongoDB.

This design supports modular development, easier testing, CI/CD compatibility, containerized deployment, and future scalability.

---

## Section 2: Numbered Flow of Data and Control

1. Users interact with the application through Thymeleaf dashboards or REST API clients.

2. Incoming requests are routed to the appropriate controller based on URL paths and HTTP methods.

3. Controllers delegate processing to the service layer where business logic and validation are applied.

4. Services communicate with repositories to perform data access operations.

5. MySQL repositories manage structured relational entities while MongoDB repositories manage flexible document data.

6. Retrieved data is mapped into Java application models using JPA entities and MongoDB document models.

7. Processed data is returned either as rendered HTML pages through Thymeleaf or serialized JSON responses through REST APIs.
