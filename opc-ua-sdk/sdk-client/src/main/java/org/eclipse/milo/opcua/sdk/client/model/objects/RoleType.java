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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointType;
import org.eclipse.milo.opcua.stack.core.types.structured.IdentityMappingRuleType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable IdentityMappingRuleType @Nullable [] getIdentities() throws UaException;

  /** Sets the existing node's local value. */
  void setIdentities(@Nullable IdentityMappingRuleType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable IdentityMappingRuleType @Nullable [] readIdentities() throws UaException;

  /** Writes the value remotely. */
  void writeIdentities(@Nullable IdentityMappingRuleType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable IdentityMappingRuleType @Nullable []> readIdentitiesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIdentitiesAsync(
      @Nullable IdentityMappingRuleType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIdentitiesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getIdentitiesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getApplicationsExclude() throws UaException;

  /** Sets the existing node's local value. */
  void setApplicationsExclude(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readApplicationsExclude() throws UaException;

  /** Writes the value remotely. */
  void writeApplicationsExclude(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readApplicationsExcludeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeApplicationsExcludeAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getApplicationsExcludeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getApplicationsExcludeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getApplications() throws UaException;

  /** Sets the existing node's local value. */
  void setApplications(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readApplications() throws UaException;

  /** Writes the value remotely. */
  void writeApplications(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readApplicationsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeApplicationsAsync(@Nullable String @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getApplicationsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getApplicationsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getEndpointsExclude() throws UaException;

  /** Sets the existing node's local value. */
  void setEndpointsExclude(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readEndpointsExclude() throws UaException;

  /** Writes the value remotely. */
  void writeEndpointsExclude(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readEndpointsExcludeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEndpointsExcludeAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEndpointsExcludeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getEndpointsExcludeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable EndpointType @Nullable [] getEndpoints() throws UaException;

  /** Sets the existing node's local value. */
  void setEndpoints(@Nullable EndpointType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable EndpointType @Nullable [] readEndpoints() throws UaException;

  /** Writes the value remotely. */
  void writeEndpoints(@Nullable EndpointType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable EndpointType @Nullable []> readEndpointsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEndpointsAsync(@Nullable EndpointType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEndpointsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getEndpointsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getCustomConfiguration() throws UaException;

  /** Sets the existing node's local value. */
  void setCustomConfiguration(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readCustomConfiguration() throws UaException;

  /** Writes the value remotely. */
  void writeCustomConfiguration(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readCustomConfigurationAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCustomConfigurationAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCustomConfigurationNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getCustomConfigurationNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddIdentityMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddIdentityMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5
   *
   * <p>Invokes <code>AddIdentity</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callAddIdentity(@Nullable IdentityMappingRuleType rule) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5
   *
   * <p>Invokes <code>AddIdentity</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callAddIdentityAsync(
      @Nullable IdentityMappingRuleType rule);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5
   *
   * <p>Invokes <code>AddIdentity</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddIdentityDetailed(
      @Nullable IdentityMappingRuleType rule) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5
   *
   * <p>Invokes <code>AddIdentity</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddIdentityDetailed(
      MethodCallOptions options, @Nullable IdentityMappingRuleType rule) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5
   *
   * <p>Invokes <code>AddIdentity</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddIdentityDetailedAsync(@Nullable IdentityMappingRuleType rule);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5
   *
   * <p>Invokes <code>AddIdentity</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddIdentityDetailedAsync(
          MethodCallOptions options, @Nullable IdentityMappingRuleType rule);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRemoveIdentityMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemoveIdentityMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6
   *
   * <p>Invokes <code>RemoveIdentity</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveIdentity(@Nullable IdentityMappingRuleType rule) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6
   *
   * <p>Invokes <code>RemoveIdentity</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveIdentityAsync(
      @Nullable IdentityMappingRuleType rule);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6
   *
   * <p>Invokes <code>RemoveIdentity</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveIdentityDetailed(
      @Nullable IdentityMappingRuleType rule) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6
   *
   * <p>Invokes <code>RemoveIdentity</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveIdentityDetailed(
      MethodCallOptions options, @Nullable IdentityMappingRuleType rule) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6
   *
   * <p>Invokes <code>RemoveIdentity</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveIdentityDetailedAsync(@Nullable IdentityMappingRuleType rule);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6
   *
   * <p>Invokes <code>RemoveIdentity</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveIdentityDetailedAsync(
          MethodCallOptions options, @Nullable IdentityMappingRuleType rule);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddApplicationMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddApplicationMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7
   *
   * <p>Invokes <code>AddApplication</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callAddApplication(@Nullable String applicationUri) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7
   *
   * <p>Invokes <code>AddApplication</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callAddApplicationAsync(
      @Nullable String applicationUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7
   *
   * <p>Invokes <code>AddApplication</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddApplicationDetailed(
      @Nullable String applicationUri) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7
   *
   * <p>Invokes <code>AddApplication</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddApplicationDetailed(
      MethodCallOptions options, @Nullable String applicationUri) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7
   *
   * <p>Invokes <code>AddApplication</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddApplicationDetailedAsync(@Nullable String applicationUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7
   *
   * <p>Invokes <code>AddApplication</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddApplicationDetailedAsync(MethodCallOptions options, @Nullable String applicationUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRemoveApplicationMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemoveApplicationMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8
   *
   * <p>Invokes <code>RemoveApplication</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveApplication(@Nullable String applicationUri) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8
   *
   * <p>Invokes <code>RemoveApplication</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveApplicationAsync(
      @Nullable String applicationUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8
   *
   * <p>Invokes <code>RemoveApplication</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveApplicationDetailed(
      @Nullable String applicationUri) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8
   *
   * <p>Invokes <code>RemoveApplication</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveApplicationDetailed(
      MethodCallOptions options, @Nullable String applicationUri) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8
   *
   * <p>Invokes <code>RemoveApplication</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveApplicationDetailedAsync(@Nullable String applicationUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8
   *
   * <p>Invokes <code>RemoveApplication</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveApplicationDetailedAsync(
          MethodCallOptions options, @Nullable String applicationUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddEndpointMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddEndpointMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9
   *
   * <p>Invokes <code>AddEndpoint</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callAddEndpoint(@Nullable EndpointType endpoint) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9
   *
   * <p>Invokes <code>AddEndpoint</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callAddEndpointAsync(@Nullable EndpointType endpoint);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9
   *
   * <p>Invokes <code>AddEndpoint</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddEndpointDetailed(
      @Nullable EndpointType endpoint) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9
   *
   * <p>Invokes <code>AddEndpoint</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddEndpointDetailed(
      MethodCallOptions options, @Nullable EndpointType endpoint) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9
   *
   * <p>Invokes <code>AddEndpoint</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddEndpointDetailedAsync(@Nullable EndpointType endpoint);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9
   *
   * <p>Invokes <code>AddEndpoint</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddEndpointDetailedAsync(MethodCallOptions options, @Nullable EndpointType endpoint);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRemoveEndpointMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemoveEndpointMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10
   *
   * <p>Invokes <code>RemoveEndpoint</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveEndpoint(@Nullable EndpointType endpoint) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10
   *
   * <p>Invokes <code>RemoveEndpoint</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveEndpointAsync(
      @Nullable EndpointType endpoint);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10
   *
   * <p>Invokes <code>RemoveEndpoint</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveEndpointDetailed(
      @Nullable EndpointType endpoint) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10
   *
   * <p>Invokes <code>RemoveEndpoint</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveEndpointDetailed(
      MethodCallOptions options, @Nullable EndpointType endpoint) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10
   *
   * <p>Invokes <code>RemoveEndpoint</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveEndpointDetailedAsync(@Nullable EndpointType endpoint);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10
   *
   * <p>Invokes <code>RemoveEndpoint</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveEndpointDetailedAsync(MethodCallOptions options, @Nullable EndpointType endpoint);
}
