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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.crypto.Cipher;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.EccEncryptedSecret;
import org.eclipse.milo.opcua.stack.core.security.EnhancedUserTokenAdditionalHeader;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.UserTokenSecurityPolicyRules;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.CipherFactory;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link IdentityProvider} that chooses a {@link UserTokenPolicy} with {@link
 * UserTokenType#UserName}.
 *
 * <p>The provider protects the password according to the selected user-token security policy. For
 * legacy RSA policies it encrypts the password and server nonce with the endpoint certificate. For
 * supported enhanced ECC or RSA-DH username-token policies it uses the CreateSession
 * additional-header key material carried in {@link IdentityProviderContext} and produces an {@link
 * EccEncryptedSecret}. The supplied password bytes are cleared after token construction.
 *
 * <p>OPC UA Part 4 (7.41) lets a client encrypt the password only to a server certificate it
 * trusts. On a secured {@code opc.tcp} endpoint the client's validator, set with {@link
 * OpcUaClientConfigBuilder#setCertificateValidator}, has already checked the endpoint's certificate
 * when the SecureChannel opened and again at CreateSession, and this provider does not check it
 * again. On an endpoint whose SecurityPolicy is {@code None}, or on a transport other than {@code
 * opc.tcp}, nothing has checked it, so before encrypting the password to it the provider validates
 * it: with the validator passed to its constructor or, when none was passed, with the client's
 * validator. When neither the provider nor the client has a validator, the certificate is not
 * validated.
 *
 * <p>A username {@link UserTokenPolicy} whose security policy is {@code None} sends the password as
 * plain UTF-8, which only a {@code SignAndEncrypt} SecureChannel or a TLS transport keeps private
 * (Part 4 (7.40.4)). The provider sends it as the policy asks, and logs a WARN the first time it
 * sends a password that way over a {@code None} or {@code Sign} SecureChannel.
 *
 * <p>When an endpoint advertises more than one username {@link UserTokenPolicy}, the provider
 * prefers one whose security policy is usable on the current SecureChannel and skips those that are
 * not (for example an ECC or RSA-DH policy offered on a {@code None} channel), as required by OPC
 * UA Part 4 (7.41). The {@link #enforceUserTokenSecurityPolicyRules} guards still reject the chosen
 * policy if no usable one was advertised.
 */
public class UsernameProvider implements IdentityProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(UsernameProvider.class);

  private final AtomicBoolean unencryptedPasswordWarningLogged = new AtomicBoolean(false);

  private final String username;
  private final Supplier<byte[]> passwordSupplier;
  private final @Nullable CertificateValidator certificateValidator;
  private final Function<List<UserTokenPolicy>, UserTokenPolicy> policyChooser;

  /**
   * Construct a {@link UsernameProvider} that validates the server certificate with the client's
   * validator and selects the first available {@link UserTokenPolicy} with {@link
   * UserTokenType#UserName}.
   *
   * <p>The certificate validated is the one the password is encrypted to where the SecureChannel
   * has not already checked it, as on an endpoint whose SecurityPolicy is {@code None}. When the
   * client has no validator, it is not validated.
   *
   * @param username the username to authenticate with.
   * @param password the password to authenticate with.
   */
  public UsernameProvider(String username, String password) {
    this(username, password, null, ps -> ps.get(0));
  }

  /**
   * Construct a {@link UsernameProvider} that validates the server certificate with {@code
   * certificateValidator} instead of the client's validator, and selects the first available {@link
   * UserTokenPolicy} with {@link UserTokenType#UserName}.
   *
   * @param username the username to authenticate with.
   * @param password the password to authenticate with.
   * @param certificateValidator the {@link CertificateValidator} used to validate the server
   *     certificate the password is encrypted to, where the SecureChannel has not already checked
   *     it, as on an endpoint whose SecurityPolicy is {@code None}.
   */
  public UsernameProvider(
      String username, String password, CertificateValidator certificateValidator) {
    this(username, password, certificateValidator, ps -> ps.get(0));
  }

  /**
   * Construct a {@link UsernameProvider} that validates the server certificate with {@code
   * certificateValidator}, or with the client's validator when it is {@code null}, and selects a
   * {@link UserTokenPolicy} using {@code policyChooser}.
   *
   * <p>Useful if the server might return more than one {@link UserTokenPolicy} with {@link
   * UserTokenType#UserName}.
   *
   * @param username the username to authenticate with.
   * @param password the password to authenticate with.
   * @param certificateValidator the {@link CertificateValidator} used to validate the server
   *     certificate the password is encrypted to, where the SecureChannel has not already checked
   *     it, as on an endpoint whose SecurityPolicy is {@code None}; or {@code null} to use the
   *     client's validator.
   * @param policyChooser a function that selects a {@link UserTokenPolicy} to use. The provided
   *     list is non-null and non-empty; when the endpoint advertises more than one username policy
   *     it is restricted to the policies usable on the current SecureChannel (per Part 4 (7.41)),
   *     falling back to all advertised username policies only when none are usable. The list may
   *     therefore be shorter than the advertised set, so a chooser should select by policy property
   *     rather than by fixed index and should be deterministic, so CreateSession negotiation and a
   *     later reactivation resolve the same policy.
   */
  public UsernameProvider(
      String username,
      String password,
      @Nullable CertificateValidator certificateValidator,
      Function<List<UserTokenPolicy>, UserTokenPolicy> policyChooser) {

    this(
        username,
        () -> password.getBytes(StandardCharsets.UTF_8),
        certificateValidator,
        policyChooser);
  }

  /**
   * Construct a {@link UsernameProvider} that validates the server certificate with the client's
   * validator and selects the first available {@link UserTokenPolicy} with {@link
   * UserTokenType#UserName}.
   *
   * <p>The certificate validated is the one the password is encrypted to where the SecureChannel
   * has not already checked it, as on an endpoint whose SecurityPolicy is {@code None}. When the
   * client has no validator, it is not validated.
   *
   * @param username the username to authenticate with.
   * @param passwordSupplier a supplier providing the password bytes. The supplied byte[] will be
   *     zeroed out after being retrieved and used.
   */
  public UsernameProvider(String username, Supplier<byte[]> passwordSupplier) {
    this(username, passwordSupplier, null, ps -> ps.get(0));
  }

  /**
   * Construct a {@link UsernameProvider} that validates the server certificate with {@code
   * certificateValidator} instead of the client's validator, and selects the first available {@link
   * UserTokenPolicy} with {@link UserTokenType#UserName}.
   *
   * @param username the username to authenticate with.
   * @param passwordSupplier a supplier providing the password bytes. The supplied byte[] will be
   *     zeroed out after being retrieved and used.
   * @param certificateValidator the {@link CertificateValidator} used to validate the server
   *     certificate the password is encrypted to, where the SecureChannel has not already checked
   *     it, as on an endpoint whose SecurityPolicy is {@code None}.
   */
  public UsernameProvider(
      String username,
      Supplier<byte[]> passwordSupplier,
      CertificateValidator certificateValidator) {

    this(username, passwordSupplier, certificateValidator, ps -> ps.get(0));
  }

  /**
   * Construct a {@link UsernameProvider} that validates the server certificate with {@code
   * certificateValidator}, or with the client's validator when it is {@code null}, and selects a
   * {@link UserTokenPolicy} using {@code policyChooser}.
   *
   * <p>Useful if the server might return more than one {@link UserTokenPolicy} with {@link
   * UserTokenType#UserName}.
   *
   * @param username the username to authenticate with.
   * @param passwordSupplier a supplier providing the password bytes. The supplied byte[] will be
   *     zeroed out after being retrieved and used.
   * @param certificateValidator the {@link CertificateValidator} used to validate the server
   *     certificate the password is encrypted to, where the SecureChannel has not already checked
   *     it, as on an endpoint whose SecurityPolicy is {@code None}; or {@code null} to use the
   *     client's validator.
   * @param policyChooser a function that selects a {@link UserTokenPolicy} to use. The provided
   *     list is non-null and non-empty; when the endpoint advertises more than one username policy
   *     it is restricted to the policies usable on the current SecureChannel (per Part 4 (7.41)),
   *     falling back to all advertised username policies only when none are usable. The list may
   *     therefore be shorter than the advertised set, so a chooser should select by policy property
   *     rather than by fixed index and should be deterministic, so CreateSession negotiation and a
   *     later reactivation resolve the same policy.
   */
  public UsernameProvider(
      String username,
      Supplier<byte[]> passwordSupplier,
      @Nullable CertificateValidator certificateValidator,
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
  public Optional<SecurityPolicy> getEnhancedUserTokenSecurityPolicy(EndpointDescription endpoint)
      throws Exception {

    SecurityPolicy securityPolicy = getUserTokenSecurityPolicy(endpoint).orElseThrow();

    if (securityPolicy.getProfile().usesEnhancedUserTokenSecret()) {
      return Optional.of(securityPolicy);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public ExtensionObject getCreateSessionAdditionalHeader(
      EncodingContext context, EndpointDescription endpoint) throws Exception {

    Optional<SecurityPolicy> securityPolicy = getEnhancedUserTokenSecurityPolicy(endpoint);

    if (securityPolicy.isPresent()) {
      return EnhancedUserTokenAdditionalHeader.createRequest(context, securityPolicy.get());
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
        warnIfPasswordTravelsUnencrypted(endpoint, tokenPolicy);

        buffer.writeBytes(passwordBytes);
      } else if (securityPolicy.getProfile().usesEnhancedUserTokenSecret()) {

        NonceUtil.validateNonce(serverNonce);

        ByteString encryptedSecret =
            encryptEnhancedPassword(
                context,
                securityPolicy,
                serverNonce,
                ByteString.of(Arrays.copyOf(passwordBytes, passwordBytes.length)));

        // UA Part 4, Table 188: for SecureChannelEnhancement policies the EncryptedSecret format
        // already identifies the encryption used, so encryptionAlgorithm must be null/empty.
        UserNameIdentityToken token =
            new UserNameIdentityToken(tokenPolicy.getPolicyId(), username, encryptedSecret, null);

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
          resolveCertificateValidator(context)
              .validateCertificateChain(
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

  /**
   * Return the validator for the server certificate the password is encrypted to: this provider's
   * own when it has one, otherwise the client's, otherwise one that accepts every certificate.
   *
   * @param context the identity-provider context for the session activation.
   * @return the validator to check the server certificate with.
   */
  private CertificateValidator resolveCertificateValidator(IdentityProviderContext context) {
    if (certificateValidator != null) {
      return certificateValidator;
    } else {
      return context
          .getCertificateValidator()
          .orElseGet(CertificateValidator.InsecureCertificateValidator::new);
    }
  }

  /**
   * Log a WARN, once per provider, when a password the token does not encrypt is about to cross a
   * {@code None} or {@code Sign} SecureChannel, where anyone on the path can read it (Part 4
   * (7.40.4)).
   *
   * <p>Transports other than {@code opc.tcp} run over TLS, which encrypts the password in transit.
   *
   * @param endpoint the endpoint being activated.
   * @param tokenPolicy the selected username token policy, whose security policy is {@code None}.
   */
  private void warnIfPasswordTravelsUnencrypted(
      EndpointDescription endpoint, UserTokenPolicy tokenPolicy) {

    boolean channelEncrypts =
        endpoint.getSecurityMode() == MessageSecurityMode.SignAndEncrypt
            || !Stack.TCP_UASC_UABINARY_TRANSPORT_URI.equals(endpoint.getTransportProfileUri());

    if (!channelEncrypts && unencryptedPasswordWarningLogged.compareAndSet(false, true)) {
      LOGGER.warn(
          "Sending password unencrypted to {}: UserTokenPolicy '{}' has SecurityPolicy None and"
              + " the SecureChannel's MessageSecurityMode is {}. Logged once per UsernameProvider.",
          endpoint.getEndpointUrl(),
          tokenPolicy.getPolicyId(),
          endpoint.getSecurityMode());
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

    // Prefer username policies the SecureChannel can actually use (Part 4 (7.41)) and skip the
    // rest, so a usable policy is chosen rather than failing on an unusable one the server also
    // advertised. Fall back to the full list when none are usable, so resolveSecurityPolicy still
    // reports the precise rejection reason for the chosen policy.
    List<UserTokenPolicy> usableTokenPolicies =
        tokenPolicies.stream()
            .filter(t -> isUserTokenPolicyUsable(endpoint, t))
            .collect(Collectors.toList());

    return policyChooser.apply(usableTokenPolicies.isEmpty() ? tokenPolicies : usableTokenPolicies);
  }

  /**
   * Return whether {@code tokenPolicy} can protect a username token on {@code endpoint} without
   * violating the Part 4 (7.41) rules enforced by {@link #enforceUserTokenSecurityPolicyRules}.
   *
   * <p>Selection uses this to skip server-advertised username policies that are unusable on the
   * current SecureChannel (for example, an ECC or RSA-DH policy offered on a {@code None} channel,
   * or a policy whose URI this client does not support) so a usable alternative can be chosen
   * instead. It never throws; the throwing enforcement in {@link #resolveSecurityPolicy} remains
   * the authority when no usable policy is available.
   *
   * @param endpoint the endpoint being connected to.
   * @param tokenPolicy a username token policy advertised by {@code endpoint}.
   * @return {@code true} if the policy resolves and satisfies the Part 4 (7.41) rules.
   */
  private static boolean isUserTokenPolicyUsable(
      EndpointDescription endpoint, UserTokenPolicy tokenPolicy) {

    try {
      resolveSecurityPolicy(endpoint, tokenPolicy);
      return true;
    } catch (UaException e) {
      LOGGER.warn(
          "skipping unusable username UserTokenPolicy (policyId={}): {}",
          tokenPolicy.getPolicyId(),
          e.getMessage());
      return false;
    }
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
                    EnhancedUserTokenAdditionalHeader.resolveUserTokenSecurityPolicy(endpoint, t)
                        .map(securityPolicy::equals)
                        .orElse(false))
            .collect(Collectors.toList());

    return tokenPolicies.isEmpty()
        ? Optional.empty()
        : Optional.of(policyChooser.apply(tokenPolicies));
  }

  private static SecurityPolicy resolveSecurityPolicy(
      EndpointDescription endpoint, UserTokenPolicy tokenPolicy) throws UaException {

    String tokenPolicyUri = tokenPolicy.getSecurityPolicyUri();
    boolean explicitlySpecified = tokenPolicyUri != null && !tokenPolicyUri.isEmpty();

    SecurityPolicy securityPolicy;
    try {
      String securityPolicyUri =
          explicitlySpecified ? tokenPolicyUri : endpoint.getSecurityPolicyUri();
      securityPolicy = SecurityPolicy.fromUri(securityPolicyUri);
    } catch (Throwable t) {
      throw new UaException(StatusCodes.Bad_SecurityPolicyRejected, t);
    }

    enforceUserTokenSecurityPolicyRules(endpoint, securityPolicy, explicitlySpecified);

    return securityPolicy;
  }

  /**
   * Enforce the client-side OPC UA Part 4 (7.41) rules for a {@link UserNameIdentityToken}: an
   * enhanced (ECC or RSA-DH) secret may not be used on a None SecureChannel, and an explicitly
   * specified user-token SecurityPolicy must share the SecureChannel's public-key algorithm.
   *
   * <p>Both rules live in {@link UserTokenSecurityPolicyRules}, shared with {@link
   * X509IdentityProvider} (which applies only the public-key-algorithm rule, because its token is
   * signed rather than encrypted). Because every resolution path runs through here, rejecting the
   * None-mode enhanced case also short-circuits {@link
   * #getEnhancedUserTokenSecurityPolicy(EndpointDescription)} and {@link
   * #getCreateSessionAdditionalHeader(EncodingContext, EndpointDescription)} so the client never
   * emits ephemeral-key material on a None channel. The companion RSA trusted-certificate rule from
   * the same clause is enforced separately, in {@link #getIdentityToken(IdentityProviderContext)}.
   *
   * @param endpoint the endpoint whose SecurityMode and SecurityPolicy describe the SecureChannel.
   * @param userTokenSecurityPolicy the resolved user-token security policy.
   * @param explicitlySpecified whether the token policy named its own {@code securityPolicyUri}
   *     rather than inheriting the endpoint's.
   * @throws UaException with {@link StatusCodes#Bad_SecurityPolicyRejected} if either rule is
   *     violated.
   */
  private static void enforceUserTokenSecurityPolicyRules(
      EndpointDescription endpoint,
      SecurityPolicy userTokenSecurityPolicy,
      boolean explicitlySpecified)
      throws UaException {

    UserTokenSecurityPolicyRules.requireSecuredChannelForEnhancedSecret(
        endpoint, userTokenSecurityPolicy);
    UserTokenSecurityPolicyRules.requireSamePublicKeyAlgorithmAsChannel(
        endpoint, userTokenSecurityPolicy, explicitlySpecified);
  }

  private ByteString encryptEnhancedPassword(
      IdentityProviderContext context,
      SecurityPolicy securityPolicy,
      ByteString serverNonce,
      ByteString password)
      throws UaException {

    ByteString receiverPublicKey =
        context
            .getReceiverEphemeralPublicKey()
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
      return CipherFactory.createForEncryption(
          securityPolicy.getAsymmetricEncryptionAlgorithm(), serverCertificate.getPublicKey());
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  @Override
  public String toString() {
    return "UsernameProvider{" + "username='" + username + '\'' + '}';
  }
}
