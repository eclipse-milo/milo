/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ObjectTypeManager {

  private final ConcurrentMap<NodeId, ObjectTypeDefinition> typeDefinitions =
      new ConcurrentHashMap<>();

  public void registerObjectType(
      NodeId typeDefinition,
      Class<? extends UaObjectNode> nodeClass,
      ObjectNodeConstructor objectNodeConstructor) {

    typeDefinitions.put(typeDefinition, new ObjectTypeDefinition(nodeClass, objectNodeConstructor));
  }

  public void registerObjectType(
      NodeId typeDefinition,
      Class<? extends UaObjectNode> nodeClass,
      LegacyObjectNodeConstructor objectNodeConstructor) {

    ObjectNodeConstructor adapted =
        new ObjectNodeConstructor() {
          @Override
          public UaObjectNode apply(
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

            return objectNodeConstructor.apply(
                context, nodeId, browseName, displayName, description, writeMask, userWriteMask);
          }
        };

    typeDefinitions.put(typeDefinition, new ObjectTypeDefinition(nodeClass, adapted));
  }

  public Optional<ObjectNodeConstructor> getNodeConstructor(NodeId typeDefinition) {
    ObjectTypeDefinition def = typeDefinitions.get(typeDefinition);

    return Optional.ofNullable(def).map(d -> d.nodeConstructor);
  }

  private static class ObjectTypeDefinition {
    final Class<? extends UaNode> nodeClass;
    final ObjectNodeConstructor nodeConstructor;

    private ObjectTypeDefinition(
        Class<? extends UaNode> nodeClass, ObjectNodeConstructor nodeConstructor) {

      this.nodeClass = nodeClass;
      this.nodeConstructor = nodeConstructor;
    }
  }

  @FunctionalInterface
  public interface ObjectNodeConstructor {

    UaObjectNode apply(
        UaNodeContext context,
        NodeId nodeId,
        QualifiedName browseName,
        LocalizedText displayName,
        LocalizedText description,
        UInteger writeMask,
        UInteger userWriteMask,
        RolePermissionType[] rolePermissions,
        RolePermissionType[] userRolePermissions,
        AccessRestrictionType accessRestrictions);
  }

  @FunctionalInterface
  public interface LegacyObjectNodeConstructor {

    UaObjectNode apply(
        UaNodeContext context,
        NodeId nodeId,
        QualifiedName browseName,
        LocalizedText displayName,
        LocalizedText description,
        UInteger writeMask,
        UInteger userWriteMask);
  }
}
