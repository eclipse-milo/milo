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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListValidationOptions;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TrustListType extends FileType {
  QualifiedProperty<DateTime> LAST_UPDATE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastUpdateTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<Double> UPDATE_FREQUENCY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UpdateFrequency",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> ACTIVITY_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ActivityTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<TrustListValidationOptions> DEFAULT_VALIDATION_OPTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultValidationOptions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23564"),
          -1,
          TrustListValidationOptions.class);

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastUpdateTime() throws UaException;

  /** Sets the existing node's local value. */
  void setLastUpdateTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readLastUpdateTime() throws UaException;

  /** Writes the value remotely. */
  void writeLastUpdateTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readLastUpdateTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastUpdateTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastUpdateTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLastUpdateTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getUpdateFrequency() throws UaException;

  /** Sets the existing node's local value. */
  void setUpdateFrequency(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readUpdateFrequency() throws UaException;

  /** Writes the value remotely. */
  void writeUpdateFrequency(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readUpdateFrequencyAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUpdateFrequencyAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getUpdateFrequencyNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getUpdateFrequencyNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getActivityTimeout() throws UaException;

  /** Sets the existing node's local value. */
  void setActivityTimeout(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readActivityTimeout() throws UaException;

  /** Writes the value remotely. */
  void writeActivityTimeout(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readActivityTimeoutAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeActivityTimeoutAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getActivityTimeoutNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getActivityTimeoutNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable TrustListValidationOptions getDefaultValidationOptions() throws UaException;

  /** Sets the existing node's local value. */
  void setDefaultValidationOptions(@Nullable TrustListValidationOptions value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable TrustListValidationOptions readDefaultValidationOptions() throws UaException;

  /** Writes the value remotely. */
  void writeDefaultValidationOptions(@Nullable TrustListValidationOptions value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable TrustListValidationOptions>
      readDefaultValidationOptionsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDefaultValidationOptionsAsync(
      @Nullable TrustListValidationOptions value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefaultValidationOptionsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDefaultValidationOptionsNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getOpenWithMasksMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getOpenWithMasksMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3
   *
   * <p>Invokes <code>OpenWithMasks</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable UInteger callOpenWithMasks(@Nullable UInteger masks) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3
   *
   * <p>Invokes <code>OpenWithMasks</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UInteger> callOpenWithMasksAsync(@Nullable UInteger masks);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3
   *
   * <p>Invokes <code>OpenWithMasks</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable UInteger> callOpenWithMasksDetailed(@Nullable UInteger masks)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3
   *
   * <p>Invokes <code>OpenWithMasks</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable UInteger> callOpenWithMasksDetailed(
      MethodCallOptions options, @Nullable UInteger masks) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3
   *
   * <p>Invokes <code>OpenWithMasks</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable UInteger>>
      callOpenWithMasksDetailedAsync(@Nullable UInteger masks);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.3
   *
   * <p>Invokes <code>OpenWithMasks</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable UInteger>>
      callOpenWithMasksDetailedAsync(MethodCallOptions options, @Nullable UInteger masks);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getCloseAndUpdateMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getCloseAndUpdateMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable Boolean callCloseAndUpdate(@Nullable UInteger fileHandle) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Boolean> callCloseAndUpdateAsync(
      @Nullable UInteger fileHandle);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Boolean> callCloseAndUpdateDetailed(
      @Nullable UInteger fileHandle) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Boolean> callCloseAndUpdateDetailed(
      MethodCallOptions options, @Nullable UInteger fileHandle) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Boolean>>
      callCloseAndUpdateDetailedAsync(@Nullable UInteger fileHandle);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.5
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Boolean>>
      callCloseAndUpdateDetailedAsync(MethodCallOptions options, @Nullable UInteger fileHandle);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getAddCertificateMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getAddCertificateMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6
   *
   * <p>Invokes <code>AddCertificate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callAddCertificate(@Nullable ByteString certificate, @Nullable Boolean isTrustedCertificate)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6
   *
   * <p>Invokes <code>AddCertificate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callAddCertificateAsync(
      @Nullable ByteString certificate, @Nullable Boolean isTrustedCertificate);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6
   *
   * <p>Invokes <code>AddCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddCertificateDetailed(
      @Nullable ByteString certificate, @Nullable Boolean isTrustedCertificate) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6
   *
   * <p>Invokes <code>AddCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddCertificateDetailed(
      MethodCallOptions options,
      @Nullable ByteString certificate,
      @Nullable Boolean isTrustedCertificate)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6
   *
   * <p>Invokes <code>AddCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddCertificateDetailedAsync(
          @Nullable ByteString certificate, @Nullable Boolean isTrustedCertificate);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6
   *
   * <p>Invokes <code>AddCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddCertificateDetailedAsync(
          MethodCallOptions options,
          @Nullable ByteString certificate,
          @Nullable Boolean isTrustedCertificate);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getRemoveCertificateMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getRemoveCertificateMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7
   *
   * <p>Invokes <code>RemoveCertificate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveCertificate(@Nullable String thumbprint, @Nullable Boolean isTrustedCertificate)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7
   *
   * <p>Invokes <code>RemoveCertificate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveCertificateAsync(
      @Nullable String thumbprint, @Nullable Boolean isTrustedCertificate);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7
   *
   * <p>Invokes <code>RemoveCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveCertificateDetailed(
      @Nullable String thumbprint, @Nullable Boolean isTrustedCertificate) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7
   *
   * <p>Invokes <code>RemoveCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveCertificateDetailed(
      MethodCallOptions options,
      @Nullable String thumbprint,
      @Nullable Boolean isTrustedCertificate)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7
   *
   * <p>Invokes <code>RemoveCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveCertificateDetailedAsync(
          @Nullable String thumbprint, @Nullable Boolean isTrustedCertificate);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7
   *
   * <p>Invokes <code>RemoveCertificate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveCertificateDetailedAsync(
          MethodCallOptions options,
          @Nullable String thumbprint,
          @Nullable Boolean isTrustedCertificate);
}
