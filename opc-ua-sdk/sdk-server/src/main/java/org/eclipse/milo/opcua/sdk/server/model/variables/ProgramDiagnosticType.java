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
import org.eclipse.milo.opcua.stack.core.types.structured.StatusResult;

public interface ProgramDiagnosticType extends BaseDataVariableType {
  QualifiedProperty<NodeId> CREATE_SESSION_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CreateSessionId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<String> CREATE_CLIENT_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CreateClientName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<DateTime> INVOCATION_CREATION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InvocationCreationTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> LAST_TRANSITION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastTransitionTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<String> LAST_METHOD_CALL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastMethodCall",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<NodeId> LAST_METHOD_SESSION_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastMethodSessionId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<Object[]> LAST_METHOD_INPUT_ARGUMENTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastMethodInputArguments",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          1,
          Object[].class);

  QualifiedProperty<Object[]> LAST_METHOD_OUTPUT_ARGUMENTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastMethodOutputArguments",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          1,
          Object[].class);

  QualifiedProperty<DateTime> LAST_METHOD_CALL_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastMethodCallTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<StatusResult> LAST_METHOD_RETURN_STATUS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastMethodReturnStatus",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=299"),
          -1,
          StatusResult.class);

  NodeId getCreateSessionId();

  void setCreateSessionId(NodeId value);

  PropertyType getCreateSessionIdNode();

  String getCreateClientName();

  void setCreateClientName(String value);

  PropertyType getCreateClientNameNode();

  DateTime getInvocationCreationTime();

  void setInvocationCreationTime(DateTime value);

  PropertyType getInvocationCreationTimeNode();

  DateTime getLastTransitionTime();

  void setLastTransitionTime(DateTime value);

  PropertyType getLastTransitionTimeNode();

  String getLastMethodCall();

  void setLastMethodCall(String value);

  PropertyType getLastMethodCallNode();

  NodeId getLastMethodSessionId();

  void setLastMethodSessionId(NodeId value);

  PropertyType getLastMethodSessionIdNode();

  Object[] getLastMethodInputArguments();

  void setLastMethodInputArguments(Object[] value);

  PropertyType getLastMethodInputArgumentsNode();

  Object[] getLastMethodOutputArguments();

  void setLastMethodOutputArguments(Object[] value);

  PropertyType getLastMethodOutputArgumentsNode();

  DateTime getLastMethodCallTime();

  void setLastMethodCallTime(DateTime value);

  PropertyType getLastMethodCallTimeNode();

  StatusResult getLastMethodReturnStatus();

  void setLastMethodReturnStatus(StatusResult value);

  PropertyType getLastMethodReturnStatusNode();
}
