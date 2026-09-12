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
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.3">https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ProvisionableDeviceType extends BaseObjectType {
  QualifiedProperty<Boolean> IS_SINGLETON =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IsSingleton",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable Boolean getIsSingleton();

  /** Sets the existing node's local value. */
  void setIsSingleton(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIsSingletonNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getRequestTicketsMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRequestTickets(MethodBindings bindings, RequestTicketsHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRequestTicketsDetailed(
      MethodBindings bindings, RequestTicketsDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getSetRegistrarEndpointsMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSetRegistrarEndpoints(
      MethodBindings bindings, SetRegistrarEndpointsHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSetRegistrarEndpointsDetailed(
      MethodBindings bindings, SetRegistrarEndpointsDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4 */
  @FunctionalInterface
  interface RequestTicketsHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable String @Nullable [] invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4 */
  @FunctionalInterface
  interface RequestTicketsDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable String @Nullable []> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5 */
  @FunctionalInterface
  interface SetRegistrarEndpointsHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ApplicationDescription @Nullable [] registrars)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5 */
  @FunctionalInterface
  interface SetRegistrarEndpointsDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ApplicationDescription @Nullable [] registrars)
        throws UaException;
  }
}
