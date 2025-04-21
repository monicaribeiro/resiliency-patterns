
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
| Circuit Breaker  | 🔜 Coming Soon | Prevents cascading failures by opening a circuit |
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
curl http://localhost:8080/api/retry
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

## 📅 What's Next?

More patterns coming soon:
- Circuit Breaker with Resilience4j
- Timeout using CompletableFuture and WebClient
- Bulkhead isolation techniques
- Fallback strategies

Stay tuned! 💥
