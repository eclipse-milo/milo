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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodHandlerResult;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundantServerMode;
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.15">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.15</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface NonTransparentBackupRedundancyType extends NonTransparentRedundancyType {
  QualifiedProperty<RedundantServerDataType[]> REDUNDANT_SERVER_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RedundantServerArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=853"),
          1,
          RedundantServerDataType[].class);

  QualifiedProperty<RedundantServerMode> MODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Mode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32417"),
          -1,
          RedundantServerMode.class);

  /** Gets the existing node's local value. */
  @Nullable RedundantServerDataType @Nullable [] getRedundantServerArray();

  /** Sets the existing node's local value. */
  void setRedundantServerArray(@Nullable RedundantServerDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRedundantServerArrayNode();

  /** Gets the existing node's local value. */
  @Nullable RedundantServerMode getMode();

  /** Sets the existing node's local value. */
  void setMode(@Nullable RedundantServerMode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getModeNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getFailoverMethodNode();

  /**
   * Binds a synchronous callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindFailover(MethodBindings bindings, FailoverHandler handler) throws UaException;

  /**
   * Binds a synchronous callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindFailoverDetailed(MethodBindings bindings, FailoverDetailedHandler handler)
      throws UaException;

  /** */
  @FunctionalInterface
  interface FailoverHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** */
  @FunctionalInterface
  interface FailoverDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }
}
