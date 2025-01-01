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
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointType;
import org.eclipse.milo.opcua.stack.core.types.structured.IdentityMappingRuleType;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.1</a>
 */
public interface RoleType extends BaseObjectType {
  QualifiedProperty<IdentityMappingRuleType[]> IDENTITIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Identities",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15634"),
          1,
          IdentityMappingRuleType[].class);

  QualifiedProperty<Boolean> APPLICATIONS_EXCLUDE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationsExclude",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<String[]> APPLICATIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Applications",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<Boolean> ENDPOINTS_EXCLUDE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EndpointsExclude",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<EndpointType[]> ENDPOINTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Endpoints",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15528"),
          1,
          EndpointType[].class);

  QualifiedProperty<Boolean> CUSTOM_CONFIGURATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CustomConfiguration",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  IdentityMappingRuleType[] getIdentities();

  void setIdentities(IdentityMappingRuleType[] value);

  PropertyType getIdentitiesNode();

  Boolean getApplicationsExclude();

  void setApplicationsExclude(Boolean value);

  PropertyType getApplicationsExcludeNode();

  String[] getApplications();

  void setApplications(String[] value);

  PropertyType getApplicationsNode();

  Boolean getEndpointsExclude();

  void setEndpointsExclude(Boolean value);

  PropertyType getEndpointsExcludeNode();

  EndpointType[] getEndpoints();

  void setEndpoints(EndpointType[] value);

  PropertyType getEndpointsNode();

  Boolean getCustomConfiguration();

  void setCustomConfiguration(Boolean value);

  PropertyType getCustomConfigurationNode();

  MethodNode getAddIdentityMethodNode();

  MethodNode getRemoveIdentityMethodNode();

  MethodNode getAddApplicationMethodNode();

  MethodNode getRemoveApplicationMethodNode();

  MethodNode getAddEndpointMethodNode();

  MethodNode getRemoveEndpointMethodNode();

  abstract class AddIdentityMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public AddIdentityMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Rule",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15634")
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
      IdentityMappingRuleType rule = (IdentityMappingRuleType) inputValues[0].getValue();
      invoke(context, rule);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, IdentityMappingRuleType rule)
        throws UaException;
  }

  abstract class RemoveIdentityMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public RemoveIdentityMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Rule",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15634")
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
      IdentityMappingRuleType rule = (IdentityMappingRuleType) inputValues[0].getValue();
      invoke(context, rule);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, IdentityMappingRuleType rule)
        throws UaException;
  }

  abstract class AddApplicationMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public AddApplicationMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ApplicationUri",
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
      return new Argument[] {};
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      String applicationUri = (String) inputValues[0].getValue();
      invoke(context, applicationUri);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, String applicationUri)
        throws UaException;
  }

  abstract class RemoveApplicationMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public RemoveApplicationMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ApplicationUri",
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
      return new Argument[] {};
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      String applicationUri = (String) inputValues[0].getValue();
      invoke(context, applicationUri);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, String applicationUri)
        throws UaException;
  }

  abstract class AddEndpointMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public AddEndpointMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Endpoint",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15528")
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
      EndpointType endpoint = (EndpointType) inputValues[0].getValue();
      invoke(context, endpoint);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, EndpointType endpoint)
        throws UaException;
  }

  abstract class RemoveEndpointMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public RemoveEndpointMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Endpoint",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15528")
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
      EndpointType endpoint = (EndpointType) inputValues[0].getValue();
      invoke(context, endpoint);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, EndpointType endpoint)
        throws UaException;
  }
}
