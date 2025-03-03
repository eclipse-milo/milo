/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.6.5">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.6.5</a>
 */
public interface ExpressionGuardVariableType extends GuardVariableType {
  QualifiedProperty<ContentFilter> EXPRESSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Expression",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=586"),
          -1,
          ContentFilter.class);

  ContentFilter getExpression();

  void setExpression(ContentFilter value);

  PropertyType getExpressionNode();
}
