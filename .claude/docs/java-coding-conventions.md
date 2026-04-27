# Java Coding Conventions

## Variables and Types

Choose type declarations that make the code's intent immediately clear to readers. While `var` reduces verbosity, explicit types often communicate intent more effectively.

### Type Declarations

Use `var` for local variable declarations when the type is immediately obvious from the right-hand
side. Use an explicit type when the right-hand side hides the type or when the explicit type adds
meaning that is not already present in the expression.

When the right-hand side is a concrete constructor call, a cast, or a literal, `var` is usually
preferred unless the explicit type carries important domain meaning not present on the right-hand
side. Generic constructor calls are still obvious when the concrete type appears after `new`.

**Decision Checklist:**

- ✓ Can you tell the exact type in 1 second? → Use `var`
- ✓ Is the right-hand side a concrete constructor call, cast, or literal? → Usually use `var`
- ✗ Would you need to check documentation or method signatures? → Use explicit type
- ✗ Is the type hidden behind a method return, factory, or interface? → Use explicit type
- ✗ Is the variable used far from its declaration? → Use explicit type
- ✗ Does the explicit type reveal important semantic information? → Use explicit type

**What counts as "obvious from the right-hand side":**

- Constructor calls with concrete types: `new ArrayList<String>()`, `new User(...)`
- Casts: `(OpcTcpMultiplexedReverseConnectTransport) client.getTransport()`
- Literals: strings, numbers, booleans, `null`
- Collection factory methods with only literals: `List.of(1, 2, 3)`, `Map.of("key", "value")`
- Standard library methods with obvious return types: `isEmpty()`, `size()`, `toString()`
- Builder patterns that return the same type: `User.builder().name("John").build()`

```java
// Good: Type is clear from the right-hand side
var list = new ArrayList<String>();
var future = new CompletableFuture<Channel>();
var name = "John";
var count = 42;
var user = new User(id, name, email);
var transport = (OpcTcpMultiplexedReverseConnectTransport) client.getTransport();
var isEmpty = list.isEmpty();
var items = List.of("a", "b", "c");
try(
var socket = new Socket("localhost", port)){
        // ...
        }

// Good: Explicit type when not obvious
InputStream stream = getStream();
Result<User> result = repository.getUser(id);
Function<String, Integer> parser = Integer::parseInt;
List<Item> items = Stream.of(item1, item2).collect(toList());
Socket socket = connectAndSendRhe(serverUri);
CompletableFuture<Unit> connectFuture = transport.connect(context);

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

**Avoid style-only churn:** Do not change existing `var` or explicit declarations solely for style
unless the surrounding code is already being edited and the declaration clearly violates these
rules.

**Test code:** `var` is fine for obvious setup objects and fixtures. Prefer explicit types for
method-return values, futures, and values asserted later when the type helps explain the behavior
under test.

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

## JSpecify And Nullability

Use these nullability conventions:

- New packages should usually have a `package-info.java` with `@NullMarked`.
- In an `@NullMarked` package, types are non-null by default. Add `@Nullable` only when `null`
  is part of the real contract.
- Common `@Nullable` cases: optional fields, parameters that explicitly accept `null`, return
  values that use `null` to mean "not present", and record components that may be absent.
- Do not put `@Nullable` on local variables, and do not mark something nullable just because the
  implementation happens to pass through a null check.
- Avoid routine `Objects.requireNonNull(...)` in normal `@NullMarked` code. Use runtime null
  checks mainly at interop/framework boundaries or when a fail-fast diagnostic is genuinely
  useful.

## Javadoc Guidelines

Javadoc should help someone **reading the code** understand what something is and how to use it.
It should not explain how something was built, why it was designed that way internally, or
reference plans/tasks/decisions that led to the current implementation.

### Class-Level Javadoc

State **what** the class is and **when** you'd use it. If it has a non-obvious lifecycle or
usage pattern, describe that.

**Include:**

- What the class represents or does (one or two sentences)
- How a caller interacts with it, if non-obvious
- Important constraints (thread-safety, lifecycle, ownership)
- For public API classes: a short code example if usage isn't obvious from the signature

**Omit:**

- How it works internally ("uses a ConcurrentHashMap to…", "delegates to…")
- Design rationale ("we chose this approach because…")
- References to plans, tasks, or implementation history

### Method/Constructor-Level Javadoc

State **what** the method or constructor does from the caller's perspective.

**Include:**

- What it does and, for methods, what it returns
- Parameter semantics when the name alone isn't enough
- Notable behavior: side effects, checked or runtime exceptions, nullability contract
- For public API methods and constructors: a short code example when the call has non-obvious
  usage patterns, required call sequences, or interacts with other API surfaces in ways the
  signature doesn't convey

**Omit:**

- Step-by-step description of the implementation
- Internal algorithms or data structures used
- Assumptions that are only meaningful to someone editing the method body

### Javadoc Tag Formatting

Descriptions for Javadoc block tags such as `@param`, `@return`, and `@throws` must begin with a
lowercase letter and end with a period.

### Code Examples in Javadoc

When a class, constructor, or method is part of the public API, consider whether a code example
would help a user understand how to use it. Good candidates for examples:

- Builder patterns or multistep construction
- Methods that participate in a larger call sequence
- Non-obvious parameter combinations or return value interpretation

Keep examples minimal — show the common case, not every option. If the usage is clear from the
signature and parameter names alone, skip the example.

### Rule of Thumb

If a sentence only is useful to someone **modifying the implementation**, it belongs in a
code comment inside the body, not in Javadoc. Javadoc is for **callers**.

## Other

For any coding practices not explicitly covered by these conventions, defer to established Java best
practices and community standards. This codebase uses Java 17.
