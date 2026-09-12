/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.methods;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3
 *
 * <p>Synchronous returned callback for this Method. The invocation context retains the ObjectId,
 * Method, session and available request-wide diagnostic context. A supplied null input differs from
 * an omitted optional input.
 */
@FunctionalInterface
public interface KeyCredentialConfigurationFolderTypeCreateCredentialHandler {
  /**
   * Return all outputs together. Normal completion produces exact Good. Conversion failure occurs
   * after this callback and never causes an automatic retry.
   *
   * @param context the actual invocation owner, Method, session and diagnostics context
   * @param name the input value; supplied null is retained
   * @param resourceUri the input value; supplied null is retained
   * @param profileUri the input value; supplied null is retained
   * @param endpointUrls the input value; supplied null is retained
   * @return the output value, including null
   * @throws UaException for an operation failure
   */
  @Nullable NodeId invoke(
      AbstractMethodInvocationHandler.InvocationContext context,
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls)
      throws UaException;
}
