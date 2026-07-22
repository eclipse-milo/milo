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
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.util.SignatureUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Builds, signs, and verifies the byte sequences exchanged during CreateSession and
 * ActivateSession.
 *
 * <p>Legacy security policies sign the peer certificate and nonce. Policies with SecureChannel
 * Enhancements also bind the session signature to the SecureChannel that carried the session
 * service. That binding starts with the first OpenSecureChannel response signature, exposed as the
 * channel thumbprint, and includes certificate hashes from the current channel context.
 *
 * <p>The {@code *SignatureData} methods own the protocol byte layout; {@link #sign} and {@link
 * #verify} apply the policy's asymmetric algorithm to those bytes and encode the {@link
 * SignatureData#getAlgorithm() algorithm URI} per Part 4 §7.36 (legacy policies carry the URI on
 * the wire; ECC and RSA-DH policies leave it empty). Callers still choose the signing key and
 * verify endpoint certificates, and handle {@link SecurityPolicy#None} (an empty signature)
 * themselves.
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
      return enhancedSignatureData(
          profile,
          channelThumbprint,
          serverNonce,
          clientNonce,
          serverCertificate,
          serverChannelCertificate,
          clientChannelCertificate);
    } else {
      // Sign the serverCertificate bytes verbatim (the chain as received, not a re-extracted
      // leaf), matching the OPC UA reference stack and wire semantics. See the method Javadoc for
      // the interop trade-off and the leaf-then-chain dual-attempt verification on Milo's server.
      return Bytes.concat(serverCertificate.bytesOrEmpty(), serverNonce.bytesOrEmpty());
    }
  }

  /**
   * Return the ActivateSession bytes signed by the user certificate for a {@code Certificate}
   * (X509) user identity token.
   *
   * <p>For SecureChannel-enhancement profiles the returned bytes follow OPC UA Part 4 §6.1.8 Table
   * 101: they bind the user-token signature to the OpenSecureChannel issue exchange, the
   * CreateSession server certificate, both channel certificates, the CreateSession client
   * (application) certificate, and both nonces. This is the {@link #clientSignatureData} layout
   * with an additional {@code HASH(ClientCertificate)} inserted after {@code
   * HASH(ServerChannelCertificate)}. When the carrying SecureChannel is unsecured ({@code
   * SecurityMode} is {@code None}) there is no channel thumbprint or channel certificate to bind,
   * so the reduced {@code ServerNonce | HASH(ServerCertificate) | ClientNonce} layout is used. For
   * legacy profiles the returned bytes keep the historical {@code ServerCertificate | ServerNonce}
   * layout.
   *
   * <p>The signing algorithm is the user-token policy's algorithm (see Part 4 §6.1.8); callers
   * resolve it from that policy rather than this helper. As with {@link #clientSignatureData}, on
   * the legacy path {@code serverCertificate} is hashed/signed using the bytes as received; a
   * verifier may need a leaf-then-chain dual attempt to match a chain-returning peer.
   *
   * @param profile the security policy profile of the selected user-token policy.
   * @param secureChannelSecured {@code true} when the carrying SecureChannel uses a SecurityMode
   *     other than {@code None}; {@code false} selects the reduced unsecured-channel layout.
   * @param channelThumbprint the first OpenSecureChannel response signature for enhancement
   *     profiles on a secured channel.
   * @param serverNonce the latest server nonce returned by CreateSession or ActivateSession.
   * @param serverCertificate the CreateSession server certificate.
   * @param serverChannelCertificate the server certificate used by the SecureChannel.
   * @param clientCertificate the CreateSession client (application) certificate.
   * @param clientChannelCertificate the client certificate used by the SecureChannel.
   * @param clientNonce the original client nonce from CreateSession.
   * @return the bytes the user certificate signs for the ActivateSession user-token signature.
   * @throws UaException if required enhancement binding bytes are missing or hashing is
   *     unavailable.
   */
  public static byte[] userTokenSignatureData(
      SecurityPolicyProfile profile,
      boolean secureChannelSecured,
      @Nullable ByteString channelThumbprint,
      ByteString serverNonce,
      ByteString serverCertificate,
      ByteString serverChannelCertificate,
      ByteString clientCertificate,
      ByteString clientChannelCertificate,
      ByteString clientNonce)
      throws UaException {

    if (!profile.secureChannelEnhancements()) {
      return legacyUserTokenSignatureData(serverCertificate, serverNonce);
    } else if (secureChannelSecured) {
      // Same channel-binding sequence as the application client signature, with an additional
      // HASH(ClientCertificate) inserted after HASH(ServerChannelCertificate).
      return enhancedSignatureData(
          profile,
          channelThumbprint,
          serverNonce,
          clientNonce,
          serverCertificate,
          serverChannelCertificate,
          clientCertificate,
          clientChannelCertificate);
    } else {
      // SecureChannel SecurityMode None: no channel thumbprint or channel certificates to bind.
      return Bytes.concat(
          requiredBytes(serverNonce, "server nonce"),
          certificateHash(profile, serverCertificate),
          requiredBytes(clientNonce, "client nonce"));
    }
  }

  /**
   * Return the legacy {@code Certificate} (X509) user-token signature layout: {@code
   * ServerCertificate | ServerNonce}, the historical layout for non-enhancement policies. As on the
   * legacy application-signature path, {@code serverCertificate} is signed using the bytes as
   * supplied (see {@link #clientSignatureData}).
   *
   * @param serverCertificate the CreateSession server certificate bytes.
   * @param serverNonce the latest server nonce returned by CreateSession or ActivateSession.
   * @return the bytes the user certificate signs for a legacy user-token signature.
   */
  public static byte[] legacyUserTokenSignatureData(
      ByteString serverCertificate, ByteString serverNonce) {

    return Bytes.concat(serverCertificate.bytesOrEmpty(), serverNonce.bytesOrEmpty());
  }

  /**
   * Build the common SecureChannel-enhancement ActivateSession signature sequence: {@code
   * ChannelThumbprint | ServerNonce | HASH(certificate)... | ClientNonce}, where the certificate
   * hashes appear in the supplied order. Shared by {@link #clientSignatureData} and the secured
   * branch of {@link #userTokenSignatureData} so the channel-binding order is defined in one place;
   * each caller supplies the certificate order its layout requires.
   */
  private static byte[] enhancedSignatureData(
      SecurityPolicyProfile profile,
      @Nullable ByteString channelThumbprint,
      ByteString serverNonce,
      ByteString clientNonce,
      ByteString... certificates)
      throws UaException {

    List<byte[]> parts = new ArrayList<>();
    parts.add(requiredBytes(channelThumbprint, "channel thumbprint"));
    parts.add(requiredBytes(serverNonce, "server nonce"));
    for (ByteString certificate : certificates) {
      parts.add(certificateHash(profile, certificate));
    }
    parts.add(requiredBytes(clientNonce, "client nonce"));

    return Bytes.concat(parts.toArray(new byte[0][]));
  }

  /**
   * Verify that an enhanced (SecureChannel-enhancement) user-token policy can be used over the
   * carrying SecureChannel, throwing if not.
   *
   * <p>An enhanced user-token signature either binds to the SecureChannel (the full Part 4 §6.1.8
   * Table 101 layout, available only on an enhanced channel that has a channel thumbprint and
   * channel certificates) or, on an unsecured channel ({@code SecurityMode None}), uses the reduced
   * SecurityMode-None layout. A legacy <em>secured</em> channel offers neither — it has no channel
   * thumbprint to bind and no spec-defined reduced layout — so an enhanced user-token policy is not
   * supported there and is rejected here rather than failing deep in the byte builder.
   *
   * <p>Legacy user-token policies impose no such constraint and pass through unchecked.
   *
   * @param tokenProfile the profile of the resolved user-token policy.
   * @param channelPolicy the {@link SecurityPolicy} of the carrying SecureChannel.
   * @throws UaException if {@code tokenProfile} is an enhancement profile and {@code channelPolicy}
   *     is a legacy policy other than {@code None}.
   */
  public static void checkUserTokenChannelCompatibility(
      SecurityPolicyProfile tokenProfile, SecurityPolicy channelPolicy) throws UaException {

    if (tokenProfile.secureChannelEnhancements()
        && channelPolicy != SecurityPolicy.None
        && !channelPolicy.getProfile().secureChannelEnhancements()) {

      throw new UaException(
          StatusCodes.Bad_IdentityTokenInvalid,
          "an enhanced (ECC or RSA-DH) certificate user-token policy is not supported over a "
              + "legacy secured SecureChannel ("
              + channelPolicy.getUri()
              + "); use an enhanced or unsecured (None) SecureChannel");
    }
  }

  /**
   * Sign {@code data} with a SecurityPolicy's asymmetric signature algorithm and return the wire
   * {@link SignatureData}.
   *
   * <p>ECC policies sign with the ECDSA/EdDSA axis selected by the profile; RSA-DH and legacy
   * policies sign with the policy's asymmetric algorithm. The {@code SignatureData.algorithm} URI
   * is populated only for legacy policies (Part 4 §7.36).
   *
   * @param securityPolicy the SecurityPolicy whose asymmetric algorithm signs {@code data}; must
   *     not be {@link SecurityPolicy#None} (callers emit an empty {@link SignatureData} for {@code
   *     None}).
   * @param privateKey the private key to sign with.
   * @param data the bytes to sign (typically from one of the {@code *SignatureData} builders).
   * @return the {@link SignatureData} to send on the wire.
   * @throws UaException if signing fails or the policy is unsupported.
   */
  public static SignatureData sign(
      SecurityPolicy securityPolicy, PrivateKey privateKey, byte[] data) throws UaException {

    SecurityAlgorithm algorithm = securityPolicy.getAsymmetricSignatureAlgorithm();

    byte[] signature;
    if (algorithm == SecurityAlgorithm.None) {
      signature =
          EccSignatureUtil.sign(securityPolicy.getProfile(), privateKey, ByteBuffer.wrap(data));
    } else {
      signature = SignatureUtil.sign(algorithm, privateKey, ByteBuffer.wrap(data));
    }

    return new SignatureData(
        wireSignatureAlgorithm(securityPolicy, algorithm), ByteString.of(signature));
  }

  /**
   * Verify a {@link SignatureData} (as produced by {@link #sign}) over {@code data} using the
   * public key of {@code certificate}.
   *
   * <p>ECC policies verify with the profile's ECDSA/EdDSA axis; RSA-DH and legacy policies verify
   * with the policy's asymmetric algorithm. For legacy policies the algorithm is taken from the
   * wire URI (Part 4 §7.36); callers that must additionally reject a legacy wire URI disagreeing
   * with the policy do so before calling this method.
   *
   * @param securityPolicy the SecurityPolicy whose asymmetric algorithm verifies the signature.
   * @param certificate the certificate whose public key verifies the signature.
   * @param data the signed bytes.
   * @param signature the {@link SignatureData} to verify.
   * @throws UaException if verification fails or the policy is unsupported.
   */
  public static void verify(
      SecurityPolicy securityPolicy,
      X509Certificate certificate,
      byte[] data,
      SignatureData signature)
      throws UaException {

    SecurityAlgorithm algorithm = securityPolicy.getAsymmetricSignatureAlgorithm();
    byte[] signatureBytes = signature.getSignature().bytesOrEmpty();

    if (algorithm == SecurityAlgorithm.None) {
      EccSignatureUtil.verify(
          securityPolicy.getProfile(),
          certificate.getPublicKey(),
          signatureBytes,
          ByteBuffer.wrap(data));
    } else {
      SignatureUtil.verify(
          verifySignatureAlgorithm(securityPolicy, signature), certificate, data, signatureBytes);
    }
  }

  /**
   * Return the {@code SignatureData.algorithm} URI to put on the wire: the algorithm URI for legacy
   * policies, or {@code null} for SecureChannel-enhancement policies (ECC, RSA-DH), which leave it
   * empty because the algorithm is implied by the SecurityPolicy (Part 4 §7.36).
   *
   * @param securityPolicy the SecurityPolicy that produced the signature.
   * @param algorithm the asymmetric signature algorithm used.
   * @return the wire algorithm URI, or {@code null} for enhancement policies.
   */
  public static @Nullable String wireSignatureAlgorithm(
      SecurityPolicy securityPolicy, SecurityAlgorithm algorithm) {

    return securityPolicy.isLegacy() ? algorithm.getUri() : null;
  }

  /**
   * Return the algorithm to verify a signature with: for legacy policies the algorithm named by the
   * wire URI (Part 4 §7.36); for SecureChannel-enhancement policies the policy's asymmetric
   * algorithm (the wire URI is empty).
   *
   * @param securityPolicy the SecurityPolicy that produced the signature.
   * @param signature the {@link SignatureData} being verified.
   * @return the {@link SecurityAlgorithm} to verify with.
   * @throws UaException if the wire algorithm URI of a legacy signature is not recognized.
   */
  public static SecurityAlgorithm verifySignatureAlgorithm(
      SecurityPolicy securityPolicy, SignatureData signature) throws UaException {

    return securityPolicy.isLegacy()
        ? SecurityAlgorithm.fromUri(signature.getAlgorithm())
        : securityPolicy.getAsymmetricSignatureAlgorithm();
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
