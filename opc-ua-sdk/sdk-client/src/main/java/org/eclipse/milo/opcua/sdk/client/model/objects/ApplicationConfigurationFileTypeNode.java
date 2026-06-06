/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

public class ApplicationConfigurationFileTypeNode extends ConfigurationFileTypeNode
    implements ApplicationConfigurationFileType {
  public ApplicationConfigurationFileTypeNode(
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
  public String[] getAvailableNetworks() throws UaException {
    PropertyTypeNode node = getAvailableNetworksNode();
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setAvailableNetworks(String[] value) throws UaException {
    PropertyTypeNode node = getAvailableNetworksNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String[] readAvailableNetworks() throws UaException {
    try {
      return readAvailableNetworksAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeAvailableNetworks(String[] value) throws UaException {
    try {
      writeAvailableNetworksAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends String[]> readAvailableNetworksAsync() {
    return getAvailableNetworksNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeAvailableNetworksAsync(String[] availableNetworks) {
    DataValue value = DataValue.valueOnly(new Variant(availableNetworks));
    return getAvailableNetworksNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getAvailableNetworksNode() throws UaException {
    try {
      return getAvailableNetworksNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAvailableNetworksNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "AvailableNetworks",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getAvailablePorts() throws UaException {
    PropertyTypeNode node = getAvailablePortsNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setAvailablePorts(String value) throws UaException {
    PropertyTypeNode node = getAvailablePortsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readAvailablePorts() throws UaException {
    try {
      return readAvailablePortsAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeAvailablePorts(String value) throws UaException {
    try {
      writeAvailablePortsAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends String> readAvailablePortsAsync() {
    return getAvailablePortsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeAvailablePortsAsync(String availablePorts) {
    DataValue value = DataValue.valueOnly(new Variant(availablePorts));
    return getAvailablePortsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getAvailablePortsNode() throws UaException {
    try {
      return getAvailablePortsNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAvailablePortsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "AvailablePorts", ExpandedNodeId.parse("i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getMaxEndpoints() throws UaException {
    PropertyTypeNode node = getMaxEndpointsNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxEndpoints(UShort value) throws UaException {
    PropertyTypeNode node = getMaxEndpointsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readMaxEndpoints() throws UaException {
    try {
      return readMaxEndpointsAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeMaxEndpoints(UShort value) throws UaException {
    try {
      writeMaxEndpointsAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readMaxEndpointsAsync() {
    return getMaxEndpointsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxEndpointsAsync(UShort maxEndpoints) {
    DataValue value = DataValue.valueOnly(new Variant(maxEndpoints));
    return getMaxEndpointsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxEndpointsNode() throws UaException {
    try {
      return getMaxEndpointsNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxEndpointsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "MaxEndpoints", ExpandedNodeId.parse("i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getMaxCertificateGroups() throws UaException {
    PropertyTypeNode node = getMaxCertificateGroupsNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxCertificateGroups(UShort value) throws UaException {
    PropertyTypeNode node = getMaxCertificateGroupsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readMaxCertificateGroups() throws UaException {
    try {
      return readMaxCertificateGroupsAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeMaxCertificateGroups(UShort value) throws UaException {
    try {
      writeMaxCertificateGroupsAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readMaxCertificateGroupsAsync() {
    return getMaxCertificateGroupsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxCertificateGroupsAsync(UShort maxCertificateGroups) {
    DataValue value = DataValue.valueOnly(new Variant(maxCertificateGroups));
    return getMaxCertificateGroupsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxCertificateGroupsNode() throws UaException {
    try {
      return getMaxCertificateGroupsNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxCertificateGroupsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxCertificateGroups",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String[] getSecurityPolicyUris() throws UaException {
    PropertyTypeNode node = getSecurityPolicyUrisNode();
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setSecurityPolicyUris(String[] value) throws UaException {
    PropertyTypeNode node = getSecurityPolicyUrisNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String[] readSecurityPolicyUris() throws UaException {
    try {
      return readSecurityPolicyUrisAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeSecurityPolicyUris(String[] value) throws UaException {
    try {
      writeSecurityPolicyUrisAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends String[]> readSecurityPolicyUrisAsync() {
    return getSecurityPolicyUrisNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityPolicyUrisAsync(String[] securityPolicyUris) {
    DataValue value = DataValue.valueOnly(new Variant(securityPolicyUris));
    return getSecurityPolicyUrisNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUrisNode() throws UaException {
    try {
      return getSecurityPolicyUrisNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityPolicyUrisNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SecurityPolicyUris",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UserTokenPolicy[] getUserTokenTypes() throws UaException {
    PropertyTypeNode node = getUserTokenTypesNode();
    return cast(node.getValue().getValue().getValue(), UserTokenPolicy[].class);
  }

  @Override
  public void setUserTokenTypes(UserTokenPolicy[] value) throws UaException {
    PropertyTypeNode node = getUserTokenTypesNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public UserTokenPolicy[] readUserTokenTypes() throws UaException {
    try {
      return readUserTokenTypesAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeUserTokenTypes(UserTokenPolicy[] value) throws UaException {
    try {
      writeUserTokenTypesAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends UserTokenPolicy[]> readUserTokenTypesAsync() {
    return getUserTokenTypesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), UserTokenPolicy[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeUserTokenTypesAsync(UserTokenPolicy[] userTokenTypes) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), userTokenTypes);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getUserTokenTypesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getUserTokenTypesNode() throws UaException {
    try {
      return getUserTokenTypesNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUserTokenTypesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "UserTokenTypes", ExpandedNodeId.parse("i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId[] getCertificateTypes() throws UaException {
    PropertyTypeNode node = getCertificateTypesNode();
    return (NodeId[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setCertificateTypes(NodeId[] value) throws UaException {
    PropertyTypeNode node = getCertificateTypesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId[] readCertificateTypes() throws UaException {
    try {
      return readCertificateTypesAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeCertificateTypes(NodeId[] value) throws UaException {
    try {
      writeCertificateTypesAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends NodeId[]> readCertificateTypesAsync() {
    return getCertificateTypesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCertificateTypesAsync(NodeId[] certificateTypes) {
    DataValue value = DataValue.valueOnly(new Variant(certificateTypes));
    return getCertificateTypesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getCertificateTypesNode() throws UaException {
    try {
      return getCertificateTypesNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCertificateTypesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CertificateTypes",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId[] getCertificateGroupPurposes() throws UaException {
    PropertyTypeNode node = getCertificateGroupPurposesNode();
    return (NodeId[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setCertificateGroupPurposes(NodeId[] value) throws UaException {
    PropertyTypeNode node = getCertificateGroupPurposesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId[] readCertificateGroupPurposes() throws UaException {
    try {
      return readCertificateGroupPurposesAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeCertificateGroupPurposes(NodeId[] value) throws UaException {
    try {
      writeCertificateGroupPurposesAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends NodeId[]> readCertificateGroupPurposesAsync() {
    return getCertificateGroupPurposesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCertificateGroupPurposesAsync(
      NodeId[] certificateGroupPurposes) {
    DataValue value = DataValue.valueOnly(new Variant(certificateGroupPurposes));
    return getCertificateGroupPurposesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getCertificateGroupPurposesNode() throws UaException {
    try {
      return getCertificateGroupPurposesNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCertificateGroupPurposesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CertificateGroupPurposes",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
