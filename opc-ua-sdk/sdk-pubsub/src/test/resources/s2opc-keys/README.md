# S2OPC static key-part fixtures

Fixture directories for `StaticSecurityKeyProvider.loadS2OpcDirectory(Path)`, following the
S2OPC/OPC Labs directory convention: `signingKey.key` (32 raw bytes), `encryptKey.key`
(16 or 32 raw bytes), `keyNonce.key` (4 raw bytes). Note the middle file is `encryptKey.key`,
not `encryptingKey.key`.

Key material provenance: `aes128/` is the split of the OPC Labs demo 52-byte static key
(the `static:?key=0101010101020202...0B0B` example: each byte value `01`–`0A` repeated five
times, then `0B 0B`), so the same fixture can serve live OpcCmd/UADemoPublisher interop via
`static:?keyPartsDirectory=`. `aes256/` extends the pattern to 68 bytes (`01`–`0D` five times
each, then `0E 0E 0E`).

| Directory | Contents | Expected loader behavior |
|---|---|---|
| `aes128/` | 32/16/4 bytes | loads; policy inferred PubSub-Aes128-CTR |
| `aes256/` | 32/32/4 bytes | loads; policy inferred PubSub-Aes256-CTR |
| `truncated-signing-key/` | signingKey.key is 31 bytes | `Bad_ConfigurationError` naming signingKey.key |
| `oversized-key-nonce/` | keyNonce.key is 5 bytes | `Bad_ConfigurationError` naming keyNonce.key |
| `invalid-encrypt-key/` | encryptKey.key is 20 bytes | `Bad_ConfigurationError` naming encryptKey.key |
| `missing-key-nonce/` | keyNonce.key absent | `Bad_ConfigurationError` naming keyNonce.key |

Consumed by `org.eclipse.milo.opcua.sdk.pubsub.security.StaticSecurityKeyProviderTest`.
