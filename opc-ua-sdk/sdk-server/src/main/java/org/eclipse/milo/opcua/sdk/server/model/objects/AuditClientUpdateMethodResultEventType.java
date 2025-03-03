/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.37">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.37</a>
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

  ExpandedNodeId getObjectId();

  void setObjectId(ExpandedNodeId value);

  PropertyType getObjectIdNode();

  ExpandedNodeId getMethodId();

  void setMethodId(ExpandedNodeId value);

  PropertyType getMethodIdNode();

  StatusCode getStatusCodeId();

  void setStatusCodeId(StatusCode value);

  PropertyType getStatusCodeIdNode();

  Object[] getInputArguments();

  void setInputArguments(Object[] value);

  PropertyType getInputArgumentsNode();

  Object[] getOutputArguments();

  void setOutputArguments(Object[] value);

  PropertyType getOutputArgumentsNode();
}
