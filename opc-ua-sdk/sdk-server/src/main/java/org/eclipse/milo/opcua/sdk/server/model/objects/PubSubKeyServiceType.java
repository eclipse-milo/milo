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
import org.eclipse.milo.opcua.sdk.server.methods.Out;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.1</a>
 */
public interface PubSubKeyServiceType extends BaseObjectType {
  MethodNode getGetSecurityKeysMethodNode();

  MethodNode getGetSecurityGroupMethodNode();

  SecurityGroupFolderType getSecurityGroupsNode();

  PubSubKeyPushTargetFolderType getKeyPushTargetsNode();

  abstract class GetSecurityKeysMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public GetSecurityKeysMethod(UaMethodNode node) {
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
                  "StartingTokenId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=288")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RequestedKeyCount",
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
                  "SecurityPolicyUri",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "FirstTokenId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=288")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "Keys",
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
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      String securityGroupId = (String) inputValues[0].getValue();
      UInteger startingTokenId = (UInteger) inputValues[1].getValue();
      UInteger requestedKeyCount = (UInteger) inputValues[2].getValue();
      Out<String> securityPolicyUri = new Out<>();
      Out<UInteger> firstTokenId = new Out<>();
      Out<ByteString[]> keys = new Out<>();
      Out<Double> timeToNextKey = new Out<>();
      Out<Double> keyLifetime = new Out<>();
      invoke(
          context,
          securityGroupId,
          startingTokenId,
          requestedKeyCount,
          securityPolicyUri,
          firstTokenId,
          keys,
          timeToNextKey,
          keyLifetime);
      return new Variant[] {
        new Variant(securityPolicyUri.get()),
        new Variant(firstTokenId.get()),
        new Variant(keys.get()),
        new Variant(timeToNextKey.get()),
        new Variant(keyLifetime.get())
      };
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String securityGroupId,
        UInteger startingTokenId,
        UInteger requestedKeyCount,
        Out<String> securityPolicyUri,
        Out<UInteger> firstTokenId,
        Out<ByteString[]> keys,
        Out<Double> timeToNextKey,
        Out<Double> keyLifetime)
        throws UaException;
  }

  abstract class GetSecurityGroupMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public GetSecurityGroupMethod(UaMethodNode node) {
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
                  "SecurityGroupNodeId",
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
      String securityGroupId = (String) inputValues[0].getValue();
      Out<NodeId> securityGroupNodeId = new Out<>();
      invoke(context, securityGroupId, securityGroupNodeId);
      return new Variant[] {new Variant(securityGroupNodeId.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String securityGroupId,
        Out<NodeId> securityGroupNodeId)
        throws UaException;
  }
}
