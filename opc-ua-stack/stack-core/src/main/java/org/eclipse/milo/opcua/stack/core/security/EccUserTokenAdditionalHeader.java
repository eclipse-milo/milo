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

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import java.util.Optional;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.AdditionalParametersType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EphemeralKeyType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates and decodes enhanced user-token additional-header negotiation parameters.
 *
 * <p>When a username token is protected by an ECC or RSA-DH user-token security policy, the client
 * asks for the server's session ephemeral public key in the CreateSession request. The server
 * returns that key as a signed {@link EphemeralKeyType} in the CreateSession response. The password
 * itself is not carried here; it remains in the opaque {@link EccEncryptedSecret} payload sent
 * later in ActivateSession.
 *
 * <p>The request identifies the desired user-token policy by {@code ECDHPolicyUri}. The response
 * carries only {@code ECDHKey}; servers may place a {@link StatusCode} in that parameter when they
 * cannot create the key material. These parameter names are OPC UA wire names inherited from the
 * {@code EccEncryptedSecret} definition. RSA-DH profiles still use them because they reuse the same
 * additional-header negotiation wrapper.
 */
public final class EccUserTokenAdditionalHeader {

  private static final Logger LOGGER = LoggerFactory.getLogger(EccUserTokenAdditionalHeader.class);

  private EccUserTokenAdditionalHeader() {}

  /**
   * The result of inspecting a CreateSession/ActivateSession request AdditionalHeader for an
   * enhanced user-token key-negotiation request.
   *
   * <p>Part 4 requires servers to ignore additional headers they do not recognize, and Part 6,
   * 6.8.2 (Table 70) requires EphemeralKey-creation errors to be reported as a {@link StatusCode}
   * in the {@code ECDHKey} response parameter rather than as a service fault. This type lets the
   * server distinguish "no negotiation requested" (ignore) from "an unavailable policy was
   * requested" (succeed and return an in-parameter status code) without throwing.
   */
  public sealed interface NegotiationRequest {

    /** No enhanced user-token negotiation was requested; the header should be ignored. */
    record None() implements NegotiationRequest {}

    /** A supported enhanced user-token policy was requested. */
    record Supported(SecurityPolicy securityPolicy) implements NegotiationRequest {}

    /**
     * A user-token policy was requested by URI, but it is unknown or does not support enhanced
     * negotiation. The server should still succeed and return {@code ECDHKey =
     * StatusCode(Bad_SecurityPolicyRejected)}.
     */
    record Unsupported(String securityPolicyUri) implements NegotiationRequest {}
  }

  /**
   * Return whether {@code endpoint} has a username token policy that needs enhanced user-token key
   * negotiation.
   *
   * <p>Callers that already know which {@link UserTokenPolicy} was selected should resolve that
   * policy directly instead of using this endpoint-wide probe.
   *
   * @param endpoint the endpoint being connected to.
   * @return {@code true} when at least one username token policy resolves to a supported enhanced
   *     policy.
   */
  public static boolean requiresNegotiation(EndpointDescription endpoint) {
    return selectNegotiatedSecurityPolicy(endpoint).isPresent();
  }

  /**
   * Select the first username token security policy on {@code endpoint} that needs enhanced
   * user-token key negotiation.
   *
   * <p>This is a convenience for endpoint inspection. Session code that has an application-selected
   * identity provider should negotiate the provider-selected policy, not the first matching
   * endpoint policy.
   *
   * @param endpoint the endpoint being connected to.
   * @return the selected enhanced user-token security policy, if present.
   */
  public static Optional<SecurityPolicy> selectNegotiatedSecurityPolicy(
      EndpointDescription endpoint) {
    requireNonNull(endpoint, "endpoint");

    UserTokenPolicy[] tokenPolicies =
        requireNonNullElse(endpoint.getUserIdentityTokens(), new UserTokenPolicy[0]);

    return Stream.of(tokenPolicies)
        .filter(t -> t.getTokenType() == UserTokenType.UserName)
        .map(t -> resolveUserTokenSecurityPolicy(endpoint, t))
        .flatMap(Optional::stream)
        .filter(p -> isSupportedEccProfile(p.getProfile()))
        .findFirst();
  }

