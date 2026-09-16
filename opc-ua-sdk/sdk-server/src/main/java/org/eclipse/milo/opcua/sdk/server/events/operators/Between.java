/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.jspecify.annotations.Nullable;

public class Between implements Operator<Boolean> {

  Between() {}

  @Override
  public void validate(FilterContext context, FilterOperand[] operands) throws ValidationException {
    OperatorUtil.validateMinOperandCount(operands, 3);
  }

  @Nullable
  @Override
  public Boolean apply(
      OperatorContext context, BaseEventTypeNode eventNode, FilterOperand[] operands)
      throws UaException {

    validate(context, operands);

    Object value = OperatorUtil.resolve(context, eventNode, operands[0]);
    Object lowerBound = OperatorUtil.resolve(context, eventNode, operands[1]);
    Object upperBound = OperatorUtil.resolve(context, eventNode, operands[2]);

    if (value == null || lowerBound == null || upperBound == null) {
      return null;
    }

    OperatorUtil.TernaryOperands commonOperands =
        OperatorUtil.convertToCommonType(value, lowerBound, upperBound);

    if (commonOperands == null) {
      return false;
    }

    Integer lowerCompare =
        OperatorUtil.compareOrdered(
            commonOperands.dataType(), commonOperands.operand0(), commonOperands.operand1());
    Integer upperCompare =
        OperatorUtil.compareOrdered(
            commonOperands.dataType(), commonOperands.operand0(), commonOperands.operand2());

    if (lowerCompare == null || upperCompare == null) {
      return false;
    }

    return lowerCompare >= 0 && upperCompare <= 0;
  }
}
