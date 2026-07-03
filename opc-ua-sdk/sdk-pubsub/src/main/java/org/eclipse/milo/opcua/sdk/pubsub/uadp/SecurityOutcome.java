/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.uadp;

/**
 * The outcome of processing the message security of one received, secured UADP NetworkMessage.
 *
 * <p>Surfaced via {@link ReceivedSecurity#outcome()} on {@link DecodedNetworkMessage#security()}.
 * Any outcome other than {@link #VERIFIED} yields a header-only result: empty messages and
 * metadata, no discovery content, no chunk. Non-{@code VERIFIED} outcomes are tolerated skips — not
 * decode failures, never exceptions — the engine maps them to security drop counters. The one
 * exception is a message truncated below its promised SecurityFooter and Signature: a truncation
 * signature that also reports a {@code Bad_DecodingError} {@link DecodedNetworkMessage#failure()}
 * alongside its {@link #INVALID_SIGNATURE} outcome.
 */
public enum SecurityOutcome {

  /** The signature was verified (and, when flagged encrypted, the payload was decrypted). */
  VERIFIED,

  /**
   * The message is secured but the {@link DecodeContext} carries no {@link
   * SecurityContextResolver}: the payload is skipped. Not an error; this is the decode-side
   * equivalent of a receiver with no key infrastructure attached.
   */
  NO_RESOLVER,

  /** The resolver refused: no secured group (or no key material) covers this stream. */
  NO_KEYS,

  /**
   * The resolver refused: the SecurityTokenId is outside the receiver's token window. The resolver
   * triggers its own key refresh; the decoder just drops.
   */
  UNKNOWN_TOKEN,

  /**
   * The trailing signature did not verify, or the message is too short to carry the signature (and
   * SecurityFooter) its SecurityHeader promises. No payload byte was parsed.
   */
  INVALID_SIGNATURE,

  /** The payload could not be decrypted (bad nonce length, cipher init/transform failure). */
  DECRYPT_FAILED
}
