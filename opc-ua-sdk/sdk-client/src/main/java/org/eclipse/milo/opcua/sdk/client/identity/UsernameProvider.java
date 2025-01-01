/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.identity;

import static java.util.Objects.requireNonNullElse;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.crypto.Cipher;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;

/**
 * An {@link IdentityProvider} that chooses a {@link UserTokenPolicy} with {@link
 * UserTokenType#UserName}.
 */
public class UsernameProvider implements IdentityProvider {

  private final String username;
  private final String password;
  private final CertificateValidator certificateValidator;
  private final Function<List<UserTokenPolicy>, UserTokenPolicy> policyChooser;

  /**
   * Construct a {@link UsernameProvider} that does not validate the remote certificate and selects
   * the first available {@link UserTokenPolicy} with {@link UserTokenType#UserName}.
   *
   * @param username the username to authenticate with.
   * @param password the password to authenticate with.
   */
  public UsernameProvider(String username, String password) {
    this(username, password, new CertificateValidator.InsecureCertificateValidator());
  }

  /**
   * Construct a {@link UsernameProvider} that validates the remote certificate using {@code
   * certificateValidator} and selects the first available {@link UserTokenPolicy} with {@link
   * UserTokenType#UserName}.
   *
   * @param username the username to authenticate with.
   * @param password the password to authenticate with.
   * @param certificateValidator the {@link CertificateValidator} used to validate the remote
   *     certificate.
   */
  public UsernameProvider(
      String username, String password, CertificateValidator certificateValidator) {
    this(username, password, certificateValidator, ps -> ps.get(0));
  }

  /**
   * Construct a {@link UsernameProvider} that validates the remote certificate using {@code
   * certificateValidator} and selects ta {@link UserTokenPolicy} using {@code policyChooser}.
   *
   * <p>Useful if the server might return more than one {@link UserTokenPolicy} with {@link
   * UserTokenType#UserName}.
   *
   * @param username the username to authenticate with.
   * @param password the password to authenticate with.
   * @param certificateValidator the {@link CertificateValidator} used to validate the remote
   *     certificate.
   * @param policyChooser a function that selects a {@link UserTokenPolicy} to use. The policy list
   *     is guaranteed to be non-null and non-empty.
   */
  public UsernameProvider(
      String username,
      String password,
      CertificateValidator certificateValidator,
      Function<List<UserTokenPolicy>, UserTokenPolicy> policyChooser) {

    this.username = username;
    this.password = password;
    this.certificateValidator = certificateValidator;
    this.policyChooser = policyChooser;
  }