  /**
   * Return whether {@code endpoint} advertises a username token policy protected by {@code
   * securityPolicy}.
   *
   * @param endpoint the endpoint being connected to.
   * @param securityPolicy the resolved user-token security policy.
   * @return {@code true} when an exact username-token policy match is advertised.
   */
  public static boolean hasUsernameTokenSecurityPolicy(
      EndpointDescription endpoint, SecurityPolicy securityPolicy) {

    requireNonNull(endpoint, "endpoint");
    requireNonNull(securityPolicy, "securityPolicy");

    UserTokenPolicy[] tokenPolicies =
        requireNonNullElse(endpoint.getUserIdentityTokens(), new UserTokenPolicy[0]);

    return Stream.of(tokenPolicies)
        .filter(t -> t.getTokenType() == UserTokenType.UserName)
        .map(t -> resolveUserTokenSecurityPolicy(endpoint, t))
        .flatMap(Optional::stream)
        .anyMatch(securityPolicy::equals);
  }

  /**
   * Resolve the security policy that protects {@code tokenPolicy}.
   *
   * @param endpoint the endpoint that owns the token policy.
   * @param tokenPolicy the token policy.
   * @return the resolved policy, or empty if no usable policy URI is present.
   */
  public static Optional<SecurityPolicy> resolveUserTokenSecurityPolicy(
      EndpointDescription endpoint, UserTokenPolicy tokenPolicy) {

    requireNonNull(endpoint, "endpoint");
    requireNonNull(tokenPolicy, "tokenPolicy");

    String securityPolicyUri = tokenPolicy.getSecurityPolicyUri();
    if (securityPolicyUri == null || securityPolicyUri.isEmpty()) {
      securityPolicyUri = endpoint.getSecurityPolicyUri();
    }

    if (securityPolicyUri == null || securityPolicyUri.isEmpty()) {
      return Optional.empty();
    }

    return SecurityPolicy.fromUriSafe(securityPolicyUri);
  }

  /**
   * Return whether {@code profile} is supported by the current enhanced token-secret helper.
   *
   * <p>The method name follows the {@code EccEncryptedSecret} terminology used by OPC UA. It
   * returns {@code true} for both ECC profiles and RSA-DH profiles that use the same
   * additional-header negotiation and token-secret payload structure.
   *
   * @param profile a security policy profile.
   * @return {@code true} for user-token profiles supported by {@link EccEncryptedSecret}.
   */
  public static boolean isSupportedEccProfile(SecurityPolicyProfile profile) {
    requireNonNull(profile, "profile");

    return (profile.keyAgreementAxis() == KeyAgreementAxis.ECDH_NIST_P256
            || profile.keyAgreementAxis() == KeyAgreementAxis.ECDH_NIST_P384
            || profile.keyAgreementAxis() == KeyAgreementAxis.ECDH_BRAINPOOL_P256R1
            || profile.keyAgreementAxis() == KeyAgreementAxis.ECDH_BRAINPOOL_P384R1
            || profile.keyAgreementAxis() == KeyAgreementAxis.X25519
            || profile.keyAgreementAxis() == KeyAgreementAxis.X448
            || profile.keyAgreementAxis() == KeyAgreementAxis.FFDH_3072)
        && (profile.chunkProtectionAxis() == ChunkProtectionAxis.AES_GCM
            || profile.chunkProtectionAxis() == ChunkProtectionAxis.CHACHA20_POLY1305);
  }

  /**
   * Create the CreateSession request additional header for an enhanced username-token policy.
   *
   * @param context an encoding context.
   * @param securityPolicy the requested user-token security policy.
   * @return an encoded {@link AdditionalParametersType} extension object.
   * @throws UaException if the policy is not supported or encoding fails.
   */
  public static ExtensionObject createRequest(
      EncodingContext context, SecurityPolicy securityPolicy) throws UaException {

    requireEnhancedPolicy(securityPolicy);

    return encode(
        context,
        new AdditionalParametersType(
            new KeyValuePair[] {
              parameter(
                  EccEncryptedSecret.ECDH_POLICY_URI_PARAMETER,
                  Variant.ofString(securityPolicy.getUri()))
            }));
  }

