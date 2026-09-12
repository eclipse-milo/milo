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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Outputs of the <code>GetEncryptingKey</code> Method, in declaration order.
 *
 * <p>Values may be null. Factories and accessors copy array containers shallowly. Application
 * status outputs remain separate from the Method operation status.
 */
@NullMarked
public interface KeyCredentialConfigurationTypeGetEncryptingKeyOutputs {
  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable ByteString publicKey();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable String revisedSecurityPolicyUri();

  /** Creates an immutable output object. Array containers are copied shallowly. */
  static KeyCredentialConfigurationTypeGetEncryptingKeyOutputs of(
      @Nullable ByteString publicKey, @Nullable String revisedSecurityPolicyUri) {
    @Nullable ByteString publicKeyValue =
        KeyCredentialConfigurationTypeGetEncryptingKeyOutputs.snapshot(publicKey);
    @Nullable String revisedSecurityPolicyUriValue =
        KeyCredentialConfigurationTypeGetEncryptingKeyOutputs.snapshot(revisedSecurityPolicyUri);
    return new KeyCredentialConfigurationTypeGetEncryptingKeyOutputs() {
      @Override
      public @Nullable ByteString publicKey() {
        return KeyCredentialConfigurationTypeGetEncryptingKeyOutputs.snapshot(publicKeyValue);
      }

      @Override
      public @Nullable String revisedSecurityPolicyUri() {
        return KeyCredentialConfigurationTypeGetEncryptingKeyOutputs.snapshot(
            revisedSecurityPolicyUriValue);
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
