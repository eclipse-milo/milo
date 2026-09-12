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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.35">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.35</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ProgressEventType extends BaseEventType {
  QualifiedProperty<Object> CONTEXT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Context",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  QualifiedProperty<UShort> PROGRESS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Progress",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  /** Gets the existing node's local value. */
  @Nullable Object getContext();

  /** Sets the existing node's local value. */
  void setContext(@Nullable Object value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getContextNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getProgress();

  /** Sets the existing node's local value. */
  void setProgress(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getProgressNode();
}
