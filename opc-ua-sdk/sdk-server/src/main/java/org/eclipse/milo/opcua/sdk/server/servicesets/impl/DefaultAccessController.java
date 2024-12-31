/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.jspecify.annotations.Nullable;

public class DefaultAccessController implements AccessController {

  private final OpcUaServer server;

  public DefaultAccessController(OpcUaServer server) {
    this.server = server;
  }

  // region Read

  @Override
  public Map<ReadValueId, AccessResult> checkReadAccess(
      Session session, List<ReadValueId> readValueIds) {
    var context = new DefaultAccessControlContext(server, session);

    return checkReadAccess(context, readValueIds);
  }

  static Map<ReadValueId, AccessResult> checkReadAccess(
      AccessControlContext context, List<ReadValueId> readValueIds) {
    List<PendingResult<ReadValueId>> pending =
        readValueIds.stream().map(PendingResult::new).toList();

    List<NodeId> nodeIds = readValueIds.stream().map(ReadValueId::getNodeId).toList();

    Map<NodeId, AccessControlAttributes> attributes = context.readAccessControlAttributes(nodeIds);

    checkAccessRestrictions(context, pending, attributes, ReadValueId::getNodeId);

    for (PendingResult<ReadValueId> p : pending) {
      if (p.result.isDenied()) {
        continue;
      }

      NodeId nodeId = p.value.getNodeId();
      UInteger attributeId = p.value.getAttributeId();

      if (AttributeId.Value.uid().equals(attributeId)) {
        UByte userAccessLevel = attributes.get(nodeId).userAccessLevel();

        if (userAccessLevel != null) {
          Set<AccessLevel> accessLevels = AccessLevel.fromValue(userAccessLevel);
          if (!accessLevels.contains(AccessLevel.CurrentRead)) {
            p.result = AccessResult.DENIED_USER_ACCESS;
          }
        }
      } else if (AttributeId.RolePermissions.uid().equals(attributeId)) {
        List<NodeId> roleIds = context.getRoleIds().orElse(null);

        RolePermissionType[] userRolePermissions = attributes.get(nodeId).userRolePermissions();

        if (roleIds != null && userRolePermissions != null) {
          boolean hasAccess =
              Stream.of(userRolePermissions)
                  .anyMatch(rp -> rp.getPermissions().getReadRolePermissions());

          if (!hasAccess) {
            p.result = AccessResult.DENIED_USER_ACCESS;
          }
        }
      }
    }

    return pending.stream().collect(Collectors.toMap(p -> p.value, p -> p.result, (a, b) -> b));
  }

  // endregion

  // region Write

  @Override
  public Map<WriteValue, AccessResult> checkWriteAccess(
      Session session, List<WriteValue> writeValues) {
    var context = new DefaultAccessControlContext(server, session);

    return checkWriteAccess(context, writeValues);
  }

  static Map<WriteValue, AccessResult> checkWriteAccess(
      AccessControlContext context, List<WriteValue> writeValues) {
    List<PendingResult<WriteValue>> pending = writeValues.stream().map(PendingResult::new).toList();

    List<NodeId> nodeIds = writeValues.stream().map(WriteValue::getNodeId).toList();

    Map<NodeId, AccessControlAttributes> attributes = context.readAccessControlAttributes(nodeIds);

    checkAccessRestrictions(context, pending, attributes, WriteValue::getNodeId);

    for (PendingResult<WriteValue> p : pending) {
      if (p.result.isDenied()) {
        continue;
      }

      NodeId nodeId = p.value.getNodeId();
      UInteger attributeId = p.value.getAttributeId();

      if (AttributeId.Value.uid().equals(attributeId)) {
        UByte userAccessLevel = attributes.get(nodeId).userAccessLevel();

        if (userAccessLevel != null) {
          Set<AccessLevel> accessLevels = AccessLevel.fromValue(userAccessLevel);
          if (!accessLevels.contains(AccessLevel.CurrentWrite)) {
            p.result = AccessResult.DENIED_USER_ACCESS;
          }
        }
      } else if (AttributeId.RolePermissions.uid().equals(attributeId)) {
        List<NodeId> roleIds = context.getRoleIds().orElse(null);

        RolePermissionType[] userRolePermissions = attributes.get(nodeId).userRolePermissions();

        if (roleIds != null && userRolePermissions != null) {
          boolean hasAccess =
              Stream.of(userRolePermissions)
                  .anyMatch(rp -> rp.getPermissions().getWriteRolePermissions());

          if (!hasAccess) {
            p.result = AccessResult.DENIED_USER_ACCESS;
          }
        }
      } else if (AttributeId.Historizing.uid().equals(attributeId)) {
        List<NodeId> roleIds = context.getRoleIds().orElse(null);

        RolePermissionType[] userRolePermissions = attributes.get(nodeId).userRolePermissions();

        if (roleIds != null && userRolePermissions != null) {
          boolean hasAccess =
              Stream.of(userRolePermissions)
                  .anyMatch(rp -> rp.getPermissions().getWriteHistorizing());

          if (!hasAccess) {
            p.result = AccessResult.DENIED_USER_ACCESS;
          }
        }
      }
    }

    return pending.stream().collect(Collectors.toMap(p -> p.value, p -> p.result, (a, b) -> b));
  }

