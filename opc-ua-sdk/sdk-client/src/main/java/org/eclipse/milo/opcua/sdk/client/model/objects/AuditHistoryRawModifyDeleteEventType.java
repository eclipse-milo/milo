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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.6">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.6</a>
 */
public interface AuditHistoryRawModifyDeleteEventType extends AuditHistoryDeleteEventType {
  QualifiedProperty<Boolean> IS_DELETE_MODIFIED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IsDeleteModified",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

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

  QualifiedProperty<DataValue[]> OLD_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OldValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23"),
          1,
          DataValue[].class);

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
  @Nullable Boolean getIsDeleteModified() throws UaException;

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
  void setIsDeleteModified(@Nullable Boolean value) throws UaException;

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
  @Nullable Boolean readIsDeleteModified() throws UaException;

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
  void writeIsDeleteModified(@Nullable Boolean value) throws UaException;

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
  CompletableFuture<? extends @Nullable Boolean> readIsDeleteModifiedAsync();

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
  CompletableFuture<StatusCode> writeIsDeleteModifiedAsync(@Nullable Boolean value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  PropertyType getIsDeleteModifiedNode() throws UaException;

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
  CompletableFuture<? extends PropertyType> getIsDeleteModifiedNodeAsync();

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
  @Nullable DateTime getStartTime() throws UaException;

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
  void setStartTime(@Nullable DateTime value) throws UaException;

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
  @Nullable DateTime readStartTime() throws UaException;

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
  void writeStartTime(@Nullable DateTime value) throws UaException;

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
  CompletableFuture<? extends @Nullable DateTime> readStartTimeAsync();

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
  CompletableFuture<StatusCode> writeStartTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  PropertyType getStartTimeNode() throws UaException;

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
  CompletableFuture<? extends PropertyType> getStartTimeNodeAsync();

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
  @Nullable DateTime getEndTime() throws UaException;

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
  void setEndTime(@Nullable DateTime value) throws UaException;

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
  @Nullable DateTime readEndTime() throws UaException;

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
  void writeEndTime(@Nullable DateTime value) throws UaException;

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
  CompletableFuture<? extends @Nullable DateTime> readEndTimeAsync();

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
  CompletableFuture<StatusCode> writeEndTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  PropertyType getEndTimeNode() throws UaException;

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
  CompletableFuture<? extends PropertyType> getEndTimeNodeAsync();

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
  @Nullable DataValue @Nullable [] getOldValues() throws UaException;

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
  void setOldValues(@Nullable DataValue @Nullable [] value) throws UaException;

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
  @Nullable DataValue @Nullable [] readOldValues() throws UaException;

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
  void writeOldValues(@Nullable DataValue @Nullable [] value) throws UaException;

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
  CompletableFuture<? extends @Nullable DataValue @Nullable []> readOldValuesAsync();

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
  CompletableFuture<StatusCode> writeOldValuesAsync(@Nullable DataValue @Nullable [] value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  PropertyType getOldValuesNode() throws UaException;

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
  CompletableFuture<? extends PropertyType> getOldValuesNodeAsync();
}
