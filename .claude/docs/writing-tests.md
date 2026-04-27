# Writing Tests

Every test should justify its existence by protecting a behavior that matters. If you
cannot explain what would break for users, correctness, or protocol compliance if the
test were deleted, the test probably should not exist.

## Name The What, Comment The Why

Use readable camelCase test names that describe the behavior under test. Add a regular
`//` comment above the test when the reason the behavior matters is not obvious from the
name alone.

```java
// ServerUriArray and RedundantServerArray must stay aligned after topology
// changes. A mismatch causes clients to map the wrong URI to the wrong
// member, which breaks failover.
@Test
void membersExposeRedundancyMetadataWithAlignedArrays() {
    ...
}
```

```java
// A failed mutation must not advance the revision or alter the snapshot.
// If it did, subsequent diffs against the current snapshot would be wrong.
@Test
void failedMutationLeavesTheCurrentSnapshotAndRevisionUnchanged() {
    ...
}
```

```java
// When startup partially succeeds, the already-started members must be
// shut down so we do not leak running OPC UA servers. The original
// failure must remain the thrown exception.
@Test
void startupRollsBackStartedMembersAndPreservesTheOriginalFailure() {
    ...
}
```

If the why is already obvious from the test name, skip the comment. The goal is to leave
future readers with enough context to decide whether the test still matters.

## What Good Tests Protect

Good tests challenge the code by verifying:

- Behavioral contracts
- Edge cases with real consequences
- Protocol invariants
- Immediate visibility of mutations
- Error handling and rollback behavior

Examples:

- `resetRestoresTheDeterministicBaselineAndAdvancesRevision`
- `failedMutationLeavesTheCurrentSnapshotAndRevisionUnchanged`
- `membersExposeRedundancyMetadataWithAlignedArrays`
- `serviceLevelMutationsAreVisibleImmediatelyThroughOpcUaReads`
- `startupRollsBackStartedMembersAndPreservesTheOriginalFailure`

## Common Weak-Test Patterns

Weak tests exercise code paths without protecting meaningful decisions. Watch for:

- Getter or property echo tests that only assert a constructor stored what you passed in
- Exhaustive enum round-trips when multiple values exercise the same mutation path
- Exception constructor tests
- Constant value tests that only restate definitions
- Tests that only assert a method does not throw without checking a meaningful outcome

## Quick Smell Check For LLM-Generated Tests

Ask these questions:

1. Would deleting this test make me nervous?
2. Does it fail for an interesting reason?
3. Is it testing Milo behavior, or just Java and the framework?

If the answer to those questions is no, no, and the framework, the test is probably
coverage theater.

## Test Structure

- One behavior per test.
- Keep logic out of test bodies. Avoid `if`, `for`, `switch`, and `try/catch` in tests.
- Follow Arrange / Act / Assert.
- Use helper methods or factories for repeated test data setup.

## Java Test Conventions

- Prefer `assertThrows(T.class, ...)` over `@Test(expected = ...)`.
- Prefer `assertEquals(expected, actual)` over `assertTrue(a == b)`.
- Prefer try-with-resources for closable test resources when it makes cleanup clearer
  and more reliable.

## Organizing Tests

- Use `@Nested` when it clarifies the test suite by grouping a meaningful behavior area,
  scenario, or lifecycle phase that has multiple related tests or shared setup.
- Skip `@Nested` when a test class is already small and flat. Do not introduce nesting
  just to mirror production method names or to wrap a single test in extra structure.
- When you do use `@Nested`, group by feature or scenario, not by method name.
- Name groups after behavior areas like `Topology`, `Maintenance`, or `Rollback`.
