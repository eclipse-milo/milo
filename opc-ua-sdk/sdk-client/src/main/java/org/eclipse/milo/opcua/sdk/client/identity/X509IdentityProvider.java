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

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.ChannelBoundSignatureData;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.types.structured.X509IdentityToken;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.jspecify.annotations.Nullable;

/**
 * An {@link IdentityProvider} that authenticates with a certificate user-token policy.
 *
 * <p>The provider sends the configured certificate chain as the user identity token and signs the
 * server certificate plus nonce when the selected user-token security policy requires a signature.
 * Certificate-token policies do not use the enhanced username-secret additional-header exchange,
 * even when the certificate-token policy itself uses an ECC or RSA-DH security policy.
 */
public class X509IdentityProvider implements IdentityProvider {

  private final List<X509Certificate> certificateChain =
      Collections.synchronizedList(new ArrayList<>());

  private final Supplier<PrivateKey> privateKeySupplier;

  /**
   * Construct an {@link X509IdentityProvider} with a single certificate.
   *
   * @param certificate the {@link X509Certificate} to authenticate with.
   * @param privateKey the {@link PrivateKey} corresponding to the certificate.
   */
  public X509IdentityProvider(X509Certificate certificate, PrivateKey privateKey) {
    this(certificate, () -> privateKey);
  }

  /**
   * Construct an {@link X509IdentityProvider} with a certificate chain.
   *
   * @param certificateChain the certificate chain to authenticate with.
   * @param privateKey the {@link PrivateKey} corresponding to the certificate.
   */
  public X509IdentityProvider(List<X509Certificate> certificateChain, PrivateKey privateKey) {
    this(certificateChain, () -> privateKey);
  }

  /**
   * Construct an {@link X509IdentityProvider} with a single certificate.
   *
   * @param certificate the {@link X509Certificate} to authenticate with.
   * @param privateKeySupplier a supplier providing the {@link PrivateKey} corresponding to the
   *     certificate.
   */
  public X509IdentityProvider(
      X509Certificate certificate, Supplier<PrivateKey> privateKeySupplier) {
    this.privateKeySupplier = privateKeySupplier;

    certificateChain.add(certificate);
  }

  /**
   * Construct an {@link X509IdentityProvider} with a certificate chain.
   *
   * @param certificateChain the certificate chain to authenticate with.
   * @param privateKeySupplier a supplier providing the {@link PrivateKey} corresponding to the
   *     certificate.
   */
  public X509IdentityProvider(
      List<X509Certificate> certificateChain, Supplier<PrivateKey> privateKeySupplier) {
    this.privateKeySupplier = privateKeySupplier;

    this.certificateChain.addAll(certificateChain);
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

    selectTokenPolicy(endpoint);

    return Optional.empty();
  }

  @Override
  public SignedIdentityToken getIdentityToken(IdentityProviderContext context) throws Exception {
    return buildSignedIdentityToken(
        context.getEndpoint(),
        context.getServerNonce(),
        context.getChannelSignatureInputs().orElse(null));
  }

  @Override
  public SignedIdentityToken getIdentityToken(EndpointDescription endpoint, ByteString serverNonce)
      throws Exception {

    // This legacy entry point has no SecureChannel-bound inputs; enhanced (ECC or RSA-DH)
    // user-token policies require the IdentityProviderContext overload, which the SDK uses.
    return buildSignedIdentityToken(endpoint, serverNonce, null);
  }

