package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the DataSetWriterType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.2">Model
 *     documentation</a>
 */
public interface DataSetWriterType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15298L);

  QualifiedProperty<UInteger> KeyFrameCount_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "KeyFrameCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UShort> DataSetWriterId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetWriterId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<DataSetFieldContentMask> DataSetFieldContentMask_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetFieldContentMask",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15583L),
          -1,
          DataSetFieldContentMask.class);

  QualifiedProperty<KeyValuePair[]> DataSetWriterProperties_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetWriterProperties",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14533L),
          1,
          KeyValuePair[].class);

  /**
   * Resolves the optional Diagnostics child, a PubSubDiagnosticsDataSetWriterType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.11">PubSubDiagnosticsDataSetWriterType
   *     documentation</a>
   */
  @Nullable PubSubDiagnosticsDataSetWriterType getDiagnosticsNode() throws UaException;

  /** Asynchronous form of {@link #getDiagnosticsNode()}. */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsDataSetWriterType>
      getDiagnosticsNodeAsync();

  /**
   * Resolves the optional KeyFrameCount child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getKeyFrameCountNode() throws UaException;

  /** Asynchronous form of {@link #getKeyFrameCountNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getKeyFrameCountNodeAsync();

  /**
   * Reads the Value of the KeyFrameCount child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readKeyFrameCount() throws UaException;

  /**
   * Writes the Value of the KeyFrameCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeKeyFrameCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readKeyFrameCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readKeyFrameCountAsync();

  /** Asynchronous form of {@link #writeKeyFrameCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeKeyFrameCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory DataSetWriterId child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDataSetWriterIdNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetWriterIdNode()}. */
  CompletableFuture<? extends PropertyType> getDataSetWriterIdNodeAsync();

  /**
   * Reads the Value of the DataSetWriterId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readDataSetWriterId() throws UaException;

  /**
   * Writes the Value of the DataSetWriterId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetWriterId(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readDataSetWriterId()}. */
  CompletableFuture<? extends @Nullable UShort> readDataSetWriterIdAsync();

  /** Asynchronous form of {@link #writeDataSetWriterId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDataSetWriterIdAsync(@Nullable UShort value);

  /**
   * Resolves the optional MessageSettings child, a DataSetWriterMessageType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.4">DataSetWriterMessageType
   *     documentation</a>
   */
  @Nullable DataSetWriterMessageType getMessageSettingsNode() throws UaException;

  /** Asynchronous form of {@link #getMessageSettingsNode()}. */
  CompletableFuture<? extends @Nullable DataSetWriterMessageType> getMessageSettingsNodeAsync();

  /**
   * Resolves the optional TransportSettings child, a DataSetWriterTransportType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.3">DataSetWriterTransportType
   *     documentation</a>
   */
  @Nullable DataSetWriterTransportType getTransportSettingsNode() throws UaException;

  /** Asynchronous form of {@link #getTransportSettingsNode()}. */
  CompletableFuture<? extends @Nullable DataSetWriterTransportType> getTransportSettingsNodeAsync();

  /**
   * Resolves the mandatory DataSetFieldContentMask child, a PropertyType with DataType
   * DataSetFieldContentMask.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDataSetFieldContentMaskNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetFieldContentMaskNode()}. */
  CompletableFuture<? extends PropertyType> getDataSetFieldContentMaskNodeAsync();

  /**
   * Reads the Value of the DataSetFieldContentMask child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DataSetFieldContentMask readDataSetFieldContentMask() throws UaException;

  /**
   * Writes the Value of the DataSetFieldContentMask child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetFieldContentMask(@Nullable DataSetFieldContentMask value) throws UaException;

  /** Asynchronous form of {@link #readDataSetFieldContentMask()}. */
  CompletableFuture<? extends @Nullable DataSetFieldContentMask> readDataSetFieldContentMaskAsync();

  /**
   * Asynchronous form of {@link #writeDataSetFieldContentMask}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDataSetFieldContentMaskAsync(
      @Nullable DataSetFieldContentMask value);

  /**
   * Resolves the mandatory DataSetWriterProperties child, a PropertyType with DataType
   * KeyValuePair.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDataSetWriterPropertiesNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetWriterPropertiesNode()}. */
  CompletableFuture<? extends PropertyType> getDataSetWriterPropertiesNodeAsync();

  /**
   * Reads the Value of the DataSetWriterProperties child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable KeyValuePair @Nullable [] readDataSetWriterProperties() throws UaException;

  /**
   * Writes the Value of the DataSetWriterProperties child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetWriterProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readDataSetWriterProperties()}. */
  CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readDataSetWriterPropertiesAsync();

  /**
   * Asynchronous form of {@link #writeDataSetWriterProperties}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDataSetWriterPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value);

  /**
   * Resolves the mandatory Status child, a PubSubStatusType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.1">PubSubStatusType
   *     documentation</a>
   */
  PubSubStatusType getStatusNode() throws UaException;

  /** Asynchronous form of {@link #getStatusNode()}. */
  CompletableFuture<? extends PubSubStatusType> getStatusNodeAsync();
}
