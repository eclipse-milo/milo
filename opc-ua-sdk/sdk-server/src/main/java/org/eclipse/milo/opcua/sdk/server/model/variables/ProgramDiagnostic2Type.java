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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9">https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ProgramDiagnostic2Type extends BaseDataVariableType {
  QualifiedProperty<DateTime> LAST_TRANSITION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastTransitionTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

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

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCreateSessionIdNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getCreateSessionId();

  /** Sets the existing node's local value. */
  void setCreateSessionId(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getCreateClientNameNode();

  /** Gets the existing node's local value. */
  @Nullable String getCreateClientName();

  /** Sets the existing node's local value. */
  void setCreateClientName(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getInvocationCreationTimeNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getInvocationCreationTime();

  /** Sets the existing node's local value. */
  void setInvocationCreationTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodCallNode();

  /** Gets the existing node's local value. */
  @Nullable String getLastMethodCall();

  /** Sets the existing node's local value. */
  void setLastMethodCall(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodSessionIdNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getLastMethodSessionId();

  /** Sets the existing node's local value. */
  void setLastMethodSessionId(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodInputArgumentsNode();

  /** Gets the existing node's local value. */
  @Nullable Argument @Nullable [] getLastMethodInputArguments();

  /** Sets the existing node's local value. */
  void setLastMethodInputArguments(@Nullable Argument @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodOutputArgumentsNode();

  /** Gets the existing node's local value. */
  @Nullable Argument @Nullable [] getLastMethodOutputArguments();

  /** Sets the existing node's local value. */
  void setLastMethodOutputArguments(@Nullable Argument @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodInputValuesNode();

  /** Gets the existing node's local value. */
  @Nullable Object @Nullable [] getLastMethodInputValues();

  /** Sets the existing node's local value. */
  void setLastMethodInputValues(@Nullable Object @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodOutputValuesNode();

  /** Gets the existing node's local value. */
  @Nullable Object @Nullable [] getLastMethodOutputValues();

  /** Sets the existing node's local value. */
  void setLastMethodOutputValues(@Nullable Object @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodCallTimeNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastMethodCallTime();

  /** Sets the existing node's local value. */
  void setLastMethodCallTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastMethodReturnStatusNode();

  /** Gets the existing node's local value. */
  @Nullable StatusCode getLastMethodReturnStatus();

  /** Sets the existing node's local value. */
  void setLastMethodReturnStatus(@Nullable StatusCode value);
}
