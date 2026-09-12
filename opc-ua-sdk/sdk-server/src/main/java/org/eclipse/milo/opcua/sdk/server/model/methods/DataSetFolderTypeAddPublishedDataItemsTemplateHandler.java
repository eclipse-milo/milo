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

import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedDataItemsTemplateOutputs;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.jspecify.annotations.Nullable;

/**
 * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4
 *
 * <p>Synchronous returned callback for this Method. The invocation context retains the ObjectId,
 * Method, session and available request-wide diagnostic context. A supplied null input differs from
 * an omitted optional input.
 */
@FunctionalInterface
public interface DataSetFolderTypeAddPublishedDataItemsTemplateHandler {
  /**
   * Return all outputs together. Normal completion produces exact Good. Conversion failure occurs
   * after this callback and never causes an automatic retry.
   *
   * @param context the actual invocation owner, Method, session and diagnostics context
   * @param name the input value; supplied null is retained
   * @param dataSetMetaData the input value; supplied null is retained
   * @param variablesToAdd the input value; supplied null is retained
   * @return a non-null container holding all output values
   * @throws UaException for an operation failure
   */
  DataSetFolderTypeAddPublishedDataItemsTemplateOutputs invoke(
      AbstractMethodInvocationHandler.InvocationContext context,
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException;
}
