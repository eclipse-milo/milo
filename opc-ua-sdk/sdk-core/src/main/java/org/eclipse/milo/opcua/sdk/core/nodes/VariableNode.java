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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;

public interface VariableNode extends Node {

  DataValue getValue();

  NodeId getDataType();

  Integer getValueRank();

  UInteger[] getArrayDimensions();

  UByte getAccessLevel();

  UByte getUserAccessLevel();

  Double getMinimumSamplingInterval();

  Boolean getHistorizing();

  /**
   * Get the AccessLevelEx attribute.
   *
   * <p>The AccessLevelEx Attribute is used to indicate how the Value of a Variable can be accessed
   * (read/write), and if it contains current and/or historic data and its atomicity.
   *
   * <p>See OPC UA Part 3, section 5.6.2.
   *
   * @return if this attribute is present, the AccessLevelEx ({@link AccessLevelExType}).
   */
  AccessLevelExType getAccessLevelEx();

  /**
   * Set the Value attribute of this Node.
   *
   * @param value the Value to set.
   */
  void setValue(DataValue value);

  /**
   * Set the DataType attribute of this Node.
   *
   * @param dataType the DataType to set.
   */
  void setDataType(NodeId dataType);

  void setValueRank(Integer valueRank);

  void setArrayDimensions(UInteger[] arrayDimensions);

  void setAccessLevel(UByte accessLevel);

  void setUserAccessLevel(UByte userAccessLevel);

  void setMinimumSamplingInterval(Double minimumSamplingInterval);

  void setHistorizing(Boolean historizing);

  /**
   * Set the AccessLevelEx attribute.
   *
   * @param accessLevelEx the AccessLevelEx to set.
   * @see #getAccessLevelEx()
   */
  void setAccessLevelEx(AccessLevelExType accessLevelEx);
}
