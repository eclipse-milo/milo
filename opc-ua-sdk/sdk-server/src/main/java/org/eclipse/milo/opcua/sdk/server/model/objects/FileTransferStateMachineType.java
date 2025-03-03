/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.6">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.6</a>
 */
public interface FileTransferStateMachineType extends FiniteStateMachineType {
  InitialStateType getIdleNode();

  StateType getReadPrepareNode();

  StateType getReadTransferNode();

  StateType getApplyWriteNode();

  StateType getErrorNode();

  TransitionType getIdleToReadPrepareNode();

  TransitionType getReadPrepareToReadTransferNode();

  TransitionType getReadTransferToIdleNode();

  TransitionType getIdleToApplyWriteNode();

  TransitionType getApplyWriteToIdleNode();

  TransitionType getReadPrepareToErrorNode();

  TransitionType getReadTransferToErrorNode();

  TransitionType getApplyWriteToErrorNode();

  TransitionType getErrorToIdleNode();

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
