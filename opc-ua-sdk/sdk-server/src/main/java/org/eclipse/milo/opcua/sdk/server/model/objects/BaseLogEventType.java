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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part26/6.3">https://reference.opcfoundation.org/v105/Core/docs/Part26/6.3</a>
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

  NodeId getConditionClassId();

  void setConditionClassId(NodeId value);

  PropertyType getConditionClassIdNode();

  LocalizedText getConditionClassName();

  void setConditionClassName(LocalizedText value);

  PropertyType getConditionClassNameNode();

  StatusCode getErrorCode();

  void setErrorCode(StatusCode value);

  PropertyType getErrorCodePropertyNode();

  NodeId getErrorCodeNode();

  void setErrorCodeNode(NodeId value);

  PropertyType getErrorCodeNodeNode();

  TraceContextDataType getTraceContext();

  void setTraceContext(TraceContextDataType value);

  PropertyType getTraceContextNode();
}
