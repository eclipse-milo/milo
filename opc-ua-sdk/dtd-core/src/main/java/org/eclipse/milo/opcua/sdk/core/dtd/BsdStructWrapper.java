/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.dtd;

import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;

public record BsdStructWrapper<T>(DataType dataType, T object) implements UaStructuredType {

  @Override
  public ExpandedNodeId getTypeId() {
    return dataType.getNodeId().expanded();
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    return dataType.getBinaryEncodingId().expanded();
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    return ExpandedNodeId.NULL_VALUE;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    return ExpandedNodeId.NULL_VALUE;
  }

  @Override
  public String getTypeName() {
    return dataType.getBrowseName().getName();
  }
}
