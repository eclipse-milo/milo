# GDS client

An OPC UA Global Discovery Server (GDS, OPC 10000-12) registers applications, issues their
certificates, and distributes the trust lists they validate against. Milo ships the GDS
information model as generated code inside the core SDK modules and a client layer in the
`milo-sdk-client-gds` module, so an application can register itself, obtain and renew certificates
through the Pull Model (Part 12 §7.6), and keep a GDS-managed trust list without reimplementing the
namespace, the `Directory` method calls, or the FileType read loop.

* * *

## Table of contents

- [Overview](#overview)
- [How it works](#how-it-works)
- [Usage](#usage)
- [Generated model](#generated-model)
- [Limitations](#limitations)

* * *

## Overview

Support is split into two layers.

The **generated model** ships in every Milo client and server jar, the same way the namespace 0
model does. It lives in subpackages of the existing modules:

| Module           | Package                                              | Contents                                                                                          |
|------------------|------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| `milo-stack-core` | `org.eclipse.milo.opcua.stack.core.gds`             | `GdsNodeIds` (one `ExpandedNodeId` per GDS node), `DataTypeInitializer`                          |
| `milo-stack-core` | `org.eclipse.milo.opcua.stack.core.gds.types`       | `ApplicationRecordDataType` with its codec and `StructureDefinition`                             |
| `milo-dtd-core`   | `org.eclipse.milo.opcua.sdk.core.dtd.gds`           | `BinaryDataTypeDictionaryInitializer` for the legacy type dictionary mechanism (deprecated)      |
| `milo-sdk-client` | `org.eclipse.milo.opcua.sdk.client.gds.model`             | `ObjectTypeInitializer`, `VariableTypeInitializer`                                                |
| `milo-sdk-client` | `org.eclipse.milo.opcua.sdk.client.gds.model.objects` | Client node classes for `DirectoryType`, `CertificateDirectoryType`, the KeyCredential and AuthorizationService types, and the GDS audit event types |
| `milo-sdk-server` | `org.eclipse.milo.opcua.sdk.server.gds` and `.model.objects` | The server-side mirror of the same types, for a GDS hosted on Milo                        |

The **client layer** is the module `opc-ua-sdk/sdk-client-gds` (artifact `milo-sdk-client-gds`,
listed in `milo-bom`). It depends only on `milo-sdk-client` and owns the package
`org.eclipse.milo.opcua.sdk.client.gds`. The generated initializers remain in the core SDK
under `.gds.model`, so the two jars can be used together as Java modules:

- `GdsClient` wraps a connected `OpcUaClient` and exposes every `DirectoryType` and
  `CertificateDirectoryType` method with a typed signature, plus reads of a CertificateGroup's
  `CertificateTypes` and a TrustList's `LastUpdateTime` and `UpdateFrequency`. It also resolves a
  desired certificate type to the value accepted by a group's certificate request methods.
- `TrustListReader` reads a `TrustListType` object with the FileType `Open`, `Read`, and `Close`
  methods and decodes the body into a `TrustListDataType`.
- `TrustListApplier` installs a `TrustListDataType` into a `TrustListManager` and builds one back
  from a manager.

The module stops at these typed primitives. Scheduling, persistence of the `ApplicationId` and
pending `RequestId`s, retry policy, and what to do with an issued certificate depend on the
application's configuration and lifecycle, so there is no Pull engine; the application drives the
sequence.

The publishable `milo-sdk-client-gds-testing` module provides `FakeGdsNamespace` in
`org.eclipse.milo.opcua.sdk.client.gds.testing`. It hosts an in-memory GDS on a Milo server for
application-side Pull workflow tests. Tests can control registration and CertificateDirectory
access, pre-register applications, change the advertised application certificate types, delay or
reject requests, and inspect method counters and TrustList file calls. The artifact is listed in
`milo-bom` and is separate from `milo-sdk-client-gds` to avoid adding server dependencies to the
runtime client module. Its new-key requests return PKCS#8 PEM or PKCS#12 PFX according to
`PrivateKeyFormat`, encrypt the private key when `PrivateKeyPassword` is supplied, and reject
unsupported formats with `Bad_InvalidArgument`. PFX output includes the issued certificate and
its issuer.

* * *

## How it works

### Namespace resolution and type registration

The GDS namespace `http://opcfoundation.org/UA/GDS/` gets a different index on every server, so
nothing in the model can be registered before a client has read the server's namespace array.
`GdsClient.create(client)` looks the URI up in the client's `NamespaceTable` (re-reading the array
from the server once if the local copy lacks it), fails with `Bad_NotFound` if the server does not
host the namespace, and otherwise:

1. registers the `ApplicationRecordDataType` codec with the client's static `DataTypeManager`
   through `DataTypeInitializer`, so the structure can be sent as a method input and decoded from
   method outputs;
2. registers the GDS object types with the client's `ObjectTypeManager` through
   `ObjectTypeInitializer`, so `AddressSpace.getObjectNode(directoryId)` returns a
   `CertificateDirectoryTypeNode`;
3. resolves the `Directory` object id (`GdsNodeIds.Directory`, `i=141`).

The namespace index is captured once. A client that is later pointed at a different GDS needs a
new `GdsClient`.

### Method invocation

Every GDS method is a component of the `Directory` object with a NodeId fixed by the NodeSet, so
each wrapper issues exactly one `Call` request with the known ids: `FindApplications` `i=143`,
`RegisterApplication` `i=146`, `UpdateApplication` `i=200`, `UnregisterApplication` `i=149`,
`GetApplication` `i=216`, `QueryApplications` `i=992`, `QueryServers` `i=151`,
`StartSigningRequest` `i=157`, `StartNewKeyPairRequest` `i=154`, `FinishRequest` `i=163`,
`GetCertificateGroups` `i=508`, `GetCertificates` `i=174`, `GetTrustList` `i=204`,
`GetCertificateStatus` `i=225`, `RevokeCertificate` `i=15005`, `CheckRevocationStatus` `i=177`.
No browse or argument read precedes a call. The generated node classes remain available for
callers who want to navigate the `Applications` and `CertificateGroups` folders.

Method wrapper results are not interpreted. A Bad operation-level status becomes a
`UaMethodException` (a `UaException`) carrying the server's `StatusCode`, so a caller branches on
the codes Part 12 defines: `Bad_NothingToDo` from `FinishRequest` means the request is still pending,
`Bad_RequestNotAllowed` means it was rejected, `Bad_UserAccessDenied` means the session lacks the
role, and `Bad_CertificateUriInvalid` means the CSR's ApplicationUri does not match the record.
Output arguments are validated by type and count; a server that returns something other than the
spec's argument list produces `Bad_UnexpectedError` with a message naming the method, the argument,
and the types received. Multi-output methods return records (`FinishRequestResult`,
`CertificatesResult`, `RevocationStatus`, `QueryApplicationsResult`, `QueryServersResult`).

Optional outputs are normalized: a GDS answering a signing request may send either a null Variant
or an empty ByteString for the `PrivateKey` output, and `FinishRequestResult.privateKey()` is null in
both cases; `issuerCertificates()` is an empty array when the chain is not returned.
`TrustListInfo.updateFrequency()` is null when the GDS does not expose the optional
`UpdateFrequency` property.

`resolveCertificateTypeId(groupId, desiredTypeId)` interprets `CertificateTypes` locally. It
returns the desired id for an exact advertised match. It returns null, selecting the group's
default, only when every advertised non-null type is an abstract ancestor of the desired type in
the standard CertificateType hierarchy. It fails with `Bad_NotSupported` when the advertised
types are incompatible, including a list that mixes an abstract ancestor with an incompatible
abstract type or a concrete type other than the desired one, because the group default could then
be a different profile. In particular, `RsaMinApplicationCertificateType` and
`RsaSha256ApplicationCertificateType` are sibling types, so neither can stand in for the other.
Types outside namespace 0 are matched exactly only; their subtype relationships are not
evaluated.

### TrustList files

A TrustList is a `FileType` object whose body is the OPC UA Binary encoding of a bare
`TrustListDataType`, not an `ExtensionObject` (Part 12 §7.8.2). `TrustListReader` calls `Open`
with `OpenFileMode.Read`, loops `Read` until a chunk shorter than the requested length arrives,
calls `Close` in all cases, and decodes the body with `TrustListDataType.Codec`; trailing bytes or a
truncated body fail with `Bad_DecodingError` before anything is applied. The FileType methods are
addressed by their `FileType` declaration ids (`FileType_Open` and so on), which Part 4 §5.12.2.2
permits for any instance of a FileType subtype.

The chunk size comes from the TrustList's optional `MaxByteStringLength` property, else 64 KiB,
capped so a chunk fits inside the client's `EncodingLimits.getMaxMessageSize()`. An explicit-size
overload exists for unusual servers.

`TrustListApplier.apply` reads `SpecifiedLists` as `TrustListMasks` bits and replaces each
specified list in the `TrustListManager` in full. Replacement rather than merging list entries is
what the Pull Model calls for: each specified list is the complete authoritative list, and merging
its entries could never remove a certificate the GDS dropped. All entries are decoded first, so a
malformed certificate or CRL rejects the update without touching the manager. The decoded lists are
then merged into the manager's current snapshot inside `TrustListManager.update`, which Milo's
built-in managers run under their own synchronization. Unspecified lists therefore keep whatever
value they hold at commit time, a concurrent change to one of them is not lost, and a validator
reading `getSnapshot()` sees either the old lists or the new ones, never a mix.

* * *

## Usage

Connect an ordinary `OpcUaClient` to the GDS with whatever identity and certificate validator the
application chooses; the module makes no trust decisions. Then wrap it:

```java
GdsClient gds = GdsClient.create(client);

// Part 12 §6.4: find first, register only when nothing is registered.
ApplicationRecordDataType[] found = gds.findApplications(applicationUri);
NodeId applicationId =
    found.length == 0
        ? gds.registerApplication(
            new ApplicationRecordDataType(
                NodeId.NULL_VALUE,
                applicationUri,
                ApplicationType.Client,
                new LocalizedText[] {LocalizedText.english("My Application")},
                productUri,
                null,
                null))
        : found[0].getApplicationId();

NodeId desiredTypeId = NodeIds.RsaSha256ApplicationCertificateType;
for (NodeId groupId : gds.getCertificateGroups(applicationId)) {
  // Groups such as DefaultUserTokenGroup do not issue application certificates and fail with
  // Bad_NotSupported; skip signing for them but still pull their TrustList.
  NodeId requestTypeId = null;
  boolean issuesDesiredType = true;
  try {
    requestTypeId = gds.resolveCertificateTypeId(groupId, desiredTypeId);
  } catch (UaException e) {
    if (e.getStatusCode().value() != StatusCodes.Bad_NotSupported) throw e;
    issuesDesiredType = false;
  }
  if (issuesDesiredType && gds.getCertificateStatus(applicationId, groupId, requestTypeId)) {
    ByteString csr = ByteString.of(CertificateUtil.generateCsr(keyPair, ...).getEncoded());
    NodeId requestId = gds.startSigningRequest(applicationId, groupId, requestTypeId, csr);
    // Later, and again on Bad_NothingToDo:
    FinishRequestResult issued = gds.finishRequest(applicationId, requestId);
    X509Certificate certificate = CertificateUtil.decodeCertificate(issued.certificate().bytesOrEmpty());
    GdsClient.verifyIssuedCertificate(certificate, keyPair.getPublic(), applicationUri);
    certificateGroup.updateCertificate(
        NodeIds.RsaSha256ApplicationCertificateType, keyPair, new X509Certificate[] {certificate});
  }

  NodeId trustListId = gds.getTrustList(applicationId, groupId);
  TrustListInfo info = gds.readTrustListInfo(trustListId);
  if (info.lastUpdateTime().getUtcTime() > lastApplied.getUtcTime()) {
    TrustListDataType trustList = TrustListReader.read(client, trustListId);
    TrustListApplier.apply(trustList, certificateGroup.getTrustListManager());
  }
}
```

Every method has an `...Async` twin returning a `CompletableFuture`. `StartSigningRequest`,
`StartNewKeyPairRequest`, and `FinishRequest` need an encrypted channel, and `FinishRequest`
should run on a channel using the same certificate as the matching start request (Part 12 §7.9.5).

`GdsClient.verifyIssuedCertificate` checks that an issued certificate carries the caller's public
key and ApplicationUri before it is installed. A certificate for the wrong key cannot be used for
the channel and one with the wrong ApplicationUri is rejected by every peer, and neither problem is
otherwise visible until a connection fails.

The install target on the application side is a `CertificateGroup`: the issued certificate goes in
through `updateCertificate` and the pulled trust list through the group's `TrustListManager`. A
client that pulls therefore configures a real group (`OpcUaClientConfigBuilder.setCertificateGroup`)
rather than the `setCertificateIdentity` shortcut, whose group has no trust list of its own. A
client configures exactly one group and never names it; the `certificateGroupId` arguments above
are the GDS's NodeIds, not the application's.
A server that pulls for several of its own groups registers each under its `CertificateGroupType`
NodeId in its `CertificateManager` and keeps its own mapping from GDS group ids to local groups.

Use the group ids returned by `getCertificateGroups`. The OPC Foundation reference GDS identifies
its default application group in the GDS namespace as
`GdsNodeIds.Directory_CertificateGroups_DefaultApplicationGroup` (`ns=<gds>;i=615`). It is not the
namespace-zero ServerConfiguration group at `ns=0;i=14156`. If a known default id is needed before
registration, resolve the `GdsNodeIds` `ExpandedNodeId` through the connected server's
`NamespaceTable` instead of hard-coding a namespace index.

A null CertificateTypeId means "the group's default". The OPC Foundation reference GDS advertises
the abstract `ApplicationCertificateType` as its only default-group type but rejects that abstract
id when it is passed back to certificate methods. For a desired concrete type such as
`RsaSha256ApplicationCertificateType`, `resolveCertificateTypeId` recognizes the advertised
ancestor and returns null. Pass that nullable result to `GetCertificateStatus`,
`StartSigningRequest`, or `StartNewKeyPairRequest`; null is the portable group-default request.

`GdsPullExample` in `milo-examples/client-examples` runs the sequence once against a GDS named by
the `gds.endpoint`, `gds.username`, and `gds.password` system properties, printing the issued
certificate and the trust list counts without installing anything. While the GDS answers
`Bad_NothingToDo`, the example polls `FinishRequest` every two seconds for up to a minute so an
administrator can approve the request.

* * *

## Generated model

The model is generated and checked in; no code generation runs in the Maven build. The source is
the [opc-ua-gds-model](https://github.com/kevinherron/opc-ua-gds-model) repository at commit
`800aeae`, produced by the GDS generators in
[opc-ua-codegen2](https://github.com/kevinherron/opc-ua-codegen2) at commit `4027daa` from GDS
NodeSet2 1.05.07 on a 1.05.07 base, the same base as Milo's namespace 0 model. Every file is copied with its package
rewritten and the Eclipse Milo license header prepended; nothing else changes.

| Source package (`opc-ua-gds-model`)          | Milo package                                          | Module            |
|----------------------------------------------|-------------------------------------------------------|-------------------|
| `com.digitalpetri.opcua.gds`                 | `org.eclipse.milo.opcua.stack.core.gds`               | `stack-core`      |
| `com.digitalpetri.opcua.gds.types`           | `org.eclipse.milo.opcua.stack.core.gds.types`         | `stack-core`      |
| `com.digitalpetri.opcua.gds` (`BinaryDataTypeDictionaryInitializer` only) | `org.eclipse.milo.opcua.sdk.core.dtd.gds` | `dtd-core` |
| `com.digitalpetri.opcua.gds.client`          | `org.eclipse.milo.opcua.sdk.client.gds.model`               | `sdk-client`      |
| `com.digitalpetri.opcua.gds.client.objects`  | `org.eclipse.milo.opcua.sdk.client.gds.model.objects` | `sdk-client`      |
| `com.digitalpetri.opcua.gds.server`          | `org.eclipse.milo.opcua.sdk.server.gds`               | `sdk-server`      |
| `com.digitalpetri.opcua.gds.server.objects`  | `org.eclipse.milo.opcua.sdk.server.gds.model.objects` | `sdk-server`      |

To pick up a new GDS NodeSet release:

1. Regenerate in `opc-ua-gds-model` and commit there. Do not edit the Milo copy by hand.
2. Copy `src/main/java` of `gds-model-core`, `gds-model-client`, and `gds-model-server` into the
   Milo packages above, rewriting `package` and `import` lines by prefix (most specific prefix
   first: `.client.objects` before `.client`, `.server.objects` before `.server`, `.types` before
   the bare prefix). `BinaryDataTypeDictionaryInitializer` gets the `dtd-core` package because it
   extends `DataTypeDictionaryInitializer` from that module.
3. Prepend the EPL-2.0 header, run `mise exec -- mvn -q spotless:apply`, and compile.
4. Update the commit and NodeSet version recorded in each `package-info.java` and in this
   document.
5. Diff the Milo packages against the source with the prefixes rewritten; only the header and
   import order may differ.

Generated blocking property writers throw `UaException` carrying any non-Good operation status.
Their asynchronous counterparts return the `StatusCode`. The namespace 0 model uses the same
writer generator and follows the same contract.

The GDS `DataTypeInitializer` and both `ObjectTypeInitializer`s take a `NamespaceTable` and must
stay out of the namespace 0 startup path (`DefaultDataTypeManager.createAndInitialize` and the
client and server namespace 0 initializers). `GdsClient.create` invokes them on the client side; a
server hosting the GDS namespace invokes them itself after adding the URI to its table.

* * *

## Limitations

- `GetCertificates`, `CheckRevocationStatus`, and `RevokeCertificate` are `Directory` instance
  methods only from GDS NodeSet 1.05.07. Older deployments fail those wrappers with
  `Bad_MethodInvalid` or `Bad_NodeIdUnknown`.
- `StartNewKeyPairRequest` is a thin wrapper. No helper decodes the returned PFX or PEM private
  key; prefer `StartSigningRequest` with a locally generated key pair.
- KeyCredential management (Part 12 §8), AuthorizationServices (§9), and the GDS audit event types
  are covered only by their generated model classes.
