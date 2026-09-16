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
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.jspecify.annotations.Nullable;

/**
 * A binary operator where the operands undergo implicit conversion to the same type based on their
 * type precedence before being operated on.
 */
abstract class ImplicitConversionBinaryOperator implements Operator<Boolean> {

  @Override
  public void validate(FilterContext context, FilterOperand[] operands) throws ValidationException {
    if (operands.length < 2) {
      throw new ValidationException(StatusCodes.Bad_FilterOperandCountMismatch);
    }
  }

  @Nullable
  @Override
  public Boolean apply(
      OperatorContext context, BaseEventTypeNode eventNode, FilterOperand[] operands)
      throws UaException {

    validate(context, operands);

    FilterOperand op0 = operands[0];
    FilterOperand op1 = operands[1];

    Object value0 = context.resolve(op0, eventNode);
    Object value1 = context.resolve(op1, eventNode);

    if (value0 == null || value1 == null) {
      return null;
    }

    OperatorUtil.BinaryOperands commonOperands = OperatorUtil.convertToCommonType(value0, value1);

    if (commonOperands == null) {
      return false;
    }

    return apply(
        context,
        eventNode,
        commonOperands.dataType(),
        commonOperands.operand0(),
        commonOperands.operand1());
  }

  @Nullable
  protected abstract Boolean apply(
      OperatorContext context,
      BaseEventTypeNode eventNode,
      OpcUaDataType dataType,
      @Nullable Object operand0,
      @Nullable Object operand1)
      throws UaException;
}
