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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.AdditionalParametersType;
import org.eclipse.milo.opcua.stack.core.types.structured.EphemeralKeyType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class EccUserTokenAdditionalHeaderTest {

  // The client asks for ECC username-token key material by policy URI, not by endpoint order.
  @ParameterizedTest
  @EnumSource(
      value = SecurityPolicy.class,
      names = {
        "ECC_nistP256_AesGcm",
        "ECC_nistP256_ChaChaPoly",
        "ECC_curve25519_AesGcm",
        "ECC_curve25519_ChaChaPoly"
      })
  void encodesAndDecodesCreateSessionRequestPolicy(SecurityPolicy securityPolicy) throws Exception {
    ExtensionObject additionalHeader =
        EccUserTokenAdditionalHeader.createRequest(DefaultEncodingContext.INSTANCE, securityPolicy);

    assertEquals(
        securityPolicy,
        EccUserTokenAdditionalHeader.decodeRequest(
                DefaultEncodingContext.INSTANCE, additionalHeader)
            .orElseThrow());
  }

  // The server's response must carry the signed session key in the generated UA structure.
  @ParameterizedTest
  @EnumSource(
      value = SecurityPolicy.class,
      names = {
        "ECC_nistP256_AesGcm",
        "ECC_nistP256_ChaChaPoly",
        "ECC_curve25519_AesGcm",
        "ECC_curve25519_ChaChaPoly"
      })
  void encodesAndDecodesCreateSessionResponseKey(SecurityPolicy securityPolicy) throws Exception {
    EphemeralKeyType ephemeralKey =
        new EphemeralKeyType(ByteString.of(new byte[] {0x01}), ByteString.of(new byte[] {0x02}));

    ExtensionObject additionalHeader =
        EccUserTokenAdditionalHeader.createResponse(
            DefaultEncodingContext.INSTANCE, securityPolicy, ephemeralKey);

    assertEquals(
        ephemeralKey,
        EccUserTokenAdditionalHeader.decodeResponse(
                DefaultEncodingContext.INSTANCE, additionalHeader, securityPolicy)
            .orElseThrow());
  }

  // Part 6 defines ECDHPolicyUri as the request selector; interoperable servers return ECDHKey
  // without echoing the selected policy URI.
  @Test
  void createSessionResponseContainsOnlyEcdhKeyParameter() throws Exception {
    EphemeralKeyType ephemeralKey =
        new EphemeralKeyType(ByteString.of(new byte[] {0x01}), ByteString.of(new byte[] {0x02}));

    ExtensionObject additionalHeader =
        EccUserTokenAdditionalHeader.createResponse(
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
                EccUserTokenAdditionalHeader.decodeResponse(
                    DefaultEncodingContext.INSTANCE,
                    additionalHeader,
                    SecurityPolicy.ECC_nistP256_AesGcm));

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, exception.getStatusCode().getValue());
  }

  // Anonymous and non-ECC username-token paths do not negotiate ECDH material, so a missing header
  // must be distinguishable from a malformed one.
  @Test
  void nullAdditionalHeaderDecodesAsEmpty() throws Exception {
    assertTrue(
        EccUserTokenAdditionalHeader.decodeRequest(DefaultEncodingContext.INSTANCE, null)
            .isEmpty());
  }

  // Malformed AdditionalParametersType payloads must fail before ActivateSession encrypts or
  // validates a username secret against an ambiguous receiver key.
  @Test
  void malformedAdditionalHeaderFailsWithDecodingError() {
    ExtensionObject additionalHeader =
        ExtensionObject.of(
            ByteString.of(new byte[] {0x01}),
            NodeIds.AdditionalParametersType_Encoding_DefaultBinary);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                EccUserTokenAdditionalHeader.decodeRequest(
                    DefaultEncodingContext.INSTANCE, additionalHeader));

    assertEquals(StatusCodes.Bad_DecodingError, exception.getStatusCode().getValue());
  }
}