  // endregion

  // region Browse

  @Override
  public Map<NodeId, AccessResult> checkBrowseAccess(Session session, List<NodeId> nodeIds) {
    var context = new DefaultAccessControlContext(server, session);

    return checkBrowseAccess(context, nodeIds);
  }

  static Map<NodeId, AccessResult> checkBrowseAccess(
      AccessControlContext context, List<NodeId> nodeIds) {
    List<PendingResult<NodeId>> pending = nodeIds.stream().map(PendingResult::new).toList();

    Map<NodeId, AccessControlAttributes> attributes = context.readAccessControlAttributes(nodeIds);

    checkBrowseAccessRestrictions(context, pending, attributes, Function.identity());

    for (PendingResult<NodeId> p : pending) {
      if (p.result.isDenied()) {
        continue;
      }

      NodeId nodeId = p.value;
      List<NodeId> roleIds = context.getRoleIds().orElse(null);
      RolePermissionType[] userRolePermissions = attributes.get(nodeId).userRolePermissions();

      if (roleIds != null && userRolePermissions != null) {
        boolean hasAccess =
            Stream.of(userRolePermissions).anyMatch(rp -> rp.getPermissions().getBrowse());

        if (!hasAccess) {
          p.result = AccessResult.DENIED_USER_ACCESS;
        }
      }
    }

    return pending.stream().collect(Collectors.toMap(p -> p.value, p -> p.result, (a, b) -> b));
  }

  // endregion

  // region Call

  @Override
  public Map<CallMethodRequest, AccessResult> checkCallAccess(
      Session session, List<CallMethodRequest> requests) {
    var context = new DefaultAccessControlContext(server, session);

    return checkCallAccess(context, requests);
  }

