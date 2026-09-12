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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodHandlerResult;
import org.eclipse.milo.opcua.sdk.core.model.methods.SecurityGroupFolderTypeAddSecurityGroupOutputs;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2
 *
 * <p>Synchronous returned callback for this Method. The invocation context retains the ObjectId,
 * Method, session and available request-wide diagnostic context. A supplied null input differs from
 * an omitted optional input.
 */
@FunctionalInterface
public interface SecurityGroupFolderTypeAddSecurityGroupDetailedHandler {
  /**
   * Return a complete Good/Uncertain outcome or an explicit Bad outcome. Bad outcomes have no wire
   * outputs. Argument diagnostics use supplied positions and the context's request-wide string
   * table.
   *
   * @param context the actual invocation owner, Method, session and diagnostics context
   * @param securityGroupName the input value; supplied null is retained
   * @param keyLifetime the input value; supplied null is retained
   * @param securityPolicyUri the input value; supplied null is retained
   * @param maxFutureKeyCount the input value; supplied null is retained
   * @param maxPastKeyCount the input value; supplied null is retained
   * @return a non-null complete operation outcome
   * @throws UaException for an operation failure
   */
  MethodHandlerResult<SecurityGroupFolderTypeAddSecurityGroupOutputs> invoke(
      AbstractMethodInvocationHandler.InvocationContext context,
      @Nullable String securityGroupName,
      @Nullable Double keyLifetime,
      @Nullable String securityPolicyUri,
      @Nullable UInteger maxFutureKeyCount,
      @Nullable UInteger maxPastKeyCount)
      throws UaException;
}
