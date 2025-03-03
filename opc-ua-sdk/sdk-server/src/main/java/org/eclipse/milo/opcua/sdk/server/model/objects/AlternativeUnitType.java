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
import org.eclipse.milo.opcua.stack.core.types.structured.LinearConversionDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.4">https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.4</a>
 */
public interface AlternativeUnitType extends UnitType {
  QualifiedProperty<LinearConversionDataType> LINEAR_CONVERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LinearConversion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32435"),
          -1,
          LinearConversionDataType.class);

  QualifiedProperty<String> MATH_ML_CONVERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MathMLConversion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> MATH_ML_INVERSE_CONVERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MathMLInverseConversion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  LinearConversionDataType getLinearConversion();

  void setLinearConversion(LinearConversionDataType value);

  PropertyType getLinearConversionNode();

  String getMathMlConversion();

  void setMathMlConversion(String value);

  PropertyType getMathMlConversionNode();

  String getMathMlInverseConversion();

  void setMathMlInverseConversion(String value);

  PropertyType getMathMlInverseConversionNode();
}
