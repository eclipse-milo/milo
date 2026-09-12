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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.TransactionErrorType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.17">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.17</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TransactionDiagnosticsType extends BaseObjectType {
  QualifiedProperty<DateTime> START_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StartTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> END_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EndTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<StatusCode> RESULT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Result",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19"),
          -1,
          StatusCode.class);

  QualifiedProperty<NodeId[]> AFFECTED_TRUST_LISTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AffectedTrustLists",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<NodeId[]> AFFECTED_CERTIFICATE_GROUPS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AffectedCertificateGroups",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<TransactionErrorType[]> ERRORS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Errors",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32285"),
          1,
          TransactionErrorType[].class);

  /** Gets the existing node's local value. */
  @Nullable DateTime getStartTime() throws UaException;

  /** Sets the existing node's local value. */
  void setStartTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readStartTime() throws UaException;

  /** Writes the value remotely. */
  void writeStartTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readStartTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStartTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStartTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getStartTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getEndTime() throws UaException;

  /** Sets the existing node's local value. */
  void setEndTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readEndTime() throws UaException;

  /** Writes the value remotely. */
  void writeEndTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readEndTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEndTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEndTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEndTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable StatusCode getResult() throws UaException;

  /** Sets the existing node's local value. */
  void setResult(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable StatusCode readResult() throws UaException;

  /** Writes the value remotely. */
  void writeResult(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable StatusCode> readResultAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeResultAsync(@Nullable StatusCode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getResultNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getResultNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getAffectedTrustLists() throws UaException;

  /** Sets the existing node's local value. */
  void setAffectedTrustLists(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId @Nullable [] readAffectedTrustLists() throws UaException;

  /** Writes the value remotely. */
  void writeAffectedTrustLists(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId @Nullable []> readAffectedTrustListsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAffectedTrustListsAsync(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAffectedTrustListsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getAffectedTrustListsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getAffectedCertificateGroups() throws UaException;

  /** Sets the existing node's local value. */
  void setAffectedCertificateGroups(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId @Nullable [] readAffectedCertificateGroups() throws UaException;

  /** Writes the value remotely. */
  void writeAffectedCertificateGroups(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId @Nullable []> readAffectedCertificateGroupsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAffectedCertificateGroupsAsync(
      @Nullable NodeId @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAffectedCertificateGroupsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getAffectedCertificateGroupsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable TransactionErrorType @Nullable [] getErrors() throws UaException;

  /** Sets the existing node's local value. */
  void setErrors(@Nullable TransactionErrorType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable TransactionErrorType @Nullable [] readErrors() throws UaException;

  /** Writes the value remotely. */
  void writeErrors(@Nullable TransactionErrorType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable TransactionErrorType @Nullable []> readErrorsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeErrorsAsync(@Nullable TransactionErrorType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getErrorsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getErrorsNodeAsync();
}
