# Message Security and SKS

This page covers securing PubSub messages themselves: signing and encrypting UADP
NetworkMessages, and distributing the symmetric keys that make it work. Read it when you need
messages protected on the wire — against a broker you don't fully trust, on a multicast segment,
or to interoperate with a peer that requires secured PubSub — or when you want Milo to act as the
Security Key Service (SKS) for other publishers and subscribers.

Message security is a different layer than transport security. MQTT TLS
([the mqtt page](mqtt.md#tls-and-authentication)) protects the connection to the broker;
message security protects each NetworkMessage end to end, publisher to subscriber, no matter what
carries it. It applies to the UADP mapping only — on UDP and on UADP-over-MQTT, where the secured
bytes simply travel inside the broker message (the security path is transport-independent, but
the in-repo secured test matrix runs over UDP; treat secured-over-MQTT as applied-but-unverified,
like MQTT TLS). JSON NetworkMessages have no message security in
OPC UA 1.05 (Part 14 7.3.4.1); a secured mode on a JSON-mapped group is rejected at startup with
`Bad_ConfigurationError`, and TLS is the JSON-side substitute.

Because there are no sessions, PubSub security is built on symmetric keys shared through a named
*SecurityGroup*: everyone holding the group's current key can sign/encrypt and verify/decrypt.
The SKS is the Part 14 service that hands those keys out — it has no counterpart in client/server
OPC UA. Milo implements both sides: a pull client that fetches keys from any SKS
(`milo-sdk-pubsub-sks`), and an opt-in server face that makes an `OpcUaServer` *be* the SKS for
its configured SecurityGroups (`milo-sdk-pubsub-server`). For fixed pre-shared keys, no SKS is
needed at all.

## Policies and modes

Part 14 defines exactly two PubSub security policies, and Milo implements both, in both modes,
in both roles. The parameter table lives on `PubSubSecurityPolicy` in
`org.eclipse.milo.opcua.sdk.pubsub.security`:

| | PubSub-Aes128-CTR | PubSub-Aes256-CTR |
|---|---|---|
| URI | `...SecurityPolicy#PubSub-Aes128-CTR` | `...SecurityPolicy#PubSub-Aes256-CTR` |
| Encryption | AES-128 in CTR mode | AES-256 in CTR mode |
| Signature | HMAC-SHA2-256, 32 bytes | HMAC-SHA2-256, 32 bytes |
| Key data per token | 52 bytes | 68 bytes |
| MessageNonce | 8 bytes | 8 bytes |

One key data ByteString is `SigningKey(32) || EncryptingKey(16 or 32) || KeyNonce(4)` (Part 14
Table 155), and each is published under a numeric SecurityTokenId that identifies it on the wire.

`MessageSecurityMode` selects what is protected. `Sign` appends an HMAC signature over the whole
NetworkMessage; `SignAndEncrypt` additionally AES-CTR-encrypts the payload region first. On
decode the order is the safe one: the trailing signature is verified *before* any secured byte is
parsed, and decryption operates on a copy. Neither policy uses a SecurityFooter — Milo never
emits one and skips one if a peer's message carries it.

Wire-format details that matter for interop: sign-only messages carry the real SecurityTokenId
and the full 8-byte MessageNonce (the Part 14 Annex A form, matching open62541); on decode either
sign-only form is accepted, since the nonce length is self-describing. A message with reserved
SecurityFlags bits set is skipped whole, per Table 154. Discovery and metadata messages are
always sent with mode None, whatever their groups configure — see [limitations](#limitations).

## Configuring a secured group

Three pieces make a group secured, and all three are validated together.

First, a `SecurityGroupConfig` at the top of the config declares the security group: its name
(unique in the config), the `securityGroupId` the SKS knows it by (defaults to the name), the
security policy, and the key rotation parameters (`keyLifeTime`, default one hour;
`maxFutureKeyCount` / `maxPastKeyCount`, defaults 0). `securityGroupFolder` and `rolePermissions`
round-trip and feed the [SKS server face](#the-sks-server-face).

Second, a `MessageSecurityConfig` on each writer group and reader group selects the mode and
references the security group. The publisher and subscriber must agree on the group's identity
and keys, not on config names:

```java
SecurityGroupConfig securityGroup =
    SecurityGroupConfig.builder("sg-demo")
        .securityPolicyUri(PubSubSecurityPolicy.PubSubAes256Ctr.getUri())
        .build();

WriterGroupConfig writerGroup =
    WriterGroupConfig.builder("demo")
        .writerGroupId(ushort(1))
        .publishingInterval(Duration.ofMillis(500))
        .messageSecurity(
            MessageSecurityConfig.builder()
                .mode(MessageSecurityMode.SignAndEncrypt)
                .securityGroup(securityGroup.ref())
                .build())
        .dataSetWriter(/* ... */)
        .build();

PubSubConfig config =
    PubSubConfig.builder()
        .securityGroup(securityGroup)
        .publishedDataSet(/* ... */)
        .connection(/* ... */)
        .build();
```

Third, a `SecurityKeyProvider` must be bound for the referenced group — the next section.

Validation is layered the same way as everywhere else in the config model. `build()` rejects a
dangling `SecurityGroupRef` with `PubSubConfigValidationException`. At startup, reconfigure, and
group activation, every *enabled* group with mode `Sign` or `SignAndEncrypt` must have a
resolvable SecurityGroup reference, a supported security policy (where one is named — a null
policy URI is legal and lets the provider's returned policy decide), and a bound key provider;
anything missing fails with `Bad_ConfigurationError` naming the missing piece. Secured JSON
groups fail the same way, with a message pointing at `BrokerSecurityConfig` as the JSON-side
alternative. Disabled groups are tolerated so configs round-trip; a group-level mode of
`Invalid` is treated like `None`.

Where the policy URI can live: on the `SecurityGroupConfig`, or overridden per group on the
`MessageSecurityConfig`. SecurityKeyServices endpoint lists resolve through an inheritance chain
— reader override, else group, else `PubSubConfig.defaultSecurityKeyServices()` —
(`EffectiveMessageSecurity` implements it), which is what an [SKS provider](#pulling-keys-from-an-sks-milo-sdk-pubsub-sks)
consumes at construction.

One knob to not be fooled by: `DataSetReaderConfig.messageSecurity`, the Part 14 per-reader
security override, is configuration-only in this version. It round-trips and its reference is
validated, but the runtime resolves security at the *group* level only — the override is never
consulted for key management or message routing. Give readers with different security needs
different reader groups.

## Binding a key provider

Key material reaches the runtime through the `SecurityKeyProvider` SPI, bound per security group
on the bindings — the same object that binds data sources and listeners:

```java
PubSubBindings bindings =
    PubSubBindings.builder()
        .source(dataSet.ref(), this::readTelemetry)
        .securityKeys(securityGroup.ref(), provider)
        .build();

PubSubService service = PubSubService.create(config, bindings);
```

Binding a provider to a security group name that does not exist in the config throws
`IllegalArgumentException` at create time. The SPI itself is one method —
`getKeys(securityGroupId, startingTokenId, requestedKeyCount)` returning a
`CompletableFuture<SecurityKeySet>` — so anything that can produce Part 14 key sets can plug in.
One contract pin worth knowing even if you never implement it: the `securityPolicyUri` in a
returned key set is authoritative, and a mismatch against a non-null configured policy fails the
fetch rather than silently downgrading.

Two implementations ship.

### Static pre-shared keys

`StaticSecurityKeyProvider` (in `milo-sdk-pubsub`) pins a single key that never rotates —
`of(policy, keyData)` publishes it under SecurityTokenId 1, and an overload takes an explicit
token id. This is the interop shape for peers using pre-shared static keys, and
`loadS2OpcDirectory(Path)` reads the S2OPC/OPC Labs file convention directly: a directory
containing `signingKey.key` (32 bytes), `encryptKey.key` (16 or 32 bytes — the length selects the
policy), and `keyNonce.key` (4 bytes), each holding raw key bytes. All validation failures are
`UaException(Bad_ConfigurationError)` naming the offending file. A static key set carries zero
durations, which the runtime treats as "schedule no rotation, apply no expiry".

### Pulling keys from an SKS: milo-sdk-pubsub-sks

`SksSecurityKeyProvider`, in the separate `org.eclipse.milo:milo-sdk-pubsub-sks` artifact
(managed by `milo-bom`; it depends on `milo-sdk-client`, which is why it is not part of the core
module), fetches rotating keys from a live SKS by calling `GetSecurityKeys` on the well-known ns0
`PublishSubscribe` object. Each fetch is an ephemeral connect–call–disconnect session; no client
connection outlives it.

Resolution follows Part 14 Table 40, which is subtler than "connect to the URL": each configured
`EndpointDescription` entry is an SKS *identity record*, keyed by its `server.applicationUri`.
Per fetch the provider runs GetEndpoints against the entry's `server.discoveryUrls`, keeps only
endpoints whose ApplicationUri matches and whose mode is SignAndEncrypt (an SKS connection is
always encrypted — keys travel in it), constrains by the entry's SecurityPolicyUri if it names
one (otherwise ranks by the server-reported securityLevel), validates the SKS certificate, and
authenticates per the entry's `UserIdentityTokens`. Resolved endpoints are cached and re-resolved
only after a failure; multiple entries are redundant SKS instances, failed over in array order.
The one non-failover failure is an operation-level `Bad_NotFound` — an unknown SecurityGroupId
would 404 at every redundant instance, so it fails the fetch immediately. Entries are validated
against Table 40 at `build()`: hard violations throw `Bad_ConfigurationError`, and the common
non-conformance — a filled `endpointUrl` with empty `discoveryUrls` — is tolerated by default as
a discovery target, with a WARN.

```java
SksSecurityKeyProvider provider =
    SksSecurityKeyProvider.builder()
        .keyServices(securityGroup.getKeyServices())
        .keyPair(applicationKeyPair)
        .certificate(applicationCertificate)
        .applicationUri("urn:example:pubsub:subscriber")
        .trustListManager(trustListManager)
        .build();
```

The builder requirements are deliberate: an application key pair and certificate (the connection
is SignAndEncrypt), the ApplicationUri (it must match the certificate's SAN URI, or session
activation at the SKS will fail — mismatches are warned at build), and a **certificate validator,
with no insecure default** — supply `certificateValidator(...)` or the `trustListManager(...)`
convenience, or `build()` throws. An SKS connection is exactly the place where a
trust-everything default would be a vulnerability.

Identity is chosen per entry from its `UserIdentityTokens`, tried in listed order. Anonymous
needs nothing; username/password comes from a `KeyCredentialStore` — a one-method SPI in
`milo-sdk-pubsub`'s `.security` package, `lookup(resourceUri)` keyed by the SKS ApplicationUri,
with an in-memory implementation shipped — via `keyCredentialStore(...)`; X.509 user identity via
`userIdentityCertificate(...)`. Without a credential store, USERNAME policies are skipped in
favor of other listed token types.

`getKeys` never blocks the caller: fetches run on a provider-owned single-threaded executor. The
provider is `AutoCloseable`; close it when the `PubSubService` using it shuts down.

## The key lifecycle at runtime

Once a secured group activates, the engine's key manager owns the lifecycle — there is nothing to
drive from application code, but the observable behavior is worth knowing:

- **Startup is asynchronous.** A secured writer or reader group stays `PreOperational` until its
  first key fetch succeeds (a secured reader group's readers sit in `Paused` beneath it). A fetch
  that never succeeds within twice the configured `keyLifeTime` fails the group into `Error`; the
  fetch loop keeps retrying and the next success recovers it.
- **Rotation is provider-driven.** Keys are refreshed every KeyLifetime/2 and switched on the
  provider-reported TimeToNextKey/KeyLifetime cadence, re-learned from every response. A publish
  cycle uses one token throughout, even if the switch deadline passes mid-cycle.
- **Expiry stops traffic.** If the last available key expires and no replacement arrives within
  2×KeyLifetime, publishing stops and every group attached to that security group — both roles —
  fails into `Error` with `Bad_SecurityChecksFailed`. Prefer expired-and-loud over unprotected.
- **Nonces never repeat.** Each message's 8-byte MessageNonce is 4 random bytes plus a counter
  that resets per token and is never allowed to wrap within one token; a writer group that
  somehow exhausts it skips cycles (ticking `encryptionErrors`) rather than reuse a (key, nonce)
  pair.
- **Subscribers keep a token window** — previous, current, and the provider's future keys — so
  rotation never drops in-order traffic. A message with an unknown token is dropped and counted,
  and triggers at most one in-flight refresh (rate-floored at 1 s; messages are never buffered
  while it runs). A peer's force-key-reset flag (SecurityFlags bit 3) triggers the same refresh.

On the receive side, what a reader group accepts is a function of its configured mode versus the
received mode (Part 14 7.2.4.3):

| Received → / Configured ↓ | None | Sign | SignAndEncrypt |
|---|---|---|---|
| **None** | accepted | dropped | dropped |
| **Sign** | dropped | verified | verified + decrypted |
| **SignAndEncrypt** | dropped | dropped | verified + decrypted |

Every "dropped" cell ticks `staleKeyMessages` against the group and nothing else — mode mismatches
are a configuration disagreement, not an attack signal. That includes secured traffic that no
secured group claims (by publisher, writer group, and writer ids): its payload is skipped at
decode, and each group whose configured mode rejects it and whose readers it would have matched
still gets its tick — so a subscriber left at mode None sees `staleKeyMessages` move, not silence,
when its publisher turns security on. Secured traffic that matches no reader at all is skipped
without counting: it is not yours. Signature verification runs before the sequence-number window,
so forged traffic never reaches the
[7.2.3 recency tracking](operations.md#sequence-numbers-and-duplicate-handling), and a replayed
message that does verify is dropped there like any other duplicate.

Five security counters join the diagnostics snapshot (`PubSubDiagnostics`), alongside the
counters described in [operations](operations.md#diagnostics):

| Counter | Ticks at | When |
|---|---|---|
| `encryptionErrors` | writer group | A publish cycle skipped for want of usable keys. `lastError` and an error event only on the first skip after a success — a dead key source does not storm the event queue. |
| `decryptionErrors` | reader group | Payload decryption failed after the signature verified. Sets `lastError` and emits an event. |
| `invalidSignatureMessages` | reader group | Signature verification failed. Sets `lastError`, no event (forged traffic must not become event-queue pressure); a rate-limited WARN is logged. |
| `unknownTokenMessages` | reader group | SecurityTokenId outside the token window. Quiet — no `lastError`, no event. |
| `staleKeyMessages` | reader group | No usable key window, or a receive-mode mismatch (the table above). Quiet. |

Security drops never tick `decodeErrors`.

One sizing consequence, covered in [structuring and sizing](structuring-and-sizing.md): a secured
NetworkMessage carries 46–47 bytes of overhead (14-byte SecurityHeader plus the 32-byte
signature, +1 if ExtendedFlags1 was not already present), and a writer group's
`maxNetworkMessageSize` is checked against the final secured size.

## The SKS server face

The other side: `milo-sdk-pubsub-server` can serve keys. It is opt-in and off by default:

```java
ServerPubSubOptions options =
    ServerPubSubOptions.builder().securityKeyServerEnabled(true).build();

ServerPubSub serverPubSub = ServerPubSub.attach(server, config, options);
```

At startup this attaches a `GetSecurityKeys` handler to the ns0 method node and sets the
capability flags (`SupportSecurityKeyPull=true`, `SupportSecurityKeyPush=false`,
`SupportSecurityKeyServer=true`). It works independently of `exposeInformationModel`, and at most
one enabled face per `OpcUaServer` is supported. The served groups are the `SecurityGroupConfig`s
of the current configuration: seeded at attach and refreshed on every successful apply through
`runtime()` or a remote `CloseAndUpdate` — retained groups keep serving their keys undisturbed,
while a group whose `SecurityPolicyUri` or `KeyLifetime` changed has its existing keys
invalidated and regenerated (Part 14 §6.2.12.2). A SecurityGroup with no policy URI is served as
PubSub-Aes256-CTR with a WARN;
an unsupported non-null URI fails attach with `PubSubConfigValidationException`.

Keys are generated lazily from a store-owned `SecureRandom` and rotate by arithmetic, not by
timers: per Part 14 the SecurityTokenId increments every `keyLifeTime` whether or not anyone
asks, so the current token is a pure function of time, and repeated requests inside the retention
window (`maxPastKeyCount` back, `maxFutureKeyCount` forward) return byte-identical key data.
Out-of-window `StartingTokenId` requests are clamped into the window, never errored.

Authorization is deliberately conservative. The handler requires a session on a SignAndEncrypt
channel — anything less is rejected with `Bad_SecurityModeInsufficient`, both by the ns0 node's
access restrictions and again by the handler itself — and consults an authorizer *before*
checking whether the requested group exists, so an unauthorized caller cannot probe for
SecurityGroup names. The default posture:

| Server state | GetSecurityKeys decision |
|---|---|
| RoleMapper configured, group has non-empty `rolePermissions` | Allowed only for a session holding a listed role with the Call permission |
| RoleMapper configured, no per-group permissions | Requires the well-known SecurityKeyServerAccess or SecurityKeyServerAdmin role |
| No RoleMapper, no per-group permissions | Allowed (the channel encryption requirement still applies) |
| No RoleMapper, group has non-empty `rolePermissions` | **Denied** — explicit restrictions fail closed when no roles exist to satisfy them |

To replace the posture wholesale, supply a `PubSubMethodAuthorizer` via
`ServerPubSubOptions.methodAuthorizer(...)` — its `checkKeyAccess` is consulted per
`GetSecurityKeys` call. The same instance's `checkConfigure` gates the
[remote-configuration](server-integration.md#remote-configuration) and diagnostics-`Reset`
handlers, and `checkSksAdmin` gates SecurityGroup-touching `CloseAndUpdate` references; one
posture governs every PubSub method of the attachment.

A note for anyone running the face on a server built from an older Milo: the
security-mode rejection depends on an sdk-server fix that ships in the same release —
`DefaultAccessController` previously collapsed every Call denial to `Bad_UserAccessDenied`, so
the ns0 access restrictions on `GetSecurityKeys` could not surface
`Bad_SecurityModeInsufficient`. The handler's own re-check has no such dependency.

The counterpart consumer works out of the box: an `SksSecurityKeyProvider` pointed at the
server's endpoint pulls from this face, which is exactly what the in-repo
`SksSecuredLoopbackIntegrationTest` proves end to end (SKS server, secured publisher, secured
subscriber, live key rotation). For manual and third-party runs, `PubSubInteropTool` (see
[limitations and interop](limitations-and-interop.md#interop-status)) grew secured-UDP options —
`--security-mode`, `--security-policy`, `--static-keys`/`--static-keys-hex`, and `--sks` for
pulling from a live SKS.

## Limitations

The honest list, in the spirit of [limitations and interop](limitations-and-interop.md):

- **JSON has no message security.** Part 14 v1.05 defines none for the JSON mapping, and Milo
  rejects secured JSON-mapped groups with `Bad_ConfigurationError` rather than inventing a wire
  format. Use MQTT TLS.
- **Push distribution is not implemented.** `SetSecurityKeys` remains unbacked (and the face
  advertises `SupportSecurityKeyPush=false`); key distribution is pull-only. Push targets in a
  Part 14 wire config are a documented mapper loss.
- **SKS management methods are not implemented.** `AddSecurityGroup`, `RemoveSecurityGroup`,
  SecurityGroupFolder management, and key invalidation/rotation methods return
  `Bad_NotImplemented`. Security groups are defined by the attached configuration — editable
  through `runtime()` reconfiguration or a remote
  [`CloseAndUpdate`](server-integration.md#remote-configuration) with SecurityGroup references,
  which the face follows — but not through the management methods.
- **Chunk emission is still absent.** Inbound chunked NetworkMessages — each chunk secured and
  verified individually, per spec — are now reassembled, with hard DoS caps (4 MiB reassembled
  payload, 64 concurrent streams, 10 s idle eviction). Outbound, an oversize message is still
  skipped, never split. See [chunking and message size](limitations-and-interop.md#chunking-and-message-size).
- **Discovery and metadata messages are always mode None**, on the wire in both directions,
  regardless of group security — spec-legal (no rule assigns discovery messages a security
  group), but worth knowing: dataset metadata is not confidential under this design.
- **The reader-level security override is configuration-only.** `DataSetReaderConfig.messageSecurity`
  round-trips and validates but is not consumed by the runtime; security resolves per group.

For how these fit the wider feature matrix, see
[limitations and interop](limitations-and-interop.md#message-security-and-sks); for the
configuration model around it, the [configuration page](configuration.md).