  static Map<CallMethodRequest, AccessResult> checkCallAccess(
      AccessControlContext context, List<CallMethodRequest> requests) {
    List<PendingResult<NodeId>> pending =
        requests.stream()
            .flatMap(
                r ->
                    Stream.of(
                        new PendingResult<>(r.getObjectId()), new PendingResult<>(r.getMethodId())))
            .toList();

    var nodeIds = new ArrayList<NodeId>();

    for (CallMethodRequest request : requests) {
      nodeIds.add(request.getObjectId());
      nodeIds.add(request.getMethodId());
    }

    Map<NodeId, AccessControlAttributes> attributes = context.readAccessControlAttributes(nodeIds);

    checkAccessRestrictions(context, pending, attributes, Function.identity());

    for (int i = 0; i < pending.size(); i += 2) {
      PendingResult<NodeId> p0 = pending.get(i);
      PendingResult<NodeId> p1 = pending.get(i + 1);

      if (p0.result.isDenied() || p1.result.isDenied()) {
        continue;
      }

      AccessControlAttributes objectAttributes = attributes.get(p0.value);
      AccessControlAttributes methodAttributes = attributes.get(p1.value);

      RolePermissionType[] objectPermissions = objectAttributes.userRolePermissions();
      RolePermissionType[] methodPermissions = methodAttributes.userRolePermissions();

      List<NodeId> roleIds = context.getRoleIds().orElse(null);

      if (roleIds != null && objectPermissions != null && methodPermissions != null) {
        boolean objectPermission =
            Stream.of(objectPermissions).anyMatch(rp -> rp.getPermissions().getCall());

        boolean methodPermission =
            Stream.of(methodPermissions).anyMatch(rp -> rp.getPermissions().getCall());

        if (!objectPermission || !methodPermission) {
          p0.result = AccessResult.DENIED_USER_ACCESS;
          p1.result = AccessResult.DENIED_USER_ACCESS;
        }
      }

      Boolean userExecutable = methodAttributes.userExecutable();
      if (userExecutable != null && !userExecutable) {
        p1.result = AccessResult.DENIED_USER_ACCESS;
      }
    }

    var results = new HashMap<CallMethodRequest, AccessResult>();

    for (int i = 0; i < pending.size(); i += 2) {
      CallMethodRequest request = requests.get(i / 2);
      PendingResult<NodeId> p0 = pending.get(i);
      PendingResult<NodeId> p1 = pending.get(i + 1);

      boolean allowed = p0.result.isAllowed() && p1.result.isAllowed();

      results.put(request, allowed ? AccessResult.ALLOWED : AccessResult.DENIED_USER_ACCESS);
    }

    return results;
  }

  // endregion

  // region AddReferences

  @Override
  public Map<AddReferencesItem, AccessResult> checkAddReferencesAccess(
      Session session, List<AddReferencesItem> referencesToAdd) {
    var context = new DefaultAccessControlContext(server, session);

    return checkAddReferencesAccess(context, referencesToAdd);
  }

  static Map<AddReferencesItem, AccessResult> checkAddReferencesAccess(
      AccessControlContext context, List<AddReferencesItem> referencesToAdd) {

    List<PendingResult<AddReferencesItem>> pending =
        referencesToAdd.stream().map(PendingResult::new).toList();

    List<NodeId> nodeIds =
        referencesToAdd.stream().map(AddReferencesItem::getSourceNodeId).toList();

    Map<NodeId, AccessControlAttributes> attributes = context.readAccessControlAttributes(nodeIds);

    checkAccessRestrictions(context, pending, attributes, AddReferencesItem::getSourceNodeId);

    for (PendingResult<AddReferencesItem> p : pending) {
      if (p.result.isDenied()) {
        continue;
      }

      NodeId nodeId = p.value.getSourceNodeId();
      List<NodeId> roleIds = context.getRoleIds().orElse(null);
      RolePermissionType[] userRolePermissions = attributes.get(nodeId).userRolePermissions();

      if (roleIds != null && userRolePermissions != null) {
        boolean hasAccess =
            Stream.of(userRolePermissions).anyMatch(rp -> rp.getPermissions().getAddReference());

        if (!hasAccess) {
          p.result = AccessResult.DENIED_USER_ACCESS;
        }
      }
    }

    return pending.stream().collect(Collectors.toMap(p -> p.value, p -> p.result, (a, b) -> b));
  }

  // endregion

  // region DeleteNodes

  @Override
  public Map<DeleteNodesItem, AccessResult> checkDeleteNodesAccess(
      Session session, List<DeleteNodesItem> nodesToDelete) {
    var context = new DefaultAccessControlContext(server, session);

    return checkDeleteNodesAccess(context, nodesToDelete);
  }

