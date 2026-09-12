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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15</a>
 */
public interface IPriorityMappingEntryType extends BaseInterfaceType {
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
  @Nullable String getMappingUri() throws UaException;

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
  void setMappingUri(@Nullable String value) throws UaException;

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
  @Nullable String readMappingUri() throws UaException;

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
  void writeMappingUri(@Nullable String value) throws UaException;

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
  CompletableFuture<? extends @Nullable String> readMappingUriAsync();

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
  CompletableFuture<StatusCode> writeMappingUriAsync(@Nullable String value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  BaseDataVariableType getMappingUriNode() throws UaException;

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
  CompletableFuture<? extends BaseDataVariableType> getMappingUriNodeAsync();

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
  @Nullable String getPriorityLabel() throws UaException;

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
  void setPriorityLabel(@Nullable String value) throws UaException;

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
  @Nullable String readPriorityLabel() throws UaException;

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
  void writePriorityLabel(@Nullable String value) throws UaException;

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
  CompletableFuture<? extends @Nullable String> readPriorityLabelAsync();

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
  CompletableFuture<StatusCode> writePriorityLabelAsync(@Nullable String value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  BaseDataVariableType getPriorityLabelNode() throws UaException;

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
  CompletableFuture<? extends BaseDataVariableType> getPriorityLabelNodeAsync();

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
  @Nullable UByte getPriorityValuePcp() throws UaException;

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
  void setPriorityValuePcp(@Nullable UByte value) throws UaException;

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
  @Nullable UByte readPriorityValuePcp() throws UaException;

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
  void writePriorityValuePcp(@Nullable UByte value) throws UaException;

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
  CompletableFuture<? extends @Nullable UByte> readPriorityValuePcpAsync();

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
  CompletableFuture<StatusCode> writePriorityValuePcpAsync(@Nullable UByte value);

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @Nullable BaseDataVariableType getPriorityValuePcpNode() throws UaException;

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getPriorityValuePcpNodeAsync();

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
  @Nullable UInteger getPriorityValueDscp() throws UaException;

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
  void setPriorityValueDscp(@Nullable UInteger value) throws UaException;

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
  @Nullable UInteger readPriorityValueDscp() throws UaException;

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
  void writePriorityValueDscp(@Nullable UInteger value) throws UaException;

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
  CompletableFuture<? extends @Nullable UInteger> readPriorityValueDscpAsync();

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
  CompletableFuture<StatusCode> writePriorityValueDscpAsync(@Nullable UInteger value);

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @Nullable BaseDataVariableType getPriorityValueDscpNode() throws UaException;

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getPriorityValueDscpNodeAsync();
}
