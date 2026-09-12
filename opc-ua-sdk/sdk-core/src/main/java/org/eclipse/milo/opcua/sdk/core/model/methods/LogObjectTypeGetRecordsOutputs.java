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
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecordsDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Outputs of the <code>GetRecords</code> Method, in declaration order.
 *
 * <p>Values may be null. Factories and accessors copy array containers shallowly. Application
 * status outputs remain separate from the Method operation status.
 */
@NullMarked
public interface LogObjectTypeGetRecordsOutputs {
  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable LogRecordsDataType results();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable ByteString continuationPointOut();

  /** Creates an immutable output object. Array containers are copied shallowly. */
  static LogObjectTypeGetRecordsOutputs of(
      @Nullable LogRecordsDataType results, @Nullable ByteString continuationPointOut) {
    @Nullable LogRecordsDataType resultsValue = LogObjectTypeGetRecordsOutputs.snapshot(results);
    @Nullable ByteString continuationPointOutValue =
        LogObjectTypeGetRecordsOutputs.snapshot(continuationPointOut);
    return new LogObjectTypeGetRecordsOutputs() {
      @Override
      public @Nullable LogRecordsDataType results() {
        return LogObjectTypeGetRecordsOutputs.snapshot(resultsValue);
      }

      @Override
      public @Nullable ByteString continuationPointOut() {
        return LogObjectTypeGetRecordsOutputs.snapshot(continuationPointOutValue);
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
