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

public class Or implements Operator<Boolean> {

  Or() {}

  @Override
  public void validate(FilterContext context, FilterOperand[] operands) throws ValidationException {
    OperatorUtil.validateMinOperandCount(operands, 2);
  }

  @Nullable
  @Override
  public Boolean apply(
      OperatorContext context, BaseEventTypeNode eventNode, FilterOperand[] operands)
      throws UaException {

    validate(context, operands);

    // Part 4 Table 124 defines three-valued logical OR; do not collapse NULL to FALSE here. A
    // determining TRUE short-circuits before the other operand is resolved, so an error (or cost)
    // resolving the non-determining operand cannot turn a TRUE result into a thrown exception.
    Boolean lhs = asBoolean(OperatorUtil.resolve(context, eventNode, operands[0]));

    if (Boolean.TRUE.equals(lhs)) {
      return true;
    }

    Boolean rhs = asBoolean(OperatorUtil.resolve(context, eventNode, operands[1]));

    if (Boolean.TRUE.equals(rhs)) {
      return true;
    } else if (lhs == null || rhs == null) {
      return null;
    } else {
      return false;
    }
  }

  @Nullable
  private static Boolean asBoolean(@Nullable Object value) {
    return OperatorUtil.toBoolean(value);
  }
}
