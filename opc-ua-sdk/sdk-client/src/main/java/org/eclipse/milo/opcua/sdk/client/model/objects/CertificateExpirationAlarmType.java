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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.7">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.7</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface CertificateExpirationAlarmType extends SystemOffNormalAlarmType {
  QualifiedProperty<DateTime> EXPIRATION_DATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ExpirationDate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  QualifiedProperty<Double> EXPIRATION_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ExpirationLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<NodeId> CERTIFICATE_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<ByteString> CERTIFICATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Certificate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  /** Gets the existing node's local value. */
  @Nullable DateTime getExpirationDate() throws UaException;

  /** Sets the existing node's local value. */
  void setExpirationDate(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readExpirationDate() throws UaException;

  /** Writes the value remotely. */
  void writeExpirationDate(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readExpirationDateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeExpirationDateAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getExpirationDateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getExpirationDateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getExpirationLimit() throws UaException;

  /** Sets the existing node's local value. */
  void setExpirationLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readExpirationLimit() throws UaException;

  /** Writes the value remotely. */
  void writeExpirationLimit(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readExpirationLimitAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeExpirationLimitAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getExpirationLimitNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getExpirationLimitNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getCertificateType() throws UaException;

  /** Sets the existing node's local value. */
  void setCertificateType(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readCertificateType() throws UaException;

  /** Writes the value remotely. */
  void writeCertificateType(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readCertificateTypeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCertificateTypeAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCertificateTypeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getCertificateTypeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ByteString getCertificate() throws UaException;

  /** Sets the existing node's local value. */
  void setCertificate(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ByteString readCertificate() throws UaException;

  /** Writes the value remotely. */
  void writeCertificate(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ByteString> readCertificateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCertificateAsync(@Nullable ByteString value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCertificateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getCertificateNodeAsync();
}
