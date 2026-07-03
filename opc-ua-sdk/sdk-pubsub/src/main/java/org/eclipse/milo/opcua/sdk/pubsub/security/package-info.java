/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/**
 * PubSub message security primitives and key-provisioning SPIs.
 *
 * <p>{@code SecurityKeyProvider} supplies {@code SecurityKeySet} key material for SecurityGroups
 * (statically via {@code StaticSecurityKeyProvider}, or pulled from a Security Key Service); {@code
 * PubSubSecurityPolicy} carries the PubSub-Aes128-CTR / PubSub-Aes256-CTR parameter tables; {@code
 * SecurityKeyMaterial} splits key data into its signing/encrypting/nonce parts; {@code
 * UadpMessageSecurity} is the single AES-CTR + HMAC choke point used by the UADP codec; {@code
 * KeyCredentialStore} resolves credentials for key-service connections; {@code
 * SecurityKeyServicesValidator} checks Part 14 Table 40 SecurityKeyServices entries where they are
 * consumed.
 *
 * <h2>Key zeroization posture</h2>
 *
 * <p>Key handling is best-effort wipe at the split-key layer, containment everywhere else:
 *
 * <ul>
 *   <li>{@code SecurityKeyMaterial} is the only wipeable owner of key bytes; it holds private
 *       copies and {@code destroy()} zeroes them. Its accessors hand out the live arrays under
 *       borrow semantics — no per-message defensive copies.
 *   <li>{@code ByteString} boundaries ({@code SecurityKeySet.keys}) are never wiped: {@code
 *       ByteString} is an immutable shared value type, so consumers split key sets into {@code
 *       SecurityKeyMaterial} promptly and drop the reference.
 *   <li>{@code char[]} credentials are stored and returned as copies; stores wipe on remove/replace
 *       and callers wipe after use. Secrets are never converted to {@code String}.
 *   <li>No key bytes ever appear in {@code toString()}, logs, or exception messages — lengths and
 *       token ids are fine.
 * </ul>
 *
 * <p><b>JVM limits (why wiping is best-effort):</b> garbage-collector copying and compaction can
 * leave unreachable copies of key bytes behind; {@code Mac} and {@code Cipher} instances retain
 * internal key schedules that cannot be wiped; heap memory is not pinned or locked. Wiping is
 * defense-in-depth, not a guarantee. Destruction is deterministic — no finalizers or cleaners.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.pubsub.security;

import org.jspecify.annotations.NullMarked;
