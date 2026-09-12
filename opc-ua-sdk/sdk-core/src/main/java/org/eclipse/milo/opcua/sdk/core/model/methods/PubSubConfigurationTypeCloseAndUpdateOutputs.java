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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationValueDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Outputs of the <code>CloseAndUpdate</code> Method, in declaration order.
 *
 * <p>Values may be null. Factories and accessors copy array containers shallowly. Application
 * status outputs remain separate from the Method operation status.
 */
@NullMarked
public interface PubSubConfigurationTypeCloseAndUpdateOutputs {
  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable Boolean changesApplied();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable StatusCode @Nullable [] referencesResults();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable PubSubConfigurationValueDataType @Nullable [] configurationValues();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable NodeId @Nullable [] configurationObjects();

  /** Creates an immutable output object. Array containers are copied shallowly. */
  static PubSubConfigurationTypeCloseAndUpdateOutputs of(
      @Nullable Boolean changesApplied,
      @Nullable StatusCode @Nullable [] referencesResults,
      @Nullable PubSubConfigurationValueDataType @Nullable [] configurationValues,
      @Nullable NodeId @Nullable [] configurationObjects) {
    @Nullable Boolean changesAppliedValue =
        PubSubConfigurationTypeCloseAndUpdateOutputs.snapshot(changesApplied);
    @Nullable StatusCode @Nullable [] referencesResultsValue =
        PubSubConfigurationTypeCloseAndUpdateOutputs.snapshot(referencesResults);
    @Nullable PubSubConfigurationValueDataType @Nullable [] configurationValuesValue =
        PubSubConfigurationTypeCloseAndUpdateOutputs.snapshot(configurationValues);
    @Nullable NodeId @Nullable [] configurationObjectsValue =
        PubSubConfigurationTypeCloseAndUpdateOutputs.snapshot(configurationObjects);
    return new PubSubConfigurationTypeCloseAndUpdateOutputs() {
      @Override
      public @Nullable Boolean changesApplied() {
        return PubSubConfigurationTypeCloseAndUpdateOutputs.snapshot(changesAppliedValue);
      }

      @Override
      public @Nullable StatusCode @Nullable [] referencesResults() {
        return PubSubConfigurationTypeCloseAndUpdateOutputs.snapshot(referencesResultsValue);
      }

      @Override
      public @Nullable PubSubConfigurationValueDataType @Nullable [] configurationValues() {
        return PubSubConfigurationTypeCloseAndUpdateOutputs.snapshot(configurationValuesValue);
      }

      @Override
      public @Nullable NodeId @Nullable [] configurationObjects() {
        return PubSubConfigurationTypeCloseAndUpdateOutputs.snapshot(configurationObjectsValue);
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
