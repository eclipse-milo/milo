/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes;

import org.eclipse.milo.opcua.sdk.core.nodes.ViewNode;
import org.eclipse.milo.opcua.sdk.core.nodes.ViewNodeProperties;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class UaViewNode extends UaNode implements ViewNode {

  private Boolean containsNoLoops;
  private UByte eventNotifier;

  /** Construct a {@link UaViewNode} using only attributes defined prior to OPC UA 1.04. */
  public UaViewNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      Boolean containsNoLoops,
      UByte eventNotifier) {

    super(
        context,
        nodeId,
        NodeClass.View,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask);

    this.containsNoLoops = containsNoLoops;
    this.eventNotifier = eventNotifier;
  }

  /** Construct a {@link UaViewNode} using all available attributes. */
  public UaViewNode(
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
      Boolean containsNoLoops,
      UByte eventNotifier) {

    super(
        context,
        nodeId,
        NodeClass.View,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);

    this.containsNoLoops = containsNoLoops;
    this.eventNotifier = eventNotifier;
  }

  @Override
  public Boolean getContainsNoLoops() {
    return (Boolean) filterChain.getAttribute(this, AttributeId.ContainsNoLoops);
  }

  @Override
  public UByte getEventNotifier() {
    return (UByte) filterChain.getAttribute(this, AttributeId.EventNotifier);
  }

  @Override
  public void setContainsNoLoops(Boolean containsNoLoops) {
    filterChain.setAttribute(this, AttributeId.ContainsNoLoops, containsNoLoops);
  }

  @Override
  public void setEventNotifier(UByte eventNotifier) {
    filterChain.setAttribute(this, AttributeId.EventNotifier, eventNotifier);
  }

  @Override
  public synchronized Object getAttribute(AttributeId attributeId) {
    switch (attributeId) {
      case ContainsNoLoops:
        return containsNoLoops;

      case EventNotifier:
        return eventNotifier;

      default:
        return super.getAttribute(attributeId);
    }
  }

  @Override
  public synchronized void setAttribute(AttributeId attributeId, Object value) {
    switch (attributeId) {
      case ContainsNoLoops:
        containsNoLoops = (Boolean) value;
        break;

      case EventNotifier:
        eventNotifier = (UByte) value;
        break;

      default:
        super.setAttribute(attributeId, value);
        return; // prevent firing an attribute change
    }

    fireAttributeChanged(attributeId, value);
  }

  /**
   * Get the value of the NodeVersion Property, if it exists.
   *
   * @return the value of the NodeVersion Property, if it exists.
   * @see ViewNodeProperties#NodeVersion
   */
  @Nullable
  public String getNodeVersion() {
    return getProperty(ViewNodeProperties.NodeVersion).orElse(null);
  }

  /**
   * Get the value of the ViewVersion Property, if it exists.
   *
   * @return the value of the ViewVersion Property, if it exists.
   * @see ViewNodeProperties#ViewVersion
   */
  @Nullable
  public UInteger getViewVersion() {
    return getProperty(ViewNodeProperties.ViewVersion).orElse(null);
  }

  /**
   * Set the value of the NodeVersion Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param nodeVersion the value to set.
   * @see ViewNodeProperties#NodeVersion
   */
  public void setNodeVersion(String nodeVersion) {
    setProperty(ViewNodeProperties.NodeVersion, nodeVersion);
  }

  /**
   * Set the value of the ViewVersion Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param viewVersion the value to set.
   * @see ViewNodeProperties#NodeVersion
   */
  public void setViewVersion(UInteger viewVersion) {
    setProperty(ViewNodeProperties.ViewVersion, viewVersion);
  }
}
