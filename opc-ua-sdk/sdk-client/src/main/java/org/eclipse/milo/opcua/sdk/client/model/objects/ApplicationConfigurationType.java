/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.14">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.14</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ApplicationConfigurationType extends ServerConfigurationType {
  QualifiedProperty<String> APPLICATION_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  QualifiedProperty<String> PRODUCT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ProductUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  QualifiedProperty<ApplicationType> APPLICATION_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=307"),
          -1,
          ApplicationType.class);

  QualifiedProperty<Boolean> ENABLED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Enabled",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> IS_NON_UA_APPLICATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IsNonUaApplication",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable String getApplicationUri() throws UaException;

  /** Sets the existing node's local value. */
  void setApplicationUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readApplicationUri() throws UaException;

  /** Writes the value remotely. */
  void writeApplicationUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readApplicationUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeApplicationUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getApplicationUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getApplicationUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getProductUri() throws UaException;

  /** Sets the existing node's local value. */
  void setProductUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readProductUri() throws UaException;

  /** Writes the value remotely. */
  void writeProductUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readProductUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeProductUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getProductUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getProductUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ApplicationType getApplicationType() throws UaException;

  /** Sets the existing node's local value. */
  void setApplicationType(@Nullable ApplicationType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ApplicationType readApplicationType() throws UaException;

  /** Writes the value remotely. */
  void writeApplicationType(@Nullable ApplicationType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ApplicationType> readApplicationTypeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeApplicationTypeAsync(@Nullable ApplicationType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getApplicationTypeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getApplicationTypeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getEnabled() throws UaException;

  /** Sets the existing node's local value. */
  void setEnabled(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readEnabled() throws UaException;

  /** Writes the value remotely. */
  void writeEnabled(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readEnabledAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEnabledAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEnabledNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEnabledNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getIsNonUaApplication() throws UaException;

  /** Sets the existing node's local value. */
  void setIsNonUaApplication(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readIsNonUaApplication() throws UaException;

  /** Writes the value remotely. */
  void writeIsNonUaApplication(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readIsNonUaApplicationAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIsNonUaApplicationAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getIsNonUaApplicationNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getIsNonUaApplicationNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable KeyCredentialConfigurationFolderType getKeyCredentialsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable KeyCredentialConfigurationFolderType>
      getKeyCredentialsNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable AuthorizationServicesConfigurationFolderType getAuthorizationServicesNode()
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable AuthorizationServicesConfigurationFolderType>
      getAuthorizationServicesNodeAsync();
}
