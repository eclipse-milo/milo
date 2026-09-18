package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the UadpWriterGroupMessageType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.1">Model
 *     documentation</a>
 */
public interface UadpWriterGroupMessageType extends WriterGroupMessageType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21105L);

  QualifiedProperty<UInteger> GroupVersion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "GroupVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
          -1,
          UInteger.class);

  QualifiedProperty<Double> SamplingOffset_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SamplingOffset",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<DataSetOrderingType> DataSetOrdering_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetOrdering",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20408L),
          -1,
          DataSetOrderingType.class);

  QualifiedProperty<Double[]> PublishingOffset_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PublishingOffset",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          1,
          Double[].class);

  QualifiedProperty<UadpNetworkMessageContentMask> NetworkMessageContentMask_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NetworkMessageContentMask",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15642L),
          -1,
          UadpNetworkMessageContentMask.class);

  /**
   * Resolves the mandatory GroupVersion child, a PropertyType with DataType VersionTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getGroupVersionNode() throws UaException;

  /** Asynchronous form of {@link #getGroupVersionNode()}. */
  CompletableFuture<? extends PropertyType> getGroupVersionNodeAsync();

  /**
   * Reads the Value of the GroupVersion child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readGroupVersion() throws UaException;

  /**
   * Writes the Value of the GroupVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeGroupVersion(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readGroupVersion()}. */
  CompletableFuture<? extends @Nullable UInteger> readGroupVersionAsync();

  /** Asynchronous form of {@link #writeGroupVersion}; completes with the operation status. */
  CompletableFuture<StatusCode> writeGroupVersionAsync(@Nullable UInteger value);

  /**
   * Resolves the optional SamplingOffset child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSamplingOffsetNode() throws UaException;

  /** Asynchronous form of {@link #getSamplingOffsetNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSamplingOffsetNodeAsync();

  /**
   * Reads the Value of the SamplingOffset child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readSamplingOffset() throws UaException;

  /**
   * Writes the Value of the SamplingOffset child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSamplingOffset(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readSamplingOffset()}. */
  CompletableFuture<? extends @Nullable Double> readSamplingOffsetAsync();

  /** Asynchronous form of {@link #writeSamplingOffset}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSamplingOffsetAsync(@Nullable Double value);

  /**
   * Resolves the mandatory DataSetOrdering child, a PropertyType with DataType DataSetOrderingType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDataSetOrderingNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetOrderingNode()}. */
  CompletableFuture<? extends PropertyType> getDataSetOrderingNodeAsync();

  /**
   * Reads the Value of the DataSetOrdering child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DataSetOrderingType readDataSetOrdering() throws UaException;

  /**
   * Writes the Value of the DataSetOrdering child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetOrdering(@Nullable DataSetOrderingType value) throws UaException;

  /** Asynchronous form of {@link #readDataSetOrdering()}. */
  CompletableFuture<? extends @Nullable DataSetOrderingType> readDataSetOrderingAsync();

  /** Asynchronous form of {@link #writeDataSetOrdering}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDataSetOrderingAsync(@Nullable DataSetOrderingType value);

  /**
   * Resolves the mandatory PublishingOffset child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPublishingOffsetNode() throws UaException;

  /** Asynchronous form of {@link #getPublishingOffsetNode()}. */
  CompletableFuture<? extends PropertyType> getPublishingOffsetNodeAsync();

  /**
   * Reads the Value of the PublishingOffset child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  Double @Nullable [] readPublishingOffset() throws UaException;

  /**
   * Writes the Value of the PublishingOffset child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePublishingOffset(Double @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readPublishingOffset()}. */
  CompletableFuture<? extends Double @Nullable []> readPublishingOffsetAsync();

  /** Asynchronous form of {@link #writePublishingOffset}; completes with the operation status. */
  CompletableFuture<StatusCode> writePublishingOffsetAsync(Double @Nullable [] value);

  /**
   * Resolves the mandatory NetworkMessageContentMask child, a PropertyType with DataType
   * UadpNetworkMessageContentMask.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNetworkMessageContentMaskNode() throws UaException;

  /** Asynchronous form of {@link #getNetworkMessageContentMaskNode()}. */
  CompletableFuture<? extends PropertyType> getNetworkMessageContentMaskNodeAsync();

  /**
   * Reads the Value of the NetworkMessageContentMask child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UadpNetworkMessageContentMask readNetworkMessageContentMask() throws UaException;

  /**
   * Writes the Value of the NetworkMessageContentMask child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNetworkMessageContentMask(@Nullable UadpNetworkMessageContentMask value)
      throws UaException;

  /** Asynchronous form of {@link #readNetworkMessageContentMask()}. */
  CompletableFuture<? extends @Nullable UadpNetworkMessageContentMask>
      readNetworkMessageContentMaskAsync();

  /**
   * Asynchronous form of {@link #writeNetworkMessageContentMask}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeNetworkMessageContentMaskAsync(
      @Nullable UadpNetworkMessageContentMask value);
}
