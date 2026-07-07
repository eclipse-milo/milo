/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.UUID;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.Out;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationUpdateTargetType;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.1">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.1</a>
 */
public interface ConfigurationFileType extends FileType {
  QualifiedProperty<DateTime> LAST_UPDATE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastUpdateTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<UInteger> CURRENT_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CurrentVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<Double> ACTIVITY_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ActivityTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<NodeId> SUPPORTED_DATA_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportedDataType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  DateTime getLastUpdateTime();

  void setLastUpdateTime(DateTime value);

  PropertyType getLastUpdateTimeNode();

  UInteger getCurrentVersion();

  void setCurrentVersion(UInteger value);

  PropertyType getCurrentVersionNode();

  Double getActivityTimeout();

  void setActivityTimeout(Double value);

  PropertyType getActivityTimeoutNode();

  NodeId getSupportedDataType();

  void setSupportedDataType(NodeId value);

  PropertyType getSupportedDataTypeNode();

  MethodNode getConfirmUpdateMethodNode();

  MethodNode getCloseAndUpdateMethodNode();

  abstract class ConfirmUpdateMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public ConfirmUpdateMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "UpdateId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14")
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
      UUID updateId = (UUID) inputValues[0].getValue();
      invoke(context, updateId);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, UUID updateId)
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
                  "VersionToUpdate",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "Targets",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15538")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "RevertAfterTime",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RestartDelayTime",
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
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "UpdateResults",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "NewVersion",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "UpdateId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14")
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
      UInteger fileHandle = (UInteger) inputValues[0].getValue();
      UInteger versionToUpdate = (UInteger) inputValues[1].getValue();
      ConfigurationUpdateTargetType[] targets =
          (ConfigurationUpdateTargetType[]) inputValues[2].getValue();
      Double revertAfterTime = (Double) inputValues[3].getValue();
      Double restartDelayTime = (Double) inputValues[4].getValue();
      Out<StatusCode[]> updateResults = new Out<>();
      Out<UInteger> newVersion = new Out<>();
      Out<UUID> updateId = new Out<>();
      invoke(
          context,
          fileHandle,
          versionToUpdate,
          targets,
          revertAfterTime,
          restartDelayTime,
          updateResults,
          newVersion,
          updateId);
      return new Variant[] {
        new Variant(updateResults.get()), new Variant(newVersion.get()), new Variant(updateId.get())
      };
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        UInteger fileHandle,
        UInteger versionToUpdate,
        ConfigurationUpdateTargetType[] targets,
        Double revertAfterTime,
        Double restartDelayTime,
        Out<StatusCode[]> updateResults,
        Out<UInteger> newVersion,
        Out<UUID> updateId)
        throws UaException;
  }
}
