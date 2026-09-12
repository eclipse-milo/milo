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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.TraceContextDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part26/6.3">https://reference.opcfoundation.org/v105/Core/docs/Part26/6.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface BaseLogEventType extends BaseEventType {
  QualifiedProperty<NodeId> CONDITION_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<LocalizedText> CONDITION_CLASS_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionClassName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<StatusCode> ERROR_CODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ErrorCode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19"),
          -1,
          StatusCode.class);

  QualifiedProperty<NodeId> ERROR_CODE_NODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ErrorCodeNode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<TraceContextDataType> TRACE_CONTEXT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TraceContext",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19747"),
          -1,
          TraceContextDataType.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId getConditionClassId();

  /** Sets the existing node's local value. */
  void setConditionClassId(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConditionClassIdNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getConditionClassName();

  /** Sets the existing node's local value. */
  void setConditionClassName(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConditionClassNameNode();

  /** Gets the existing node's local value. */
  @Nullable StatusCode getErrorCode();

  /** Sets the existing node's local value. */
  void setErrorCode(@Nullable StatusCode value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getErrorCodePropertyNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getErrorCodeNode();

  /** Sets the existing node's local value. */
  void setErrorCodeNode(@Nullable NodeId value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getErrorCodeNodeNode();

  /** Gets the existing node's local value. */
  @Nullable TraceContextDataType getTraceContext();

  /** Sets the existing node's local value. */
  void setTraceContext(@Nullable TraceContextDataType value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getTraceContextNode();
}
