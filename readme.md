# Resiliency Patterns

Resilience patterns are critical in building robust applications that can handle failures gracefully. These patterns help ensure that the system remains functional under various failure conditions, providing stability and a better user experience. By using resilience patterns, we can avoid application crashes, minimize downtime, and improve overall system reliability.

This project demonstrates several resilience patterns using Spring Boot.

### 🚀 How to Run
#### Prerequisites
1. Java 21

#### Running the Project

1. Clone the repository:

`> git clone https://github.com/your-username/resiliency-patterns.git`

`> cd resiliency-patterns`

2. Build and run the application:

`> mvnw spring-boot:run`
---


## 📌 1. Retry

Retry is a resilience pattern used to handle temporary failures when executing an operation. Instead of failing immediately, the application retries a defined number of times before giving up.

### 🔹 Implemented Features:

- **Automatic Retry**: The service automatically retries the operation up to a maximum number of attempts.
- **Exponential Backoff**: The wait time between attempts increases exponentially to reduce system overload.
- **Recovery Method (Recover)**: If all retry attempts fail, an alternative method is triggered to handle the failure in a controlled manner.


Access the API to test the retry mechanism:

curl http://localhost:8080/api/retry

If the operation fails multiple times, the logs will display the retry attempts with progressively increasing wait times.

📖 Code Explanation

The Retry mechanism is configured in the UnstableService class using the @Retryable annotation:

@Retryable(
maxAttempts = 3,
value = RuntimeException.class,
backoff = @Backoff(delay = 1000, multiplier = 2.0)
)

How does this configuration work?

maxAttempts = 3: The method will be retried up to 3 times before failing.

value = RuntimeException.class: Only failures of this type trigger the retry mechanism.

backoff = @Backoff(delay = 1000, multiplier = 2.0): Defines a progressive wait time between retry attempts:

First failure → waits 1 second

Second failure → waits 2 seconds

Third failure → waits 4 seconds

If all attempts fail, the recoverMethod is called:

@Recover
public String recoverMethod(RuntimeException e) {
return "Definitive failure after multiple attempts. Providing alternative response.";
}

This method provides an alternative response to prevent uncontrolled failures in the application.