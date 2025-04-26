
# Resiliency Patterns

Resilience patterns are critical in building robust applications that can handle failures gracefully. These patterns help ensure that the system remains functional under various failure conditions, providing stability and a better user experience.

By applying these patterns, we can avoid application crashes, minimize downtime, and improve overall system reliability — especially in **distributed systems**.

This project demonstrates several resilience patterns using **Java 21** and **Spring Boot**.

> 💬 *"Failures happen. Deal with it."* — Michael T. Nygard, *Release It!*

---

## 🚀 How to Run

### 🔧 Prerequisites

- Java 21

### ▶️ Running the Project

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/resiliency-patterns.git
   cd resiliency-patterns
   ```

2. Build and run the application:

   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🧱 Implemented Patterns

| Pattern          | Status   | Description                                       |
|------------------|----------|---------------------------------------------------|
| Retry            | ✅ Done  | Automatically retries failed operations           |
| Circuit Breaker  | ✅ Done | Prevents cascading failures by opening a circuit |
| Timeout          | 🔜 Coming Soon | Limits how long operations can block             |
| Bulkhead         | 🔜 Coming Soon | Isolates failures to specific components         |
| Fallback         | 🔜 Coming Soon | Provides default responses on failure            |

---

## 📌 Pattern 1: Retry

The **Retry** pattern is used to handle **temporary failures** when executing an operation. Instead of failing immediately, the application **retries** a defined number of times before giving up.

### 🔹 Features

- ✅ Automatic Retry
- ✅ Exponential Backoff
- ✅ Recovery Method (`@Recover`)

### 🧪 How to Test

Call the retry endpoint:

```bash
curl http://localhost:8080/retry
```

If the operation fails multiple times, the logs will show retry attempts with **increasing delays** between each try.

### 💡 Configuration Overview

```java
@Retryable(
    maxAttempts = 3,
    value = RuntimeException.class,
    backoff = @Backoff(delay = 1000, multiplier = 2.0)
)
public String unstableMethod() {
    // simulated failure
}
```

- `maxAttempts = 3`: Tries up to 3 times.
- `value = RuntimeException.class`: Retries only on that exception.
- `backoff`: Implements **exponential backoff** (1s → 2s → 4s).

### 🛠 Recovery Strategy

When all attempts fail, a fallback is triggered using `@Recover`:

```java
@Recover
public String recoverMethod(RuntimeException e) {
    return "Definitive failure after multiple attempts. Providing alternative response.";
}
```

This ensures the application degrades gracefully, instead of propagating the failure.

---

## 📚 References

- *Release It!* – Michael T. Nygard
- *Designing Data-Intensive Applications* – Martin Kleppmann
- [Spring Retry Documentation](https://docs.spring.io/spring-retry/docs/current/reference/html/)

---

## 📌 Pattern 2: Circuit Breaker

The **Circuit Breaker** pattern is a defensive mechanism that helps prevent a system from repeatedly trying operations that are likely to fail.
It works by monitoring the outcome of operations and "opening the circuit" if failures reach a threshold — temporarily blocking requests to give the system time to recover.

### 🔹 Features

- **Automatic transition between states**: Closed → Open → Half-Open.
- **Failure threshold**: Opens the circuit after a defined number of failures.
- **Recovery testing**: Allows some requests to pass through in Half-Open state to check if the service has recovered.

### 🧪 How to Test

Call the retry endpoint:

```bash
curl http://localhost:8080/circuit-breaker
```

- If the service fails several times, the Circuit Breaker will move to Open state.
- While Open, new requests will immediately fail, protecting the system.
- After a wait time, it will transition to Half-Open to test recovery.

You can monitor the current state via the Actuator endpoint:

```bash
curl http://localhost:8080/actuator/metrics/resilience4j.circuitbreaker.state
```

### 📖 Code Explanation
In the *UnstableCBService*, the operation is protected by the *@CircuitBreaker* annotation:

```java
@CircuitBreaker(name = "unstableCBService", fallbackMethod = "fallbackMethod")
public String unstableMethod() {
   // implementation
}
```

Key parts:


| **Annotation Element**            | **Explanation**                                                |
|-----------------------------------|----------------------------------------------------------------|
| name = "unstableCBService"        | Links the method to a specific Circuit Breaker configuration   |
| fallbackMethod = "fallbackMethod" | Defines an alternative method to call when the circuit is open |

When the operation consistently fails, the fallback method is triggered:

```java
public String fallbackMethod(Exception e) {
   return "Circuit breaker is open. Returning fallback response.";
}
```
This ensures the application responds gracefully even during service disruptions.

### 🔎 Circuit States Overview

| **State**     | **Description**                                                    |
|---------------|--------------------------------------------------------------------|
| **Closed**    | Everything is working normally.                                    |
| **Open**      | Failures exceeded the threshold; requests are blocked temporarily. |
| **Half-Open** | Some requests are allowed to check if recovery is possible.        |

### ⚠️ Best Practices

- Configure thresholds carefully to avoid premature circuit opening.
- Use proper timeout settings to avoid waiting too long on failing services.
- Always provide meaningful fallback responses to maintain user experience.

### Properties configuration information
Here is a extra information about how I did the properties configuration:

| Property | Description | 
|---------------|-----------------------------------------------------------------------|
| sliding-window-type=COUNT_BASED | Watch number of calls, not time |
| sliding-window-size=5 | Evaluates the last 5 calls |
| minimum-number-of-calls=5 | Only decide to open after having at least 5 calls |
| failure-rate-threshold=50 | If 50% (or more) of calls fail, it opens |
| wait-duration-in-open-state=5s | It stays open for 5 seconds before trying to close again |
| permitted-number-of-calls-in-half-open-state=2 | Allows 2 test calls in half-open |
| slow-call-rate-threshold=100 | If 100% of calls are slow, that also counts as a problem |
| slow-call-duration-threshold=2s | Consider a call slow if it takes more than 2 seconds |
---

## 📅 What's Next?

More patterns coming soon:
- Bulkhead isolation techniques
- Timeout using CompletableFuture and WebClient
- Fallback strategies

Stay tuned! 💥
