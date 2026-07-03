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

import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyMaterial;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.jspecify.annotations.Nullable;

/**
 * Resolves the key material for one received, secured UADP NetworkMessage.
 *
 * <p>Carried into the codec via {@link DecodeContext#securityResolver()}; a {@code null} context
 * component makes every secured message a {@link SecurityOutcome#NO_RESOLVER} skip. Implemented by
 * the receive-side key manager over its token windows; the SecurityHeader does not name the
 * SecurityGroup — "The relation to the SecurityGroup is done through DataSetWriterIds contained in
 * the NetworkMessage" (Part 14 Table 154) — so the resolver maps the plaintext header identifiers
 * to a secured reader group and selects the key for the requested token.
 *
 * <p>Contract (binding):
 *
 * <ul>
 *   <li>Called on the connection dispatch thread, after the plaintext NetworkMessage header is
 *       parsed and before any payload byte is touched.
 *   <li>MUST NOT block and MUST NOT throw for normal misses: unknown tokens and unmatched streams
 *       return a typed {@link Resolution.Refused}. Side effects (unknown-token single-flight
 *       refresh, force-reset refetch) happen inside the implementation, off this thread.
 *   <li>Returns the FIRST matching secured group's keys in declaration order; there is no
 *       try-until-a-signature-verifies loop across candidate groups. A failed verification counts
 *       {@link SecurityOutcome#INVALID_SIGNATURE} against that first group.
 *   <li>{@link Resolution.Keys#keys()} follows the {@link SecurityKeyMaterial} borrow contract: the
 *       material remains valid for the duration of the decode call.
 * </ul>
 */
public interface SecurityContextResolver {

  /**
   * Resolve the key material for one secured NetworkMessage.
   *
   * @param request the plaintext header identifiers and SecurityHeader values of the message.
   * @return the resolved key material, or a typed refusal; never {@code null}, never an exception
   *     for a normal miss.
   */
  Resolution resolve(ResolveRequest request);

  /**
   * The inputs available to key resolution: the plaintext header identifiers of the received
   * NetworkMessage and the security values of its SecurityHeader.
   *
   * @param publisherId the PublisherId, or {@code null} if not present on the wire.
   * @param writerGroupId the WriterGroupId, or {@code null} if not present on the wire.
   * @param dataSetWriterIds the plaintext PayloadHeader DataSetWriterIds; empty when the
   *     PayloadHeader is absent.
   * @param receivedMode the mode indicated by the SecurityFlags: {@link MessageSecurityMode#Sign}
   *     or {@link MessageSecurityMode#SignAndEncrypt}.
   * @param securityTokenId the SecurityTokenId from the SecurityHeader.
   * @param forceKeyReset whether SecurityFlags bit 3 (force key reset) is set: the publisher is
   *     about to invalidate all keys, and the resolver should refetch proactively.
   */
  record ResolveRequest(
      @Nullable PublisherId publisherId,
      @Nullable UShort writerGroupId,
      List<UShort> dataSetWriterIds,
      MessageSecurityMode receivedMode,
      UInteger securityTokenId,
      boolean forceKeyReset) {

    /**
     * Create a new {@link ResolveRequest}.
     *
     * @param publisherId the PublisherId, or {@code null} if not present on the wire.
     * @param writerGroupId the WriterGroupId, or {@code null} if not present on the wire.
     * @param dataSetWriterIds the plaintext PayloadHeader DataSetWriterIds; empty when the
     *     PayloadHeader is absent.
     * @param receivedMode the mode indicated by the SecurityFlags.
     * @param securityTokenId the SecurityTokenId from the SecurityHeader.
     * @param forceKeyReset whether SecurityFlags bit 3 (force key reset) is set.
     */
    public ResolveRequest {
      dataSetWriterIds = List.copyOf(dataSetWriterIds);
    }
  }

  /** The result of a {@link #resolve(ResolveRequest)} call: key material or a typed refusal. */
  sealed interface Resolution {

    /**
     * The resolved key material for the requested token.
     *
     * @param keys the split key material, borrowed under the {@link SecurityKeyMaterial} contract;
     *     valid for the duration of the decode call.
     */
    record Keys(SecurityKeyMaterial keys) implements Resolution {}

    /**
     * A typed refusal: the message is dropped with the given outcome, header-only.
     *
     * @param reason {@link SecurityOutcome#UNKNOWN_TOKEN} or {@link SecurityOutcome#NO_KEYS}.
     */
    record Refused(SecurityOutcome reason) implements Resolution {

      /**
       * Create a new {@link Refused} resolution.
       *
       * @param reason {@link SecurityOutcome#UNKNOWN_TOKEN} or {@link SecurityOutcome#NO_KEYS}.
       * @throws IllegalArgumentException if {@code reason} is any other outcome.
       */
      public Refused {
        if (reason != SecurityOutcome.UNKNOWN_TOKEN && reason != SecurityOutcome.NO_KEYS) {
          throw new IllegalArgumentException("reason must be UNKNOWN_TOKEN or NO_KEYS: " + reason);
        }
      }
    }
  }
}
