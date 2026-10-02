# Access control, roles, and permissions

A Milo server decides what each Session may read, write, browse, and call from three inputs:
the Session's identity and SecureChannel, the roles a `RoleMapper` derives from them, and the
access attributes its Nodes expose for that Session. This guide covers configuring those inputs,
what each service checks, the results a client sees, and what to do when permissions change
while clients are subscribed. How the server samples MonitoredItems and keeps their access
decisions current is covered in the [sampling framework guide](sampling.md).

* * *

## Table of contents

- [Overview](#overview)
- [How it works](#how-it-works)
- [Configuration](#configuration)
- [Service checks](#service-checks)
- [Read results and monitored items](#read-results-and-monitored-items)
- [Changing permissions at runtime](#changing-permissions-at-runtime)
- [Troubleshooting](#troubleshooting)
- [Testing](#testing)

* * *

## Overview

```text
Session identity and endpoint security
  -> RoleMapper: role ids for the Session
  -> Node attributes, through attribute filters: effective user attributes for the Session
  -> AccessController: one result per operation
  -> service result, or the stored read access result of a MonitoredItem
```

The `AccessController` is the only component that decides. It reads a Node's access attributes
for the requesting Session and applies the rules in [Service checks](#service-checks). It does
not know how those attributes were produced; that is the application's job. The controller, the
cache of read decisions that samplers use, and the invalidation that keeps the cache current are
grouped in the server's `AccessControlManager`, reached through `server.getAccessControlManager()`.

One consequence matters more than any other in this guide. Milo does not derive effective user
attributes from role metadata. Setting `RolePermissions` on a Node and configuring a
`RoleMapper` does not change what `UserAccessLevel` or `UserRolePermissions` return for a
Session on an ordinary `UaNode`. The application supplies those effective attributes, usually
with an attribute filter that consults the Session, or with a custom controller. When role
metadata is absent the default controller skips the role checks rather than inferring a policy.

## How it works

The attributes with "User" in their name are the effective, per-Session view; the others
describe the Node itself (Part 3 §5.6.2).

| Attribute or API | Role in the default controller |
| --- | --- |
| `AccessLevel` | What the Variable can do at all. A Value read requires CurrentRead; a Value write requires CurrentWrite. |
| `UserAccessLevel` | What this Session may do with the Value. Its CurrentRead and CurrentWrite are checked after the `AccessLevel` bits. |
| `AccessRestrictions` | Whether the channel must sign or encrypt. Checked first, for every service. |
| `UserWriteMask` | Which non-Value attributes this Session may write. |
| `UserExecutable` | Whether this Session may call the Method. |
| `UserRolePermissions` | Role-keyed permissions matched against the Session's role ids for Browse, Call, AddReferences, DeleteNodes, DeleteReferences, reading RolePermissions, and writing RolePermissions or Historizing. |
| `RoleMapper` | Configured on the server. Maps identity, client application URI, and endpoint to role ids. `Session.getRoleIds()` asks it on every call. |

For each check the controller reads seven attributes per distinct Node: NodeClass,
AccessRestrictions, UserWriteMask, AccessLevel, UserAccessLevel, UserExecutable, and
UserRolePermissions. A filter that supplies any of them is part of authorization. It must be
quick and thread-safe, and it must not call `checkReadAccess()` itself, because the controller
is reading that very attribute to make its decision.

Ordinary Read and Write are checked in the service layer and only then handed to
`AddressSpace.read()` or `write()`, which do not check again. The sampling framework follows the
same split: a group refreshes its items' read access and then reads the permitted ones through
the lower-level method.

## Configuration

### Static access

For a Variable whose access does not depend on who asks, set both attributes and keep them
consistent:

```java
node.setAccessLevel(AccessLevel.toValue(AccessLevel.READ_ONLY));
node.setUserAccessLevel(AccessLevel.toValue(AccessLevel.READ_ONLY));
```

### Session-dependent access

Expose `UserAccessLevel` through an attribute filter that looks at the Session. The example
server's [`RestrictedAccessFilter`][restricted-access-filter] maps the Session's `Identity` to a
set of access levels and delegates every other attribute to the Node:

```java
node.getFilterChain()
    .addLast(
        new RestrictedAccessFilter(
            identity ->
                identity instanceof Identity.UsernameIdentity user
                        && "admin".equals(user.getUsername())
                    ? AccessLevel.READ_WRITE
                    : AccessLevel.NONE));
```

A call with no Session is the server reading its own Node, and the filter answers it with full
access rather than treating it as an anonymous client. The filter runs on every check, so a
decision it makes from mutable state, such as the device lock in the
[`DeviceNamespace` example][device-namespace], changes as soon as that state does, except where
the answer was cached (see [Changing permissions at runtime](#changing-permissions-at-runtime)).

Do not put the only permission check in a Value filter. A custom sampler reads the device
directly and never passes through that filter; the access attributes and the item's stored
result are what every path shares.

### Role mapping

Configure a `RoleMapper` with `OpcUaServerConfigBuilder.setRoleMapper()`. Implement
`getRoleIds(Identity)`, or the overload that also receives the client application URI and the
`EndpointDescription` when those affect the roles. A filter can then decide from roles:

```java
Predicate<Session> isOperator =
    session -> session.getRoleIds().orElse(List.of()).contains(operatorRoleId);
```

Define explicitly which roles an anonymous or unrecognized identity receives. For Browse, Call,
and the node-management services, expose `UserRolePermissions` from the same policy; a Value
filter supplies only `UserAccessLevel`.

### Custom controller

An application whose rules go beyond attributes installs its own `AccessController` through the
configuration. The factory receives the server, so a controller that adds rules can wrap the
default one:

```java
OpcUaServerConfig.builder()
    .setAccessControllerFactory(
        server -> new AuditingAccessController(server, new DefaultAccessController(server)));
```

Preserve the default's denials and its `AccessResult.NODE_UNKNOWN`. Unknown is "no decision":
it lets Read report `Bad_NodeIdUnknown` and lets a refresher leave a MonitoredItem's last result
in place.

## Service checks

Every check begins with `AccessRestrictions` against the channel's security mode and reports
`Bad_SecurityModeInsufficient` when it is not met; Browse applies that only when the
restriction's ApplyRestrictionsToBrowse flag is set. Then, per service:

| Service | Checked | Denied with |
| --- | --- | --- |
| Read, Value | `AccessLevel` CurrentRead, then `UserAccessLevel` CurrentRead | `Bad_NotReadable`, then `Bad_UserAccessDenied` |
| Read, RolePermissions | `UserRolePermissions` ReadRolePermissions for the Session's roles | `Bad_UserAccessDenied` |
| Write, Value | `AccessLevel` CurrentWrite, then `UserAccessLevel` CurrentWrite | `Bad_NotWritable`, then `Bad_UserAccessDenied` |
| Write, other attributes | `UserWriteMask`; for RolePermissions and Historizing also `UserRolePermissions` WriteRolePermissions or WriteHistorizing | `Bad_UserAccessDenied` |
| Browse | `UserRolePermissions` Browse | `Bad_UserAccessDenied` |
| Call | `UserRolePermissions` Call on both the Object and the Method, then the Method's `UserExecutable` | `Bad_UserAccessDenied` |
| AddReferences, DeleteReferences | `UserRolePermissions` AddReference or RemoveReference on the source Node | `Bad_UserAccessDenied` |
| DeleteNodes | `UserRolePermissions` DeleteNode | `Bad_UserAccessDenied` |

A role check runs only when the Session has role ids and the Node has `UserRolePermissions`;
otherwise it is skipped. An invalid attribute id is `Bad_AttributeIdInvalid` before anything
else. `AccessLevelEx`, HistoryRead, and event MonitoredItems are not part of these checks.

## Read results and monitored items

A Read distinguishes a Variable nobody can read from one this user cannot read. The same
distinction decides what happens when a client creates a data MonitoredItem for it.

| Situation | Read result | CreateMonitoredItems |
| --- | --- | --- |
| `AccessLevel` lacks CurrentRead | `Bad_NotReadable` | Succeeds; the denial is reported through Publish. |
| `UserAccessLevel` lacks CurrentRead | `Bad_UserAccessDenied` | Succeeds; the denial is reported through Publish. |
| Channel does not meet `AccessRestrictions` | `Bad_SecurityModeInsufficient` | Fails with that status. |
| Allowed | The AddressSpace's value or its own error | Normal sampling. |

When both access-level attributes deny, `Bad_NotReadable` wins. An event MonitoredItem is still
rejected at creation by any denial; it has no sampled value to carry a status.

Part 4 §5.13.2.1 requires the server to report a denied read on the MonitoredItem, including
when access changes after it was created. The SDK does this by storing a read access result on
every `MonitoredDataItem` and applying it to each value a sampler delivers:

```text
allowed:  setValue(value) -> data-change filter -> notification queue
denied:   setValue(value) -> a DataValue carrying the denial status and no value, instead
restored: the next value is reported even if it equals the last one before the denial
```

The client sees an ordinary DataChangeNotification whose DataValue has `Bad_UserAccessDenied`
or `Bad_NotReadable` as its status and no value. A change to a denial queues that status at
once, without waiting for a sample. A Disabled item queues nothing until monitoring resumes.
Values queued before the decision changed are not purged, so a client can receive a last good
value ahead of the denial.

The stored result is a snapshot. It does not re-check permissions on every `setValue()`; some
component has to refresh it. For items sampled by the framework, the group does so before every
sample. For an application that samples on its own, see
[Sampling outside the framework](sampling.md#sampling-outside-the-framework). An
application-defined `DataItem` implementation inherits no-op access methods and must enforce the
result itself if it wants these guarantees.

## Changing permissions at runtime

A Session's access can change without a new Session: an external role assignment, a Node's
attributes, a filter's policy, or an identity change through ActivateSession. The SDK re-checks
on its own for an identity or endpoint change, a Subscription transfer, and a Session close.
For everything the application controls, commit the change first and then invalidate:

```java
node.setUserAccessLevel(AccessLevel.toValue(AccessLevel.NONE));
server.getAccessControlManager().invalidateReadAccess(node.getNodeId());
```

Invalidation drops the matching entries from the server's read access cache and tells every
`ReadAccessListener`. Choose the narrowest scope that covers the change:

```java
AccessControlManager access = server.getAccessControlManager();

access.invalidateReadAccess(nodeId);                                    // one Node, every Session
access.invalidateReadAccess(session);                                   // every Node, one Session
access.invalidateReadAccess(ReadAccessScope.namespace(namespaceIndex)); // a whole namespace
access.invalidateReadAccess(ReadAccessScope.nodes(nodeIds).forSession(session));
access.invalidateReadAccess(ReadAccessScope.matching(nodeId -> isDeviceNode(nodeId)));
access.invalidateReadAccess();                                          // everything
```

A `matching` predicate runs under the cache's write lock during invalidation, so it must be
quick and must not block. Removing a Node and adding another with the same NodeId also needs an
invalidation. An unchanged identity does not mean unchanged permissions.

Invalidation does not change any MonitoredItem's stored result by itself. A framework group
picks up the new answer on its next cycle; an application refresher does so when its
`ReadAccessListener` prompts it. The [sampling guide](sampling.md#read-access-policy) describes
the cache, which policy to choose, and how stale a stored result can be.

## Troubleshooting

| Symptom | Check |
| --- | --- |
| Value read or MonitoredItem reports `Bad_NotReadable` | The Node's `AccessLevel` CurrentRead bit. |
| Value read or MonitoredItem reports `Bad_UserAccessDenied` | The effective `UserAccessLevel` for that Session: the filter or policy that supplies it, and the identity it saw. |
| Write reports `Bad_NotWritable` | The Node's `AccessLevel` CurrentWrite bit, or for a non-Value attribute its `WriteMask`. |
| Any request reports `Bad_SecurityModeInsufficient` | `AccessRestrictions` against the Session's endpoint security mode. |
| Setting `RolePermissions` changed nothing | Ordinary UaNodes do not derive effective attributes from it; supply `UserAccessLevel` or `UserRolePermissions` from a filter. |
| A permission change has no effect on subscribed clients | Was `invalidateReadAccess` called after the change, with a scope that covers the Node and Session? On the per-cycle policy, has a cycle run since? |
| An old value arrives before the denial | Values queued before the decision changed are not purged. |

## Testing

[`DefaultAccessControllerTest`][controller-test] pins every rule in the service table, including
the order of `Bad_NotReadable` and `Bad_NotWritable` before `Bad_UserAccessDenied`.
[`MonitoredItemReadAccessTest`][item-test] drives a client against a server and checks what
Publish carries when an item is created denied, when access is revoked, and when it is restored.
The example server's [`DeviceNamespace`][device-namespace] is a runnable version of this guide:
connect as `user` and as `admin`, subscribe to a register, and write `Device/Locked`.

The read-side distinction and Publish reporting arrived in
[#2076](https://github.com/eclipse-milo/milo/pull/2076); the invalidation API and the sampling
framework that refreshes stored results in [#2089](https://github.com/eclipse-milo/milo/pull/2089).

[restricted-access-filter]: ../../milo-examples/server-examples/src/main/java/org/eclipse/milo/examples/server/RestrictedAccessFilter.java
[device-namespace]: ../../milo-examples/server-examples/src/main/java/org/eclipse/milo/examples/server/sampling/DeviceNamespace.java
[controller-test]: ../../opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/access/DefaultAccessControllerTest.java
[item-test]: ../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/server/subscriptions/MonitoredItemReadAccessTest.java
