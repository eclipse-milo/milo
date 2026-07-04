# UADP message-security golden vectors

Fixtures and an independent recompute harness for UADP message security
(OPC 10000-14 v1.05 §7.2.4.4). Each vector is one raw NetworkMessage (`<name>.bin`,
no transport framing) plus its key material and expectations (`<name>.keys.json`).

`check_security_vectors.py` is **manual / documentation tooling — it is never executed
by Maven or CI** (it lands on the test classpath only because resources are copied
verbatim). Run it by hand whenever the vectors or the security codec change:

```
python3 check_security_vectors.py            # 'cryptography' if importable, else openssl
python3 check_security_vectors.py --openssl  # force the pure stdlib + openssl path
```

It structurally parses each message from the Part 14 tables (never via Milo code), then
verifies the HMAC-SHA256 signature (stdlib `hmac`), the AES-CTR counter-block assembly
`KeyNonce(4) ‖ MessageNonce(8) ‖ 00000001` and payload decryption, the Table 155
key-data split, and the header fields pinned in `keys.json`. Exit code 0 iff every
check on every vector passes.

## Trust chain

```
Part 14 tables (hand-derivation) ──> check_security_vectors.py (python stdlib + openssl)
        │                                        │  must agree
        └──> computed/*.bin  <── UadpSecurityGoldenVectorTest (Milo encoder, injected nonce)
                    └──────────── UadpSecurityVectorFixturesTest (fixtures == encoder output)
```

The Java tests are the source of truth: `UadpSecurityGoldenVectorTest` proves the encoder
against a fully hand-derived worked example and an independent `javax.crypto` recompute;
`UadpSecurityVectorFixturesTest` proves these exported fixtures byte-identical to that
encoder output (and `keys.json` identical to the test constants), so the fixtures cannot
drift. The Python harness then re-derives everything a third time with a non-JVM stack.

## computed/ — encode-direction vectors

Deterministic Milo encoder output: fixed MessageNonce `a1a2a3a4 01000000` (injected via
the `MessageNonceSupplier` seam), SecurityTokenId 7, sequential key data
`00 01 02 ..` (52 or 68 bytes). Message layout (wire-security §12 worked layout):
PublisherId UInt16 4660, WriterGroupId 258, full GroupHeader, 2-DataSetMessage
PayloadHeader (writers 1 and 2, Int32 fields 42/43 — the Sizes array sits inside the
encrypted region), Timestamp; SecurityHeader at offset 28, payload region [42, 62),
HMAC-SHA256 signature in the trailing 32 bytes.

| Vector | Mode | Policy | Key data |
|---|---|---|---|
| `milo-aes128ctr-sign` | Sign | PubSub-Aes128-CTR | 52 B sequential |
| `milo-aes128ctr-se` | SignAndEncrypt | PubSub-Aes128-CTR | 52 B sequential |
| `milo-aes256ctr-sign` | Sign | PubSub-Aes256-CTR | 68 B sequential |
| `milo-aes256ctr-se` | SignAndEncrypt | PubSub-Aes256-CTR | 68 B sequential |

`milo-aes256ctr-se` is bit-identical to the hand-derived worked example in
`UadpSecurityGoldenVectorTest.handDerivedWorkedExampleVector`. Sign-only vectors carry
the real token id and the 8-byte nonce (the Annex A form).

To add a computed vector: extend `UadpSecurityGoldenVectorTest` first, export the bytes
it pins, then extend `UadpSecurityVectorFixturesTest` (it also asserts the exact fixture
set, so an unregistered fixture fails the build) and rerun the harness.

## captured/ — decode-direction vectors

Reserved for third-party captures (open62541 `pubsub_publish_encrypted` with patched
static keys; OPC Labs UADemoPublisher `static:?key=`) produced by the live-interop
secured interop capture procedure — see `milo-pubsub-notes/interop-fleet/SECURED-RUNBOOK.md`.
Curate one NetworkMessage per `.bin` with a hand-written `.keys.json` (same schema);
`UadpSecurityCapturedVectorTest` picks the directory up automatically.
UA-.NETStandard is never a secured-vector source (its PubSub security is stubbed).

**These captures are required before claiming third-party decode coverage**: decode-direction
verification depends on vectors captured from third-party stacks, and until they are committed the
decode direction rests only on Milo-encoder round-trips plus the hand-derived vector. While the
directory is empty, `UadpSecurityCapturedVectorTest`
surfaces one "captures missing" marker test — skipped (aborted) in routine CI, FAILING
when run with `-Dmilo.pubsub.security.requireCapturedVectors=true` (the exit-gate mode) —
so the gap is never reported as a silent green.

## keys.json schema

| Field | Meaning |
|---|---|
| `description`, `source`, `notes` | provenance, free text |
| `direction` | `computed` or `captured` |
| `securityPolicyUri` | `…#PubSub-Aes128-CTR` or `…#PubSub-Aes256-CTR` |
| `securityMode` | `Sign` or `SignAndEncrypt` |
| `securityTokenId` | expected SecurityTokenId (UInt32) |
| `keyData` | hex, 52 or 68 bytes — canonical; the splitter derives the parts |
| `signingKey`, `encryptingKey`, `keyNonce` | hex, redundant split for cross-checking |
| `messageNonce` | hex 8 B, as in the message (computed: the injected value) |
| `publisherId` | `{ "type": "UInt16", "value": 4660 }` |
| `writerGroupId`, `dataSetWriterIds` | expected header identifiers |
| `expectedPlaintext` | hex of the payload region after decrypt (optional for captures) |
