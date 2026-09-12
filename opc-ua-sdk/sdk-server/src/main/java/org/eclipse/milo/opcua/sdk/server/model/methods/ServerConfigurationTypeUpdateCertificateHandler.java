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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.5
 *
 * <p>Synchronous returned callback for this Method. The invocation context retains the ObjectId,
 * Method, session and available request-wide diagnostic context. A supplied null input differs from
 * an omitted optional input.
 */
@FunctionalInterface
public interface ServerConfigurationTypeUpdateCertificateHandler {
  /**
   * Return all outputs together. Normal completion produces exact Good. Conversion failure occurs
   * after this callback and never causes an automatic retry.
   *
   * @param context the actual invocation owner, Method, session and diagnostics context
   * @param certificateGroupId the input value; supplied null is retained
   * @param certificateTypeId the input value; supplied null is retained
   * @param certificate the input value; supplied null is retained
   * @param issuerCertificates the input value; supplied null is retained
   * @param privateKeyFormat the input value; supplied null is retained
   * @param privateKey the input value; supplied null is retained
   * @return the output value, including null
   * @throws UaException for an operation failure
   */
  @Nullable Boolean invoke(
      AbstractMethodInvocationHandler.InvocationContext context,
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable ByteString certificate,
      @Nullable ByteString @Nullable [] issuerCertificates,
      @Nullable String privateKeyFormat,
      @Nullable ByteString privateKey)
      throws UaException;
}
