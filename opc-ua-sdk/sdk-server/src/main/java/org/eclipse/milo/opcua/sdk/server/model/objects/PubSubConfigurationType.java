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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationValueDataType;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.1</a>
 */
public interface PubSubConfigurationType extends FileType {
  MethodNode getReserveIdsMethodNode();

  MethodNode getCloseAndUpdateMethodNode();

  abstract class ReserveIdsMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public ReserveIdsMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "TransportProfileUri",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "NumReqWriterGroupIds",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "NumReqDataSetWriterIds",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5")
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
                  "DefaultPublisherId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "WriterGroupIds",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "DataSetWriterIds",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5")
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
      String transportProfileUri = (String) inputValues[0].getValue();
      UShort numReqWriterGroupIds = (UShort) inputValues[1].getValue();
      UShort numReqDataSetWriterIds = (UShort) inputValues[2].getValue();
      Out<Object> defaultPublisherId = new Out<>();
      Out<UShort[]> writerGroupIds = new Out<>();
      Out<UShort[]> dataSetWriterIds = new Out<>();
      invoke(
          context,
          transportProfileUri,
          numReqWriterGroupIds,
          numReqDataSetWriterIds,
          defaultPublisherId,
          writerGroupIds,
          dataSetWriterIds);
      return new Variant[] {
        new Variant(defaultPublisherId.get()),
        new Variant(writerGroupIds.get()),
        new Variant(dataSetWriterIds.get())
      };
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String transportProfileUri,
        UShort numReqWriterGroupIds,
        UShort numReqDataSetWriterIds,
        Out<Object> defaultPublisherId,
        Out<UShort[]> writerGroupIds,
        Out<UShort[]> dataSetWriterIds)
        throws UaException;
  }

  abstract class CloseAndUpdateMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public CloseAndUpdateMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "FileHandle",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RequireCompleteUpdate",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "ConfigurationReferences",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=25519")
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
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ChangesApplied",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "ReferencesResults",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "ConfigurationValues",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=25520")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "ConfigurationObjects",
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
      UInteger fileHandle = (UInteger) inputValues[0].getValue();
      Boolean requireCompleteUpdate = (Boolean) inputValues[1].getValue();
      PubSubConfigurationRefDataType[] configurationReferences =
          (PubSubConfigurationRefDataType[]) inputValues[2].getValue();
      Out<Boolean> changesApplied = new Out<>();
      Out<StatusCode[]> referencesResults = new Out<>();
      Out<PubSubConfigurationValueDataType[]> configurationValues = new Out<>();
      Out<NodeId[]> configurationObjects = new Out<>();
      invoke(
          context,
          fileHandle,
          requireCompleteUpdate,
          configurationReferences,
          changesApplied,
          referencesResults,
          configurationValues,
          configurationObjects);
      return new Variant[] {
        new Variant(changesApplied.get()),
        new Variant(referencesResults.get()),
        new Variant(configurationValues.get()),
        new Variant(configurationObjects.get())
      };
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        UInteger fileHandle,
        Boolean requireCompleteUpdate,
        PubSubConfigurationRefDataType[] configurationReferences,
        Out<Boolean> changesApplied,
        Out<StatusCode[]> referencesResults,
        Out<PubSubConfigurationValueDataType[]> configurationValues,
        Out<NodeId[]> configurationObjects)
        throws UaException;
  }
}
