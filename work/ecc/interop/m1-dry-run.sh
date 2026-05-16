#!/usr/bin/env bash
set -euo pipefail

output="${OUTPUT:-work/ecc/interop/m1-dry-run-report.md}"
peer_stack="${PEER_STACK:-not recorded}"
peer_version="${PEER_VERSION:-not recorded}"
endpoint_url="${ENDPOINT_URL:-opc.tcp://<host>:<port>}"
certificate_notes="${CERTIFICATE_NOTES:-Milo and peer trust each other's application instance certificates; ECC_nistP256_AesGcm uses NIST P-256 ECDSA certificates; ECC_curve25519_ChaChaPoly uses Ed25519 application certificates.}"
supported_policies="${SUPPORTED_POLICIES:-ECC_nistP256_AesGcm, ECC_curve25519_ChaChaPoly}"

cat > "${output}" <<REPORT
# M1 ECC Interop Dry Run

- Peer stack: ${peer_stack}
- Peer version/build: ${peer_version}
- Endpoint URL: ${endpoint_url}
- Supported policies observed: ${supported_policies}
- Certificate material assumptions: ${certificate_notes}

## Scenarios

Record PASS/FAIL/SKIPPED and notes for each policy:

| Policy | SecureChannel Issue | SecureChannel Renew | CreateSession | ActivateSession username-token auth | Basic read/write | Notes |
| --- | --- | --- | --- | --- | --- | --- |
| ECC_nistP256_AesGcm |  |  |  |  |  |  |
| ECC_curve25519_ChaChaPoly |  |  |  |  |  |  |

## Procedure

1. Confirm GetEndpoints advertises only supported M1 ECC policies expected for this peer.
2. Confirm each endpoint certificate matches the policy certificate type.
3. Open a SecureChannel with SignAndEncrypt for each policy.
4. Force or wait for SecureChannel Renew and confirm the active session remains usable.
5. CreateSession and ActivateSession with username-token auth for each policy.
6. Read and write a simple scalar node after activation.

## Live Run Status

Live UA-.NETStandard/open62541 peers were not run by this harness automatically. Fill this section
when a peer stack is available.
REPORT

echo "wrote ${output}"
