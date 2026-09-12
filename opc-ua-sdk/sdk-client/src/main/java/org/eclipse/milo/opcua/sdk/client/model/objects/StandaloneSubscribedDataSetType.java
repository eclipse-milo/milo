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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.5">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.5</a>
 */
public interface StandaloneSubscribedDataSetType extends BaseObjectType {
  QualifiedProperty<DataSetMetaDataType> DATA_SET_META_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMetaData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14523"),
          -1,
          DataSetMetaDataType.class);

  QualifiedProperty<Boolean> IS_CONNECTED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IsConnected",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Lookup may perform service I/O; the value is not read
   * remotely. Use the node's raw DataValue to inspect quality and timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable DataSetMetaDataType getDataSetMetaData() throws UaException;

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   * This does not send a Write service request.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  void setDataSetMetaData(@Nullable DataSetMetaDataType value) throws UaException;

  /**
   * Reads the existing member's value remotely. Only Good status is accepted, including Good
   * subcodes; Uncertain and Bad statuses fail before conversion. A Good null value is valid. An
   * absent node fails with Bad_NotFound. This does not update the wrapper's local value. Use the
   * node's raw readValue to retain quality, timestamps and unconverted values.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails; non-Good operation status also fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable DataSetMetaDataType readDataSetMetaData() throws UaException;

  /**
   * Writes the existing member's value remotely. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or Write. This does not create nodes or update the wrapper's
   * local value.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails; non-Good operation status also fails
   */
  void writeDataSetMetaData(@Nullable DataSetMetaDataType value) throws UaException;

  /**
   * Reads the existing member's value remotely. Only Good status is accepted, including Good
   * subcodes; Uncertain and Bad statuses fail before conversion. A Good null value is valid. An
   * absent node fails with Bad_NotFound. This does not update the wrapper's local value. Use the
   * node's raw readValue to retain quality, timestamps and unconverted values.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the value, which may be null on a present member
   */
  CompletableFuture<? extends @Nullable DataSetMetaDataType> readDataSetMetaDataAsync();

  /**
   * Writes the existing member's value remotely. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or Write. This does not create nodes or update the wrapper's
   * local value.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @param value the value to store; null is permitted
   * @return a nonnull future completing with the Write operation status, including non-Good
   *     statuses
   */
  CompletableFuture<StatusCode> writeDataSetMetaDataAsync(@Nullable DataSetMetaDataType value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  PropertyType getDataSetMetaDataNode() throws UaException;

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member
   */
  CompletableFuture<? extends PropertyType> getDataSetMetaDataNodeAsync();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Lookup may perform service I/O; the value is not read
   * remotely. Use the node's raw DataValue to inspect quality and timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable Boolean getIsConnected() throws UaException;

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   * This does not send a Write service request.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  void setIsConnected(@Nullable Boolean value) throws UaException;

  /**
   * Reads the existing member's value remotely. Only Good status is accepted, including Good
   * subcodes; Uncertain and Bad statuses fail before conversion. A Good null value is valid. An
   * absent node fails with Bad_NotFound. This does not update the wrapper's local value. Use the
   * node's raw readValue to retain quality, timestamps and unconverted values.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails; non-Good operation status also fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable Boolean readIsConnected() throws UaException;

  /**
   * Writes the existing member's value remotely. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or Write. This does not create nodes or update the wrapper's
   * local value.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails; non-Good operation status also fails
   */
  void writeIsConnected(@Nullable Boolean value) throws UaException;

  /**
   * Reads the existing member's value remotely. Only Good status is accepted, including Good
   * subcodes; Uncertain and Bad statuses fail before conversion. A Good null value is valid. An
   * absent node fails with Bad_NotFound. This does not update the wrapper's local value. Use the
   * node's raw readValue to retain quality, timestamps and unconverted values.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the value, which may be null on a present member
   */
  CompletableFuture<? extends @Nullable Boolean> readIsConnectedAsync();

  /**
   * Writes the existing member's value remotely. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or Write. This does not create nodes or update the wrapper's
   * local value.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @param value the value to store; null is permitted
   * @return a nonnull future completing with the Write operation status, including non-Good
   *     statuses
   */
  CompletableFuture<StatusCode> writeIsConnectedAsync(@Nullable Boolean value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  PropertyType getIsConnectedNode() throws UaException;

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member
   */
  CompletableFuture<? extends PropertyType> getIsConnectedNodeAsync();

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  SubscribedDataSetType getSubscribedDataSetNode() throws UaException;

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member
   */
  CompletableFuture<? extends SubscribedDataSetType> getSubscribedDataSetNodeAsync();
}
