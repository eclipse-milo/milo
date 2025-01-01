/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;

public class EventFilterBuilder {

  private final List<SimpleAttributeOperand> selectClauses = new ArrayList<>();
  private ContentFilter whereClause = new ContentFilter(null);

  public EventFilterBuilder select(NodeId typeDefinitionId, QualifiedName browseName) {
    SimpleAttributeOperand operand =
        new SimpleAttributeOperand(
            typeDefinitionId, new QualifiedName[] {browseName}, AttributeId.Value.uid(), null);

    return select(operand);
  }

  public EventFilterBuilder select(NodeId typeDefinitionId, QualifiedName... browseNames) {
    SimpleAttributeOperand operand =
        new SimpleAttributeOperand(typeDefinitionId, browseNames, AttributeId.Value.uid(), null);

    return select(operand);
  }

  public EventFilterBuilder select(NodeId typeDefinitionId, List<QualifiedName> browseNames) {
    SimpleAttributeOperand operand =
        new SimpleAttributeOperand(
            typeDefinitionId,
            browseNames.toArray(new QualifiedName[0]),
            AttributeId.Value.uid(),
            null);

    return select(operand);
  }

  public EventFilterBuilder select(SimpleAttributeOperand selectClause) {
    selectClauses.add(selectClause);

    return this;
  }

  public EventFilterBuilder where(ContentFilter whereClause) {
    this.whereClause = whereClause;
    return this;
  }

  public EventFilter build() {
    return new EventFilter(selectClauses.toArray(new SimpleAttributeOperand[0]), whereClause);
  }
}
