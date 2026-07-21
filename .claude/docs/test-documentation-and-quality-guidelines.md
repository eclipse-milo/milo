# Test Documentation & Quality Guidelines

## Every test must justify its existence

A test earns its place by verifying a **behavior that matters** — something that could break in a way that affects
users, correctness, or OPC UA (IEC 62541) spec compliance. If you can't articulate what would go wrong if the test were
deleted, the test probably shouldn't exist.

## Name the WHAT, comment the WHY

Write test method names as camelCase sentences that describe the behavior being tested. Use a Javadoc or `//` comment
above the test to explain *why* it matters — what invariant is being protected, what bug it prevents, or what contract
it enforces. When the behavior comes from the OPC UA specification, cite the part and clause (e.g., "Part 3 §6.3.3.2")
so a future reader can check the test against the source of truth.

```java
// Part 6 requires the decoder to consume the entire chunk body;
// leftover bytes indicate a framing or length field error.
@Test
void binaryDecoderConsumesEntireChunkWithNoLeftoverBytes() {
    ...
}
```

```java
// Callers should not have to detect and retry after a channel drop —
// the session must be transparently re-established by the SDK.
@Test
void clientReEstablishesSessionAfterChannelDrop() {
    ...
}
```

```java
/**
 * Part 4 §5.10.2: a Read on an unknown NodeId must return Bad_NodeIdUnknown
 * in the per-item results, not fail the whole service call.
 */
@Test
void readUnknownNodeIdReturnsBadNodeIdUnknownPerItem() {
    ...
}
```

Single-line comments are fine. The goal is a sentence that a future reader (or LLM) can use to decide whether this test
is still relevant.

### Tests that don't need comments

If the WHY is obvious from the test name alone, skip the comment. A test named
`invalidEndpointUrlThrowsIllegalArgumentException` doesn't need a comment explaining that invalid input should be
rejected. Use judgment — but when in doubt, add the comment.

## What makes a good test

**Good tests challenge the code.** They verify:

- **Behavioral contracts** — "connect then disconnect leaves the client in the Disconnected state"
- **Edge cases with real consequences** — "an unknown type ID in an ExtensionObject must decode to the raw body, not
  throw or silently corrupt"
- **Protocol invariants** — "encoded structures round-trip back to an equivalent object with no leftover bytes"
- **Spec-mandated semantics** — "a Mandatory instance declaration on a supertype is inherited by instances of the
  subtype (Part 3 §6.3.3.2)"
- **Concurrency / timing** — "a request sent while reconnecting is queued and delivered once the channel is
  re-established"
- **Error recovery** — "after a session timeout, the next service call recreates the session and transfers
  subscriptions"

## What makes a weak test

**Weak tests exercise code paths without testing decisions.** Watch for:

- **Getter/property echo tests** — constructing an object and asserting each field equals what you passed in. This tests
  the language, not your code. Only justified if construction has validation logic or computed defaults worth
  protecting.
- **Exhaustive enum round-trips** — if `roundTrip(StatusCode.GOOD)` and
  `roundTrip(StatusCode.BAD_INTERNAL_ERROR)` exercise the same codec path, one is sufficient. Use parameterized
  golden-value tests when enum entries represent externally specified wire codes.
- **Exception constructor tests** — testing that a `UaException` can be instantiated adds zero value.
- **Implementation constant echo tests** — asserting a private implementation constant equals the value it was
  initialized with adds little value. Do write spec/golden-value tests for externally defined wire values, well-known
  NodeIds, status codes, and binary layouts.
- **"Walk the path" tests** — tests that call a method and assert it doesn't throw, without checking the result means
  anything.

## The LLM test smell

When reviewing LLM-generated tests, ask:

1. **Would deleting this test make me nervous?** If no, it's coverage theater.
2. **Does this test fail for an interesting reason?** A good test fails when behavior changes in a way that matters. A
   weak test fails when you rename a field.
3. **Is this testing my code or the language/framework?** Don't test that Java records or Netty buffers work.

## Test structure

- **One observable behavior per test.** If a test fails, the name should tell you what broke.
- **Avoid hidden branching.** Prefer `@ParameterizedTest` with `@MethodSource` (the pattern used throughout this
  codebase) or `@ValueSource`/`@CsvSource` for table-driven cases with clear assertion messages. Keep control flow out
  of the main test body unless it makes fixture setup or protocol cases clearer.
- **Arrange / Act / Assert.** Set up state, perform the action or sequence under test, verify the result.
- **Use fixture/helper classes for test data.** Don't repeat builder or constructor calls with slightly different args —
  extract a shared fixture (e.g., `TypeFixtures`,
  `SerializationFixture2`) or a factory method.
- **Control assertions.** When a test proves something *happens*, consider a companion assertion proving the same thing
  *doesn't* happen in the baseline case, with a message saying so — it guards against the test passing vacuously.

## Java / JUnit conventions

- Tests use JUnit 5 (Jupiter) with statically imported `org.junit.jupiter.api.Assertions` methods. (Some older tests in
  `stack-tests` still use TestNG — don't write new TestNG tests.)
- Prefer `assertThrows(SomeException.class, () -> ...)` and assert on the caught exception when the message or status
  code matters.
- Prefer `assertEquals(expected, actual)` over `assertTrue(a.equals(b))` — better failure messages. Mind the argument
  order: expected first.
- Add a failure message (the last argument) when the assertion's purpose isn't obvious from the test name, e.g.
  `assertEquals(baseType.getNodeId(), decl.declaringTypeId(), "provenance: ...")`.
- Use `assertArrayEquals` for arrays and byte payloads; prefer readable hex or golden-byte helpers for protocol buffers.
- For async behavior, prefer completing `CompletableFuture`s with bounded
  `get(timeout, unit)`/`orTimeout` over `Thread.sleep`. Never assert on state that a background thread races to update
  without synchronization.
- New test files use the standard Eclipse Milo EPL-2.0 header with the current year.

## Organizing tests

- Use `@Nested` inner classes to group tests by **feature or scenario**. Method-oriented groups are acceptable when the
  public API entry point is the behavior boundary, such as codecs or parsers.
- Name groups after the behavior area: `Lifecycle`, `Encode`, `Decode`, `Inheritance`,
  `ModellingRules`, `ErrorRecovery`. Split into separate test classes when a group grows large.
- `@DisplayName` is optional; it's used sparingly here, mostly on parameterized serialization tests. A good method name
  usually makes it redundant.
- Remember module targeting when running tests: see `.claude/docs/running-tests.md` for `-pl`/`-am`
  flags (and note that `sdk-server` tests need `verify -pl ... -am`, not plain `test -pl`).
