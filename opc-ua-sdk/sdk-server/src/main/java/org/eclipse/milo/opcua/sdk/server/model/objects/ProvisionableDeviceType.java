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
import org.eclipse.milo.opcua.sdk.server.methods.Out;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.3">https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.3</a>
 */
public interface ProvisionableDeviceType extends BaseObjectType {
  QualifiedProperty<Boolean> IS_SINGLETON =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IsSingleton",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  Boolean getIsSingleton();

  void setIsSingleton(Boolean value);

  PropertyType getIsSingletonNode();

  MethodNode getRequestTicketsMethodNode();

  MethodNode getSetRegistrarEndpointsMethodNode();

  abstract class RequestTicketsMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public RequestTicketsMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return new Argument[] {};
    }

    @Override
    public Argument[] getOutputArguments() {
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Tickets",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=25726")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      Out<String[]> tickets = new Out<>();
      invoke(context, tickets);
      return new Variant[] {new Variant(tickets.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Out<String[]> tickets)
        throws UaException;
  }

  abstract class SetRegistrarEndpointsMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public SetRegistrarEndpointsMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Registrars",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=308")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
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
      ApplicationDescription[] registrars = (ApplicationDescription[]) inputValues[0].getValue();
      invoke(context, registrars);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        ApplicationDescription[] registrars)
        throws UaException;
  }
}
