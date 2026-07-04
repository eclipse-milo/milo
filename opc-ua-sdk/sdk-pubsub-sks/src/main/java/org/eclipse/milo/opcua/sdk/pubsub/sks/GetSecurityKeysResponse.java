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

import java.time.Duration;
import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.jspecify.annotations.Nullable;

/**
 * Parses the five {@code GetSecurityKeys} output arguments into a {@link SecurityKeySet}.
 *
 * <p>Expected shape (Part 14 §8.3.2): {@code SecurityPolicyUri (String)}, {@code FirstTokenId
 * (IntegerId/UInt32)}, {@code Keys (ByteString[])}, {@code TimeToNextKey (Duration)}, {@code
 * KeyLifetime (Duration)}. Part 3 {@code Duration} is a Double of (possibly fractional)
 * milliseconds; NaN, infinite, zero, and negative values are malformed — zero durations are
 * reserved for the static-key sentinel that only {@code StaticSecurityKeyProvider} produces.
 *
 * <p>{@code SecurityPolicyUri} is passed through VERBATIM: the policy-vs-config mismatch check
 * belongs to the consuming key manager, never to this transport.
 *
 * <p>Every field is defensively checked — a foreign SKS returning null, short, or mistyped outputs
 * must fail the fetch with a diagnostic, not throw {@code ClassCastException} on the provider
 * thread.
 */
final class GetSecurityKeysResponse {

  private GetSecurityKeysResponse() {}

  /**
   * Parse a Good {@link CallMethodResult} into a {@link SecurityKeySet}.
   *
   * @param result the {@link CallMethodResult} of a GetSecurityKeys call.
   * @return the parsed {@link SecurityKeySet}.
   * @throws UaException with {@code Bad_UnexpectedError} if the outputs are malformed.
   */
  static SecurityKeySet parse(CallMethodResult result) throws UaException {
    Variant[] outputs = result.getOutputArguments();
    if (outputs == null || outputs.length < 5) {
      throw malformed(
          "expected 5 output arguments, got "
              + (outputs == null ? "none" : String.valueOf(outputs.length)));
    }

    String securityPolicyUri = value(outputs[0], String.class, "SecurityPolicyUri");
    UInteger firstTokenId = value(outputs[1], UInteger.class, "FirstTokenId");
    ByteString[] keys = value(outputs[2], ByteString[].class, "Keys");
    Double timeToNextKeyMillis = value(outputs[3], Double.class, "TimeToNextKey");
    Double keyLifetimeMillis = value(outputs[4], Double.class, "KeyLifetime");

    if (keys.length == 0) {
      throw malformed("Keys must be non-empty");
    }
    for (int i = 0; i < keys.length; i++) {
      if (keys[i] == null) {
        throw malformed("Keys[" + i + "] is null");
      }
    }

    Duration timeToNextKey = duration(timeToNextKeyMillis, "TimeToNextKey");
    Duration keyLifetime = duration(keyLifetimeMillis, "KeyLifetime");

    return new SecurityKeySet(
        securityPolicyUri, firstTokenId, List.of(keys), timeToNextKey, keyLifetime);
  }

  private static <T> T value(@Nullable Variant variant, Class<T> type, String name)
      throws UaException {
    Object value = variant == null ? null : variant.value();
    if (!type.isInstance(value)) {
      throw malformed(
          "%s: expected %s, got %s"
              .formatted(
                  name,
                  type.getSimpleName(),
                  value == null ? "null" : value.getClass().getSimpleName()));
    }
    return type.cast(value);
  }

  private static Duration duration(double millis, String name) throws UaException {
    // !(millis > 0.0) catches NaN as well as zero and negative values.
    if (!(millis > 0.0) || Double.isInfinite(millis)) {
      throw malformed(name + ": expected a positive finite millisecond Duration, got " + millis);
    }
    // Convert via nanos to preserve fractional milliseconds; the (long) cast saturates, so
    // absurdly large values clamp rather than overflow.
    return Duration.ofNanos((long) (millis * 1_000_000.0));
  }

  private static UaException malformed(String detail) {
    return new UaException(
        StatusCodes.Bad_UnexpectedError, "malformed GetSecurityKeys response: " + detail);
  }
}
