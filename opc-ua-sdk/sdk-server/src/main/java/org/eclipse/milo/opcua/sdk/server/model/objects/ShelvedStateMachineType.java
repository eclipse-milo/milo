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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.1">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.1</a>
 */
public interface ShelvedStateMachineType extends FiniteStateMachineType {
  QualifiedProperty<Double> UNSHELVE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UnshelveTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  Double getUnshelveTime();

  void setUnshelveTime(Double value);

  PropertyType getUnshelveTimeNode();

  StateType getUnshelvedNode();

  StateType getTimedShelvedNode();

  StateType getOneShotShelvedNode();

  TransitionType getUnshelvedToTimedShelvedNode();

  TransitionType getUnshelvedToOneShotShelvedNode();

  TransitionType getTimedShelvedToUnshelvedNode();

  TransitionType getTimedShelvedToOneShotShelvedNode();

  TransitionType getOneShotShelvedToUnshelvedNode();

  TransitionType getOneShotShelvedToTimedShelvedNode();

  MethodNode getTimedShelveMethodNode();

  MethodNode getTimedShelve2MethodNode();

  MethodNode getUnshelveMethodNode();

  MethodNode getUnshelve2MethodNode();

  MethodNode getOneShotShelveMethodNode();

  MethodNode getOneShotShelve2MethodNode();

  abstract class TimedShelveMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public TimedShelveMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ShelvingTime",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
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
      Double shelvingTime = (Double) inputValues[0].getValue();
      invoke(context, shelvingTime);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Double shelvingTime)
        throws UaException;
  }

  abstract class TimedShelve2Method extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public TimedShelve2Method(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ShelvingTime",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
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
      Double shelvingTime = (Double) inputValues[0].getValue();
      LocalizedText comment = (LocalizedText) inputValues[1].getValue();
      invoke(context, shelvingTime, comment);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        Double shelvingTime,
        LocalizedText comment)
        throws UaException;
  }

  abstract class UnshelveMethod extends AbstractMethodInvocationHandler {
    public UnshelveMethod(UaMethodNode node) {
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

  abstract class Unshelve2Method extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public Unshelve2Method(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
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
      LocalizedText comment = (LocalizedText) inputValues[0].getValue();
      invoke(context, comment);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, LocalizedText comment)
        throws UaException;
  }

  abstract class OneShotShelveMethod extends AbstractMethodInvocationHandler {
    public OneShotShelveMethod(UaMethodNode node) {
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

  abstract class OneShotShelve2Method extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public OneShotShelve2Method(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Comment",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
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
      LocalizedText comment = (LocalizedText) inputValues[0].getValue();
      invoke(context, comment);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, LocalizedText comment)
        throws UaException;
  }
}
