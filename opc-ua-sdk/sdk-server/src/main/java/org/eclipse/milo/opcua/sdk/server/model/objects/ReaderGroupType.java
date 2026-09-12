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
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.9">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.9</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ReaderGroupType extends PubSubGroupType {
  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubDiagnosticsReaderGroupType getDiagnosticsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable ReaderGroupTransportType getTransportSettingsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable ReaderGroupMessageType getMessageSettingsNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddDataSetReaderMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddDataSetReader(MethodBindings bindings, AddDataSetReaderHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddDataSetReaderDetailed(
      MethodBindings bindings, AddDataSetReaderDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemoveDataSetReaderMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveDataSetReader(MethodBindings bindings, RemoveDataSetReaderHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveDataSetReaderDetailed(
      MethodBindings bindings, RemoveDataSetReaderDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10 */
  @FunctionalInterface
  interface AddDataSetReaderHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable DataSetReaderDataType configuration)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10 */
  @FunctionalInterface
  interface AddDataSetReaderDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable DataSetReaderDataType configuration)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11 */
  @FunctionalInterface
  interface RemoveDataSetReaderHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId dataSetReaderNodeId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11 */
  @FunctionalInterface
  interface RemoveDataSetReaderDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId dataSetReaderNodeId)
        throws UaException;
  }
}
