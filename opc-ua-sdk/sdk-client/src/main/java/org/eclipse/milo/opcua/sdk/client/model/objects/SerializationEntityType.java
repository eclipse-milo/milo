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
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part25/6.3.1</a>
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

  /**
   * Get the local value of the IncludeReferenceTypes Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the IncludeReferenceTypes Node.
   * @throws UaException if an error occurs creating or getting the IncludeReferenceTypes Node.
   */
  NodeId[] getIncludeReferenceTypes() throws UaException;

  /**
   * Set the local value of the IncludeReferenceTypes Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the IncludeReferenceTypes Node.
   * @throws UaException if an error occurs creating or getting the IncludeReferenceTypes Node.
   */
  void setIncludeReferenceTypes(NodeId[] value) throws UaException;

  /**
   * Read the value of the IncludeReferenceTypes Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link NodeId[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId[] readIncludeReferenceTypes() throws UaException;

  /**
   * Write a new value for the IncludeReferenceTypes Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link NodeId[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeIncludeReferenceTypes(NodeId[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readIncludeReferenceTypes}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId[]> readIncludeReferenceTypesAsync();

  /**
   * An asynchronous implementation of {@link #writeIncludeReferenceTypes}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeIncludeReferenceTypesAsync(NodeId[] value);

  /**
   * Get the IncludeReferenceTypes {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the IncludeReferenceTypes {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getIncludeReferenceTypesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIncludeReferenceTypesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getIncludeReferenceTypesNodeAsync();

  /**
   * Get the local value of the ExcludeReferenceTypes Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ExcludeReferenceTypes Node.
   * @throws UaException if an error occurs creating or getting the ExcludeReferenceTypes Node.
   */
  NodeId[] getExcludeReferenceTypes() throws UaException;

  /**
   * Set the local value of the ExcludeReferenceTypes Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ExcludeReferenceTypes Node.
   * @throws UaException if an error occurs creating or getting the ExcludeReferenceTypes Node.
   */
  void setExcludeReferenceTypes(NodeId[] value) throws UaException;

  /**
   * Read the value of the ExcludeReferenceTypes Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link NodeId[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId[] readExcludeReferenceTypes() throws UaException;

  /**
   * Write a new value for the ExcludeReferenceTypes Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link NodeId[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeExcludeReferenceTypes(NodeId[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readExcludeReferenceTypes}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId[]> readExcludeReferenceTypesAsync();

  /**
   * An asynchronous implementation of {@link #writeExcludeReferenceTypes}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeExcludeReferenceTypesAsync(NodeId[] value);

  /**
   * Get the ExcludeReferenceTypes {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ExcludeReferenceTypes {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getExcludeReferenceTypesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getExcludeReferenceTypesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getExcludeReferenceTypesNodeAsync();

  /**
   * Get the local value of the SerializationDepth Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SerializationDepth Node.
   * @throws UaException if an error occurs creating or getting the SerializationDepth Node.
   */
  UShort getSerializationDepth() throws UaException;

  /**
   * Set the local value of the SerializationDepth Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SerializationDepth Node.
   * @throws UaException if an error occurs creating or getting the SerializationDepth Node.
   */
  void setSerializationDepth(UShort value) throws UaException;

  /**
   * Read the value of the SerializationDepth Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readSerializationDepth() throws UaException;

  /**
   * Write a new value for the SerializationDepth Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSerializationDepth(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSerializationDepth}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readSerializationDepthAsync();

  /**
   * An asynchronous implementation of {@link #writeSerializationDepth}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSerializationDepthAsync(UShort value);

  /**
   * Get the SerializationDepth {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SerializationDepth {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSerializationDepthNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSerializationDepthNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSerializationDepthNodeAsync();

  /**
   * Get the local value of the ConsiderSubElementSerializationProperties Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConsiderSubElementSerializationProperties Node.
   * @throws UaException if an error occurs creating or getting the
   *     ConsiderSubElementSerializationProperties Node.
   */
  Boolean getConsiderSubElementSerializationProperties() throws UaException;

  /**
   * Set the local value of the ConsiderSubElementSerializationProperties Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConsiderSubElementSerializationProperties Node.
   * @throws UaException if an error occurs creating or getting the
   *     ConsiderSubElementSerializationProperties Node.
   */
  void setConsiderSubElementSerializationProperties(Boolean value) throws UaException;

  /**
   * Read the value of the ConsiderSubElementSerializationProperties Node from the server and update
   * the local value if the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readConsiderSubElementSerializationProperties() throws UaException;

  /**
   * Write a new value for the ConsiderSubElementSerializationProperties Node to the server and
   * update the local value if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConsiderSubElementSerializationProperties(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConsiderSubElementSerializationProperties}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readConsiderSubElementSerializationPropertiesAsync();

  /**
   * An asynchronous implementation of {@link #writeConsiderSubElementSerializationProperties}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConsiderSubElementSerializationPropertiesAsync(Boolean value);

  /**
   * Get the ConsiderSubElementSerializationProperties {@link PropertyType} Node, or {@code null} if
   * it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConsiderSubElementSerializationProperties {@link PropertyType} Node, or {@code
   *     null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConsiderSubElementSerializationPropertiesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConsiderSubElementSerializationPropertiesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConsiderSubElementSerializationPropertiesNodeAsync();

  /**
   * Get the local value of the CustomMetaDataProperties Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CustomMetaDataProperties Node.
   * @throws UaException if an error occurs creating or getting the CustomMetaDataProperties Node.
   */
  KeyValuePair[] getCustomMetaDataProperties() throws UaException;

  /**
   * Set the local value of the CustomMetaDataProperties Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CustomMetaDataProperties Node.
   * @throws UaException if an error occurs creating or getting the CustomMetaDataProperties Node.
   */
  void setCustomMetaDataProperties(KeyValuePair[] value) throws UaException;

  /**
   * Read the value of the CustomMetaDataProperties Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link KeyValuePair[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  KeyValuePair[] readCustomMetaDataProperties() throws UaException;

  /**
   * Write a new value for the CustomMetaDataProperties Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link KeyValuePair[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCustomMetaDataProperties(KeyValuePair[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCustomMetaDataProperties}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends KeyValuePair[]> readCustomMetaDataPropertiesAsync();

  /**
   * An asynchronous implementation of {@link #writeCustomMetaDataProperties}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCustomMetaDataPropertiesAsync(KeyValuePair[] value);

  /**
   * Get the CustomMetaDataProperties {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CustomMetaDataProperties {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCustomMetaDataPropertiesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCustomMetaDataPropertiesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCustomMetaDataPropertiesNodeAsync();

  /**
   * Get the local value of the CustomMetaDataRef Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CustomMetaDataRef Node.
   * @throws UaException if an error occurs creating or getting the CustomMetaDataRef Node.
   */
  NodeId getCustomMetaDataRef() throws UaException;

  /**
   * Set the local value of the CustomMetaDataRef Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CustomMetaDataRef Node.
   * @throws UaException if an error occurs creating or getting the CustomMetaDataRef Node.
   */
  void setCustomMetaDataRef(NodeId value) throws UaException;

  /**
   * Read the value of the CustomMetaDataRef Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readCustomMetaDataRef() throws UaException;

  /**
   * Write a new value for the CustomMetaDataRef Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCustomMetaDataRef(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCustomMetaDataRef}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readCustomMetaDataRefAsync();

  /**
   * An asynchronous implementation of {@link #writeCustomMetaDataRef}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCustomMetaDataRefAsync(NodeId value);

  /**
   * Get the CustomMetaDataRef {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CustomMetaDataRef {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCustomMetaDataRefNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCustomMetaDataRefNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCustomMetaDataRefNodeAsync();

  /**
   * Get the local value of the IncludeStatus Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the IncludeStatus Node.
   * @throws UaException if an error occurs creating or getting the IncludeStatus Node.
   */
  Boolean getIncludeStatus() throws UaException;

  /**
   * Set the local value of the IncludeStatus Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the IncludeStatus Node.
   * @throws UaException if an error occurs creating or getting the IncludeStatus Node.
   */
  void setIncludeStatus(Boolean value) throws UaException;

  /**
   * Read the value of the IncludeStatus Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readIncludeStatus() throws UaException;

  /**
   * Write a new value for the IncludeStatus Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeIncludeStatus(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readIncludeStatus}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readIncludeStatusAsync();

  /**
   * An asynchronous implementation of {@link #writeIncludeStatus}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeIncludeStatusAsync(Boolean value);

  /**
   * Get the IncludeStatus {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the IncludeStatus {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getIncludeStatusNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIncludeStatusNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getIncludeStatusNodeAsync();

  /**
   * Get the local value of the IncludeSourceTimestamp Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the IncludeSourceTimestamp Node.
   * @throws UaException if an error occurs creating or getting the IncludeSourceTimestamp Node.
   */
  Boolean getIncludeSourceTimestamp() throws UaException;

  /**
   * Set the local value of the IncludeSourceTimestamp Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the IncludeSourceTimestamp Node.
   * @throws UaException if an error occurs creating or getting the IncludeSourceTimestamp Node.
   */
  void setIncludeSourceTimestamp(Boolean value) throws UaException;

  /**
   * Read the value of the IncludeSourceTimestamp Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readIncludeSourceTimestamp() throws UaException;

  /**
   * Write a new value for the IncludeSourceTimestamp Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeIncludeSourceTimestamp(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readIncludeSourceTimestamp}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readIncludeSourceTimestampAsync();

  /**
   * An asynchronous implementation of {@link #writeIncludeSourceTimestamp}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeIncludeSourceTimestampAsync(Boolean value);

  /**
   * Get the IncludeSourceTimestamp {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the IncludeSourceTimestamp {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getIncludeSourceTimestampNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIncludeSourceTimestampNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getIncludeSourceTimestampNodeAsync();

  /**
   * Get the local value of the IncludeDictionaryReference Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the IncludeDictionaryReference Node.
   * @throws UaException if an error occurs creating or getting the IncludeDictionaryReference Node.
   */
  Boolean getIncludeDictionaryReference() throws UaException;

  /**
   * Set the local value of the IncludeDictionaryReference Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the IncludeDictionaryReference Node.
   * @throws UaException if an error occurs creating or getting the IncludeDictionaryReference Node.
   */
  void setIncludeDictionaryReference(Boolean value) throws UaException;

  /**
   * Read the value of the IncludeDictionaryReference Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readIncludeDictionaryReference() throws UaException;

  /**
   * Write a new value for the IncludeDictionaryReference Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeIncludeDictionaryReference(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readIncludeDictionaryReference}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readIncludeDictionaryReferenceAsync();

  /**
   * An asynchronous implementation of {@link #writeIncludeDictionaryReference}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeIncludeDictionaryReferenceAsync(Boolean value);

  /**
   * Get the IncludeDictionaryReference {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the IncludeDictionaryReference {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getIncludeDictionaryReferenceNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIncludeDictionaryReferenceNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getIncludeDictionaryReferenceNodeAsync();

  /**
   * Get the local value of the SerializedData Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SerializedData Node.
   * @throws UaException if an error occurs creating or getting the SerializedData Node.
   */
  Structure getSerializedData() throws UaException;

  /**
   * Set the local value of the SerializedData Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SerializedData Node.
   * @throws UaException if an error occurs creating or getting the SerializedData Node.
   */
  void setSerializedData(Structure value) throws UaException;

  /**
   * Read the value of the SerializedData Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Structure} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Structure readSerializedData() throws UaException;

  /**
   * Write a new value for the SerializedData Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Structure} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSerializedData(Structure value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSerializedData}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Structure> readSerializedDataAsync();

  /**
   * An asynchronous implementation of {@link #writeSerializedData}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSerializedDataAsync(Structure value);

  /**
   * Get the SerializedData {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SerializedData {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getSerializedDataNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSerializedDataNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSerializedDataNodeAsync();
}
