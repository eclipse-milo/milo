# CertificateGroup Redesign for Client and Server: Implementation Plan

Make `CertificateGroup` the complete unit of local identity and trust on both sides of Milo: groups
carry no NodeId and no `CertificateFactory`, servers register groups under NodeIds in the
`CertificateManager`, clients configure exactly one group and nothing else, and provisioning is a
factory-side helper. Delivered as breaking changes on `integration/1.2`.

Based on eclipse-milo/milo issue [#1861](https://github.com/eclipse-milo/milo/issues/1861) (remove
`CertificateFactory` from `CertificateGroup`; its downstream-survey comment records Ignition's
usage), the design conversation of 2026-09-03 in the Ignition `feature/opc-ua-gds-pull` session
(summarized under [Current State](#current-state) because no separate design document exists), and
OPC 10000-12 (Part 12) clause 7.8, which defines `CertificateGroupId` as the NodeId of a
`CertificateGroupType` instance under `ServerConfiguration.CertificateGroups`.

**Plan ID:** `certificate-group-redesign`
**Status:** Implemented
**Parent manifest:** None
**Grounded against:** `integration/1.2` at `9bbb41808` in `~/Development/ThirdParty/milo`
(working tree also holds two untracked files, `EndpointDescriptions.java` and its test, unrelated
to this plan); inspected 2026-09-03
**Re-ground before:** None

* * *

## Table of Contents

- [Progress](#progress)
- [Scope](#scope)
- [Current State](#current-state)
- [Desired End State](#desired-end-state)
- [Assumptions and Gaps](#assumptions-and-gaps)
- [Prerequisites](#prerequisites)
- [Implementation Map](#implementation-map)
- [WP1 — Core model: groups without ids or factories, manager as NodeId registry](#work-package-1-core-model-groups-without-ids-or-factories-manager-as-nodeid-registry)
- [WP2 — Client configures one group](#work-package-2-client-configures-one-group)
- [WP3 — Quarantine belongs to the group](#work-package-3-quarantine-belongs-to-the-group)
- [Rejected Splits](#rejected-splits)
- [File Inventory](#file-inventory)
- [Verification Summary](#verification-summary)
- [Downstream Handoff](#downstream-handoff)

## Progress

- [x] [WP1 — Core model: groups without ids or factories, manager as NodeId registry](#work-package-1-core-model-groups-without-ids-or-factories-manager-as-nodeid-registry)
- [x] [WP2 — Client configures one group](#work-package-2-client-configures-one-group)
- [x] [WP3 — Quarantine belongs to the group](#work-package-3-quarantine-belongs-to-the-group)

* * *

## Scope

This plan covers:

1. `CertificateGroup` loses `getCertificateGroupId()` and `getCertificateFactory()`, gains
   `hasCertificate(NodeId)`; `Entry` loses its group id; `CertificateIdentity` carries a
   `CertificateGroup` reference instead of a group NodeId.
2. `CertificateManager` becomes the server-side registry that maps `CertificateGroupType` NodeIds to
   groups (`getCertificateGroup(NodeId)`, new `getCertificateGroupId(CertificateGroup)`), with
   `DefaultCertificateManager.addCertificateGroup(NodeId, CertificateGroup)` and an insertion-ordered
   group list that defines cross-group selection precedence.
3. Identity selection takes candidate groups, not a manager plus a group id; the explicit-certificate
   pin stays for the server's per-endpoint certificate only.
4. `DefaultApplicationGroup` drops the id and factory constructor parameters and the deprecated
   `createAndInitialize` statics; provisioning moves to
   `CertificateFactory.createMissingCertificates(CertificateGroup)` (issue #1861, including its
   presence-check resolution). Renamed to `DefaultCertificateGroup`.
5. `OpcUaClientConfig` takes one `CertificateGroup` and loses `setCertificateManager`,
   `setCertificateGroupId`, `setKeyPair`, `setCertificate`, and `setCertificateChain`; the client
   transport and session code lose their fixed-certificate fallbacks; the ApplicationUri derives
   from the connection's group; a group-of-one helper replaces the fixed-certificate use case.
6. The quarantine moves from the manager to the group (WP3).
7. Package docs, `docs/features/gds-client.md`, and the examples describe the new model.

**Out of scope:**

- Making `CertificateGroup` `AutoCloseable`. Groups borrow their `TrustListManager` and
  `CertificateStore` (the `TrustListManager` contract says borrowers do not close), so lifecycle
  stays with the application that owns the resources.
- Dropping `CertificateFactory.createCertificateChain` (#1861 question 3). The provisioning helper
  needs it; revisit when `SelfSignedCertificateBuilder` covers every supported type directly.
- Server-side Push (`CreateSigningRequest`, `UpdateCertificate`) and any per-group factory lookup for
  it (#1861 question 2). The NodeId registry this plan adds is the seam a future Push
  implementation resolves groups through.
- Ignition adoption. Recorded in [Downstream Handoff](#downstream-handoff) for the consuming
  branch.

* * *

## Current State

**The abstraction was designed from the server side.** `CertificateGroup`
(`opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateGroup.java`)
requires `NodeId getCertificateGroupId()` because on a server every group is a
`CertificateGroupType` node. Milo 1.2 reuses the same interface and `CertificateManager` on the
client (`OpcUaClientConfig.setCertificateManager` / `setCertificateGroupId`, since #1846), where
there is no address space and the id is only a lookup key. A client application that owns several
groups must mint NodeIds for them; there is no namespace to mint them in, so any choice (ns=0 with a
string or GUID, or an unregistered index) is arbitrary. Ignition's GDS branch uses
`NodeId(0, "urn:inductiveautomation:...:certificate-group:<name>")` today.

**Where the NodeId is used.** Genuine node references, all server-side and unchanged by this plan:
`EndpointCertificateConfig.getCertificateGroupId()`, `OpcUaServer.effectiveCertificateGroupId`
(`OpcUaServer.java:1448`), Push arguments an application resolves through
`CertificateManager.getCertificateGroup(NodeId)`, and every `certificateGroupId` argument of
`GdsClient` (those are the GDS's nodes). Pure key uses: `OpcUaClientConfig.getCertificateGroupId()`
and its default `getCertificateIdentity` (`OpcUaClientConfig.java:107-146`),
`CertificateIdentitySelectionContext.certificateGroupId`, the group filter in
`DefaultCertificateIdentitySelector`, `CertificateIdentity.certificateGroupId()`,
`CertificateGroup.Entry.certificateGroupId`, and `CertificateIdentityOrdering.STABLE`, which sorts
identities by the group NodeId's parseable string.

**The client never needed a manager.** `ClientApplicationContext`
(`opc-ua-stack/transport/.../client/ClientApplicationContext.java`) exposes `getKeyPair`,
`getCertificate`, `getCertificateChain`, and `getCertificateIdentity(profile)`; the transport
(`UascClientMessageHandler.java:855-882`) and session code (`SessionFsmFactory.java:1312-1336`,
`1737-1758`, `2278-2280`) take the selected identity and fall back to the fixed key pair and
certificate. `OpcUaClient` (`OpcUaClient.java:841-903`, `905-937`) selects through
`config.getCertificateIdentity`, caches per profile, and derives the ApplicationUri from the
identity, the fixed certificate, or the URI shared by every manager identity (with a WARN when they
differ). No transport code references the manager on the client.

**The factory is on the group** (#1861). `DefaultApplicationGroup` takes a `CertificateFactory` in
every constructor and uses it only in `createMissingCertificates()`; `createAndInitialize` statics
are deprecated. Milo's server has no Push, so nothing in Milo reads `getCertificateFactory()`
outside the group. Ignition's Push reads it for `CreateSigningRequest` only (see the #1861 comment).

**The quarantine is on the manager.** `CertificateManager.getCertificateQuarantine()` is implemented
by `DefaultCertificateManager` and the two test `TestCertificateManager` classes, and is read by no
Milo main code. Validators (`DefaultClientCertificateValidator`, `DefaultServerCertificateValidator`)
receive a quarantine directly. Ignition reads the manager quarantine for `GetRejectedList` and as a
fallback when a GDS session has no group; its GDS groups already keep a quarantine each.

**Server selection.** `OpcUaServer.resolveCertificateIdentity` (`OpcUaServer.java:1335-1367`) builds
a `CertificateIdentitySelectionContext.forEndpointAdvertisement(manager, profile, groupId, typeId,
explicitCertificate)` and, when the endpoint carries a fixed certificate (`EndpointConfig.getCertificate()`,
still used by Ignition), checks the selected identity's thumbprint matches. Unresolvable endpoints
are omitted with a WARN (`logOmittedEndpoint`), not fatal.

**Other consumers to keep compiling.** `SessionManager` (thumbprint lookups, `getCertificateGroup(thumbprint)`
for the validator), `AbstractIdentityValidator` (`getKeyPair(thumbprint)`), `ExampleServer`,
`ClientExampleRunner` (fixed client certificate plus trusting it into every server group),
`GdsPullExample`, `opc-ua-sdk/integration-tests/.../test/TestServer.java`, the `TestCertificateManager`
and `SecurityFixture` classes in `opc-ua-stack/stack-tests` and `opc-ua-stack/transport`, and the
tests listed in the [File Inventory](#file-inventory). `OpcUaClientConfigTest` (713 lines) and
`EccSessionIntegrationTest` (1505 lines) exercise the client manager, group id, and fixed-certificate
pin paths and are the largest test rewrites.

**Build conventions.** Java 17 via `mise`, Maven reactor; `AGENTS.md` requires the
`maven-command-runner` agent for every Maven command, `spotless:apply` before compiling, and the
docs under `.claude/docs/` (`running-tests.md`, `test-documentation-and-quality-guidelines.md`,
`documentation-guidelines.md`, `java-coding-conventions.md`) before writing tests or Javadoc.

* * *

## Desired End State

- A `CertificateGroup` is types, `hasCertificate`/entries/key pairs/chains, `updateCertificate`, a
  trust list manager, a validator, and (WP3) a quarantine. It has no id and no factory. `Entry` is
  `(certificateTypeId, certificateChain)`; `CertificateIdentity` is
  `(certificateGroup, certificateTypeId, keyPair, certificateChain)`.
- A server builds `new DefaultCertificateManager(defaultGroup)` (registers it under
  `NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup`) and
  `addCertificateGroup(nodeId, group)` for further groups; `getCertificateGroup(NodeId)`,
  `getCertificateGroupId(group)`, and `getCertificateGroups()` in registration order are the
  registry. `EndpointCertificateConfig`, Push, and `GdsClient` still speak NodeIds. An endpoint that
  names an unregistered group is omitted with reason `certificate group not registered`.
- A client builds a config with `setCertificateGroup(group)` and optionally `setCertificateTypeId`,
  `setCertificateIdentitySelector`, `setCertificateValidator`. `setCertificateManager`,
  `setCertificateGroupId`, `setKeyPair`, `setCertificate`, and `setCertificateChain` do not exist.
  A single key pair and chain become a group through
  `DefaultCertificateGroup.forIdentity(keyPair, chain, trustListManager, validator)`, which infers
  the certificate type from the leaf certificate. `ClientApplicationContext` exposes
  `getCertificateIdentity(profile)` and `getCertificateValidator()` only. The advertised
  ApplicationUri is the selected identity's SAN URI, or on `SecurityPolicy.None` the URI shared by the
  group's identities, else `APPLICATION_URI_NOT_CONFIGURED`; no manager-wide scan and no WARN about
  differing URIs.
- `CertificateFactory.createMissingCertificates(CertificateGroup)` is the only provisioning path;
  `DefaultCertificateGroup` has no `createMissingCertificates()` or `initialize()`, and no
  `createAndInitialize` statics exist.
- `DefaultCertificateIdentitySelector` selects from `context.candidateGroups()` in order, prefers the
  requested type, then the profile's preferred type, then candidate order; the explicit-certificate pin
  applies only when the server passes one.
- `mise exec -- mvn -q clean verify` passes; `grep -rn 'getCertificateGroupId()' --include=*.java
  opc-ua-stack opc-ua-sdk milo-examples` finds only `EndpointCertificateConfig` and generated model
  classes; `grep -rn 'setKeyPair\|setCertificateManager\|setCertificateGroupId' --include=*.java
  opc-ua-sdk/sdk-client milo-examples/client-examples opc-ua-sdk/integration-tests` finds nothing.
- `docs/features/gds-client.md`, the `stack.core.security`, `sdk.server`, and `transport.server`
  package docs, and the examples describe the model above.

* * *

## Assumptions and Gaps

**Assumptions:**

- Breaking API changes are acceptable on `integration/1.2` without deprecation shims (stated by
  Kevin Herron on 2026-09-03). The plan therefore removes rather than deprecates.
- Milo main code never reads `CertificateManager.getCertificateQuarantine()`; confirmed by grep over
  `opc-ua-stack/*/src/main` and `opc-ua-sdk/*/src/main` on 2026-09-03 (only the interface and its
  implementations match).
- The client transport and session code obtain the local identity only through
  `ClientApplicationContext.getCertificateIdentity` plus the three fixed getters; confirmed at the
  line references in Current State. Removing the fixed getters therefore has exactly those call sites.
- `SecurityPolicyProfile.certificateTypeIds()` lists the compatible types in preference order and
  `CertificateCompatibility` already knows the public-key family per type, so type inference for the
  group-of-one helper can be built from existing knowledge (`CertificateCompatibility.java:203-230`).

**Gaps:**

- Whether `DefaultCertificateManager` keeps registration order with a synchronized `LinkedHashMap` or
  a copy-on-write list of `(NodeId, group)` pairs. Either is fine; the observable contract is that
  `getCertificateGroups()` returns registration order and `addCertificateGroup` with an existing
  NodeId replaces in place without changing position.
- Whether `CertificateGroup.Entry` becomes a record or stays a final class with public fields. Prefer
  a record; callers change from field access to accessor either way.
- Exact wording of new Javadoc, following `.claude/docs/documentation-guidelines.md`.

* * *

## Prerequisites

- `mise install` done and `mise exec -- mvn -q clean compile` green on `integration/1.2` before
  starting, so failures are attributable to this plan.
- Read `.claude/docs/running-tests.md`, `.claude/docs/test-documentation-and-quality-guidelines.md`,
  and `.claude/docs/documentation-guidelines.md` (repository rule).
- All Maven commands run through the `maven-command-runner` agent (repository rule).

* * *

## Implementation Map

### State and data flow

1. **Server startup.** The application builds groups and a `DefaultCertificateManager`, registering
   each group under its `CertificateGroupType` NodeId. `OpcUaServer.resolveCertificateIdentity`
   resolves the endpoint's group NodeId (or the DefaultApplicationGroup id) through
   `manager.getCertificateGroup(NodeId)`; missing → omit the endpoint with reason
   `certificate group not registered`. It builds
   `CertificateIdentitySelectionContext.forEndpointAdvertisement(List.of(group), profile, typeId,
   endpointCertificate)` and selects. Thumbprint-based lookups during OpenSecureChannel,
   CreateSession, and user-token decryption keep using the manager unchanged.
2. **Client connect.** The config holds one group. `OpcUaClientConfig.getCertificateIdentity(profile)`
   builds `forClientConnectionSetup(List.of(group), profile, typeId)` and selects.
   `UascClientMessageHandler` and `SessionFsmFactory` use the identity or fail with
   `Bad_ConfigurationError` on a secured endpoint. `OpcUaClient.resolveApplicationUri(identity)`
   uses the configured URI, else the identity's SAN URI, else (identity absent, i.e. `None`) the URI
   shared by `group.getCertificateIdentities()`, else the placeholder.
3. **Selection.** `DefaultCertificateIdentitySelector` flattens `candidateGroups` to identities in
   order, drops locally incompatible ones (`CertificateCompatibility.checkLocalCompatible`), pins to
   the explicit certificate when present, otherwise orders by requested type, profile-preferred type,
   then candidate order.
4. **Provisioning.** `certificateFactory.createMissingCertificates(group)` iterates
   `group.getSupportedCertificateTypeIds()`, skips types where `group.hasCertificate(typeId)` is
   true, creates a key pair and chain, and calls `group.updateCertificate`. A `hasCertificate` that
   throws propagates: an unreadable entry is never silently replaced.

### Attachment seams

- `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateGroup.java`
  — interface change; the default `getCertificateIdentities()` passes `this` into each identity.
- `.../security/CertificateManager.java` and `.../security/DefaultCertificateManager.java` — the
  NodeId registry and ordered group list.
- `.../security/CertificateIdentitySelectionContext.java`,
  `DefaultCertificateIdentitySelector.java`, `CertificateIdentityOrdering.java` — candidate-group
  selection.
- `.../security/CertificateFactory.java` — the `createMissingCertificates(CertificateGroup)` default.
- `opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/OpcUaServer.java:1335-1470`
  — group resolution before selection.
- `opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/OpcUaClientConfig.java`,
  `OpcUaClientConfigBuilder.java`, `OpcUaClient.java`, `session/SessionFsmFactory.java` and
  `opc-ua-stack/transport/.../client/ClientApplicationContext.java`,
  `.../client/uasc/UascClientMessageHandler.java` — the one-group client.

### Planning decisions

- **`DefaultApplicationGroup` is renamed to `DefaultCertificateGroup`, with no deprecated shim**
  (decided by Kevin Herron, 2026-09-03). With the id gone the class no longer knows it is anyone's
  default; the name now comes from the NodeId a server registers it under.
- **The quarantine moves from the manager to the group** (decided by Kevin Herron, 2026-09-03). The
  manager-level quarantine dated from reading Part 12 `GetRejectedList` as one server-wide list; a
  rejection is a per-group trust decision, so the group exposes its quarantine and a server aggregates
  over `getCertificateGroups()` for `GetRejectedList`.
- **WP1 keeps the client compiling with a planned, throwaway adaptation.** The reactor compiles every
  module, so WP1 must touch `OpcUaClientConfig.getCertificateIdentity` to build the new context. It
  does so by resolving `getCertificateGroupId()` through the manager into candidate groups (about
  five lines) that WP2 deletes along with the manager and group id. This is an explicit shim, not
  rework of a capability.
- **Server-side explicit certificate pin stays; client pin goes.** `EndpointConfig.getCertificate()`
  is a live server API (Ignition sets it on every endpoint). The client's `setCertificate` pin exists
  only because a client could not otherwise present a certificate the manager did not hold; with a
  group-of-one it can.
- **Unregistered endpoint group is omitted, not fatal.** Matches the existing `logOmittedEndpoint`
  behavior for endpoints with no compatible identity and keeps `OpcUaServerEndpointDescriptionTest`
  and the lifecycle tests' assumptions about startup.
- **Cross-group precedence is manager registration order.** `CertificateManager.getCertificateIdentities()`
  concatenates per group in `getCertificateGroups()` order and no longer re-sorts;
  `CertificateGroup.getCertificateIdentities()` still sorts within the group by certificate type id.
  This replaces sorting on a NodeId string that no longer exists and makes precedence an explicit
  configuration choice.
- **WP3 stays its own package.** Nothing in WP1 or WP2 reads the quarantine, so the move is
  reviewable on its own and can follow WP2 or run alongside it.

* * *

## Work Package 1: Core model: groups without ids or factories, manager as NodeId registry

Deliver the new `stack-core` security model, adapt the server to resolve groups through the manager
registry, and adopt #1861's factory-side provisioning, leaving the client compiling through the
planned adaptation in `OpcUaClientConfig`. Ordered first because WP2 and WP3 change the same
interfaces further and every fixture is rewritten once here.

**ID:** `WP1`
**Depends on:** Nothing
**Done when:** `CertificateGroup` has no `getCertificateGroupId()` or `getCertificateFactory()` and
has `hasCertificate(NodeId)`; `CertificateIdentity` carries a `CertificateGroup`;
`DefaultCertificateManager` registers groups by NodeId in order and exposes
`getCertificateGroupId(group)`; `DefaultCertificateGroup` has constructors
`(trustListManager, certificateStore, certificateValidator[, supportedCertificateTypeIds])` only;
`CertificateFactory.createMissingCertificates(group)` exists and the `createAndInitialize` statics do
not; `OpcUaServer` omits endpoints whose group is unregistered with the new reason; stack-core,
stack-tests, transport, sdk-server, and integration-tests suites pass.
**Checkpoint:** None

### 1.1 Group, entry, and identity

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateGroup.java`

Remove `getCertificateGroupId()` and `getCertificateFactory()`. Add
`boolean hasCertificate(NodeId certificateTypeId) throws Exception`, documented as a presence check
that distinguishes "absent" from "unreadable" (throws for the latter), for use by provisioning.
`Entry` becomes `(NodeId certificateTypeId, X509Certificate[] certificateChain)`. The default
`getCertificateIdentities()` constructs `new CertificateIdentity(this, entry.certificateTypeId(),
keyPair, entry.certificateChain())` and keeps its within-group sort by type id.

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateIdentity.java`

First component becomes `CertificateGroup certificateGroup`. Equality and hash use the group's
`equals`/`hashCode` (reference identity for all built-in groups); document that identities from the
same group instance compare equal on group.

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateIdentityOrdering.java`

Drop the group sort key; `STABLE` orders by certificate type id only and is documented as
within-group ordering. `CertificateManager.getCertificateIdentities()` stops applying it across
groups (see 1.2).

### 1.2 Manager as NodeId registry

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateManager.java`

Keep `getKeyPair/getCertificate/getCertificateChain/getCertificateGroup(ByteString)`,
`getCertificateGroup(NodeId)`, `getCertificateGroups()` (now documented as registration order and
selection precedence), the three `getDefault*Group()` defaults, and `getCertificateQuarantine()` (WP3
removes it). Add `Optional<NodeId> getCertificateGroupId(CertificateGroup certificateGroup)`. The
default `getCertificateIdentities()` concatenates `group.getCertificateIdentities()` in
`getCertificateGroups()` order without re-sorting.

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateManager.java`

Registry of `(NodeId, CertificateGroup)` in registration order. Constructors:
`DefaultCertificateManager(CertificateQuarantine)` (empty) and
`DefaultCertificateManager(CertificateQuarantine, CertificateGroup defaultApplicationGroup)` which
registers the group under `NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup`.
Methods: `Optional<CertificateGroup> addCertificateGroup(NodeId, CertificateGroup)` (replaces in
place and returns the previous group for that id; a group instance may be registered under one id
only, a second registration of the same instance throws `IllegalArgumentException`),
`Optional<CertificateGroup> removeCertificateGroup(NodeId)`,
`boolean removeCertificateGroup(CertificateGroup)`. Thumbprint lookups iterate registered groups in
order. WP3 removes the quarantine parameter.

### 1.3 Selection over candidate groups

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateIdentitySelectionContext.java`

Record becomes `(Purpose purpose, List<CertificateGroup> candidateGroups, SecurityPolicyProfile
securityPolicyProfile, @Nullable NodeId certificateTypeId, @Nullable X509Certificate
explicitCertificate)`. Factories: `forEndpointAdvertisement(candidateGroups, profile, typeId,
explicitCertificate)` and `forClientConnectionSetup(candidateGroups, profile, typeId)` (no explicit
certificate on the client). Document that `candidateGroups` order is precedence.

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateIdentitySelector.java`

Candidates are `candidateGroups` flattened in order; remove the group-id filter; keep local
compatibility filtering, the explicit-certificate pin (empty when the pinned certificate is not among
the candidates, so the server can report a misconfigured endpoint certificate), requested-type then
profile-preferred-type ordering, then candidate order (a stable sort, no `CertificateIdentityOrdering`
across groups).

### 1.4 Default group and factory-side provisioning (#1861)

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/DefaultApplicationGroup.java`

Rename to `DefaultCertificateGroup.java` (git move). Constructors:
`(TrustListManager, CertificateStore, CertificateValidator)` for RSA SHA-256 and
`(TrustListManager, CertificateStore, CertificateValidator, List<NodeId>)`. Remove the NodeId
constructors, the factory parameter, `createMissingCertificates()`, `initialize()`, and both
`createAndInitialize` statics. Implement `hasCertificate` as `certificateStore.contains(typeId)`.
`getCertificateEntries()` keeps its per-type WARN-and-skip behavior. WP3 adds a quarantine parameter.

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateFactory.java`

Add `default List<NodeId> createMissingCertificates(CertificateGroup group) throws Exception`: for
each supported type where `!group.hasCertificate(typeId)`, `createKeyPair`, `createCertificateChain`,
`group.updateCertificate`; return the types created in order; a thrown `hasCertificate` or store
failure propagates and earlier material stays (same contract the instance method had). Call sites:

```java
var group = new DefaultCertificateGroup(trustListManager, certificateStore, certificateValidator);
certificateFactory.createMissingCertificates(group);
var certificateManager = new DefaultCertificateManager(certificateQuarantine, group);
```

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/package-info.java`

Update "Certificate and trust material": a group is the unit of identity and trust and has no id;
servers register groups under `CertificateGroupType` NodeIds in the manager; clients use one group
(WP2 finalizes the wording); provisioning is `CertificateFactory.createMissingCertificates`.

### 1.5 Server resolves groups through the registry

**File:** `opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/OpcUaServer.java`

In `resolveCertificateIdentity`, resolve `effectiveCertificateGroupId(endpoint)` through
`certificateManager.getCertificateGroup(nodeId)`; when empty, throw
`EndpointResolutionException("certificate group not registered: <nodeId>")` so the existing
`logOmittedEndpoint` path reports it. Build the context with `List.of(group)`. Keep the thumbprint
check against the endpoint's fixed certificate. `EndpointCertificateConfig`, `SessionManager`,
`AbstractIdentityValidator`, and `OpcUaServerConfigBuilder` need no change.

**File:** `opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/package-info.java`

**File:** `opc-ua-stack/transport/src/main/java/org/eclipse/milo/opcua/stack/transport/server/package-info.java`

Where they describe certificate groups or the manager, say groups are registered under NodeIds and
endpoints reference registered groups.

### 1.6 Planned client adaptation (removed by WP2)

**File:** `opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/OpcUaClientConfig.java`

In the default `getCertificateIdentity`, build candidate groups from the manager:
`getCertificateGroupId()` present → `manager.getCertificateGroup(id).map(List::of).orElse(List.of())`,
else `manager.getCertificateGroups()`; call `forClientConnectionSetup(candidates, profile, typeId)`.
The explicit-certificate pin is dropped from the client context here; `OpcUaClientConfigTest` cases
that assert pinning behavior are rewritten in WP2 and may be disabled with a `// WP2` note in WP1
only if they cannot pass without the pin. Everything else in the client is untouched until WP2.

**File:** `opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/OpcUaClient.java`

`getManagerApplicationUris` and `resolveApplicationUri` compile unchanged (they use
`CertificateIdentity::certificate` and the manager); WP2 rewrites them.

### 1.7 Fixtures, examples, and tests

**File:** `opc-ua-stack/stack-tests/src/test/java/org/eclipse/milo/opcua/stack/TestCertificateManager.java`

**File:** `opc-ua-stack/transport/src/test/java/org/eclipse/milo/opcua/stack/transport/client/tcp/TestCertificateManager.java`

Build the group with the new constructor and a factory whose `createMissingCertificates(group)`
installs the fixed key pair and certificate; implement `getCertificateGroupId(group)`; keep the
quarantine until WP3.

**File:** `milo-examples/server-examples/src/main/java/org/eclipse/milo/examples/server/ExampleServer.java`

**File:** `opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/test/TestServer.java`

New group constructor plus `certificateFactory.createMissingCertificates(defaultGroup)`.

**File:** `milo-examples/client-examples/src/main/java/org/eclipse/milo/examples/client/ClientExampleRunner.java`

Compiles unchanged in WP1 (uses `getCertificateGroups()` and entries); the entry field access
changes to accessors if `Entry` becomes a record. WP2 rewrites the client configuration.

### Design Decisions

- **`hasCertificate` on the interface rather than "empty chain means missing".** Per #1861 question
  1 and Ignition's survey: a store whose `get()` returns null for a half-readable entry would
  otherwise have its server identity silently regenerated by provisioning.
- **Same group instance under two NodeIds is an error.** `getCertificateGroupId(group)` must be a
  function; allowing aliases would make Push and endpoint resolution ambiguous.
- **Server explicit certificate keeps pinning inside the selector** rather than a post-selection
  filter, so a group holding two profile-compatible identities still resolves to the configured one.

### Failure, Safety, and Security

- Provisioning must never overwrite an entry it cannot read; `hasCertificate` throwing aborts the
  helper for that type and propagates.
- An endpoint naming an unregistered group is omitted and logged, never silently served from another
  group.
- Thumbprint lookups by the transport, session manager, and identity validator are unchanged in
  semantics: they iterate registered groups.

### Tests

**File:** `opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateManagerTest.java`

Tests:

- Registering a group under a NodeId makes it resolvable both ways (`getCertificateGroup(id)`,
  `getCertificateGroupId(group)`) and `getCertificateGroups()` preserves registration order across
  add, replace-in-place, and remove.
- Registering the same instance under a second NodeId throws `IllegalArgumentException`.
- `getCertificateIdentities()` concatenates per group in registration order (two groups whose type
  ids would sort the other way).
- Thumbprint lookups find the key pair, certificate, chain, and group of an identity in the second
  registered group.

**File:** `opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/security/DefaultApplicationGroupTest.java`

Renamed with the class. Tests:

- `hasCertificate` is false for an absent type, true after `updateCertificate`, and propagates a store
  exception rather than returning false.
- `createMissingCertificates(group)` on `CertificateFactory` creates only absent types, returns them in
  supported-type order, leaves present entries untouched, and stops at the first failing type keeping
  earlier material.
- Existing entry/key-pair/chain and rotation-race tests re-pointed at the new `Entry` shape.

**File:** `opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateIdentitySelectorTest.java`

Tests:

- Candidate order decides between two groups holding compatible identities of the same type.
- Requested type wins over candidate order; profile-preferred type wins over candidate order when no
  type is requested.
- Explicit certificate pins to the matching candidate and returns empty when it is in no candidate
  group (server misconfiguration path); existing legacy-KeyUsage tests re-pointed.

**File:** `opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/security/CertificateIdentityTest.java`

Re-point equality and construction tests at the group-reference component.

**File:** `opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/OpcUaServerEndpointDescriptionTest.java`

Tests:

- An endpoint whose `EndpointCertificateConfig` names an unregistered NodeId is omitted and the
  omission log reason is `certificate group not registered`.
- An endpoint naming a second registered group advertises that group's certificate.

Adapt constructor calls in `EndpointConfigTest`, `OpcUaServerConfigTest`,
`OpcUaServerLifecycleParticipantTest`, `OpcUaServerReverseConnectTargetTest`,
`OpcUaServerServiceSetsTest`, `SessionEndpointBindingTest`,
`SessionSecurityDiagnosticsAccessModeTest`, `AbstractUsernameIdentityValidatorTest`,
`DefaultDiscoveryServiceSetTest`, the `stack-tests` and `transport` `SecurityFixture`,
`ClientServerTest`, `StackIntegrationTest`, `OpcTcpTransportTest`, `OpcTcpServerTransportTest`,
`OpcTcpServerChannelInitializerTest`, `OpcTcpServerReverseConnectorTest`,
`UascServerChunkLifecycleTest`, `UascServerSymmetricHandlerTest`, and `EccSessionIntegrationTest`'s
server setup. No behavior change intended in those files beyond construction.

### Verification

#### Automated

- [x] Standard gate for `opc-ua-stack/stack-core`, `opc-ua-stack/stack-tests`,
  `opc-ua-stack/transport`, `opc-ua-sdk/sdk-server` — commands in
  [Verification Summary](#verification-summary) (2026-09-03, 1223 tests in those modules)
- [x] `mise exec -- mvn -q -pl opc-ua-sdk/integration-tests -am test` passes (2026-09-03; WP2 was
  delivered in the same tree, so no `OpcUaClientConfigTest` cases were deferred).

#### Agent review

- [x] `grep -rn 'getCertificateGroupId()' --include=*.java opc-ua-stack opc-ua-sdk milo-examples`
  matches only `EndpointCertificateConfig` and generated `model/objects` or GDS model classes.
- [x] `grep -rn 'getCertificateFactory()' --include=*.java opc-ua-stack opc-ua-sdk milo-examples`
  matches nothing. `createAndInitialize` still matches its unrelated callers
  (`FileBasedTrustListManager`, `KeyStoreCertificateStore`, `DefaultDataTypeManager`,
  `DefaultEncodingManager` and the classes that call them); no `DefaultCertificateGroup`
  static remains.

### Implementation Notes

- Delivered together with WP2 and WP3 in one working tree, so the planned throwaway client
  adaptation in 1.6 was never written: `OpcUaClientConfig` went straight to the one-group
  contract. Fixtures were rewritten once with the WP3 constructor shapes.
- `DefaultCertificateManager` keeps registration order with a copy-on-write `List<Registration>`
  guarded by a lock for writes; reads are lock-free and each lookup iterates the snapshot it
  observed. Registering the same instance under the same id is a no-op replace; under a different
  id it throws.
- `CertificateGroup.Entry` is a record. `DefaultCertificateGroup.hasCertificate` returns `false`
  for an unsupported type and otherwise delegates to `CertificateStore.contains`.
- `CertificateFactory.createMissingCertificates(group)` is not synchronized; the old instance
  method synchronized on the group. Concurrent provisioning of the same group is the application's
  concern.
- The server-side omission test asserts the WARN through captured stderr because `sdk-server`
  tests log through `slf4j-simple`, not Logback.

* * *

## Work Package 2: Client configures one group

Replace the client's manager, group id, and fixed key pair/certificate configuration with a single
`CertificateGroup`, slim the client transport contract to identity selection, derive the
ApplicationUri from that group, and provide the group-of-one helper. Ordered after WP1 because it
deletes WP1's adaptation and relies on the candidate-group selection context.

**ID:** `WP2`
**Depends on:** `WP1` (context shape, `DefaultCertificateGroup`, `CertificateIdentity` carrying the group)
**Done when:** `OpcUaClientConfigBuilder` has `setCertificateGroup` and no `setCertificateManager`,
`setCertificateGroupId`, `setKeyPair`, `setCertificate`, or `setCertificateChain`;
`ClientApplicationContext` has no key pair or certificate getters; a client with a group-of-one built
by `DefaultCertificateGroup.forIdentity` connects on Basic256Sha256 and ECC nistP256 against the
integration `TestServer`; a client on a `None` endpoint advertises the group's shared URI; every
example and integration test compiles and passes.
**Checkpoint:** None

### 2.1 Client config and builder

**File:** `opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/OpcUaClientConfig.java`

Remove `getKeyPair`, `getCertificate`, `getCertificateChain`, `getCertificateManager`,
`getCertificateGroupId`. Add `Optional<CertificateGroup> getCertificateGroup()`. Default
`getCertificateIdentity(profile)` returns empty without a group, else selects with
`forClientConnectionSetup(List.of(group), profile, getCertificateTypeId().orElse(null))`. Update
`copy(...)` accordingly. Document that a secured endpoint requires a group holding a compatible
identity and that `setCertificateValidator` overrides the group's validator.

**File:** `opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/OpcUaClientConfigBuilder.java`

Replace the five setters with `setCertificateGroup(CertificateGroup)`; keep
`setCertificateTypeId`, `setCertificateIdentitySelector`, `setCertificateValidator`. When
`setCertificateValidator` was never called and a group is set, `build()` defaults the validator to
`group.getCertificateValidator()` (today's default is the insecure validator; keep that default only
when no group is configured).

### 2.2 Transport and session use the identity only

**File:** `opc-ua-stack/transport/src/main/java/org/eclipse/milo/opcua/stack/transport/client/ClientApplicationContext.java`

Remove `getKeyPair`, `getCertificate`, `getCertificateChain`. `getCertificateIdentity(profile)`
becomes abstract (no default empty).

**File:** `opc-ua-stack/transport/src/main/java/org/eclipse/milo/opcua/stack/transport/client/uasc/UascClientMessageHandler.java`

`newSecureChannel`: on a secured policy, `getCertificateIdentity(profile)` or
`Bad_ConfigurationError("no certificate identity for <policy>")`; drop the three `.or(application::...)`
fallbacks.

**File:** `opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/session/SessionFsmFactory.java`

At the three fallback sites (CreateSession client certificate, ActivateSession signature key pair and
chain, the certificate-bytes helper) use the identity only; a missing identity on a secured policy
fails with `Bad_ConfigurationError`, on `None` yields `ByteString.NULL_VALUE` as today.

**File:** `opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/OpcUaClient.java`

The anonymous `ClientApplicationContext` drops the three getters. `resolveApplicationUri(identity)`:
configured URI, else identity SAN URI, else (identity null) the single distinct SAN URI across
`config.getCertificateGroup().getCertificateIdentities()`, else `APPLICATION_URI_NOT_CONFIGURED`.
Delete `getManagerApplicationUris` and its WARN; keep the per-profile identity cache and its clearing
on connect and disconnect.

### 2.3 Group-of-one helper and type inference

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateGroup.java`

Add `static DefaultCertificateGroup forIdentity(KeyPair keyPair, X509Certificate[] certificateChain,
TrustListManager trustListManager, CertificateValidator certificateValidator)`: infers the
certificate type from the leaf certificate, builds a `MemoryCertificateStore` holding that one entry,
and returns a group supporting only that type. Throws `IllegalArgumentException` when the type cannot
be inferred or the key pair's public key does not match the leaf certificate.

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateCompatibility.java`

Add `public static Optional<NodeId> inferCertificateTypeId(X509Certificate certificate)`: RSA public
key → `RsaSha256ApplicationCertificateType` (or `RsaMinApplicationCertificateType` when the signature
algorithm is SHA-1 based), EC keys by curve → the matching `Ecc*ApplicationCertificateType`,
Ed25519/Ed448 → the Curve25519/Curve448 types, else empty. Reuse the curve constants already in the
class.

### 2.4 Examples, docs, and the GDS example

**File:** `milo-examples/client-examples/src/main/java/org/eclipse/milo/examples/client/ClientExampleRunner.java`

Configure the client with `DefaultCertificateGroup.forIdentity(loader.getClientKeyPair(),
loader.getClientCertificateChain(), trustListManager, certificateValidator)`; trust the client
certificate into the example server's groups via `client.getConfig().getCertificateGroup()` identities.

**File:** `milo-examples/client-examples/src/main/java/org/eclipse/milo/examples/client/GdsPullExample.java`

Where it configures the client identity or comments on the manager, use the group. Its pull loop
already writes through `CertificateGroup.updateCertificate`.

**File:** `docs/features/gds-client.md`

Where it mentions installing through a `CertificateManager` or `setCertificateGroupId`, describe the
group as the install target and the client's single-group configuration; GDS `certificateGroupId`
arguments remain the GDS's NodeIds.

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/package-info.java`

Finalize the client wording started in 1.4.

### Design Decisions

- **Validator defaults to the group's when a group is set.** A group already pairs trust material with
  a validator; defaulting the client to the insecure validator when a group was configured would be a
  silent downgrade. Explicit `setCertificateValidator` still wins (Ignition's "validation disabled").
- **URI derivation on `None` uses the connection's group, not a manager.** Removes the cross-group
  divergence WARN and the placeholder case for applications whose groups carry different URIs.
- **No deprecated setters.** Allowed on 1.2; shims would keep two identity paths alive in the
  transport.

### Failure, Safety, and Security

- A secured endpoint with no compatible identity in the group fails with `Bad_ConfigurationError`
  naming the policy, at OpenSecureChannel, before any session is created.
- `forIdentity` rejects a mismatched key pair and certificate so a misconfigured client cannot sign
  with a key that does not match the certificate it presents.
- `None` endpoints never trigger identity selection (existing early return in `newSecureChannel`).

### Tests

**File:** `opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/OpcUaClientConfigTest.java`

Tests (rewritten from the manager and pin cases):

- A client with a group holding RSA and ECC identities selects the profile-compatible type per
  endpoint (existing selection cases re-pointed).
- `setCertificateTypeId` selects that type within the group; an absent type yields no identity and
  the connect fails with `Bad_ConfigurationError`.
- A group-of-one from `forIdentity` connects and presents that certificate; a `forIdentity` call with
  a mismatched key pair throws.
- `None` endpoint advertises the group's shared URI; a group with identities carrying different URIs
  yields `APPLICATION_URI_NOT_CONFIGURED`; a configured URI always wins (re-pointed from the manager
  URI tests).
- The per-profile identity cache still returns the same identity for SecureChannel and Session
  setup and clears on reconnect (existing rotation test re-pointed).

**File:** `opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/session/EccSessionIntegrationTest.java`

Client setup moves to `setCertificateGroup`; assertions unchanged.

**File:** `opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/security/CertificateCompatibilityTest.java`

Tests:

- `inferCertificateTypeId` maps RSA SHA-256, RSA SHA-1, each supported EC curve, Ed25519, and Ed448
  certificates to their type ids and returns empty for an unsupported key.

Adapt client construction in `ClientServerTest`, `StackIntegrationTest`, `OpcTcpTransportTest`, and
the two `SecurityFixture` classes to a group-of-one.

### Verification

#### Automated

- [x] Standard gate for `opc-ua-stack/stack-core`, `opc-ua-stack/transport`,
  `opc-ua-sdk/sdk-client`, `opc-ua-sdk/integration-tests` — commands in
  [Verification Summary](#verification-summary) (2026-09-03)
- [x] `mise exec -- mvn -q -pl milo-examples/client-examples,milo-examples/server-examples -am compile`
  (2026-09-03).

#### Agent review

- [x] The grep for removed client setters returns only two test comments that name the removed
  methods, plus `ClientExampleRunner` and `TestServer` reading the server's
  `OpcUaServerConfig.getCertificateManager()`, which is expected.
- [x] `ClientApplicationContext` declares exactly: `getEndpoint`, `getCertificateIdentity`,
  `getCertificateValidator`, `getEncodingContext`, `getRequestTimeout`, `getSecurityKeysListener`.

### Implementation Notes

- `OpcUaClientConfig.copy` now copies the effective validator; before, a copied config fell back to
  the insecure default. With the validator defaulting to the group's, copying without it would have
  been a silent downgrade.
- `DefaultCertificateGroup.forIdentity` compares the key pair and leaf certificate by encoded
  public key rather than `Key.equals`, because JDK and BouncyCastle key classes do not consider
  each other equal.
- `SessionFsmFactory` fails CreateSession and the session signature with `Bad_ConfigurationError`
  when a secured endpoint has no identity. `buildIdentityProviderContext` stays tolerant: a `None`
  endpoint whose UserName token policy names an RSA policy still activates without a client
  identity, as it did before, because the identity provider encrypts with the server certificate.
- `setCertificateTypeId` remains a ranking preference in `DefaultCertificateIdentitySelector`, not
  a filter: an absent requested type falls back to another profile-compatible identity. The plan's
  "absent type yields no identity" test therefore uses a profile the group cannot satisfy.
- Added after review (Kevin Herron, 2026-09-03): `OpcUaClientConfigBuilder.setCertificateIdentity(KeyPair, X509Certificate...)`
  is the simple path for a client with only a key pair and certificate. The builder wraps them in a
  `DefaultCertificateGroup.forIdentity` group with an empty in-memory trust list and quarantine and
  the effective validator; the client never reads that trust material. `setCertificateGroup` and
  `setCertificateIdentity` replace each other, last one wins. `forIdentity` itself keeps requiring
  real trust material. `ClientExampleRunner` uses the shortcut; `GdsPullExample` configures a full
  group in its own security directory and installs the pulled trust list into it.
- `OpcUaClientCertificateGroupTest` (new) connects both a `forIdentity` group of one and a
  `setCertificateIdentity` client to the integration `TestServer` on Basic256Sha256/SignAndEncrypt. `TestServer` advertises no ECC endpoint, so the
  ECC nistP256 half of the "done when" clause is covered by `EccSessionIntegrationTest`, whose
  clients now use `setCertificateGroup`.

* * *

## Work Package 3: Quarantine belongs to the group

Move rejected-certificate storage from the manager to the group so a group's trust decisions
(trust list, validator, rejections) live in one place, and applications aggregate for
`GetRejectedList`.

**ID:** `WP3`
**Depends on:** `WP1` (interface shape); independent of `WP2`
**Done when:** `CertificateGroup.getCertificateQuarantine()` exists; `CertificateManager` and
`DefaultCertificateManager` have no quarantine; `DefaultCertificateGroup` constructors take a
`CertificateQuarantine`; examples and fixtures pass a quarantine per group; all suites pass.
**Checkpoint:** None

### 3.1 Interface and implementations

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateGroup.java`

Add `CertificateQuarantine getCertificateQuarantine()`, documented as the store for certificates
this group's validator rejected.

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateManager.java`

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateManager.java`

Remove `getCertificateQuarantine()` and the constructor parameter; constructors become
`DefaultCertificateManager()` and `DefaultCertificateManager(CertificateGroup defaultApplicationGroup)`.

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateGroup.java`

Constructors gain a `CertificateQuarantine` parameter placed before the validator:
`(TrustListManager, CertificateStore, CertificateQuarantine, CertificateValidator[, List<NodeId>])`;
`forIdentity` gains the same parameter.

### 3.2 Callers

**File:** `milo-examples/server-examples/src/main/java/org/eclipse/milo/examples/server/ExampleServer.java`

**File:** `opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/test/TestServer.java`

**File:** `opc-ua-stack/stack-tests/src/test/java/org/eclipse/milo/opcua/stack/TestCertificateManager.java`

**File:** `opc-ua-stack/transport/src/test/java/org/eclipse/milo/opcua/stack/transport/client/tcp/TestCertificateManager.java`

**File:** `milo-examples/client-examples/src/main/java/org/eclipse/milo/examples/client/ClientExampleRunner.java`

Pass the quarantine the validator already uses into the group; remove it from manager construction;
drop the manager `getCertificateQuarantine` overrides. `SessionManager`, `OpcUaServer`, and the
transport do not reference the quarantine and need no change.

**File:** `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/package-info.java`

State that rejections are per group and that a server-wide rejected list is the union over
`getCertificateGroups()`.

### Failure, Safety, and Security

- A certificate rejected by one group's validator lands only in that group's quarantine; trusting it
  from another group's UI must not be possible by accident. Document in the package info.

### Tests

**File:** `opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateGroupTest.java`

Tests:

- Two groups with distinct quarantines: a chain rejected through group A's validator appears in A's
  quarantine and not in B's.

### Verification

#### Automated

- [x] Standard gate for `opc-ua-stack/stack-core`, `opc-ua-stack/stack-tests`,
  `opc-ua-stack/transport`, `opc-ua-sdk/sdk-server`, `opc-ua-sdk/integration-tests` — commands in
  [Verification Summary](#verification-summary) (2026-09-03)

#### Agent review

- [x] `grep -rn 'getCertificateQuarantine' --include=*.java opc-ua-stack opc-ua-sdk milo-examples`
  matches only `CertificateGroup`, `DefaultCertificateGroup`, and tests.

### Implementation Notes

- Delivered in the same pass as WP1 so every fixture took the quarantine constructor parameter
  once. `DefaultCertificateGroup` requires a non-null quarantine; the two `TestCertificateManager`
  fixtures and the examples pass the quarantine their validator already used.

* * *

## Rejected Splits

- **Core model first, server adaptation second.** The reactor compiles every module, so a core-only
  work package cannot leave the repository compilable; `OpcUaServer` and the fixtures must move with
  the interface.
- **Client change inside WP1.** Would put the two largest test rewrites (`OpcUaClientConfigTest`,
  `EccSessionIntegrationTest`) and the transport contract change into the same package as the model
  change, hiding which one broke a test; the five-line adaptation in 1.6 is cheaper than that.
- **Quarantine move inside WP1.** Nothing in WP1 or WP2 depends on it, and folding it in would
  add a constructor parameter to every fixture rewrite in the same diff as the model change.
- **A `CertificateGroupId` value type instead of removing the id.** Considered in the 2026-09-03
  conversation and rejected: it gives client ids a nicer type but keeps clients naming groups they
  have no reason to name, and it retypes every server surface that is correct today.

* * *

## File Inventory

| Path or destination | Work package | Change | Purpose |
| --- | --- | --- | --- |
| `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateGroup.java` | `WP1`, `WP3` | Modify | Drop id and factory; add `hasCertificate`; `Entry` without group id; quarantine (WP3) |
| `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateIdentity.java` | `WP1` | Modify | Carry a `CertificateGroup` reference |
| `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateIdentityOrdering.java` | `WP1` | Modify | Within-group ordering only |
| `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateManager.java` | `WP1`, `WP3` | Modify | NodeId registry contract; `getCertificateGroupId`; drop quarantine (WP3) |
| `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateManager.java` | `WP1`, `WP3` | Modify | Ordered `(NodeId, group)` registry; constructors |
| `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateIdentitySelectionContext.java` | `WP1` | Modify | Candidate groups instead of manager + group id |
| `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateIdentitySelector.java` | `WP1` | Modify | Select over candidate groups in order |
| `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/DefaultApplicationGroup.java` | `WP1` | Rename to `DefaultCertificateGroup.java` | Constructors without id or factory; `hasCertificate` |
| `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateGroup.java` | `WP1`, `WP2`, `WP3` | Modify (after rename) | `forIdentity` helper (WP2); quarantine parameter (WP3) |
| `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateFactory.java` | `WP1` | Modify | `createMissingCertificates(CertificateGroup)` default |
| `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/CertificateCompatibility.java` | `WP2` | Modify | `inferCertificateTypeId` |
| `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/package-info.java` | `WP1`, `WP2`, `WP3` | Modify | Model description |
| `opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/OpcUaServer.java` | `WP1` | Modify | Resolve endpoint group through the registry; omission reason |
| `opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/package-info.java` | `WP1` | Modify | Registry wording |
| `opc-ua-stack/transport/src/main/java/org/eclipse/milo/opcua/stack/transport/server/package-info.java` | `WP1` | Modify | Registry wording |
| `opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/OpcUaClientConfig.java` | `WP1`, `WP2` | Modify | Planned adaptation (WP1); one-group contract (WP2) |
| `opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/OpcUaClientConfigBuilder.java` | `WP2` | Modify | `setCertificateGroup`; remove five setters |
| `opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/OpcUaClient.java` | `WP2` | Modify | Context without fixed getters; URI from the group |
| `opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/session/SessionFsmFactory.java` | `WP2` | Modify | Identity-only signature and certificate paths |
| `opc-ua-stack/transport/src/main/java/org/eclipse/milo/opcua/stack/transport/client/ClientApplicationContext.java` | `WP2` | Modify | Drop fixed getters |
| `opc-ua-stack/transport/src/main/java/org/eclipse/milo/opcua/stack/transport/client/uasc/UascClientMessageHandler.java` | `WP2` | Modify | Identity-only secure channel setup |
| `milo-examples/server-examples/src/main/java/org/eclipse/milo/examples/server/ExampleServer.java` | `WP1`, `WP3` | Modify | New constructors; factory helper |
| `milo-examples/client-examples/src/main/java/org/eclipse/milo/examples/client/ClientExampleRunner.java` | `WP1`, `WP2`, `WP3` | Modify | Group-of-one client |
| `milo-examples/client-examples/src/main/java/org/eclipse/milo/examples/client/GdsPullExample.java` | `WP2` | Modify | Group-based wording and setup |
| `docs/features/gds-client.md` | `WP2` | Modify | Client single-group model |
| `opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/test/TestServer.java` | `WP1`, `WP3` | Modify | New constructors; factory helper |
| `opc-ua-stack/stack-tests/src/test/java/org/eclipse/milo/opcua/stack/TestCertificateManager.java` | `WP1`, `WP3` | Modify | Registry methods; quarantine |
| `opc-ua-stack/transport/src/test/java/org/eclipse/milo/opcua/stack/transport/client/tcp/TestCertificateManager.java` | `WP1`, `WP3` | Modify | Registry methods; quarantine |
| `opc-ua-stack/stack-tests/src/test/java/org/eclipse/milo/opcua/stack/SecurityFixture.java` | `WP1`, `WP2` | Modify | Construction; group-of-one client |
| `opc-ua-stack/transport/src/test/java/org/eclipse/milo/opcua/stack/transport/client/tcp/SecurityFixture.java` | `WP1`, `WP2` | Modify | Construction; group-of-one client |
| `opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateManagerTest.java` | `WP1` | Modify | Registry and ordering tests |
| `opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/security/DefaultApplicationGroupTest.java` | `WP1` | Rename to `DefaultCertificateGroupTest.java` | `hasCertificate`, provisioning helper |
| `opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateGroupTest.java` | `WP3` | Modify (after rename) | Per-group quarantine |
| `opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/security/DefaultCertificateIdentitySelectorTest.java` | `WP1` | Modify | Candidate-order selection |
| `opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/security/CertificateIdentityTest.java` | `WP1` | Modify | Group-reference component |
| `opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/security/CertificateCompatibilityTest.java` | `WP2` | Modify | Type inference |
| `opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/OpcUaServerEndpointDescriptionTest.java` | `WP1` | Modify | Unregistered-group omission; second-group endpoint |
| `opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/{EndpointConfigTest,OpcUaServerConfigTest,OpcUaServerLifecycleParticipantTest,OpcUaServerReverseConnectTargetTest,OpcUaServerServiceSetsTest,SessionEndpointBindingTest}.java` | `WP1` | Modify | Construction only |
| `opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/diagnostics/variables/SessionSecurityDiagnosticsAccessModeTest.java` | `WP1` | Modify | Construction only |
| `opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/identity/AbstractUsernameIdentityValidatorTest.java` | `WP1` | Modify | Construction only |
| `opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/servicesets/impl/DefaultDiscoveryServiceSetTest.java` | `WP1` | Modify | Construction only |
| `opc-ua-stack/stack-tests/src/test/java/org/eclipse/milo/opcua/stack/{ClientServerTest,StackIntegrationTest}.java` | `WP1`, `WP2` | Modify | Construction; group-of-one client |
| `opc-ua-stack/transport/src/test/java/org/eclipse/milo/opcua/stack/transport/client/tcp/OpcTcpTransportTest.java` | `WP1`, `WP2` | Modify | Construction; group-of-one client |
| `opc-ua-stack/transport/src/test/java/org/eclipse/milo/opcua/stack/transport/server/tcp/{OpcTcpServerTransportTest,OpcTcpServerChannelInitializerTest,OpcTcpServerReverseConnectorTest}.java` | `WP1` | Modify | Construction only |
| `opc-ua-stack/transport/src/test/java/org/eclipse/milo/opcua/stack/transport/server/uasc/{UascServerChunkLifecycleTest,UascServerSymmetricHandlerTest}.java` | `WP1` | Modify | Construction only |
| `opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/OpcUaClientConfigTest.java` | `WP1`, `WP2` | Modify | Deferred pin cases (WP1); one-group rewrite (WP2) |
| `opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/session/EccSessionIntegrationTest.java` | `WP1`, `WP2` | Modify | Server construction (WP1); client group (WP2) |

* * *

## Verification Summary

All Maven commands run through the `maven-command-runner` agent. Use `-pl` with `-am` when the
changed module differs from the tested module (see `.claude/docs/running-tests.md`).

### Preparation

```bash
mise exec -- mvn -q spotless:apply
```

### Standard affected-scope gate

```bash
mise exec -- mvn -q -pl opc-ua-stack/stack-core test
mise exec -- mvn -q -pl opc-ua-stack/stack-tests,opc-ua-stack/transport -am test
mise exec -- mvn -q -pl opc-ua-sdk/sdk-server -am test
mise exec -- mvn -q -pl opc-ua-sdk/integration-tests -am test
mise exec -- mvn -q -pl milo-examples/client-examples,milo-examples/server-examples -am compile
```

### Final plan gate

```bash
mise exec -- mvn -q clean verify
mise exec -- mvn -q install -DskipTests   # so downstream (Ignition) can resolve 1.2.0-SNAPSHOT from ~/.m2
```

| Work package | Scope | Required evidence | Result |
| --- | --- | --- | --- |
| `WP1` | `stack-core`, `stack-tests`, `transport`, `sdk-server`, `integration-tests` | Standard gate; agent-review greps for `getCertificateGroupId()`, `createAndInitialize`, `getCertificateFactory()` | Green, 2026-09-03 |
| `WP2` | `stack-core`, `transport`, `sdk-client`, `integration-tests`, examples | Standard gate; agent-review grep for removed client setters; `ClientApplicationContext` method list | Green, 2026-09-03 |
| `WP3` | `stack-core`, fixtures, examples | Standard gate; agent-review grep for `getCertificateQuarantine` | Green, 2026-09-03 |
| Final | full plan | `mvn -q clean verify` green; `install` done; Downstream Handoff filled in | `clean verify` green and `install -DskipTests` done 2026-09-03 13:43 (UTC-7), after the `setCertificateIdentity` addition; 1.2.0-SNAPSHOT jars in `~/.m2` |

* * *

## Downstream Handoff

Consumed by the Ignition branch `feature/opc-ua-gds-pull` (Milo bump to a snapshot at or after this
plan's merge). Expected adoption, to be confirmed against the final API:

- `GdsCertificateGroup` drops `localGroupId` and the URN-prefixed ns=0 NodeId; `CertificateGroupManager`
  keeps its name registry and no longer registers groups with a manager.
- `OpcUaClientIdentityMaterial` holds a `CertificateGroup` and a validator; the explicit keystore
  alias becomes `DefaultCertificateGroup.forIdentity(...)`; `requireCompatibleIdentity` checks the
  material's group directly.
- `IgnitionOpcUaServer` registers its group under `ServerConfiguration_CertificateGroups_DefaultApplicationGroup`
  via `DefaultCertificateManager(group)`; `ServerConfigurationObject` keeps resolving wire NodeIds
  through `getCertificateGroup(NodeId)` and uses `getCertificateGroupId(group)` where it builds the
  node; `GetRejectedList` aggregates `getCertificateGroups()` quarantines under WP3.
- `ClientCertificateFactory` is passed to `createMissingCertificates` nowhere on the client (GDS
  groups never self-sign) and remains the CSR/key-pair factory for the pull cycle.

### Public contracts

All in `opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/security/` unless
noted.

- `CertificateGroup`: `getSupportedCertificateTypeIds()`, `getTrustListManager()`,
  `getCertificateQuarantine()`, `getCertificateEntries()`, default `getCertificateIdentities()`,
  `hasCertificate(NodeId) throws Exception`, `getKeyPair(NodeId)`, `getCertificateChain(NodeId)`,
  `updateCertificate(NodeId, KeyPair, X509Certificate[]) throws Exception`,
  `getCertificateValidator()`; `record Entry(NodeId certificateTypeId, X509Certificate[] certificateChain)`.
- `CertificateIdentity(CertificateGroup certificateGroup, NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain)`.
- `CertificateManager`: thumbprint lookups unchanged; `getCertificateGroup(NodeId)`,
  `Optional<NodeId> getCertificateGroupId(CertificateGroup)`, `getCertificateGroups()` (registration
  order), default `getCertificateIdentities()` (concatenated in that order), the three
  `getDefault*Group()` defaults. No quarantine.
- `DefaultCertificateManager()`, `DefaultCertificateManager(CertificateGroup defaultApplicationGroup)`;
  `Optional<CertificateGroup> addCertificateGroup(NodeId, CertificateGroup)` (replace in place; same
  instance under a second id throws `IllegalArgumentException`),
  `Optional<CertificateGroup> removeCertificateGroup(NodeId)`,
  `boolean removeCertificateGroup(CertificateGroup)`.
- `DefaultCertificateGroup(TrustListManager, CertificateStore, CertificateQuarantine, CertificateValidator)`,
  `DefaultCertificateGroup(TrustListManager, CertificateStore, CertificateQuarantine, CertificateValidator, List<NodeId>)`,
  `static DefaultCertificateGroup forIdentity(KeyPair, X509Certificate[], TrustListManager, CertificateQuarantine, CertificateValidator)`.
- `CertificateFactory`: `default List<NodeId> createMissingCertificates(CertificateGroup) throws Exception`.
- `CertificateCompatibility.inferCertificateTypeId(X509Certificate)`.
- `CertificateIdentitySelectionContext(Purpose, List<CertificateGroup> candidateGroups, SecurityPolicyProfile, @Nullable NodeId certificateTypeId, @Nullable X509Certificate explicitCertificate)`;
  `forEndpointAdvertisement(candidateGroups, profile, typeId, explicitCertificate)`,
  `forClientConnectionSetup(candidateGroups, profile, typeId)`.
- `opc-ua-sdk/sdk-client/.../OpcUaClientConfigBuilder`: `setCertificateGroup(CertificateGroup)`,
  `setCertificateIdentity(KeyPair, X509Certificate...)` (mutually replacing),
  `setCertificateTypeId`, `setCertificateIdentitySelector`, `setCertificateValidator` (defaults to the
  group's validator when a group is set). `OpcUaClientConfig.getCertificateGroup()`.
- `opc-ua-stack/transport/.../client/ClientApplicationContext`: `getEndpoint()`,
  `getCertificateIdentity(SecurityPolicyProfile)` (abstract), `getCertificateValidator()`,
  `getEncodingContext()`, `getRequestTimeout()`, default `getSecurityKeysListener()`.

### Design deviations

- No WP1-only client shim (1.6); WP1, WP2, and WP3 landed together. See each package's
  Implementation Notes.
- `OpcUaClientConfig.copy` copies the validator (previously not copied).
- `forIdentity` compares public keys by encoding, not `Key.equals`.

### Verification evidence

- Module gates and examples compile: green on 2026-09-03 (see each package's checklist).
- Final `mise exec -- mvn -q clean verify` and `install -DskipTests`: recorded in
  [Verification Summary](#verification-summary).

### Downstream actions (Ignition `feature/opc-ua-gds-pull`)

- `GdsCertificateGroup` drops `localGroupId` and the URN-prefixed ns=0 NodeId; implement
  `hasCertificate(NodeId)` and `getCertificateQuarantine()`; construct `CertificateGroup.Entry` with
  two arguments; drop `getCertificateGroupId()` and `getCertificateFactory()`.
- `CertificateGroupManager` keeps its name registry and no longer registers groups with a manager.
- `OpcUaClientIdentityMaterial` holds a `CertificateGroup`; the explicit keystore alias becomes
  `setCertificateIdentity(keyPair, chain)` when Ignition supplies its own validator, or
  `DefaultCertificateGroup.forIdentity(keyPair, chain, trustListManager, quarantine, validator)`
  when the group's trust list is needed;
  client configs call `setCertificateGroup` and may omit `setCertificateValidator` unless validation
  is disabled; `requireCompatibleIdentity` checks the material's group directly.
- `IgnitionOpcUaServer` registers its group with `new DefaultCertificateManager(group)`; further
  groups through `addCertificateGroup(nodeId, group)`. `ServerConfigurationObject` keeps resolving
  wire NodeIds through `getCertificateGroup(NodeId)` and uses `getCertificateGroupId(group)` where it
  builds the node. `GetRejectedList` aggregates `getCertificateGroups()` quarantines; the manager
  quarantine and its "session has no group" fallback are gone.
- Ignition's Push `CreateSigningRequest` read `group.getCertificateFactory()`; it must keep its own
  factory reference (per group or shared) instead.
- `ClientCertificateFactory` remains the CSR/key-pair factory for the pull cycle;
  `createMissingCertificates` is never called on GDS-managed groups.

### Retained risks and unsupported behavior

- Custom `CertificateManager` implementations that relied on `getCertificateIdentities()` sorting
  across groups by NodeId string now get registration order; they must also implement
  `getCertificateGroupId(CertificateGroup)`.
- The client's fixed-certificate pin (`setCertificate` alongside a manager) is gone; an application
  that needs one exact certificate calls `setCertificateIdentity` or configures a group of one.
- `CertificateIdentity` equality includes the group reference and `KeyPair` identity, so two reads
  of the same store-backed group yield non-equal identities. Compare by group, type, and certificate
  where value equality is needed.
- A `None` endpoint whose UserName policy encrypts with an RSA policy still activates without a
  client identity; that path never required one.
