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
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Outputs of the <code>CloseAndUpdate</code> Method, in declaration order.
 *
 * <p>Values may be null. Factories and accessors copy array containers shallowly. Application
 * status outputs remain separate from the Method operation status.
 */
@NullMarked
public interface ConfigurationFileTypeCloseAndUpdateOutputs {
  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable StatusCode @Nullable [] updateResults();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable UInteger newVersion();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable UUID updateId();

  /** Creates an immutable output object. Array containers are copied shallowly. */
  static ConfigurationFileTypeCloseAndUpdateOutputs of(
      @Nullable StatusCode @Nullable [] updateResults,
      @Nullable UInteger newVersion,
      @Nullable UUID updateId) {
    @Nullable StatusCode @Nullable [] updateResultsValue =
        ConfigurationFileTypeCloseAndUpdateOutputs.snapshot(updateResults);
    @Nullable UInteger newVersionValue =
        ConfigurationFileTypeCloseAndUpdateOutputs.snapshot(newVersion);
    @Nullable UUID updateIdValue = ConfigurationFileTypeCloseAndUpdateOutputs.snapshot(updateId);
    return new ConfigurationFileTypeCloseAndUpdateOutputs() {
      @Override
      public @Nullable StatusCode @Nullable [] updateResults() {
        return ConfigurationFileTypeCloseAndUpdateOutputs.snapshot(updateResultsValue);
      }

      @Override
      public @Nullable UInteger newVersion() {
        return ConfigurationFileTypeCloseAndUpdateOutputs.snapshot(newVersionValue);
      }

      @Override
      public @Nullable UUID updateId() {
        return ConfigurationFileTypeCloseAndUpdateOutputs.snapshot(updateIdValue);
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
