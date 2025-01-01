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
import org.eclipse.milo.opcua.sdk.server.model.variables.ConditionVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.2</a>
 */
public interface ConditionType extends BaseEventType {
  QualifiedProperty<NodeId> CONDITION_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<LocalizedText> CONDITION_CLASS_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionClassName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<String> CONDITION_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<NodeId> BRANCH_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BranchId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<Boolean> RETAIN =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Retain",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SUPPORTS_FILTERED_RETAIN =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportsFilteredRetain",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<String> CLIENT_USER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientUserId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  NodeId getConditionClassId();

  void setConditionClassId(NodeId value);

  PropertyType getConditionClassIdNode();

  LocalizedText getConditionClassName();

  void setConditionClassName(LocalizedText value);

  PropertyType getConditionClassNameNode();

  String getConditionName();

  void setConditionName(String value);

  PropertyType getConditionNameNode();

  NodeId getBranchId();

  void setBranchId(NodeId value);

  PropertyType getBranchIdNode();

  Boolean getRetain();

  void setRetain(Boolean value);

  PropertyType getRetainNode();

  Boolean getSupportsFilteredRetain();

  void setSupportsFilteredRetain(Boolean value);

  PropertyType getSupportsFilteredRetainNode();

  String getClientUserId();

  void setClientUserId(String value);

  PropertyType getClientUserIdNode();

  TwoStateVariableType getEnabledStateNode();

  LocalizedText getEnabledState();

  void setEnabledState(LocalizedText value);

  ConditionVariableType getQualityNode();

  StatusCode getQuality();

  void setQuality(StatusCode value);

  ConditionVariableType getLastSeverityNode();

  UShort getLastSeverity();

  void setLastSeverity(UShort value);

  ConditionVariableType getCommentNode();

  LocalizedText getComment();

  void setComment(LocalizedText value);

  MethodNode getDisableMethodNode();

  MethodNode getEnableMethodNode();

  MethodNode getAddCommentMethodNode();

  MethodNode getConditionRefreshMethodNode();

  MethodNode getConditionRefresh2MethodNode();

  abstract class DisableMethod extends AbstractMethodInvocationHandler {
    public DisableMethod(UaMethodNode node) {
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

  abstract class EnableMethod extends AbstractMethodInvocationHandler {
    public EnableMethod(UaMethodNode node) {
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

  abstract class AddCommentMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public AddCommentMethod(UaMethodNode node) {
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

  abstract class ConditionRefreshMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public ConditionRefreshMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "SubscriptionId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=288")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "The identifier for the subscription to refresh."))
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
      UInteger subscriptionId = (UInteger) inputValues[0].getValue();
      invoke(context, subscriptionId);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, UInteger subscriptionId)
        throws UaException;
  }

  abstract class ConditionRefresh2Method extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public ConditionRefresh2Method(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "SubscriptionId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=288")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "The identifier for the subscription to refresh.")),
              new Argument(
                  "MonitoredItemId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=288")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "The identifier for the monitored item to refresh."))
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
      UInteger subscriptionId = (UInteger) inputValues[0].getValue();
      UInteger monitoredItemId = (UInteger) inputValues[1].getValue();
      invoke(context, subscriptionId, monitoredItemId);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        UInteger subscriptionId,
        UInteger monitoredItemId)
        throws UaException;
  }
}
