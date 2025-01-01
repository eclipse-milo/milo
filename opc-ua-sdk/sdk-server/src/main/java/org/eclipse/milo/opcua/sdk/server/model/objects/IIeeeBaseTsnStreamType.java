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

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.7">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.7</a>
 */
public interface IIeeeBaseTsnStreamType extends BaseInterfaceType {
  BaseDataVariableType getStreamIdNode();

  UByte[] getStreamId();

  void setStreamId(UByte[] value);

  BaseDataVariableType getStreamNameNode();

  String getStreamName();

  void setStreamName(String value);

  BaseDataVariableType getStateNode();

  TsnStreamState getState();

  void setState(TsnStreamState value);

  BaseDataVariableType getAccumulatedLatencyNode();

  UInteger getAccumulatedLatency();

  void setAccumulatedLatency(UInteger value);

  BaseDataVariableType getSrClassIdNode();

  UByte getSrClassId();

  void setSrClassId(UByte value);
}
