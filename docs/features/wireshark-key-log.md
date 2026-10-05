# Wireshark key log

Use [Diagnostics and Wireshark](https://github.com/eclipse-milo/milo/wiki/Diagnostics-and-Wireshark) for Milo 1.2.0-SNAPSHOT capture setup, cleanup, and handling of sensitive key material. The executed capture procedure uses TShark 4.4.19 with Basic256Sha256 and SignAndEncrypt; other analyzer versions, policies, and capture workflows need their own validation.

<a id="table-of-contents"></a>
<a id="overview"></a>
<a id="usage"></a>
<a id="client"></a>
<a id="server"></a>
<a id="loading-in-wireshark"></a>
<a id="custom-listener"></a>

## Application guide

The Wiki covers [key-writer configuration and lifetime](https://github.com/eclipse-milo/milo/wiki/Diagnostics-and-Wireshark#opt-in-to-a-key-log), the tested [capture and inspection procedure](https://github.com/eclipse-milo/milo/wiki/Diagnostics-and-Wireshark#capture-and-inspect), and [capture resource handling](https://github.com/eclipse-milo/milo/wiki/Diagnostics-and-Wireshark#handle-capture-resources). It records the client/server scope, listener failure behavior, analyzer preferences, and limits of the executed capture evidence.

<a id="how-it-works"></a>
<a id="key-components"></a>

## Implementation

The SDK configurations pass the configured [SecurityKeysListener](../../opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/channel/SecurityKeysListener.java) through [ClientApplicationContext](../../opc-ua-stack/transport/src/main/java/org/eclipse/milo/opcua/stack/transport/client/ClientApplicationContext.java) or [ServerApplicationContext](../../opc-ua-stack/transport/src/main/java/org/eclipse/milo/opcua/stack/transport/server/ServerApplicationContext.java). The integration points are `installSecurityToken()` in [UascClientMessageHandler](../../opc-ua-stack/transport/src/main/java/org/eclipse/milo/opcua/stack/transport/client/uasc/UascClientMessageHandler.java) and `openSecureChannel()` in [UascServerAsymmetricHandler](../../opc-ua-stack/transport/src/main/java/org/eclipse/milo/opcua/stack/transport/server/uasc/UascServerAsymmetricHandler.java).

Both paths notify after symmetric key derivation and installation. No listener is called for a None-policy channel. Preserve the callback on initial token installation and renewal when changing these handlers; a valid Session alone does not prove that key export succeeded.

<a id="key-log-file-format"></a>
<a id="design-decisions"></a>
<a id="signing-keys-are-excluded-from-the-keyset"></a>

## Key-log representation

[WiresharkKeyLogWriter](../../opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/channel/WiresharkKeyLogWriter.java) writes one synchronized six-line entry per keyset. Each direction has an IV, encryption key, and decimal signature length; names carry the channel ID and token ID. IVs and keys use uppercase hexadecimal without separators. The writer appends and flushes each entry.

[SecurityKeyset](../../opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/channel/SecurityKeyset.java) carries both directions' encryption material and the signature size, but excludes signing keys. The exact field names, order, and numeric formatting are pinned by the writer test linked below. Keep that format and the analyzer capture result separate: a passing format test does not establish dissector compatibility.

<a id="listener-is-invoked-synchronously-on-the-event-loop"></a>
<a id="securitykeyset-uses-defensive-copying-for-byte-arrays"></a>

## Threading and value ownership

Listener invocation is synchronous on the Netty event-loop thread. Preserve prompt callback completion and thread safety across channels. Slow I/O affects the event loop; there is no fixed file-I/O latency guarantee. The built-in writer logs I/O failures, while a throwing custom listener can fail channel opening or renewal. Applications own any off-thread queue and its shutdown.

`SecurityKeyset` copies byte arrays in its constructor and accessors. Preserve both copies so callers cannot mutate another listener's view or corrupt retained key material.

## Testing

[SecurityKeysetTest](../../opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/channel/SecurityKeysetTest.java) checks constructor and accessor copies and value round-tripping. [WiresharkKeyLogWriterTest](../../opc-ua-stack/stack-core/src/test/java/org/eclipse/milo/opcua/stack/core/channel/WiresharkKeyLogWriterTest.java) checks the exact six-line representation, append behavior, and non-interleaved writes from concurrent threads.

[WikiDiagnosticsTest](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/WikiDiagnosticsTest.java) exercises secured local operations and key-writer ownership. The Wiki records the separate TShark capture check and its tested scope. Follow the repository's [test invocation guidance](../../.claude/docs/running-tests.md) and [test quality guidelines](../../.claude/docs/test-documentation-and-quality-guidelines.md).
