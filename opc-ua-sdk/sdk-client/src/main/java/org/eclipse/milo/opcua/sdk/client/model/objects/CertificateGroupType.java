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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface CertificateGroupType extends BaseObjectType {
  QualifiedProperty<NodeId[]> CERTIFICATE_TYPES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateTypes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<NodeId> PURPOSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Purpose",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getCertificateTypes() throws UaException;

  /** Sets the existing node's local value. */
  void setCertificateTypes(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId @Nullable [] readCertificateTypes() throws UaException;

  /** Writes the value remotely. */
  void writeCertificateTypes(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId @Nullable []> readCertificateTypesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCertificateTypesAsync(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCertificateTypesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getCertificateTypesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getPurpose() throws UaException;

  /** Sets the existing node's local value. */
  void setPurpose(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readPurpose() throws UaException;

  /** Writes the value remotely. */
  void writePurpose(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readPurposeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePurposeAsync(@Nullable NodeId value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getPurposeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getPurposeNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TrustListType getTrustListNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TrustListType> getTrustListNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable CertificateExpirationAlarmType getCertificateExpiredNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable CertificateExpirationAlarmType>
      getCertificateExpiredNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TrustListOutOfDateAlarmType getTrustListOutOfDateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable TrustListOutOfDateAlarmType>
      getTrustListOutOfDateNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getGetRejectedListMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getGetRejectedListMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2
   *
   * <p>Invokes <code>GetRejectedList</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable ByteString @Nullable [] callGetRejectedList() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2
   *
   * <p>Invokes <code>GetRejectedList</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable ByteString @Nullable []> callGetRejectedListAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2
   *
   * <p>Invokes <code>GetRejectedList</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ByteString @Nullable []> callGetRejectedListDetailed()
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2
   *
   * <p>Invokes <code>GetRejectedList</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ByteString @Nullable []> callGetRejectedListDetailed(
      MethodCallOptions options) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2
   *
   * <p>Invokes <code>GetRejectedList</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ByteString @Nullable []>>
      callGetRejectedListDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2
   *
   * <p>Invokes <code>GetRejectedList</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ByteString @Nullable []>>
      callGetRejectedListDetailedAsync(MethodCallOptions options);
}
