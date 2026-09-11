# Object-scoped Method bindings

Use one application-owned `MethodBindings` registry for shared Method nodes across namespaces on
the same server. Bindings select a handler by the Call request's ObjectId. Each Method has one
dispatcher, so binding another Object does not overwrite the first Object's behavior.

```java
try (MethodBindings bindings = new MethodBindings()) {
  MethodBinding first = bindings.bind(firstObject, sharedMethod, firstHandler);
  MethodBinding second = bindings.bind(secondObject, sharedMethod, secondHandler);
  // Close a token before removing its owner node.
  first.close();
}
```

The owner must be a live Object or ObjectType with a callable component relationship to the Method.
Method lookup includes registered HasComponent subtypes, including HasOrderedComponent, and retains
qualified Method declaration lookup through the Object's type hierarchy. Update the server's
reference type tree after adding reference types. Organizes references do not establish invocation
ownership. A modelled instance declaration cannot be called with its ObjectType as ObjectId.
ConditionManager-owned handlers retain their existing precedence; bind rejects those relationships
with Bad_NotSupported.

An AbstractMethodInvocationHandler must refer to the same Method node and declare matching input
and output names, data types, ranks and dimensions. Absent and empty dimension arrays both mean
unspecified dimensions. Malformed or mismatched metadata fails with Bad_TypeMismatch before handler
installation. The registry does not create or rewrite Method argument Properties. Raw handlers
remain responsible for their own argument validation.

## Replacement and cleanup

Binding the same ObjectId again atomically replaces only that Object's registration. The returned
token owns that specific registration; closing an old token cannot remove a replacement. Other
Objects keep their handlers. Unregistered Objects use the exact handler captured before the
registry installed its dispatcher.

The last registration restores that captured fallback using identity-based compare-and-set. The
same registry may bind the Method again while the fallback remains unchanged. A competing registry
cannot wrap another registry's dispatcher, including when the first compare-and-set attempt loses.

External `setInvocationHandler` replacement is authoritative. Before each bind, the registry checks
every Method it has managed, including Methods whose last token already restored their fallback.
Once ownership loss is observed, all further binds through that registry fail with
IllegalStateException. Existing registrations on unaffected Methods remain callable. Closing the
registry preserves the external replacement and restores other fallbacks only where it still owns
the dispatcher. Create a new registry for a deliberate new binding lifetime.

Close each token, or call `removeObject(objectId)`, before removing an owner node. NodeId reuse does
not automatically remove old registrations. The registry validates live node identity at bind time
and rejects removed or replaced owner/Method objects with Bad_NodeIdUnknown. Coordinate node
removal and binding in application lifecycle code.

Registry close is idempotent and rejects future binds. It does not remove nodes or drain application
callbacks. A dispatcher captures a handler under a short lock and invokes it after releasing the
lock. A callback selected before replacement or cleanup may finish afterward; applications that
need draining shutdown must track their own in-flight work.

The first successful bind attaches a registry to one server. Owner and Method nodes on different
servers are rejected with IllegalArgumentException. Reusing an attached registry for another server
fails with IllegalStateException.

## Access and verification

Registry operations do not replace Call service access checks. ManagedAddressSpace validates Method
ownership, and the service validates the session and permissions before invocation. Direct
MethodInvocationHandler calls remain a low-level boundary where the caller supplies access checks.

MethodBindingsTest executes replacement, stale-token close, competing installation, raw replacement,
explicit removal/recreation and deterministic invocation/bind/close ordering. It verifies metadata
rejection without rewriting Properties. MethodBindingsServiceTest uses a username-authenticated
client and actual server Calls for custom component reference subtypes, per-Object dispatch,
non-executable Method denial, ObjectType instance-declaration rejection and ConditionManager
preemption. The existing Condition and Method dispatch tests remain regression gates.
