/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9">https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9</a>
 */
public interface ProgramDiagnostic2Type extends BaseDataVariableType {
  QualifiedProperty<DateTime> LAST_TRANSITION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastTransitionTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  DateTime getLastTransitionTime();

  void setLastTransitionTime(DateTime value);

  PropertyType getLastTransitionTimeNode();

  BaseDataVariableType getCreateSessionIdNode();

  NodeId getCreateSessionId();

  void setCreateSessionId(NodeId value);

  BaseDataVariableType getCreateClientNameNode();

  String getCreateClientName();

  void setCreateClientName(String value);

  BaseDataVariableType getInvocationCreationTimeNode();

  DateTime getInvocationCreationTime();

  void setInvocationCreationTime(DateTime value);

  BaseDataVariableType getLastMethodCallNode();

  String getLastMethodCall();

  void setLastMethodCall(String value);

  BaseDataVariableType getLastMethodSessionIdNode();

  NodeId getLastMethodSessionId();

  void setLastMethodSessionId(NodeId value);

  BaseDataVariableType getLastMethodInputArgumentsNode();

  Argument[] getLastMethodInputArguments();

  void setLastMethodInputArguments(Argument[] value);

  BaseDataVariableType getLastMethodOutputArgumentsNode();

  Argument[] getLastMethodOutputArguments();

  void setLastMethodOutputArguments(Argument[] value);

  BaseDataVariableType getLastMethodInputValuesNode();

  Object[] getLastMethodInputValues();

  void setLastMethodInputValues(Object[] value);

  BaseDataVariableType getLastMethodOutputValuesNode();

  Object[] getLastMethodOutputValues();

  void setLastMethodOutputValues(Object[] value);

  BaseDataVariableType getLastMethodCallTimeNode();

  DateTime getLastMethodCallTime();

  void setLastMethodCallTime(DateTime value);

  BaseDataVariableType getLastMethodReturnStatusNode();

  StatusCode getLastMethodReturnStatus();

  void setLastMethodReturnStatus(StatusCode value);
}
