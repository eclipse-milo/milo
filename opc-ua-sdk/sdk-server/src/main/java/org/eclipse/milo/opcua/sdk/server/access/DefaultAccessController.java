/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.access;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.WriteMask;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
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
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.jspecify.annotations.Nullable;

public class DefaultAccessController implements AccessController {

  /** Every attribute a check can decide from, in {@link AccessControlAttributes} order. */
  private static final List<AttributeId> ACCESS_ATTRIBUTE_ORDER =
      List.of(
          AttributeId.NodeClass,
          AttributeId.AccessRestrictions,
          AttributeId.UserWriteMask,
          AttributeId.AccessLevel,
          AttributeId.UserAccessLevel,
          AttributeId.UserExecutable,
          AttributeId.UserRolePermissions);

  private static final Set<AttributeId> ACCESS_ATTRIBUTES = EnumSet.copyOf(ACCESS_ATTRIBUTE_ORDER);

  private final OpcUaServer server;

  public DefaultAccessController(OpcUaServer server) {
    this.server = server;
  }

  // region Read

  /**
   * {@inheritDoc}
   *
   * <p>A Node the AddressSpace does not know gets {@link AccessResult#NODE_UNKNOWN}, which is no
   * decision rather than a denial: Read proceeds and the AddressSpace answers {@code
   * Bad_NodeIdUnknown} as before, and a caller that re-checks items whose Node may since have been
   * removed leaves their last result in place. An invalid attribute id is still denied first.
   */
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

    Map<NodeId, AccessControlAttributes> attributes =
        context.readAccessControlAttributes(nodeIds, readAccessAttributes(readValueIds));

    for (PendingResult<ReadValueId> p : pending) {
      if (!AttributeId.isValid(p.value.getAttributeId())) {
        p.result = AccessResult.DENIED_ATTRIBUTE_ID_INVALID;
      } else if (attributes.get(p.value.getNodeId()).nodeUnknown()) {
        p.result = AccessResult.NODE_UNKNOWN;
      }
    }

    checkAccessRestrictions(context, pending, attributes, ReadValueId::getNodeId);

    for (PendingResult<ReadValueId> p : pending) {
      if (!p.result.isAllowed()) {
        continue;
      }

      NodeId nodeId = p.value.getNodeId();
      UInteger attributeId = p.value.getAttributeId();

      if (AttributeId.Value.uid().equals(attributeId)) {
        UByte accessLevel = attributes.get(nodeId).accessLevel();
        UByte userAccessLevel = attributes.get(nodeId).userAccessLevel();

        if (accessLevel != null
            && !AccessLevel.fromValue(accessLevel).contains(AccessLevel.CurrentRead)) {
          p.result = AccessResult.DENIED_NOT_READABLE;
        } else if (userAccessLevel != null
            && !AccessLevel.fromValue(userAccessLevel).contains(AccessLevel.CurrentRead)) {
          p.result = AccessResult.DENIED_USER_ACCESS;
        }
      } else if (AttributeId.RolePermissions.uid().equals(attributeId)) {
        List<NodeId> roleIds = context.getRoleIds().orElse(null);

        RolePermissionType[] userRolePermissions = attributes.get(nodeId).userRolePermissions();

        if (roleIds != null && userRolePermissions != null) {
          boolean hasAccess =
              hasPermission(roleIds, userRolePermissions, PermissionType::getReadRolePermissions);

          if (!hasAccess) {
            p.result = AccessResult.DENIED_USER_ACCESS;
          }
        }
      }
    }

