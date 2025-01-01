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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.1</a>
 */
public interface PublishedEventsType extends PublishedDataSetType {
  QualifiedProperty<NodeId> PUB_SUB_EVENT_NOTIFIER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EventNotifier",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<SimpleAttributeOperand[]> SELECTED_FIELDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SelectedFields",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=601"),
          1,
          SimpleAttributeOperand[].class);

  QualifiedProperty<ContentFilter> FILTER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Filter",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=586"),
          -1,
          ContentFilter.class);

  NodeId getPubSubEventNotifier();

  void setPubSubEventNotifier(NodeId value);

  PropertyType getPubSubEventNotifierNode();

  SimpleAttributeOperand[] getSelectedFields();

  void setSelectedFields(SimpleAttributeOperand[] value);

  PropertyType getSelectedFieldsNode();

  ContentFilter getFilter();

  void setFilter(ContentFilter value);

  PropertyType getFilterNode();

  MethodNode getModifyFieldSelectionMethodNode();

  abstract class ModifyFieldSelectionMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public ModifyFieldSelectionMethod(UaMethodNode node) {
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
                  "SelectedFields",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=601")
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
      SimpleAttributeOperand[] selectedFields =
          (SimpleAttributeOperand[]) inputValues[3].getValue();
      Out<ConfigurationVersionDataType> newConfigurationVersion = new Out<>();
      invoke(
          context,
          configurationVersion,
          fieldNameAliases,
          promotedFields,
          selectedFields,
          newConfigurationVersion);
      return new Variant[] {new Variant(newConfigurationVersion.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        ConfigurationVersionDataType configurationVersion,
        String[] fieldNameAliases,
        Boolean[] promotedFields,
        SimpleAttributeOperand[] selectedFields,
        Out<ConfigurationVersionDataType> newConfigurationVersion)
        throws UaException;
  }
}