  @Override
  public SignedIdentityToken getIdentityToken(EndpointDescription endpoint, ByteString serverNonce)
      throws Exception {

    UserTokenPolicy[] userIdentityTokens =
        requireNonNullElse(endpoint.getUserIdentityTokens(), new UserTokenPolicy[0]);

    List<UserTokenPolicy> tokenPolicies =
        Stream.of(userIdentityTokens)
            .filter(t -> t.getTokenType() == UserTokenType.UserName)
            .collect(Collectors.toList());

    if (tokenPolicies.isEmpty()) {
      throw new Exception("no UserTokenPolicy with UserTokenType.UserName found");
    }

    UserTokenPolicy tokenPolicy = policyChooser.apply(tokenPolicies);

    SecurityPolicy securityPolicy;

    String securityPolicyUri = tokenPolicy.getSecurityPolicyUri();
    try {
      if (securityPolicyUri == null || securityPolicyUri.isEmpty()) {
        securityPolicyUri = endpoint.getSecurityPolicyUri();
      }
      securityPolicy = SecurityPolicy.fromUri(securityPolicyUri);
    } catch (Throwable t) {
      throw new UaException(StatusCodes.Bad_SecurityPolicyRejected, t);
    }

    byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
    byte[] nonceBytes = serverNonce.bytesOrEmpty();

    ByteBuf buffer = Unpooled.buffer();

    if (securityPolicy == SecurityPolicy.None) {
      buffer.writeBytes(passwordBytes);
    } else {
      NonceUtil.validateNonce(serverNonce);

      buffer.writeIntLE(passwordBytes.length + nonceBytes.length);
      buffer.writeBytes(passwordBytes);
      buffer.writeBytes(nonceBytes);

      ByteString bs = endpoint.getServerCertificate();

      if (bs == null || bs.isNull()) {
        throw new UaException(
            StatusCodes.Bad_ConfigurationError,
            "UserTokenPolicy requires encryption but "
                + "server did not provide a certificate in endpoint");
      }

      List<X509Certificate> certificateChain = CertificateUtil.decodeCertificates(bs.bytes());
      X509Certificate certificate = certificateChain.get(0);

      if (SecurityPolicy.None.getUri().equals(endpoint.getSecurityPolicyUri())
          || !Stack.TCP_UASC_UABINARY_TRANSPORT_URI.equals(endpoint.getTransportProfileUri())) {

        // If the SecurityPolicy is None or if this is an HTTP(S) connection the certificate used to
        // encrypt
        // the username and password must be trusted. Otherwise, if it's a secure connection, the
        // certificate
        // will have already been validated and verified when the secure channel or session was
        // created.
        certificateValidator.validateCertificateChain(
            certificateChain,
            endpoint.getServer().getApplicationUri(),
            new String[] {EndpointUtil.getHost(endpoint.getEndpointUrl())});
      }

      int plainTextBlockSize =
          SecureChannel.getAsymmetricPlainTextBlockSize(
              certificate, securityPolicy.getAsymmetricEncryptionAlgorithm());
      int cipherTextBlockSize =
          SecureChannel.getAsymmetricCipherTextBlockSize(
              certificate, securityPolicy.getAsymmetricEncryptionAlgorithm());
      int blockCount = (buffer.readableBytes() + plainTextBlockSize - 1) / plainTextBlockSize;
      Cipher cipher = getAndInitializeCipher(certificate, securityPolicy);

      ByteBuffer plainTextNioBuffer = buffer.nioBuffer();
      ByteBuffer cipherTextNioBuffer =
          Unpooled.buffer(cipherTextBlockSize * blockCount)
              .nioBuffer(0, cipherTextBlockSize * blockCount);

      for (int blockNumber = 0; blockNumber < blockCount; blockNumber++) {
        int position = blockNumber * plainTextBlockSize;
        int limit = Math.min(buffer.readableBytes(), (blockNumber + 1) * plainTextBlockSize);
        ((Buffer) plainTextNioBuffer).position(position);
        ((Buffer) plainTextNioBuffer).limit(limit);

        cipher.doFinal(plainTextNioBuffer, cipherTextNioBuffer);
      }

      ((Buffer) cipherTextNioBuffer).flip();
      buffer = Unpooled.wrappedBuffer(cipherTextNioBuffer);
    }

    byte[] bs = new byte[buffer.readableBytes()];
    buffer.readBytes(bs);

    // UA Part 4, Section 7.35.3 UserNameIdentityToken:
    // encryptionAlgorithm parameter is null if the password is not encrypted.
    String securityAlgorithmUri = securityPolicy.getAsymmetricEncryptionAlgorithm().getUri();
    String encryptionAlgorithm = securityAlgorithmUri.isEmpty() ? null : securityAlgorithmUri;

    UserNameIdentityToken token =
        new UserNameIdentityToken(
            tokenPolicy.getPolicyId(), username, ByteString.of(bs), encryptionAlgorithm);

    return new SignedIdentityToken(token, new SignatureData(null, null));
  }

  private Cipher getAndInitializeCipher(
      X509Certificate serverCertificate, SecurityPolicy securityPolicy) throws UaException {

    assert (serverCertificate != null);

    try {
      String transformation = securityPolicy.getAsymmetricEncryptionAlgorithm().getTransformation();
      Cipher cipher = Cipher.getInstance(transformation);
      cipher.init(Cipher.ENCRYPT_MODE, serverCertificate.getPublicKey());
      return cipher;
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  @Override
  public String toString() {
    return "UsernameProvider{" + "username='" + username + '\'' + '}';
  }

  public static UsernameProvider of(
      String username, String password, CertificateValidator certificateValidator) {

    return new UsernameProvider(username, password, certificateValidator);
  }
}
