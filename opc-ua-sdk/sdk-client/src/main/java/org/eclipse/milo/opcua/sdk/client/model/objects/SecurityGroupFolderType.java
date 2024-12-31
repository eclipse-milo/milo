/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.1</a>
 */
public interface SecurityGroupFolderType extends FolderType {
  QualifiedProperty<String[]> SUPPORTED_SECURITY_POLICY_URIS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportedSecurityPolicyUris",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  /**
   * Get the local value of the SupportedSecurityPolicyUris Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SupportedSecurityPolicyUris Node.
   * @throws UaException if an error occurs creating or getting the SupportedSecurityPolicyUris
   *     Node.
   */
  String[] getSupportedSecurityPolicyUris() throws UaException;

  /**
   * Set the local value of the SupportedSecurityPolicyUris Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SupportedSecurityPolicyUris Node.
   * @throws UaException if an error occurs creating or getting the SupportedSecurityPolicyUris
   *     Node.
   */
  void setSupportedSecurityPolicyUris(String[] value) throws UaException;

  /**
   * Read the value of the SupportedSecurityPolicyUris Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readSupportedSecurityPolicyUris() throws UaException;

  /**
   * Write a new value for the SupportedSecurityPolicyUris Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSupportedSecurityPolicyUris(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSupportedSecurityPolicyUris}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readSupportedSecurityPolicyUrisAsync();

  /**
   * An asynchronous implementation of {@link #writeSupportedSecurityPolicyUris}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSupportedSecurityPolicyUrisAsync(String[] value);

  /**
   * Get the SupportedSecurityPolicyUris {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SupportedSecurityPolicyUris {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSupportedSecurityPolicyUrisNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSupportedSecurityPolicyUrisNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSupportedSecurityPolicyUrisNodeAsync();
}
