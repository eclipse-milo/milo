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

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class BrokerDataSetWriterTransportTypeNode extends DataSetWriterTransportTypeNode
    implements BrokerDataSetWriterTransportType {
  public BrokerDataSetWriterTransportTypeNode(
      UaNodeContext context,
      NodeId nodeId,
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
        context,
        nodeId,
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

  public BrokerDataSetWriterTransportTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);
  }

  @Override
  public PropertyTypeNode getQueueNameNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(BrokerDataSetWriterTransportType.QUEUE_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getQueueName() {
    return getProperty(BrokerDataSetWriterTransportType.QUEUE_NAME).orElse(null);
  }

  @Override
  public void setQueueName(String value) {
    setProperty(BrokerDataSetWriterTransportType.QUEUE_NAME, value);
  }

  @Override
  public PropertyTypeNode getMetaDataQueueNameNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(BrokerDataSetWriterTransportType.META_DATA_QUEUE_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getMetaDataQueueName() {
    return getProperty(BrokerDataSetWriterTransportType.META_DATA_QUEUE_NAME).orElse(null);
  }

  @Override
  public void setMetaDataQueueName(String value) {
    setProperty(BrokerDataSetWriterTransportType.META_DATA_QUEUE_NAME, value);
  }

  @Override
  public PropertyTypeNode getResourceUriNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(BrokerDataSetWriterTransportType.RESOURCE_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getResourceUri() {
    return getProperty(BrokerDataSetWriterTransportType.RESOURCE_URI).orElse(null);
  }

  @Override
  public void setResourceUri(String value) {
    setProperty(BrokerDataSetWriterTransportType.RESOURCE_URI, value);
  }

  @Override
  public PropertyTypeNode getAuthenticationProfileUriNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(BrokerDataSetWriterTransportType.AUTHENTICATION_PROFILE_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getAuthenticationProfileUri() {
    return getProperty(BrokerDataSetWriterTransportType.AUTHENTICATION_PROFILE_URI).orElse(null);
  }

  @Override
  public void setAuthenticationProfileUri(String value) {
    setProperty(BrokerDataSetWriterTransportType.AUTHENTICATION_PROFILE_URI, value);
  }

  @Override
  public PropertyTypeNode getRequestedDeliveryGuaranteeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(BrokerDataSetWriterTransportType.REQUESTED_DELIVERY_GUARANTEE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public BrokerTransportQualityOfService getRequestedDeliveryGuarantee() {
    return getProperty(BrokerDataSetWriterTransportType.REQUESTED_DELIVERY_GUARANTEE).orElse(null);
  }

  @Override
  public void setRequestedDeliveryGuarantee(BrokerTransportQualityOfService value) {
    setProperty(BrokerDataSetWriterTransportType.REQUESTED_DELIVERY_GUARANTEE, value);
  }

  @Override
  public PropertyTypeNode getMetaDataUpdateTimeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(BrokerDataSetWriterTransportType.META_DATA_UPDATE_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getMetaDataUpdateTime() {
    return getProperty(BrokerDataSetWriterTransportType.META_DATA_UPDATE_TIME).orElse(null);
  }

  @Override
  public void setMetaDataUpdateTime(Double value) {
    setProperty(BrokerDataSetWriterTransportType.META_DATA_UPDATE_TIME, value);
  }
}
