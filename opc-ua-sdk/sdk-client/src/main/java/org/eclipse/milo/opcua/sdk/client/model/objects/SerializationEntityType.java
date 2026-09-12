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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SerializationEntityType extends BaseObjectType {
  QualifiedProperty<NodeId[]> INCLUDE_REFERENCE_TYPES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IncludeReferenceTypes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<NodeId[]> EXCLUDE_REFERENCE_TYPES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ExcludeReferenceTypes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<UShort> SERIALIZATION_DEPTH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SerializationDepth",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<Boolean> CONSIDER_SUB_ELEMENT_SERIALIZATION_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConsiderSubElementSerializationProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<KeyValuePair[]> CUSTOM_META_DATA_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CustomMetaDataProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14533"),
          1,
          KeyValuePair[].class);

  QualifiedProperty<NodeId> CUSTOM_META_DATA_REF =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CustomMetaDataRef",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<Boolean> INCLUDE_STATUS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IncludeStatus",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> INCLUDE_SOURCE_TIMESTAMP =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IncludeSourceTimestamp",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> INCLUDE_DICTIONARY_REFERENCE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IncludeDictionaryReference",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getIncludeReferenceTypes() throws UaException;

  /** Sets the existing node's local value. */
  void setIncludeReferenceTypes(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId @Nullable [] readIncludeReferenceTypes() throws UaException;

  /** Writes the value remotely. */
  void writeIncludeReferenceTypes(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId @Nullable []> readIncludeReferenceTypesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIncludeReferenceTypesAsync(
      @Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getIncludeReferenceTypesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getIncludeReferenceTypesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getExcludeReferenceTypes() throws UaException;

  /** Sets the existing node's local value. */
  void setExcludeReferenceTypes(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId @Nullable [] readExcludeReferenceTypes() throws UaException;

  /** Writes the value remotely. */
  void writeExcludeReferenceTypes(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId @Nullable []> readExcludeReferenceTypesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeExcludeReferenceTypesAsync(
      @Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getExcludeReferenceTypesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getExcludeReferenceTypesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getSerializationDepth() throws UaException;

  /** Sets the existing node's local value. */
  void setSerializationDepth(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readSerializationDepth() throws UaException;

  /** Writes the value remotely. */
  void writeSerializationDepth(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readSerializationDepthAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSerializationDepthAsync(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSerializationDepthNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSerializationDepthNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getConsiderSubElementSerializationProperties() throws UaException;

  /** Sets the existing node's local value. */
  void setConsiderSubElementSerializationProperties(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readConsiderSubElementSerializationProperties() throws UaException;

  /** Writes the value remotely. */
  void writeConsiderSubElementSerializationProperties(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean>
      readConsiderSubElementSerializationPropertiesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConsiderSubElementSerializationPropertiesAsync(
      @Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConsiderSubElementSerializationPropertiesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType>
      getConsiderSubElementSerializationPropertiesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable KeyValuePair @Nullable [] getCustomMetaDataProperties() throws UaException;

  /** Sets the existing node's local value. */
  void setCustomMetaDataProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable KeyValuePair @Nullable [] readCustomMetaDataProperties() throws UaException;

  /** Writes the value remotely. */
  void writeCustomMetaDataProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readCustomMetaDataPropertiesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCustomMetaDataPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCustomMetaDataPropertiesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getCustomMetaDataPropertiesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getCustomMetaDataRef() throws UaException;

  /** Sets the existing node's local value. */
  void setCustomMetaDataRef(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readCustomMetaDataRef() throws UaException;

  /** Writes the value remotely. */
  void writeCustomMetaDataRef(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readCustomMetaDataRefAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCustomMetaDataRefAsync(@Nullable NodeId value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCustomMetaDataRefNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getCustomMetaDataRefNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getIncludeStatus() throws UaException;

  /** Sets the existing node's local value. */
  void setIncludeStatus(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readIncludeStatus() throws UaException;

  /** Writes the value remotely. */
  void writeIncludeStatus(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readIncludeStatusAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIncludeStatusAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getIncludeStatusNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getIncludeStatusNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getIncludeSourceTimestamp() throws UaException;

  /** Sets the existing node's local value. */
  void setIncludeSourceTimestamp(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readIncludeSourceTimestamp() throws UaException;

  /** Writes the value remotely. */
  void writeIncludeSourceTimestamp(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readIncludeSourceTimestampAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIncludeSourceTimestampAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getIncludeSourceTimestampNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getIncludeSourceTimestampNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getIncludeDictionaryReference() throws UaException;

  /** Sets the existing node's local value. */
  void setIncludeDictionaryReference(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readIncludeDictionaryReference() throws UaException;

  /** Writes the value remotely. */
  void writeIncludeDictionaryReference(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readIncludeDictionaryReferenceAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIncludeDictionaryReferenceAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getIncludeDictionaryReferenceNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getIncludeDictionaryReferenceNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UaStructuredType getSerializedData() throws UaException;

  /** Sets the existing node's local value. */
  void setSerializedData(@Nullable UaStructuredType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UaStructuredType readSerializedData() throws UaException;

  /** Writes the value remotely. */
  void writeSerializedData(@Nullable UaStructuredType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UaStructuredType> readSerializedDataAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSerializedDataAsync(@Nullable UaStructuredType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSerializedDataNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSerializedDataNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getConfigureSerializationMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getConfigureSerializationMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3
   *
   * <p>Invokes <code>ConfigureSerialization</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable Integer @Nullable [] callConfigureSerialization(
      @Nullable KeyValuePair @Nullable [] serializationFilterProperties) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3
   *
   * <p>Invokes <code>ConfigureSerialization</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Integer @Nullable []> callConfigureSerializationAsync(
      @Nullable KeyValuePair @Nullable [] serializationFilterProperties);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3
   *
   * <p>Invokes <code>ConfigureSerialization</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Integer @Nullable []> callConfigureSerializationDetailed(
      @Nullable KeyValuePair @Nullable [] serializationFilterProperties) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3
   *
   * <p>Invokes <code>ConfigureSerialization</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Integer @Nullable []> callConfigureSerializationDetailed(
      MethodCallOptions options, @Nullable KeyValuePair @Nullable [] serializationFilterProperties)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3
   *
   * <p>Invokes <code>ConfigureSerialization</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Integer @Nullable []>>
      callConfigureSerializationDetailedAsync(
          @Nullable KeyValuePair @Nullable [] serializationFilterProperties);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.3
   *
   * <p>Invokes <code>ConfigureSerialization</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Integer @Nullable []>>
      callConfigureSerializationDetailedAsync(
          MethodCallOptions options,
          @Nullable KeyValuePair @Nullable [] serializationFilterProperties);
}
