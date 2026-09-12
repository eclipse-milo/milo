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
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Outputs of the <code>AddPublishedDataItems</code> Method, in declaration order.
 *
 * <p>Values may be null. Factories and accessors copy array containers shallowly. Application
 * status outputs remain separate from the Method operation status.
 */
@NullMarked
public interface DataSetFolderTypeAddPublishedDataItemsOutputs {
  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable NodeId dataSetNodeId();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable ConfigurationVersionDataType configurationVersion();

  /**
   * @return the output value, including null; array containers are copied.
   */
  @Nullable StatusCode @Nullable [] addResults();

  /** Creates an immutable output object. Array containers are copied shallowly. */
  static DataSetFolderTypeAddPublishedDataItemsOutputs of(
      @Nullable NodeId dataSetNodeId,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable StatusCode @Nullable [] addResults) {
    @Nullable NodeId dataSetNodeIdValue =
        DataSetFolderTypeAddPublishedDataItemsOutputs.snapshot(dataSetNodeId);
    @Nullable ConfigurationVersionDataType configurationVersionValue =
        DataSetFolderTypeAddPublishedDataItemsOutputs.snapshot(configurationVersion);
    @Nullable StatusCode @Nullable [] addResultsValue =
        DataSetFolderTypeAddPublishedDataItemsOutputs.snapshot(addResults);
    return new DataSetFolderTypeAddPublishedDataItemsOutputs() {
      @Override
      public @Nullable NodeId dataSetNodeId() {
        return DataSetFolderTypeAddPublishedDataItemsOutputs.snapshot(dataSetNodeIdValue);
      }

      @Override
      public @Nullable ConfigurationVersionDataType configurationVersion() {
        return DataSetFolderTypeAddPublishedDataItemsOutputs.snapshot(configurationVersionValue);
      }

      @Override
      public @Nullable StatusCode @Nullable [] addResults() {
        return DataSetFolderTypeAddPublishedDataItemsOutputs.snapshot(addResultsValue);
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
