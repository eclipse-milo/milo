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

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class JsonDataSetReaderMessageTypeNode extends DataSetReaderMessageTypeNode
    implements JsonDataSetReaderMessageType {
  public JsonDataSetReaderMessageTypeNode(
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

  public JsonDataSetReaderMessageTypeNode(
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
  public PropertyTypeNode getNetworkMessageContentMaskNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(JsonDataSetReaderMessageType.NETWORK_MESSAGE_CONTENT_MASK);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public JsonNetworkMessageContentMask getNetworkMessageContentMask() {
    return getProperty(JsonDataSetReaderMessageType.NETWORK_MESSAGE_CONTENT_MASK).orElse(null);
  }

  @Override
  public void setNetworkMessageContentMask(JsonNetworkMessageContentMask value) {
    setProperty(JsonDataSetReaderMessageType.NETWORK_MESSAGE_CONTENT_MASK, value);
  }

  @Override
  public PropertyTypeNode getDataSetMessageContentMaskNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(JsonDataSetReaderMessageType.DATA_SET_MESSAGE_CONTENT_MASK);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public JsonDataSetMessageContentMask getDataSetMessageContentMask() {
    return getProperty(JsonDataSetReaderMessageType.DATA_SET_MESSAGE_CONTENT_MASK).orElse(null);
  }

  @Override
  public void setDataSetMessageContentMask(JsonDataSetMessageContentMask value) {
    setProperty(JsonDataSetReaderMessageType.DATA_SET_MESSAGE_CONTENT_MASK, value);
  }
}
