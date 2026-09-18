package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AlarmMask;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AlarmStateVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/8.2">Model
 *     documentation</a>
 */
public interface AlarmStateVariableType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32244L);

  QualifiedProperty<UInteger> ActiveCount_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ActiveCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> UnconfirmedCount_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UnconfirmedCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> UnacknowledgedCount_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UnacknowledgedCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UShort> HighestUnackSeverity_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "HighestUnackSeverity",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<UShort> HighestActiveSeverity_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "HighestActiveSeverity",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<ContentFilter> Filter_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Filter",
          ExpandedNodeId.of(Namespaces.OPC_UA, 586L),
          -1,
          ContentFilter.class);

  /**
   * Resolves the mandatory ActiveCount child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getActiveCountNode() throws UaException;

  /** Asynchronous form of {@link #getActiveCountNode()}. */
  CompletableFuture<? extends PropertyType> getActiveCountNodeAsync();

  /**
   * Reads the Value of the ActiveCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readActiveCount() throws UaException;

  /**
   * Writes the Value of the ActiveCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeActiveCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readActiveCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readActiveCountAsync();

  /** Asynchronous form of {@link #writeActiveCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeActiveCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory UnconfirmedCount child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUnconfirmedCountNode() throws UaException;

  /** Asynchronous form of {@link #getUnconfirmedCountNode()}. */
  CompletableFuture<? extends PropertyType> getUnconfirmedCountNodeAsync();

  /**
   * Reads the Value of the UnconfirmedCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readUnconfirmedCount() throws UaException;

  /**
   * Writes the Value of the UnconfirmedCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUnconfirmedCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readUnconfirmedCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readUnconfirmedCountAsync();

  /** Asynchronous form of {@link #writeUnconfirmedCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeUnconfirmedCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory UnacknowledgedCount child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUnacknowledgedCountNode() throws UaException;

  /** Asynchronous form of {@link #getUnacknowledgedCountNode()}. */
  CompletableFuture<? extends PropertyType> getUnacknowledgedCountNodeAsync();

  /**
   * Reads the Value of the UnacknowledgedCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readUnacknowledgedCount() throws UaException;

  /**
   * Writes the Value of the UnacknowledgedCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUnacknowledgedCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readUnacknowledgedCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readUnacknowledgedCountAsync();

  /**
   * Asynchronous form of {@link #writeUnacknowledgedCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeUnacknowledgedCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory HighestUnackSeverity child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getHighestUnackSeverityNode() throws UaException;

  /** Asynchronous form of {@link #getHighestUnackSeverityNode()}. */
  CompletableFuture<? extends PropertyType> getHighestUnackSeverityNodeAsync();

  /**
   * Reads the Value of the HighestUnackSeverity child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readHighestUnackSeverity() throws UaException;

  /**
   * Writes the Value of the HighestUnackSeverity child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHighestUnackSeverity(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readHighestUnackSeverity()}. */
  CompletableFuture<? extends @Nullable UShort> readHighestUnackSeverityAsync();

  /**
   * Asynchronous form of {@link #writeHighestUnackSeverity}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeHighestUnackSeverityAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory HighestActiveSeverity child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getHighestActiveSeverityNode() throws UaException;

  /** Asynchronous form of {@link #getHighestActiveSeverityNode()}. */
  CompletableFuture<? extends PropertyType> getHighestActiveSeverityNodeAsync();

  /**
   * Reads the Value of the HighestActiveSeverity child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readHighestActiveSeverity() throws UaException;

  /**
   * Writes the Value of the HighestActiveSeverity child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHighestActiveSeverity(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readHighestActiveSeverity()}. */
  CompletableFuture<? extends @Nullable UShort> readHighestActiveSeverityAsync();

  /**
   * Asynchronous form of {@link #writeHighestActiveSeverity}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeHighestActiveSeverityAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory Filter child, a PropertyType with DataType ContentFilter.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getFilterNode() throws UaException;

  /** Asynchronous form of {@link #getFilterNode()}. */
  CompletableFuture<? extends PropertyType> getFilterNodeAsync();

  /**
   * Reads the Value of the Filter child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ContentFilter readFilter() throws UaException;

  /**
   * Writes the Value of the Filter child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeFilter(@Nullable ContentFilter value) throws UaException;

  /** Asynchronous form of {@link #readFilter()}. */
  CompletableFuture<? extends @Nullable ContentFilter> readFilterAsync();

  /** Asynchronous form of {@link #writeFilter}; completes with the operation status. */
  CompletableFuture<StatusCode> writeFilterAsync(@Nullable ContentFilter value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable AlarmMask readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable AlarmMask value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable AlarmMask> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable AlarmMask value);
}
