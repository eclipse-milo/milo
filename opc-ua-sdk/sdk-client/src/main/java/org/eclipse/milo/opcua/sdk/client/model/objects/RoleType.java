/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointType;
import org.eclipse.milo.opcua.stack.core.types.structured.IdentityMappingRuleType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.1</a>
 */
public interface RoleType extends BaseObjectType {
  QualifiedProperty<IdentityMappingRuleType[]> IDENTITIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Identities",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15634"),
          1,
          IdentityMappingRuleType[].class);

  QualifiedProperty<Boolean> APPLICATIONS_EXCLUDE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationsExclude",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<String[]> APPLICATIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Applications",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<Boolean> ENDPOINTS_EXCLUDE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EndpointsExclude",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<EndpointType[]> ENDPOINTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Endpoints",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15528"),
          1,
          EndpointType[].class);

  QualifiedProperty<Boolean> CUSTOM_CONFIGURATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CustomConfiguration",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /**
   * Get the local value of the Identities Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Identities Node.
   * @throws UaException if an error occurs creating or getting the Identities Node.
   */
  IdentityMappingRuleType[] getIdentities() throws UaException;

  /**
   * Set the local value of the Identities Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Identities Node.
   * @throws UaException if an error occurs creating or getting the Identities Node.
   */
  void setIdentities(IdentityMappingRuleType[] value) throws UaException;

  /**
   * Read the value of the Identities Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link IdentityMappingRuleType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  IdentityMappingRuleType[] readIdentities() throws UaException;

  /**
   * Write a new value for the Identities Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link IdentityMappingRuleType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeIdentities(IdentityMappingRuleType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readIdentities}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends IdentityMappingRuleType[]> readIdentitiesAsync();

  /**
   * An asynchronous implementation of {@link #writeIdentities}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeIdentitiesAsync(IdentityMappingRuleType[] value);

  /**
   * Get the Identities {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Identities {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getIdentitiesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIdentitiesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getIdentitiesNodeAsync();

  /**
   * Get the local value of the ApplicationsExclude Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ApplicationsExclude Node.
   * @throws UaException if an error occurs creating or getting the ApplicationsExclude Node.
   */
  Boolean getApplicationsExclude() throws UaException;

  /**
   * Set the local value of the ApplicationsExclude Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ApplicationsExclude Node.
   * @throws UaException if an error occurs creating or getting the ApplicationsExclude Node.
   */
  void setApplicationsExclude(Boolean value) throws UaException;

  /**
   * Read the value of the ApplicationsExclude Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readApplicationsExclude() throws UaException;

  /**
   * Write a new value for the ApplicationsExclude Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeApplicationsExclude(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readApplicationsExclude}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readApplicationsExcludeAsync();

  /**
   * An asynchronous implementation of {@link #writeApplicationsExclude}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeApplicationsExcludeAsync(Boolean value);

  /**
   * Get the ApplicationsExclude {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ApplicationsExclude {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getApplicationsExcludeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getApplicationsExcludeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getApplicationsExcludeNodeAsync();

  /**
   * Get the local value of the Applications Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Applications Node.
   * @throws UaException if an error occurs creating or getting the Applications Node.
   */
  String[] getApplications() throws UaException;

  /**
   * Set the local value of the Applications Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Applications Node.
   * @throws UaException if an error occurs creating or getting the Applications Node.
   */
  void setApplications(String[] value) throws UaException;

  /**
   * Read the value of the Applications Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readApplications() throws UaException;

  /**
   * Write a new value for the Applications Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeApplications(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readApplications}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readApplicationsAsync();

  /**
   * An asynchronous implementation of {@link #writeApplications}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeApplicationsAsync(String[] value);

  /**
   * Get the Applications {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Applications {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getApplicationsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getApplicationsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getApplicationsNodeAsync();

  /**
   * Get the local value of the EndpointsExclude Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EndpointsExclude Node.
   * @throws UaException if an error occurs creating or getting the EndpointsExclude Node.
   */
  Boolean getEndpointsExclude() throws UaException;

  /**
   * Set the local value of the EndpointsExclude Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EndpointsExclude Node.
   * @throws UaException if an error occurs creating or getting the EndpointsExclude Node.
   */
  void setEndpointsExclude(Boolean value) throws UaException;

  /**
   * Read the value of the EndpointsExclude Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readEndpointsExclude() throws UaException;

  /**
   * Write a new value for the EndpointsExclude Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEndpointsExclude(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEndpointsExclude}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readEndpointsExcludeAsync();

  /**
   * An asynchronous implementation of {@link #writeEndpointsExclude}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEndpointsExcludeAsync(Boolean value);

  /**
   * Get the EndpointsExclude {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EndpointsExclude {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEndpointsExcludeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEndpointsExcludeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEndpointsExcludeNodeAsync();

  /**
   * Get the local value of the Endpoints Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Endpoints Node.
   * @throws UaException if an error occurs creating or getting the Endpoints Node.
   */
  EndpointType[] getEndpoints() throws UaException;

  /**
   * Set the local value of the Endpoints Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Endpoints Node.
   * @throws UaException if an error occurs creating or getting the Endpoints Node.
   */
  void setEndpoints(EndpointType[] value) throws UaException;

  /**
   * Read the value of the Endpoints Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link EndpointType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  EndpointType[] readEndpoints() throws UaException;

  /**
   * Write a new value for the Endpoints Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link EndpointType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEndpoints(EndpointType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEndpoints}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends EndpointType[]> readEndpointsAsync();

  /**
   * An asynchronous implementation of {@link #writeEndpoints}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEndpointsAsync(EndpointType[] value);

  /**
   * Get the Endpoints {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Endpoints {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEndpointsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEndpointsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEndpointsNodeAsync();

  /**
   * Get the local value of the CustomConfiguration Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CustomConfiguration Node.
   * @throws UaException if an error occurs creating or getting the CustomConfiguration Node.
   */
  Boolean getCustomConfiguration() throws UaException;

  /**
   * Set the local value of the CustomConfiguration Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CustomConfiguration Node.
   * @throws UaException if an error occurs creating or getting the CustomConfiguration Node.
   */
  void setCustomConfiguration(Boolean value) throws UaException;

  /**
   * Read the value of the CustomConfiguration Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readCustomConfiguration() throws UaException;

  /**
   * Write a new value for the CustomConfiguration Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCustomConfiguration(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCustomConfiguration}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readCustomConfigurationAsync();

  /**
   * An asynchronous implementation of {@link #writeCustomConfiguration}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCustomConfigurationAsync(Boolean value);

  /**
   * Get the CustomConfiguration {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CustomConfiguration {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCustomConfigurationNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCustomConfigurationNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCustomConfigurationNodeAsync();
}
