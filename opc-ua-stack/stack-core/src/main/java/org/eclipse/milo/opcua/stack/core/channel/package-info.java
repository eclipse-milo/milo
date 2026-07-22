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
 * SecureChannel chunking, message serialization, and channel-key state for the OPC UA TCP stack.
 *
 * <h2>Channel lifecycle</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.channel.SecureChannel} carries the local and remote
 * certificate identity, selected security policy, negotiated nonces, and installed {@link
 * org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity} token/key material. Client and server
 * transports establish that state during OpenSecureChannel and then reuse it when encoding or
 * decoding symmetric service chunks.
 *
 * <h2>Chunk flow</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.channel.ChunkEncoder} and {@link
 * org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder} are the byte-level UASC chunk codecs.
 * They frame message headers, security headers, sequence headers, message bodies, signatures,
 * padding, and encrypted payload bytes according to the selected security policy profile.
 *
 * <h2>Runtime boundaries</h2>
 *
 * <p>SecureChannel cryptography is resolved from security policy profiles into authentication,
 * key-agreement, asymmetric-encryption, and chunk-protection strategies. Authentication signs and
 * verifies OpenSecureChannel chunks with application-instance certificates. Asymmetric encryption
 * is a separate responsibility used by legacy RSA OpenSecureChannel encryption. Key agreement turns
 * the negotiated client/server nonces into directional symmetric keys. Chunk protection then uses
 * those keys to sign, verify, pad, encrypt, or decrypt ordinary service chunks.
 *
 * <p>Existing RSA policies use RSA signatures, RSA-nonce P_SHA key derivation, RSA asymmetric
 * OpenSecureChannel encryption, and CBC/HMAC symmetric chunks. ECC and RSA-DH OpenSecureChannel
 * policies carry ephemeral public keys in the nonce fields and keep the first OpenSecureChannel
 * signed but unencrypted; RSA-DH combines RSA application-certificate authentication with ffdhe3072
 * finite-field DH agreement. AEAD policies use the same chunk boundary with mode-dependent
 * invariants. In {@code SignAndEncrypt}, the secure message and symmetric security headers are
 * associated data, the sequence header and message body are encrypted, and the authentication tag
 * is carried as the chunk signature footer. In {@code Sign}, the same AEAD tag is used as a
 * tag-only signature footer: bytes remain plaintext, but the secure message header, symmetric
 * security header, sequence header, and body are still authenticated before the decoder trusts the
 * sequence number. AEAD nonce state belongs to the directional {@code SecretKeys} installed for
 * each token; it combines the token id and LastSequenceNumber with the derived IV base so encoders
 * cannot reuse a key/nonce pair. Enhanced SecureChannel renewals retain the current token's input
 * key material so the next token can be derived from current material XOR fresh key-agreement
 * material, which keeps both endpoints synchronized across token rollover. New policy families
 * should extend those strategy boundaries rather than adding policy-specific branches to transport
 * handlers or generic codecs.
 */
package org.eclipse.milo.opcua.stack.core.channel;
