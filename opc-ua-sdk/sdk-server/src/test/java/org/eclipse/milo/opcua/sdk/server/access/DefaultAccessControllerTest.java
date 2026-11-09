/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.access;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.WriteMask;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.RoleMapper;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.access.DefaultAccessController.AccessControlAttributes;
import org.eclipse.milo.opcua.sdk.server.access.DefaultAccessController.AccessControlContext;
import org.eclipse.milo.opcua.sdk.server.identity.Identity;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

class DefaultAccessControllerTest {

  private static final NodeId ROLE_A = new NodeId(1, "RoleA");
  private static final NodeId ROLE_B = new NodeId(1, "RoleB");

  private final AccessControlContext context = Mockito.mock(AccessControlContext.class);
  private final Map<NodeId, AccessControlAttributes> attributesMap = new HashMap<>();

  @BeforeEach
  void setup() {
    Mockito.when(context.readAccessControlAttributes(Mockito.anyList(), Mockito.any()))
        .thenReturn(attributesMap);
    Mockito.when(context.getNamespaceTable()).thenReturn(new NamespaceTable());
  }

  /**
   * A Node the AddressSpace does not know yields no decision rather than a denial, so Read still
   * reaches the AddressSpace, which answers Bad_NodeIdUnknown as it always has, and a component
   * that re-checks a MonitoredItem whose Node has been removed leaves the item's last result alone.
   */
  @Test
  void checkReadAccess_UnknownNode_NoDecision() {
    var nodeId = new NodeId(1, "removed");
    var readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);

    attributesMap.put(
        nodeId, new AccessControlAttributes(null, null, null, null, null, null, null, null, true));

    AccessResult result =
        DefaultAccessController.checkReadAccess(context, List.of(readValueId)).get(readValueId);

