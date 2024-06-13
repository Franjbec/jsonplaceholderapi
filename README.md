# JSONPlaceholder API Project

## Description

This project is a RESTful service that interacts with the JSONPlaceholder API to perform CRUD (Create, Read, Update, Delete) operations on `Post` and `Comment` resources. It uses Java 17, Spring Boot, and Maven 3.6.3 or higher. Additionally, API documentation is available via Swagger.

## Technologies Used

- **Java 17**
- **Spring Boot**
- **Maven 3.6.3 or higher**
- **WebClient for non-blocking calls**
- **Jackson and Jackson XML for serialization and deserialization**
- **Lombok to reduce boilerplate code**
- **Springdoc OpenAPI for API documentation**

## Decisions Made During Development

- **WebClient**: The `WebClient` class is used to interact with the JSONPlaceholder API. It is a non-blocking, reactive client that is part of the Spring WebFlux module. It is a good choice for making HTTP requests to external APIs.
- **Data extraction**: For simplicity, data has been extracted and save in both JSON and XML files in the main directory of the project.
- **Exception handling**: Common exceptions, such as `NotFoundException`, are centrally handled using `@ControllerAdvice`.
- **API documentation**: Interactive API documentation is available via Swagger at the following URL: `http://localhost:8080/swagger-ui.html`.
- **Logging**: Logging is done using SLF4J and Logback. The log level can be adjusted using the `logging.level.*` properties.
- **Testing**: Unit tests are written using JUnit 5 and Mockito. The tests are located in the `src/test` directory.

## Future Improvements

- **Database**: The application can be extended to use a database to store the posts and comments retrieved from the fake API and be more accessible.
- **Message Queues**: Implement message queues like Kafka to handle asynchronous processing of requests and send retrieved data to other services.
- **Caching**: Implement caching to reduce the number of requests made to the JSONPlaceholder API.
- **Security**: Implement security features to protect the API endpoints.
- **Docker**: Create a Dockerfile to containerize the application.
- **Error handling**: Implement more detailed error handling and logging.
- **Pagination**: Implement pagination to manage large datasets.
- **Testing**: Add more unit tests and integration tests to improve test coverage.
- **Documentation**: Improve the documentation to make it more comprehensive and user-friendly usin Javadoc for example.
- **Refactoring**: Refactor the code to improve readability, maintainability, and scalability.
- **Logging**: Add more detailed logging to track the application's behavior and performance.

## Configuration and Execution Instructions

1. **Build the project**:
   ```sh
   mvn clean install

2. **Run the application**:
   ```sh
   mvn spring-boot:run
   
3. **Access the application**:

    You can access ant test the application at the following URLs:
    - **URL**: `http://localhost:8080`
    - **Swagger**: `http://localhost:8080/swagger-ui.html`


Notes: Exception Handling
Common exceptions, such as NotFoundException, are centrally handled using @ControllerAdvice.

## Available Operations

### Posts

#### Get Posts

- **Description**: Fetches a list of posts, optionally filtered by `userId` and/or `title`.
- **URL**: `/posts`
- **HTTP Method**: GET
- **Parameters**:
    - `userId` (optional)
    - `title` (optional)

#### Get Post by ID

- **Description**: Retrieves a specific post by its ID.
- **URL**: `/posts/{id}`
- **HTTP Method**: GET
- **Parameters**:
    - `id` (required)

#### Create Post

- **Description**: Creates a new post.
- **URL**: `/posts`
- **HTTP Method**: POST
- **Body**: JSON object with post details

#### Update Post

- **Description**: Updates an existing post by its ID.
- **URL**: `/posts/{id}`
- **HTTP Method**: PUT
- **Parameters**:
    - `id` (required)
- **Body**: JSON object with updated post details

#### Partially Update Post

- **Description**: Partially updates an existing post by its ID.
- **URL**: `/posts/{id}`
- **HTTP Method**: PATCH
- **Parameters**:
    - `id` (required)
- **Body**: JSON object with fields to modify in the post

#### Delete Post

- **Description**: Deletes a post by its ID.
- **URL**: `/posts/{id}`
- **HTTP Method**: DELETE
- **Parameters**:
    - `id` (required)

### Comments

#### Get Comments

- **Description**: Fetches a list of comments for a specific post, optionally filtered by `commentId`.
- **URL**: `/posts/{id}/comments`
- **HTTP Method**: GET
- **Parameters**:
    - `id` (required)
    - `commentId` (optional)

### Fetch and Save Data

#### Fetch and Save Posts

- **Description**: Fetches a list of posts (optionally filtered by `userId` and/or `title`) and saves them as JSON and XML files.
- **URL**: `/fetch-and-save`
- **HTTP Method**: GET
- **Parameters**:
    - `userId` (optional)
    - `title` (optional)

## API Documentation

Interactive API documentation is available via Swagger at the following URL:

- **URL**: `http://localhost:8080/swagger-ui.html`

