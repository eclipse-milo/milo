#!/usr/bin/env python3
"""Independent recompute harness for the UADP message-security golden vectors.

MANUAL / DOCUMENTATION TOOLING — never executed by Maven or CI. Run it by hand:

    python3 check_security_vectors.py [--openssl]

For every ``<name>.bin`` + ``<name>.keys.json`` pair under ``computed/`` (and
``captured/``, if present) this script re-derives the security processing of the
NetworkMessage from OPC 10000-14 v1.05 alone — never by calling any Milo code:

  1. Parses the UADP NetworkMessage header structurally (Tables 156/157) and the
     SecurityHeader (Table 154) to locate the payload region and signature.
  2. Checks the header fields against the expectations in ``keys.json``
     (PublisherId, WriterGroupId, DataSetWriterIds, SecurityTokenId, MessageNonce,
     SecurityFlags per the declared mode).
  3. Cross-checks the ``keyData`` split into SigningKey / EncryptingKey / KeyNonce
     (Table 155 key-data split).
  4. Verifies the trailing HMAC-SHA256 signature over everything before it
     (Part 14 section 7.2.4.4.3.2) with the Python stdlib ``hmac``.
  5. For SignAndEncrypt: assembles the AES-CTR counter block
     KeyNonce(4) || MessageNonce(8) || 00000001 (Table 157, big-endian block
     counter starting at 1), decrypts the payload region, and compares it against
     ``expectedPlaintext``.

AES-CTR uses the ``cryptography`` package when importable, else an
``openssl enc -aes-{128,256}-ctr`` subprocess — pure stdlib + openssl always works.
Everything else is stdlib only.

Exit code 0 iff every check on every vector passes.
"""

import hashlib
import hmac
import json
import subprocess
import sys
from pathlib import Path

SIGNATURE_LENGTH = 32  # HMAC-SHA256, both PubSub policies

POLICIES = {
    "http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes128-CTR": {
        "keyDataLength": 52,
        "encryptingKeyLength": 16,
        "cipher": "aes-128-ctr",
    },
    "http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes256-CTR": {
        "keyDataLength": 68,
        "encryptingKeyLength": 32,
        "cipher": "aes-256-ctr",
    },
}

PUBLISHER_ID_TYPES = {0: ("Byte", 1), 1: ("UInt16", 2), 2: ("UInt32", 4), 3: ("UInt64", 8)}


# ---------------------------------------------------------------------------
# AES-CTR backends
# ---------------------------------------------------------------------------


def _aes_ctr_cryptography(key, iv, data):
    from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes

    decryptor = Cipher(algorithms.AES(key), modes.CTR(iv)).decryptor()
    return decryptor.update(data) + decryptor.finalize()


def _aes_ctr_openssl(key, iv, data):
    cipher = {16: "aes-128-ctr", 32: "aes-256-ctr"}[len(key)]
    result = subprocess.run(
        ["openssl", "enc", "-" + cipher, "-d", "-K", key.hex(), "-iv", iv.hex()],
        input=data,
        capture_output=True,
        check=True,
    )
    return result.stdout


def select_aes_ctr(force_openssl):
    if not force_openssl:
        try:
            import cryptography  # noqa: F401

            return _aes_ctr_cryptography, "cryptography"
        except ImportError:
            pass
    return _aes_ctr_openssl, "openssl subprocess"


# ---------------------------------------------------------------------------
# Structural UADP NetworkMessage parse (Tables 154/156/157) — no Milo code
# ---------------------------------------------------------------------------


class ParseError(Exception):
    pass


class Reader:
    def __init__(self, data):
        self.data = data
        self.offset = 0

    def take(self, n, what):
        if self.offset + n > len(self.data):
            raise ParseError(f"truncated reading {what} at offset {self.offset}")
        chunk = self.data[self.offset : self.offset + n]
        self.offset += n
        return chunk

    def u8(self, what):
        return self.take(1, what)[0]

    def u16(self, what):
        return int.from_bytes(self.take(2, what), "little")

    def u32(self, what):
        return int.from_bytes(self.take(4, what), "little")


