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
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.PubSubDiagnosticsCounterType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.2</a>
 */
public interface PubSubDiagnosticsType extends BaseObjectType {
  BaseDataVariableType getDiagnosticsLevelNode();

  DiagnosticsLevel getDiagnosticsLevel();

  void setDiagnosticsLevel(DiagnosticsLevel value);

  PubSubDiagnosticsCounterType getTotalInformationNode();

  UInteger getTotalInformation();

  void setTotalInformation(UInteger value);

  PubSubDiagnosticsCounterType getTotalErrorNode();

  UInteger getTotalError();

  void setTotalError(UInteger value);

  MethodNode getResetMethodNode();

  BaseDataVariableType getSubErrorNode();

  Boolean getSubError();

  void setSubError(Boolean value);

  BaseObjectType getCountersNode();

  BaseObjectType getLiveValuesNode();

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
