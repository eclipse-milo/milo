/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.2</a>
 */
public interface PubSubTransportLimitsExceedEventType extends PubSubStatusEventType {
  QualifiedProperty<UInteger> ACTUAL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Actual",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAXIMUM =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Maximum",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  UInteger getActual();

  void setActual(UInteger value);

  PropertyType getActualNode();

  UInteger getMaximum();

  void setMaximum(UInteger value);

  PropertyType getMaximumNode();
}