def parse_network_message(data):
    """Parse header + SecurityHeader; return a dict of fields, offsets and regions."""
    r = Reader(data)
    m = {}

    b0 = r.u8("UADPFlags")
    m["version"] = b0 & 0x0F
    has_publisher_id = bool(b0 & 0x10)
    has_group_header = bool(b0 & 0x20)
    has_payload_header = bool(b0 & 0x40)

    e1 = r.u8("ExtendedFlags1") if b0 & 0x80 else 0
    publisher_id_type = e1 & 0x07
    has_dataset_class_id = bool(e1 & 0x08)
    has_security = bool(e1 & 0x10)
    has_timestamp = bool(e1 & 0x20)
    has_picoseconds = bool(e1 & 0x40)

    e2 = r.u8("ExtendedFlags2") if e1 & 0x80 else 0
    chunk = bool(e2 & 0x01)
    if e2 & 0x02:
        raise ParseError("PromotedFields not supported by this harness")
    network_message_type = (e2 >> 2) & 0x07
    if network_message_type != 0:
        raise ParseError(f"non-data NetworkMessageType {network_message_type} not supported")

    if has_publisher_id:
        if publisher_id_type not in PUBLISHER_ID_TYPES:
            raise ParseError(f"PublisherIdType {publisher_id_type} not supported")
        type_name, size = PUBLISHER_ID_TYPES[publisher_id_type]
        m["publisherIdType"] = type_name
        m["publisherId"] = int.from_bytes(r.take(size, "PublisherId"), "little")

    if has_dataset_class_id:
        r.take(16, "DataSetClassId")

    if has_group_header:
        group_flags = r.u8("GroupFlags")
        if group_flags & 0x01:
            m["writerGroupId"] = r.u16("WriterGroupId")
        if group_flags & 0x02:
            m["groupVersion"] = r.u32("GroupVersion")
        if group_flags & 0x04:
            m["networkMessageNumber"] = r.u16("NetworkMessageNumber")
        if group_flags & 0x08:
            m["sequenceNumber"] = r.u16("SequenceNumber")

    if has_payload_header:
        if chunk:
            m["dataSetWriterIds"] = [r.u16("chunk DataSetWriterId")]
        else:
            count = r.u8("PayloadHeader Count")
            m["dataSetWriterIds"] = [r.u16(f"DataSetWriterIds[{i}]") for i in range(count)]

    if has_timestamp:
        m["timestamp"] = int.from_bytes(r.take(8, "Timestamp"), "little")
    if has_picoseconds:
        r.take(2, "PicoSeconds")

    if not has_security:
        raise ParseError("ExtendedFlags1 security bit not set — not a secured message")

    m["securityHeaderOffset"] = r.offset
    security_flags = r.u8("SecurityFlags")
    m["signed"] = bool(security_flags & 0x01)
    m["encrypted"] = bool(security_flags & 0x02)
    footer_enabled = bool(security_flags & 0x04)
    m["forceKeyReset"] = bool(security_flags & 0x08)
    m["reservedSecurityFlags"] = security_flags & 0xF0
    m["securityTokenId"] = r.u32("SecurityTokenId")
    nonce_length = r.u8("NonceLength")
    m["nonceLength"] = nonce_length
    m["messageNonce"] = r.take(nonce_length, "MessageNonce")
    footer_size = r.u16("SecurityFooterSize") if footer_enabled else 0

    payload_start = r.offset
    payload_end = len(data) - footer_size - (SIGNATURE_LENGTH if m["signed"] else 0)
    if payload_end < payload_start:
        raise ParseError("payload region is negative — truncated message")
    m["payloadStart"] = payload_start
    m["payloadEnd"] = payload_end
    m["payload"] = data[payload_start:payload_end]
    m["signedRegionEnd"] = len(data) - (SIGNATURE_LENGTH if m["signed"] else 0)
    m["signature"] = data[m["signedRegionEnd"] :]
    return m


# ---------------------------------------------------------------------------
# Per-vector checks
# ---------------------------------------------------------------------------


class Report:
    def __init__(self):
        self.failures = 0

    def check(self, ok, message):
        print(f"  [{'PASS' if ok else 'FAIL'}] {message}")
        if not ok:
            self.failures += 1
        return ok

    def note(self, message):
        print(f"  [note] {message}")


def unhex(keys, field):
    value = keys.get(field)
    return None if value is None else bytes.fromhex(value)


