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
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerStatusType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.1</a>
 */
public interface ServerType extends BaseObjectType {
  QualifiedProperty<String[]> SERVER_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<String[]> NAMESPACE_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NamespaceArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<UInteger> URIS_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UrisVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<UByte> SERVICE_LEVEL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServiceLevel",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=3"),
          -1,
          UByte.class);

  QualifiedProperty<Boolean> AUDITING =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Auditing",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<DateTime> ESTIMATED_RETURN_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EstimatedReturnTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  QualifiedProperty<TimeZoneDataType> LOCAL_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LocalTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=8912"),
          -1,
          TimeZoneDataType.class);

  String[] getServerArray();

  void setServerArray(String[] value);

  PropertyType getServerArrayNode();

  String[] getNamespaceArray();

  void setNamespaceArray(String[] value);

  PropertyType getNamespaceArrayNode();

  UInteger getUrisVersion();

  void setUrisVersion(UInteger value);

  PropertyType getUrisVersionNode();

  UByte getServiceLevel();

  void setServiceLevel(UByte value);

  PropertyType getServiceLevelNode();

  Boolean getAuditing();

  void setAuditing(Boolean value);

  PropertyType getAuditingNode();

  DateTime getEstimatedReturnTime();

  void setEstimatedReturnTime(DateTime value);

  PropertyType getEstimatedReturnTimeNode();

  TimeZoneDataType getLocalTime();

  void setLocalTime(TimeZoneDataType value);

  PropertyType getLocalTimeNode();

  ServerStatusType getServerStatusNode();

  ServerStatusDataType getServerStatus();

  void setServerStatus(ServerStatusDataType value);

  ServerCapabilitiesType getServerCapabilitiesNode();

  ServerDiagnosticsType getServerDiagnosticsNode();

  VendorServerInfoType getVendorServerInfoNode();

  ServerRedundancyType getServerRedundancyNode();

  NamespacesType getNamespacesNode();

  MethodNode getGetMonitoredItemsMethodNode();

  MethodNode getResendDataMethodNode();

  MethodNode getSetSubscriptionDurableMethodNode();

  MethodNode getRequestServerStateChangeMethodNode();

  abstract class GetMonitoredItemsMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public GetMonitoredItemsMethod(UaMethodNode node) {
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
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
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
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ServerHandles",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "ClientHandles",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
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
      UInteger subscriptionId = (UInteger) inputValues[0].getValue();
      Out<UInteger[]> serverHandles = new Out<>();
      Out<UInteger[]> clientHandles = new Out<>();
      invoke(context, subscriptionId, serverHandles, clientHandles);
      return new Variant[] {new Variant(serverHandles.get()), new Variant(clientHandles.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        UInteger subscriptionId,
        Out<UInteger[]> serverHandles,
        Out<UInteger[]> clientHandles)
        throws UaException;
  }

  abstract class ResendDataMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public ResendDataMethod(UaMethodNode node) {
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
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
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
      UInteger subscriptionId = (UInteger) inputValues[0].getValue();
      invoke(context, subscriptionId);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, UInteger subscriptionId)
        throws UaException;
  }

  abstract class SetSubscriptionDurableMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public SetSubscriptionDurableMethod(UaMethodNode node) {
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
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "LifetimeInHours",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
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
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "RevisedLifetimeInHours",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      UInteger subscriptionId = (UInteger) inputValues[0].getValue();
      UInteger lifetimeInHours = (UInteger) inputValues[1].getValue();
      Out<UInteger> revisedLifetimeInHours = new Out<>();
      invoke(context, subscriptionId, lifetimeInHours, revisedLifetimeInHours);
      return new Variant[] {new Variant(revisedLifetimeInHours.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        UInteger subscriptionId,
        UInteger lifetimeInHours,
        Out<UInteger> revisedLifetimeInHours)
        throws UaException;
  }

  abstract class RequestServerStateChangeMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public RequestServerStateChangeMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "State",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=852")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "EstimatedReturnTime",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "SecondsTillShutdown",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "Reason",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "Restart",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1")
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
      ServerState state = (ServerState) inputValues[0].getValue();
      DateTime estimatedReturnTime = (DateTime) inputValues[1].getValue();
      UInteger secondsTillShutdown = (UInteger) inputValues[2].getValue();
      LocalizedText reason = (LocalizedText) inputValues[3].getValue();
      Boolean restart = (Boolean) inputValues[4].getValue();
      invoke(context, state, estimatedReturnTime, secondsTillShutdown, reason, restart);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        ServerState state,
        DateTime estimatedReturnTime,
        UInteger secondsTillShutdown,
        LocalizedText reason,
        Boolean restart)
        throws UaException;
  }
}
