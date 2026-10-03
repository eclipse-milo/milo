# Sampling framework

The sampling framework produces the values of a server's data MonitoredItems. Each
`ManagedAddressSpace` owns a `SamplingManager`, the manager keeps one `SamplingGroup` per
sampling interval, and each group's cycle refreshes its items' read access and then collects
their values. The default group reads through the AddressSpace; a device driver replaces that
one step with its own protocol read and keeps the rest. This guide covers using the default,
configuring it, choosing how read access is refreshed, writing a custom group, and migrating
from `SubscriptionModel`. Permission configuration is covered in
[Access control, roles, and permissions](access-control.md).

* * *

## Table of contents

- [Overview](#overview)
- [How it works](#how-it-works)
- [Usage](#usage)
  - [Default sampling](#default-sampling)
  - [Configuration](#configuration)
  - [Read-access policy](#read-access-policy)
  - [Custom sampling group](#custom-sampling-group)
  - [Explicit manager wiring](#explicit-manager-wiring)
  - [Sampling outside the framework](#sampling-outside-the-framework)
- [Migrating from SubscriptionModel](#migrating-from-subscriptionmodel)
- [Troubleshooting](#troubleshooting)
- [Testing](#testing)

* * *

## Overview

| Your server | Start at |
| --- | --- |
| A managed namespace whose values live in its Nodes and attribute filters | [Default sampling](#default-sampling) |
| A managed namespace whose permissions change at known times | [Read-access policy](#read-access-policy) |
| A device driver that batches or optimizes requests | [Custom sampling group](#custom-sampling-group) |
| A direct `AddressSpace` implementation | [Explicit manager wiring](#explicit-manager-wiring) |
| An existing scheduler or push-based value source | [Sampling outside the framework](#sampling-outside-the-framework) |

The example server's [`DeviceNamespace`][device-namespace] is a complete, runnable custom
integration: a simulated device read in one request per cycle, the cached read-access policy,
and invalidation when an operator locks the device.

## How it works

A data MonitoredItem's sampling interval says how often the server collects a value; its
Subscription's publishing interval says how often notifications are sent. The framework owns
the first. Items are grouped by interval, and each group runs one turn at a time:

```text
cycle due
  tell the subclass if membership changed      onItemsChanged(all members)
  refresh read access for the items to sample  ReadAccessPolicy.check(...)
  apply the results to the items               item.setReadAccessResult(...)
  read the items whose Session may read them   sample(permitted items)
  when the returned stage completes, schedule the next cycle
```

A new or re-enabled item gets a debounced initial sample through the same sequence. An item
whose Session may not read it is left out of `sample()`; its denial is already queued by the
item. Whatever a sampler then delivers passes through the item's stored result, so a sampler
cannot leak a value, only fail to refresh. The
[architecture overview](../architecture/sampling-framework.md) explains the boundaries, the
invariants, and the reasons behind them.

## Usage

### Default sampling

`ManagedAddressSpace` creates its manager on first use and forwards the four data-item
callbacks to it. `ManagedNamespaceWithLifecycle`, `ManagedAddressSpaceWithLifecycle`, and
`ManagedAddressSpaceFragmentWithLifecycle` start the manager with their registration and stop
it before unregistering. A managed namespace needs no sampling code at all.

The default `AddressSpaceSamplingGroup` reads each Session's items with one
`AddressSpace.read()` per Session, split at `MaxNodesPerRead`, through the Node filter chain,
and delivers each value with the timestamps the item asked for. It does not authorize again
inside that read: an ordinary client Read is authorized by the service layer before it reaches
`AddressSpace.read()`, and sampling has already authorized in the refresh step.

### Configuration

Override `samplingManagerConfig()` in your managed address space. The manager captures the
configuration when it is created.

| Setting | Default | Effect |
| --- | --- | --- |
| `bucketMillis` | 50 | Nearby intervals share a group. Zero disables bucketing. |
| `minimumIntervalMillis` | 1 | The fastest a group runs, including for a revised interval of zero. |
| `initialSampleDelayMillis` | 100 | How long a new item waits for more new items before its first sample. |
| `initialSampleMaxWindowMillis` | 500 | How long a steady stream of new items can postpone that first sample. |
| `overrunWarningMultiple` | 3 | Log a turn that has not completed after this many intervals. |
| `readAccessPolicy` | `perCycle()` | How a group refreshes read access before each sample. |

A requested interval is revised to the one the framework samples at: rounded up to a whole
millisecond, raised to the minimum, then rounded up to the next bucket multiple. Part 4 §7.21
requires the revised interval to be equal to or higher than the requested one and to be the
interval the item is assigned, so `ManagedAddressSpace` returns it from `onCreateDataItem()` and
`onModifyDataItem()` and the client is told the interval in effect. With the defaults:

| Requested | Revised and sampled at |
| --- | --- |
| 0 ms | 50 ms |
| 1.2 ms | 50 ms |
| 100 ms | 100 ms |
| 120 ms | 150 ms |
| 250 ms | 250 ms |

A device that cannot poll faster than some rate sets the minimum to it, as the device example
does, and the revision picks it up:

```java
@Override
protected SamplingManagerConfig samplingManagerConfig() {
  return SamplingManagerConfig.defaults().withMinimumIntervalMillis(100);
}
```

Configure the manager through `samplingManagerConfig()` rather than wiring another beside it;
the revision uses that manager's configuration, so a second manager would sample at intervals
the client was not told.

The initial-sample window bounds how long new items are batched, not how soon the first value
arrives; a busy group or a slow read can delay it further. The overrun warning only logs.

### Read-access policy

| Policy | What the group does | What you provide |
| --- | --- | --- |
| `ReadAccessPolicy.perCycle()`, the default | Asks the `AccessController` once per Session on every refresh. | Nothing. Correct as long as your attributes and filters are. |
| `ReadAccessPolicy.cached()` | Answers from the server's `ReadAccessCache` and checks only the misses. | An `invalidateReadAccess` call for every change that can alter an answer. |
| Your own `ReadAccessPolicy` | Applies whatever your `check()` returns. | Session isolation, freshness, and your own cache's invalidation. |

The per-cycle policy costs what a Read costs: for the default controller, seven attribute reads
per distinct Node per Session per cycle. Choose the cached policy when that cost matters and
your namespace knows every event that changes an answer:

```java
@Override
protected SamplingManagerConfig samplingManagerConfig() {
  return SamplingManagerConfig.defaults().withReadAccessPolicy(ReadAccessPolicy.cached());
}
```

The cache is one per server, owned by its `AccessControlManager` and shared by every sampler
and by application refreshers. Its key is
the Session object, the NodeId, and the AttributeId. Grants and denials both stay until an
invalidation drops them or the Session closes; there is no time-to-live and no size limit. A
different Session for the same user has its own entries. Index range and data encoding are not
part of the key, so a rule that depends on either needs the per-cycle policy or a custom one.
Selecting the cached policy caches sampling's checks only; Read and CreateMonitoredItems still
ask the controller.

The SDK invalidates for a Session's identity or endpoint change, a Subscription transfer, and a
Session close, and for the first three also re-checks the affected items itself. The
application invalidates for its own changes, with the scopes shown in
[Changing permissions at runtime](access-control.md#changing-permissions-at-runtime). If you
cannot name every such change, stay on the per-cycle policy.

What an invalidation does and does not do:

- It drops cache entries and tells `ReadAccessListener`s. It does not change any item's stored
  result, wake a group, cancel a device request in flight, or purge queued notifications. The
  next cycle picks up the new answer.
- A check that an invalidation overtakes is made again, up to three times, and an answer that
  keeps being overtaken is used once without being cached.
- A check that throws, returns no answer, or returns `AccessResult.NODE_UNKNOWN` leaves the
  item's last result in place. A retained grant keeps delivering until a later refresh sees the
  revocation; a retained denial keeps withholding. If your application needs revocation to take
  effect faster than the sampling cadence, it has to coordinate the change, the refresh, and
  delivery itself.

A custom policy implements one method, `check(OpcUaServer, List<DataItem>)`, returning a result
for each item it has an answer for. Resolve each item's Session at call time, key any cache of
your own by Session, and treat an omitted result as "keep the previous decision", not as a
denial. Return the results and let the group apply them; the group refuses to apply a result
from a check that started before the item left or rejoined, or moved to another Session.

### Custom sampling group

A subclass of `SamplingGroup` replaces value collection and keeps everything else. It gets the
whole membership in `onItemsChanged()` when items are added or removed, which is where a driver
rebuilds its request plan, and the items to read in `sample()`, which can be fewer: the items
whose Session may read them, or only the new items of an initial sample. Permission changes do
not call `onItemsChanged()`. Reading more registers than this turn's items cover is harmless,
since delivery is gated per item.

The example server's [`DeviceSamplingGroup`][device-group] reads every register behind its items
in one request per cycle. Its core:

```java
public final class DeviceSamplingGroup extends SamplingGroup {

  private volatile List<String> registers = List.of();

  @Override
  protected void onItemsChanged(List<DataItem> items) {
    registers = items.stream().map(this::registerOf).filter(Objects::nonNull).distinct().toList();
    setRequestCount(registers.isEmpty() ? 0 : 1);
  }

  @Override
  protected CompletionStage<@Nullable Void> sample(List<DataItem> items) {
    return device
        .read(registers)
        .handle(
            (values, failure) -> {
              for (DataItem item : items) {
                DataValue value = failure == null ? values.get(registerOf(item)) : null;

                deliver(
                    item,
                    value != null ? value : new DataValue(StatusCodes.Bad_CommunicationError));
              }

              if (failure != null) {
                throw new CompletionException(failure);
              }
              return null;
            });
  }
}
```

`deliver()` trims the value to the timestamps the item asked for and hands it to the item, so a
driver reads with both timestamps and never implements that rule. The full example also routes
items the device cannot answer for, such as a non-Value attribute, through `AddressSpace.read()`.
A device sampler bypasses the Node's Value filter; if a filter transforms values per Session,
apply the same transformation or use `AddressSpaceSamplingGroup` for those Nodes.

Install the group from your namespace:

```java
@Override
protected SamplingGroupFactory samplingGroupFactory() {
  return (server, intervalMillis) ->
      new DeviceSamplingGroup(server, intervalMillis, device, this, this::registerOf);
}
```

Rules a sampler has to live by:

- Return a stage that completes when every value has been delivered or failed, not when the
  request was sent. The group holds its turn until then and times the next cycle from it. A
  stage that never completes stalls the group, and the overrun watchdog only logs.
- Return promptly from `sample()` if the transport is asynchronous. The refresh and the default
  address-space read are synchronous and occupy a server worker thread while they run.
- `onItemsChanged()` and `sample()` never overlap. `onItemsAdded()` and `onItemsRemoved()` run on
  the thread that changed membership and can overlap a sample in progress, so guard any per-item
  resource they manage.
- An exception from `sample()`, or an exceptional stage, is logged and the next cycle runs. It
  does not publish bad quality on its own; deliver a bad `DataValue` if that is what you want.
  An exception from `onItemsChanged()` is logged too, and the change flag is consumed, so keep a
  usable plan or rebuild it lazily.
- The framework serializes turns per group, not requests per device. It sets no transport
  timeouts and cancels nothing on shutdown. Those belong to the driver.

### Explicit manager wiring

An `AddressSpace` that does not extend `ManagedAddressSpace` creates and owns a manager itself:

```java
public abstract class ManuallySampledAddressSpace implements AddressSpace, Lifecycle {

  private final SamplingManager sampling;

  protected ManuallySampledAddressSpace(OpcUaServer server) {
    sampling =
        new SamplingManager(
            server, (s, intervalMillis) -> new AddressSpaceSamplingGroup(s, this, intervalMillis));
  }

  @Override
  public void startup() {
    sampling.startup();
  }

  @Override
  public void shutdown() {
    sampling.shutdown();
  }

  @Override
  public void onDataItemsCreated(List<DataItem> items) {
    sampling.onDataItemsCreated(items);
  }

  @Override
  public void onDataItemsModified(List<DataItem> items) {
    sampling.onDataItemsModified(items);
  }

  @Override
  public void onDataItemsDeleted(List<DataItem> items) {
    sampling.onDataItemsDeleted(items);
  }

  @Override
  public void onMonitoringModeChanged(List<MonitoredItem> items) {
    sampling.onMonitoringModeChanged(items);
  }
}
```

A third constructor argument takes a `SamplingManagerConfig`. Return
`config.reviseSamplingInterval(requested)` from `onCreateDataItem()` and `onModifyDataItem()`,
as `ManagedAddressSpace` does, so the client is told the interval the manager samples at.
Callbacks that arrive before `startup()` are kept and sampled once it runs; callbacks after
`shutdown()` are ignored. The lifecycle is one-shot: create a new manager rather than restarting
one.

### Sampling outside the framework

A server can keep an existing device scheduler or a push-based source that calls
`DataItem.setValue()` itself. Override the managed data-item callbacks so the inherited manager
does not also sample those items. `ManagedAddressSpace` still revises requested intervals to the
inherited manager's buckets in `onCreateDataItem()` and `onModifyDataItem()`, so override those
too if your sampler supports other intervals; whatever they return is what the items carry and
the client is told. Then give one component the job of keeping the items' stored read access
results current. The SDK provides the observations; the ordering is yours.

| Hook | What it tells you |
| --- | --- |
| `OpcUaServer.addDataItemListener()` | Every data item created, deleted, mode-changed, or transferred on the server. Synchronous, before the AddressSpace's own callback. No modified-item callback. |
| `AccessControlManager.addReadAccessListener()` | A scope whose cached answers were just dropped. Synchronous, on the invalidating thread. |
| `SessionManager.addSessionListener()` | Identity and endpoint changes and Session lifecycle. Asynchronous; pending callbacks can be skipped at shutdown. |
| `AddressSpace.onDataItemsTransferred()` | Your own items that moved to another Session, already pointing at it and already checked for it. |

Register before items exist, or seed the coordinator with the items that already do. Keep the
synchronous listeners short: record what changed and enqueue the work.

```java
server.getAccessControlManager().addReadAccessListener(scope -> coordinator.markStale(scope));
```

A refresh task snapshots the live items with their Sessions, checks them with
`server.getAccessController().checkReadAccess(session, readValueIds)` or through
`server.getAccessControlManager().getReadAccessCache().getOrCheck(session, readValueIds)`, and applies each result with
`item.setReadAccessResult()` only if the item is still tracked, still on that Session, and no
newer refresh has been requested for it. `setReadAccessResult()` is thread-safe but applies calls
in arrival order and cannot tell an old allowance from a new one, so the coordinator serializes
or versions its checks. The SDK re-checks a Session's items itself when its identity or
endpoint changes and checks transferred items for their new Session; re-enabled items need a
refresh before their next value, and so does every change the application makes. Repeated cache
hits do not bound staleness, so a periodic
fallback either asks the controller directly or invalidates before re-checking. Do not run such
a refresher alongside a framework group for the same items.

## Migrating from SubscriptionModel

`SubscriptionModel` is deprecated and is now an adapter over a `SamplingManager` with default
groups and the default configuration without bucketing, so existing forwarding keeps working and
each item is still sampled at the interval its AddressSpace reported. For a managed namespace with a
lifecycle, delete the field and its construction, its lifecycle registration, and the four
callback overrides that forwarded to it; the inherited manager, callbacks, and lifecycle take
over. [Commit b44aee410][migration-commit] does exactly this for the six in-tree namespaces. If
an override also did other work, keep that work and call the inherited callback. If it forwarded
to a different sampler, pick one owner for those items rather than forwarding to both. A direct
`AddressSpace` uses [explicit wiring](#explicit-manager-wiring).

What changes for a managed namespace, whether or not it deletes the forwarding: a requested
interval is revised up to the next supported one and reported to the client, so a request of 0
becomes 50 ms by default where `SubscriptionModel` polled at 1 ms. A direct `AddressSpace` that
keeps forwarding keeps its intervals, since its own `onCreateDataItem()` still decides them. For
both: new items get a debounced initial sample; a membership change no longer rebuilds every schedule; a
sampling exception no longer stops the schedule; reads are split at `MaxNodesPerRead`; callbacks
before startup wait instead of throwing; and callbacks after shutdown are ignored.

## Troubleshooting

| Symptom | Check |
| --- | --- |
| Custom sampler never runs | Is the manager started, are the callbacks forwarded, is the item's monitoring mode Disabled, or is every candidate denied? `getSamplingManager().getGroups()` shows interval, item count, and request count. |
| Sampling stops after an overrun warning | A stage that never completed, or a refresh or synchronous read that blocked. |
| Optimizer does not rerun | Membership is unchanged; permission changes and same-bucket interval changes do not mark it changed. A rebuild that threw is not retried. |
| Values arrive twice per interval | An inherited manager and a custom or legacy sampler both own the items. |
| Many blocked worker threads | A synchronous read or an access-attribute filter is doing I/O on the shared platform-thread executor. |
| The revised interval is higher than requested | Expected: it is the next supported interval, a bucket multiple at or above the minimum. Lower `bucketMillis` or `minimumIntervalMillis` if the device allows. |
| A permission change does not reach subscribed clients | See [Changing permissions at runtime](access-control.md#changing-permissions-at-runtime). |

## Testing

The [sampling unit tests][unit-tests] drive `SamplingGroup` and `SamplingManager` with a manual
scheduler and pin the turn ordering, the initial sample, and the membership races.
[`SamplingFrameworkTest`][integration-test] runs a client against a server and covers the cached
policy with invalidation, recovery after a sampler failure, and the interval floor. The example
server's [`DeviceNamespace`][device-namespace] exercises a custom group end to end.

[device-namespace]: ../../milo-examples/server-examples/src/main/java/org/eclipse/milo/examples/server/sampling/DeviceNamespace.java
[device-group]: ../../milo-examples/server-examples/src/main/java/org/eclipse/milo/examples/server/sampling/DeviceSamplingGroup.java
[migration-commit]: https://github.com/eclipse-milo/milo/commit/b44aee410387fc2d0865d5aa1627cc25068ec054
[unit-tests]: ../../opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/sampling
[integration-test]: ../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/server/sampling/SamplingFrameworkTest.java
