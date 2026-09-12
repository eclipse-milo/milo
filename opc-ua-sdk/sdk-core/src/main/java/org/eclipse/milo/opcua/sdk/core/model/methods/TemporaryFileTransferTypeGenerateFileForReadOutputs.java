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
 * Outputs of the <code>GenerateFileForRead</code> Method, in declaration order.
 *
 * <p>Values may be null. Factories and accessors copy array containers shallowly. Application
 * status outputs remain separate from the Method operation status.
 */
@NullMarked
public interface TemporaryFileTransferTypeGenerateFileForReadOutputs {
  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable NodeId fileNodeId();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable UInteger fileHandle();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable NodeId completionStateMachine();

  /** Creates an immutable output object. Array containers are copied shallowly. */
  static TemporaryFileTransferTypeGenerateFileForReadOutputs of(
      @Nullable NodeId fileNodeId,
      @Nullable UInteger fileHandle,
      @Nullable NodeId completionStateMachine) {
    @Nullable NodeId fileNodeIdValue =
        TemporaryFileTransferTypeGenerateFileForReadOutputs.snapshot(fileNodeId);
    @Nullable UInteger fileHandleValue =
        TemporaryFileTransferTypeGenerateFileForReadOutputs.snapshot(fileHandle);
    @Nullable NodeId completionStateMachineValue =
        TemporaryFileTransferTypeGenerateFileForReadOutputs.snapshot(completionStateMachine);
    return new TemporaryFileTransferTypeGenerateFileForReadOutputs() {
      @Override
      public @Nullable NodeId fileNodeId() {
        return TemporaryFileTransferTypeGenerateFileForReadOutputs.snapshot(fileNodeIdValue);
      }

      @Override
      public @Nullable UInteger fileHandle() {
        return TemporaryFileTransferTypeGenerateFileForReadOutputs.snapshot(fileHandleValue);
      }

      @Override
      public @Nullable NodeId completionStateMachine() {
        return TemporaryFileTransferTypeGenerateFileForReadOutputs.snapshot(
            completionStateMachineValue);
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