    assertEquals(AccessResult.NODE_UNKNOWN, result);
    assertFalse(result.isAllowed(), "no decision is not an allowance");
    assertFalse(result.isDenied(), "no decision is not a denial");
  }

  // An invalid attribute id is wrong whatever the Node, so it is still denied first.
  @Test
  void checkReadAccess_UnknownNodeWithInvalidAttributeId_DeniedAttributeIdInvalid() {
    var nodeId = new NodeId(1, "removed");
    var readValueId = new ReadValueId(nodeId, uint(99), null, null);

    attributesMap.put(
        nodeId, new AccessControlAttributes(null, null, null, null, null, null, null, null, true));

    AccessResult result =
        DefaultAccessController.checkReadAccess(context, List.of(readValueId)).get(readValueId);

    assertEquals(AccessResult.DENIED_ATTRIBUTE_ID_INVALID, result);
  }

  @Test
  void checkReadAccess_Value_Allowed() {
    var nodeId = new NodeId(1, "foo");
    var readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);

    UByte userAccessLevel = AccessLevel.toValue(AccessLevel.READ_ONLY);

    var attributes =
        new AccessControlAttributes(null, null, null, null, userAccessLevel, null, null);
    attributesMap.put(nodeId, attributes);

    AccessResult result =
        DefaultAccessController.checkReadAccess(context, List.of(readValueId)).get(readValueId);

    assertEquals(AccessResult.ALLOWED, result);
  }

  @Test
  void checkReadAccess_Value_Denied() {
    var nodeId = new NodeId(1, "foo");
    var readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);

    UByte userAccessLevel = AccessLevel.toValue(AccessLevel.NONE);

    var attributes =
        new AccessControlAttributes(null, null, null, null, userAccessLevel, null, null);
    attributesMap.put(nodeId, attributes);

    AccessResult result =
        DefaultAccessController.checkReadAccess(context, List.of(readValueId)).get(readValueId);

    assertEquals(AccessResult.DENIED_USER_ACCESS, result);
  }

  // Part 4 §7.38.2: Bad_NotReadable means the AccessLevel itself does not allow reading, which is
  // not a statement about the user, so it applies even when UserAccessLevel would allow the read.
  @Test
  void checkReadAccess_Value_NotReadable() {
    var nodeId = new NodeId(1, "foo");
    var readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);

    UByte accessLevel = AccessLevel.toValue(AccessLevel.WRITE_ONLY);
    UByte userAccessLevel = AccessLevel.toValue(AccessLevel.READ_ONLY);

    var attributes =
        new AccessControlAttributes(null, null, null, accessLevel, userAccessLevel, null, null);
    attributesMap.put(nodeId, attributes);

    AccessResult result =
        DefaultAccessController.checkReadAccess(context, List.of(readValueId)).get(readValueId);

    assertEquals(AccessResult.DENIED_NOT_READABLE, result);
  }

  // When both levels deny the read, the Node-level reason is reported.
  @Test
  void checkReadAccess_Value_NotReadableTakesPrecedenceOverUserAccessDenied() {
    var nodeId = new NodeId(1, "foo");
    var readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);

    UByte accessLevel = AccessLevel.toValue(AccessLevel.NONE);
    UByte userAccessLevel = AccessLevel.toValue(AccessLevel.NONE);

    var attributes =
        new AccessControlAttributes(null, null, null, accessLevel, userAccessLevel, null, null);
    attributesMap.put(nodeId, attributes);

    AccessResult result =
        DefaultAccessController.checkReadAccess(context, List.of(readValueId)).get(readValueId);

    assertEquals(AccessResult.DENIED_NOT_READABLE, result);
  }

  @Test
  void checkReadAccess_Value_ReadableButUserAccessDenied() {
    var nodeId = new NodeId(1, "foo");
    var readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);

    UByte accessLevel = AccessLevel.toValue(AccessLevel.READ_WRITE);
    UByte userAccessLevel = AccessLevel.toValue(AccessLevel.WRITE_ONLY);

    var attributes =
        new AccessControlAttributes(null, null, null, accessLevel, userAccessLevel, null, null);
    attributesMap.put(nodeId, attributes);

    AccessResult result =
        DefaultAccessController.checkReadAccess(context, List.of(readValueId)).get(readValueId);

    assertEquals(AccessResult.DENIED_USER_ACCESS, result);
  }

  @Test
  void checkReadAccess_RolePermissions_Allowed() {
    var nodeId = new NodeId(1, "foo");
    var readValueId = new ReadValueId(nodeId, AttributeId.RolePermissions.uid(), null, null);

    var attributes =
        new AccessControlAttributes(
            null,
            null,
            null,
            null,
            null,
            null,
            new RolePermissionType[] {
              new RolePermissionType(
                  ROLE_A, PermissionType.of(PermissionType.Field.ReadRolePermissions))
            });
    attributesMap.put(nodeId, attributes);

    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    AccessResult result =
        DefaultAccessController.checkReadAccess(context, List.of(readValueId)).get(readValueId);

    assertEquals(AccessResult.ALLOWED, result);
  }

  @Test
  void checkReadAccess_RolePermissions_Denied() {
    var nodeId = new NodeId(1, "foo");
    var readValueId = new ReadValueId(nodeId, AttributeId.RolePermissions.uid(), null, null);

    var attributes =
        new AccessControlAttributes(
            null,
            null,
            null,
            null,
            null,
            null,
            new RolePermissionType[] {new RolePermissionType(ROLE_A, PermissionType.of())});
    attributesMap.put(nodeId, attributes);

    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    AccessResult result =
        DefaultAccessController.checkReadAccess(context, List.of(readValueId)).get(readValueId);

    assertEquals(AccessResult.DENIED_USER_ACCESS, result);
  }

  @Test
  void checkReadAccess_InvalidAttributeId() {
    var nodeId = new NodeId(1, "foo");
    var invalidAttributeId = UInteger.valueOf(9999);
    var readValueId = new ReadValueId(nodeId, invalidAttributeId, null, null);

    var attributes = new AccessControlAttributes(null, null, null, null, null, null, null);
    attributesMap.put(nodeId, attributes);

    AccessResult result =
        DefaultAccessController.checkReadAccess(context, List.of(readValueId)).get(readValueId);

    assertEquals(AccessResult.DENIED_ATTRIBUTE_ID_INVALID, result);
  }

  // Part 3 §8.57: the HistoryRead bit of AccessLevel says the Variable's history is available, and
  // the same bit of UserAccessLevel says this user may read it.
  @Test
  void checkHistoryReadAccess_Variable_Allowed() {
    AccessControlAttributes attributes =
        variable(AccessLevel.HISTORY_READ_ONLY, AccessLevel.HISTORY_READ_ONLY);

    assertEquals(AccessResult.ALLOWED, checkHistoryReadAccess(attributes));
  }

  // Readable current values say nothing about history: a Variable without the HistoryRead bit has
  // no history to offer, which Part 4 §5.11.3.4 reports as Bad_HistoryOperationUnsupported.
  @Test
  void checkHistoryReadAccess_Variable_HistoryNotReadable() {
    AccessControlAttributes attributes = variable(AccessLevel.READ_WRITE, AccessLevel.READ_WRITE);

    assertEquals(AccessResult.DENIED_HISTORY_NOT_READABLE, checkHistoryReadAccess(attributes));
  }

  @Test
  void checkHistoryReadAccess_Variable_UserAccessDenied() {
    AccessControlAttributes attributes =
        variable(AccessLevel.HISTORY_READ_ONLY, AccessLevel.READ_ONLY);

    assertEquals(AccessResult.DENIED_USER_ACCESS, checkHistoryReadAccess(attributes));
  }

  // When both levels deny the read, the Node-level reason is reported, as it is for Read.
  @Test
  void checkHistoryReadAccess_Variable_HistoryNotReadableTakesPrecedenceOverUserAccessDenied() {
    AccessControlAttributes attributes = variable(AccessLevel.NONE, AccessLevel.NONE);

    assertEquals(AccessResult.DENIED_HISTORY_NOT_READABLE, checkHistoryReadAccess(attributes));
  }

  // Part 3 §8.59: for Objects and Views the HistoryRead bit of EventNotifier says whether the
  // history of Events is readable. SubscribeToEvents alone does not.
  @ParameterizedTest
  @EnumSource(
      value = NodeClass.class,
      names = {"Object", "View"})
  void checkHistoryReadAccess_EventNotifier(NodeClass nodeClass) {
    assertEquals(
        AccessResult.ALLOWED, checkHistoryReadAccess(eventNotifier(nodeClass, ubyte(0x05))));

    assertEquals(
        AccessResult.DENIED_HISTORY_NOT_READABLE,
        checkHistoryReadAccess(eventNotifier(nodeClass, ubyte(0x01))));
  }

  // Part 3 §8.55: ReadHistory is the permission for reading history. Read, the permission for the
  // current value, does not grant it, and a role the Session does not have grants nothing.
  @Test
  void checkHistoryReadAccess_RolePermissions() {
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    assertEquals(
        AccessResult.ALLOWED,
        checkHistoryReadAccess(
            historicalVariable(rolePermissions(PermissionType.Field.ReadHistory))));

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        checkHistoryReadAccess(historicalVariable(rolePermissions(PermissionType.Field.Read))));

    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_B)));

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        checkHistoryReadAccess(
            historicalVariable(rolePermissions(PermissionType.Field.ReadHistory))));
  }

  // A server without a RoleMapper has no roles to compare, so RolePermissions decide nothing.
  @Test
  void checkHistoryReadAccess_RolePermissions_IgnoredWithoutRoles() {
    Mockito.when(context.getRoleIds()).thenReturn(Optional.empty());

    assertEquals(
        AccessResult.ALLOWED,
        checkHistoryReadAccess(historicalVariable(rolePermissions(PermissionType.Field.Read))));
  }

  @Test
  void checkHistoryReadAccess_AccessRestrictions() {
    // EncryptionRequired
    var accessRestrictions = new AccessRestrictionType(UShort.valueOf(2));

    var attributes =
        new AccessControlAttributes(
            NodeClass.Variable,
            accessRestrictions,
            null,
            AccessLevel.toValue(AccessLevel.HISTORY_READ_ONLY),
            AccessLevel.toValue(AccessLevel.HISTORY_READ_ONLY),
            null,
            null,
            null,
            false);

    Mockito.when(context.getSecurityMode()).thenReturn(MessageSecurityMode.Sign);
    assertEquals(AccessResult.DENIED_SECURITY_MODE, checkHistoryReadAccess(attributes));

    Mockito.when(context.getSecurityMode()).thenReturn(MessageSecurityMode.SignAndEncrypt);
    assertEquals(AccessResult.ALLOWED, checkHistoryReadAccess(attributes));
  }

  // As for Read, an unknown Node is no decision, so HistoryRead still reaches the AddressSpace and
  // the client is told Bad_NodeIdUnknown rather than that access was denied.
  @Test
  void checkHistoryReadAccess_UnknownNode_NoDecision() {
    var attributes =
        new AccessControlAttributes(null, null, null, null, null, null, null, null, true);

    assertEquals(AccessResult.NODE_UNKNOWN, checkHistoryReadAccess(attributes));
  }

  // Two reads of one Node that differ only in their continuation point are separate operations,
  // and the service looks each one up by instance.
  @Test
  void checkHistoryReadAccess_ResultsAreKeyedByIdentity() {
    var nodeId = new NodeId(1, "foo");
    var first = new HistoryReadValueId(nodeId, null, null, null);
    var second = new HistoryReadValueId(nodeId, null, null, null);

    attributesMap.put(nodeId, variable(AccessLevel.READ_WRITE, AccessLevel.READ_WRITE));

    Map<HistoryReadValueId, AccessResult> results =
        DefaultAccessController.checkHistoryReadAccess(context, List.of(first, second));

    assertEquals(2, results.size());
    assertEquals(AccessResult.DENIED_HISTORY_NOT_READABLE, results.get(first));
    assertEquals(AccessResult.DENIED_HISTORY_NOT_READABLE, results.get(second));
  }

  private AccessResult checkHistoryReadAccess(AccessControlAttributes attributes) {
    var nodeId = new NodeId(1, "foo");
    var historyReadValueId = new HistoryReadValueId(nodeId, null, null, null);

    attributesMap.put(nodeId, attributes);

    return DefaultAccessController.checkHistoryReadAccess(context, List.of(historyReadValueId))
        .get(historyReadValueId);
  }

  private static AccessControlAttributes variable(
      Set<AccessLevel> accessLevel, Set<AccessLevel> userAccessLevel) {

    return new AccessControlAttributes(
        NodeClass.Variable,
        null,
        null,
        AccessLevel.toValue(accessLevel),
        AccessLevel.toValue(userAccessLevel),
        null,
        null);
  }

  /** A Variable whose access levels allow reading history, with {@code userRolePermissions}. */
  private static AccessControlAttributes historicalVariable(
      RolePermissionType[] userRolePermissions) {

    return new AccessControlAttributes(
        NodeClass.Variable,
        null,
        null,
        AccessLevel.toValue(AccessLevel.HISTORY_READ_ONLY),
        AccessLevel.toValue(AccessLevel.HISTORY_READ_ONLY),
        null,
        userRolePermissions);
  }

  private static AccessControlAttributes eventNotifier(NodeClass nodeClass, UByte eventNotifier) {
    return new AccessControlAttributes(
        nodeClass, null, null, null, null, eventNotifier, null, null, false);
  }

  @Test
  void checkWriteAccess_Value_Allowed() {
    var nodeId = new NodeId(1, "foo");
    var writeValue =
        new WriteValue(
            nodeId, AttributeId.Value.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));

    UByte userAccessLevel = AccessLevel.toValue(AccessLevel.READ_WRITE);

    var attributes =
        new AccessControlAttributes(null, null, null, null, userAccessLevel, null, null);
    attributesMap.put(nodeId, attributes);

    AccessResult result =
        DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

    assertEquals(AccessResult.ALLOWED, result);
  }

  // WriteValue.hashCode hashes the value being written, every element of an array value, so a
  // check that keyed its results that way cost time in proportion to the size of the write.
  @Test
  void checkWriteAccess_DoesNotHashTheWriteValue() {
    var nodeId = new NodeId(1, "foo");
    var writeValue =
        new WriteValue(
            nodeId,
            AttributeId.Value.uid(),
            null,
            DataValue.valueOnly(Variant.ofInt32Array(new int[1024]))) {

          @Override
          public int hashCode() {
            throw new AssertionError("checkWriteAccess hashed the WriteValue");
          }
        };

    UByte userAccessLevel = AccessLevel.toValue(AccessLevel.READ_WRITE);
    attributesMap.put(
        nodeId, new AccessControlAttributes(null, null, null, null, userAccessLevel, null, null));

    AccessResult result =
        DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

    assertEquals(AccessResult.ALLOWED, result);
  }

  // The results are keyed by WriteValue identity, as checkWriteAccess documents: each instance in
  // a request has its own entry, even when equal to another, and an equal copy finds nothing.
  @Test
  void checkWriteAccess_ResultsAreKeyedByIdentity() {
    var nodeId = new NodeId(1, "foo");
    var first =
        new WriteValue(
            nodeId, AttributeId.Value.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));
    var second =
        new WriteValue(
            nodeId, AttributeId.Value.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));
    var copy =
        new WriteValue(
            nodeId, AttributeId.Value.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));

    UByte userAccessLevel = AccessLevel.toValue(AccessLevel.READ_WRITE);
    attributesMap.put(
        nodeId, new AccessControlAttributes(null, null, null, null, userAccessLevel, null, null));

    Map<WriteValue, AccessResult> results =
        DefaultAccessController.checkWriteAccess(context, List.of(first, second));

    assertEquals(2, results.size());
    assertEquals(AccessResult.ALLOWED, results.get(first));
    assertEquals(AccessResult.ALLOWED, results.get(second));
    assertNull(results.get(copy), "lookup with an equal but different WriteValue");
  }

  @Test
  void checkWriteAccess_Value_Denied() {
    var nodeId = new NodeId(1, "foo");
    var writeValue =
        new WriteValue(
            nodeId, AttributeId.Value.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));

    UByte userAccessLevel = AccessLevel.toValue(AccessLevel.READ_ONLY);

    var attributes =
        new AccessControlAttributes(null, null, null, null, userAccessLevel, null, null);
    attributesMap.put(nodeId, attributes);

    AccessResult result =
        DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

    assertEquals(AccessResult.DENIED_USER_ACCESS, result);
  }

  /**
   * Part 4 §5.10.4: a Write to a Variable whose AccessLevel lacks CurrentWrite is Bad_NotWritable,
   * a property of the Node, so the controller decides it before looking at the Session's
   * UserAccessLevel, the same way a Read decides Bad_NotReadable before Bad_UserAccessDenied.
   */
  @Test
  void checkWriteAccess_Value_NotWritable() {
    var nodeId = new NodeId(1, "foo");
    var writeValue =
        new WriteValue(
            nodeId, AttributeId.Value.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));

    UByte accessLevel = AccessLevel.toValue(AccessLevel.READ_ONLY);
    UByte userAccessLevel = AccessLevel.toValue(AccessLevel.READ_WRITE);

    var attributes =
        new AccessControlAttributes(null, null, null, accessLevel, userAccessLevel, null, null);
    attributesMap.put(nodeId, attributes);

    AccessResult result =
        DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

    assertEquals(AccessResult.DENIED_NOT_WRITABLE, result);
  }

  // When neither attribute allows writing the client learns that the Node is not writable at all,
  // not that this user in particular may not write it.
  @Test
  void checkWriteAccess_Value_NotWritableTakesPrecedenceOverUserAccess() {
    var nodeId = new NodeId(1, "foo");
    var writeValue =
        new WriteValue(
            nodeId, AttributeId.Value.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));

    UByte readOnly = AccessLevel.toValue(AccessLevel.READ_ONLY);

    var attributes = new AccessControlAttributes(null, null, null, readOnly, readOnly, null, null);
    attributesMap.put(nodeId, attributes);

    AccessResult result =
        DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

    assertEquals(AccessResult.DENIED_NOT_WRITABLE, result);
  }

  @Test
  void checkWriteAccess_InvalidAttributeId() {
    var nodeId = new NodeId(1, "foo");
    var invalidAttributeId = UInteger.valueOf(9999);
    var writeValue =
        new WriteValue(nodeId, invalidAttributeId, null, DataValue.valueOnly(Variant.NULL_VALUE));

    var attributes = new AccessControlAttributes(null, null, null, null, null, null, null);
    attributesMap.put(nodeId, attributes);

    AccessResult result =
        DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

    assertEquals(AccessResult.DENIED_ATTRIBUTE_ID_INVALID, result);
  }

  @Test
  void checkWriteAccess_AttributeProtectedByUserWriteMask_Allowed() {
    var nodeId = new NodeId(1, "wm-node");

    var wvDisplayName =
        new WriteValue(
            nodeId, AttributeId.DisplayName.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));
    var wvDescription =
        new WriteValue(
            nodeId, AttributeId.Description.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));
    var wvBrowseName =
        new WriteValue(
            nodeId, AttributeId.BrowseName.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));

    // Roles/Permissions configuration is not required; they are implied by UserWriteMask value.
    attributesMap.put(
        nodeId,
        new AccessControlAttributes(
            null, null, UInteger.MAX, null, null, null, new RolePermissionType[] {}));
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of()));

    var results =
        DefaultAccessController.checkWriteAccess(
            context, List.of(wvDisplayName, wvDescription, wvBrowseName));
    assertEquals(AccessResult.ALLOWED, results.get(wvDisplayName));
    assertEquals(AccessResult.ALLOWED, results.get(wvDescription));
    assertEquals(AccessResult.ALLOWED, results.get(wvBrowseName));
  }

  @Test
  void checkWriteAccess_AttributeProtectedByUserWriteMask_Denied() {
    var nodeId = new NodeId(1, "wm-node");

    var wvDisplayName =
        new WriteValue(
            nodeId, AttributeId.DisplayName.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));
    var wvDescription =
        new WriteValue(
            nodeId, AttributeId.Description.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));
    var wvBrowseName =
        new WriteValue(
            nodeId, AttributeId.BrowseName.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));

    // Roles/Permissions configuration is not required; they are implied by UserWriteMask value.
    attributesMap.put(
        nodeId,
        new AccessControlAttributes(
            null, null, UInteger.MIN, null, null, null, new RolePermissionType[] {}));
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of()));

    var results =
        DefaultAccessController.checkWriteAccess(
            context, List.of(wvDisplayName, wvDescription, wvBrowseName));
    assertEquals(AccessResult.DENIED_USER_ACCESS, results.get(wvDisplayName));
    assertEquals(AccessResult.DENIED_USER_ACCESS, results.get(wvDescription));
    assertEquals(AccessResult.DENIED_USER_ACCESS, results.get(wvBrowseName));
  }

  @Test
  void checkWriteAccess_RolePermissions_DeniedByUserWriteMask() {
    var nodeId = new NodeId(1, "foo");
    var writeValue =
        new WriteValue(
            nodeId,
            AttributeId.RolePermissions.uid(),
            null,
            DataValue.valueOnly(Variant.NULL_VALUE));

    attributesMap.put(
        nodeId,
        new AccessControlAttributes(
            null,
            null,
            UInteger.MIN,
            null,
            null,
            null,
            new RolePermissionType[] {
              new RolePermissionType(
                  ROLE_A, PermissionType.of(PermissionType.Field.WriteRolePermissions))
            }));
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    AccessResult result =
        DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

    assertEquals(AccessResult.DENIED_USER_ACCESS, result);
  }

  @Test
  void checkWriteAccess_Historizing_DeniedByUserWriteMask() {
    var nodeId = new NodeId(1, "foo");
    var writeValue =
        new WriteValue(
            nodeId, AttributeId.Historizing.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));

    attributesMap.put(
        nodeId,
        new AccessControlAttributes(
            null,
            null,
            UInteger.MIN,
            null,
            null,
            null,
            new RolePermissionType[] {
              new RolePermissionType(
                  ROLE_A, PermissionType.of(PermissionType.Field.WriteHistorizing))
            }));
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    AccessResult result =
        DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

    assertEquals(AccessResult.DENIED_USER_ACCESS, result);
  }

  @Test
  void checkWriteAccess_UserRolePermissions_DeniedByUserWriteMask() {
    var nodeId = new NodeId(1, "foo");
    var writeValue =
        new WriteValue(
            nodeId,
            AttributeId.UserRolePermissions.uid(),
            null,
            DataValue.valueOnly(Variant.NULL_VALUE));

    attributesMap.put(
        nodeId,
        new AccessControlAttributes(
            null, null, UInteger.MAX, null, null, null, new RolePermissionType[] {}));
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of()));

    AccessResult result =
        DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

    assertEquals(AccessResult.DENIED_USER_ACCESS, result);
  }

  @Test
  void checkWriteAccess_RolePermissions_AllowedByUserWriteMask() {
    var nodeId = new NodeId(1, "foo");
    var writeValue =
        new WriteValue(
            nodeId,
            AttributeId.RolePermissions.uid(),
            null,
            DataValue.valueOnly(Variant.NULL_VALUE));

    attributesMap.put(
        nodeId,
        new AccessControlAttributes(
            null,
            null,
            UInteger.valueOf(WriteMask.RolePermissions.getValue()),
            null,
            null,
            null,
            new RolePermissionType[] {
              new RolePermissionType(
                  ROLE_A, PermissionType.of(PermissionType.Field.WriteRolePermissions))
            }));
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    AccessResult result =
        DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

    assertEquals(AccessResult.ALLOWED, result);
  }

  @Test
  void checkWriteAccess_NullUserWriteMask_Allowed() {
    // Simulate a non-existent node - all attributes would be null/unreadable
    var nonExistentNodeId = new NodeId(1, "non-existent-node");

    var wvDisplayName =
        new WriteValue(
            nonExistentNodeId,
            AttributeId.DisplayName.uid(),
            null,
            DataValue.valueOnly(Variant.NULL_VALUE));
    var wvDescription =
        new WriteValue(
            nonExistentNodeId,
            AttributeId.Description.uid(),
            null,
            DataValue.valueOnly(Variant.NULL_VALUE));
    var wvBrowseName =
        new WriteValue(
            nonExistentNodeId,
            AttributeId.BrowseName.uid(),
            null,
            DataValue.valueOnly(Variant.NULL_VALUE));

    // For a non-existent node, all attributes including userWriteMask would be null.
    // The access check should return ALLOWED so the operation proceeds and fails
    // later with Bad_NodeIdUnknown rather than incorrectly returning Bad_UserAccessDenied.
    attributesMap.put(
        nonExistentNodeId, new AccessControlAttributes(null, null, null, null, null, null, null));
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of()));

    var results =
        DefaultAccessController.checkWriteAccess(
            context, List.of(wvDisplayName, wvDescription, wvBrowseName));

    assertEquals(AccessResult.ALLOWED, results.get(wvDisplayName));
    assertEquals(AccessResult.ALLOWED, results.get(wvDescription));
    assertEquals(AccessResult.ALLOWED, results.get(wvBrowseName));
  }

  @Test
  void checkWriteAccess_RolePermissions() {
    var nodeId = new NodeId(1, "foo");
    var writeValue =
        new WriteValue(
            nodeId,
            AttributeId.RolePermissions.uid(),
            null,
            DataValue.valueOnly(Variant.NULL_VALUE));

    {
      attributesMap.put(
          nodeId,
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {new RolePermissionType(ROLE_A, PermissionType.of())}));

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

      AccessResult result =
          DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

      assertEquals(AccessResult.DENIED_USER_ACCESS, result);
    }

    {
      attributesMap.put(
          nodeId,
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {
                new RolePermissionType(
                    ROLE_B, PermissionType.of(PermissionType.Field.WriteRolePermissions))
              }));

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_B)));

      AccessResult result =
          DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

      assertEquals(AccessResult.ALLOWED, result);
    }
  }

  @Test
  void checkWriteAccess_Historizing() {
    var nodeId = new NodeId(1, "foo");
    var writeValue =
        new WriteValue(
            nodeId, AttributeId.Historizing.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));

    {
      attributesMap.put(
          nodeId,
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {
                new RolePermissionType(ROLE_A, PermissionType.of()),
              }));

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

      AccessResult result =
          DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

      assertEquals(AccessResult.DENIED_USER_ACCESS, result);
    }

    {
      attributesMap.put(
          nodeId,
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {
                new RolePermissionType(
                    ROLE_B, PermissionType.of(PermissionType.Field.WriteHistorizing)),
              }));

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_B)));

      AccessResult result =
          DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

      assertEquals(AccessResult.ALLOWED, result);
    }
  }

  @Test
  void checkBrowseAccess() {
    var nodeId = new NodeId(1, "foo");

    {
      attributesMap.put(
          nodeId,
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {new RolePermissionType(ROLE_A, PermissionType.of())}));

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

      AccessResult result =
          DefaultAccessController.checkBrowseAccess(context, List.of(nodeId)).get(nodeId);

      assertEquals(AccessResult.DENIED_USER_ACCESS, result);
    }

    {
      attributesMap.put(
          nodeId,
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {
                new RolePermissionType(ROLE_B, PermissionType.of(PermissionType.Field.Browse))
              }));

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_B)));

      AccessResult result =
          DefaultAccessController.checkBrowseAccess(context, List.of(nodeId)).get(nodeId);

      assertEquals(AccessResult.ALLOWED, result);
    }
  }

  @Test
  void checkCallAccess() {
    var objectNodeId = new NodeId(1, "object");
    var methodNodeId = new NodeId(1, "method");
    var callMethodRequest = new CallMethodRequest(objectNodeId, methodNodeId, null);

    {
      var attributes =
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {
                new RolePermissionType(ROLE_A, PermissionType.of()),
              });
      attributesMap.put(objectNodeId, attributes);
      attributesMap.put(methodNodeId, attributes);

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

      AccessResult result =
          DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
              .get(callMethodRequest);

      assertEquals(AccessResult.DENIED_USER_ACCESS, result);
    }

    {
      var attributes =
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {
                new RolePermissionType(ROLE_B, PermissionType.of(PermissionType.Field.Call))
              });
      attributesMap.put(objectNodeId, attributes);
      attributesMap.put(methodNodeId, attributes);

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_B)));

      AccessResult result =
          DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
              .get(callMethodRequest);

      assertEquals(AccessResult.ALLOWED, result);
    }
  }

  @Test
  void checkCallAccess_UserExecutable() {
    var objectNodeId = new NodeId(1, "object");
    var methodNodeId = new NodeId(1, "method");
    var callMethodRequest = new CallMethodRequest(objectNodeId, methodNodeId, null);

    attributesMap.put(
        objectNodeId, new AccessControlAttributes(null, null, null, null, null, null, null));

    {
      attributesMap.put(
          methodNodeId, new AccessControlAttributes(null, null, null, null, null, false, null));

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of()));

      AccessResult result =
          DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
              .get(callMethodRequest);

      assertEquals(AccessResult.DENIED_USER_ACCESS, result);
    }

    {
      attributesMap.put(
          methodNodeId, new AccessControlAttributes(null, null, null, null, null, true, null));

      AccessResult result =
          DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
              .get(callMethodRequest);

      assertEquals(AccessResult.ALLOWED, result);
    }
  }

  @Test
  void checkCallAccess_AccessRestrictions() {
    var objectNodeId = new NodeId(1, "object");
    var methodNodeId = new NodeId(1, "method");
    var callMethodRequest = new CallMethodRequest(objectNodeId, methodNodeId, null);

    // SigningRequired | EncryptionRequired
    var accessRestrictions = new AccessRestrictionType(UShort.valueOf(3));

    attributesMap.put(
        objectNodeId, new AccessControlAttributes(null, null, null, null, null, null, null));
    attributesMap.put(
        methodNodeId,
        new AccessControlAttributes(null, accessRestrictions, null, null, null, null, null));

    {
      Mockito.when(context.getSecurityMode()).thenReturn(MessageSecurityMode.None);

      AccessResult result =
          DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
              .get(callMethodRequest);

      assertEquals(AccessResult.DENIED_SECURITY_MODE, result);
    }

    {
      Mockito.when(context.getSecurityMode()).thenReturn(MessageSecurityMode.Sign);

      AccessResult result =
          DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
              .get(callMethodRequest);

      assertEquals(AccessResult.DENIED_SECURITY_MODE, result);
    }

    {
      Mockito.when(context.getSecurityMode()).thenReturn(MessageSecurityMode.SignAndEncrypt);

      AccessResult result =
          DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
              .get(callMethodRequest);

      assertEquals(AccessResult.ALLOWED, result);
    }
  }

  @Test
  void checkCallAccess_AccessRestrictions_ObjectNode() {
    var objectNodeId = new NodeId(1, "object");
    var methodNodeId = new NodeId(1, "method");
    var callMethodRequest = new CallMethodRequest(objectNodeId, methodNodeId, null);

    // SigningRequired | EncryptionRequired
    var accessRestrictions = new AccessRestrictionType(UShort.valueOf(3));

    attributesMap.put(
        objectNodeId,
        new AccessControlAttributes(null, accessRestrictions, null, null, null, null, null));
    attributesMap.put(
        methodNodeId, new AccessControlAttributes(null, null, null, null, null, null, null));

    {
      Mockito.when(context.getSecurityMode()).thenReturn(MessageSecurityMode.None);

      AccessResult result =
          DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
              .get(callMethodRequest);

      assertEquals(AccessResult.DENIED_SECURITY_MODE, result);
    }

    {
      Mockito.when(context.getSecurityMode()).thenReturn(MessageSecurityMode.SignAndEncrypt);

      AccessResult result =
          DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
              .get(callMethodRequest);

      assertEquals(AccessResult.ALLOWED, result);
    }
  }

  @Test
  void checkCallAccess_RolePermissions_DeniedOnSecureChannel() {
    var objectNodeId = new NodeId(1, "object");
    var methodNodeId = new NodeId(1, "method");
    var callMethodRequest = new CallMethodRequest(objectNodeId, methodNodeId, null);

    var attributes =
        new AccessControlAttributes(
            null,
            null,
            null,
            null,
            null,
            null,
            new RolePermissionType[] {
              new RolePermissionType(ROLE_A, PermissionType.of()),
            });
    attributesMap.put(objectNodeId, attributes);
    attributesMap.put(methodNodeId, attributes);

    Mockito.when(context.getSecurityMode()).thenReturn(MessageSecurityMode.SignAndEncrypt);
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    AccessResult result =
        DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
            .get(callMethodRequest);

    assertEquals(AccessResult.DENIED_USER_ACCESS, result);
  }

  @Test
  void checkAddReferences() {
    var sourceNodeId = new NodeId(1, "source");
    var targetNodeId = new NodeId(1, "target");
    var addReferencesItem =
        new AddReferencesItem(
            sourceNodeId,
            NodeIds.HasComponent,
            true,
            null,
            targetNodeId.expanded(),
            NodeClass.Object);

    {
      var attributes =
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {new RolePermissionType(ROLE_A, PermissionType.of())});
      attributesMap.put(sourceNodeId, attributes);
      attributesMap.put(targetNodeId, attributes);

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

      AccessResult result =
          DefaultAccessController.checkAddReferencesAccess(context, List.of(addReferencesItem))
              .get(addReferencesItem);

      assertEquals(AccessResult.DENIED_USER_ACCESS, result);
    }

    {
      var attributes =
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {
                new RolePermissionType(ROLE_B, PermissionType.of(PermissionType.Field.AddReference))
              });
      attributesMap.put(sourceNodeId, attributes);
      attributesMap.put(targetNodeId, attributes);

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_B)));

      AccessResult result =
          DefaultAccessController.checkAddReferencesAccess(context, List.of(addReferencesItem))
              .get(addReferencesItem);

      assertEquals(AccessResult.ALLOWED, result);
    }
  }

  // Adding a Node adds a Reference from its parent, so the parent's AddReference permission
  // (Part 3 §8.55) decides, the way it does for AddReferences.
  @Test
  void checkAddNodesAccess_ParentLacksAddReferencePermission_Denied() {
    var parentNodeId = new NodeId(1, "parent");
    AddNodesItem addNodesItem = addNodesItem(parentNodeId.expanded());

    attributesMap.put(
        parentNodeId,
        new AccessControlAttributes(
            null, null, null, null, null, null, rolePermissions(PermissionType.Field.Browse)));

    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    AccessResult result =
        DefaultAccessController.checkAddNodesAccess(context, List.of(addNodesItem))
            .get(addNodesItem);

    assertEquals(AccessResult.DENIED_USER_ACCESS, result);
  }

  @Test
  void checkAddNodesAccess_ParentGrantsAddReferencePermission_Allowed() {
    var parentNodeId = new NodeId(1, "parent");
    AddNodesItem addNodesItem = addNodesItem(parentNodeId.expanded());

    attributesMap.put(
        parentNodeId,
        new AccessControlAttributes(
            null,
            null,
            null,
            null,
            null,
            null,
            rolePermissions(PermissionType.Field.AddReference)));

    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    AccessResult result =
        DefaultAccessController.checkAddNodesAccess(context, List.of(addNodesItem))
            .get(addNodesItem);

    assertEquals(AccessResult.ALLOWED, result);
  }

  // A parent that requires encryption cannot be added to over an unencrypted channel, whatever
  // the Session's roles.
  @Test
  void checkAddNodesAccess_ParentAccessRestrictions_DeniedSecurityMode() {
    var parentNodeId = new NodeId(1, "parent");
    AddNodesItem addNodesItem = addNodesItem(parentNodeId.expanded());

    // SigningRequired | EncryptionRequired
    var accessRestrictions = new AccessRestrictionType(UShort.valueOf(3));

    attributesMap.put(
        parentNodeId,
        new AccessControlAttributes(null, accessRestrictions, null, null, null, null, null));

    Mockito.when(context.getSecurityMode()).thenReturn(MessageSecurityMode.Sign);

    assertEquals(
        AccessResult.DENIED_SECURITY_MODE,
        DefaultAccessController.checkAddNodesAccess(context, List.of(addNodesItem))
            .get(addNodesItem));

    Mockito.when(context.getSecurityMode()).thenReturn(MessageSecurityMode.SignAndEncrypt);

    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkAddNodesAccess(context, List.of(addNodesItem))
            .get(addNodesItem),
        "the same parent allows the add once the channel is encrypted");
  }

  /**
   * A parent the controller cannot resolve to a local Node gives it nothing to decide from, so the
   * item is allowed through and the AddressSpace answers Bad_ParentNodeIdInvalid (Part 4 §5.8.2)
   * rather than the controller hiding it behind a denial. The resolvable item in the same request
   * is still decided from its own parent.
   */
  @Test
  void checkAddNodesAccess_ParentNotALocalNode_AllowedWithoutReadingIt() {
    var deniedParentNodeId = new NodeId(1, "parent");

    AddNodesItem nullParent = addNodesItem(ExpandedNodeId.NULL_VALUE);
    AddNodesItem remoteParent =
        addNodesItem(new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of(1), "remote"));
    AddNodesItem unregisteredNamespace =
        addNodesItem(ExpandedNodeId.of("urn:not:registered", "parent"));
    AddNodesItem deniedItem = addNodesItem(deniedParentNodeId.expanded());

    attributesMap.put(
        deniedParentNodeId,
        new AccessControlAttributes(
            null, null, null, null, null, null, rolePermissions(PermissionType.Field.Browse)));

    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    Map<AddNodesItem, AccessResult> results =
        DefaultAccessController.checkAddNodesAccess(
            context, List.of(nullParent, remoteParent, unregisteredNamespace, deniedItem));

    assertEquals(AccessResult.ALLOWED, results.get(nullParent));
    assertEquals(AccessResult.ALLOWED, results.get(remoteParent));
    assertEquals(AccessResult.ALLOWED, results.get(unregisteredNamespace));
    assertEquals(AccessResult.DENIED_USER_ACCESS, results.get(deniedItem));
  }

  @Test
  void checkDeleteNodes() {
    var deleteNodesItem = new DeleteNodesItem(new NodeId(1, "foo"), true);

    {
      var attributes =
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {new RolePermissionType(ROLE_A, PermissionType.of())});
      attributesMap.put(deleteNodesItem.getNodeId(), attributes);

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

      AccessResult result =
          DefaultAccessController.checkDeleteNodesAccess(context, List.of(deleteNodesItem))
              .get(deleteNodesItem);

      assertEquals(AccessResult.DENIED_USER_ACCESS, result);
    }

    {
      var attributes =
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {
                new RolePermissionType(ROLE_B, PermissionType.of(PermissionType.Field.DeleteNode))
              });
      attributesMap.put(deleteNodesItem.getNodeId(), attributes);

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_B)));

      AccessResult result =
          DefaultAccessController.checkDeleteNodesAccess(context, List.of(deleteNodesItem))
              .get(deleteNodesItem);

      assertEquals(AccessResult.ALLOWED, result);
    }
  }

  @Test
  void checkDeleteReferences() {
    var sourceNodeId = new NodeId(1, "source");
    var targetNodeId = new NodeId(1, "target");
    var deleteReferencesItem =
        new DeleteReferencesItem(
            sourceNodeId, NodeIds.HasComponent, true, targetNodeId.expanded(), true);

    {
      var attributes =
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {new RolePermissionType(ROLE_A, PermissionType.of())});
      attributesMap.put(deleteReferencesItem.getSourceNodeId(), attributes);

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

      AccessResult result =
          DefaultAccessController.checkDeleteReferencesAccess(
                  context, List.of(deleteReferencesItem))
              .get(deleteReferencesItem);

      assertEquals(AccessResult.DENIED_USER_ACCESS, result);
    }

    {
      var attributes =
          new AccessControlAttributes(
              null,
              null,
              null,
              null,
              null,
              null,
              new RolePermissionType[] {
                new RolePermissionType(
                    ROLE_B, PermissionType.of(PermissionType.Field.RemoveReference))
              });
      attributesMap.put(deleteReferencesItem.getSourceNodeId(), attributes);

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_B)));

      AccessResult result =
          DefaultAccessController.checkDeleteReferencesAccess(
                  context, List.of(deleteReferencesItem))
              .get(deleteReferencesItem);

      assertEquals(AccessResult.ALLOWED, result);
    }
  }

  @Test
  void checkRolePermissionAccess_DeniesPermissionsForUnassignedRoles() {
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_B)));

    var readRolePermissionsNodeId = new NodeId(1, "readRolePermissions");
    var readValueId =
        new ReadValueId(readRolePermissionsNodeId, AttributeId.RolePermissions.uid(), null, null);
    attributesMap.put(
        readRolePermissionsNodeId,
        new AccessControlAttributes(
            null,
            null,
            null,
            null,
            null,
            null,
            rolePermissions(PermissionType.Field.ReadRolePermissions)));

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkReadAccess(context, List.of(readValueId)).get(readValueId));

    var writeRolePermissionsNodeId = new NodeId(1, "writeRolePermissions");
    var writeRolePermissionsValue =
        new WriteValue(
            writeRolePermissionsNodeId,
            AttributeId.RolePermissions.uid(),
            null,
            DataValue.valueOnly(Variant.NULL_VALUE));
    attributesMap.put(
        writeRolePermissionsNodeId,
        new AccessControlAttributes(
            null,
            null,
            null,
            null,
            null,
            null,
            rolePermissions(PermissionType.Field.WriteRolePermissions)));

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkWriteAccess(context, List.of(writeRolePermissionsValue))
            .get(writeRolePermissionsValue));

    var writeHistorizingNodeId = new NodeId(1, "writeHistorizing");
    var writeHistorizingValue =
        new WriteValue(
            writeHistorizingNodeId,
            AttributeId.Historizing.uid(),
            null,
            DataValue.valueOnly(Variant.NULL_VALUE));
    attributesMap.put(
        writeHistorizingNodeId,
        new AccessControlAttributes(
            null,
            null,
            null,
            null,
            null,
            null,
            rolePermissions(PermissionType.Field.WriteHistorizing)));

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkWriteAccess(context, List.of(writeHistorizingValue))
            .get(writeHistorizingValue));

    var browseNodeId = new NodeId(1, "browse");
    attributesMap.put(
        browseNodeId,
        new AccessControlAttributes(
            null, null, null, null, null, null, rolePermissions(PermissionType.Field.Browse)));

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkBrowseAccess(context, List.of(browseNodeId))
            .get(browseNodeId));

    var objectNodeId = new NodeId(1, "object");
    var methodNodeId = new NodeId(1, "method");
    var callMethodRequest = new CallMethodRequest(objectNodeId, methodNodeId, null);
    attributesMap.put(
        objectNodeId,
        new AccessControlAttributes(
            null, null, null, null, null, null, rolePermissions(PermissionType.Field.Call)));
    attributesMap.put(
        methodNodeId,
        new AccessControlAttributes(
            null, null, null, null, null, null, rolePermissions(PermissionType.Field.Call)));

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
            .get(callMethodRequest));

    var addReferenceSourceNodeId = new NodeId(1, "addReferenceSource");
    var addReferenceTargetNodeId = new NodeId(1, "addReferenceTarget");
    var addReferencesItem =
        new AddReferencesItem(
            addReferenceSourceNodeId,
            NodeIds.HasComponent,
            true,
            null,
            addReferenceTargetNodeId.expanded(),
            NodeClass.Object);
    attributesMap.put(
        addReferenceSourceNodeId,
        new AccessControlAttributes(
            null,
            null,
            null,
            null,
            null,
            null,
            rolePermissions(PermissionType.Field.AddReference)));

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkAddReferencesAccess(context, List.of(addReferencesItem))
            .get(addReferencesItem));

    var deleteNodesItem = new DeleteNodesItem(new NodeId(1, "deleteNode"), true);
    attributesMap.put(
        deleteNodesItem.getNodeId(),
        new AccessControlAttributes(
            null, null, null, null, null, null, rolePermissions(PermissionType.Field.DeleteNode)));

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkDeleteNodesAccess(context, List.of(deleteNodesItem))
            .get(deleteNodesItem));

    var deleteReferenceSourceNodeId = new NodeId(1, "deleteReferenceSource");
    var deleteReferenceTargetNodeId = new NodeId(1, "deleteReferenceTarget");
    var deleteReferencesItem =
        new DeleteReferencesItem(
            deleteReferenceSourceNodeId,
            NodeIds.HasComponent,
            true,
            deleteReferenceTargetNodeId.expanded(),
            true);
    attributesMap.put(
        deleteReferenceSourceNodeId,
        new AccessControlAttributes(
            null,
            null,
            null,
            null,
            null,
            null,
            rolePermissions(PermissionType.Field.RemoveReference)));

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkDeleteReferencesAccess(context, List.of(deleteReferencesItem))
            .get(deleteReferencesItem));
  }

  @Test
  void copiedConfigPreservesRoleBasedAuthorizationBehavior() {
    Identity identity = Mockito.mock(Identity.class);
    RoleMapper roleMapper = ignored -> List.of(ROLE_B);
    OpcUaServerConfig directConfig = OpcUaServerConfig.builder().setRoleMapper(roleMapper).build();
    OpcUaServerConfig copiedConfig = OpcUaServerConfig.copy(directConfig).build();

    var nodeId = new NodeId(1, "browse");
    attributesMap.put(
        nodeId,
        new AccessControlAttributes(
            null, null, null, null, null, null, rolePermissions(PermissionType.Field.Browse)));

    Mockito.when(context.getRoleIds())
        .thenReturn(directConfig.getRoleMapper().map(mapper -> mapper.getRoleIds(identity)))
        .thenReturn(copiedConfig.getRoleMapper().map(mapper -> mapper.getRoleIds(identity)));

    AccessResult directResult =
        DefaultAccessController.checkBrowseAccess(context, List.of(nodeId)).get(nodeId);
    AccessResult copiedResult =
        DefaultAccessController.checkBrowseAccess(context, List.of(nodeId)).get(nodeId);

    assertEquals(AccessResult.DENIED_USER_ACCESS, directResult);
    assertEquals(directResult, copiedResult);
  }

  /**
   * A read check runs for every sampling cycle on the default policy, so a Value read asks the
   * AddressSpace only for the attributes it decides from, and still decides from them: here
   * AccessLevel allows the read and UserAccessLevel denies it.
   */
  @Test
  void checkReadAccess_Value_ReadsOnlyTheAttributesItDecidesFrom() {
    OpcUaServer server = Mockito.mock(OpcUaServer.class);
    AddressSpaceManager addressSpaceManager = Mockito.mock(AddressSpaceManager.class);
    Mockito.when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);

    Session session = Mockito.mock(Session.class);
    Mockito.when(session.getRoleIds()).thenReturn(Optional.empty());
    Mockito.when(session.getEndpoint())
        .thenReturn(
            new EndpointDescription(
                null, null, null, MessageSecurityMode.None, null, null, null, null));

    Map<AttributeId, Variant> attributeValues =
        Map.of(
            AttributeId.NodeClass, new Variant(NodeClass.Variable),
            AttributeId.AccessLevel, new Variant(AccessLevel.toValue(AccessLevel.READ_ONLY)),
            AttributeId.UserAccessLevel, new Variant(AccessLevel.toValue(AccessLevel.NONE)));

    var attributesRead = new ArrayList<AttributeId>();
    Mockito.when(
            addressSpaceManager.read(
                Mockito.any(),
                Mockito.anyDouble(),
                Mockito.eq(TimestampsToReturn.Neither),
                Mockito.anyList()))
        .thenAnswer(
            invocation -> {
              List<ReadValueId> readValueIds = invocation.getArgument(3);
              return readValueIds.stream()
                  .map(
                      readValueId -> {
                        AttributeId attributeId =
                            AttributeId.from(readValueId.getAttributeId()).orElseThrow();
                        attributesRead.add(attributeId);
                        return new DataValue(
                            attributeValues.getOrDefault(attributeId, Variant.NULL_VALUE));
                      })
                  .toList();
            });

    var readValueId = new ReadValueId(new NodeId(1, "foo"), AttributeId.Value.uid(), null, null);

    AccessResult result =
        new DefaultAccessController(server)
            .checkReadAccess(session, List.of(readValueId))
            .get(readValueId);

    assertEquals(AccessResult.DENIED_USER_ACCESS, result);
    assertEquals(
        List.of(
            AttributeId.NodeClass,
            AttributeId.AccessRestrictions,
            AttributeId.AccessLevel,
            AttributeId.UserAccessLevel),
        attributesRead);
  }

  private static AddNodesItem addNodesItem(ExpandedNodeId parentNodeId) {
    return new AddNodesItem(
        parentNodeId,
        NodeIds.HasComponent,
        ExpandedNodeId.NULL_VALUE,
        new QualifiedName(1, "child-of-" + parentNodeId.toParseableString()),
        NodeClass.Object,
        null,
        NodeIds.BaseObjectType.expanded());
  }

  private static RolePermissionType[] rolePermissions(PermissionType.Field field) {
    return new RolePermissionType[] {
      new RolePermissionType(ROLE_A, PermissionType.of(field)),
      new RolePermissionType(ROLE_B, PermissionType.of())
    };
  }
}
