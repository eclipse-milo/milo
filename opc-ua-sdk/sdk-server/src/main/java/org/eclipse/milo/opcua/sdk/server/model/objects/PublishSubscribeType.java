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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.2</a>
 */
public interface PublishSubscribeType extends PubSubKeyServiceType {
  QualifiedProperty<String[]> SUPPORTED_TRANSPORT_PROFILES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportedTransportProfiles",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<ULong> DEFAULT_DATAGRAM_PUBLISHER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultDatagramPublisherId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=9"),
          -1,
          ULong.class);

  QualifiedProperty<UInteger> CONFIGURATION_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConfigurationVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<EndpointDescription[]> DEFAULT_SECURITY_KEY_SERVICES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultSecurityKeyServices",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=312"),
          1,
          EndpointDescription[].class);

  QualifiedProperty<KeyValuePair[]> CONFIGURATION_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConfigurationProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14533"),
          1,
          KeyValuePair[].class);

  String[] getSupportedTransportProfiles();

  void setSupportedTransportProfiles(String[] value);

  PropertyType getSupportedTransportProfilesNode();

  ULong getDefaultDatagramPublisherId();

  void setDefaultDatagramPublisherId(ULong value);

  PropertyType getDefaultDatagramPublisherIdNode();

  UInteger getConfigurationVersion();

  void setConfigurationVersion(UInteger value);

  PropertyType getConfigurationVersionNode();

  EndpointDescription[] getDefaultSecurityKeyServices();

  void setDefaultSecurityKeyServices(EndpointDescription[] value);

  PropertyType getDefaultSecurityKeyServicesNode();

  KeyValuePair[] getConfigurationProperties();

  void setConfigurationProperties(KeyValuePair[] value);

  PropertyType getConfigurationPropertiesNode();

  MethodNode getSetSecurityKeysMethodNode();

  MethodNode getAddConnectionMethodNode();

  MethodNode getRemoveConnectionMethodNode();

  DataSetFolderType getPublishedDataSetsNode();

  SubscribedDataSetFolderType getSubscribedDataSetsNode();

  PubSubConfigurationType getPubSubConfigurationNode();

  PubSubStatusType getStatusNode();

  PubSubDiagnosticsRootType getDiagnosticsNode();

  PubSubCapabilitiesType getPubSubCapablitiesNode();

  FolderType getDataSetClassesNode();

  abstract class SetSecurityKeysMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public SetSecurityKeysMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "SecurityGroupId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "SecurityPolicyUri",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "CurrentTokenId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=288")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "CurrentKey",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "FutureKeys",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "TimeToNextKey",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "KeyLifetime",
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
      String securityGroupId = (String) inputValues[0].getValue();
      String securityPolicyUri = (String) inputValues[1].getValue();
      UInteger currentTokenId = (UInteger) inputValues[2].getValue();
      ByteString currentKey = (ByteString) inputValues[3].getValue();
      ByteString[] futureKeys = (ByteString[]) inputValues[4].getValue();
      Double timeToNextKey = (Double) inputValues[5].getValue();
      Double keyLifetime = (Double) inputValues[6].getValue();
      invoke(
          context,
          securityGroupId,
          securityPolicyUri,
          currentTokenId,
          currentKey,
          futureKeys,
          timeToNextKey,
          keyLifetime);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String securityGroupId,
        String securityPolicyUri,
        UInteger currentTokenId,
        ByteString currentKey,
        ByteString[] futureKeys,
        Double timeToNextKey,
        Double keyLifetime)
        throws UaException;
  }

  abstract class AddConnectionMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public AddConnectionMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Configuration",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15617")
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
                  "ConnectionId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17")
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
      PubSubConnectionDataType configuration = (PubSubConnectionDataType) inputValues[0].getValue();
      Out<NodeId> connectionId = new Out<>();
      invoke(context, configuration, connectionId);
      return new Variant[] {new Variant(connectionId.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        PubSubConnectionDataType configuration,
        Out<NodeId> connectionId)
        throws UaException;
  }

  abstract class RemoveConnectionMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public RemoveConnectionMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ConnectionId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17")
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
      NodeId connectionId = (NodeId) inputValues[0].getValue();
      invoke(context, connectionId);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, NodeId connectionId)
        throws UaException;
  }
}
