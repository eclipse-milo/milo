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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.primitives.Bytes;
import java.security.MessageDigest;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;

class ChannelBoundSignatureDataTest {

  // SecureChannelEnhancements bind CreateSession server signatures to the first OpenSecureChannel
  // response signature and channel certificates; the legacy nonce-only layout is not interoperable.
  @Test
  void enhancedServerSignatureDataIncludesChannelThumbprintAndCertificateHashes() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.ECC_nistP256_AesGcm.getProfile();
    ByteString channelThumbprint = bytes(0x01, 0x02);
    ByteString clientNonce = bytes(0x03);
    ByteString serverChannelCertificate = bytes(0x04, 0x05);
    ByteString clientChannelCertificate = bytes(0x06);
    ByteString serverNonce = bytes(0x07);

    byte[] data =
        ChannelBoundSignatureData.serverSignatureData(
            profile,
            channelThumbprint,
            clientNonce,
            serverChannelCertificate,
            clientChannelCertificate,
            serverNonce,
            bytes(0x7f));

    assertArrayEquals(
        Bytes.concat(
            channelThumbprint.bytesOrEmpty(),
            clientNonce.bytesOrEmpty(),
            sha256(serverChannelCertificate),
            sha256(clientChannelCertificate),
            serverNonce.bytesOrEmpty()),
        data);
  }

  // ActivateSession client signatures add the endpoint server certificate hash to the same channel
  // binding, which catches certificate substitution across discovery and session activation.
  @Test
  void enhancedClientSignatureDataIncludesEndpointAndChannelCertificateHashes() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.ECC_nistP256_AesGcm.getProfile();
    ByteString channelThumbprint = bytes(0x01, 0x02);
    ByteString serverNonce = bytes(0x03);
    ByteString serverCertificate = bytes(0x04);
    ByteString serverChannelCertificate = bytes(0x05);
    ByteString clientChannelCertificate = bytes(0x06);
    ByteString clientNonce = bytes(0x07);

    byte[] data =
        ChannelBoundSignatureData.clientSignatureData(
            profile,
            channelThumbprint,
            serverNonce,
            serverCertificate,
            serverChannelCertificate,
            clientChannelCertificate,
            clientNonce);

    assertArrayEquals(
        Bytes.concat(
            channelThumbprint.bytesOrEmpty(),
            serverNonce.bytesOrEmpty(),
            sha256(serverCertificate),
            sha256(serverChannelCertificate),
            sha256(clientChannelCertificate),
            clientNonce.bytesOrEmpty()),
        data);
  }

  // RSA-era policies keep the historical CreateSession/ActivateSession signature layout so ECC
  // conformance work does not silently break legacy username-token deployments.
  @Test
  void legacyProfilesKeepCertificateAndNonceLayouts() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.Basic256Sha256.getProfile();

    assertArrayEquals(
        new byte[] {0x04, 0x05, 0x06},
        ChannelBoundSignatureData.serverSignatureData(
            profile, null, bytes(0x06), bytes(0x7f), bytes(0x7f), bytes(0x7f), bytes(0x04, 0x05)));

    assertArrayEquals(
        new byte[] {0x01, 0x02, 0x03},
        ChannelBoundSignatureData.clientSignatureData(
            profile, null, bytes(0x03), bytes(0x01, 0x02), bytes(0x7f), bytes(0x7f), bytes(0x7f)));
  }

  // Without the channel thumbprint, session signatures are not tied to the OpenSecureChannel issue
  // exchange and can appear valid for the wrong channel.
  @Test
  void enhancedProfilesRequireChannelThumbprint() {
    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                ChannelBoundSignatureData.clientSignatureData(
                    SecurityPolicy.ECC_nistP256_AesGcm.getProfile(),
                    ByteString.NULL_VALUE,
                    bytes(0x01),
                    bytes(0x02),
                    bytes(0x03),
                    bytes(0x04),
                    bytes(0x05)));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, exception.getStatusCode().getValue());
  }

  private static ByteString bytes(int... values) {
    byte[] bytes = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      bytes[i] = (byte) values[i];
    }
    return ByteString.of(bytes);
  }

  private static byte[] sha256(ByteString value) throws Exception {
    return MessageDigest.getInstance("SHA-256").digest(value.bytesOrEmpty());
  }
}
