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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface KeyCredentialConfigurationFolderType extends FolderType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getCreateCredentialMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateCredential(MethodBindings bindings, CreateCredentialHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateCredentialDetailed(
      MethodBindings bindings, CreateCredentialDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3 */
  @FunctionalInterface
  interface CreateCredentialHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name,
        @Nullable String resourceUri,
        @Nullable String profileUri,
        @Nullable String @Nullable [] endpointUrls)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3 */
  @FunctionalInterface
  interface CreateCredentialDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name,
        @Nullable String resourceUri,
        @Nullable String profileUri,
        @Nullable String @Nullable [] endpointUrls)
        throws UaException;
  }
}
