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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.AdditionalParametersType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EphemeralKeyType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class EnhancedUserTokenAdditionalHeaderTest {

  // The client asks for enhanced username-token key material by policy URI, not by endpoint order.
  @ParameterizedTest
  @EnumSource(
      value = SecurityPolicy.class,
      names = {
        "ECC_nistP256_AesGcm",
        "ECC_nistP256_ChaChaPoly",
        "ECC_nistP384_AesGcm",
        "ECC_nistP384_ChaChaPoly",
        "ECC_brainpoolP256r1_AesGcm",
        "ECC_brainpoolP256r1_ChaChaPoly",
        "ECC_curve25519_AesGcm",
        "ECC_curve25519_ChaChaPoly",
        "ECC_curve448_AesGcm",
        "ECC_curve448_ChaChaPoly",
        "ECC_brainpoolP384r1_AesGcm",
        "ECC_brainpoolP384r1_ChaChaPoly",
        "RSA_DH_AesGcm",
        "RSA_DH_ChaChaPoly"
      })
  void encodesAndDecodesCreateSessionRequestPolicy(SecurityPolicy securityPolicy) throws Exception {
    ExtensionObject additionalHeader =
        EnhancedUserTokenAdditionalHeader.createRequest(
            DefaultEncodingContext.INSTANCE, securityPolicy);

    assertEquals(
        new EnhancedUserTokenAdditionalHeader.NegotiationRequest.Supported(securityPolicy),
        EnhancedUserTokenAdditionalHeader.decodeRequest(
            DefaultEncodingContext.INSTANCE, additionalHeader));
  }

  // The server's response must carry the signed session key in the generated UA structure.
  @ParameterizedTest
  @EnumSource(
      value = SecurityPolicy.class,
      names = {
        "ECC_nistP256_AesGcm",
        "ECC_nistP256_ChaChaPoly",
        "ECC_nistP384_AesGcm",
        "ECC_nistP384_ChaChaPoly",
        "ECC_brainpoolP256r1_AesGcm",
        "ECC_brainpoolP256r1_ChaChaPoly",
        "ECC_curve25519_AesGcm",
        "ECC_curve25519_ChaChaPoly",
        "ECC_curve448_AesGcm",
        "ECC_curve448_ChaChaPoly",
        "ECC_brainpoolP384r1_AesGcm",
        "ECC_brainpoolP384r1_ChaChaPoly",
        "RSA_DH_AesGcm",
        "RSA_DH_ChaChaPoly"
      })
  void encodesAndDecodesCreateSessionResponseKey(SecurityPolicy securityPolicy) throws Exception {
    EphemeralKeyType ephemeralKey =
        new EphemeralKeyType(ByteString.of(new byte[] {0x01}), ByteString.of(new byte[] {0x02}));

    ExtensionObject additionalHeader =
        EnhancedUserTokenAdditionalHeader.createResponse(
            DefaultEncodingContext.INSTANCE, securityPolicy, ephemeralKey);

    assertEquals(
        ephemeralKey,
        EnhancedUserTokenAdditionalHeader.decodeResponse(
                DefaultEncodingContext.INSTANCE, additionalHeader, securityPolicy)
            .orElseThrow());
  }

  // Part 6 defines ECDHPolicyUri as the request selector; interoperable servers return ECDHKey
  // without echoing the selected policy URI. RSA-DH keeps these field names on the wire.
  @Test
  void createSessionResponseContainsOnlyEcdhKeyParameter() throws Exception {
    EphemeralKeyType ephemeralKey =
        new EphemeralKeyType(ByteString.of(new byte[] {0x01}), ByteString.of(new byte[] {0x02}));

    ExtensionObject additionalHeader =
        EnhancedUserTokenAdditionalHeader.createResponse(
            DefaultEncodingContext.INSTANCE, SecurityPolicy.ECC_nistP256_AesGcm, ephemeralKey);

    AdditionalParametersType parameters =
        (AdditionalParametersType) additionalHeader.decode(DefaultEncodingContext.INSTANCE);
    KeyValuePair[] keyValuePairs = requireNonNull(parameters.getParameters());

    assertEquals(1, keyValuePairs.length);
    assertEquals(
        new QualifiedName(0, EccEncryptedSecret.ECDH_KEY_PARAMETER), keyValuePairs[0].getKey());
  }

  // The server reports an unsupported ECDH policy by returning a StatusCode under ECDHKey.
  @Test
  void decodeResponsePropagatesEcdhKeyStatusCode() {
    ExtensionObject additionalHeader =
        ExtensionObject.encode(
            DefaultEncodingContext.INSTANCE,
            new AdditionalParametersType(
                new KeyValuePair[] {
                  new KeyValuePair(
                      new QualifiedName(0, EccEncryptedSecret.ECDH_KEY_PARAMETER),
                      Variant.ofStatusCode(new StatusCode(StatusCodes.Bad_SecurityPolicyRejected)))
                }));

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                EnhancedUserTokenAdditionalHeader.decodeResponse(
                    DefaultEncodingContext.INSTANCE,
                    additionalHeader,
                    SecurityPolicy.ECC_nistP256_AesGcm));

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, exception.getStatusCode().getValue());
  }

  // The server reports an unavailable user-token policy by succeeding and putting a StatusCode
  // under
  // ECDHKey; the round trip must match what the client's response decoder already accepts.
  @Test
  void createResponseStatusCodeRoundTripsThroughDecodeResponse() throws Exception {
    ExtensionObject additionalHeader =
        EnhancedUserTokenAdditionalHeader.createResponse(
            DefaultEncodingContext.INSTANCE,
            new StatusCode(StatusCodes.Bad_SecurityPolicyRejected));

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                EnhancedUserTokenAdditionalHeader.decodeResponse(
                    DefaultEncodingContext.INSTANCE,
                    additionalHeader,
                    SecurityPolicy.ECC_nistP256_AesGcm));

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, exception.getStatusCode().getValue());
  }

  // Anonymous and unenhanced username-token paths do not negotiate ephemeral key material, so a
  // missing header must be reported as "no negotiation requested" rather than failing.
  @Test
  void nullAdditionalHeaderDecodesAsNone() throws Exception {
    assertInstanceOf(
        EnhancedUserTokenAdditionalHeader.NegotiationRequest.None.class,
        EnhancedUserTokenAdditionalHeader.decodeRequest(DefaultEncodingContext.INSTANCE, null));
  }

  // Part 4 requires servers to IGNORE additional headers they do not recognize. A vendor or future
  // header carries an unknown type id that does not decode; failing it would regress even pure
  // legacy RSA clients that attach such a header to CreateSession.
  @Test
  void unknownTypeIdAdditionalHeaderDecodesAsNone() throws Exception {
    ExtensionObject additionalHeader =
        ExtensionObject.of(ByteString.of(new byte[] {0x01}), new NodeId(0, 999_999));

    assertInstanceOf(
        EnhancedUserTokenAdditionalHeader.NegotiationRequest.None.class,
        EnhancedUserTokenAdditionalHeader.decodeRequest(
            DefaultEncodingContext.INSTANCE, additionalHeader));
  }

  // The server must not issue enhanced username receiver keys for an endpoint whose SecureChannel
  // cannot carry that token policy; otherwise ActivateSession can reach an incompatible policy.
  @Test
  void enhancedNegotiationSkipsUsernamePolicyOnNoneChannel() {
    EndpointDescription endpoint =
        endpoint(
            SecurityPolicy.None,
            MessageSecurityMode.None,
            usernamePolicy(SecurityPolicy.ECC_nistP256_AesGcm));

    assertEquals(
        Optional.empty(),
        EnhancedUserTokenAdditionalHeader.selectNegotiatedSecurityPolicy(endpoint));
    assertFalse(
        EnhancedUserTokenAdditionalHeader.hasUsernameTokenSecurityPolicy(
            endpoint, SecurityPolicy.ECC_nistP256_AesGcm));
  }

  // The same compatibility check applies before issuing a receiver key for an explicit policy whose
  // public-key family differs from the SecureChannel.
  @Test
  void enhancedNegotiationSkipsCrossFamilyUsernamePolicy() {
    EndpointDescription endpoint =
        endpoint(
            SecurityPolicy.Basic256Sha256,
            MessageSecurityMode.SignAndEncrypt,
            usernamePolicy(SecurityPolicy.ECC_nistP256_AesGcm));

    assertEquals(
        Optional.empty(),
        EnhancedUserTokenAdditionalHeader.selectNegotiatedSecurityPolicy(endpoint));
    assertFalse(
        EnhancedUserTokenAdditionalHeader.hasUsernameTokenSecurityPolicy(
            endpoint, SecurityPolicy.ECC_nistP256_AesGcm));
  }

  // A header that decodes to some other structure is still not a negotiation request and must be
  // ignored rather than faulted.
  @Test
  void nonAdditionalParametersTypeAdditionalHeaderDecodesAsNone() throws Exception {
    ExtensionObject additionalHeader =
        ExtensionObject.encode(
            DefaultEncodingContext.INSTANCE,
            new EphemeralKeyType(
                ByteString.of(new byte[] {0x01}), ByteString.of(new byte[] {0x02})));

    assertInstanceOf(
        EnhancedUserTokenAdditionalHeader.NegotiationRequest.None.class,
        EnhancedUserTokenAdditionalHeader.decodeRequest(
            DefaultEncodingContext.INSTANCE, additionalHeader));
  }

  // Part 6, 6.8.2 (Table 70): an unknown/non-enhanced ECDHPolicyUri is not a hard fault. The
  // service succeeds and the caller reports Bad_SecurityPolicyRejected in-parameter, so foreign
  // clients using the deprecated suffix-less 1.05 ECC URIs can fall back gracefully.
  @Test
  void unknownPolicyUriDecodesAsUnsupported() throws Exception {
    String suffixlessEccUri = "http://opcfoundation.org/UA/SecurityPolicy#ECC_nistP256";

    ExtensionObject additionalHeader =
        ExtensionObject.encode(
            DefaultEncodingContext.INSTANCE,
            new AdditionalParametersType(
                new KeyValuePair[] {
                  new KeyValuePair(
                      new QualifiedName(0, EccEncryptedSecret.ECDH_POLICY_URI_PARAMETER),
                      Variant.ofString(suffixlessEccUri))
                }));

    assertEquals(
        new EnhancedUserTokenAdditionalHeader.NegotiationRequest.Unsupported(suffixlessEccUri),
        EnhancedUserTokenAdditionalHeader.decodeRequest(
            DefaultEncodingContext.INSTANCE, additionalHeader));
  }

  // A recognized AdditionalParametersType with a structurally invalid ECDHPolicyUri (not a String)
  // is the one case that must still hard-fault, before ActivateSession encrypts or validates a
  // username secret against an ambiguous receiver key.
  @Test
  void malformedPolicyUriParameterFailsWithDecodingError() {
    ExtensionObject additionalHeader =
        ExtensionObject.encode(
            DefaultEncodingContext.INSTANCE,
            new AdditionalParametersType(
                new KeyValuePair[] {
                  new KeyValuePair(
                      new QualifiedName(0, EccEncryptedSecret.ECDH_POLICY_URI_PARAMETER),
                      Variant.ofInt32(42))
                }));

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                EnhancedUserTokenAdditionalHeader.decodeRequest(
                    DefaultEncodingContext.INSTANCE, additionalHeader));

    assertEquals(StatusCodes.Bad_DecodingError, exception.getStatusCode().getValue());
  }

  private static EndpointDescription endpoint(
      SecurityPolicy channelPolicy, MessageSecurityMode securityMode, UserTokenPolicy tokenPolicy) {

    ApplicationDescription server =
        new ApplicationDescription(
            "urn:eclipse:milo:test:server",
            "urn:eclipse:milo:test",
            null,
            ApplicationType.Server,
            null,
            null,
            null);

    return new EndpointDescription(
        "opc.tcp://localhost:12686/milo",
        server,
        ByteString.NULL_VALUE,
        securityMode,
        channelPolicy.getUri(),
        new UserTokenPolicy[] {tokenPolicy},
        "http://opcfoundation.org/UA-Profile/Transport/uatcp-uasc-uabinary",
        ubyte(0));
  }

  private static UserTokenPolicy usernamePolicy(SecurityPolicy securityPolicy) {
    return new UserTokenPolicy(
        "username", UserTokenType.UserName, null, null, securityPolicy.getUri());
  }
}
