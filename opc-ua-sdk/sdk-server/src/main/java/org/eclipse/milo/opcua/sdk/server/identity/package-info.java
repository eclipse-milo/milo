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
 * Server-side validation of user identity tokens during ActivateSession.
 *
 * <p>Identity validators are the boundary between OPC UA token parsing and application-specific
 * authentication. The SDK decodes the incoming token, selects the matching {@code UserTokenPolicy}
 * from the active endpoint, and then calls the validator for that token type. Validators return an
 * {@link org.eclipse.milo.opcua.sdk.server.identity.Identity} when authentication succeeds and
 * throw a service-level error when the token is malformed, uses the wrong policy, or fails
 * authentication.
 *
 * <h2>Username-token protection</h2>
 *
 * <p>Username tokens can be sent in three forms: unencrypted for a {@code None} user-token policy,
 * RSA-encrypted with the endpoint certificate for legacy policies, or ECC-protected with the
 * session ephemeral key negotiated in CreateSession. The username validator owns the common token
 * validation, nonce check, size limits, and decryption path before handing the resulting username
 * and password to the application-specific authentication method.
 *
 * <h2>Extension guidance</h2>
 *
 * <p>Applications usually extend the abstract validators and implement only the final credential
 * check. Keep token decoding, nonce validation, and cryptographic policy checks in this package so
 * authentication implementations receive already-validated credentials instead of protocol wire
 * formats.
 */
package org.eclipse.milo.opcua.sdk.server.identity;
