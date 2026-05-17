/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.crypto.Cipher;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.EccEncryptedSecret;
import org.eclipse.milo.opcua.stack.core.security.EccUserTokenAdditionalHeader;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
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
 *
 * <p>The provider protects the password according to the selected user-token security policy. For
 * legacy RSA policies it encrypts the password and server nonce with the endpoint certificate. For
 * supported enhanced ECC or RSA-DH username-token policies it uses the CreateSession
 * additional-header key material carried in {@link IdentityProviderContext} and produces an {@link
 * EccEncryptedSecret}. The supplied password bytes are cleared after token construction.
 */
public class UsernameProvider implements IdentityProvider {

  private final String username;
  private final Supplier<byte[]> passwordSupplier;
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
   * certificateValidator} and selects a {@link UserTokenPolicy} using {@code policyChooser}.
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

    this(
        username,
        () -> password.getBytes(StandardCharsets.UTF_8),
        certificateValidator,
        policyChooser);
  }

  /**
   * Construct a {@link UsernameProvider} that does not validate the remote certificate and selects
   * the first available {@link UserTokenPolicy} with {@link UserTokenType#UserName}.
   *
   * @param username the username to authenticate with.
   * @param passwordSupplier a supplier providing the password bytes. The supplied byte[] will be
   *     zeroed out after being retrieved and used.
   */
  public UsernameProvider(String username, Supplier<byte[]> passwordSupplier) {
    this(
        username,
        passwordSupplier,
        new CertificateValidator.InsecureCertificateValidator(),
        ps -> ps.get(0));
  }

  /**
   * Construct a {@link UsernameProvider} that validates the remote certificate using {@code
   * certificateValidator} and selects the first available {@link UserTokenPolicy} with {@link
   * UserTokenType#UserName}.
   *
   * @param username the username to authenticate with.
   * @param passwordSupplier a supplier providing the password bytes. The supplied byte[] will be
   *     zeroed out after being retrieved and used.
   * @param certificateValidator the {@link CertificateValidator} used to validate the remote
   *     certificate.
   */
  public UsernameProvider(
      String username,
      Supplier<byte[]> passwordSupplier,
      CertificateValidator certificateValidator) {

    this(username, passwordSupplier, certificateValidator, ps -> ps.get(0));
  }

  /**
   * Construct a {@link UsernameProvider} that validates the remote certificate using {@code
   * certificateValidator} and selects a {@link UserTokenPolicy} using {@code policyChooser}.
   *
   * <p>Useful if the server might return more than one {@link UserTokenPolicy} with {@link
   * UserTokenType#UserName}.
   *
   * @param username the username to authenticate with.
   * @param passwordSupplier a supplier providing the password bytes. The supplied byte[] will be
   *     zeroed out after being retrieved and used.
   * @param certificateValidator the {@link CertificateValidator} used to validate the remote
   *     certificate.
   * @param policyChooser a function that selects a {@link UserTokenPolicy} to use. The policy list
   *     is guaranteed to be non-null and non-empty.
   */
  public UsernameProvider(
      String username,
      Supplier<byte[]> passwordSupplier,
      CertificateValidator certificateValidator,
      Function<List<UserTokenPolicy>, UserTokenPolicy> policyChooser) {

    this.username = username;
    this.passwordSupplier = passwordSupplier;
    this.certificateValidator = certificateValidator;
    this.policyChooser = policyChooser;
  }

  @Override
  public Optional<SecurityPolicy> getUserTokenSecurityPolicy(EndpointDescription endpoint)
      throws Exception {

    UserTokenPolicy tokenPolicy = selectTokenPolicy(endpoint);

    return Optional.of(resolveSecurityPolicy(endpoint, tokenPolicy));
  }

  @Override
  public Optional<SecurityPolicy> getEccUserTokenSecurityPolicy(EndpointDescription endpoint)
      throws Exception {

    SecurityPolicy securityPolicy = getUserTokenSecurityPolicy(endpoint).orElseThrow();

    if (EccUserTokenAdditionalHeader.isSupportedEccProfile(securityPolicy.getProfile())) {
      return Optional.of(securityPolicy);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public ExtensionObject getCreateSessionAdditionalHeader(
      EncodingContext context, EndpointDescription endpoint) throws Exception {

    Optional<SecurityPolicy> securityPolicy = getEccUserTokenSecurityPolicy(endpoint);

    if (securityPolicy.isPresent()) {
      return EccUserTokenAdditionalHeader.createRequest(context, securityPolicy.get());
    } else {
      return null;
    }
  }

  @Override
  public SignedIdentityToken getIdentityToken(EndpointDescription endpoint, ByteString serverNonce)
      throws Exception {

    return getIdentityToken(new IdentityProviderContext(endpoint, serverNonce));
  }

  @Override
  public SignedIdentityToken getIdentityToken(IdentityProviderContext context) throws Exception {
    EndpointDescription endpoint = context.getEndpoint();
    ByteString serverNonce = context.getServerNonce();

    Optional<SecurityPolicy> requestedPolicy = context.getUserTokenSecurityPolicy();
    UserTokenPolicy tokenPolicy;

    if (requestedPolicy.isPresent()) {
      tokenPolicy =
          selectTokenPolicy(endpoint, requestedPolicy.get())
              .orElseThrow(
                  () ->
                      new UaException(
                          StatusCodes.Bad_SecurityPolicyRejected,
                          "no username token policy matches requested security policy"));
    } else {
      tokenPolicy = selectTokenPolicy(endpoint);
    }

    SecurityPolicy securityPolicy = resolveSecurityPolicy(endpoint, tokenPolicy);

    if (requestedPolicy.isPresent() && !requestedPolicy.get().equals(securityPolicy)) {
      throw new UaException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "selected username token policy does not match requested security policy");
    }

    byte[] passwordBytes = passwordSupplier.get();

    try {
      byte[] nonceBytes = serverNonce.bytesOrEmpty();

      ByteBuf buffer = Unpooled.buffer();

      if (securityPolicy == SecurityPolicy.None) {
        buffer.writeBytes(passwordBytes);
      } else if (EccUserTokenAdditionalHeader.isSupportedEccProfile(securityPolicy.getProfile())) {

        NonceUtil.validateNonce(serverNonce);

        ByteString encryptedSecret =
            encryptEnhancedPassword(
                context,
                securityPolicy,
                serverNonce,
                ByteString.of(Arrays.copyOf(passwordBytes, passwordBytes.length)));

        UserNameIdentityToken token =
            new UserNameIdentityToken(
                tokenPolicy.getPolicyId(), username, encryptedSecret, securityPolicy.getUri());

        return new SignedIdentityToken(token, new SignatureData(null, null));
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

          // If the SecurityPolicy is None or if this is an HTTP(S) connection the certificate used
          // to encrypt the username and password must be trusted. Otherwise, if it's a secure
          // connection, the certificate will have already been validated and verified when the
          // secure channel or session was created.
          certificateValidator.validateCertificateChain(
              certificateChain,
              endpoint.getServer().getApplicationUri(),
              new String[] {EndpointUtil.getHost(endpoint.getEndpointUrl())},
              securityPolicy.getProfile());
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
    } finally {
      Arrays.fill(passwordBytes, (byte) 0);
    }
  }

  private UserTokenPolicy selectTokenPolicy(EndpointDescription endpoint) throws Exception {
    UserTokenPolicy[] userIdentityTokens =
        requireNonNullElse(endpoint.getUserIdentityTokens(), new UserTokenPolicy[0]);

    List<UserTokenPolicy> tokenPolicies =
        Stream.of(userIdentityTokens)
            .filter(t -> t.getTokenType() == UserTokenType.UserName)
            .collect(Collectors.toList());

    if (tokenPolicies.isEmpty()) {
      throw new Exception("no UserTokenPolicy with UserTokenType.UserName found");
    }

    return policyChooser.apply(tokenPolicies);
  }

  private Optional<UserTokenPolicy> selectTokenPolicy(
      EndpointDescription endpoint, SecurityPolicy securityPolicy) {

    UserTokenPolicy[] userIdentityTokens =
        requireNonNullElse(endpoint.getUserIdentityTokens(), new UserTokenPolicy[0]);

    List<UserTokenPolicy> tokenPolicies =
        Stream.of(userIdentityTokens)
            .filter(t -> t.getTokenType() == UserTokenType.UserName)
            .filter(
                t ->
                    EccUserTokenAdditionalHeader.resolveUserTokenSecurityPolicy(endpoint, t)
                        .map(securityPolicy::equals)
                        .orElse(false))
            .collect(Collectors.toList());

    return tokenPolicies.isEmpty()
        ? Optional.empty()
        : Optional.of(policyChooser.apply(tokenPolicies));
  }

  private static SecurityPolicy resolveSecurityPolicy(
      EndpointDescription endpoint, UserTokenPolicy tokenPolicy) throws UaException {

    String securityPolicyUri = tokenPolicy.getSecurityPolicyUri();

    try {
      if (securityPolicyUri == null || securityPolicyUri.isEmpty()) {
        securityPolicyUri = endpoint.getSecurityPolicyUri();
      }
      return SecurityPolicy.fromUri(securityPolicyUri);
    } catch (Throwable t) {
      throw new UaException(StatusCodes.Bad_SecurityPolicyRejected, t);
    }
  }

  private ByteString encryptEnhancedPassword(
      IdentityProviderContext context,
      SecurityPolicy securityPolicy,
      ByteString serverNonce,
      ByteString password)
      throws UaException {

    ByteString receiverPublicKey =
        context
            .getReceiverEccPublicKey()
            .orElseThrow(
                () ->
                    new UaException(
                        StatusCodes.Bad_SecurityChecksFailed,
                        "server did not provide enhanced user-token key material"));

    KeyPair clientApplicationKeyPair =
        context
            .getClientApplicationKeyPair()
            .orElseThrow(
                () ->
                    new UaException(
                        StatusCodes.Bad_ConfigurationError,
                        "enhanced username token requires a client application key pair"));

    X509Certificate[] clientCertificateChain =
        context
            .getClientCertificateChain()
            .orElseThrow(
                () ->
                    new UaException(
                        StatusCodes.Bad_ConfigurationError,
                        "enhanced username token requires a client certificate chain"));

    return EccEncryptedSecret.encrypt(
        securityPolicy.getProfile(),
        clientApplicationKeyPair,
        clientCertificateChain,
        receiverPublicKey,
        serverNonce,
        password,
        false);
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
}
