/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
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

  LocalizedText getSymbol();

  void setSymbol(LocalizedText value);

  PropertyType getSymbolNode();

  AnnotationDataType[] getAnnotation();

  void setAnnotation(AnnotationDataType[] value);

  PropertyType getAnnotationNode();

  String getConversionService();

  void setConversionService(String value);

  PropertyType getConversionServiceNode();

  QuantityDimension getDimension();

  void setDimension(QuantityDimension value);

  PropertyType getDimensionNode();

  BaseObjectType getServerUnitsNode();
}
