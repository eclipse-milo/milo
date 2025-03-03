/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.AnnotationDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.QuantityDimension;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.1</a>
 */
public interface QuantityType extends BaseObjectType {
  QualifiedProperty<LocalizedText> SYMBOL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Symbol",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<AnnotationDataType[]> ANNOTATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Annotation",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32434"),
          1,
          AnnotationDataType[].class);

  QualifiedProperty<String> CONVERSION_SERVICE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConversionService",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  QualifiedProperty<QuantityDimension> DIMENSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Dimension",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32438"),
          -1,
          QuantityDimension.class);

  /**
   * Get the local value of the Symbol Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Symbol Node.
   * @throws UaException if an error occurs creating or getting the Symbol Node.
   */
  LocalizedText getSymbol() throws UaException;

  /**
   * Set the local value of the Symbol Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Symbol Node.
   * @throws UaException if an error occurs creating or getting the Symbol Node.
   */
  void setSymbol(LocalizedText value) throws UaException;

  /**
   * Read the value of the Symbol Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readSymbol() throws UaException;

  /**
   * Write a new value for the Symbol Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSymbol(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSymbol}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readSymbolAsync();

  /**
   * An asynchronous implementation of {@link #writeSymbol}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSymbolAsync(LocalizedText value);

  /**
   * Get the Symbol {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Symbol {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSymbolNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSymbolNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSymbolNodeAsync();

  /**
   * Get the local value of the Annotation Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Annotation Node.
   * @throws UaException if an error occurs creating or getting the Annotation Node.
   */
  AnnotationDataType[] getAnnotation() throws UaException;

  /**
   * Set the local value of the Annotation Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Annotation Node.
   * @throws UaException if an error occurs creating or getting the Annotation Node.
   */
  void setAnnotation(AnnotationDataType[] value) throws UaException;

  /**
   * Read the value of the Annotation Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link AnnotationDataType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  AnnotationDataType[] readAnnotation() throws UaException;

  /**
   * Write a new value for the Annotation Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link AnnotationDataType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAnnotation(AnnotationDataType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAnnotation}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends AnnotationDataType[]> readAnnotationAsync();

  /**
   * An asynchronous implementation of {@link #writeAnnotation}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAnnotationAsync(AnnotationDataType[] value);

  /**
   * Get the Annotation {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Annotation {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getAnnotationNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAnnotationNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getAnnotationNodeAsync();

  /**
   * Get the local value of the ConversionService Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConversionService Node.
   * @throws UaException if an error occurs creating or getting the ConversionService Node.
   */
  String getConversionService() throws UaException;

  /**
   * Set the local value of the ConversionService Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConversionService Node.
   * @throws UaException if an error occurs creating or getting the ConversionService Node.
   */
  void setConversionService(String value) throws UaException;

  /**
   * Read the value of the ConversionService Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readConversionService() throws UaException;

  /**
   * Write a new value for the ConversionService Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConversionService(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConversionService}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readConversionServiceAsync();

  /**
   * An asynchronous implementation of {@link #writeConversionService}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConversionServiceAsync(String value);

  /**
   * Get the ConversionService {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConversionService {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConversionServiceNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConversionServiceNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConversionServiceNodeAsync();

  /**
   * Get the local value of the Dimension Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Dimension Node.
   * @throws UaException if an error occurs creating or getting the Dimension Node.
   */
  QuantityDimension getDimension() throws UaException;

  /**
   * Set the local value of the Dimension Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Dimension Node.
   * @throws UaException if an error occurs creating or getting the Dimension Node.
   */
  void setDimension(QuantityDimension value) throws UaException;

  /**
   * Read the value of the Dimension Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link QuantityDimension} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  QuantityDimension readDimension() throws UaException;

  /**
   * Write a new value for the Dimension Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link QuantityDimension} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDimension(QuantityDimension value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDimension}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends QuantityDimension> readDimensionAsync();

  /**
   * An asynchronous implementation of {@link #writeDimension}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDimensionAsync(QuantityDimension value);

  /**
   * Get the Dimension {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Dimension {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDimensionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDimensionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDimensionNodeAsync();

  /**
   * Get the ServerUnits {@link BaseObjectType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ServerUnits {@link BaseObjectType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseObjectType getServerUnitsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getServerUnitsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseObjectType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseObjectType> getServerUnitsNodeAsync();
}
