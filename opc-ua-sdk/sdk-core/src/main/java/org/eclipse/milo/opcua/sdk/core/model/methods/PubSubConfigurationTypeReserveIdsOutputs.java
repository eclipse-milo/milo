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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Outputs of the <code>ReserveIds</code> Method, in declaration order.
 *
 * <p>Values may be null. Factories and accessors copy array containers shallowly. Application
 * status outputs remain separate from the Method operation status.
 */
@NullMarked
public interface PubSubConfigurationTypeReserveIdsOutputs {
  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable Object defaultPublisherId();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable UShort @Nullable [] writerGroupIds();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable UShort @Nullable [] dataSetWriterIds();

  /** Creates an immutable output object. Array containers are copied shallowly. */
  static PubSubConfigurationTypeReserveIdsOutputs of(
      @Nullable Object defaultPublisherId,
      @Nullable UShort @Nullable [] writerGroupIds,
      @Nullable UShort @Nullable [] dataSetWriterIds) {
    @Nullable Object defaultPublisherIdValue =
        PubSubConfigurationTypeReserveIdsOutputs.snapshot(defaultPublisherId);
    @Nullable UShort @Nullable [] writerGroupIdsValue =
        PubSubConfigurationTypeReserveIdsOutputs.snapshot(writerGroupIds);
    @Nullable UShort @Nullable [] dataSetWriterIdsValue =
        PubSubConfigurationTypeReserveIdsOutputs.snapshot(dataSetWriterIds);
    return new PubSubConfigurationTypeReserveIdsOutputs() {
      @Override
      public @Nullable Object defaultPublisherId() {
        return PubSubConfigurationTypeReserveIdsOutputs.snapshot(defaultPublisherIdValue);
      }

      @Override
      public @Nullable UShort @Nullable [] writerGroupIds() {
        return PubSubConfigurationTypeReserveIdsOutputs.snapshot(writerGroupIdsValue);
      }

      @Override
      public @Nullable UShort @Nullable [] dataSetWriterIds() {
        return PubSubConfigurationTypeReserveIdsOutputs.snapshot(dataSetWriterIdsValue);
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
