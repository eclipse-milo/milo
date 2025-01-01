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

import static org.eclipse.milo.opcua.sdk.server.events.EventContentFilter.subtypeOf;

import org.eclipse.milo.opcua.sdk.server.events.FilterContext;
import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.events.ValidationException;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.jspecify.annotations.Nullable;

public class OfType implements Operator<Boolean> {

  OfType() {}

  @Override
  public void validate(FilterContext context, FilterOperand[] operands) throws ValidationException {
    if (operands.length < 1) {
      throw new ValidationException(StatusCodes.Bad_FilterOperandCountMismatch);
    }
  }

  @Nullable
  @Override
  public Boolean apply(
      OperatorContext context, BaseEventTypeNode eventNode, FilterOperand[] operands)
      throws UaException {

    validate(context, operands);

    Object value = context.resolve(operands[0], eventNode);

    if (value instanceof NodeId) {
      NodeId eventTypeDefinitionId = eventNode.getTypeDefinitionNode().getNodeId();

      NodeId targetTypeDefinitionId = (NodeId) value;

      return eventTypeDefinitionId.equals(targetTypeDefinitionId)
          || subtypeOf(eventTypeDefinitionId, targetTypeDefinitionId, context.getServer());
    } else {
      return false;
    }
  }
}
