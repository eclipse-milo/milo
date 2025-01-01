/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointType;
import org.eclipse.milo.opcua.stack.core.types.structured.IdentityMappingRuleType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class RoleTypeNode extends BaseObjectTypeNode implements RoleType {
  public RoleTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {
    super(
        client,
        nodeId,
        nodeClass,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        eventNotifier);
  }

  @Override
  public IdentityMappingRuleType[] getIdentities() throws UaException {
    PropertyTypeNode node = getIdentitiesNode();
    return cast(node.getValue().getValue().getValue(), IdentityMappingRuleType[].class);
  }

  @Override
  public void setIdentities(IdentityMappingRuleType[] value) throws UaException {
    PropertyTypeNode node = getIdentitiesNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public IdentityMappingRuleType[] readIdentities() throws UaException {
    try {
      return readIdentitiesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeIdentities(IdentityMappingRuleType[] value) throws UaException {
    try {
      writeIdentitiesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends IdentityMappingRuleType[]> readIdentitiesAsync() {
    return getIdentitiesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), IdentityMappingRuleType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeIdentitiesAsync(IdentityMappingRuleType[] identities) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), identities);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getIdentitiesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getIdentitiesNode() throws UaException {
    try {
      return getIdentitiesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIdentitiesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Identities", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getApplicationsExclude() throws UaException {
    PropertyTypeNode node = getApplicationsExcludeNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setApplicationsExclude(Boolean value) throws UaException {
    PropertyTypeNode node = getApplicationsExcludeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readApplicationsExclude() throws UaException {
    try {
      return readApplicationsExcludeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeApplicationsExclude(Boolean value) throws UaException {
    try {
      writeApplicationsExcludeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readApplicationsExcludeAsync() {
    return getApplicationsExcludeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeApplicationsExcludeAsync(Boolean applicationsExclude) {
    DataValue value = DataValue.valueOnly(new Variant(applicationsExclude));
    return getApplicationsExcludeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getApplicationsExcludeNode() throws UaException {
    try {
      return getApplicationsExcludeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getApplicationsExcludeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ApplicationsExclude",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String[] getApplications() throws UaException {
    PropertyTypeNode node = getApplicationsNode();
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setApplications(String[] value) throws UaException {
    PropertyTypeNode node = getApplicationsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String[] readApplications() throws UaException {
    try {
      return readApplicationsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeApplications(String[] value) throws UaException {
    try {
      writeApplicationsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String[]> readApplicationsAsync() {
    return getApplicationsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeApplicationsAsync(String[] applications) {
    DataValue value = DataValue.valueOnly(new Variant(applications));
    return getApplicationsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getApplicationsNode() throws UaException {
    try {
      return getApplicationsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getApplicationsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "Applications",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getEndpointsExclude() throws UaException {
    PropertyTypeNode node = getEndpointsExcludeNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setEndpointsExclude(Boolean value) throws UaException {
    PropertyTypeNode node = getEndpointsExcludeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readEndpointsExclude() throws UaException {
    try {
      return readEndpointsExcludeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeEndpointsExclude(Boolean value) throws UaException {
    try {
      writeEndpointsExcludeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readEndpointsExcludeAsync() {
    return getEndpointsExcludeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeEndpointsExcludeAsync(Boolean endpointsExclude) {
    DataValue value = DataValue.valueOnly(new Variant(endpointsExclude));
    return getEndpointsExcludeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getEndpointsExcludeNode() throws UaException {
    try {
      return getEndpointsExcludeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEndpointsExcludeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "EndpointsExclude",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public EndpointType[] getEndpoints() throws UaException {
    PropertyTypeNode node = getEndpointsNode();
    return cast(node.getValue().getValue().getValue(), EndpointType[].class);
  }

  @Override
  public void setEndpoints(EndpointType[] value) throws UaException {
    PropertyTypeNode node = getEndpointsNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public EndpointType[] readEndpoints() throws UaException {
    try {
      return readEndpointsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeEndpoints(EndpointType[] value) throws UaException {
    try {
      writeEndpointsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends EndpointType[]> readEndpointsAsync() {
    return getEndpointsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), EndpointType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeEndpointsAsync(EndpointType[] endpoints) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), endpoints);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getEndpointsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getEndpointsNode() throws UaException {
    try {
      return getEndpointsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEndpointsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Endpoints", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getCustomConfiguration() throws UaException {
    PropertyTypeNode node = getCustomConfigurationNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setCustomConfiguration(Boolean value) throws UaException {
    PropertyTypeNode node = getCustomConfigurationNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readCustomConfiguration() throws UaException {
    try {
      return readCustomConfigurationAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCustomConfiguration(Boolean value) throws UaException {
    try {
      writeCustomConfigurationAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readCustomConfigurationAsync() {
    return getCustomConfigurationNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCustomConfigurationAsync(Boolean customConfiguration) {
    DataValue value = DataValue.valueOnly(new Variant(customConfiguration));
    return getCustomConfigurationNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getCustomConfigurationNode() throws UaException {
    try {
      return getCustomConfigurationNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCustomConfigurationNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CustomConfiguration",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
