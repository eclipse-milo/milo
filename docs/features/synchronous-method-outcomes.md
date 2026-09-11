# Synchronous Method outcomes

Subclass `AbstractMethodInvocationHandler` to validate inputs before application code runs.
Existing subclasses that override `invoke(InvocationContext, Variant[])` keep exact input counts
and return Good with their output array. Recompilation is not required.

Override `getRequiredInputArgumentCount(Argument[])` and `invokeResult` when a Method has an
optional input suffix or needs to return a complete result. The count hook receives a defensive
copy of the call's metadata snapshot. It must return a count between zero and the declared input
count. Invalid counts return Bad_InternalError without invoking the callback.

For a Method with one optional String input and one String output, a complete callback can retain
Uncertain status and distinguish omission from supplied null:

```java
@Override
protected int getRequiredInputArgumentCount(Argument[] inputs) {
  return 0;
}

@Override
protected CallMethodResult invokeResult(InvocationContext context, Variant[] inputs) {
  Variant output = inputs.length == 0 ? new Variant("default") : inputs[0];
  return new CallMethodResult(
      StatusCode.UNCERTAIN, null, null, new Variant[] {output});
}
```

Declare the String input and output through `getInputArguments()` and `getOutputArguments()`
and the Method's corresponding argument Properties. Omission shortens the input array; a supplied
null occupies a position. The validator checks only supplied positions, preserving existing data
type, rank, dimension, structure decoding and application value validation. Invalid input prevents
the callback. Array substitutions and returned arrays do not mutate the request or callback's
array.

A complete callback returns exact Good, Uncertain, or Bad. Bad results have no outputs. Input
argument results may be populated only for Bad_InvalidArgument and must match the supplied input
count. Argument diagnostics are empty or have one slot per supplied input, using
`DiagnosticInfo.NULL_VALUE` for an empty slot. Invalid combinations, null results, and Good
subcodes become Bad_InternalError with empty outputs. A thrown `UaException` or
`UaRuntimeException` returns its status and empty outputs. Application-status output arguments
remain separate from the operation status.

## Argument diagnostics

A callback invoked through the Call service can obtain `context.getCallDiagnostics()`. Use
`addString()` on that request-local context for indexes in a `DiagnosticInfo`. Independent local
invocations may have no diagnostics context. The context is shared across address-space groups,
and string interning is thread-safe.

The Call service filters the response using the request's ReturnDiagnostics operation bits:

| Bit | Retained fields |
| --- | --- |
| 0x20 | SymbolicId and NamespaceUri |
| 0x40 | LocalizedText and Locale |
| 0x80 | AdditionalInfo |
| 0x100 | InnerStatusCode |
| 0x200 | InnerDiagnosticInfo, filtered with the same field bits |

Service-only bits do not authorize argument diagnostics. Unrequested fields and unused strings
are removed. Retained indexes are remapped into one compact ResponseHeader StringTable.
Malformed retained indexes, invalid argument-diagnostic cardinality and excessive nesting fail
only that operation with Bad_InternalError. Interned strings are bounded by the configured
response message budget and maximum string length. A budget failure does not retry the callback.

This API produces argument diagnostics. It does not add operation diagnostics or service
diagnostics. Those response slots remain empty. The existing legacy operation diagnostic
producer stubs in `DiagnosticsContext` are unchanged.

`OpcUaClient.callAsync()` requests no diagnostics. A client that needs them can send a
`CallRequest` with the desired ReturnDiagnostics mask through `sendRequestAsync()`, preserving
the current session authentication token and the other request-header fields. Inspect
`CallResponse` directly to retain Uncertain outputs and the response StringTable.

## Ownership and verification

The handler runs synchronously. Direct invocation is a low-level boundary whose caller supplies
access checks. Normal service dispatch validates the active session, access rights and Method
ownership before invocation.

`SynchronousMethodServiceTest` executes authenticated client Calls across two namespaces,
including denied operations, optional omission, supplied null, Uncertain outputs and diagnostic
filtering/failure paths. `SynchronousMethodOutcomeTest` loads an unchanged class compiled against
published Milo 20260910.202114 core/server54 to verify binary compatibility. Its source and artifact
hashes are retained with the fixture. The existing argument-validation tests cover structure,
array, Matrix, rank and dimension behavior.
