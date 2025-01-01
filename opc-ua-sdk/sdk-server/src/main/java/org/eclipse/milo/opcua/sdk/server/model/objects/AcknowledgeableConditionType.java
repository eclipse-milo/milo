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
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.2</a>
 */
public interface AcknowledgeableConditionType extends ConditionType {
  TwoStateVariableType getEnabledStateNode();

  LocalizedText getEnabledState();

  void setEnabledState(LocalizedText value);

  TwoStateVariableType getAckedStateNode();

  LocalizedText getAckedState();

  void setAckedState(LocalizedText value);

  TwoStateVariableType getConfirmedStateNode();

  LocalizedText getConfirmedState();

  void setConfirmedState(LocalizedText value);

  MethodNode getAcknowledgeMethodNode();

  MethodNode getConfirmMethodNode();

  abstract class AcknowledgeMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public AcknowledgeMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "EventId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "The identifier for the event to comment.")),
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "The comment to add to the condition."))
            };
          });
    }

    @Override
    public Argument[] getOutputArguments() {
      return new Argument[] {};
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      ByteString eventId = (ByteString) inputValues[0].getValue();
      LocalizedText comment = (LocalizedText) inputValues[1].getValue();
      invoke(context, eventId, comment);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        ByteString eventId,
        LocalizedText comment)
        throws UaException;
  }

  abstract class ConfirmMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public ConfirmMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "EventId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "The identifier for the event to comment.")),
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "The comment to add to the condition."))
            };
          });
    }

    @Override
    public Argument[] getOutputArguments() {
      return new Argument[] {};
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      ByteString eventId = (ByteString) inputValues[0].getValue();
      LocalizedText comment = (LocalizedText) inputValues[1].getValue();
      invoke(context, eventId, comment);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        ByteString eventId,
        LocalizedText comment)
        throws UaException;
  }
}
