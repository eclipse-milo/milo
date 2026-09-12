/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.2/#9.2.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.2/#9.2.2.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface JsonDataSetWriterMessageType extends DataSetWriterMessageType {
  QualifiedProperty<JsonDataSetMessageContentMask> DATA_SET_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15658"),
          -1,
          JsonDataSetMessageContentMask.class);

  /** Gets the existing node's local value. */
  @Nullable JsonDataSetMessageContentMask getDataSetMessageContentMask();

  /** Sets the existing node's local value. */
  void setDataSetMessageContentMask(@Nullable JsonDataSetMessageContentMask value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetMessageContentMaskNode();
}
