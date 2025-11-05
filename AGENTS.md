# Agent Instructions

## Java Conventions

Follow these Java conventions when working with this codebase.

### Variables and Types

Choose type declarations that make the code's intent immediately clear to readers. While `var` reduces verbosity,
explicit types often communicate intent more effectively.

#### Type Declarations

Use `var` for local variable declarations ONLY when the type is immediately obvious from the
right-hand side. When in doubt, explicit types improve clarity and maintainability.

**Decision Checklist:**

- ✓ Can you tell the exact type in 1 second? → Use `var`
- ✗ Would you need to check documentation or method signatures? → Use explicit type
- ✗ Is the type generic, an interface, or complex? → Use explicit type
- ✗ Is the variable used far from its declaration? → Use explicit type
- ✗ Does the explicit type reveal important semantic information? → Use explicit type

**What counts as "obvious from the right-hand side":**

- Constructor calls with concrete types: `new ArrayList<String>()`, `new User(...)`
- Literals: strings, numbers, booleans, `null`
- Collection factory methods with only literals: `List.of(1, 2, 3)`, `Map.of("key", "value")`
- Standard library methods with obvious return types: `isEmpty()`, `size()`, `toString()`
- Builder patterns that return the same type: `User.builder().name("John").build()`

```java
// Good: Type is clear from the right-hand side
var list = new ArrayList<String>();
var name = "John";
var count = 42;
var user = new User(id, name, email);
var isEmpty = list.isEmpty();
var items = List.of("a", "b", "c");

// Good: Explicit type when not obvious
InputStream stream = getStream();
Result<User> result = repository.getUser(id);
Function<String, Integer> parser = Integer::parseInt;
List<Item> items = Stream.of(item1, item2).collect(toList());

// Good: Explicit type for interface/abstract return types
Map<String, Object> config = loadConfiguration();
Callable<Data> task = () -> fetchData();

// Good: Explicit type for method chains
ProcessedData result = data.transform().normalize();

// Good: Explicit type for factory methods
User user = User.create(name);
Order order = orderService.findById(id);

// Avoid: Unclear type from the right-hand side
var data = process(); // What type is returned?
var result = calculate(); // Not immediately obvious
var callback = createHandler(); // What functional interface?
```

**When in doubt, prefer explicit types.**

### Records

Leverage records as the default choice for immutable data carriers. They provide concise syntax while maintaining type
safety and clarity.

- Prefer record classes for immutable data carriers

  ```java
  record Point(int x, int y) {}
  ```

- Use records instead of classes with only final fields and accessors

- Add custom methods to records when needed for behavior

  ```java
  record Temperature(double celsius) {
      public double fahrenheit() {
          return celsius * 9/5 + 32;
      }
  }
  ```

- Use compact constructors for validation

  ```java
  record User(String name, int age) {
      public User {
          Objects.requireNonNull(name, "name cannot be null");
          if (age < 0) throw new IllegalArgumentException("age must be non-negative");
      }
  }
  ```

### Sealed Classes

Use sealed classes to create explicit, type-safe hierarchies that the compiler can verify exhaustively.

- Use sealed classes to restrict inheritance hierarchies

  ```java
  sealed interface Shape permits Circle, Rectangle, Triangle {}
  ```

- Combine with records for algebraic data types

  ```java
  sealed interface Result<T> permits Success, Failure {
      record Success<T>(T value) implements Result<T> {}
      record Failure<T>(String error) implements Result<T> {}
  }
  ```

- Enable exhaustive pattern matching in switch expressions

  ```java
  String message = switch (result) {
      case Success<String> s -> "Got: " + s.value();
      case Failure<String> f -> "Error: " + f.error();
      // No default needed - compiler verifies exhaustiveness
  };
  ```

### Pattern Matching

Pattern matching eliminates boilerplate and makes type-safe operations more expressive and less error-prone.

- Use pattern matching for `instanceof` checks (JDK 16+)

  ```java
  if (obj instanceof String s) {
      return s.length();
  }
  ```

- Use switch expressions with pattern matching (JDK 17+)

  ```java
  return switch (shape) {
      case Circle c -> c.radius() * c.radius() * Math.PI;
      case Rectangle r -> r.width() * r.height();
  };
  ```

- Combine with sealed types for exhaustive, type-safe handling

  ```java
  sealed interface PaymentMethod permits CreditCard, BankTransfer {}
  
  double processFee(PaymentMethod method) {
      return switch (method) {
          case CreditCard cc -> cc.amount() * 0.029;
          case BankTransfer bt -> 0.0; // No fees
      };
  }
  ```

