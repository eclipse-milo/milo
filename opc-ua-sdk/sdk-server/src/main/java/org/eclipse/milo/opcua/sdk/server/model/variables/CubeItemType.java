/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.AxisInformation;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.5">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.5</a>
 */
public interface CubeItemType extends ArrayItemType {
  QualifiedProperty<AxisInformation> X_AXIS_DEFINITION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "XAxisDefinition",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12079"),
          -1,
          AxisInformation.class);

  QualifiedProperty<AxisInformation> Y_AXIS_DEFINITION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "YAxisDefinition",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12079"),
          -1,
          AxisInformation.class);

  QualifiedProperty<AxisInformation> Z_AXIS_DEFINITION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ZAxisDefinition",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12079"),
          -1,
          AxisInformation.class);

  AxisInformation getXAxisDefinition();

  void setXAxisDefinition(AxisInformation value);

  PropertyType getXAxisDefinitionNode();

  AxisInformation getYAxisDefinition();

  void setYAxisDefinition(AxisInformation value);

  PropertyType getYAxisDefinitionNode();

  AxisInformation getZAxisDefinition();

  void setZAxisDefinition(AxisInformation value);

  PropertyType getZAxisDefinitionNode();
}
