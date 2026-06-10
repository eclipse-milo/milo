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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.Out;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecordMask;
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecordsDataType;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.2">https://reference.opcfoundation.org/v105/Core/docs/Part26/5.2</a>
 */
public interface LogObjectType extends BaseObjectType {
  QualifiedProperty<UInteger> MAX_RECORDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxRecords",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<Double> MAX_STORAGE_DURATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxStorageDuration",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UShort> MINIMUM_SEVERITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MinimumSeverity",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  UInteger getMaxRecords();

  void setMaxRecords(UInteger value);

  PropertyType getMaxRecordsNode();

  Double getMaxStorageDuration();

  void setMaxStorageDuration(Double value);

  PropertyType getMaxStorageDurationNode();

  UShort getMinimumSeverity();

  void setMinimumSeverity(UShort value);

  PropertyType getMinimumSeverityNode();

  MethodNode getGetRecordsMethodNode();

  MethodNode getReleaseContinuationPointMethodNode();

  abstract class GetRecordsMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public GetRecordsMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "StartTime",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "EndTime",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "MaxReturnRecords",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "MinimumSeverity",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RequestMask",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19749")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "ContinuationPointIn",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
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
                  "Results",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19745")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "ContinuationPointOut",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
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
      DateTime startTime = (DateTime) inputValues[0].getValue();
      DateTime endTime = (DateTime) inputValues[1].getValue();
      UInteger maxReturnRecords = (UInteger) inputValues[2].getValue();
      UShort minimumSeverity = (UShort) inputValues[3].getValue();
      LogRecordMask requestMask = (LogRecordMask) inputValues[4].getValue();
      ByteString continuationPointIn = (ByteString) inputValues[5].getValue();
      Out<LogRecordsDataType> results = new Out<>();
      Out<ByteString> continuationPointOut = new Out<>();
      invoke(
          context,
          startTime,
          endTime,
          maxReturnRecords,
          minimumSeverity,
          requestMask,
          continuationPointIn,
          results,
          continuationPointOut);
      return new Variant[] {new Variant(results.get()), new Variant(continuationPointOut.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        DateTime startTime,
        DateTime endTime,
        UInteger maxReturnRecords,
        UShort minimumSeverity,
        LogRecordMask requestMask,
        ByteString continuationPointIn,
        Out<LogRecordsDataType> results,
        Out<ByteString> continuationPointOut)
        throws UaException;
  }

  abstract class ReleaseContinuationPointMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public ReleaseContinuationPointMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ContinuationPointIn",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
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
      ByteString continuationPointIn = (ByteString) inputValues[0].getValue();
      invoke(context, continuationPointIn);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, ByteString continuationPointIn)
        throws UaException;
  }
}
