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

import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.AlarmRateVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/9.2</a>
 */
public interface AlarmMetricsType extends BaseObjectType {
  BaseDataVariableType getAlarmCountNode();

  UInteger getAlarmCount();

  void setAlarmCount(UInteger value);

  BaseDataVariableType getStartTimeNode();

  DateTime getStartTime();

  void setStartTime(DateTime value);

  BaseDataVariableType getMaximumActiveStateNode();

  Double getMaximumActiveState();

  void setMaximumActiveState(Double value);

  BaseDataVariableType getMaximumUnAckNode();

  Double getMaximumUnAck();

  void setMaximumUnAck(Double value);

  AlarmRateVariableType getCurrentAlarmRateNode();

  Double getCurrentAlarmRate();

  void setCurrentAlarmRate(Double value);

  AlarmRateVariableType getMaximumAlarmRateNode();

  Double getMaximumAlarmRate();

  void setMaximumAlarmRate(Double value);

  BaseDataVariableType getMaximumReAlarmCountNode();

  UInteger getMaximumReAlarmCount();

  void setMaximumReAlarmCount(UInteger value);

  AlarmRateVariableType getAverageAlarmRateNode();

  Double getAverageAlarmRate();

  void setAverageAlarmRate(Double value);

  MethodNode getResetMethodNode();

  abstract class ResetMethod extends AbstractMethodInvocationHandler {
    public ResetMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return new Argument[] {};
    }

    @Override
    public Argument[] getOutputArguments() {
      return new Argument[] {};
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      invoke(context);
      return new Variant[] {};
    }

    protected abstract void invoke(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException;
  }
}
