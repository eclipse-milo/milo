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
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConfigurationTypeCloseAndUpdateOutputs;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConfigurationTypeReserveIdsOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubConfigurationType extends FileType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getReserveIdsMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindReserveIds(MethodBindings bindings, ReserveIdsHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindReserveIdsDetailed(MethodBindings bindings, ReserveIdsDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getCloseAndUpdateMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCloseAndUpdate(MethodBindings bindings, CloseAndUpdateHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCloseAndUpdateDetailed(
      MethodBindings bindings, CloseAndUpdateDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5 */
  @FunctionalInterface
  interface ReserveIdsHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    PubSubConfigurationTypeReserveIdsOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String transportProfileUri,
        @Nullable UShort numReqWriterGroupIds,
        @Nullable UShort numReqDataSetWriterIds)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5 */
  @FunctionalInterface
  interface ReserveIdsDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<PubSubConfigurationTypeReserveIdsOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String transportProfileUri,
        @Nullable UShort numReqWriterGroupIds,
        @Nullable UShort numReqDataSetWriterIds)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6 */
  @FunctionalInterface
  interface CloseAndUpdateHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    PubSubConfigurationTypeCloseAndUpdateOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle,
        @Nullable Boolean requireCompleteUpdate,
        @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6 */
  @FunctionalInterface
  interface CloseAndUpdateDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<PubSubConfigurationTypeCloseAndUpdateOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle,
        @Nullable Boolean requireCompleteUpdate,
        @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences)
        throws UaException;
  }
}
