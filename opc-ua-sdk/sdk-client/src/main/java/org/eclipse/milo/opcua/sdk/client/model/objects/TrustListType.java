package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListValidationOptions;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the TrustListType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.1">Model
 *     documentation</a>
 */
public interface TrustListType extends FileType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12522L);

  QualifiedProperty<DateTime> LastUpdateTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastUpdateTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<Double> ActivityTimeout_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ActivityTimeout",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<Double> UpdateFrequency_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UpdateFrequency",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<TrustListValidationOptions> DefaultValidationOptions_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DefaultValidationOptions",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23564L),
          -1,
          TrustListValidationOptions.class);

  /**
   * Resolves the mandatory LastUpdateTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLastUpdateTimeNode() throws UaException;

  /** Asynchronous form of {@link #getLastUpdateTimeNode()}. */
  CompletableFuture<? extends PropertyType> getLastUpdateTimeNodeAsync();

  /**
   * Reads the Value of the LastUpdateTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readLastUpdateTime() throws UaException;

  /**
   * Writes the Value of the LastUpdateTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastUpdateTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readLastUpdateTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readLastUpdateTimeAsync();

  /** Asynchronous form of {@link #writeLastUpdateTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLastUpdateTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the optional ActivityTimeout child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getActivityTimeoutNode() throws UaException;

  /** Asynchronous form of {@link #getActivityTimeoutNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getActivityTimeoutNodeAsync();

  /**
   * Reads the Value of the ActivityTimeout child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readActivityTimeout() throws UaException;

  /**
   * Writes the Value of the ActivityTimeout child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeActivityTimeout(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readActivityTimeout()}. */
  CompletableFuture<? extends @Nullable Double> readActivityTimeoutAsync();

  /** Asynchronous form of {@link #writeActivityTimeout}; completes with the operation status. */
  CompletableFuture<StatusCode> writeActivityTimeoutAsync(@Nullable Double value);

  /**
   * Resolves the optional UpdateFrequency child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getUpdateFrequencyNode() throws UaException;

  /** Asynchronous form of {@link #getUpdateFrequencyNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getUpdateFrequencyNodeAsync();

  /**
   * Reads the Value of the UpdateFrequency child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readUpdateFrequency() throws UaException;

  /**
   * Writes the Value of the UpdateFrequency child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUpdateFrequency(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readUpdateFrequency()}. */
  CompletableFuture<? extends @Nullable Double> readUpdateFrequencyAsync();

  /** Asynchronous form of {@link #writeUpdateFrequency}; completes with the operation status. */
  CompletableFuture<StatusCode> writeUpdateFrequencyAsync(@Nullable Double value);

  /**
   * Resolves the optional DefaultValidationOptions child, a PropertyType with DataType
   * TrustListValidationOptions.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDefaultValidationOptionsNode() throws UaException;

  /** Asynchronous form of {@link #getDefaultValidationOptionsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDefaultValidationOptionsNodeAsync();

  /**
   * Reads the Value of the DefaultValidationOptions child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable TrustListValidationOptions readDefaultValidationOptions() throws UaException;

  /**
   * Writes the Value of the DefaultValidationOptions child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDefaultValidationOptions(@Nullable TrustListValidationOptions value) throws UaException;

  /** Asynchronous form of {@link #readDefaultValidationOptions()}. */
  CompletableFuture<? extends @Nullable TrustListValidationOptions>
      readDefaultValidationOptionsAsync();

  /**
   * Asynchronous form of {@link #writeDefaultValidationOptions}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDefaultValidationOptionsAsync(
      @Nullable TrustListValidationOptions value);

  /**
   * Resolves the mandatory AddCertificate Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6">Model
   *     documentation</a>
   */
  UaMethodNode getAddCertificateMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddCertificateMethodNode()}. */
  CompletableFuture<UaMethodNode> getAddCertificateMethodNodeAsync();

  /**
   * Calls the AddCertificate Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6">Model
   *     documentation</a>
   */
  void addCertificate(@Nullable ByteString certificate, @Nullable Boolean isTrustedCertificate)
      throws UaException;

  /**
   * Calls the AddCertificate Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddCertificate(
      @Nullable ByteString certificate, @Nullable Boolean isTrustedCertificate) throws UaException;

  /**
   * Calls the AddCertificate Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddCertificateWith(
      MethodCallOptions options,
      @Nullable ByteString certificate,
      @Nullable Boolean isTrustedCertificate)
      throws UaException;

  /** Asynchronous form of {@link #addCertificate}. */
  CompletableFuture<Void> addCertificateAsync(
      @Nullable ByteString certificate, @Nullable Boolean isTrustedCertificate);

  /** Asynchronous form of {@link #callAddCertificate}. */
  CompletableFuture<MethodCallResult<Void>> callAddCertificateAsync(
      @Nullable ByteString certificate, @Nullable Boolean isTrustedCertificate);

  /** Asynchronous form of {@link #callAddCertificateWith}. */
  CompletableFuture<MethodCallResult<Void>> callAddCertificateWithAsync(
      MethodCallOptions options,
      @Nullable ByteString certificate,
      @Nullable Boolean isTrustedCertificate);

  /**
   * Resolves the mandatory CloseAndUpdate Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5">Model
   *     documentation</a>
   */
  UaMethodNode getCloseAndUpdateMethodNode() throws UaException;

  /** Asynchronous form of {@link #getCloseAndUpdateMethodNode()}. */
  CompletableFuture<UaMethodNode> getCloseAndUpdateMethodNodeAsync();

  /**
   * Calls the CloseAndUpdate Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5">Model
   *     documentation</a>
   */
  @Nullable Boolean closeAndUpdate(@Nullable UInteger fileHandle) throws UaException;

  /**
   * Calls the CloseAndUpdate Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable Boolean> callCloseAndUpdate(@Nullable UInteger fileHandle)
      throws UaException;

  /**
   * Calls the CloseAndUpdate Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable Boolean> callCloseAndUpdateWith(
      MethodCallOptions options, @Nullable UInteger fileHandle) throws UaException;

  /** Asynchronous form of {@link #closeAndUpdate}. */
  CompletableFuture<@Nullable Boolean> closeAndUpdateAsync(@Nullable UInteger fileHandle);

  /** Asynchronous form of {@link #callCloseAndUpdate}. */
  CompletableFuture<MethodCallResult<@Nullable Boolean>> callCloseAndUpdateAsync(
      @Nullable UInteger fileHandle);

  /** Asynchronous form of {@link #callCloseAndUpdateWith}. */
  CompletableFuture<MethodCallResult<@Nullable Boolean>> callCloseAndUpdateWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle);

  /**
   * Resolves the mandatory OpenWithMasks Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3">Model
   *     documentation</a>
   */
  UaMethodNode getOpenWithMasksMethodNode() throws UaException;

  /** Asynchronous form of {@link #getOpenWithMasksMethodNode()}. */
  CompletableFuture<UaMethodNode> getOpenWithMasksMethodNodeAsync();

  /**
   * Calls the OpenWithMasks Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3">Model
   *     documentation</a>
   */
  @Nullable UInteger openWithMasks(@Nullable UInteger masks) throws UaException;

  /**
   * Calls the OpenWithMasks Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable UInteger> callOpenWithMasks(@Nullable UInteger masks)
      throws UaException;

  /**
   * Calls the OpenWithMasks Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable UInteger> callOpenWithMasksWith(
      MethodCallOptions options, @Nullable UInteger masks) throws UaException;

  /** Asynchronous form of {@link #openWithMasks}. */
  CompletableFuture<@Nullable UInteger> openWithMasksAsync(@Nullable UInteger masks);

  /** Asynchronous form of {@link #callOpenWithMasks}. */
  CompletableFuture<MethodCallResult<@Nullable UInteger>> callOpenWithMasksAsync(
      @Nullable UInteger masks);

  /** Asynchronous form of {@link #callOpenWithMasksWith}. */
  CompletableFuture<MethodCallResult<@Nullable UInteger>> callOpenWithMasksWithAsync(
      MethodCallOptions options, @Nullable UInteger masks);

  /**
   * Resolves the mandatory RemoveCertificate Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7">Model
   *     documentation</a>
   */
  UaMethodNode getRemoveCertificateMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveCertificateMethodNode()}. */
  CompletableFuture<UaMethodNode> getRemoveCertificateMethodNodeAsync();

  /**
   * Calls the RemoveCertificate Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7">Model
   *     documentation</a>
   */
  void removeCertificate(@Nullable String thumbprint, @Nullable Boolean isTrustedCertificate)
      throws UaException;

  /**
   * Calls the RemoveCertificate Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveCertificate(
      @Nullable String thumbprint, @Nullable Boolean isTrustedCertificate) throws UaException;

  /**
   * Calls the RemoveCertificate Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveCertificateWith(
      MethodCallOptions options,
      @Nullable String thumbprint,
      @Nullable Boolean isTrustedCertificate)
      throws UaException;

  /** Asynchronous form of {@link #removeCertificate}. */
  CompletableFuture<Void> removeCertificateAsync(
      @Nullable String thumbprint, @Nullable Boolean isTrustedCertificate);

  /** Asynchronous form of {@link #callRemoveCertificate}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveCertificateAsync(
      @Nullable String thumbprint, @Nullable Boolean isTrustedCertificate);

  /** Asynchronous form of {@link #callRemoveCertificateWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveCertificateWithAsync(
      MethodCallOptions options,
      @Nullable String thumbprint,
      @Nullable Boolean isTrustedCertificate);
}
