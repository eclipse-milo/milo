/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity.SecretKeys;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity.SecurityKeys;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.util.DigestUtil;

public interface SecureChannel {

  KeyPair getKeyPair();

  X509Certificate getLocalCertificate();

  List<X509Certificate> getLocalCertificateChain();

  X509Certificate getRemoteCertificate();

  List<X509Certificate> getRemoteCertificateChain();

  SecurityPolicy getSecurityPolicy();

  MessageSecurityMode getMessageSecurityMode();

  long getChannelId();

  ChannelSecurity getChannelSecurity();

  SecretKeys getEncryptionKeys(SecurityKeys securityKeys);

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
      SecurityAlgorithm algorithm = getSecurityPolicy().getAsymmetricEncryptionAlgorithm();

      return getAsymmetricCipherTextBlockSize(getLocalCertificate(), algorithm);
    } else {
      return 1;
    }
  }

  default int getRemoteAsymmetricCipherTextBlockSize() {
    if (isAsymmetricEncryptionEnabled()) {
      SecurityAlgorithm algorithm = getSecurityPolicy().getAsymmetricEncryptionAlgorithm();

      return getAsymmetricCipherTextBlockSize(getRemoteCertificate(), algorithm);
    } else {
      return 1;
    }
  }

  default int getLocalAsymmetricPlainTextBlockSize() {
    if (isAsymmetricEncryptionEnabled()) {
      SecurityAlgorithm algorithm = getSecurityPolicy().getAsymmetricEncryptionAlgorithm();

      return getAsymmetricPlainTextBlockSize(getLocalCertificate(), algorithm);
    } else {
      return 1;
    }
  }

  default int getRemoteAsymmetricPlainTextBlockSize() {
    if (isAsymmetricEncryptionEnabled()) {
      SecurityAlgorithm algorithm = getSecurityPolicy().getAsymmetricEncryptionAlgorithm();

      return getAsymmetricPlainTextBlockSize(getRemoteCertificate(), algorithm);
    } else {
      return 1;
    }
  }

  default int getLocalAsymmetricSignatureSize() {
    SecurityAlgorithm algorithm = getSecurityPolicy().getAsymmetricSignatureAlgorithm();

    return getAsymmetricSignatureSize(getLocalCertificate(), algorithm);
  }

  default int getRemoteAsymmetricSignatureSize() {
    SecurityAlgorithm algorithm = getSecurityPolicy().getAsymmetricSignatureAlgorithm();

    return getAsymmetricSignatureSize(getRemoteCertificate(), algorithm);
  }

  default boolean isAsymmetricSigningEnabled() {
    return getSecurityPolicy() != SecurityPolicy.None && getLocalCertificate() != null;
  }

  default boolean isAsymmetricEncryptionEnabled() {
    return getSecurityPolicy() != SecurityPolicy.None
        && getLocalCertificate() != null
        && getRemoteCertificate() != null;
  }

  default int getSymmetricBlockSize() {
    if (isSymmetricEncryptionEnabled()) {
      SecurityAlgorithm algorithm = getSecurityPolicy().getSymmetricEncryptionAlgorithm();

      switch (algorithm) {
        case Aes128:
        case Aes256:
          return 16;
        default:
          return 1;
      }
    } else {
      return 1;
    }
  }

  default int getSymmetricSignatureSize() {
    SecurityAlgorithm algorithm = getSecurityPolicy().getSymmetricSignatureAlgorithm();

    switch (algorithm) {
      case HmacSha1:
        return 20;
      case HmacSha256:
        return 32;
      default:
        return 0;
    }
  }

  default int getSymmetricSignatureKeySize() {
    switch (getSecurityPolicy()) {
      case None:
        return 0;
      case Basic128Rsa15:
        return 16;
      case Basic256:
        return 24;
      case Basic256Sha256:
      case Aes128_Sha256_RsaOaep:
      case Aes256_Sha256_RsaPss:
        return 32;
      default:
        return 0;
    }
  }

  default int getSymmetricEncryptionKeySize() {
    switch (getSecurityPolicy()) {
      case None:
        return 0;
      case Basic128Rsa15:
      case Aes128_Sha256_RsaOaep:
        return 16;
      case Basic256:
      case Basic256Sha256:
      case Aes256_Sha256_RsaPss:
        return 32;
      default:
        return 0;
    }
  }

  default boolean isSymmetricSigningEnabled() {
    return getLocalCertificate() != null
        && getSecurityPolicy() != SecurityPolicy.None
        && (getMessageSecurityMode() == MessageSecurityMode.Sign
            || getMessageSecurityMode() == MessageSecurityMode.SignAndEncrypt);
  }

  default boolean isSymmetricEncryptionEnabled() {
    return getRemoteCertificate() != null
        && getSecurityPolicy() != SecurityPolicy.None
        && getMessageSecurityMode() == MessageSecurityMode.SignAndEncrypt;
  }

  static int getAsymmetricKeyLength(Certificate certificate) {
    PublicKey publicKey = certificate != null ? certificate.getPublicKey() : null;

    return (publicKey instanceof RSAPublicKey)
        ? ((RSAPublicKey) publicKey).getModulus().bitLength()
        : 0;
  }

  static int getAsymmetricSignatureSize(Certificate certificate, SecurityAlgorithm algorithm) {
    switch (algorithm) {
      case RsaSha1:
      case RsaSha256:
      case RsaSha256Pss:
        return (getAsymmetricKeyLength(certificate) + 7) / 8;
      default:
        return 0;
    }
  }

  static int getAsymmetricCipherTextBlockSize(
      Certificate certificate, SecurityAlgorithm algorithm) {
    switch (algorithm) {
      case Rsa15:
      case RsaOaepSha1:
      case RsaOaepSha256:
        return (getAsymmetricKeyLength(certificate) + 7) / 8;
      default:
        return 1;
    }
  }

  static int getAsymmetricPlainTextBlockSize(
      X509Certificate certificate, SecurityAlgorithm algorithm) {
    switch (algorithm) {
      case Rsa15:
        return (getAsymmetricKeyLength(certificate) + 7) / 8 - 11;
      case RsaOaepSha1:
        return (getAsymmetricKeyLength(certificate) + 7) / 8 - 42;
      case RsaOaepSha256:
        return (getAsymmetricKeyLength(certificate) + 7) / 8 - 66;
      default:
        return 1;
    }
  }
}
