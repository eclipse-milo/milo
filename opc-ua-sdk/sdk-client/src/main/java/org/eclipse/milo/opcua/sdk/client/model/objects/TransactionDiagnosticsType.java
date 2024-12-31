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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.TransactionErrorType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11</a>
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

  /**
   * Get the local value of the StartTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the StartTime Node.
   * @throws UaException if an error occurs creating or getting the StartTime Node.
   */
  DateTime getStartTime() throws UaException;

  /**
   * Set the local value of the StartTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the StartTime Node.
   * @throws UaException if an error occurs creating or getting the StartTime Node.
   */
  void setStartTime(DateTime value) throws UaException;

  /**
   * Read the value of the StartTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readStartTime() throws UaException;

  /**
   * Write a new value for the StartTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStartTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStartTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readStartTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeStartTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStartTimeAsync(DateTime value);

  /**
   * Get the StartTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the StartTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getStartTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStartTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getStartTimeNodeAsync();

  /**
   * Get the local value of the EndTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EndTime Node.
   * @throws UaException if an error occurs creating or getting the EndTime Node.
   */
  DateTime getEndTime() throws UaException;

  /**
   * Set the local value of the EndTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EndTime Node.
   * @throws UaException if an error occurs creating or getting the EndTime Node.
   */
  void setEndTime(DateTime value) throws UaException;

  /**
   * Read the value of the EndTime Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readEndTime() throws UaException;

  /**
   * Write a new value for the EndTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEndTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEndTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readEndTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeEndTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEndTimeAsync(DateTime value);

  /**
   * Get the EndTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EndTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getEndTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEndTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getEndTimeNodeAsync();

  /**
   * Get the local value of the Result Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Result Node.
   * @throws UaException if an error occurs creating or getting the Result Node.
   */
  StatusCode getResult() throws UaException;

  /**
   * Set the local value of the Result Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Result Node.
   * @throws UaException if an error occurs creating or getting the Result Node.
   */
  void setResult(StatusCode value) throws UaException;

  /**
   * Read the value of the Result Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link StatusCode} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  StatusCode readResult() throws UaException;

  /**
   * Write a new value for the Result Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link StatusCode} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeResult(StatusCode value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readResult}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends StatusCode> readResultAsync();

  /**
   * An asynchronous implementation of {@link #writeResult}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeResultAsync(StatusCode value);

  /**
   * Get the Result {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Result {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getResultNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getResultNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getResultNodeAsync();

  /**
   * Get the local value of the AffectedTrustLists Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AffectedTrustLists Node.
   * @throws UaException if an error occurs creating or getting the AffectedTrustLists Node.
   */
  NodeId[] getAffectedTrustLists() throws UaException;

  /**
   * Set the local value of the AffectedTrustLists Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AffectedTrustLists Node.
   * @throws UaException if an error occurs creating or getting the AffectedTrustLists Node.
   */
  void setAffectedTrustLists(NodeId[] value) throws UaException;

  /**
   * Read the value of the AffectedTrustLists Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId[] readAffectedTrustLists() throws UaException;

  /**
   * Write a new value for the AffectedTrustLists Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link NodeId[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAffectedTrustLists(NodeId[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAffectedTrustLists}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId[]> readAffectedTrustListsAsync();

  /**
   * An asynchronous implementation of {@link #writeAffectedTrustLists}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAffectedTrustListsAsync(NodeId[] value);

  /**
   * Get the AffectedTrustLists {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AffectedTrustLists {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getAffectedTrustListsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAffectedTrustListsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getAffectedTrustListsNodeAsync();

  /**
   * Get the local value of the AffectedCertificateGroups Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AffectedCertificateGroups Node.
   * @throws UaException if an error occurs creating or getting the AffectedCertificateGroups Node.
   */
  NodeId[] getAffectedCertificateGroups() throws UaException;

  /**
   * Set the local value of the AffectedCertificateGroups Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AffectedCertificateGroups Node.
   * @throws UaException if an error occurs creating or getting the AffectedCertificateGroups Node.
   */
  void setAffectedCertificateGroups(NodeId[] value) throws UaException;

  /**
   * Read the value of the AffectedCertificateGroups Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link NodeId[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId[] readAffectedCertificateGroups() throws UaException;

  /**
   * Write a new value for the AffectedCertificateGroups Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link NodeId[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAffectedCertificateGroups(NodeId[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAffectedCertificateGroups}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId[]> readAffectedCertificateGroupsAsync();

  /**
   * An asynchronous implementation of {@link #writeAffectedCertificateGroups}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAffectedCertificateGroupsAsync(NodeId[] value);

  /**
   * Get the AffectedCertificateGroups {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AffectedCertificateGroups {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getAffectedCertificateGroupsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAffectedCertificateGroupsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getAffectedCertificateGroupsNodeAsync();

  /**
   * Get the local value of the Errors Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Errors Node.
   * @throws UaException if an error occurs creating or getting the Errors Node.
   */
  TransactionErrorType[] getErrors() throws UaException;

  /**
   * Set the local value of the Errors Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Errors Node.
   * @throws UaException if an error occurs creating or getting the Errors Node.
   */
  void setErrors(TransactionErrorType[] value) throws UaException;

  /**
   * Read the value of the Errors Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link TransactionErrorType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  TransactionErrorType[] readErrors() throws UaException;

  /**
   * Write a new value for the Errors Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link TransactionErrorType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeErrors(TransactionErrorType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readErrors}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends TransactionErrorType[]> readErrorsAsync();

  /**
   * An asynchronous implementation of {@link #writeErrors}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeErrorsAsync(TransactionErrorType[] value);

  /**
   * Get the Errors {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Errors {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getErrorsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getErrorsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getErrorsNodeAsync();
}
