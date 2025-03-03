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
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class FileTypeNode extends BaseObjectTypeNode implements FileType {
  public FileTypeNode(
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

  public FileTypeNode(
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
  public PropertyTypeNode getSizeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(FileType.SIZE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ULong getSize() {
    return getProperty(FileType.SIZE).orElse(null);
  }

  @Override
  public void setSize(ULong value) {
    setProperty(FileType.SIZE, value);
  }

  @Override
  public PropertyTypeNode getWritableNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(FileType.WRITABLE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getWritable() {
    return getProperty(FileType.WRITABLE).orElse(null);
  }

  @Override
  public void setWritable(Boolean value) {
    setProperty(FileType.WRITABLE, value);
  }

  @Override
  public PropertyTypeNode getUserWritableNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(FileType.USER_WRITABLE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getUserWritable() {
    return getProperty(FileType.USER_WRITABLE).orElse(null);
  }

  @Override
  public void setUserWritable(Boolean value) {
    setProperty(FileType.USER_WRITABLE, value);
  }

  @Override
  public PropertyTypeNode getOpenCountNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(FileType.OPEN_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getOpenCount() {
    return getProperty(FileType.OPEN_COUNT).orElse(null);
  }

  @Override
  public void setOpenCount(UShort value) {
    setProperty(FileType.OPEN_COUNT, value);
  }

  @Override
  public PropertyTypeNode getMimeTypeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(FileType.MIME_TYPE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getMimeType() {
    return getProperty(FileType.MIME_TYPE).orElse(null);
  }

  @Override
  public void setMimeType(String value) {
    setProperty(FileType.MIME_TYPE, value);
  }

  @Override
  public PropertyTypeNode getMaxByteStringLengthNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(FileType.MAX_BYTE_STRING_LENGTH);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxByteStringLength() {
    return getProperty(FileType.MAX_BYTE_STRING_LENGTH).orElse(null);
  }

  @Override
  public void setMaxByteStringLength(UInteger value) {
    setProperty(FileType.MAX_BYTE_STRING_LENGTH, value);
  }

  @Override
  public PropertyTypeNode getLastModifiedTimeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(FileType.LAST_MODIFIED_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getLastModifiedTime() {
    return getProperty(FileType.LAST_MODIFIED_TIME).orElse(null);
  }

  @Override
  public void setLastModifiedTime(DateTime value) {
    setProperty(FileType.LAST_MODIFIED_TIME, value);
  }

  @Override
  public UaMethodNode getOpenMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Open",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getCloseMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Close",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getReadMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Read",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getWriteMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Write",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getGetPositionMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "GetPosition",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getSetPositionMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "SetPosition",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
