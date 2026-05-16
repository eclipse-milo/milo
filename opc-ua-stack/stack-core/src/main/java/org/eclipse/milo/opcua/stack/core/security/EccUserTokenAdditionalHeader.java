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

/**
 * Creates and decodes ECC user-token additional-header negotiation parameters.
 *
 * <p>When a username token is protected by an ECC user-token security policy, the client asks for
 * the server's session ephemeral public key in the CreateSession request. The server returns that
 * key as a signed {@link EphemeralKeyType} in the CreateSession response. The password itself is
 * not carried here; it remains in the opaque {@link EccEncryptedSecret} payload sent later in
 * ActivateSession.
 *
 * <p>The request identifies the desired ECDH policy by {@code ECDHPolicyUri}. The response carries
 * only {@code ECDHKey}; servers may place a {@link StatusCode} in that parameter when they cannot
 * create the key material.
 */
public final class EccUserTokenAdditionalHeader {

  private EccUserTokenAdditionalHeader() {}

  /**
   * Return whether {@code endpoint} has a username token policy that needs ECC user-token key
   * negotiation.
   *
   * <p>Callers that already know which {@link UserTokenPolicy} was selected should resolve that
   * policy directly instead of using this endpoint-wide probe.
   *
   * @param endpoint the endpoint being connected to.
   * @return {@code true} when at least one username token policy resolves to a supported ECC
   *     policy.
   */
  public static boolean requiresNegotiation(EndpointDescription endpoint) {
    return selectNegotiatedSecurityPolicy(endpoint).isPresent();
  }

  /**
   * Select the first username token security policy on {@code endpoint} that needs ECC user-token
   * key negotiation.
   *
   * <p>This is a convenience for endpoint inspection. Session code that has an application-selected
   * identity provider should negotiate the provider-selected policy, not the first matching
   * endpoint policy.
   *
   * @param endpoint the endpoint being connected to.
   * @return the selected ECC user-token security policy, if present.
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
   * Return whether {@code profile} is supported by the current ECC token-secret helper.
   *
   * @param profile a security policy profile.
   * @return {@code true} for ECC user-token profiles supported by {@link EccEncryptedSecret}.
   */
  public static boolean isSupportedEccProfile(SecurityPolicyProfile profile) {
    requireNonNull(profile, "profile");

    return (profile.keyAgreementAxis() == KeyAgreementAxis.ECDH_NIST_P256
            || profile.keyAgreementAxis() == KeyAgreementAxis.X25519)
        && (profile.chunkProtectionAxis() == ChunkProtectionAxis.AES_GCM
            || profile.chunkProtectionAxis() == ChunkProtectionAxis.CHACHA20_POLY1305);
  }

  /**
   * Create the CreateSession request additional header for an ECC username-token policy.
   *
   * @param context an encoding context.
   * @param securityPolicy the requested user-token security policy.
   * @return an encoded {@link AdditionalParametersType} extension object.
   * @throws UaException if the policy is not supported or encoding fails.
   */
  public static ExtensionObject createRequest(
      EncodingContext context, SecurityPolicy securityPolicy) throws UaException {

    requireEccPolicy(securityPolicy);

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
   * Decode the requested user-token security policy from a CreateSession request additional header.
   *
   * @param context an encoding context.
   * @param additionalHeader the request additional header.
   * @return the requested policy, or empty when no header was supplied.
   * @throws UaException if the header is malformed or requests an unsupported policy.
   */
  public static Optional<SecurityPolicy> decodeRequest(
      EncodingContext context, @Nullable ExtensionObject additionalHeader) throws UaException {

    Optional<AdditionalParametersType> additionalParameters =
        decodeAdditionalParameters(context, additionalHeader);

    if (additionalParameters.isEmpty()) {
      return Optional.empty();
    }

    Optional<Variant> policyUriValue =
        parameterValue(additionalParameters.get(), EccEncryptedSecret.ECDH_POLICY_URI_PARAMETER);

    if (policyUriValue.isEmpty()) {
      return Optional.empty();
    }

    Object value = policyUriValue.get().getValue();
    if (!(value instanceof String securityPolicyUri)) {
      throw new UaException(
          StatusCodes.Bad_DecodingError,
          EccEncryptedSecret.ECDH_POLICY_URI_PARAMETER + " must be a String");
    }

    SecurityPolicy securityPolicy = SecurityPolicy.fromUri(securityPolicyUri);
    requireEccPolicy(securityPolicy);

    return Optional.of(securityPolicy);
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

    requireEccPolicy(securityPolicy);
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
   * Decode the signed server ephemeral key from a CreateSession response additional header.
   *
   * @param context an encoding context.
   * @param additionalHeader the response additional header.
   * @param expectedSecurityPolicy the user-token policy that made ECC key material expected.
   * @return the signed key, or empty when no header was supplied.
   * @throws UaException if the header is malformed or the server returned an {@code ECDHKey} status
   *     code.
   */
  public static Optional<EphemeralKeyType> decodeResponse(
      EncodingContext context,
      @Nullable ExtensionObject additionalHeader,
      SecurityPolicy expectedSecurityPolicy)
      throws UaException {

    requireEccPolicy(expectedSecurityPolicy);

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

  private static void requireEccPolicy(SecurityPolicy securityPolicy) throws UaException {
    requireNonNull(securityPolicy, "securityPolicy");

    if (!isSupportedEccProfile(securityPolicy.getProfile())) {
      throw new UaException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "security policy does not support ECC user-token negotiation: "
              + securityPolicy.getUri());
    }
  }
}
