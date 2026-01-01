# Java Coding Conventions

## Variables and Types

Choose type declarations that make the code's intent immediately clear to readers. While `var` reduces verbosity, explicit types often communicate intent more effectively.

### Type Declarations

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

## Imports

Prefer importing classes and using their simple names over inline fully qualified class names.
Fully qualified names add visual clutter and make code harder to read.

**Use imports and simple names:**

```java
import com.inductiveautomation.ignition.gateway.redundancy.types.ProjectState;
import com.inductiveautomation.ignition.gateway.redundancy.types.HistoryLevel;

// Good: Clean and readable
return new RedundancyState(
    NodeRole.Backup,
    ProjectState.Unknown,
    HistoryLevel.Partial,
    activityLevel);
```

**Avoid inline fully qualified names:**

```java
// Avoid: Verbose and cluttered
return new RedundancyState(
    NodeRole.Backup,
    com.inductiveautomation.ignition.gateway.redundancy.types.ProjectState.Unknown,
    com.inductiveautomation.ignition.gateway.redundancy.types.HistoryLevel.Partial,
    activityLevel);
```

**Exception:** Use fully qualified names only when necessary to resolve ambiguity between classes
with the same simple name:

```java
import java.util.Date;

// Acceptable: Resolves ambiguity with java.util.Date
java.sql.Date sqlDate = new java.sql.Date(timestamp);
```

## Nullability

Packages should be annotated `@NullMarked` (JSpecify). Assume non-null by default; use `@Nullable`
only for parameters, fields, or return types that genuinely accept or return null.

## Documentation

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

## Other

For any coding practices not explicitly covered by these conventions, defer to established Java best
practices and community standards. This codebase uses Java 17.
