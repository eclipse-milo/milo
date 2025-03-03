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

public class BrokerDataSetReaderTransportTypeNode extends DataSetReaderTransportTypeNode
    implements BrokerDataSetReaderTransportType {
  public BrokerDataSetReaderTransportTypeNode(
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

  public BrokerDataSetReaderTransportTypeNode(
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
        getPropertyNode(BrokerDataSetReaderTransportType.QUEUE_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getQueueName() {
    return getProperty(BrokerDataSetReaderTransportType.QUEUE_NAME).orElse(null);
  }

  @Override
  public void setQueueName(String value) {
    setProperty(BrokerDataSetReaderTransportType.QUEUE_NAME, value);
  }

  @Override
  public PropertyTypeNode getResourceUriNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(BrokerDataSetReaderTransportType.RESOURCE_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getResourceUri() {
    return getProperty(BrokerDataSetReaderTransportType.RESOURCE_URI).orElse(null);
  }

  @Override
  public void setResourceUri(String value) {
    setProperty(BrokerDataSetReaderTransportType.RESOURCE_URI, value);
  }

  @Override
  public PropertyTypeNode getAuthenticationProfileUriNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(BrokerDataSetReaderTransportType.AUTHENTICATION_PROFILE_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getAuthenticationProfileUri() {
    return getProperty(BrokerDataSetReaderTransportType.AUTHENTICATION_PROFILE_URI).orElse(null);
  }

  @Override
  public void setAuthenticationProfileUri(String value) {
    setProperty(BrokerDataSetReaderTransportType.AUTHENTICATION_PROFILE_URI, value);
  }

  @Override
  public PropertyTypeNode getRequestedDeliveryGuaranteeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(BrokerDataSetReaderTransportType.REQUESTED_DELIVERY_GUARANTEE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public BrokerTransportQualityOfService getRequestedDeliveryGuarantee() {
    return getProperty(BrokerDataSetReaderTransportType.REQUESTED_DELIVERY_GUARANTEE).orElse(null);
  }

  @Override
  public void setRequestedDeliveryGuarantee(BrokerTransportQualityOfService value) {
    setProperty(BrokerDataSetReaderTransportType.REQUESTED_DELIVERY_GUARANTEE, value);
  }

  @Override
  public PropertyTypeNode getMetaDataQueueNameNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(BrokerDataSetReaderTransportType.META_DATA_QUEUE_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getMetaDataQueueName() {
    return getProperty(BrokerDataSetReaderTransportType.META_DATA_QUEUE_NAME).orElse(null);
  }

  @Override
  public void setMetaDataQueueName(String value) {
    setProperty(BrokerDataSetReaderTransportType.META_DATA_QUEUE_NAME, value);
  }
}
