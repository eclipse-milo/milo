/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel;

import static org.eclipse.milo.opcua.stack.core.util.CertificateUtil.getCertificateChainBytes;

import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity.SecretKeys;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity.SecurityKeys;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.util.DigestUtil;

/**
 * Negotiated OPC UA Secure Conversation state used by chunk encoders, chunk decoders, and
 * transports.
 *
 * <p>A {@code SecureChannel} is the security context installed on a transport after
 * OpenSecureChannel has selected a policy, exchanged endpoint certificates and nonces, and created
 * token-specific key material. Channel codecs use it to answer three questions while processing a
 * chunk: which security profile is active, which certificate identity belongs to each side, and
 * which directional keys apply to bytes being sent or received.
 *
 * <p>The local and remote directions depend on the implementation. A client sends with client keys
 * and receives with server keys; a server sends with server keys and receives with client keys.
 * {@link #getEncryptionKeys(SecurityKeys)} and {@link #getDecryptionKeys(SecurityKeys)} hide that
 * direction switch so the codecs can stay independent of a client/server role.
 */
public interface SecureChannel {

  KeyPair getKeyPair();

  X509Certificate getLocalCertificate();

  List<X509Certificate> getLocalCertificateChain();

  X509Certificate getRemoteCertificate();

  List<X509Certificate> getRemoteCertificateChain();

  SecurityPolicy getSecurityPolicy();

  /** Returns the structured security-policy profile negotiated for this channel. */
  default SecurityPolicyProfile getSecurityPolicyProfile() {
    return getSecurityPolicy().getProfile();
  }

  MessageSecurityMode getMessageSecurityMode();

  long getChannelId();

  ChannelSecurity getChannelSecurity();

  /**
   * Returns the channel thumbprint established by SecureChannel-enhancement policies.
   *
   * <p>For OPC UA TCP, this is the signature from the first OpenSecureChannel response. Legacy
   * policies and transports without a channel-bound signature return {@link ByteString#NULL_VALUE}.
   *
   * @return the negotiated channel thumbprint, or {@link ByteString#NULL_VALUE}.
   */
  default ByteString getChannelThumbprint() {
    return ByteString.NULL_VALUE;
  }

  /**
   * Returns the keys used to protect chunks sent by the local endpoint under {@code securityKeys}.
   */
  SecretKeys getEncryptionKeys(SecurityKeys securityKeys);

  /**
   * Returns the keys used to verify or decrypt chunks received by the local endpoint under {@code
   * securityKeys}.
   */
  SecretKeys getDecryptionKeys(SecurityKeys securityKeys);

  ByteString getLocalNonce();

  ByteString getRemoteNonce();

  default ByteString getLocalCertificateBytes() throws UaException {
    try {
      return getLocalCertificate() != null
          ? ByteString.of(getLocalCertificate().getEncoded())
          : ByteString.NULL_VALUE;
    } catch (CertificateEncodingException e) {
      throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
    }
  }

  default ByteString getLocalCertificateChainBytes() throws UaException {
    List<X509Certificate> localCertificateChain = getLocalCertificateChain();

    if (localCertificateChain != null) {
      return getCertificateChainBytes(localCertificateChain);
    } else {
      return ByteString.NULL_VALUE;
    }
  }

  default ByteString getLocalCertificateThumbprint() throws UaException {
    try {
      return getLocalCertificate() != null
          ? ByteString.of(DigestUtil.sha1(getLocalCertificate().getEncoded()))
          : ByteString.NULL_VALUE;
    } catch (CertificateEncodingException e) {
      throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
    }
  }

  default ByteString getRemoteCertificateBytes() throws UaException {
    try {
      return getRemoteCertificate() != null
          ? ByteString.of(getRemoteCertificate().getEncoded())
          : ByteString.NULL_VALUE;
    } catch (CertificateEncodingException e) {
      throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
    }
  }

  default ByteString getRemoteCertificateChainBytes() throws UaException {
    List<X509Certificate> remoteCertificateChain = getRemoteCertificateChain();

    if (remoteCertificateChain != null) {
      return getCertificateChainBytes(remoteCertificateChain);
    } else {
      return ByteString.NULL_VALUE;
    }
  }

  default ByteString getRemoteCertificateThumbprint() throws UaException {
    try {
      return getRemoteCertificate() != null
          ? ByteString.of(DigestUtil.sha1(getRemoteCertificate().getEncoded()))
          : ByteString.NULL_VALUE;
    } catch (CertificateEncodingException e) {
      throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
    }
  }

  default int getLocalAsymmetricCipherTextBlockSize() {
    if (isAsymmetricEncryptionEnabled()) {
      return SecureChannelStrategies.asymmetricEncryption(getSecurityPolicyProfile())
          .cipherTextBlockSize(getLocalCertificate());
    } else {
      return 1;
    }
  }

