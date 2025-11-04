# Coding Instructions

## Java Conventions

Follow these Java conventions when working with this codebase.

### Core Tenets

1. **Clarity Over Brevity** — Code should be immediately understandable; use explicit types when
   `var` doesn't make intent obvious
2. **Immutable by Default** — Prefer immutable data structures, records, and final fields unless
   mutability is required
3. **Modern Java First** — Leverage modern Java features (pattern matching, sealed classes, text
   blocks) to write expressive, type-safe code
4. **Purposeful Abstractions** — Use interfaces and composition to create flexible designs, avoiding
   inheritance hierarchies
5. **Fail Fast, Fail Clear** — Validate early with specific exceptions; use Optional for absent
   values, never for parameters

### Variables and Types

#### Type Declarations

Use `var` for local variable declarations ONLY when the type is immediately obvious from the
right-hand side.

**Decision Checklist:**

- ✓ Can you tell the exact type in 1 second? → Use `var`
- ✗ Would you need to check documentation or method signatures? → Use explicit type
- ✗ Is the type generic, an interface, or complex? → Use explicit type
- ✗ Is the variable used far from its declaration? → Use explicit type

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

- Prefer record classes for immutable data carriers
  ```java
  record Point(int x, int y) {}
  ```

- Use records instead of classes with only final fields and accessors

- Add custom methods to records when needed for behavior

### Sealed Classes

- Use sealed classes to restrict inheritance hierarchies
  ```java
  sealed interface Shape permits Circle, Rectangle, Triangle {}
  ```

- Combine with records for algebraic data types

### Pattern Matching

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

### Text Blocks

- Use text blocks for multi-line strings (JDK 15+)
  ```java
  String json = """
      {
          "name": "value"
      }
      """;
  ```

### Null Handling

- Use `Optional` for return types that may be absent, not for parameters

- Avoid `Optional` fields in classes

- Use `Objects.requireNonNull()` for parameter validation

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

- Prefer specific exception types over generic ones

- Use try-with-resources for `AutoCloseable` resources

- Don't catch `Exception` or `Throwable` unless absolutely necessary

### Naming

- Classes and interfaces: `PascalCase`
- Methods and variables: `camelCase`
- Constants: `UPPER_SNAKE_CASE`
- Packages: lowercase, no underscores
- Test classes: `ClassNameTest` for unit tests, `ClassNameIT` for integration tests

### Code Organization

- Keep methods short and focused on a single responsibility

- Prefer composition to inheritance

- Use interfaces for abstraction, not abstract classes

### Code Quality

- Single Responsibility Principle — methods focused on one task

- The appropriate use of immutability and final keywords

- Composition over inheritance

- Proper documentation with Javadoc for public APIs

### Immutability

- Make classes immutable by default

- Use `final` for fields that shouldn't change

- Prefer unmodifiable collections

### Documentation

- Document public APIs with Javadoc

- Focus on why, not what (code should be self-documenting for "what")

- Keep documentation up to date with code changes

### Javadoc

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

## Code Formatting

This project uses Spotless with Google Java Format for code formatting.

The `spotless:check` goal is bound to the `verify` phase and will fail the build if code is not
properly formatted.

If the build fails due to formatting issues, run:

```bash
mvn spotless:apply
```

This will automatically format all Java files according to Google Java Format standards.

## Finding Source Code

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

## Git Workflow

### Commit Messages

Keep the title of the commit message to ~72 characters.

Use the body to summarize the changes made in the commit. If the commit contains a number of
unrelated changes, try to generate a brief one-line subject, then summarize using bullet points. Be
concise.

Avoid subjective justification or explanation for changes; state changes on their own merit.

Do not include counts (files, tests, lines, changes, etc.).

Do not include a "generated with Claude Code" line.

### Pull Requests

Do not include a "Test Plan" section in PRs.

Do not mention the build was successful or tests passed.

Do not include counts (files, tests, lines, changes, etc.).

Do not include a "generated with Claude Code" line.