  private SignedIdentityToken buildSignedIdentityToken(
      EndpointDescription endpoint,
      ByteString serverNonce,
      @Nullable ChannelSignatureInputs channelSignatureInputs)
      throws Exception {

    UserTokenPolicy tokenPolicy = selectTokenPolicy(endpoint);
    SecurityPolicy securityPolicy = resolveSecurityPolicy(endpoint, tokenPolicy);

    X509IdentityToken token =
        new X509IdentityToken(
            tokenPolicy.getPolicyId(), CertificateUtil.getCertificateChainBytes(certificateChain));

    SignatureData signatureData;

    if (securityPolicy == SecurityPolicy.None) {
      signatureData = new SignatureData(null, null);
    } else {
      NonceUtil.validateNonce(serverNonce);

      byte[] dataToSign =
          userTokenSignatureData(endpoint, securityPolicy, serverNonce, channelSignatureInputs);

      // ECC policies sign with ECDSA/EdDSA, RSA-DH and legacy policies with the policy's algorithm;
      // the wire algorithm URI is populated only for legacy policies (Part 4 §7.36).
      signatureData =
          ChannelBoundSignatureData.sign(securityPolicy, privateKeySupplier.get(), dataToSign);
    }

    return new SignedIdentityToken(token, signatureData);
  }

  /**
   * Build the bytes the user certificate signs for the ActivateSession user-token signature.
   *
   * <p>Legacy policies sign {@code leaf(ServerCertificate) | ServerNonce}. Enhanced policies sign
   * the channel-bound layout (Part 4 §6.1.8 Table 101) and require the SDK-resolved {@code inputs}.
   */
  private static byte[] userTokenSignatureData(
      EndpointDescription endpoint,
      SecurityPolicy securityPolicy,
      ByteString serverNonce,
      @Nullable ChannelSignatureInputs inputs)
      throws Exception {

    SecurityPolicyProfile profile = securityPolicy.getProfile();

    if (!profile.secureChannelEnhancements()) {
      List<X509Certificate> serverCertificates =
          CertificateUtil.decodeCertificates(endpoint.getServerCertificate().bytesOrEmpty());
      ByteString serverCertificate = ByteString.of(serverCertificates.get(0).getEncoded());

      return ChannelBoundSignatureData.legacyUserTokenSignatureData(serverCertificate, serverNonce);
    }

    if (inputs == null) {
      throw new UaException(
          StatusCodes.Bad_SecurityChecksFailed,
          "channel-bound user-token signature requires SecureChannel inputs; "
              + "use getIdentityToken(IdentityProviderContext)");
    }

    // An enhanced user-token policy binds to the SecureChannel (full layout) or, on an unsecured
    // channel, uses the reduced SecurityMode-None layout; a legacy secured channel supports neither
    // and is rejected cleanly (Part 4 §6.1.8 Table 101).
    SecurityPolicy channelPolicy = SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri());
    ChannelBoundSignatureData.checkUserTokenChannelCompatibility(profile, channelPolicy);

    // Select the layout from whether the channel actually binds (a thumbprint exists), keeping the
    // client and server discriminators on the same physical signal.
    boolean secureChannelSecured = !inputs.channelThumbprint().isNullOrEmpty();
    // Milo uses one application certificate for both the CreateSession ClientCertificate and the
    // channel ClientChannelCertificate, so the same bytes fill both Table 101 slots. The server
    // collapses them identically (SecurityConfiguration.getClientChannelCertificateBytes()).
    ByteString clientCertificate = inputs.clientCertificate();

    return ChannelBoundSignatureData.userTokenSignatureData(
        profile,
        secureChannelSecured,
        inputs.channelThumbprint(),
        serverNonce,
        inputs.serverCertificate(),
        inputs.serverChannelCertificate(),
        clientCertificate,
        clientCertificate,
        inputs.clientNonce());
  }

  private static UserTokenPolicy selectTokenPolicy(EndpointDescription endpoint) throws Exception {
    UserTokenPolicy[] userIdentityTokens =
        requireNonNullElse(endpoint.getUserIdentityTokens(), new UserTokenPolicy[0]);

    return Stream.of(userIdentityTokens)
        .filter(t -> t.getTokenType() == UserTokenType.Certificate)
        .findFirst()
        .orElseThrow(() -> new Exception("no x509 certificate token policy found"));
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
}
