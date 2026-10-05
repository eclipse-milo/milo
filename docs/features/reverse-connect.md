# Reverse Connect

Application setup for Milo 1.2.0-SNAPSHOT is maintained in the [Reverse Connect Wiki guide](https://github.com/eclipse-milo/milo/wiki/Reverse-Connect). The [architecture document](../architecture/reverse-connect.md) and package documentation remain the contributor references.

<a id="table-of-contents"></a>
<a id="overview"></a>
<a id="usage"></a>
<a id="server-registering-a-target"></a>
<a id="client-hint-based"></a>
<a id="client-discovery-first"></a>
<a id="client-shared-listener-for-multiple-servers"></a>
<a id="dynamic-claiming"></a>

## Application guide

Choose a [connection shape](https://github.com/eclipse-milo/milo/wiki/Reverse-Connect#choose-a-connection-shape), then follow the [complete local lifecycle](https://github.com/eclipse-milo/milo/wiki/Reverse-Connect#a-complete-local-connection-lifecycle). The [selection and discovery section](https://github.com/eclipse-milo/milo/wiki/Reverse-Connect#select-claim-and-discover) covers hint-based, discovery-first, shared-listener, and direct-claim workflows.

<a id="configuration"></a>
<a id="reverseconnectmanagerbuilder-client"></a>
<a id="reverseconnecttargetbuilder-server"></a>
<a id="opcuaserverconfigbuilder-server"></a>

## Configuration and lifecycle

See the Wiki for [target configuration and retries](https://github.com/eclipse-milo/milo/wiki/Reverse-Connect#configure-server-targets-and-retries) and [security and shutdown](https://github.com/eclipse-milo/milo/wiki/Reverse-Connect#security-and-shutdown). Builder and transport configuration details also remain in the [architecture reference](../architecture/reverse-connect.md#7-configuration-reference) beside the source.

Cancellation belongs to the API that owns the work. `OpcUaClient.connectAsync()` returns a dependent future; cancelling it does not unregister the transport selector. Use `disconnectAsync()` for client cleanup. Raw selector registrations have a separate [connection-ownership contract](https://github.com/eclipse-milo/milo/wiki/Reverse-Connect#select-claim-and-discover).

<a id="how-it-works"></a>
<a id="wire-protocol"></a>
<a id="end-to-end-flow"></a>
<a id="client-side-components"></a>
<a id="server-side-components"></a>
<a id="stack-layer-primitives"></a>
<a id="key-components"></a>

## Implementation

The architecture retains the [wire protocol](../architecture/reverse-connect.md#1-protocol-overview), [runtime data flow](../architecture/reverse-connect.md#23-runtime-data-flow), and [component inventory](../architecture/reverse-connect.md#3-component-inventory). These describe ReverseHello framing and validation, the client candidate handoff, and reuse of the ordinary UASC pipelines.

Read the [client package documentation](../../opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/reverse/package-info.java) for candidate ownership, selector cancellation, callbacks, and transport handoff. Read the [server package documentation](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/reverse/package-info.java) for registration ownership, timers, executor boundaries, and late-handoff cleanup. The architecture's [Session integration section](../architecture/reverse-connect.md#6-session-integration) connects the transport capability interfaces to the Session FSM.

<a id="reversehello-is-a-routing-hint-not-authentication"></a>
<a id="selector-matching-is-one-shot"></a>
<a id="retry-is-server-driven-only"></a>
<a id="stacksdk-split"></a>

## Design decisions

The [architecture design discussion](../architecture/reverse-connect.md#9-key-design-decisions) retains the SDK/stack split, candidate state ownership, per-attempt connectors, and generation tracking. Preserve the security boundary described in the client package: ReverseHello supplies routing hints; normal certificate, SecureChannel, and Session validation establish trust after handoff. Changes to claiming or retry must preserve the one-shot ownership transfer and the distinct manager-bound and direct transport lifecycles.

## Testing

The architecture's [test inventory](../architecture/reverse-connect.md#8-testing) links wire-format, transport, SDK, and integration tests. In particular, the [client manager tests](../../opc-ua-sdk/sdk-client/src/test/java/org/eclipse/milo/opcua/sdk/client/reverse/ReverseConnectManagerTest.java), [server target-manager tests](../../opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/reverse/ReverseConnectTargetManagerTest.java), and [transport integration tests](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/stack/transport/server/tcp/OpcUaClientReverseConnectTest.java) exercise ownership across asynchronous boundaries.

Follow the repository's [test invocation guidance](../../.claude/docs/running-tests.md) and [test quality guidelines](../../.claude/docs/test-documentation-and-quality-guidelines.md).
