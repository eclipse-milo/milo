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
import org.eclipse.milo.opcua.sdk.core.model.methods.ConfigurationFileTypeCloseAndUpdateOutputs;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationUpdateTargetType;
import org.jspecify.annotations.Nullable;

/**
 * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2
 *
 * <p>Synchronous returned callback for this Method. The invocation context retains the ObjectId,
 * Method, session and available request-wide diagnostic context. A supplied null input differs from
 * an omitted optional input.
 */
@FunctionalInterface
public interface ConfigurationFileTypeCloseAndUpdateDetailedHandler {
  /**
   * Return a complete Good/Uncertain outcome or an explicit Bad outcome. Bad outcomes have no wire
   * outputs. Argument diagnostics use supplied positions and the context's request-wide string
   * table.
   *
   * @param context the actual invocation owner, Method, session and diagnostics context
   * @param fileHandle the input value; supplied null is retained
   * @param versionToUpdate the input value; supplied null is retained
   * @param targets the input value; supplied null is retained
   * @param revertAfterTime the input value; supplied null is retained
   * @param restartDelayTime the input value; supplied null is retained
   * @return a non-null complete operation outcome
   * @throws UaException for an operation failure
   */
  MethodHandlerResult<ConfigurationFileTypeCloseAndUpdateOutputs> invoke(
      AbstractMethodInvocationHandler.InvocationContext context,
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime)
      throws UaException;
}