def check_vector(bin_path, keys_path, aes_ctr, report):
    data = bin_path.read_bytes()
    keys = json.loads(keys_path.read_text())
    print(f"== {bin_path.parent.name}/{bin_path.stem} ({keys.get('direction', '?')}) ==")

    # 1. keys.json internal consistency: the Table 155 key-data split
    policy_uri = keys["securityPolicyUri"]
    policy = POLICIES.get(policy_uri)
    if not report.check(policy is not None, f"securityPolicyUri known: {policy_uri}"):
        return
    key_data = unhex(keys, "keyData")
    signing_key = unhex(keys, "signingKey")
    encrypting_key = unhex(keys, "encryptingKey")
    key_nonce = unhex(keys, "keyNonce")
    ekl = policy["encryptingKeyLength"]
    report.check(
        len(key_data) == policy["keyDataLength"],
        f"keyData length {len(key_data)} == {policy['keyDataLength']} for this policy",
    )
    report.check(
        signing_key == key_data[0:32]
        and encrypting_key == key_data[32 : 32 + ekl]
        and key_nonce == key_data[32 + ekl :]
        and len(key_nonce) == 4,
        f"keyData splits into SigningKey(32) / EncryptingKey({ekl}) / KeyNonce(4)",
    )

    # 2. structural parse
    try:
        m = parse_network_message(data)
    except ParseError as e:
        report.check(False, f"structural parse: {e}")
        return
    report.check(m["version"] == 1, f"UADPVersion == 1 (got {m['version']})")
    report.note(
        f"SecurityHeader at {m['securityHeaderOffset']}, payload region "
        f"[{m['payloadStart']}, {m['payloadEnd']}), signature at {m['signedRegionEnd']}"
    )

    # 3. header field expectations from keys.json
    expected_pid = keys.get("publisherId")
    if expected_pid is not None:
        report.check(
            m.get("publisherIdType") == expected_pid["type"]
            and m.get("publisherId") == expected_pid["value"],
            f"PublisherId {expected_pid['type']} == {expected_pid['value']} "
            f"(got {m.get('publisherIdType')} {m.get('publisherId')})",
        )
    if "writerGroupId" in keys:
        report.check(
            m.get("writerGroupId") == keys["writerGroupId"],
            f"WriterGroupId == {keys['writerGroupId']} (got {m.get('writerGroupId')})",
        )
    if "dataSetWriterIds" in keys:
        report.check(
            m.get("dataSetWriterIds") == keys["dataSetWriterIds"],
            f"DataSetWriterIds == {keys['dataSetWriterIds']} (got {m.get('dataSetWriterIds')})",
        )

    # 4. SecurityHeader expectations
    mode = keys["securityMode"]
    encrypted_expected = {"Sign": False, "SignAndEncrypt": True}[mode]
    report.check(
        m["signed"] and m["encrypted"] == encrypted_expected,
        f"SecurityFlags: signed, encrypted == {encrypted_expected} for mode {mode}",
    )
    report.check(m["reservedSecurityFlags"] == 0, "SecurityFlags reserved bits 4-7 are zero")
    report.check(
        m["securityTokenId"] == keys["securityTokenId"],
        f"SecurityTokenId == {keys['securityTokenId']} (got {m['securityTokenId']})",
    )
    message_nonce = unhex(keys, "messageNonce")
    report.check(
        m["nonceLength"] == len(message_nonce) and m["messageNonce"] == message_nonce,
        f"NonceLength == {len(message_nonce)} and MessageNonce == {message_nonce.hex()}",
    )

    # 5. signature: HMAC-SHA256 over everything before the trailing 32 bytes
    recomputed = hmac.new(signing_key, data[: m["signedRegionEnd"]], hashlib.sha256).digest()
    report.check(
        hmac.compare_digest(recomputed, m["signature"]),
        f"HMAC-SHA256 over [0, {m['signedRegionEnd']}) matches the trailing 32-byte signature",
    )

    # 6. payload region vs expectedPlaintext
    expected_plaintext = unhex(keys, "expectedPlaintext")
    if m["encrypted"]:
        iv = key_nonce + message_nonce + (1).to_bytes(4, "big")
        if not report.check(
            len(iv) == 16, f"counter block KeyNonce||MessageNonce||00000001 is 16 bytes: {iv.hex()}"
        ):
            return
        decrypted = aes_ctr(encrypting_key, iv, m["payload"])
        if expected_plaintext is None:
            report.note("no expectedPlaintext — AES-CTR decrypt ran but is unverified")
        else:
            report.check(
                decrypted == expected_plaintext,
                "AES-CTR decrypt of the payload region matches expectedPlaintext",
            )
            # equivalent keystream statement: re-encrypting the plaintext yields the wire bytes
            report.check(
                aes_ctr(encrypting_key, iv, expected_plaintext) == m["payload"],
                "AES-CTR keystream over expectedPlaintext reproduces the wire ciphertext",
            )
    elif expected_plaintext is not None:
        report.check(
            m["payload"] == expected_plaintext,
            "payload region equals expectedPlaintext (sign-only, not encrypted)",
        )


def main():
    force_openssl = "--openssl" in sys.argv[1:]
    aes_ctr, backend = select_aes_ctr(force_openssl)
    print(f"AES-CTR backend: {backend}")

    base = Path(__file__).resolve().parent
    vectors = []
    for sub in ("computed", "captured"):
        directory = base / sub
        if directory.is_dir():
            vectors += sorted(directory.glob("*.bin"))
    if not vectors:
        print("FAIL: no vectors found")
        return 2

    report = Report()
    missing = 0
    for bin_path in vectors:
        keys_path = bin_path.with_name(bin_path.stem + ".keys.json")
        if not keys_path.is_file():
            print(f"== {bin_path.parent.name}/{bin_path.stem} ==")
            print(f"  [FAIL] missing {keys_path.name}")
            missing += 1
            continue
        check_vector(bin_path, keys_path, aes_ctr, report)

    failures = report.failures + missing
    print()
    if failures == 0:
        print(f"RESULT: all {len(vectors)} vectors PASS ({backend})")
        return 0
    print(f"RESULT: {failures} check(s) FAILED across {len(vectors)} vectors")
    return 1


if __name__ == "__main__":
    sys.exit(main())
