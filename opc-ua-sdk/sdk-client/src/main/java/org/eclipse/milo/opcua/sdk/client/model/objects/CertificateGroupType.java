package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the CertificateGroupType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.1">Model
 *     documentation</a>
 */
public interface CertificateGroupType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12555L);

  QualifiedProperty<NodeId[]> CertificateTypes_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CertificateTypes",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          1,
          NodeId[].class);

  QualifiedProperty<NodeId> Purpose_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Purpose",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  /**
   * Resolves the mandatory CertificateTypes child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getCertificateTypesNode() throws UaException;

  /** Asynchronous form of {@link #getCertificateTypesNode()}. */
  CompletableFuture<? extends PropertyType> getCertificateTypesNodeAsync();

  /**
   * Reads the Value of the CertificateTypes child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  NodeId @Nullable [] readCertificateTypes() throws UaException;

  /**
   * Writes the Value of the CertificateTypes child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCertificateTypes(NodeId @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readCertificateTypes()}. */
  CompletableFuture<? extends NodeId @Nullable []> readCertificateTypesAsync();

  /** Asynchronous form of {@link #writeCertificateTypes}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCertificateTypesAsync(NodeId @Nullable [] value);

  /**
   * Resolves the optional CertificateExpired child, a CertificateExpirationAlarmType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.7">CertificateExpirationAlarmType
   *     documentation</a>
   */
  @Nullable CertificateExpirationAlarmType getCertificateExpiredNode() throws UaException;

  /** Asynchronous form of {@link #getCertificateExpiredNode()}. */
  CompletableFuture<? extends @Nullable CertificateExpirationAlarmType>
      getCertificateExpiredNodeAsync();

  /**
   * Resolves the optional TrustListOutOfDate child, a TrustListOutOfDateAlarmType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.11">TrustListOutOfDateAlarmType
   *     documentation</a>
   */
  @Nullable TrustListOutOfDateAlarmType getTrustListOutOfDateNode() throws UaException;

  /** Asynchronous form of {@link #getTrustListOutOfDateNode()}. */
  CompletableFuture<? extends @Nullable TrustListOutOfDateAlarmType>
      getTrustListOutOfDateNodeAsync();

  /**
   * Resolves the optional Purpose child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getPurposeNode() throws UaException;

  /** Asynchronous form of {@link #getPurposeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getPurposeNodeAsync();

  /**
   * Reads the Value of the Purpose child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readPurpose() throws UaException;

  /**
   * Writes the Value of the Purpose child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePurpose(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readPurpose()}. */
  CompletableFuture<? extends @Nullable NodeId> readPurposeAsync();

  /** Asynchronous form of {@link #writePurpose}; completes with the operation status. */
  CompletableFuture<StatusCode> writePurposeAsync(@Nullable NodeId value);

  /**
   * Resolves the mandatory TrustList child, a TrustListType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.1">TrustListType
   *     documentation</a>
   */
  TrustListType getTrustListNode() throws UaException;

  /** Asynchronous form of {@link #getTrustListNode()}. */
  CompletableFuture<? extends TrustListType> getTrustListNodeAsync();

  /**
   * Resolves the optional GetRejectedList Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetRejectedListMethodNode() throws UaException;

  /** Asynchronous form of {@link #getGetRejectedListMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getGetRejectedListMethodNodeAsync();

  /**
   * Calls the GetRejectedList Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2">Model
   *     documentation</a>
   */
  ByteString @Nullable [] getRejectedList() throws UaException;

  /**
   * Calls the GetRejectedList Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<ByteString @Nullable []> callGetRejectedList() throws UaException;

  /**
   * Calls the GetRejectedList Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<ByteString @Nullable []> callGetRejectedListWith(MethodCallOptions options)
      throws UaException;

  /** Asynchronous form of {@link #getRejectedList}. */
  CompletableFuture<ByteString @Nullable []> getRejectedListAsync();

  /** Asynchronous form of {@link #callGetRejectedList}. */
  CompletableFuture<MethodCallResult<ByteString @Nullable []>> callGetRejectedListAsync();

  /** Asynchronous form of {@link #callGetRejectedListWith}. */
  CompletableFuture<MethodCallResult<ByteString @Nullable []>> callGetRejectedListWithAsync(
      MethodCallOptions options);
}
