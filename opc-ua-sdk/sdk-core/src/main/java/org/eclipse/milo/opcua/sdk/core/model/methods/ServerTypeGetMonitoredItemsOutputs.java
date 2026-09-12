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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Outputs of the <code>GetMonitoredItems</code> Method, in declaration order.
 *
 * <p>Values may be null. Factories and accessors copy array containers shallowly. Application
 * status outputs remain separate from the Method operation status.
 */
@NullMarked
public interface ServerTypeGetMonitoredItemsOutputs {
  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable UInteger @Nullable [] serverHandles();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable UInteger @Nullable [] clientHandles();

  /** Creates an immutable output object. Array containers are copied shallowly. */
  static ServerTypeGetMonitoredItemsOutputs of(
      @Nullable UInteger @Nullable [] serverHandles,
      @Nullable UInteger @Nullable [] clientHandles) {
    @Nullable UInteger @Nullable [] serverHandlesValue =
        ServerTypeGetMonitoredItemsOutputs.snapshot(serverHandles);
    @Nullable UInteger @Nullable [] clientHandlesValue =
        ServerTypeGetMonitoredItemsOutputs.snapshot(clientHandles);
    return new ServerTypeGetMonitoredItemsOutputs() {
      @Override
      public @Nullable UInteger @Nullable [] serverHandles() {
        return ServerTypeGetMonitoredItemsOutputs.snapshot(serverHandlesValue);
      }

      @Override
      public @Nullable UInteger @Nullable [] clientHandles() {
        return ServerTypeGetMonitoredItemsOutputs.snapshot(clientHandlesValue);
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
