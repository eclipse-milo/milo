/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.NumberRange;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.7">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.7</a>
 */
public interface AnalogNumberUnitRangeType extends AnalogUnitRangeType {
  QualifiedProperty<NumberRange> EU_NUMBER_RANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EUNumberRange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23903"),
          -1,
          NumberRange.class);

  NumberRange getEuNumberRange();

  void setEuNumberRange(NumberRange value);

  PropertyType getEuNumberRangeNode();
}
