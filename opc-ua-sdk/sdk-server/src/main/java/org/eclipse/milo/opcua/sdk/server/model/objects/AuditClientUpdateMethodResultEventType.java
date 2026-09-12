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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.37">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.37</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditClientUpdateMethodResultEventType extends AuditClientEventType {
  QualifiedProperty<ExpandedNodeId> OBJECT_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ObjectId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=18"),
          -1,
          ExpandedNodeId.class);

  QualifiedProperty<ExpandedNodeId> METHOD_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MethodId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=18"),
          -1,
          ExpandedNodeId.class);

  QualifiedProperty<StatusCode> STATUS_CODE_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StatusCodeId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19"),
          -1,
          StatusCode.class);

  QualifiedProperty<Object[]> INPUT_ARGUMENTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InputArguments",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          1,
          Object[].class);

  QualifiedProperty<Object[]> OUTPUT_ARGUMENTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OutputArguments",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          1,
          Object[].class);

  /** Gets the existing node's local value. */
  @Nullable ExpandedNodeId getObjectId();

  /** Sets the existing node's local value. */
  void setObjectId(@Nullable ExpandedNodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getObjectIdNode();

  /** Gets the existing node's local value. */
  @Nullable ExpandedNodeId getMethodId();

  /** Sets the existing node's local value. */
  void setMethodId(@Nullable ExpandedNodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMethodIdNode();

  /** Gets the existing node's local value. */
  @Nullable StatusCode getStatusCodeId();

  /** Sets the existing node's local value. */
  void setStatusCodeId(@Nullable StatusCode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStatusCodeIdNode();

  /** Gets the existing node's local value. */
  @Nullable Object @Nullable [] getInputArguments();

  /** Sets the existing node's local value. */
  void setInputArguments(@Nullable Object @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInputArgumentsNode();

  /** Gets the existing node's local value. */
  @Nullable Object @Nullable [] getOutputArguments();

  /** Sets the existing node's local value. */
  void setOutputArguments(@Nullable Object @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getOutputArgumentsNode();
}
