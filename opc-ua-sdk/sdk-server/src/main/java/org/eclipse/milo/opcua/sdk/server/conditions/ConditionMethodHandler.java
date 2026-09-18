/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.conditions;

import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;

/**
 * Invocation handler for a Condition instance Method whose argument definitions come from a
 * generated Method descriptor and whose behavior produces no outputs.
 *
 * <p>The argument definitions are resolved once at construction; the Method's NodeContext must be
 * set by then.
 */
final class ConditionMethodHandler extends AbstractMethodInvocationHandler {

  /** The behavior of a Condition Method that returns no outputs. */
  @FunctionalInterface
  interface Body {
    void invoke(InvocationContext context, Variant[] values) throws UaException;
  }

  private final Argument[] inputArguments;
  private final Argument[] outputArguments;
  private final Body body;

  ConditionMethodHandler(
      UaMethodNode node,
      Function<NamespaceTable, Argument[]> inputArguments,
      Function<NamespaceTable, Argument[]> outputArguments,
      Body body) {

    super(node);

    NamespaceTable namespaceTable = node.getNodeContext().getServer().getNamespaceTable();

    this.inputArguments = inputArguments.apply(namespaceTable);
    this.outputArguments = outputArguments.apply(namespaceTable);
    this.body = body;
  }

  @Override
  public Argument[] getInputArguments() {
    return inputArguments;
  }

  @Override
  public Argument[] getOutputArguments() {
    return outputArguments;
  }

  @Override
  protected Variant[] invoke(InvocationContext context, Variant[] values) throws UaException {
    body.invoke(context, values);
    return new Variant[0];
  }
}
