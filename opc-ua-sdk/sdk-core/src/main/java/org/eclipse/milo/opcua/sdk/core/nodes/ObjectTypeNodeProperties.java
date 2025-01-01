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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

public final class ObjectTypeNodeProperties {

  private ObjectTypeNodeProperties() {}

  /**
   * The NodeVersion Property is used to indicate the version of a Node.
   *
   * <p>The NodeVersion Property is updated each time a Reference is added or removed from the Node
   * the Property belongs to.
   */
  public static final QualifiedProperty<String> NodeVersion =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NodeVersion",
          NodeIds.String.expanded(),
          ValueRanks.Scalar,
          String.class);

  /**
   * The Icon Property is provides an image that can be used by Clients when displaying the Node.
   *
   * <p>It is expected that the Icon Property contains a relatively small image.
   */
  public static final QualifiedProperty<ByteString> Icon =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "Icon", NodeIds.Image.expanded(), ValueRanks.Scalar, ByteString.class);
}
