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
import org.eclipse.milo.opcua.sdk.server.methods.Out;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.1</a>
 */
public interface PublishedDataItemsType extends PublishedDataSetType {
  QualifiedProperty<PublishedVariableDataType[]> PUBLISHED_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublishedData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14273"),
          1,
          PublishedVariableDataType[].class);

  PublishedVariableDataType[] getPublishedData();

  void setPublishedData(PublishedVariableDataType[] value);

  PropertyType getPublishedDataNode();

  MethodNode getAddVariablesMethodNode();

  MethodNode getRemoveVariablesMethodNode();

  abstract class AddVariablesMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public AddVariablesMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ConfigurationVersion",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14593")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "FieldNameAliases",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "PromotedFields",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "VariablesToAdd",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14273")
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
                  "NewConfigurationVersion",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14593")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "AddResults",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19")
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
      ConfigurationVersionDataType configurationVersion =
          (ConfigurationVersionDataType) inputValues[0].getValue();
      String[] fieldNameAliases = (String[]) inputValues[1].getValue();
      Boolean[] promotedFields = (Boolean[]) inputValues[2].getValue();
      PublishedVariableDataType[] variablesToAdd =
          (PublishedVariableDataType[]) inputValues[3].getValue();
      Out<ConfigurationVersionDataType> newConfigurationVersion = new Out<>();
      Out<StatusCode[]> addResults = new Out<>();
      invoke(
          context,
          configurationVersion,
          fieldNameAliases,
          promotedFields,
          variablesToAdd,
          newConfigurationVersion,
          addResults);
      return new Variant[] {
        new Variant(newConfigurationVersion.get()), new Variant(addResults.get())
      };
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        ConfigurationVersionDataType configurationVersion,
        String[] fieldNameAliases,
        Boolean[] promotedFields,
        PublishedVariableDataType[] variablesToAdd,
        Out<ConfigurationVersionDataType> newConfigurationVersion,
        Out<StatusCode[]> addResults)
        throws UaException;
  }

  abstract class RemoveVariablesMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public RemoveVariablesMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ConfigurationVersion",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14593")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "VariablesToRemove",
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
    public Argument[] getOutputArguments() {
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "NewConfigurationVersion",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14593")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RemoveResults",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19")
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
      ConfigurationVersionDataType configurationVersion =
          (ConfigurationVersionDataType) inputValues[0].getValue();
      UInteger[] variablesToRemove = (UInteger[]) inputValues[1].getValue();
      Out<ConfigurationVersionDataType> newConfigurationVersion = new Out<>();
      Out<StatusCode[]> removeResults = new Out<>();
      invoke(
          context, configurationVersion, variablesToRemove, newConfigurationVersion, removeResults);
      return new Variant[] {
        new Variant(newConfigurationVersion.get()), new Variant(removeResults.get())
      };
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        ConfigurationVersionDataType configurationVersion,
        UInteger[] variablesToRemove,
        Out<ConfigurationVersionDataType> newConfigurationVersion,
        Out<StatusCode[]> removeResults)
        throws UaException;
  }
}
