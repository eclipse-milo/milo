/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.operators;

import org.eclipse.milo.opcua.sdk.server.events.FilterContext;
import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.events.ValidationException;
import org.eclipse.milo.opcua.sdk.server.events.conversions.ImplicitConversions;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.jspecify.annotations.Nullable;

public class Cast implements Operator<Object> {

  Cast() {}

  @Override
  public void validate(FilterContext context, FilterOperand[] operands) throws ValidationException {
    if (operands.length < 2) {
      throw new ValidationException(StatusCodes.Bad_FilterOperandCountMismatch);
    }
  }

  @Nullable
  @Override
  public Object apply(
      OperatorContext context, BaseEventTypeNode eventNode, FilterOperand[] operands)
      throws UaException {

    validate(context, operands);

    FilterOperand op0 = operands[0];
    FilterOperand op1 = operands[1];

    Object sourceValue = context.resolve(op0, eventNode);

    Object dataTypeIdObject = context.resolve(op1, eventNode);

    if (dataTypeIdObject instanceof NodeId dataTypeId) {
      OpcUaDataType dataType = OpcUaDataType.fromNodeId(dataTypeId);

      if (dataType != null) {
        return ImplicitConversions.convert(sourceValue, dataType);
      } else {
        return null;
      }
    } else if (dataTypeIdObject instanceof ExpandedNodeId dataTypeId) {
      OpcUaDataType dataType = OpcUaDataType.fromNodeId(dataTypeId);

      if (dataType != null) {
        return ImplicitConversions.convert(sourceValue, dataType);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
}