  default int getRemoteAsymmetricCipherTextBlockSize() {
    if (isAsymmetricEncryptionEnabled()) {
      return SecureChannelStrategies.asymmetricEncryption(getSecurityPolicyProfile())
          .cipherTextBlockSize(getRemoteCertificate());
    } else {
      return 1;
    }
  }

  default int getLocalAsymmetricPlainTextBlockSize() {
    if (isAsymmetricEncryptionEnabled()) {
      return SecureChannelStrategies.asymmetricEncryption(getSecurityPolicyProfile())
          .plainTextBlockSize(getSecurityPolicyProfile(), getLocalCertificate());
    } else {
      return 1;
    }
  }

  default int getRemoteAsymmetricPlainTextBlockSize() {
    if (isAsymmetricEncryptionEnabled()) {
      return SecureChannelStrategies.asymmetricEncryption(getSecurityPolicyProfile())
          .plainTextBlockSize(getSecurityPolicyProfile(), getRemoteCertificate());
    } else {
      return 1;
    }
  }

  default int getLocalAsymmetricSignatureSize() {
    return SecureChannelStrategies.authentication(getSecurityPolicyProfile())
        .signatureSize(getLocalCertificate());
  }

  default int getRemoteAsymmetricSignatureSize() {
    return SecureChannelStrategies.authentication(getSecurityPolicyProfile())
        .signatureSize(getRemoteCertificate());
  }

  /**
   * Returns whether OpenSecureChannel chunks carry an asymmetric signature.
   *
   * <p>Signing is intentionally separate from asymmetric encryption. Some profile families
   * authenticate OpenSecureChannel chunks with certificates without RSA-encrypting those chunks.
   */
  default boolean isAsymmetricSigningEnabled() {
    return getSecurityPolicy() != SecurityPolicy.None && getLocalCertificate() != null;
  }

  /**
   * Returns whether OpenSecureChannel chunks are encrypted with the peer certificate.
   *
   * <p>This is only true for policies that define a certificate-based asymmetric encryption
   * algorithm and have both local and remote certificate identities available.
   */
  default boolean isAsymmetricEncryptionEnabled() {
    return getSecurityPolicy() != SecurityPolicy.None
        && getLocalCertificate() != null
        && getRemoteCertificate() != null
        && getSecurityPolicyProfile().asymmetricEncryptionAlgorithm() != SecurityAlgorithm.None;
  }

  /**
   * Returns the symmetric cipher block size, or {@code 1} when symmetric encryption is disabled.
   */
  default int getSymmetricBlockSize() {
    if (isSymmetricEncryptionEnabled()) {
      return getSecurityPolicy().getProfile().symmetricBlockSize();
    } else {
      return 1;
    }
  }

  default int getSymmetricSignatureSize() {
    return getSecurityPolicy().getProfile().symmetricSignatureSize();
  }

  default int getSymmetricSignatureKeySize() {
    return getSecurityPolicy().getProfile().symmetricSignatureKeySize();
  }

  default int getSymmetricEncryptionKeySize() {
    return getSecurityPolicy().getProfile().symmetricEncryptionKeySize();
  }

  /** Returns whether symmetric chunks are signed for the negotiated message security mode. */
  default boolean isSymmetricSigningEnabled() {
    return getLocalCertificate() != null
        && getSecurityPolicy() != SecurityPolicy.None
        && (getMessageSecurityMode() == MessageSecurityMode.Sign
            || getMessageSecurityMode() == MessageSecurityMode.SignAndEncrypt);
  }

  /** Returns whether symmetric chunks are encrypted for the negotiated message security mode. */
  default boolean isSymmetricEncryptionEnabled() {
    return getRemoteCertificate() != null
        && getSecurityPolicy() != SecurityPolicy.None
        && getMessageSecurityMode() == MessageSecurityMode.SignAndEncrypt;
  }

  /*
   * Compatibility helpers for callers that still ask RSA sizing questions by SecurityAlgorithm.
   * Channel codecs should resolve operations from the negotiated SecurityPolicyProfile instead.
   */
  static int getAsymmetricKeyLength(Certificate certificate) {
    return SecureChannelStrategies.getRsaKeyLength(certificate);
  }

  static int getAsymmetricSignatureSize(Certificate certificate, SecurityAlgorithm algorithm) {
    return switch (algorithm) {
      case RsaSha1, RsaSha256, RsaSha256Pss ->
          SecureChannelStrategies.rsaSignatureSize(certificate);
      default -> 0;
    };
  }

  static int getAsymmetricCipherTextBlockSize(
      Certificate certificate, SecurityAlgorithm algorithm) {
    return switch (algorithm) {
      case Rsa15, RsaOaepSha1, RsaOaepSha256 ->
          SecureChannelStrategies.rsaCipherTextBlockSize(certificate);
      default -> 1;
    };
  }

  static int getAsymmetricPlainTextBlockSize(
      X509Certificate certificate, SecurityAlgorithm algorithm) {
    return SecureChannelStrategies.rsaPlainTextBlockSize(certificate, algorithm);
  }
}
