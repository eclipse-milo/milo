package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SerializationEntityType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.1">Model
 *     documentation</a>
 */
public interface SerializationEntityType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19824L);

  QualifiedProperty<Boolean> IncludeStatus_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "IncludeStatus",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<NodeId> CustomMetaDataRef_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CustomMetaDataRef",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<UShort> SerializationDepth_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SerializationDepth",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<NodeId[]> ExcludeReferenceTypes_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ExcludeReferenceTypes",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          1,
          NodeId[].class);

  QualifiedProperty<NodeId[]> IncludeReferenceTypes_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "IncludeReferenceTypes",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          1,
          NodeId[].class);

  QualifiedProperty<Boolean> IncludeSourceTimestamp_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "IncludeSourceTimestamp",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<KeyValuePair[]> CustomMetaDataProperties_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CustomMetaDataProperties",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14533L),
          1,
          KeyValuePair[].class);

  QualifiedProperty<Boolean> IncludeDictionaryReference_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "IncludeDictionaryReference",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> ConsiderSubElementSerializationProperties_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConsiderSubElementSerializationProperties",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  /**
   * Resolves the optional IncludeStatus child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getIncludeStatusNode() throws UaException;

  /** Asynchronous form of {@link #getIncludeStatusNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getIncludeStatusNodeAsync();

  /**
   * Reads the Value of the IncludeStatus child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readIncludeStatus() throws UaException;

  /**
   * Writes the Value of the IncludeStatus child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIncludeStatus(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readIncludeStatus()}. */
  CompletableFuture<? extends @Nullable Boolean> readIncludeStatusAsync();

  /** Asynchronous form of {@link #writeIncludeStatus}; completes with the operation status. */
  CompletableFuture<StatusCode> writeIncludeStatusAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory SerializedData child, a BaseDataVariableType with DataType Structure.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSerializedDataNode() throws UaException;

  /** Asynchronous form of {@link #getSerializedDataNode()}. */
  CompletableFuture<? extends VariableNode> getSerializedDataNodeAsync();

  /**
   * Reads the Value of the SerializedData child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Structure readSerializedData() throws UaException;

  /**
   * Writes the Value of the SerializedData child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSerializedData(@Nullable Structure value) throws UaException;

  /** Asynchronous form of {@link #readSerializedData()}. */
  CompletableFuture<? extends @Nullable Structure> readSerializedDataAsync();

  /** Asynchronous form of {@link #writeSerializedData}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSerializedDataAsync(@Nullable Structure value);

  /**
   * Resolves the optional CustomMetaDataRef child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getCustomMetaDataRefNode() throws UaException;

  /** Asynchronous form of {@link #getCustomMetaDataRefNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getCustomMetaDataRefNodeAsync();

  /**
   * Reads the Value of the CustomMetaDataRef child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readCustomMetaDataRef() throws UaException;

  /**
   * Writes the Value of the CustomMetaDataRef child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCustomMetaDataRef(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readCustomMetaDataRef()}. */
  CompletableFuture<? extends @Nullable NodeId> readCustomMetaDataRefAsync();

  /** Asynchronous form of {@link #writeCustomMetaDataRef}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCustomMetaDataRefAsync(@Nullable NodeId value);

  /**
   * Resolves the optional SerializationDepth child, a PropertyType with DataType UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSerializationDepthNode() throws UaException;

  /** Asynchronous form of {@link #getSerializationDepthNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSerializationDepthNodeAsync();

  /**
   * Reads the Value of the SerializationDepth child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readSerializationDepth() throws UaException;

  /**
   * Writes the Value of the SerializationDepth child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSerializationDepth(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readSerializationDepth()}. */
  CompletableFuture<? extends @Nullable UShort> readSerializationDepthAsync();

  /** Asynchronous form of {@link #writeSerializationDepth}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSerializationDepthAsync(@Nullable UShort value);

  /**
   * Resolves the optional ExcludeReferenceTypes child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getExcludeReferenceTypesNode() throws UaException;

  /** Asynchronous form of {@link #getExcludeReferenceTypesNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getExcludeReferenceTypesNodeAsync();

  /**
   * Reads the Value of the ExcludeReferenceTypes child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  NodeId @Nullable [] readExcludeReferenceTypes() throws UaException;

  /**
   * Writes the Value of the ExcludeReferenceTypes child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeExcludeReferenceTypes(NodeId @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readExcludeReferenceTypes()}. */
  CompletableFuture<? extends NodeId @Nullable []> readExcludeReferenceTypesAsync();

  /**
   * Asynchronous form of {@link #writeExcludeReferenceTypes}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeExcludeReferenceTypesAsync(NodeId @Nullable [] value);

  /**
   * Resolves the optional IncludeReferenceTypes child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getIncludeReferenceTypesNode() throws UaException;

  /** Asynchronous form of {@link #getIncludeReferenceTypesNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getIncludeReferenceTypesNodeAsync();

  /**
   * Reads the Value of the IncludeReferenceTypes child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  NodeId @Nullable [] readIncludeReferenceTypes() throws UaException;

  /**
   * Writes the Value of the IncludeReferenceTypes child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIncludeReferenceTypes(NodeId @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readIncludeReferenceTypes()}. */
  CompletableFuture<? extends NodeId @Nullable []> readIncludeReferenceTypesAsync();

  /**
   * Asynchronous form of {@link #writeIncludeReferenceTypes}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeIncludeReferenceTypesAsync(NodeId @Nullable [] value);

  /**
   * Resolves the optional IncludeSourceTimestamp child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getIncludeSourceTimestampNode() throws UaException;

  /** Asynchronous form of {@link #getIncludeSourceTimestampNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getIncludeSourceTimestampNodeAsync();

  /**
   * Reads the Value of the IncludeSourceTimestamp child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readIncludeSourceTimestamp() throws UaException;

  /**
   * Writes the Value of the IncludeSourceTimestamp child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIncludeSourceTimestamp(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readIncludeSourceTimestamp()}. */
  CompletableFuture<? extends @Nullable Boolean> readIncludeSourceTimestampAsync();

  /**
   * Asynchronous form of {@link #writeIncludeSourceTimestamp}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeIncludeSourceTimestampAsync(@Nullable Boolean value);

  /**
   * Resolves the optional CustomMetaDataProperties child, a PropertyType with DataType
   * KeyValuePair.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getCustomMetaDataPropertiesNode() throws UaException;

  /** Asynchronous form of {@link #getCustomMetaDataPropertiesNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getCustomMetaDataPropertiesNodeAsync();

  /**
   * Reads the Value of the CustomMetaDataProperties child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable KeyValuePair @Nullable [] readCustomMetaDataProperties() throws UaException;

  /**
   * Writes the Value of the CustomMetaDataProperties child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCustomMetaDataProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readCustomMetaDataProperties()}. */
  CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readCustomMetaDataPropertiesAsync();

  /**
   * Asynchronous form of {@link #writeCustomMetaDataProperties}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeCustomMetaDataPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value);

  /**
   * Resolves the optional IncludeDictionaryReference child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getIncludeDictionaryReferenceNode() throws UaException;

  /** Asynchronous form of {@link #getIncludeDictionaryReferenceNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getIncludeDictionaryReferenceNodeAsync();

  /**
   * Reads the Value of the IncludeDictionaryReference child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readIncludeDictionaryReference() throws UaException;

  /**
   * Writes the Value of the IncludeDictionaryReference child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIncludeDictionaryReference(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readIncludeDictionaryReference()}. */
  CompletableFuture<? extends @Nullable Boolean> readIncludeDictionaryReferenceAsync();

  /**
   * Asynchronous form of {@link #writeIncludeDictionaryReference}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeIncludeDictionaryReferenceAsync(@Nullable Boolean value);

  /**
   * Resolves the optional ConsiderSubElementSerializationProperties child, a PropertyType with
   * DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getConsiderSubElementSerializationPropertiesNode() throws UaException;

  /** Asynchronous form of {@link #getConsiderSubElementSerializationPropertiesNode()}. */
  CompletableFuture<? extends @Nullable PropertyType>
      getConsiderSubElementSerializationPropertiesNodeAsync();

  /**
   * Reads the Value of the ConsiderSubElementSerializationProperties child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readConsiderSubElementSerializationProperties() throws UaException;

  /**
   * Writes the Value of the ConsiderSubElementSerializationProperties child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConsiderSubElementSerializationProperties(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readConsiderSubElementSerializationProperties()}. */
  CompletableFuture<? extends @Nullable Boolean>
      readConsiderSubElementSerializationPropertiesAsync();

  /**
   * Asynchronous form of {@link #writeConsiderSubElementSerializationProperties}; completes with
   * the operation status.
   */
  CompletableFuture<StatusCode> writeConsiderSubElementSerializationPropertiesAsync(
      @Nullable Boolean value);

  /**
   * Resolves the optional ConfigureSerialization Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getConfigureSerializationMethodNode() throws UaException;

  /** Asynchronous form of {@link #getConfigureSerializationMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getConfigureSerializationMethodNodeAsync();

  /**
   * Calls the ConfigureSerialization Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3">Model
   *     documentation</a>
   */
  Integer @Nullable [] configureSerialization(
      @Nullable KeyValuePair @Nullable [] serializationFilterProperties) throws UaException;

  /**
   * Calls the ConfigureSerialization Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Integer @Nullable []> callConfigureSerialization(
      @Nullable KeyValuePair @Nullable [] serializationFilterProperties) throws UaException;

  /**
   * Calls the ConfigureSerialization Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Integer @Nullable []> callConfigureSerializationWith(
      MethodCallOptions options, @Nullable KeyValuePair @Nullable [] serializationFilterProperties)
      throws UaException;

  /** Asynchronous form of {@link #configureSerialization}. */
  CompletableFuture<Integer @Nullable []> configureSerializationAsync(
      @Nullable KeyValuePair @Nullable [] serializationFilterProperties);

  /** Asynchronous form of {@link #callConfigureSerialization}. */
  CompletableFuture<MethodCallResult<Integer @Nullable []>> callConfigureSerializationAsync(
      @Nullable KeyValuePair @Nullable [] serializationFilterProperties);

  /** Asynchronous form of {@link #callConfigureSerializationWith}. */
  CompletableFuture<MethodCallResult<Integer @Nullable []>> callConfigureSerializationWithAsync(
      MethodCallOptions options, @Nullable KeyValuePair @Nullable [] serializationFilterProperties);
}
