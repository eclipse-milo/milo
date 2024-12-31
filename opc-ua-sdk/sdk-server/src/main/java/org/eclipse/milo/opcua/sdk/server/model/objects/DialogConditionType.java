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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableType;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.2</a>
 */
public interface DialogConditionType extends ConditionType {
  QualifiedProperty<LocalizedText> PROMPT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Prompt",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<LocalizedText[]> RESPONSE_OPTION_SET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ResponseOptionSet",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  QualifiedProperty<Integer> DEFAULT_RESPONSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultResponse",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6"),
          -1,
          Integer.class);

  QualifiedProperty<Integer> OK_RESPONSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OkResponse",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6"),
          -1,
          Integer.class);

  QualifiedProperty<Integer> CANCEL_RESPONSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CancelResponse",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6"),
          -1,
          Integer.class);

  QualifiedProperty<Integer> LAST_RESPONSE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastResponse",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6"),
          -1,
          Integer.class);

  LocalizedText getPrompt();

  void setPrompt(LocalizedText value);

  PropertyType getPromptNode();

  LocalizedText[] getResponseOptionSet();

  void setResponseOptionSet(LocalizedText[] value);

  PropertyType getResponseOptionSetNode();

  Integer getDefaultResponse();

  void setDefaultResponse(Integer value);

  PropertyType getDefaultResponseNode();

  Integer getOkResponse();

  void setOkResponse(Integer value);

  PropertyType getOkResponseNode();

  Integer getCancelResponse();

  void setCancelResponse(Integer value);

  PropertyType getCancelResponseNode();

  Integer getLastResponse();

  void setLastResponse(Integer value);

  PropertyType getLastResponseNode();

  TwoStateVariableType getEnabledStateNode();

  LocalizedText getEnabledState();

  void setEnabledState(LocalizedText value);

  TwoStateVariableType getDialogStateNode();

  LocalizedText getDialogState();

  void setDialogState(LocalizedText value);

  MethodNode getRespondMethodNode();

  MethodNode getRespond2MethodNode();

  abstract class RespondMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public RespondMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "SelectedResponse",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6")
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
      Integer selectedResponse = (Integer) inputValues[0].getValue();
      invoke(context, selectedResponse);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Integer selectedResponse)
        throws UaException;
  }

  abstract class Respond2Method extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public Respond2Method(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "SelectedResponse",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6")
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
      Integer selectedResponse = (Integer) inputValues[0].getValue();
      LocalizedText comment = (LocalizedText) inputValues[1].getValue();
      invoke(context, selectedResponse, comment);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        Integer selectedResponse,
        LocalizedText comment)
        throws UaException;
  }
}