    return pending.stream().collect(Collectors.toMap(p -> p.value, p -> p.result, (a, b) -> b));
  }

  /**
   * The attributes a read check of {@code readValueIds} decides from: AccessRestrictions always,
   * AccessLevel and UserAccessLevel for a Value read, and UserRolePermissions for a RolePermissions
   * read. A read check runs for every sampling cycle on the default policy, so it reads nothing
   * more.
   */
  private static Set<AttributeId> readAccessAttributes(List<ReadValueId> readValueIds) {
    Set<AttributeId> attributeIds = EnumSet.of(AttributeId.AccessRestrictions);

    for (ReadValueId readValueId : readValueIds) {
      UInteger attributeId = readValueId.getAttributeId();

      if (AttributeId.Value.uid().equals(attributeId)) {
        attributeIds.add(AttributeId.AccessLevel);
        attributeIds.add(AttributeId.UserAccessLevel);
      } else if (AttributeId.RolePermissions.uid().equals(attributeId)) {
        attributeIds.add(AttributeId.UserRolePermissions);
      }
    }

    return attributeIds;
  }

  // endregion

  // region Write

  /**
   * {@inheritDoc}
   *
   * <p>A Value write is checked the way a Value read is: the Node's {@code AccessLevel} first,
   * which denies with {@code Bad_NotWritable} when it lacks CurrentWrite, and then the Session's
   * {@code UserAccessLevel}, which denies with {@code Bad_UserAccessDenied}. Other attributes are
   * checked against {@code UserWriteMask}.
   */
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

    Map<NodeId, AccessControlAttributes> attributes =
        context.readAccessControlAttributes(nodeIds, ACCESS_ATTRIBUTES);

    for (PendingResult<WriteValue> p : pending) {
      if (!AttributeId.isValid(p.value.getAttributeId())) {
        p.result = AccessResult.DENIED_ATTRIBUTE_ID_INVALID;
      }
    }

    checkAccessRestrictions(context, pending, attributes, WriteValue::getNodeId);

    for (PendingResult<WriteValue> p : pending) {
      if (p.result.isDenied()) {
        continue;
      }

      NodeId nodeId = p.value.getNodeId();
      UInteger attributeId = p.value.getAttributeId();

      if (AttributeId.Value.uid().equals(attributeId)) {
        UByte accessLevel = attributes.get(nodeId).accessLevel();
        UByte userAccessLevel = attributes.get(nodeId).userAccessLevel();

        if (accessLevel != null
            && !AccessLevel.fromValue(accessLevel).contains(AccessLevel.CurrentWrite)) {
          p.result = AccessResult.DENIED_NOT_WRITABLE;
        } else if (userAccessLevel != null
            && !AccessLevel.fromValue(userAccessLevel).contains(AccessLevel.CurrentWrite)) {
          p.result = AccessResult.DENIED_USER_ACCESS;
        }
      } else {
        UInteger userWriteMask = attributes.get(nodeId).userWriteMask();
        if (userWriteMask != null) {
          Set<WriteMask> userWriteMasks = WriteMask.fromMask(userWriteMask);

          // The value of the UserWriteMask attribute implicitly accounts for whether Roles and
          // Permission are configured and if the current Session is assigned a role that includes
          // the WriteAttribute PermissionType bit.
          boolean hasAccess =
              AttributeId.from(attributeId)
                  .map(
                      id ->
                          id != AttributeId.UserRolePermissions
                              && userWriteMasks.contains(WriteMask.forAttribute(id)))
                  .orElse(false);

          if (!hasAccess) {
            p.result = AccessResult.DENIED_USER_ACCESS;
          }
        }

        if (p.result.isDenied()) {
          continue;
        }

        if (AttributeId.RolePermissions.uid().equals(attributeId)) {
          List<NodeId> roleIds = context.getRoleIds().orElse(null);

          RolePermissionType[] userRolePermissions = attributes.get(nodeId).userRolePermissions();

          if (roleIds != null && userRolePermissions != null) {
            boolean hasAccess =
                hasPermission(
                    roleIds, userRolePermissions, PermissionType::getWriteRolePermissions);

            if (!hasAccess) {
              p.result = AccessResult.DENIED_USER_ACCESS;
            }
          }
        } else if (AttributeId.Historizing.uid().equals(attributeId)) {
          List<NodeId> roleIds = context.getRoleIds().orElse(null);

          RolePermissionType[] userRolePermissions = attributes.get(nodeId).userRolePermissions();

          if (roleIds != null && userRolePermissions != null) {
            boolean hasAccess =
                hasPermission(roleIds, userRolePermissions, PermissionType::getWriteHistorizing);

            if (!hasAccess) {
              p.result = AccessResult.DENIED_USER_ACCESS;
            }
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

    Map<NodeId, AccessControlAttributes> attributes =
        context.readAccessControlAttributes(nodeIds, ACCESS_ATTRIBUTES);

    checkBrowseAccessRestrictions(context, pending, attributes, Function.identity());

    for (PendingResult<NodeId> p : pending) {
      if (p.result.isDenied()) {
        continue;
      }

      NodeId nodeId = p.value;
      List<NodeId> roleIds = context.getRoleIds().orElse(null);
      RolePermissionType[] userRolePermissions = attributes.get(nodeId).userRolePermissions();

      if (roleIds != null && userRolePermissions != null) {
        boolean hasAccess = hasPermission(roleIds, userRolePermissions, PermissionType::getBrowse);

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

    Map<NodeId, AccessControlAttributes> attributes =
        context.readAccessControlAttributes(nodeIds, ACCESS_ATTRIBUTES);

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
            hasPermission(roleIds, objectPermissions, PermissionType::getCall);

        boolean methodPermission =
            hasPermission(roleIds, methodPermissions, PermissionType::getCall);

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

      AccessResult result;
      if (p0.result.isDenied()) {
        result = p0.result;
      } else if (p1.result.isDenied()) {
        result = p1.result;
      } else {
        result = AccessResult.ALLOWED;
      }

      results.put(request, result);
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

    Map<NodeId, AccessControlAttributes> attributes =
        context.readAccessControlAttributes(nodeIds, ACCESS_ATTRIBUTES);

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
            hasPermission(roleIds, userRolePermissions, PermissionType::getAddReference);

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

    Map<NodeId, AccessControlAttributes> attributes =
        context.readAccessControlAttributes(nodeIds, ACCESS_ATTRIBUTES);

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
            hasPermission(roleIds, userRolePermissions, PermissionType::getDeleteNode);

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

    Map<NodeId, AccessControlAttributes> attributes =
        context.readAccessControlAttributes(nodeIds, ACCESS_ATTRIBUTES);

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
            hasPermission(roleIds, userRolePermissions, PermissionType::getRemoveReference);

        if (!hasAccess) {
          p.result = AccessResult.DENIED_USER_ACCESS;
        }
      }
    }

    return pending.stream().collect(Collectors.toMap(p -> p.value, p -> p.result, (a, b) -> b));
  }

  // endregion

  private static boolean hasPermission(
      List<NodeId> roleIds,
      RolePermissionType[] userRolePermissions,
      Predicate<PermissionType> permission) {

    return Stream.of(userRolePermissions)
        .anyMatch(rp -> roleIds.contains(rp.getRoleId()) && permission.test(rp.getPermissions()));
  }

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

    /**
     * Read {@code attributeIds} of each of {@code nodeIds}. NodeClass is always read, since it is
     * how a Node the AddressSpace does not know is told apart; an attribute not read is null.
     */
    Map<NodeId, AccessControlAttributes> readAccessControlAttributes(
        List<NodeId> nodeIds, Set<AttributeId> attributeIds);
  }

  /**
   * The attributes an access decision is made from, as the AddressSpace answered them.
   *
   * @param nodeUnknown {@code true} if the AddressSpace answered {@code Bad_NodeIdUnknown} for the
   *     Node, so that none of the other attributes could be read.
   */
  record AccessControlAttributes(
      @Nullable NodeClass nodeClass,
      @Nullable AccessRestrictionType accessRestrictions,
      @Nullable UInteger userWriteMask,
      @Nullable UByte accessLevel,
      @Nullable UByte userAccessLevel,
      @Nullable Boolean userExecutable,
      RolePermissionType @Nullable [] userRolePermissions,
      boolean nodeUnknown) {

    /** Attributes of a Node the AddressSpace knows. */
    AccessControlAttributes(
        @Nullable NodeClass nodeClass,
        @Nullable AccessRestrictionType accessRestrictions,
        @Nullable UInteger userWriteMask,
        @Nullable UByte accessLevel,
        @Nullable UByte userAccessLevel,
        @Nullable Boolean userExecutable,
        RolePermissionType @Nullable [] userRolePermissions) {

      this(
          nodeClass,
          accessRestrictions,
          userWriteMask,
          accessLevel,
          userAccessLevel,
          userExecutable,
          userRolePermissions,
          false);
    }
  }

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
    public Map<NodeId, AccessControlAttributes> readAccessControlAttributes(
        List<NodeId> nodeIds, Set<AttributeId> attributeIds) {

      List<AttributeId> toRead =
          ACCESS_ATTRIBUTE_ORDER.stream()
              .filter(id -> id == AttributeId.NodeClass || attributeIds.contains(id))
              .toList();

      List<NodeId> distinctNodeIds = nodeIds.stream().distinct().toList();

      List<ReadValueId> readValueIds =
          distinctNodeIds.stream()
              .flatMap(
                  id ->
                      toRead.stream()
                          .map(attributeId -> new ReadValueId(id, attributeId.uid(), null, null)))
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

      for (int i = 0; i < distinctNodeIds.size(); i++) {
        attributesMap.put(distinctNodeIds.get(i), attributes(toRead, values, i * toRead.size()));
      }

      return attributesMap;
    }

    /**
     * The attributes in {@code values} from {@code offset} on, which answer {@code attributeIds} in
     * order.
     */
    private static AccessControlAttributes attributes(
        List<AttributeId> attributeIds, List<DataValue> values, int offset) {

      NodeClass nodeClass = null;
      AccessRestrictionType accessRestrictions = null;
      UInteger userWriteMask = null;
      UByte accessLevel = null;
      UByte userAccessLevel = null;
      Boolean userExecutable = null;
      RolePermissionType[] userRolePermissions = null;
      boolean nodeUnknown = false;

      for (int j = 0; j < attributeIds.size(); j++) {
        DataValue value = values.get(offset + j);
        Object v = value.value().value();

        switch (attributeIds.get(j)) {
          case NodeClass -> {
            if (v instanceof NodeClass nc) {
              nodeClass = nc;
            }
            // NodeClass is mandatory on every Node, so Bad_NodeIdUnknown here means the
            // AddressSpace does not know the Node at all.
            nodeUnknown = value.statusCode().getValue() == StatusCodes.Bad_NodeIdUnknown;
          }
          case AccessRestrictions -> {
            if (v instanceof AccessRestrictionType art) {
              accessRestrictions = art;
            }
          }
          case UserWriteMask -> {
            if (v instanceof UInteger um) {
              userWriteMask = um;
            }
          }
          case AccessLevel -> {
            if (v instanceof UByte al) {
              accessLevel = al;
            }
          }
          case UserAccessLevel -> {
            if (v instanceof UByte ual) {
              userAccessLevel = ual;
            }
          }
          case UserExecutable -> {
            if (v instanceof Boolean b) {
              userExecutable = b;
            }
          }
          case UserRolePermissions -> {
            if (v instanceof RolePermissionType[] rpt) {
              userRolePermissions = rpt;
            }
          }
          default -> {}
        }
      }

      return new AccessControlAttributes(
          nodeClass,
          accessRestrictions,
          userWriteMask,
          accessLevel,
          userAccessLevel,
          userExecutable,
          userRolePermissions,
          nodeUnknown);
    }
  }
}
