/*
 * Copyright (c) 2022 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class DiscrepancyAlarmTypeNode extends AlarmConditionTypeNode implements DiscrepancyAlarmType {
    public DiscrepancyAlarmTypeNode(UaNodeContext context, NodeId nodeId, QualifiedName browseName,
                                    LocalizedText displayName, LocalizedText description, UInteger writeMask,
                                    UInteger userWriteMask, RolePermissionType[] rolePermissions,
                                    RolePermissionType[] userRolePermissions, AccessRestrictionType accessRestrictions,
                                    UByte eventNotifier) {
        super(context, nodeId, browseName, displayName, description, writeMask, userWriteMask, rolePermissions, userRolePermissions, accessRestrictions, eventNotifier);
    }

    public DiscrepancyAlarmTypeNode(UaNodeContext context, NodeId nodeId, QualifiedName browseName,
                                    LocalizedText displayName, LocalizedText description, UInteger writeMask,
                                    UInteger userWriteMask, RolePermissionType[] rolePermissions,
                                    RolePermissionType[] userRolePermissions, AccessRestrictionType accessRestrictions) {
        super(context, nodeId, browseName, displayName, description, writeMask, userWriteMask, rolePermissions, userRolePermissions, accessRestrictions);
    }

    @Override
    public PropertyTypeNode getTargetValueNodeNode() {
        Optional<VariableNode> propertyNode = getPropertyNode(DiscrepancyAlarmType.TARGET_VALUE_NODE);
        return (PropertyTypeNode) propertyNode.orElse(null);
    }

    @Override
    public NodeId getTargetValueNode() {
        return getProperty(DiscrepancyAlarmType.TARGET_VALUE_NODE).orElse(null);
    }

    @Override
    public void setTargetValueNode(NodeId value) {
        setProperty(DiscrepancyAlarmType.TARGET_VALUE_NODE, value);
    }

    @Override
    public PropertyTypeNode getExpectedTimeNode() {
        Optional<VariableNode> propertyNode = getPropertyNode(DiscrepancyAlarmType.EXPECTED_TIME);
        return (PropertyTypeNode) propertyNode.orElse(null);
    }

    @Override
    public Double getExpectedTime() {
        return getProperty(DiscrepancyAlarmType.EXPECTED_TIME).orElse(null);
    }

    @Override
    public void setExpectedTime(Double value) {
        setProperty(DiscrepancyAlarmType.EXPECTED_TIME, value);
    }

    @Override
    public PropertyTypeNode getToleranceNode() {
        Optional<VariableNode> propertyNode = getPropertyNode(DiscrepancyAlarmType.TOLERANCE);
        return (PropertyTypeNode) propertyNode.orElse(null);
    }

    @Override
    public Double getTolerance() {
        return getProperty(DiscrepancyAlarmType.TOLERANCE).orElse(null);
    }

    @Override
    public void setTolerance(Double value) {
        setProperty(DiscrepancyAlarmType.TOLERANCE, value);
    }
}