  static Map<DeleteNodesItem, AccessResult> checkDeleteNodesAccess(
      AccessControlContext context, List<DeleteNodesItem> nodesToDelete) {
    List<PendingResult<DeleteNodesItem>> pending =
        nodesToDelete.stream().map(PendingResult::new).toList();

    List<NodeId> nodeIds = nodesToDelete.stream().map(DeleteNodesItem::getNodeId).toList();

    Map<NodeId, AccessControlAttributes> attributes = context.readAccessControlAttributes(nodeIds);

    checkAccessRestrictions(context, pending, attributes, DeleteNodesItem::getNodeId);

    for (PendingResult<DeleteNodesItem> p : pending) {
      if (p.result.isDenied()) {
        continue;
      }

      NodeId nodeId = p.value.getNodeId();
      List<NodeId> roleIds = context.getRoleIds().orElse(null);
      RolePermissionType[] userRolePermissions = attributes.get(nodeId).userRolePermissions();

      if (roleIds != null && userRolePermissions != null) {
        boolean hasAccess =
            Stream.of(userRolePermissions).anyMatch(rp -> rp.getPermissions().getDeleteNode());

        if (!hasAccess) {
          p.result = AccessResult.DENIED_USER_ACCESS;
        }
      }
    }

    return pending.stream().collect(Collectors.toMap(p -> p.value, p -> p.result, (a, b) -> b));
  }

  // endregion DeleteNodes

  // region DeleteReferences

  @Override
  public Map<DeleteReferencesItem, AccessResult> checkDeleteReferencesAccess(
      Session session, List<DeleteReferencesItem> referencesToDelete) {

    var context = new DefaultAccessControlContext(server, session);

    return checkDeleteReferencesAccess(context, referencesToDelete);
  }

  static Map<DeleteReferencesItem, AccessResult> checkDeleteReferencesAccess(
      AccessControlContext context, List<DeleteReferencesItem> referencesToDelete) {

    List<PendingResult<DeleteReferencesItem>> pending =
        referencesToDelete.stream().map(PendingResult::new).toList();

    List<NodeId> nodeIds =
        referencesToDelete.stream().map(DeleteReferencesItem::getSourceNodeId).toList();

    Map<NodeId, AccessControlAttributes> attributes = context.readAccessControlAttributes(nodeIds);

    checkAccessRestrictions(context, pending, attributes, DeleteReferencesItem::getSourceNodeId);

    for (PendingResult<DeleteReferencesItem> p : pending) {
      if (p.result.isDenied()) {
        continue;
      }

      NodeId nodeId = p.value.getSourceNodeId();
      List<NodeId> roleIds = context.getRoleIds().orElse(null);
      RolePermissionType[] userRolePermissions = attributes.get(nodeId).userRolePermissions();

      if (roleIds != null && userRolePermissions != null) {
        boolean hasAccess =
            Stream.of(userRolePermissions).anyMatch(rp -> rp.getPermissions().getRemoveReference());

        if (!hasAccess) {
          p.result = AccessResult.DENIED_USER_ACCESS;
        }
      }
    }

    return pending.stream().collect(Collectors.toMap(p -> p.value, p -> p.result, (a, b) -> b));
  }

  // endregion

  private static <T> void checkAccessRestrictions(
      AccessControlContext context,
      List<PendingResult<T>> pending,
      Map<NodeId, AccessControlAttributes> attributes,
      Function<T, NodeId> getNodeId) {

    checkAccessRestrictions(context, pending, attributes, getNodeId, false);
  }

  private static <T> void checkBrowseAccessRestrictions(
      AccessControlContext context,
      List<PendingResult<T>> pending,
      Map<NodeId, AccessControlAttributes> attributes,
      Function<T, NodeId> getNodeId) {

    checkAccessRestrictions(context, pending, attributes, getNodeId, true);
  }

  private static <T> void checkAccessRestrictions(
      AccessControlContext context,
      List<PendingResult<T>> pending,
      Map<NodeId, AccessControlAttributes> attributes,
      Function<T, NodeId> getNodeId,
      boolean browsing) {

    MessageSecurityMode securityMode = context.getSecurityMode();

    for (PendingResult<T> p : pending) {
      if (p.result.isDenied()) {
        continue;
      }

      NodeId nodeId = getNodeId.apply(p.value);
      AccessRestrictionType accessRestrictions = attributes.get(nodeId).accessRestrictions();

      if (accessRestrictions != null) {
        if (browsing && !accessRestrictions.getApplyRestrictionsToBrowse()) {
          continue;
        }

        if (accessRestrictions.getEncryptionRequired()) {
          if (securityMode != MessageSecurityMode.SignAndEncrypt) {
            p.result = AccessResult.DENIED_SECURITY_MODE;
          }
        } else if (accessRestrictions.getSigningRequired()) {
          if (securityMode != MessageSecurityMode.Sign
              && securityMode != MessageSecurityMode.SignAndEncrypt) {

            p.result = AccessResult.DENIED_SECURITY_MODE;
          }
        }
      }
    }
  }

