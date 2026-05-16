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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.structured.EphemeralKeyType;
import org.junit.jupiter.api.Test;

class EccUserTokenAdditionalHeaderTest {

  // The client asks for ECC username-token key material by policy URI, not by endpoint order.
  @Test
  void encodesAndDecodesCreateSessionRequestPolicy() throws Exception {
    ExtensionObject additionalHeader =
        EccUserTokenAdditionalHeader.createRequest(
            DefaultEncodingContext.INSTANCE, SecurityPolicy.ECC_nistP256_AesGcm);

    assertEquals(
        SecurityPolicy.ECC_nistP256_AesGcm,
        EccUserTokenAdditionalHeader.decodeRequest(
                DefaultEncodingContext.INSTANCE, additionalHeader)
            .orElseThrow());
  }

  // The server's response must carry the signed session key in the generated UA structure.
  @Test
  void encodesAndDecodesCreateSessionResponseKey() throws Exception {
    EphemeralKeyType ephemeralKey =
        new EphemeralKeyType(ByteString.of(new byte[] {0x01}), ByteString.of(new byte[] {0x02}));

    ExtensionObject additionalHeader =
        EccUserTokenAdditionalHeader.createResponse(
            DefaultEncodingContext.INSTANCE,
            SecurityPolicy.ECC_curve25519_ChaChaPoly,
            ephemeralKey);

    assertEquals(
        ephemeralKey,
        EccUserTokenAdditionalHeader.decodeResponse(
                DefaultEncodingContext.INSTANCE,
                additionalHeader,
                SecurityPolicy.ECC_curve25519_ChaChaPoly)
            .orElseThrow());
  }

  // A response for a different token policy must not be accepted for the selected policy.
  @Test
  void decodeResponseRejectsWrongPolicy() throws Exception {
    EphemeralKeyType ephemeralKey =
        new EphemeralKeyType(ByteString.of(new byte[] {0x01}), ByteString.of(new byte[] {0x02}));

    ExtensionObject additionalHeader =
        EccUserTokenAdditionalHeader.createResponse(
            DefaultEncodingContext.INSTANCE, SecurityPolicy.ECC_nistP256_AesGcm, ephemeralKey);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                EccUserTokenAdditionalHeader.decodeResponse(
                    DefaultEncodingContext.INSTANCE,
                    additionalHeader,
                    SecurityPolicy.ECC_curve25519_ChaChaPoly));

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, exception.getStatusCode().getValue());
  }

  @Test
  void nullAdditionalHeaderDecodesAsEmpty() throws Exception {
    assertTrue(
        EccUserTokenAdditionalHeader.decodeRequest(DefaultEncodingContext.INSTANCE, null)
            .isEmpty());
  }

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