  /**
   * Decode the requested user-token key negotiation from a CreateSession/ActivateSession request
   * additional header.
   *
   * <p>Per Part 4, an unrecognized additional header (one that is absent, undecodable, or not an
   * {@link AdditionalParametersType}) is ignored and reported as {@link NegotiationRequest.None}; a
   * server must not fail the request because a client attached a vendor or future header. A
   * recognized {@code AdditionalParametersType} that names an unknown or non-enhanced policy in
   * {@code ECDHPolicyUri} is reported as {@link NegotiationRequest.Unsupported} so the caller can
   * succeed and return an in-parameter {@code ECDHKey} status code (Part 6, 6.8.2). Only a
   * structurally malformed {@code AdditionalParametersType} payload throws.
   *
   * @param context an encoding context.
   * @param additionalHeader the request additional header.
   * @return the parsed negotiation request; never {@code null}.
   * @throws UaException if the header decodes to an {@link AdditionalParametersType} whose payload
   *     is malformed (for example, {@code ECDHPolicyUri} is not a String).
   */
  public static NegotiationRequest decodeRequest(
      EncodingContext context, @Nullable ExtensionObject additionalHeader) throws UaException {

    Optional<AdditionalParametersType> additionalParameters =
        decodeRequestAdditionalParameters(context, additionalHeader);

    if (additionalParameters.isEmpty()) {
      return new NegotiationRequest.None();
    }

    Optional<Variant> policyUriValue =
        parameterValue(additionalParameters.get(), EccEncryptedSecret.ECDH_POLICY_URI_PARAMETER);

    if (policyUriValue.isEmpty()) {
      return new NegotiationRequest.None();
    }

    Object value = policyUriValue.get().getValue();
    if (!(value instanceof String securityPolicyUri)) {
      throw new UaException(
          StatusCodes.Bad_DecodingError,
          EccEncryptedSecret.ECDH_POLICY_URI_PARAMETER + " must be a String");
    }

    Optional<SecurityPolicy> securityPolicy = SecurityPolicy.fromUriSafe(securityPolicyUri);
    if (securityPolicy.isEmpty() || !isSupportedEccProfile(securityPolicy.get().getProfile())) {
      return new NegotiationRequest.Unsupported(securityPolicyUri);
    }

    return new NegotiationRequest.Supported(securityPolicy.get());
  }

  /**
   * Create the CreateSession response additional header containing the signed server ephemeral key.
   *
   * <p>The response contains one {@code ECDHKey} parameter and does not echo the request's {@code
   * ECDHPolicyUri}. Clients already know which user-token policy made this response expected.
   *
   * @param context an encoding context.
   * @param securityPolicy the negotiated user-token security policy.
   * @param ephemeralKey the signed server ephemeral key.
   * @return an encoded {@link AdditionalParametersType} extension object.
   * @throws UaException if the policy is not supported or encoding fails.
   */
  public static ExtensionObject createResponse(
      EncodingContext context, SecurityPolicy securityPolicy, EphemeralKeyType ephemeralKey)
      throws UaException {

    requireEnhancedPolicy(securityPolicy);
    requireNonNull(ephemeralKey, "ephemeralKey");

    ExtensionObject ephemeralKeyXo = encode(context, ephemeralKey);

    return encode(
        context,
        new AdditionalParametersType(
            new KeyValuePair[] {
              parameter(
                  EccEncryptedSecret.ECDH_KEY_PARAMETER, Variant.ofExtensionObject(ephemeralKeyXo))
            }));
  }

  /**
   * Create a CreateSession/ActivateSession response additional header that reports an EphemeralKey
   * creation failure as an {@code ECDHKey} status code.
   *
   * <p>Used when a client requested an unknown or non-enhanced user-token policy: the service still
   * succeeds and carries the rejection in-parameter, as required by Part 6, 6.8.2 (Table 70).
   *
   * @param context an encoding context.
   * @param statusCode the status code to report under {@code ECDHKey} (for example, {@code
   *     Bad_SecurityPolicyRejected}).
   * @return an encoded {@link AdditionalParametersType} extension object.
   * @throws UaException if encoding fails.
   */
  public static ExtensionObject createResponse(EncodingContext context, StatusCode statusCode)
      throws UaException {

    requireNonNull(statusCode, "statusCode");

    return encode(
        context,
        new AdditionalParametersType(
            new KeyValuePair[] {
              parameter(EccEncryptedSecret.ECDH_KEY_PARAMETER, Variant.ofStatusCode(statusCode))
            }));
  }

