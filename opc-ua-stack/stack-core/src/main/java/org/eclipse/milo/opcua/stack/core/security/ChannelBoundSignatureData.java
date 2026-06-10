/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import com.google.common.primitives.Bytes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Builds the byte sequences signed during CreateSession and ActivateSession.
 *
 * <p>Legacy security policies sign the peer certificate and nonce. Policies with SecureChannel
 * Enhancements also bind the session signature to the SecureChannel that carried the session
 * service. That binding starts with the first OpenSecureChannel response signature, exposed as the
 * channel thumbprint, and includes certificate hashes from the current channel context.
 *
 * <p>This helper owns only the protocol byte layout. Callers still choose the signing key, verify
 * endpoint certificates, and apply the resulting bytes with the profile's application-signature
 * algorithm.
 */
@NullMarked
public final class ChannelBoundSignatureData {

  private ChannelBoundSignatureData() {}

  /**
   * Return the CreateSession response bytes signed by the server.
   *
   * <p>For SecureChannel-enhancement profiles the returned bytes bind the server signature to the
   * OpenSecureChannel issue exchange, both channel certificates, the CreateSession client nonce,
   * and the CreateSession server nonce. For legacy profiles the returned bytes keep the historical
   * {@code ClientCertificate | ClientNonce} layout.
   *
   * @param profile the security policy profile used by the selected endpoint.
   * @param channelThumbprint the first OpenSecureChannel response signature for enhancement
   *     profiles.
   * @param clientNonce the client nonce from the CreateSession request.
   * @param serverChannelCertificate the server certificate used by the SecureChannel.
   * @param clientChannelCertificate the client certificate used by the SecureChannel.
   * @param serverNonce the server nonce from the CreateSession response.
   * @param clientCertificate the client certificate from the CreateSession request.
   * @return the bytes the server signs for CreateSession.
   * @throws UaException if required enhancement binding bytes are missing or hashing is
   *     unavailable.
   */
  public static byte[] serverSignatureData(
      SecurityPolicyProfile profile,
      @Nullable ByteString channelThumbprint,
      ByteString clientNonce,
      ByteString serverChannelCertificate,
      ByteString clientChannelCertificate,
      ByteString serverNonce,
      ByteString clientCertificate)
      throws UaException {

    if (profile.secureChannelEnhancements()) {
      return Bytes.concat(
          requiredBytes(channelThumbprint, "channel thumbprint"),
          requiredBytes(clientNonce, "client nonce"),
          certificateHash(profile, serverChannelCertificate),
          certificateHash(profile, clientChannelCertificate),
          requiredBytes(serverNonce, "server nonce"));
    } else {
      return Bytes.concat(clientCertificate.bytesOrEmpty(), clientNonce.bytesOrEmpty());
    }
  }

  /**
   * Return the ActivateSession request bytes signed by the client.
   *
   * <p>For SecureChannel-enhancement profiles the returned bytes bind the client signature to the
   * OpenSecureChannel issue exchange, the endpoint server certificate, both channel certificates,
   * the latest server nonce, and the original CreateSession client nonce. For legacy profiles the
   * returned bytes keep the historical {@code ServerCertificate | ServerNonce} layout.
   *
   * <p>On the legacy path {@code serverCertificate} is signed verbatim: the raw {@code ByteString}
   * exactly as it was received in {@code CreateSessionResponse.serverCertificate} (or replayed on
   * reactivation). It is <b>not</b> re-decoded to extract and sign only the leaf certificate
   * encoding. This is deliberate — it matches the OPC UA reference (.NET) stack and the wire
   * semantics, where the signature is computed over the bytes as transmitted. The interop
   * implication: if a peer returns a multi-certificate chain in {@code serverCertificate} but
   * verifies the client signature against only the re-extracted leaf encoding, the two inputs
   * differ and verification fails. Milo's own server avoids this mismatch by verifying with a
   * leaf-then-chain dual attempt (see {@code SessionManager.verifyClientSignature}): it first tries
   * the leaf bytes, then retries with the full chain bytes, so it accepts a signature computed over
   * either form. The behavior is pinned by {@code ChannelBoundSignatureDataTest}.
   *
   * @param profile the security policy profile used by the selected endpoint.
   * @param channelThumbprint the first OpenSecureChannel response signature for enhancement
   *     profiles.
   * @param serverNonce the latest server nonce returned by CreateSession or ActivateSession.
   * @param serverCertificate the server certificate returned by CreateSession; on the legacy path
   *     its raw bytes are signed exactly as received (no leaf re-extraction).
   * @param serverChannelCertificate the server certificate used by the SecureChannel.
   * @param clientChannelCertificate the client certificate used by the SecureChannel.
   * @param clientNonce the original client nonce from CreateSession.
   * @return the bytes the client signs for ActivateSession.
   * @throws UaException if required enhancement binding bytes are missing or hashing is
   *     unavailable.
   */
  public static byte[] clientSignatureData(
      SecurityPolicyProfile profile,
      @Nullable ByteString channelThumbprint,
      ByteString serverNonce,
      ByteString serverCertificate,
      ByteString serverChannelCertificate,
      ByteString clientChannelCertificate,
      ByteString clientNonce)
      throws UaException {

    if (profile.secureChannelEnhancements()) {
      return Bytes.concat(
          requiredBytes(channelThumbprint, "channel thumbprint"),
          requiredBytes(serverNonce, "server nonce"),
          certificateHash(profile, serverCertificate),
          certificateHash(profile, serverChannelCertificate),
          certificateHash(profile, clientChannelCertificate),
          requiredBytes(clientNonce, "client nonce"));
    } else {
      // Sign the serverCertificate bytes verbatim (the chain as received, not a re-extracted
      // leaf), matching the OPC UA reference stack and wire semantics. See the method Javadoc for
      // the interop trade-off and the leaf-then-chain dual-attempt verification on Milo's server.
      return Bytes.concat(serverCertificate.bytesOrEmpty(), serverNonce.bytesOrEmpty());
    }
  }

  private static byte[] certificateHash(SecurityPolicyProfile profile, ByteString certificate)
      throws UaException {

    byte[] bytes = certificate.bytesOrEmpty();
    if (bytes.length == 0) {
      return bytes;
    }

    try {
      MessageDigest digest =
          MessageDigest.getInstance(profile.certificateThumbprintAlgorithm().getTransformation());

      return digest.digest(bytes);
    } catch (NoSuchAlgorithmException e) {
      throw new UaException(StatusCodes.Bad_SecurityPolicyRejected, e);
    }
  }

  private static byte[] requiredBytes(@Nullable ByteString value, String name) throws UaException {
    if (value == null || value.isNullOrEmpty()) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "missing " + name);
    }

    return value.bytesOrEmpty();
  }
}
