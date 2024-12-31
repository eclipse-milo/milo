/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.nodes;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public interface Node {

  /**
   * Get the NodeId attribute.
   *
   * <p>The NodeId is an unambiguous identifier that identifies a Node in a server.
   *
   * <p>See OPC UA Part 3, section 5.2.2.
   *
   * @return the NodeId ({@link NodeId}) of this node.
   */
  NodeId getNodeId();

  /**
   * Get the NodeClass attribute.
   *
   * <p>The NodeClass attribute identifies the {@link NodeClass} of a Node.
   *
   * <p>See OPC UA Part 3, section 5.2.3.
   *
   * @return the NodeClass ({@link NodeClass}) of this node.
   */
  NodeClass getNodeClass();

  /**
   * Get the BrowseName attribute.
   *
   * <p>The BrowseName attribute is a non-localized human-readable name for the Node that is used
   * when browsing the address space. BrowseNames can be combined to form browse paths for use with
   * the TranslateBrowsePathToNodeId service.
   *
   * <p>See OPC UA Part 3, section 5.2.4.
   *
   * @return the BrowseName ({@link QualifiedName}) of this node.
   */
  QualifiedName getBrowseName();

  /**
   * See OPC UA Part 3, section 5.2.5.
   *
   * @return the DisplayName ({@link QualifiedName}) of this node.
   */
  LocalizedText getDisplayName();

  /**
   * Get the Description attribute.
   *
   * <p>The optional Description Attribute explains the meaning of the Node in a {@link
   * LocalizedText}.
   *
   * <p>See OPC UA Part 3, section 5.2.6.
   *
   * @return if this attribute is present, the Description ({@link LocalizedText}).
   */
  LocalizedText getDescription();

  /**
   * Get the WriteMask attribute.
   *
   * <p>The optional WriteMask Attribute exposes the possibilities of a client to write the
   * Attributes of the Node. The WriteMask Attribute does not take any user access rights into
   * account.
   *
   * <p>See OPC UA Part 3, section 5.2.7.
   *
   * @return if this attribute is present, the WriteMask ({@link UInteger}).
   */
  UInteger getWriteMask();

  /**
   * Get the UserWriteMask attribute.
   *
   * <p>The optional UserWriteMask Attribute exposes the possibilities of a client to write the
   * Attributes of the Node taking user access rights into account.
   *
   * <p>See OPC UA Part 3, section 5.2.8.
   *
   * @return if this attribute is present, the UserWriteMask ({@link UInteger}).
   */
  UInteger getUserWriteMask();

  /**
   * Get the RolePermissions attribute.
   *
   * <p>The optional RolePermissions Attribute specifies the Permissions that apply to a Node for
   * all Roles which have access to the Node.
   *
   * <p>See OPC UA Part 3, section 5.2.9.
   *
   * @return if this attribute is present, the RolePermissions ({@link RolePermissionType}).
   */
  RolePermissionType[] getRolePermissions();

  /**
   * Get the UserRolePermissions attribute.
   *
   * <p>The optional UserRolePermissions Attribute specifies the Permissions that apply to a Node
   * for all Roles granted to current Session.
   *
   * <p>See OPC UA Part 3, section 5.2.10.
   *
   * @return if this attribute is present, the UserRolePermissions ({@link RolePermissionType}).
   */
  RolePermissionType[] getUserRolePermissions();

  /**
   * Get the AccessRestrictions attribute.
   *
   * <p>The optional AccessRestrictions Attribute specifies the AccessRestrictions that apply to a
   * Node.
   *
   * <p>See OPC UA Part 3, section 5.2.11.
   *
   * @return if this attribute is present, the AccessRestrictions ({@link AccessRestrictionType}).
   */
  AccessRestrictionType getAccessRestrictions();

  /**
   * Set the NodeId attribute of this Node.
   *
   * @param nodeId the NodeId to set.
   * @see #getNodeId()
   */
  void setNodeId(NodeId nodeId);

  /**
   * Set the NodeClass attribute of this Node.
   *
   * @param nodeClass the NodeClass to set.
   * @see #getNodeClass()
   */
  void setNodeClass(NodeClass nodeClass);

  /**
   * Set the BrowseName attribute of this Node.
   *
   * @param browseName the BrowseName to set.
   * @see #getBrowseName()
   */
  void setBrowseName(QualifiedName browseName);

  /**
   * Set the DisplayName attribute of this Node.
   *
   * @param displayName the DisplayName to set.
   * @see #getDisplayName()
   */
  void setDisplayName(LocalizedText displayName);

  /**
   * Set the Description attribute of this Node.
   *
   * @param description the Description to set.
   * @see #getDescription()
   */
  void setDescription(LocalizedText description);

  /**
   * Set the WriteMask attribute of this Node.
   *
   * @param writeMask the WriteMask to set.
   * @see #getWriteMask()
   */
  void setWriteMask(UInteger writeMask);

  /**
   * Set the UserWriteMask attribute of this Node.
   *
   * @param userWriteMask the UserWriteMask to set.
   * @see #getUserWriteMask()
   */
  void setUserWriteMask(UInteger userWriteMask);

  /**
   * Set the RolePermissions attribute of this Node.
   *
   * @param rolePermissions the RolePermissions to set.
   * @see #getRolePermissions()
   */
  void setRolePermissions(RolePermissionType[] rolePermissions);

  /**
   * Set the UserRolePermissions attribute of this Node.
   *
   * @param rolePermissions the UserRolePermissions to set.
   * @see #getUserRolePermissions()
   */
  void setUserRolePermissions(RolePermissionType[] rolePermissions);

  /**
   * Set the AccessRestrictions attribute if this Node.
   *
   * @param accessRestrictions the AccessRestrictions to set.
   * @see #getAccessRestrictions()
   */
  void setAccessRestrictions(AccessRestrictionType accessRestrictions);
}
