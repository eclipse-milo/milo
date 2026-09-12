/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.jspecify.annotations.Nullable;

/**
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
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

  /** Gets the existing node's local value. */
  @Nullable NodeId getCreateSessionId();

  /** Sets the existing node's local value. */
  void setCreateSessionId(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCreateSessionIdNode();

  /** Gets the existing node's local value. */
  @Nullable String getCreateClientName();

  /** Sets the existing node's local value. */
  void setCreateClientName(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCreateClientNameNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getInvocationCreationTime();

  /** Sets the existing node's local value. */
  void setInvocationCreationTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInvocationCreationTimeNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastTransitionTime();

  /** Sets the existing node's local value. */
  void setLastTransitionTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastTransitionTimeNode();

  /** Gets the existing node's local value. */
  @Nullable String getLastMethodCall();

  /** Sets the existing node's local value. */
  void setLastMethodCall(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastMethodCallNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getLastMethodSessionId();

  /** Sets the existing node's local value. */
  void setLastMethodSessionId(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastMethodSessionIdNode();

  /** Gets the existing node's local value. */
  @Nullable Object @Nullable [] getLastMethodInputArguments();

  /** Sets the existing node's local value. */
  void setLastMethodInputArguments(@Nullable Object @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastMethodInputArgumentsNode();

  /** Gets the existing node's local value. */
  @Nullable Object @Nullable [] getLastMethodOutputArguments();

  /** Sets the existing node's local value. */
  void setLastMethodOutputArguments(@Nullable Object @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastMethodOutputArgumentsNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastMethodCallTime();

  /** Sets the existing node's local value. */
  void setLastMethodCallTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastMethodCallTimeNode();

  /** Gets the existing node's local value. */
  @Nullable StatusResult getLastMethodReturnStatus();

  /** Sets the existing node's local value. */
  void setLastMethodReturnStatus(@Nullable StatusResult value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastMethodReturnStatusNode();
}
