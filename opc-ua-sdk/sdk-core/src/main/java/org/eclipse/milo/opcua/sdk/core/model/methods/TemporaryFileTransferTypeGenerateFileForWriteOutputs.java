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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Outputs of the <code>GenerateFileForWrite</code> Method, in declaration order.
 *
 * <p>Values may be null. Factories and accessors copy array containers shallowly. Application
 * status outputs remain separate from the Method operation status.
 */
@NullMarked
public interface TemporaryFileTransferTypeGenerateFileForWriteOutputs {
  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable NodeId fileNodeId();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable UInteger fileHandle();

  /** Creates an immutable output object. Array containers are copied shallowly. */
  static TemporaryFileTransferTypeGenerateFileForWriteOutputs of(
      @Nullable NodeId fileNodeId, @Nullable UInteger fileHandle) {
    @Nullable NodeId fileNodeIdValue =
        TemporaryFileTransferTypeGenerateFileForWriteOutputs.snapshot(fileNodeId);
    @Nullable UInteger fileHandleValue =
        TemporaryFileTransferTypeGenerateFileForWriteOutputs.snapshot(fileHandle);
    return new TemporaryFileTransferTypeGenerateFileForWriteOutputs() {
      @Override
      public @Nullable NodeId fileNodeId() {
        return TemporaryFileTransferTypeGenerateFileForWriteOutputs.snapshot(fileNodeIdValue);
      }

      @Override
      public @Nullable UInteger fileHandle() {
        return TemporaryFileTransferTypeGenerateFileForWriteOutputs.snapshot(fileHandleValue);
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
