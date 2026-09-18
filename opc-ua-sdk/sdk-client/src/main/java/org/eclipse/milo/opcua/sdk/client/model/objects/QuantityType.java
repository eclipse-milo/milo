package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.AnnotationDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.QuantityDimension;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the QuantityType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.1">Model
 *     documentation</a>
 */
public interface QuantityType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32475L);

  QualifiedProperty<AnnotationDataType[]> Annotation_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Annotation",
          ExpandedNodeId.of(Namespaces.OPC_UA, 32434L),
          1,
          AnnotationDataType[].class);

  QualifiedProperty<String> ConversionService_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConversionService",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23751L),
          -1,
          String.class);

  QualifiedProperty<LocalizedText> Symbol_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Symbol",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  QualifiedProperty<QuantityDimension> Dimension_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Dimension",
          ExpandedNodeId.of(Namespaces.OPC_UA, 32438L),
          -1,
          QuantityDimension.class);

  /**
   * Resolves the optional Annotation child, a PropertyType with DataType AnnotationDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getAnnotationNode() throws UaException;

  /** Asynchronous form of {@link #getAnnotationNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getAnnotationNodeAsync();

  /**
   * Reads the Value of the Annotation child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable AnnotationDataType @Nullable [] readAnnotation() throws UaException;

  /**
   * Writes the Value of the Annotation child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAnnotation(@Nullable AnnotationDataType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readAnnotation()}. */
  CompletableFuture<? extends @Nullable AnnotationDataType @Nullable []> readAnnotationAsync();

  /** Asynchronous form of {@link #writeAnnotation}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAnnotationAsync(
      @Nullable AnnotationDataType @Nullable [] value);

  /**
   * Resolves the mandatory ServerUnits child, a BaseObjectType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.2">BaseObjectType
   *     documentation</a>
   */
  ObjectNode getServerUnitsNode() throws UaException;

  /** Asynchronous form of {@link #getServerUnitsNode()}. */
  CompletableFuture<? extends ObjectNode> getServerUnitsNodeAsync();

  /**
   * Resolves the optional ConversionService child, a PropertyType with DataType UriString.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getConversionServiceNode() throws UaException;

  /** Asynchronous form of {@link #getConversionServiceNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getConversionServiceNodeAsync();

  /**
   * Reads the Value of the ConversionService child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readConversionService() throws UaException;

  /**
   * Writes the Value of the ConversionService child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConversionService(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readConversionService()}. */
  CompletableFuture<? extends @Nullable String> readConversionServiceAsync();

  /** Asynchronous form of {@link #writeConversionService}; completes with the operation status. */
  CompletableFuture<StatusCode> writeConversionServiceAsync(@Nullable String value);

  /**
   * Resolves the optional Symbol child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSymbolNode() throws UaException;

  /** Asynchronous form of {@link #getSymbolNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSymbolNodeAsync();

  /**
   * Reads the Value of the Symbol child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readSymbol() throws UaException;

  /**
   * Writes the Value of the Symbol child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSymbol(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readSymbol()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readSymbolAsync();

  /** Asynchronous form of {@link #writeSymbol}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSymbolAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory Dimension child, a PropertyType with DataType QuantityDimension.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDimensionNode() throws UaException;

  /** Asynchronous form of {@link #getDimensionNode()}. */
  CompletableFuture<? extends PropertyType> getDimensionNodeAsync();

  /**
   * Reads the Value of the Dimension child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable QuantityDimension readDimension() throws UaException;

  /**
   * Writes the Value of the Dimension child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDimension(@Nullable QuantityDimension value) throws UaException;

  /** Asynchronous form of {@link #readDimension()}. */
  CompletableFuture<? extends @Nullable QuantityDimension> readDimensionAsync();

  /** Asynchronous form of {@link #writeDimension}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDimensionAsync(@Nullable QuantityDimension value);
}