### Text Blocks

Text blocks improve readability for multi-line strings and eliminate error-prone escape sequences.

- Use text blocks for multi-line strings (JDK 15+)

  ```java
  String json = """
      {
          "name": "value",
          "count": 42
      }
      """;
  ```

- Prefer text blocks for SQL, HTML, JSON, and other formatted text

  ```java
  String sql = """
      SELECT u.id, u.name, o.total
      FROM users u
      JOIN orders o ON u.id = o.user_id
      WHERE o.status = 'COMPLETED'
      """;
  ```

### Null Handling

Validate inputs immediately with specific error messages. Use `Optional` to explicitly signal absence in return values,
never for parameters.

- Use `Optional` for return types that may be absent, **never** for parameters

  ```java
  // Good: Optional return type
  Optional<User> findUserById(String id);
  
  // Bad: Optional parameter
  void updateUser(Optional<String> name); // Don't do this
  
  // Good: Make it explicit with overloads or nullable annotation
  void updateUser(@Nullable String name);
  ```

- Avoid `Optional` fields in classes (use nullable fields instead)

- Use `Objects.requireNonNull()` for parameter validation

  ```java
  public Order(String id, Customer customer) {
      this.id = Objects.requireNonNull(id, "id cannot be null");
      this.customer = Objects.requireNonNull(customer, "customer cannot be null");
  }
  ```

- Provide clear, actionable error messages

  ```java
  // Good: Specific, actionable message
  Objects.requireNonNull(email, "email is required for user registration");
  
  // Bad: Generic message
  Objects.requireNonNull(email);
  ```

### Collections

- Prefer `List.of()`, `Set.of()`, `Map.of()` for immutable collections

- Use `Stream` API for collection transformations when it improves readability

- Avoid streams for simple iterations

### Streams

- Keep stream pipelines short and readable

- Extract complex lambdas to named methods

- Consider performance implications for large datasets

### Modern APIs

- Use `java.time` API for date/time operations, never `java.util.Date`

- Prefer `Files` and `Path` over `File` for file operations

- Use `HttpClient` (JDK 11+) for HTTP operations

### Exception Handling

Throw specific exceptions early with clear messages that help diagnose problems. Don't mask failures with generic catch
blocks.

- Prefer specific exception types over generic ones

  ```java
  // Good: Specific exceptions
  throw new UserNotFoundException("User not found: " + userId);
  throw new InvalidEmailException("Invalid email format: " + email);
  
  // Bad: Generic exception
  throw new Exception("Something went wrong");
  ```

- Validate at boundaries and fail fast

  ```java
  public void processOrder(Order order) {
      Objects.requireNonNull(order, "order cannot be null");
      if (order.items().isEmpty()) {
          throw new IllegalArgumentException("order must contain at least one item");
      }
      // ... proceed with processing
  }
  ```

- Use try-with-resources for `AutoCloseable` resources

  ```java
  try (var reader = Files.newBufferedReader(path)) {
      return reader.lines().collect(toList());
  }
  ```

- Don't catch `Exception` or `Throwable` unless absolutely necessary

  ```java
  // Good: Catch specific exceptions
  try {
      return parseData(input);
  } catch (JsonParseException e) {
      throw new InvalidDataException("Failed to parse JSON", e);
  }
  
  // Bad: Catch too broadly
  try {
      return parseData(input);
  } catch (Exception e) { // Masks all failures
      return null;
  }
  ```

- Include context in exception messages

  ```java
  throw new ConfigurationException(
      "Failed to load configuration from " + configPath + 
      ": missing required property 'database.url'"
  );
  ```

### Naming

- Classes and interfaces: `PascalCase`
- Methods and variables: `camelCase`
- Constants: `UPPER_SNAKE_CASE`
- Packages: lowercase, no underscores
- Test classes: `ClassNameTest` for unit tests, `ClassNameIT` for integration tests

### Code Organization

Design flexible, composable systems using interfaces and composition. Avoid deep inheritance hierarchies that create
rigid coupling.

- Keep methods short and focused on a single responsibility

- Prefer composition to inheritance

  ```java
  // Good: Composition
  class EmailService {
      private final MessageFormatter formatter;
      private final EmailSender sender;
      
      EmailService(MessageFormatter formatter, EmailSender sender) {
          this.formatter = formatter;
          this.sender = sender;
      }
  }
  
  // Avoid: Deep inheritance
  class Service extends BaseService extends AbstractService { }
  ```