  /**
   * Decode the signed server ephemeral key from a CreateSession response additional header.
   *
   * @param context an encoding context.
   * @param additionalHeader the response additional header.
   * @param expectedSecurityPolicy the user-token policy that made enhanced key material expected.
   * @return the signed key, or empty when no header was supplied.
   * @throws UaException if the header is malformed or the server returned an {@code ECDHKey} status
   *     code.
   */
  public static Optional<EphemeralKeyType> decodeResponse(
      EncodingContext context,
      @Nullable ExtensionObject additionalHeader,
      SecurityPolicy expectedSecurityPolicy)
      throws UaException {

    requireEnhancedPolicy(expectedSecurityPolicy);

    Optional<AdditionalParametersType> additionalParameters =
        decodeAdditionalParameters(context, additionalHeader);

    if (additionalParameters.isEmpty()) {
      return Optional.empty();
    }

    Optional<Variant> keyValue =
        parameterValue(additionalParameters.get(), EccEncryptedSecret.ECDH_KEY_PARAMETER);

    if (keyValue.isEmpty()) {
      throw new UaException(
          StatusCodes.Bad_DecodingError, EccEncryptedSecret.ECDH_KEY_PARAMETER + " missing");
    }

    Object value = keyValue.get().getValue();
    if (value instanceof StatusCode statusCode) {
      throw new UaException(statusCode.getValue());
    }

    if (value instanceof EphemeralKeyType ephemeralKey) {
      return Optional.of(ephemeralKey);
    }

    if (value instanceof ExtensionObject xo) {
      try {
        UaStructuredType decoded = xo.decode(context);
        if (decoded instanceof EphemeralKeyType ephemeralKey) {
          return Optional.of(ephemeralKey);
        }
      } catch (UaSerializationException e) {
        throw new UaException(StatusCodes.Bad_DecodingError, e);
      }
    }

    throw new UaException(
        StatusCodes.Bad_DecodingError,
        EccEncryptedSecret.ECDH_KEY_PARAMETER + " must be an EphemeralKeyType");
  }

  /**
   * Decode the request additional header tolerantly: an absent, undecodable, or non-{@link
   * AdditionalParametersType} header is treated as "no negotiation requested" and ignored, so a
   * server never fails a request because of a header it does not recognize.
   */
  private static Optional<AdditionalParametersType> decodeRequestAdditionalParameters(
      EncodingContext context, @Nullable ExtensionObject additionalHeader) {

    requireNonNull(context, "context");

    if (additionalHeader == null || additionalHeader.isNull()) {
      return Optional.empty();
    }

    try {
      UaStructuredType decoded = additionalHeader.decode(context);

      if (decoded instanceof AdditionalParametersType additionalParameters) {
        return Optional.of(additionalParameters);
      }

      LOGGER.debug(
          "ignoring unrecognized request AdditionalHeader of type {}",
          decoded.getClass().getSimpleName());
    } catch (RuntimeException e) {
      LOGGER.debug("ignoring undecodable request AdditionalHeader", e);
    }

    return Optional.empty();
  }

  private static Optional<AdditionalParametersType> decodeAdditionalParameters(
      EncodingContext context, @Nullable ExtensionObject additionalHeader) throws UaException {

    requireNonNull(context, "context");

    if (additionalHeader == null || additionalHeader.isNull()) {
      return Optional.empty();
    }

    try {
      UaStructuredType decoded = additionalHeader.decode(context);

      if (decoded instanceof AdditionalParametersType additionalParameters) {
        return Optional.of(additionalParameters);
      }
    } catch (RuntimeException e) {
      throw new UaException(StatusCodes.Bad_DecodingError, e);
    }

    throw new UaException(StatusCodes.Bad_DecodingError, "expected AdditionalParametersType");
  }

  private static Optional<Variant> parameterValue(
      AdditionalParametersType additionalParameters, String parameterName) {

    KeyValuePair[] parameters =
        requireNonNullElse(additionalParameters.getParameters(), new KeyValuePair[0]);

    return Stream.of(parameters)
        .filter(p -> p != null && isParameter(p.getKey(), parameterName))
        .map(KeyValuePair::getValue)
        .findFirst();
  }

  private static boolean isParameter(QualifiedName key, String parameterName) {
    return key != null
        && key.getNamespaceIndex().intValue() == 0
        && parameterName.equals(key.getName());
  }

  private static KeyValuePair parameter(String name, Variant value) {
    return new KeyValuePair(new QualifiedName(0, name), value);
  }

  private static ExtensionObject encode(EncodingContext context, UaStructuredType value)
      throws UaException {

    requireNonNull(context, "context");

    try {
      return ExtensionObject.encode(context, value);
    } catch (UaSerializationException e) {
      throw new UaException(StatusCodes.Bad_EncodingError, e);
    }
  }

  private static void requireEnhancedPolicy(SecurityPolicy securityPolicy) throws UaException {
    requireNonNull(securityPolicy, "securityPolicy");

    if (!isSupportedEccProfile(securityPolicy.getProfile())) {
      throw new UaException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "security policy does not support enhanced user-token negotiation: "
              + securityPolicy.getUri());
    }
  }
}
