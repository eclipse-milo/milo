# Node instantiation migration guide

The Milo 1.2.0-SNAPSHOT [Server Types and Instantiation guide](https://github.com/eclipse-milo/milo/wiki/Server-Types-and-Instantiation) owns application setup and migration. The [1.2 release guide](https://github.com/eclipse-milo/milo/wiki/Release-Notes-1.2.0#legacy-node-factories) contains the versioned before-and-after example and compatibility changes.

<a id="table-of-contents"></a>
<a id="overview"></a>
<a id="quick-start-the-90-case"></a>

## Application guide

Start with [model prerequisites](https://github.com/eclipse-milo/milo/wiki/Server-Types-and-Instantiation#model-prerequisites), [request construction](https://github.com/eclipse-milo/milo/wiki/Server-Types-and-Instantiation#request-an-instance), and [result ownership](https://github.com/eclipse-milo/milo/wiki/Server-Types-and-Instantiation#planning-applying-and-ownership).

<a id="legacy-pattern-mappings"></a>
<a id="1-instancenodeid-overrides--nodeidstrategy"></a>
<a id="2-path-string-id-pinning--assignnodeid"></a>
<a id="3-includeoptionalnode--include-paths-or-a-predicate"></a>
<a id="4-the-callback-trio--onnode-and-bindmethod"></a>
<a id="5-post-creation-deletion--request-time-exclusion"></a>
<a id="6-post-hoc-fixup--request-identity-and-placement"></a>

## Legacy mappings

The Wiki's [member and identity selection table](https://github.com/eclipse-milo/milo/wiki/Server-Types-and-Instantiation#select-members-and-identities) maps optional selection, explicit IDs, ID strategies, placeholders, node hooks, and Method binding. Its [migration workflow](https://github.com/eclipse-milo/milo/wiki/Server-Types-and-Instantiation#migrating-nodefactory-users) covers persisted identities and post-creation mutation. The [release guide](https://github.com/eclipse-milo/milo/wiki/Release-Notes-1.2.0#legacy-node-factories) explains `legacyPathStrings()`, collision behavior, and copied declaration attributes.

<a id="events-eventfactory--eventinstantiator"></a>

## Events

Use the [Server Events guide](https://github.com/eclipse-milo/milo/wiki/Server-Events#instantiate-populate-and-fire) for event creation, routing, and cleanup. Its [migration section](https://github.com/eclipse-milo/milo/wiki/Server-Events#cleanup-and-migration) covers replacing `EventFactory`.

For contributors, [EventInstantiator](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation/EventInstantiator.java) owns a private node manager registered for event-field resolution. It selects the root abstract-type exception for transient event values and sets EventType during instantiation. Its typed overload validates the expected Java class before storage. Preserve these boundaries when changing the general engine; [EventInstantiatorTest](../../opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation/EventInstantiatorTest.java) exercises the event-specific path.

<a id="changed-behavior"></a>
<a id="experimental-status"></a>

## Implementation and compatibility

The [instantiation package documentation](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation/package-info.java) records the package's experimental status and the frozen legacy behavior during deprecation. Keep the versioned caller-visible differences in the [release guide](https://github.com/eclipse-milo/milo/wiki/Release-Notes-1.2.0#legacy-node-factories).

[TypeModelCompiler](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation/TypeModelCompiler.java) produces a validated structural snapshot with diagnostics and provenance. [TypeModelCache](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation/TypeModelCache.java) coordinates invalidation with pending compilation. Attribute snapshots copy standard values on ingress and egress; application values without a standard codec must be immutable, including when nested in a standard structure.

Follow [NodeInstantiator](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation/NodeInstantiator.java) from description to planning and application. [InstantiationResult](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation/InstantiationResult.java) owns recorded node and reference additions. Rollback and cleanup check object identity to preserve replacements; custom storage must provide atomic primitives or coordinate a single writer. Application resources and arbitrary hook side effects are outside that storage ownership.

Keep these implementation details with their source contracts:

- [ReferenceReplicationPolicy](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation/ReferenceReplicationPolicy.java) separates non-hierarchical declaration references from structural hierarchy and type-definition references.
- [InstantiationException](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation/InstantiationException.java) and [InstantiationDiagnostic](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation/InstantiationDiagnostic.java) retain PLAN/APPLY failure phases and `ROLLBACK_FAILED` details. Mutations to reused or shared nodes are outside ownership of newly created additions.
- [InstantiationRequest](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation/InstantiationRequest.java) documents staged `onNode` hooks and post-commit Method binding. Graph-mutating node conveniences can reach live storage through the node context; they do not become journaled additions merely because a hook called them.
- [TypeModelCompilerTest](../../opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation/TypeModelCompilerTest.java) and [TypeModelConsistencyTest](../../opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation/TypeModelConsistencyTest.java) retain inheritance, declaration-override, and reference-shape regression cases.

The [instantiation tests](../../opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/nodes/instantiation) cover compilation, snapshots, cache invalidation, planning, placeholders, application, and ownership. Use the repository's [test invocation guidance](../../.claude/docs/running-tests.md) and [test quality guidelines](../../.claude/docs/test-documentation-and-quality-guidelines.md).
