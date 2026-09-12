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
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.3">https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ProvisionableDeviceType extends BaseObjectType {
  QualifiedProperty<Boolean> IS_SINGLETON =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IsSingleton",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable Boolean getIsSingleton() throws UaException;

  /** Sets the existing node's local value. */
  void setIsSingleton(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readIsSingleton() throws UaException;

  /** Writes the value remotely. */
  void writeIsSingleton(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readIsSingletonAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIsSingletonAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIsSingletonNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getIsSingletonNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getRequestTicketsMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getRequestTicketsMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4
   *
   * <p>Invokes <code>RequestTickets</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable String @Nullable [] callRequestTickets() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4
   *
   * <p>Invokes <code>RequestTickets</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable String @Nullable []> callRequestTicketsAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4
   *
   * <p>Invokes <code>RequestTickets</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable String @Nullable []> callRequestTicketsDetailed()
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4
   *
   * <p>Invokes <code>RequestTickets</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable String @Nullable []> callRequestTicketsDetailed(
      MethodCallOptions options) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4
   *
   * <p>Invokes <code>RequestTickets</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable String @Nullable []>>
      callRequestTicketsDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.4
   *
   * <p>Invokes <code>RequestTickets</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable String @Nullable []>>
      callRequestTicketsDetailedAsync(MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getSetRegistrarEndpointsMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getSetRegistrarEndpointsMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5
   *
   * <p>Invokes <code>SetRegistrarEndpoints</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callSetRegistrarEndpoints(@Nullable ApplicationDescription @Nullable [] registrars)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5
   *
   * <p>Invokes <code>SetRegistrarEndpoints</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callSetRegistrarEndpointsAsync(
      @Nullable ApplicationDescription @Nullable [] registrars);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5
   *
   * <p>Invokes <code>SetRegistrarEndpoints</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callSetRegistrarEndpointsDetailed(
      @Nullable ApplicationDescription @Nullable [] registrars) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5
   *
   * <p>Invokes <code>SetRegistrarEndpoints</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callSetRegistrarEndpointsDetailed(
      MethodCallOptions options, @Nullable ApplicationDescription @Nullable [] registrars)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5
   *
   * <p>Invokes <code>SetRegistrarEndpoints</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callSetRegistrarEndpointsDetailedAsync(
          @Nullable ApplicationDescription @Nullable [] registrars);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.5
   *
   * <p>Invokes <code>SetRegistrarEndpoints</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callSetRegistrarEndpointsDetailedAsync(
          MethodCallOptions options, @Nullable ApplicationDescription @Nullable [] registrars);
}
