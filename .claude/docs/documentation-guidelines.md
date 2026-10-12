## General Documentation Guidelines

Documentation comments should help someone **reading the code** understand what something is, how
to use it, and, at package level, how related APIs fit together. Class and method documentation
should stay caller-focused; use package-level documentation for broader onboarding context. Avoid
references to plans, tasks, or decisions that led to the current implementation.

### Class-Level Documentation

State **what** the class is and **when** you'd use it. If it has a non-obvious lifecycle or
usage pattern, describe that.

**Include:**
- What the class represents or does (one or two sentences)
- How a caller interacts with it, if non-obvious
- Important constraints (thread-safety, lifecycle, ownership)
- Collaborating APIs or runtime boundaries a caller must coordinate with
- For public API classes: a short code example if usage isn't obvious from the signature

**Omit:**
- How it works internally ("uses a ConcurrentHashMap to…", "delegates to…")
- Design rationale ("we chose this approach because…")
- References to plans, tasks, or implementation history

### Method/Function-Level Documentation

State **what** the method, function, or constructor does from the caller's perspective.

**Include:**
- What it does and what it returns, when applicable
- Parameter semantics when the name alone isn't enough
- Notable caller-visible behavior: suspension, side effects, exceptions, or nullability contracts
- Observable handoffs to related APIs, services, or lifecycle state when they affect correct use
- For public API methods, functions, and constructors: a short code example when the call has
  non-obvious usage patterns, required call sequences, or interacts with other API surfaces in
  ways the signature doesn't convey

**Omit:**
- Step-by-step description of the implementation
- Internal algorithms or data structures used
- Assumptions that are only meaningful to someone editing the method body

### Code Examples

When a class, constructor, method, function, or property is part of the public API, consider
whether a code example would help a user understand how to use it. Good candidates for examples:

- Builder patterns or multistep construction
- Methods that participate in a larger call sequence
- Non-obvious parameter combinations or return value interpretation

Keep examples minimal — show the common case, not every option. If the usage is clear from the
signature and parameter names alone, skip the example.

### Package-Level Documentation

Prefer adding `package-info.java` files to Java packages whose purpose, data flow, runtime
boundaries, or collaboration model is not obvious from the class names alone. These files are useful
even when the package is not annotated with `@NullMarked`; package annotations are a bonus, not the
only reason to create package-level documentation.

Package documentation should be high-level, explanatory, and onboarding-focused. It is allowed to be
more verbose than class or method documentation because its job is to help a developer build the
mental model they need before reading or changing several related classes.

**Include:**
- What responsibility the package owns, in concrete product/runtime terms
- How the most important types fit together and where callers enter the package
- Key data flows, lifecycle phases, validation layers, threading expectations, or ownership rules
- Boundaries with other packages, scopes, services, persistence layers, UI layers, or network/runtime
  authorities
- Important invariants the package maintains across multiple classes
- Extension guidance: what belongs in the package, what should stay elsewhere, and which layer has
  final authority when responsibilities are split

**Omit:**
- Generated boilerplate such as "provides classes for..."
- A flat inventory of every class in the package
- Method-by-method API reference that belongs on the individual classes or methods
- Implementation trivia that only matters inside one method body
- Historical notes about tasks, tickets, abandoned alternatives, or why a change was originally made

Good package docs often use short sections such as "Data flow", "Lifecycle", "Runtime boundaries",
"Validation", "Ownership", or "Extension points". Name important classes when that helps explain
the architecture, but describe their roles and relationships rather than restating their signatures.

### Rule of Thumb

If a sentence only is useful to someone **modifying the implementation**, it belongs in a
code comment inside the body, not in the documentation comment. Documentation comments are for
**callers**.

## Javadoc Guidelines

Apply the general documentation guidelines to Java APIs, with the Java-specific guidance below.

### Method/Constructor-Level Javadoc

For Java methods and constructors, be explicit about checked exceptions, caller-visible runtime
exceptions, and nullability contracts when they are not obvious from the signature.

### Javadoc Tag Formatting

Descriptions for Javadoc block tags such as `@param`, `@return`, and `@throws` must begin with a
lowercase letter and end with a period.

### Code Examples in Javadoc

Use Java syntax in examples. Include constructors in example consideration when construction is
part of the public API and usage is not obvious from the signature.

## KDoc Guidelines

Apply the general documentation guidelines to Kotlin APIs, with the Kotlin-specific guidance below.

### Method/Function-Level KDoc

For Kotlin methods and functions, be explicit about suspension, side effects, exceptions, and
nullability contracts when they are not obvious from the signature.

### KDoc Tag Formatting

Descriptions for KDoc block tags such as `@property`, `@param`, and `@return` must begin with a
lowercase letter and end with a period.

### Code Examples in KDoc

Use Kotlin syntax in examples. Include classes, constructors, functions, and properties in example
consideration when they are part of the public API and usage is not obvious from the signature.