- Use interfaces for abstraction, not abstract classes

  ```java
  // Good: Interface-based abstraction
  interface PaymentProcessor {
      Result process(Payment payment);
  }
  
  // Implementations can compose freely
  class StripeProcessor implements PaymentProcessor { }
  class PayPalProcessor implements PaymentProcessor { }
  ```

- Design interfaces that clients need, not what implementations provide

  ```java
  // Good: Client-focused interface
  interface OrderRepository {
      Optional<Order> findById(String id);
      List<Order> findByCustomer(String customerId);
  }
  
  // Avoid: Implementation-focused interface
  interface OrderRepository {
      void save(Order order);
      void delete(Order order);
      Order load(String id);
      List<Order> loadAll();
      // ... exposing storage implementation details
  }
  ```

### Immutability

Design classes and data structures to be immutable unless there's a clear need for mutability. Immutable objects are
inherently thread-safe, easier to reason about, and prevent entire categories of bugs.

- Make classes immutable by default (prefer records for data carriers)

- Use `final` for fields that shouldn't change after construction

- Prefer unmodifiable collections (`List.of()`, `Set.of()`, `Map.of()`)

- Return defensive copies if mutable collections must be exposed

- Consider `@Value` from Lombok or records for immutable value objects

```java
// Good: Immutable record
record User(String id, String name, List<String> roles) {
  // Defensive copy for mutable field
  public User {
    roles = List.copyOf(roles);
  }
}

// Good: Immutable class
public final class Configuration {
  private final String host;
  private final int port;
  private final Map<String, String> properties;

  public Configuration(String host, int port, Map<String, String> properties) {
    this.host = host;
    this.port = port;
    this.properties = Map.copyOf(properties);
  }

  // Getters only, no setters
}
```

### Documentation

- Document public APIs with Javadoc

- Focus on why, not what (code should be self-documenting for "what")

- Keep documentation up to date with code changes

- Javadoc tag descriptions MUST begin with a lowercase letter and MUST end with a period

  ```java
  /**
   * Creates a new connection to the server.
   *
   * @param endpoint the server endpoint URL.
   * @param timeout the connection timeout in milliseconds.
   * @return the established connection.
   * @throws IOException if the connection fails.
   */
  ```

### Other

For any coding practices not explicitly covered by these conventions, defer to established Java best
practices and community standards.

## Maven

### Building and Testing

#### Build/Compile the Project

To compile the project without running tests:

```bash
mvn clean compile
```

#### Run All Tests

To run all tests and verify the project:

```bash
mvn clean verify
```

This command will:

- Clean previous builds
- Compile the code
- Run all unit tests
- Run integration tests (if configured)
- Run code quality checks (like Spotless)

#### Run Specific Tests

To run a specific test class:

```bash
mvn test -Dtest=ClassName
```

To run a specific test method:

```bash
mvn test -Dtest=ClassName#methodName
```

To run multiple test classes:

```bash
mvn test -Dtest=ClassOne,ClassTwo
```

To run tests matching a pattern:

```bash
mvn test -Dtest=*ServiceTest
```

#### Other Common Maven Commands

**Run tests only (skip compilation if already compiled):**

```bash
mvn test
```

**Package the project (creates JAR/WAR file):**

```bash
mvn clean package
```

**Install to local Maven repository:**

```bash
mvn clean install
```

**Run only unit tests (skip integration tests):**

```bash
mvn clean test
```

### Code Formatting

This project uses Spotless with Google Java Format for code formatting.

The `spotless:check` goal is bound to the `verify` phase and will fail the build if code is not
properly formatted.

If the build fails due to formatting issues, run:

```bash
mvn spotless:apply
```

This will automatically format all Java files according to Google Java Format standards.

### Dependency Source Code

To examine dependency source code, check the `external/src` directory at the project root. This
directory contains unpacked source files from all dependencies, organized by package structure for
easy browsing and searching.

**If the directory doesn't exist or content is missing:**

Run this command from the project root to download and unpack all dependency sources:

```bash
mvn generate-resources -Pdownload-external-src
```

This will create the `external/src` directory with sources from all dependencies in a single
top-level location.

## Commit Messages

Keep the title of the commit message to ~72 characters.

Use the body to summarize the changes made in the commit. If the commit contains a number of
unrelated changes, try to generate a brief one-line subject, then summarize using bullet points. Be
concise.

Avoid subjective justification or explanation for changes; state changes on their own merit.

Do not include counts (files, tests, lines, changes, etc.).

Do not include a "generated with Claude Code" line or similar.

## Pull Requests

Do not include a "Test Plan" section in PRs.

Do not mention the build was successful or tests passed.

Do not include counts (files, tests, lines, changes, etc.).

Do not include a "generated with Claude Code" line or similar.
