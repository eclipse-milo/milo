/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.nodes;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

public final class VariableTypeNodeProperties {

  private VariableTypeNodeProperties() {}

  /**
   * The NodeVersion Property is used to indicate the version of a Node. It does not apply to
   * Properties.
   *
   * <p>The NodeVersion Property is updated each time a Reference is added or removed from the Node
   * the Property belongs to.
   *
   * <p>Although the relationship of a Variable to its DataType is not modelled using References,
   * changes to the DataType Attribute of a Variable lead to an update of the NodeVersion Property.
   *
   * <p>The usage of this Property is optional.
   */
  public static final QualifiedProperty<String> NodeVersion =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NodeVersion",
          NodeIds.String.expanded(),
          ValueRanks.Scalar,
          String.class);
}
