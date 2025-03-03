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
import org.eclipse.milo.opcua.sdk.server.model.variables.AudioVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.2</a>
 */
public interface AlarmConditionType extends AcknowledgeableConditionType {
  QualifiedProperty<NodeId> INPUT_NODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InputNode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<Boolean> SUPPRESSED_OR_SHELVED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SuppressedOrShelved",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Double> MAX_TIME_SHELVED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxTimeShelved",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Boolean> AUDIBLE_ENABLED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AudibleEnabled",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Double> ON_DELAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OnDelay",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> OFF_DELAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OffDelay",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> RE_ALARM_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReAlarmTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  NodeId getInputNode();

  void setInputNode(NodeId value);

  PropertyType getInputNodeNode();

  Boolean getSuppressedOrShelved();

  void setSuppressedOrShelved(Boolean value);

  PropertyType getSuppressedOrShelvedNode();

  Double getMaxTimeShelved();

  void setMaxTimeShelved(Double value);

  PropertyType getMaxTimeShelvedNode();

  Boolean getAudibleEnabled();

  void setAudibleEnabled(Boolean value);

  PropertyType getAudibleEnabledNode();

  Double getOnDelay();

  void setOnDelay(Double value);

  PropertyType getOnDelayNode();

  Double getOffDelay();

  void setOffDelay(Double value);

  PropertyType getOffDelayNode();

  Double getReAlarmTime();

  void setReAlarmTime(Double value);

  PropertyType getReAlarmTimeNode();

  TwoStateVariableType getEnabledStateNode();

  LocalizedText getEnabledState();

  void setEnabledState(LocalizedText value);

  TwoStateVariableType getActiveStateNode();

  LocalizedText getActiveState();

  void setActiveState(LocalizedText value);

  TwoStateVariableType getSuppressedStateNode();

  LocalizedText getSuppressedState();

  void setSuppressedState(LocalizedText value);

  TwoStateVariableType getOutOfServiceStateNode();

  LocalizedText getOutOfServiceState();

  void setOutOfServiceState(LocalizedText value);

  ShelvedStateMachineType getShelvingStateNode();

  AudioVariableType getAudibleSoundNode();

  ByteString getAudibleSound();

  void setAudibleSound(ByteString value);

  TwoStateVariableType getSilenceStateNode();

  LocalizedText getSilenceState();

  void setSilenceState(LocalizedText value);

  BaseDataVariableType getFirstInGroupFlagNode();

  Boolean getFirstInGroupFlag();

  void setFirstInGroupFlag(Boolean value);

  AlarmGroupType getFirstInGroupNode();

  TwoStateVariableType getLatchedStateNode();

  LocalizedText getLatchedState();

  void setLatchedState(LocalizedText value);

  BaseDataVariableType getReAlarmRepeatCountNode();

  Short getReAlarmRepeatCount();

  void setReAlarmRepeatCount(Short value);

  MethodNode getSilenceMethodNode();

  MethodNode getSuppressMethodNode();

  MethodNode getSuppress2MethodNode();

  MethodNode getUnsuppressMethodNode();

  MethodNode getUnsuppress2MethodNode();

  MethodNode getRemoveFromServiceMethodNode();

  MethodNode getRemoveFromService2MethodNode();

  MethodNode getPlaceInServiceMethodNode();

  MethodNode getPlaceInService2MethodNode();

  MethodNode getResetMethodNode();

  MethodNode getReset2MethodNode();

  MethodNode getGetGroupMembershipsMethodNode();

  abstract class SilenceMethod extends AbstractMethodInvocationHandler {
    public SilenceMethod(UaMethodNode node) {
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

  abstract class SuppressMethod extends AbstractMethodInvocationHandler {
    public SuppressMethod(UaMethodNode node) {
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

  abstract class Suppress2Method extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public Suppress2Method(UaMethodNode node) {
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

  abstract class UnsuppressMethod extends AbstractMethodInvocationHandler {
    public UnsuppressMethod(UaMethodNode node) {
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

  abstract class Unsuppress2Method extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public Unsuppress2Method(UaMethodNode node) {
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

  abstract class RemoveFromServiceMethod extends AbstractMethodInvocationHandler {
    public RemoveFromServiceMethod(UaMethodNode node) {
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

  abstract class RemoveFromService2Method extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public RemoveFromService2Method(UaMethodNode node) {
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

  abstract class PlaceInServiceMethod extends AbstractMethodInvocationHandler {
    public PlaceInServiceMethod(UaMethodNode node) {
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

  abstract class PlaceInService2Method extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public PlaceInService2Method(UaMethodNode node) {
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

  abstract class Reset2Method extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public Reset2Method(UaMethodNode node) {
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

  abstract class GetGroupMembershipsMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public GetGroupMembershipsMethod(UaMethodNode node) {
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
                  "Groups",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17")
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
      Out<NodeId[]> groups = new Out<>();
      invoke(context, groups);
      return new Variant[] {new Variant(groups.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Out<NodeId[]> groups)
        throws UaException;
  }
}
