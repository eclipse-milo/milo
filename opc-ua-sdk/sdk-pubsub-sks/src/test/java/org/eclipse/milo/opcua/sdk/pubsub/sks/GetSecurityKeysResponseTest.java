/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.sks;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GetSecurityKeysResponseTest {

  private static final String POLICY_URI =
      "http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes256-CTR";

  private static final ByteString KEY_DATA = ByteString.of(new byte[68]);

  @Test
  void parsesGoodResponse() throws UaException {
    SecurityKeySet keySet =
        GetSecurityKeysResponse.parse(
            result(
                Variant.ofString(POLICY_URI),
                Variant.ofUInt32(uint(7)),
                Variant.ofByteStringArray(new ByteString[] {KEY_DATA, KEY_DATA}),
                Variant.ofDouble(1500.5),
                Variant.ofDouble(3_600_000.0)));

    assertEquals(POLICY_URI, keySet.securityPolicyUri());
    assertEquals(uint(7), keySet.firstTokenId());
    assertEquals(List.of(KEY_DATA, KEY_DATA), keySet.keys());
    // Fractional milliseconds preserved via nanos.
    assertEquals(Duration.ofNanos(1_500_500_000L), keySet.timeToNextKey());
    assertEquals(Duration.ofHours(1), keySet.keyLifetime());
  }

  @Test
  void policyUriIsPassedThroughVerbatim() throws UaException {
    // The policy check belongs to the consuming key manager, not this transport.
    SecurityKeySet keySet =
        GetSecurityKeysResponse.parse(
            result(
                Variant.ofString("http://example.com/some-unknown-policy"),
                Variant.ofUInt32(uint(1)),
                Variant.ofByteStringArray(new ByteString[] {KEY_DATA}),
                Variant.ofDouble(1.0),
                Variant.ofDouble(1.0)));

    assertEquals("http://example.com/some-unknown-policy", keySet.securityPolicyUri());
  }

  @Test
  void extraOutputsAreTolerated() throws UaException {
    SecurityKeySet keySet =
        GetSecurityKeysResponse.parse(
            result(
                Variant.ofString(POLICY_URI),
                Variant.ofUInt32(uint(1)),
                Variant.ofByteStringArray(new ByteString[] {KEY_DATA}),
                Variant.ofDouble(1000.0),
                Variant.ofDouble(2000.0),
                Variant.ofString("vendor extension")));

    assertEquals(uint(1), keySet.firstTokenId());
  }

  @Test
  void nullOutputsAreMalformed() {
    assertMalformed(new CallMethodResult(StatusCode.GOOD, null, null, null));
  }

  @Test
  void shortOutputArrayIsMalformed() {
    assertMalformed(
        result(
            Variant.ofString(POLICY_URI),
            Variant.ofUInt32(uint(1)),
            Variant.ofByteStringArray(new ByteString[] {KEY_DATA}),
            Variant.ofDouble(1000.0)));
  }

  @Test
  void wrongOutputTypesAreMalformed() {
    Variant[] good = {
      Variant.ofString(POLICY_URI),
      Variant.ofUInt32(uint(1)),
      Variant.ofByteStringArray(new ByteString[] {KEY_DATA}),
      Variant.ofDouble(1000.0),
      Variant.ofDouble(2000.0)
    };
    Variant[] wrong = {
      Variant.ofUInt32(uint(42)),
      Variant.ofString("not a token id"),
      Variant.ofByteString(KEY_DATA), // scalar, not array
      Variant.ofString("not a duration"),
      Variant.ofUInt32(uint(2000))
    };
    String[] names = {"SecurityPolicyUri", "FirstTokenId", "Keys", "TimeToNextKey", "KeyLifetime"};

    for (int i = 0; i < good.length; i++) {
      Variant[] outputs = good.clone();
      outputs[i] = wrong[i];

      UaException e = assertMalformed(result(outputs));
      assertTrue(e.getMessage().contains(names[i]), e.getMessage());
    }
  }

  @ParameterizedTest
  @ValueSource(doubles = {Double.NaN, 0.0, -1.0, Double.POSITIVE_INFINITY})
  void invalidTimeToNextKeyIsMalformed(double millis) {
    UaException e =
        assertMalformed(
            result(
                Variant.ofString(POLICY_URI),
                Variant.ofUInt32(uint(1)),
                Variant.ofByteStringArray(new ByteString[] {KEY_DATA}),
                Variant.ofDouble(millis),
                Variant.ofDouble(2000.0)));

    assertTrue(e.getMessage().contains("TimeToNextKey"), e.getMessage());
  }

  @ParameterizedTest
  @ValueSource(doubles = {Double.NaN, 0.0, -1.0, Double.POSITIVE_INFINITY})
  void invalidKeyLifetimeIsMalformed(double millis) {
    UaException e =
        assertMalformed(
            result(
                Variant.ofString(POLICY_URI),
                Variant.ofUInt32(uint(1)),
                Variant.ofByteStringArray(new ByteString[] {KEY_DATA}),
                Variant.ofDouble(1000.0),
                Variant.ofDouble(millis)));

    assertTrue(e.getMessage().contains("KeyLifetime"), e.getMessage());
  }

  @Test
  void emptyKeysAreMalformed() {
    assertMalformed(
        result(
            Variant.ofString(POLICY_URI),
            Variant.ofUInt32(uint(1)),
            Variant.ofByteStringArray(new ByteString[0]),
            Variant.ofDouble(1000.0),
            Variant.ofDouble(2000.0)));
  }

  @Test
  void nullKeyElementIsMalformed() {
    UaException e =
        assertMalformed(
            result(
                Variant.ofString(POLICY_URI),
                Variant.ofUInt32(uint(1)),
                Variant.ofByteStringArray(new ByteString[] {KEY_DATA, null}),
                Variant.ofDouble(1000.0),
                Variant.ofDouble(2000.0)));

    assertTrue(e.getMessage().contains("Keys[1]"), e.getMessage());
  }

  private static UaException assertMalformed(CallMethodResult result) {
    UaException e = assertThrows(UaException.class, () -> GetSecurityKeysResponse.parse(result));
    assertEquals(StatusCodes.Bad_UnexpectedError, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("malformed GetSecurityKeys response"), e.getMessage());
    return e;
  }

  private static CallMethodResult result(Variant... outputs) {
    return new CallMethodResult(StatusCode.GOOD, null, null, outputs);
  }
}
