# Test Documentation & Quality Guidelines

## Every test must justify its existence

A test earns its place by verifying a **behavior that matters** — something that could break in a
way that affects users, correctness, or protocol compliance. If you can't articulate what would go
wrong if the test were deleted, the test probably shouldn't exist.

## Name the WHAT, comment the WHY

Write test names as readable behavior statements that describe **what** is being tested.

- In Kotlin, prefer backtick test names when they clarify the behavior.
- In Java, use descriptive camelCase method names, or `@DisplayName` when a sentence-style name is
  valuable and fits the surrounding test style.

Use regular comments (`//`) above the test to explain *why* it matters — what invariant is being
protected, what bug it prevents, or what contract it enforces.

```text
// The protocol requires the buffer to be fully consumed after decoding;
// leftover bytes indicate a framing or length field error.
@Test
fun `SendRRData round-trips with no leftover bytes`() {
    // assertions here
}
```

```java
// Callers should not have to detect and retry after a channel drop —
// the persistent client must handle reconnection transparently.
@Test
void persistentClientReestablishesSessionAfterChannelDrop() {
    // assertions here
}
```

```text
// CIP Vol 2 requires general status 0x00 in the reply even when the
// extended status array carries the real error detail.
@Test
fun `reply with non-zero extended status still has zero general status`() {
    // assertions here
}
```

Single-line comments are fine. The goal is a sentence that a future reader (or LLM) can use to
decide whether this test is still relevant.

Comments may also point to adjacent lower-level coverage when an integration test deliberately
protects only the part of a larger behavior that can be made deterministic in that harness.

### Tests that don't need comments

If the WHY is obvious from the test name alone, skip the comment. A test named
``fun `invalid port throws IllegalArgumentException`()`` or
`invalidPortThrowsIllegalArgumentException` doesn't need a comment explaining that invalid input
should be rejected. Use judgment — but when in doubt, add the comment.

## What makes a good test

**Good tests challenge the code.** They verify:

- **Behavioral contracts** — "connect then disconnect leaves the client in NotConnected state"
- **Edge cases with real consequences** — "an unknown type ID in a CPF item must throw, not silently
  corrupt"
- **Protocol invariants** — "encoded bytes round-trip back to an equivalent object with no leftover
  bytes"
- **Concurrency / timing** — "a command sent while disconnected triggers a connect-first sequence"
- **Error recovery** — "after a session timeout, the next command re-establishes the session"

## What makes a weak test

**Weak tests exercise code paths without testing decisions.** Watch for:

- **Getter/property/accessor echo tests** — constructing an object and asserting each field equals
  what you passed in. This tests the language, not your code. Only justified if construction has
  validation logic or computed defaults worth protecting.
- **Exhaustive enum round-trips** — if `roundTrip(EnipStatus.SUCCESS)` and
  `roundTrip(EnipStatus.INVALID_COMMAND)` exercise the same codec path, one is sufficient. Add more
  only if specific values trigger different behavior.
- **Exception constructor tests** — testing that an exception can be instantiated adds zero value.
- **Constant value tests** — asserting that `CommandCode.RegisterSession.code == 0x0065` tests the
  definition, not behavior. These are better caught by compilation or protocol conformance tests.
- **"Walk the path" tests** — tests that call a method or function and assert it doesn't throw,
  without checking the result means anything.

## The LLM test smell

When reviewing LLM-generated tests, ask:

1. **Would deleting this test make me nervous?** If no, it's coverage theater.
2. **Does this test fail for an interesting reason?** A good test fails when behavior changes in a
   way that matters. A weak test fails when you rename a field.
3. **Is this testing my code or the language/framework?** Don't test that Java records, JavaBeans,
   or Kotlin data classes work.

## Test structure

- **One behavior per test.** If a test fails, the name should tell you what broke.
- **No logic in tests.** Avoid `if`, `for`, `when`, `switch`, or `try/catch` in test bodies —
  branching means some paths are silently untested.
- **Arrange / Act / Assert.** Set up state, perform one action, verify the result.
- **Use helpers for test data.** Don't repeat constructor calls with slightly different arguments —
  extract a factory method/function like `persistentClientConfig()`.

## Language and framework conventions

- In Java, prefer JUnit Jupiter's `assertThrows(...)` for exception tests.
- In Kotlin, prefer the exception assertion style already used by nearby tests; use
  `assertFailsWith<T>` when writing idiomatic Kotlin-only tests.
- Prefer `assertEquals(expected, actual)` over `assertTrue(a == b)` — better failure messages.
- Use JUnit 6 native `suspend` test methods for Kotlin coroutine tests (JUnit bridges via
  `runBlocking`).

## Organizing tests

- Use `@Nested` classes to group tests by **feature or scenario**, not by method/function under
  test. In Kotlin, make nested test groups `inner` classes when required by JUnit.
- Name groups after the behavior area: `Lifecycle`, `CommandTransport`, `StateListeners` — not
  `ConnectTests`, `DisconnectTests`.
