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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the TransactionDiagnosticsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.17">Model
 *     documentation</a>
 */
public interface TransactionDiagnosticsType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32286L);

  QualifiedProperty<NodeId[]> AffectedTrustLists_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AffectedTrustLists",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          1,
          NodeId[].class);

  QualifiedProperty<NodeId[]> AffectedCertificateGroups_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AffectedCertificateGroups",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          1,
          NodeId[].class);

  QualifiedProperty<TransactionErrorType[]> Errors_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Errors",
          ExpandedNodeId.of(Namespaces.OPC_UA, 32285L),
          1,
          TransactionErrorType[].class);

  QualifiedProperty<StatusCode> Result_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Result",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19L),
          -1,
          StatusCode.class);

  QualifiedProperty<DateTime> EndTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EndTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> StartTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "StartTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  /**
   * Resolves the mandatory AffectedTrustLists child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getAffectedTrustListsNode() throws UaException;

  /** Asynchronous form of {@link #getAffectedTrustListsNode()}. */
  CompletableFuture<? extends PropertyType> getAffectedTrustListsNodeAsync();

  /**
   * Reads the Value of the AffectedTrustLists child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  NodeId @Nullable [] readAffectedTrustLists() throws UaException;

  /**
   * Writes the Value of the AffectedTrustLists child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAffectedTrustLists(NodeId @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readAffectedTrustLists()}. */
  CompletableFuture<? extends NodeId @Nullable []> readAffectedTrustListsAsync();

  /** Asynchronous form of {@link #writeAffectedTrustLists}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAffectedTrustListsAsync(NodeId @Nullable [] value);

  /**
   * Resolves the mandatory AffectedCertificateGroups child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getAffectedCertificateGroupsNode() throws UaException;

  /** Asynchronous form of {@link #getAffectedCertificateGroupsNode()}. */
  CompletableFuture<? extends PropertyType> getAffectedCertificateGroupsNodeAsync();

  /**
   * Reads the Value of the AffectedCertificateGroups child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  NodeId @Nullable [] readAffectedCertificateGroups() throws UaException;

  /**
   * Writes the Value of the AffectedCertificateGroups child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAffectedCertificateGroups(NodeId @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readAffectedCertificateGroups()}. */
  CompletableFuture<? extends NodeId @Nullable []> readAffectedCertificateGroupsAsync();

  /**
   * Asynchronous form of {@link #writeAffectedCertificateGroups}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeAffectedCertificateGroupsAsync(NodeId @Nullable [] value);

  /**
   * Resolves the mandatory Errors child, a PropertyType with DataType TransactionErrorType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getErrorsNode() throws UaException;

  /** Asynchronous form of {@link #getErrorsNode()}. */
  CompletableFuture<? extends PropertyType> getErrorsNodeAsync();

  /**
   * Reads the Value of the Errors child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable TransactionErrorType @Nullable [] readErrors() throws UaException;

  /**
   * Writes the Value of the Errors child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeErrors(@Nullable TransactionErrorType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readErrors()}. */
  CompletableFuture<? extends @Nullable TransactionErrorType @Nullable []> readErrorsAsync();

  /** Asynchronous form of {@link #writeErrors}; completes with the operation status. */
  CompletableFuture<StatusCode> writeErrorsAsync(@Nullable TransactionErrorType @Nullable [] value);

  /**
   * Resolves the mandatory Result child, a PropertyType with DataType StatusCode.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getResultNode() throws UaException;

  /** Asynchronous form of {@link #getResultNode()}. */
  CompletableFuture<? extends PropertyType> getResultNodeAsync();

  /**
   * Reads the Value of the Result child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable StatusCode readResult() throws UaException;

  /**
   * Writes the Value of the Result child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeResult(@Nullable StatusCode value) throws UaException;

  /** Asynchronous form of {@link #readResult()}. */
  CompletableFuture<? extends @Nullable StatusCode> readResultAsync();

  /** Asynchronous form of {@link #writeResult}; completes with the operation status. */
  CompletableFuture<StatusCode> writeResultAsync(@Nullable StatusCode value);

  /**
   * Resolves the mandatory EndTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEndTimeNode() throws UaException;

  /** Asynchronous form of {@link #getEndTimeNode()}. */
  CompletableFuture<? extends PropertyType> getEndTimeNodeAsync();

  /**
   * Reads the Value of the EndTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readEndTime() throws UaException;

  /**
   * Writes the Value of the EndTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEndTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readEndTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readEndTimeAsync();

  /** Asynchronous form of {@link #writeEndTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEndTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory StartTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getStartTimeNode() throws UaException;

  /** Asynchronous form of {@link #getStartTimeNode()}. */
  CompletableFuture<? extends PropertyType> getStartTimeNodeAsync();

  /**
   * Reads the Value of the StartTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readStartTime() throws UaException;

  /**
   * Writes the Value of the StartTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStartTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readStartTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readStartTimeAsync();

  /** Asynchronous form of {@link #writeStartTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStartTimeAsync(@Nullable DateTime value);
}
