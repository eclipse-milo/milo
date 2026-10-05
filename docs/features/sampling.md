# Sampling framework

Application setup for Milo 1.2.0-SNAPSHOT is maintained in the [Server Sampling guide](https://github.com/eclipse-milo/milo/wiki/Server-Sampling). This file keeps implementation and test entry points with the source.

<a id="table-of-contents"></a>
<a id="overview"></a>
<a id="usage"></a>
<a id="default-sampling"></a>
<a id="configuration"></a>
<a id="explicit-manager-wiring"></a>

## Application guide

Use the Wiki for [manager configuration and lifecycle](https://github.com/eclipse-milo/milo/wiki/Server-Sampling#configure-one-manager), interval revision, and default address-space sampling.

<a id="custom-sampling-group"></a>

## Custom groups

See [custom groups and device batching](https://github.com/eclipse-milo/milo/wiki/Server-Sampling#custom-groups-and-device-batching) for the group factory, delivery contract, failure handling, and driver-owned timeouts. The in-tree [DeviceNamespace](../../milo-examples/server-examples/src/main/java/org/eclipse/milo/examples/server/sampling/DeviceNamespace.java) and [DeviceSamplingGroup](../../milo-examples/server-examples/src/main/java/org/eclipse/milo/examples/server/sampling/DeviceSamplingGroup.java) remain executable source examples.

<a id="read-access-policy"></a>
<a id="sampling-outside-the-framework"></a>

## Authorization and external producers

The Wiki documents [read-access policies and external producers](https://github.com/eclipse-milo/milo/wiki/Server-Sampling#authorization-and-external-producers), including refresh responsibilities for a quiet push source. Permission changes also require the [access-control invalidation workflow](https://github.com/eclipse-milo/milo/wiki/Server-Access-Control#permission-changes-and-cache-invalidation).

<a id="migrating-from-subscriptionmodel"></a>
<a id="troubleshooting"></a>

## Migration and troubleshooting

See [migration and cleanup](https://github.com/eclipse-milo/milo/wiki/Server-Sampling#migration-and-cleanup) for replacing `SubscriptionModel`, avoiding duplicate producers, and diagnosing stalled sampling. The [1.2 release guide](https://github.com/eclipse-milo/milo/wiki/Release-Notes-1.2.0#sampling-and-read-access) records the version changes.

<a id="how-it-works"></a>

## Implementation

Read the [sampling architecture](../architecture/sampling-framework.md) before changing group scheduling or access refresh. It retains the ownership model, turn ordering, membership races, cache invalidation, and runtime boundaries. The [package documentation](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/sampling/package-info.java) describes the lifecycle and collaboration contracts alongside the implementation.

The architecture's [implementation reading guide](../architecture/sampling-framework.md#reading-the-implementation) connects the manager, groups, access cache, item delivery gate, and managed address space. Keep changes to these boundaries documented there.

## Testing

The [sampling unit tests](../../opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/sampling) exercise scheduling and membership races. [SamplingFrameworkTest](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/server/sampling/SamplingFrameworkTest.java) checks client-visible behavior. The Wiki links its additional runnable examples.

Use the repository's [test invocation guidance](../../.claude/docs/running-tests.md) and [test quality guidelines](../../.claude/docs/test-documentation-and-quality-guidelines.md) when changing these tests.