  private static class PendingResult<T> {
    private AccessResult result = AccessResult.ALLOWED;
    private final T value;

    private PendingResult(T value) {
      this.value = value;
    }
  }

  interface AccessControlContext {

    Optional<List<NodeId>> getRoleIds();

    MessageSecurityMode getSecurityMode();

    Map<NodeId, AccessControlAttributes> readAccessControlAttributes(List<NodeId> nodeIds);
  }

  record AccessControlAttributes(
      @Nullable NodeClass nodeClass,
      @Nullable AccessRestrictionType accessRestrictions,
      @Nullable UByte userAccessLevel,
      @Nullable Boolean userExecutable,
      RolePermissionType @Nullable [] userRolePermissions) {}

  static class DefaultAccessControlContext implements AccessControlContext {

    private final OpcUaServer server;
    private final Session session;

    public DefaultAccessControlContext(OpcUaServer server, Session session) {
      this.server = server;
      this.session = session;
    }

    @Override
    public Optional<List<NodeId>> getRoleIds() {
      return session.getRoleIds();
    }

    @Override
    public MessageSecurityMode getSecurityMode() {
      return session.getEndpoint().getSecurityMode();
    }

    @Override
    public Map<NodeId, AccessControlAttributes> readAccessControlAttributes(List<NodeId> nodeIds) {
      List<ReadValueId> readValueIds =
          nodeIds.stream()
              .distinct()
              .flatMap(
                  id -> {
                    List<ReadValueId> attributes =
                        List.of(
                            new ReadValueId(id, AttributeId.NodeClass.uid(), null, null),
                            new ReadValueId(id, AttributeId.AccessRestrictions.uid(), null, null),
                            new ReadValueId(id, AttributeId.UserAccessLevel.uid(), null, null),
                            new ReadValueId(id, AttributeId.UserExecutable.uid(), null, null),
                            new ReadValueId(id, AttributeId.UserRolePermissions.uid(), null, null));

                    return attributes.stream();
                  })
              .toList();

      List<DataValue> values =
          server
              .getAddressSpaceManager()
              .read(
                  new AddressSpace.ReadContext(server, session),
                  0.0,
                  TimestampsToReturn.Neither,
                  readValueIds);

      var attributesMap = new HashMap<NodeId, AccessControlAttributes>();

      for (int i = 0; i < readValueIds.size(); i += 5) {
        NodeId nodeId = readValueIds.get(i).getNodeId();

        Object v0 = values.get(i).getValue().getValue();
        Object v1 = values.get(i + 1).getValue().getValue();
        Object v2 = values.get(i + 2).getValue().getValue();
        Object v3 = values.get(i + 3).getValue().getValue();
        Object v4 = values.get(i + 4).getValue().getValue();

        NodeClass nodeClass = null;
        AccessRestrictionType accessRestrictions = null;
        UByte userAccessLevel = null;
        Boolean userExecutable = null;
        RolePermissionType[] userRolePermissions = null;

        if (v0 instanceof NodeClass nc) {
          nodeClass = nc;
        }
        if (v1 instanceof AccessRestrictionType art) {
          accessRestrictions = art;
        }
        if (v2 instanceof UByte ub) {
          userAccessLevel = ub;
        }
        if (v3 instanceof Boolean b) {
          userExecutable = b;
        }
        if (v4 instanceof RolePermissionType[] rpt) {
          userRolePermissions = rpt;
        }

        var attributes =
            new AccessControlAttributes(
                nodeClass,
                accessRestrictions,
                userAccessLevel,
                userExecutable,
                userRolePermissions);

        attributesMap.put(nodeId, attributes);
      }

      return attributesMap;
    }
  }
}
