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

public class BitwiseOr implements Operator<Object> {

  BitwiseOr() {}

  @Override
  public void validate(FilterContext context, FilterOperand[] operands) throws ValidationException {
    OperatorUtil.validateMinOperandCount(operands, 2);
  }

  @Nullable
  @Override
  public Object apply(
      OperatorContext context, BaseEventTypeNode eventNode, FilterOperand[] operands)
      throws UaException {

    validate(context, operands);

    Object lhs = OperatorUtil.resolve(context, eventNode, operands[0]);
    Object rhs = OperatorUtil.resolve(context, eventNode, operands[1]);

    if (lhs == null || rhs == null) {
      return null;
    }

    return OperatorUtil.bitwise(lhs, rhs, (a, b) -> a | b);
  }
}
