# Node Instantiation Migration Guide

The legacy type-instantiation subsystem — `NodeFactory`, `InstanceDeclarationHierarchy`,
`NodeFactory.InstantiationCallback`, `EventFactory`, and
`ManagedAddressSpace.getNodeFactory()` — is deprecated, replaced by the
`org.eclipse.milo.opcua.sdk.server.nodes.instantiation` package. The legacy
implementation is retained with frozen behavior for the deprecation period; its removal
is a future major-version decision. This guide maps every legacy usage pattern to the
new API and lists every behavior that changes when you move.

The new package ships as **experimental** for one minor release: the API may adjust
based on in-tree migrations and placeholder validation against real companion-spec
workloads, then freezes.

* * *

## Table of Contents

- [Overview](#overview)
- [Quick Start: the 90% Case](#quick-start-the-90-case)
- [Legacy Pattern Mappings](#legacy-pattern-mappings)
  - [1. `instanceNodeId` overrides → `nodeIdStrategy`](#1-instancenodeid-overrides--nodeidstrategy)
  - [2. Path-string id pinning → `assignNodeId`](#2-path-string-id-pinning--assignnodeid)
  - [3. `includeOptionalNode` → include paths or a predicate](#3-includeoptionalnode--include-paths-or-a-predicate)
  - [4. The callback trio → `onNode` and `bindMethod`](#4-the-callback-trio--onnode-and-bindmethod)
  - [5. Post-creation deletion → request-time exclusion](#5-post-creation-deletion--request-time-exclusion)
  - [6. Post-hoc fixup → request identity and placement](#6-post-hoc-fixup--request-identity-and-placement)
- [Events: `EventFactory` → `EventInstantiator`](#events-eventfactory--eventinstantiator)
- [Changed Behavior](#changed-behavior)
- [Experimental Status](#experimental-status)

* * *

## Overview

The replacement is a server-scoped facade, `NodeInstantiator`, obtained from
`OpcUaServer.getNodeInstantiator()`. Everything about an instantiation — identity,
placement, attribute values, optional-member selection, NodeId allocation, placeholder
expansion, and behavior hooks — is described up front by an immutable
`InstantiationRequest`, built with `InstantiationRequest.of(rootClass,
typeDefinitionId)`.

The facade splits instantiation into three phases you can use separately or together:

- `describe(typeDefinitionId)` returns the type's compiled `TypeInstantiationModel` —
  the fully-inherited instance declaration hierarchy as an immutable, cacheable
  snapshot, keyed by `BrowsePath`.
- `plan(request)` joins the request with the model into a pure-data
  `InstantiationPlan`: every declaration accounted for, all NodeIds allocated and
  collision-checked, every reference resolved. Nothing is constructed or mutated.
- `apply(plan)` materializes the plan into the request's target `NodeManager` as a
  staged, journaled unit, returning a typed `InstantiationResult`.

`instantiate(request)` wraps plan and apply in one call and is what most code should
use. The result exposes the typed root (`result.root()`), per-path lookups
(`result.node(path)`, `result.require(path, type)`), the exact set of created nodes and
references, and `result.deleteCreated()` — cleanup that removes exactly what this
instantiation created, never nodes it reused.

Note on imports: the new package has its own `BrowsePath`
(`org.eclipse.milo.opcua.sdk.server.nodes.instantiation.BrowsePath`), a public
structured path built from `QualifiedName` elements. It is unrelated to the legacy
`org.eclipse.milo.opcua.sdk.server.nodes.factories.BrowsePath`; watch the import when
migrating a class that used the legacy type.

* * *

## Quick Start: the 90% Case

Legacy:

```java
UaObjectNode node =
    (UaObjectNode)
        getNodeFactory()
            .createNode(new NodeId(2, "Demo/MyObject"), NodeIds.FolderType);

node.setBrowseName(new QualifiedName(2, "MyObject"));
node.setDisplayName(LocalizedText.english("MyObject"));

getNodeManager().addNode(node);

node.addReference(
    new Reference(
        node.getNodeId(), NodeIds.Organizes, parentNodeId.expanded(), false));
```

New API — one request describes the whole instance, and the root class is validated at
plan time instead of cast afterwards:

```java
InstantiationRequest<UaObjectNode> request =
    InstantiationRequest.of(UaObjectNode.class, NodeIds.FolderType)
        .nodeId(new NodeId(2, "Demo/MyObject"))
        .browseName(new QualifiedName(2, "MyObject"))
        .displayName(LocalizedText.english("MyObject"))
        .parent(parentNodeId, NodeIds.Organizes)
        .target(getNodeManager())
        .build();

UaObjectNode node = getServer().getNodeInstantiator().instantiate(request).root();
```

Inside a `ManagedAddressSpace`, replace `getNodeFactory()` with
`getServer().getNodeInstantiator()` targeted at `getNodeManager()`, as above.

* * *

## Legacy Pattern Mappings

### 1. `instanceNodeId` overrides → `nodeIdStrategy`

Subclassing `NodeFactory` to control member NodeId derivation:

```java
NodeFactory factory =
    new NodeFactory(getNodeContext()) {
      @Override
      protected NodeId instanceNodeId(
          NodeId rootNodeId, org.eclipse.milo.opcua.sdk.server.nodes.factories.BrowsePath browsePath) {
        return deriveCustomNodeId(rootNodeId, browsePath.join());
      }
    };
```

becomes a `nodeIdStrategy` on the request. The strategy receives a `NodeIdContext` —
the root's NodeId, the occurrence's `BrowsePath`, and the `InstanceDeclaration` being
realized — and may return any identifier type; every produced id is collision-checked
before anything is constructed:

```java
InstantiationRequest.of(UaObjectNode.class, typeDefinitionId)
    .nodeId(rootNodeId)
    .nodeIdStrategy(ctx -> deriveCustomNodeId(ctx.rootNodeId(), ctx.path().toString()))
    .target(nodeManager)
    .build();
```

`NodeIdContext.defaultNodeId()` and `NodeIdContext.legacyNodeId()` expose the built-in
derivations, so a strategy can special-case some paths and fall back for the rest.

**Keeping identical derived ids.** Derived NodeIds are persisted identity for existing
deployments. If your code relied on the legacy formula (root identifier string +
`/<namespaceIndex>:<name>` per path element, unescaped), set
`Builder.legacyPathStrings()` and unpinned, non-strategy ids are derived with the
legacy formula verbatim. The new default derivation is similar but
collision-resistant — it escapes `\`, `/`, and `:` and marks non-String root
identifiers — so freshly migrated code that never persisted ids should prefer the
default.

### 2. Path-string id pinning → `assignNodeId`

Pinning specific members to fixed NodeIds by string-matching the browse path inside an
`instanceNodeId` override:

```java
NodeFactory factory =
    new NodeFactory(getNodeContext()) {
      @Override
      protected NodeId instanceNodeId(
          NodeId rootNodeId, org.eclipse.milo.opcua.sdk.server.nodes.factories.BrowsePath browsePath) {
        if ("/2:Measurement".equals(browsePath.join())) {
          return new NodeId(2, "Well-Known/Measurement");
        }
        return super.instanceNodeId(rootNodeId, browsePath);
      }
    };
```

becomes a structured per-path assignment:

```java
InstantiationRequest.of(UaObjectNode.class, typeDefinitionId)
    .nodeId(rootNodeId)
    .assignNodeId(
        BrowsePath.of(new QualifiedName(2, "Measurement")),
        new NodeId(2, "Well-Known/Measurement"))
    .target(nodeManager)
    .build();
```

Assigned ids win over the strategy and the default derivation; they are
collision-checked like every other id in the plan.

### 3. `includeOptionalNode` → include paths or a predicate

The legacy callback was consulted per optional member (except Methods — see
[Changed Behavior](#changed-behavior)) with only the declaring type and BrowseName:

```java
UaNode node =
    factory.createNode(
        nodeId,
        NodeIds.AnalogItemType,
        new NodeFactory.InstantiationCallback() {
          @Override
          public boolean includeOptionalNode(
              NodeId typeDefinitionId, QualifiedName browseName) {
            return browseName.equals(new QualifiedName(0, "InstrumentRange"));
          }
        });
```

The request replaces it with declarative selection:

```java
// Exact paths:
InstantiationRequest.of(AnalogItemTypeNode.class, NodeIds.AnalogItemType)
    .nodeId(nodeId)
    .includeOptional(BrowsePath.of(new QualifiedName(0, "InstrumentRange")))
    .target(nodeManager)
    .build();

// Everything:
InstantiationRequest.of(AnalogItemTypeNode.class, NodeIds.AnalogItemType)
    .nodeId(nodeId)
    .includeAllOptionals()
    .target(nodeManager)
    .build();

// Or a predicate over the full declaration (path, rule, type, attributes):
InstantiationRequest.of(AnalogItemTypeNode.class, NodeIds.AnalogItemType)
    .nodeId(nodeId)
    .includeOptionals(declaration -> declaration.browsePath().depth() == 1)
    .target(nodeManager)
    .build();
```

Two same-named optionals at different paths are selected independently — the selection
key is the full `BrowsePath`, not the BrowseName.

### 4. The callback trio → `onNode` and `bindMethod`

`onObjectAdded`/`onVariableAdded`/`onMethodAdded` fired per node, mid-instantiation, on
nodes already stored in the NodeManager:

```java
UaNode node =
    factory.createNode(
        nodeId,
        typeDefinitionId,
        new NodeFactory.InstantiationCallback() {
          @Override
          public void onVariableAdded(
              @Nullable UaNode parent, UaVariableNode instance, NodeId typeDefinitionId) {
            instance.setValue(new DataValue(new Variant(0.0d)));
          }

          @Override
          public void onMethodAdded(@Nullable UaObjectNode parent, UaMethodNode instance) {
            instance.setInvocationHandler(handlerFor(instance));
          }
        });
```

The replacement is two hooks with distinct timing:

- `onNode` runs during apply, after the **complete staged graph** is constructed and
  before anything is committed or published. It receives the declaration occurrence
  being realized (`null` for the root), the staged node, its staged parent, and a
  read-only `StagedGraph` for cross-node wiring. Configure node-local state here —
  values, filters, permissions — instead of mutating nodes after creation.
- `bindMethod` attaches an invocation handler to a Method at a specific path, after
  commit (a handler observable via the address space should not be callable before its
  node exists there).

```java
InstantiationRequest.of(UaObjectNode.class, typeDefinitionId)
    .nodeId(nodeId)
    .onNode(
        (declaration, node, parent, graph) -> {
          if (declaration != null
              && declaration.browseName().equals(new QualifiedName(0, "SetPoint"))
              && node instanceof UaVariableNode variableNode) {
            variableNode.setValue(new DataValue(new Variant(0.0d)));
          }
        })
    .bindMethod(
        BrowsePath.of(new QualifiedName(2, "Start")),
        methodNode -> methodNode.setInvocationHandler(handlerFor(methodNode)))
    .target(nodeManager)
    .build();
```

One caution carried over from the legacy world in a new form: `onNode` hooks should
configure node-local state only. Graph-mutating conveniences (`addReference`,
`setProperty`, `addComponent`, …) write through the node's context straight into the
live target NodeManager — outside the journaled batch — so they are not rolled back on
failure and not removed by `InstantiationResult.deleteCreated()`. Structure belongs in
the request.

### 5. Post-creation deletion → request-time exclusion

Deleting unwanted members after instantiation:

```java
UaObjectNode node = (UaObjectNode) factory.createNode(nodeId, typeDefinitionId);

node.findNode(new QualifiedName(0, "Unwanted")).ifPresent(UaNode::delete);
```

becomes exclusion in the request, so the unwanted member — and its entire subtree — is
never created at all:

```java
InstantiationRequest.of(UaObjectNode.class, typeDefinitionId)
    .nodeId(nodeId)
    .includeAllOptionals()
    .excludeOptional(BrowsePath.of(new QualifiedName(0, "Unwanted")))
    .target(nodeManager)
    .build();
```

Exclusions prune subtrees and win over broader inclusion (`includeAllOptionals` or a
predicate). Note that excluding an omitted-by-default optional is a no-op — exclusion
exists to carve exceptions out of broad inclusion. Mandatory members cannot be
excluded; a plan that omits a Mandatory declaration carries an error and is refused by
apply. Every excluded or omitted declaration is reported in
`InstantiationResult.skippedDeclarations()` with a machine-readable reason.

### 6. Post-hoc fixup → request identity and placement

Renaming and re-parenting after creation:

```java
UaObjectNode node = (UaObjectNode) factory.createNode(nodeId, typeDefinitionId);

node.setBrowseName(new QualifiedName(2, "Pump01"));
node.setDisplayName(LocalizedText.english("Pump 01"));

getNodeManager().addNode(node);

node.addReference(
    new Reference(
        node.getNodeId(), NodeIds.HasComponent, parentNodeId.expanded(), false));
```

is part of the request — the instance is born with its identity and placement, and the
parent reference is committed in the same journaled batch as the nodes:

```java
InstantiationRequest.of(UaObjectNode.class, typeDefinitionId)
    .nodeId(nodeId)
    .browseName(new QualifiedName(2, "Pump01"))
    .displayName(LocalizedText.english("Pump 01"))
    .parent(parentNodeId, NodeIds.HasComponent)
    .target(nodeManager)
    .build();
```

Parent attachment is optional: omit `parent(...)` to create an unattached instance and
wire it up yourself. Root attribute values beyond the common ones have a general form,
`rootAttribute(AttributeId, value)`, and `value(...)`/`rootAttribute(...)` set initial
values without post-creation mutation.

* * *

## Events: `EventFactory` → `EventInstantiator`

`OpcUaServer.getEventFactory()` is deprecated; use
`OpcUaServer.getEventInstantiator()`. The surface is the same two-argument
`createEvent`, plus a typed overload that replaces the legacy post-construction cast:

```java
BaseEventTypeNode event =
    server
        .getEventInstantiator()
        .createEvent(new NodeId(2, UUID.randomUUID()), NodeIds.BaseEventType);

// Typed — the expected class is validated at plan time, before any node is created:
SystemEventTypeNode systemEvent =
    server
        .getEventInstantiator()
        .createEvent(
            SystemEventTypeNode.class, new NodeId(2, UUID.randomUUID()), NodeIds.SystemEventType);
```

Behavior differences from the legacy factory:

- **Typed preflight instead of a cast.** The legacy factory cast the created node to
  `BaseEventTypeNode` after its nodes were already stored; a custom event type with no
  registered Java class threw `ClassCastException` and left residue. The replacement
  validates the expected class at plan time; failure creates nothing. An unregistered
  custom event type resolves to its nearest registered ancestor's class instead of
  failing.
- **Private, registered node manager.** Event nodes are created in the
  `EventInstantiator`'s private `UaNodeManager`, registered with the server's
  `AddressSpaceManager` while the server is running, so event fields resolve during
  filter evaluation without the events being reachable from the server's hierarchy.
- **No post-creation mutation.** The `EventType` Property is set during instantiation,
  not by mutating the created node afterwards.
- **Cleanup is still yours.** As with the legacy factory, delete event nodes
  (`UaNode.delete()`) once they have been posted to the event bus.

**Abstract event types and `allowAbstractType`.** The new engine rejects an abstract
root type at plan time (`ABSTRACT_TYPE`) — a check the legacy factory never made, which
is why legacy code could instantiate `BaseEventType` at all. The event workload is the
legitimate exception: transient events are values, never part of the server's
hierarchy, and `BaseEventType` itself is abstract. `Builder.allowAbstractType()`
(default off) bypasses only the root abstractness error — member-level abstractness
still requires a `concreteType` selection — and `EventInstantiator` sets it internally.
If you build transient event-like instances with `InstantiationRequest` directly, set
it yourself; leave it unset for anything that becomes part of the address space.

* * *

## Changed Behavior

Moving to the new API is an opt-in behavior change. The legacy implementation keeps its
frozen behavior — including the defects below, which are documented in its deprecation
Javadoc rather than fixed. The differences you can observe:

**Optional-Method selection.** Legacy created optional Methods unconditionally;
`includeOptionalNode` was never consulted for them. The new engine selects optional
Methods exactly like any other optional member. Migrating code that relied on optional
Methods appearing "for free" must include them (a `bindMethod` path is not a selection
— add the path via `includeOptional` or a predicate).

**Collision failure.** Legacy silently replaced an existing node at the same NodeId and
left the previous node's reference rows in place. The new engine's default policy
(`ConflictPolicy.FAIL`) fails the plan with `NODE_ID_COLLISION` before anything is
constructed. `ConflictPolicy.REUSE_COMPATIBLE` reuses an existing node only after
validating namespace-qualified BrowseName, NodeClass, and TypeDefinition compatibility;
an incompatible node fails with `INCOMPATIBLE_REUSE` rather than being adopted or
replaced. Reused nodes are never deleted by `deleteCreated()`.

**Attribute completeness — including security attributes.** Legacy attribute
propagation from declarations was incomplete: `MinimumSamplingInterval`, `Historizing`,
`AccessLevelEx`, `RolePermissions`, `UserRolePermissions`, and `AccessRestrictions`
were not copied. The new engine copies the full attribute set, preserving
absent/null/value distinctions. The security-relevant consequence:
**instances of security-modeled types now come up with their modeled
`RolePermissions`/`UserRolePermissions`/`AccessRestrictions`, where legacy instances
silently defaulted to unrestricted.** If your deployment depended on instances of such
types being unrestricted, that was the legacy defect, not a contract — but audit for it
before migrating.

**Publication timing.** Legacy stored nodes into the NodeManager one at a time during
construction — each node publicly visible before the instance was complete — and fired
its callbacks afterwards, on already-published nodes. The new engine
stages the complete graph unpublished, runs `onNode` hooks against the staged graph,
then commits nodes and references as one journaled batch; `bindMethod` binders run
after commit. Nothing is observable in the target until commit, and `afterCommit`
observers see only fully committed results.

**Reference reconstruction.** Non-hierarchical declaration references are re-mapped
among instantiated members (internal) or copied verbatim (external) under a named
policy, `ReferenceReplicationPolicy`, and land exactly once in each observable
direction, with no edges to omitted targets. Legacy behavior — a full-list scan with
order-dependent quirks — left inverse-only rows and edges to nodes that were never
created reachable in some shapes.

**Error shapes.** Legacy failures surfaced as generic `UaException`s, or as
`ClassCastException` after nodes were stored, with partial instance trees left behind.
New-engine failures are `InstantiationException` (itself a `UaException`) carrying the
lifecycle phase (`PLAN`/`APPLY`) and structured `InstantiationDiagnostic`s naming each
finding per declaration path. A failed plan mutates nothing; a failed apply rolls back
exactly its journaled additions. The two documented exceptions: a failed rollback is
reported loudly with `ROLLBACK_FAILED` diagnostics carrying what could not be removed,
and mutations a hook made to reused/shared nodes are not this instantiation's to undo.

**Cleanup ownership.** Legacy offered no cleanup; callers deleted recursively from the
root and — because excluding an optional member still created and stored its mandatory
descendants, unreachable from the instance root — orphans could survive anyway. The new
result is ownership-explicit: `materializedNodes()` distinguishes CREATED from REUSED,
and `deleteCreated()` removes exactly what the instantiation created. The
excluded-subtree defect does not exist in the new engine: exclusion prunes the subtree
at plan time.

Two further legacy defects fixed (not just changed) in the new engine, for
completeness: a member typed as a subtype of its declaring type now receives that
subtype's inherited mandatory members, and an on-declaration override of a child the
type also declares now wins over the type's entry instead of being discarded.

* * *

## Experimental Status

The `org.eclipse.milo.opcua.sdk.server.nodes.instantiation` package is experimental for
one minor release. The API may adjust — in particular the `BrowsePath` representation
(namespace-index-based today, resolved against the model snapshot's namespace table)
and the placeholder surface (`forPlaceholder`, `expandPlaceholder`), which is being
validated against real companion-specification workloads — then freezes. The legacy
subsystem remains available, unchanged, for the whole deprecation period.
