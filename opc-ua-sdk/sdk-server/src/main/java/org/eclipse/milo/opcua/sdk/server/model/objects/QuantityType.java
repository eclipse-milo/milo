package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.AnnotationDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.QuantityDimension;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the QuantityType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.1">Model
 *     documentation</a>
 */
public interface QuantityType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32475L);

  /**
   * Returns the optional Annotation child, a PropertyType with DataType AnnotationDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getAnnotationNode();

  /**
   * Returns the Value of the Annotation child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable AnnotationDataType @Nullable [] getAnnotation();

  /**
   * Sets the Value of the Annotation child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAnnotation(@Nullable AnnotationDataType @Nullable [] value);

  /**
   * Returns the optional ConversionService child, a PropertyType with DataType UriString.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getConversionServiceNode();

  /**
   * Returns the Value of the ConversionService child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getConversionService();

  /**
   * Sets the Value of the ConversionService child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConversionService(@Nullable String value);

  /**
   * Returns the mandatory Dimension child, a PropertyType with DataType QuantityDimension.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDimensionNode();

  /**
   * Returns the Value of the Dimension child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable QuantityDimension getDimension();

  /**
   * Sets the Value of the Dimension child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDimension(@Nullable QuantityDimension value);

  /**
   * Returns the mandatory ServerUnits child, a BaseObjectType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.2">BaseObjectType
   *     documentation</a>
   */
  BaseObjectTypeNode getServerUnitsNode();

  /**
   * Returns the optional Symbol child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSymbolNode();

  /**
   * Returns the Value of the Symbol child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getSymbol();

  /**
   * Sets the Value of the Symbol child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSymbol(@Nullable LocalizedText value);
}
