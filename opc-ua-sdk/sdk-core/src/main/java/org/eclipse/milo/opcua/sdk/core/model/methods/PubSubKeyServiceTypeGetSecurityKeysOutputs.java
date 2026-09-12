/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.lang.reflect.Array;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Outputs of the <code>GetSecurityKeys</code> Method, in declaration order.
 *
 * <p>Values may be null. Factories and accessors copy array containers shallowly. Application
 * status outputs remain separate from the Method operation status.
 */
@NullMarked
public interface PubSubKeyServiceTypeGetSecurityKeysOutputs {
  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable String securityPolicyUri();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable UInteger firstTokenId();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable ByteString @Nullable [] keys();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable Double timeToNextKey();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable Double keyLifetime();

  /** Creates an immutable output object. Array containers are copied shallowly. */
  static PubSubKeyServiceTypeGetSecurityKeysOutputs of(
      @Nullable String securityPolicyUri,
      @Nullable UInteger firstTokenId,
      @Nullable ByteString @Nullable [] keys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime) {
    @Nullable String securityPolicyUriValue =
        PubSubKeyServiceTypeGetSecurityKeysOutputs.snapshot(securityPolicyUri);
    @Nullable UInteger firstTokenIdValue =
        PubSubKeyServiceTypeGetSecurityKeysOutputs.snapshot(firstTokenId);
    @Nullable ByteString @Nullable [] keysValue =
        PubSubKeyServiceTypeGetSecurityKeysOutputs.snapshot(keys);
    @Nullable Double timeToNextKeyValue =
        PubSubKeyServiceTypeGetSecurityKeysOutputs.snapshot(timeToNextKey);
    @Nullable Double keyLifetimeValue =
        PubSubKeyServiceTypeGetSecurityKeysOutputs.snapshot(keyLifetime);
    return new PubSubKeyServiceTypeGetSecurityKeysOutputs() {
      @Override
      public @Nullable String securityPolicyUri() {
        return PubSubKeyServiceTypeGetSecurityKeysOutputs.snapshot(securityPolicyUriValue);
      }

      @Override
      public @Nullable UInteger firstTokenId() {
        return PubSubKeyServiceTypeGetSecurityKeysOutputs.snapshot(firstTokenIdValue);
      }

      @Override
      public @Nullable ByteString @Nullable [] keys() {
        return PubSubKeyServiceTypeGetSecurityKeysOutputs.snapshot(keysValue);
      }

      @Override
      public @Nullable Double timeToNextKey() {
        return PubSubKeyServiceTypeGetSecurityKeysOutputs.snapshot(timeToNextKeyValue);
      }

      @Override
      public @Nullable Double keyLifetime() {
        return PubSubKeyServiceTypeGetSecurityKeysOutputs.snapshot(keyLifetimeValue);
      }
    };
  }

  @SuppressWarnings("unchecked")
  private static <T extends @Nullable Object> T snapshot(T value) {
    if (value == null || !value.getClass().isArray()) {
      return value;
    }
    int length = Array.getLength(value);
    Object copy = Array.newInstance(value.getClass().getComponentType(), length);
    System.arraycopy(value, 0, copy, 0, length);
    return (T) copy;
  }
}
