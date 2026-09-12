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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.AnnotationDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.QuantityDimension;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getSymbol() throws UaException;

  /** Sets the existing node's local value. */
  void setSymbol(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readSymbol() throws UaException;

  /** Writes the value remotely. */
  void writeSymbol(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readSymbolAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSymbolAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSymbolNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSymbolNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable AnnotationDataType @Nullable [] getAnnotation() throws UaException;

  /** Sets the existing node's local value. */
  void setAnnotation(@Nullable AnnotationDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable AnnotationDataType @Nullable [] readAnnotation() throws UaException;

  /** Writes the value remotely. */
  void writeAnnotation(@Nullable AnnotationDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable AnnotationDataType @Nullable []> readAnnotationAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAnnotationAsync(
      @Nullable AnnotationDataType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getAnnotationNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getAnnotationNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getConversionService() throws UaException;

  /** Sets the existing node's local value. */
  void setConversionService(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readConversionService() throws UaException;

  /** Writes the value remotely. */
  void writeConversionService(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readConversionServiceAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConversionServiceAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConversionServiceNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getConversionServiceNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable QuantityDimension getDimension() throws UaException;

  /** Sets the existing node's local value. */
  void setDimension(@Nullable QuantityDimension value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable QuantityDimension readDimension() throws UaException;

  /** Writes the value remotely. */
  void writeDimension(@Nullable QuantityDimension value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable QuantityDimension> readDimensionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDimensionAsync(@Nullable QuantityDimension value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDimensionNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDimensionNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseObjectType getServerUnitsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseObjectType> getServerUnitsNodeAsync();
}
