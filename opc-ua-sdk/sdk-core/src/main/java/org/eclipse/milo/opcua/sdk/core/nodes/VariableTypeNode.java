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

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public interface VariableTypeNode extends Node {

  DataValue getValue();

  NodeId getDataType();

  Integer getValueRank();

  UInteger[] getArrayDimensions();

  Boolean getIsAbstract();

  void setValue(DataValue value);

  void setDataType(NodeId dataType);

  void setValueRank(Integer valueRank);

  void setArrayDimensions(UInteger[] arrayDimensions);

  void setIsAbstract(Boolean isAbstract);
}
