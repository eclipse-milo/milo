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
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.AttributeSnapshot;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class VariableTypeManager {

  private final ConcurrentMap<NodeId, VariableTypeDefinition> typeDefinitions =
      new ConcurrentHashMap<>();

  public void registerVariableType(
      NodeId typeDefinition,
      Class<? extends UaVariableNode> nodeClass,
      VariableNodeConstructor variableNodeConstructor) {

    typeDefinitions.put(
        typeDefinition, new VariableTypeDefinition(nodeClass, variableNodeConstructor, null));
  }

  /**
   * Register {@code nodeClass} and a snapshot-consuming constructor for {@code typeDefinition}.
   *
   * <p>Unlike the tuple-signature forms, a {@link SnapshotConstructor} receives the full planned
   * {@link AttributeSnapshot}, so attributes added to the model in future versions propagate
   * without constructor signature changes. Registrations made through this overload are used by the
   * node-instantiation engine; the legacy {@code NodeFactory} lookup path ({@link
   * #getNodeConstructor(NodeId)}) does not see them and falls back to its default construction.
   *
   * @param typeDefinition the {@link NodeId} of the VariableType.
   * @param nodeClass the Java class instances of the type are constructed as.
   * @param snapshotConstructor the snapshot-consuming constructor.
   */
  public void registerVariableType(
      NodeId typeDefinition,
      Class<? extends UaVariableNode> nodeClass,
      SnapshotConstructor snapshotConstructor) {

    typeDefinitions.put(
        typeDefinition, new VariableTypeDefinition(nodeClass, null, snapshotConstructor));
  }

  public void registerVariableType(
      NodeId typeDefinition,
      Class<? extends UaVariableNode> nodeClass,
      LegacyVariableNodeConstructor variableNodeConstructor) {

    VariableNodeConstructor adapted =
        new VariableNodeConstructor() {
          @Override
          public UaVariableNode apply(
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
              DataValue value,
              NodeId dataType,
              Integer valueRank,
              UInteger[] arrayDimensions) {

            return variableNodeConstructor.apply(
                context, nodeId, browseName, displayName, description, writeMask, userWriteMask);
          }
        };

    typeDefinitions.put(typeDefinition, new VariableTypeDefinition(nodeClass, adapted, null));
  }

  public Optional<VariableNodeConstructor> getNodeConstructor(NodeId typeDefinition) {
    VariableTypeDefinition def = typeDefinitions.get(typeDefinition);

    return Optional.ofNullable(def).map(d -> d.nodeConstructor);
  }

  /**
   * Get the full registration for {@code typeDefinition}, if one exists: the registered Java class
   * plus whichever constructor form the registration supplied.
   *
   * <p>This is the node-instantiation engine's lookup: exposing the {@link Class} enables plan-time
   * expected-class checks and nearest-registered-ancestor fallback along the {@code HasSubtype}
   * chain. Exactly one of {@link RegisteredVariableType#nodeConstructor()} and {@link
   * RegisteredVariableType#snapshotConstructor()} is non-null.
   *
   * @param typeDefinition the {@link NodeId} of the VariableType.
   * @return the registration, if one exists.
   */
  public Optional<RegisteredVariableType> getRegisteredType(NodeId typeDefinition) {
    VariableTypeDefinition def = typeDefinitions.get(typeDefinition);

    return Optional.ofNullable(def)
        .map(
            d -> new RegisteredVariableType(d.nodeClass, d.nodeConstructor, d.snapshotConstructor));
  }

  private static class VariableTypeDefinition {
    final Class<? extends UaVariableNode> nodeClass;
    final @Nullable VariableNodeConstructor nodeConstructor;
    final @Nullable SnapshotConstructor snapshotConstructor;

    private VariableTypeDefinition(
        Class<? extends UaVariableNode> nodeClass,
        @Nullable VariableNodeConstructor nodeConstructor,
        @Nullable SnapshotConstructor snapshotConstructor) {

      this.nodeClass = nodeClass;
      this.nodeConstructor = nodeConstructor;
      this.snapshotConstructor = snapshotConstructor;
    }
  }

  /**
   * A VariableType registration: the Java class instances are constructed as, plus whichever
   * constructor form the registration supplied (exactly one is non-null).
   *
   * @param nodeClass the registered Java class.
   * @param nodeConstructor the tuple-signature constructor, if registered with one.
   * @param snapshotConstructor the snapshot-consuming constructor, if registered with one.
   */
  public record RegisteredVariableType(
      Class<? extends UaVariableNode> nodeClass,
      @Nullable VariableNodeConstructor nodeConstructor,
      @Nullable SnapshotConstructor snapshotConstructor) {}

  @FunctionalInterface
  public interface VariableNodeConstructor {

    UaVariableNode apply(
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
        DataValue value,
        NodeId dataType,
        Integer valueRank,
        UInteger[] arrayDimensions);
  }

  @FunctionalInterface
  public interface LegacyVariableNodeConstructor {

    UaVariableNode apply(
        UaNodeContext context,
        NodeId nodeId,
        QualifiedName browseName,
        LocalizedText displayName,
        LocalizedText description,
        UInteger writeMask,
        UInteger userWriteMask);
  }

  /**
   * Constructs a {@link UaVariableNode} from the planned {@link AttributeSnapshot} instead of a
   * fixed attribute tuple, so newly modeled attributes propagate without signature changes.
   */
  @FunctionalInterface
  public interface SnapshotConstructor {

    /**
     * Construct an instance node.
     *
     * @param context the {@link UaNodeContext} the node is constructed with.
     * @param nodeId the instance {@link NodeId}.
     * @param attributes the effective attributes planned for the instance, absent/null/value
     *     distinction preserved.
     * @return the constructed node.
     */
    UaVariableNode apply(UaNodeContext context, NodeId nodeId, AttributeSnapshot attributes);
  }
}
