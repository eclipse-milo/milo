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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
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
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
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
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateDataDetails;
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
    Mockito.when(context.readAccessControlAttributes(Mockito.anyMap())).thenReturn(attributesMap);
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

  // AddNodesItem.hashCode hashes the new Node's encoded attributes, initial value included, so
  // the results are keyed by identity, as checkAddNodesAccess documents: the item is never hashed
  // and an equal copy finds nothing.
  @Test
  void checkAddNodesAccess_ResultsAreKeyedByIdentityWithoutHashingTheItem() {
    var parentNodeId = new NodeId(1, "parent");
    var addNodesItem =
        new AddNodesItem(
            parentNodeId.expanded(),
            NodeIds.HasComponent,
            ExpandedNodeId.NULL_VALUE,
            new QualifiedName(1, "child"),
            NodeClass.Object,
            null,
            NodeIds.BaseObjectType.expanded()) {

          @Override
          public int hashCode() {
            throw new AssertionError("checkAddNodesAccess hashed the AddNodesItem");
          }
        };
    var copy =
        new AddNodesItem(
            parentNodeId.expanded(),
            NodeIds.HasComponent,
            ExpandedNodeId.NULL_VALUE,
            new QualifiedName(1, "child"),
            NodeClass.Object,
            null,
            NodeIds.BaseObjectType.expanded());

    attributesMap.put(
        parentNodeId, new AccessControlAttributes(null, null, null, null, null, null, null));

    Map<AddNodesItem, AccessResult> results =
        DefaultAccessController.checkAddNodesAccess(context, List.of(addNodesItem));

    assertEquals(AccessResult.ALLOWED, results.get(addNodesItem));
    assertNull(results.get(copy), "lookup with an equal but different AddNodesItem");
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

  // OPC 10000-3 §4.9.1: WellKnownRole_Anonymous is implicitly assigned to every Session, even when
  // the configured RoleMapper returns only non-anonymous roles. Permissions granted to Anonymous
  // must therefore be honored consistently with UserRolePermissions filters, while permissions
  // omitted from Anonymous remain denied.
  @Test
  void checkBrowseAndReadRolePermissions_HonorsImplicitAnonymousRole() {
    var nodeId = new NodeId(1, "enabledFlag");
    var anonymousBrowseRead =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_Anonymous,
              PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read))
        };

    attributesMap.put(
        nodeId,
        new AccessControlAttributes(null, null, null, null, null, null, anonymousBrowseRead));
    Mockito.when(context.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));

    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkBrowseAccess(context, List.of(nodeId)).get(nodeId),
        "Browse granted to Anonymous must be allowed for a mapped Session omitting Anonymous");

    var readRolePermissions =
        new ReadValueId(nodeId, AttributeId.RolePermissions.uid(), null, null);
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkReadAccess(context, List.of(readRolePermissions))
            .get(readRolePermissions),
        "Anonymous without ReadRolePermissions must still be denied RolePermissions read");
  }

  // Malformed UserRolePermissions entries (null entry, null roleId, null permissions, or null
  // mask value) must fail closed without throwing NullPointerException.
  @Test
  void checkBrowseAccess_MalformedUserRolePermissionsFailClosed() {
    var nodeId = new NodeId(1, "malformed");
    var malformed =
        new RolePermissionType[] {
          null,
          new RolePermissionType(ROLE_A, null),
          new RolePermissionType(null, PermissionType.of(PermissionType.Field.Browse)),
          new RolePermissionType(ROLE_A, new PermissionType(null))
        };

    attributesMap.put(
        nodeId, new AccessControlAttributes(null, null, null, null, null, null, malformed));
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkBrowseAccess(context, List.of(nodeId)).get(nodeId));
  }

  // Fallback UserRolePermissions protect Browse on restricted nodes that omit public
  // RolePermissions / UserRolePermissions, while leaving Read(RolePermissions) unblocked so
  // AttributeReader returns Bad_AttributeIdInvalid.
  @Test
  void checkBrowseAccess_ConsultsFallbackUserRolePermissionsWithoutBlockingRolePermissionsRead() {
    var nodeId = new NodeId(1, "restrictedDiagnostics");
    attributesMap.put(
        nodeId, new AccessControlAttributes(null, null, null, null, null, null, null));
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));
    Mockito.when(context.getFallbackUserRolePermissions(nodeId))
        .thenReturn(new RolePermissionType[0]);

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkBrowseAccess(context, List.of(nodeId)).get(nodeId),
        "fallback policy with no Browse entry denies Browse");

    var readRolePermissions =
        new ReadValueId(nodeId, AttributeId.RolePermissions.uid(), null, null);
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkReadAccess(context, List.of(readRolePermissions))
            .get(readRolePermissions),
        "Read(RolePermissions) must not use fallback so AttributeReader answers"
            + " Bad_AttributeIdInvalid");

    Mockito.when(context.getFallbackUserRolePermissions(nodeId))
        .thenReturn(
            new RolePermissionType[] {
              new RolePermissionType(ROLE_A, PermissionType.of(PermissionType.Field.Browse))
            });
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkBrowseAccess(context, List.of(nodeId)).get(nodeId),
        "fallback policy granting Browse allows Browse");
  }

  // OPC 10000-3 §4.9.1 & §5.2.9: when no RoleMapper is configured, nodes governed by a
  // FallbackUserRolePermissionsFilter still enforce ReadRolePermissions against the implicit
  // Anonymous role when public UserRolePermissions is non-null, while leaving static nodes without
  // a fallback filter and nodes that omit UserRolePermissions unaffected.
  @Test
  void checkReadAccess_RolePermissions_EnforcesWithoutRoleMapperWhenFallbackFilterPresent() {
    var nodeId = new NodeId(1, "enabledFlag");
    var readRolePermissions =
        new ReadValueId(nodeId, AttributeId.RolePermissions.uid(), null, null);
    Mockito.when(context.getRoleIds()).thenReturn(Optional.empty());

    // Static node without a fallback filter stays allowed when no RoleMapper is configured.
    attributesMap.put(
        nodeId,
        new AccessControlAttributes(null, null, null, null, null, null, new RolePermissionType[0]));
    Mockito.when(context.getFallbackUserRolePermissions(nodeId)).thenReturn(null);
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkReadAccess(context, List.of(readRolePermissions))
            .get(readRolePermissions));

    // With an active fallback filter and non-null public UserRolePermissions lacking
    // Anonymous.ReadRolePermissions, Read(RolePermissions) is denied.
    Mockito.when(context.getFallbackUserRolePermissions(nodeId))
        .thenReturn(new RolePermissionType[0]);
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkReadAccess(context, List.of(readRolePermissions))
            .get(readRolePermissions));

    var anonymousWithoutReadRolePermissions =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_Anonymous,
              PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read))
        };
    attributesMap.put(
        nodeId,
        new AccessControlAttributes(
            null, null, null, null, null, null, anonymousWithoutReadRolePermissions));
    Mockito.when(context.getFallbackUserRolePermissions(nodeId))
        .thenReturn(anonymousWithoutReadRolePermissions);
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkReadAccess(context, List.of(readRolePermissions))
            .get(readRolePermissions));

    // Granting ReadRolePermissions to Anonymous allows the read without a RoleMapper.
    var anonymousWithReadRolePermissions =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_Anonymous,
              PermissionType.of(PermissionType.Field.ReadRolePermissions))
        };
    attributesMap.put(
        nodeId,
        new AccessControlAttributes(
            null, null, null, null, null, null, anonymousWithReadRolePermissions));
    Mockito.when(context.getFallbackUserRolePermissions(nodeId))
        .thenReturn(anonymousWithReadRolePermissions);
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkReadAccess(context, List.of(readRolePermissions))
            .get(readRolePermissions));

    // When public UserRolePermissions is null, Read(RolePermissions) still reaches AttributeReader
    // to report Bad_AttributeIdInvalid.
    attributesMap.put(
        nodeId, new AccessControlAttributes(null, null, null, null, null, null, null));
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkReadAccess(context, List.of(readRolePermissions))
            .get(readRolePermissions));
  }

  // Write checks for RolePermissions and Historizing must consult internal fallback
  // UserRolePermissions when public UserRolePermissions is absent.
  @Test
  void checkWriteAccess_RolePermissionsAndHistorizing_ConsultFallbackUserRolePermissions() {
    var nodeId = new NodeId(1, "restrictedDiagnostics");
    var writeRolePermissions =
        new WriteValue(
            nodeId,
            AttributeId.RolePermissions.uid(),
            null,
            DataValue.valueOnly(Variant.NULL_VALUE));
    var writeHistorizing =
        new WriteValue(
            nodeId, AttributeId.Historizing.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));

    UInteger writableMask =
        UInteger.valueOf(WriteMask.RolePermissions.getValue() | WriteMask.Historizing.getValue());
    attributesMap.put(
        nodeId, new AccessControlAttributes(null, null, writableMask, null, null, null, null));
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    // Empty fallback denies both writes even when UserWriteMask allows the attributes.
    Mockito.when(context.getFallbackUserRolePermissions(nodeId))
        .thenReturn(new RolePermissionType[0]);
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkWriteAccess(context, List.of(writeRolePermissions))
            .get(writeRolePermissions));
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkWriteAccess(context, List.of(writeHistorizing))
            .get(writeHistorizing));

    // Fallback granting WriteRolePermissions allows RolePermissions write while still denying
    // Historizing write.
    Mockito.when(context.getFallbackUserRolePermissions(nodeId))
        .thenReturn(
            new RolePermissionType[] {
              new RolePermissionType(
                  ROLE_A, PermissionType.of(PermissionType.Field.WriteRolePermissions))
            });
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkWriteAccess(context, List.of(writeRolePermissions))
            .get(writeRolePermissions));
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkWriteAccess(context, List.of(writeHistorizing))
            .get(writeHistorizing));

    // Fallback granting WriteHistorizing allows Historizing write while still denying
    // RolePermissions write.
    Mockito.when(context.getFallbackUserRolePermissions(nodeId))
        .thenReturn(
            new RolePermissionType[] {
              new RolePermissionType(
                  ROLE_A, PermissionType.of(PermissionType.Field.WriteHistorizing))
            });
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkWriteAccess(context, List.of(writeRolePermissions))
            .get(writeRolePermissions));
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkWriteAccess(context, List.of(writeHistorizing))
            .get(writeHistorizing));
  }

  // OPC 10000-3 §4.9.1: without a RoleMapper (Optional.empty()), fallback-protected nodes must
  // still enforce WriteRolePermissions and WriteHistorizing against the implicit Anonymous role,
  // while ordinary nodes without a FallbackUserRolePermissionsFilter keep existing behavior.
  @Test
  void checkWriteAccess_RolePermissionsAndHistorizing_EnforceFallbackWithoutRoleMapper() {
    var nodeId = new NodeId(1, "restrictedDiagnostics");
    var writeRolePermissions =
        new WriteValue(
            nodeId,
            AttributeId.RolePermissions.uid(),
            null,
            DataValue.valueOnly(Variant.NULL_VALUE));
    var writeHistorizing =
        new WriteValue(
            nodeId, AttributeId.Historizing.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));

    UInteger writableMask =
        UInteger.valueOf(WriteMask.RolePermissions.getValue() | WriteMask.Historizing.getValue());
    Mockito.when(context.getRoleIds()).thenReturn(Optional.empty());

    // Ordinary node without a FallbackUserRolePermissionsFilter stays allowed without a RoleMapper.
    attributesMap.put(
        nodeId,
        new AccessControlAttributes(
            null, null, writableMask, null, null, null, new RolePermissionType[0]));
    Mockito.when(context.getFallbackUserRolePermissions(nodeId)).thenReturn(null);
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkWriteAccess(context, List.of(writeRolePermissions))
            .get(writeRolePermissions));
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkWriteAccess(context, List.of(writeHistorizing))
            .get(writeHistorizing));

    // Fallback-protected node with public UserRolePermissions omitted and Anonymous lacking write
    // permissions denies both writes without a RoleMapper.
    attributesMap.put(
        nodeId, new AccessControlAttributes(null, null, writableMask, null, null, null, null));
    Mockito.when(context.getFallbackUserRolePermissions(nodeId))
        .thenReturn(
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeIds.WellKnownRole_Anonymous,
                  PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read))
            });
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkWriteAccess(context, List.of(writeRolePermissions))
            .get(writeRolePermissions));
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkWriteAccess(context, List.of(writeHistorizing))
            .get(writeHistorizing));

    // Fallback granting WriteRolePermissions to Anonymous allows RolePermissions write only.
    Mockito.when(context.getFallbackUserRolePermissions(nodeId))
        .thenReturn(
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeIds.WellKnownRole_Anonymous,
                  PermissionType.of(PermissionType.Field.WriteRolePermissions))
            });
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkWriteAccess(context, List.of(writeRolePermissions))
            .get(writeRolePermissions));
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkWriteAccess(context, List.of(writeHistorizing))
            .get(writeHistorizing));

    // Fallback granting WriteHistorizing to Anonymous allows Historizing write only.
    Mockito.when(context.getFallbackUserRolePermissions(nodeId))
        .thenReturn(
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeIds.WellKnownRole_Anonymous,
                  PermissionType.of(PermissionType.Field.WriteHistorizing))
            });
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkWriteAccess(context, List.of(writeRolePermissions))
            .get(writeRolePermissions));
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkWriteAccess(context, List.of(writeHistorizing))
            .get(writeHistorizing));

    // Explicit public UserRolePermissions on a fallback-protected node govern over fallback.
    attributesMap.put(
        nodeId,
        new AccessControlAttributes(
            null,
            null,
            writableMask,
            null,
            null,
            null,
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeIds.WellKnownRole_Anonymous,
                  PermissionType.of(PermissionType.Field.WriteRolePermissions))
            }));
    Mockito.when(context.getFallbackUserRolePermissions(nodeId))
        .thenReturn(new RolePermissionType[0]);
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkWriteAccess(context, List.of(writeRolePermissions))
            .get(writeRolePermissions));
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkWriteAccess(context, List.of(writeHistorizing))
            .get(writeHistorizing));
  }

  // OPC 10000-3 §8.55: Call checks must evaluate Object and Method effective permissions
  // independently so a policy on only one side still denies Call when Call permission is missing.
  @Test
  void checkCallAccess_EvaluatesObjectAndMethodEffectivePermissionsIndependently() {
    var objectNodeId = new NodeId(1, "object");
    var methodNodeId = new NodeId(1, "method");
    var callMethodRequest = new CallMethodRequest(objectNodeId, methodNodeId, null);

    attributesMap.put(
        objectNodeId, new AccessControlAttributes(null, null, null, null, null, null, null));
    attributesMap.put(
        methodNodeId, new AccessControlAttributes(null, null, null, null, null, true, null));
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    // Object-only fallback policy denying Call must deny even when Method has no policy.
    Mockito.when(context.getFallbackUserRolePermissions(objectNodeId))
        .thenReturn(new RolePermissionType[0]);
    Mockito.when(context.getFallbackUserRolePermissions(methodNodeId)).thenReturn(null);
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
            .get(callMethodRequest));

    // Object-only fallback policy granting Call allows when Method has no policy.
    Mockito.when(context.getFallbackUserRolePermissions(objectNodeId))
        .thenReturn(
            new RolePermissionType[] {
              new RolePermissionType(ROLE_A, PermissionType.of(PermissionType.Field.Call))
            });
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
            .get(callMethodRequest));

    // Method-only fallback policy denying Call must deny even when Object has no policy.
    Mockito.when(context.getFallbackUserRolePermissions(objectNodeId)).thenReturn(null);
    Mockito.when(context.getFallbackUserRolePermissions(methodNodeId))
        .thenReturn(new RolePermissionType[0]);
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
            .get(callMethodRequest));

    // Method-only fallback policy granting Call allows when Object has no policy.
    Mockito.when(context.getFallbackUserRolePermissions(methodNodeId))
        .thenReturn(
            new RolePermissionType[] {
              new RolePermissionType(ROLE_A, PermissionType.of(PermissionType.Field.Call))
            });
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
            .get(callMethodRequest));

    // Object-only public UserRolePermissions denying Call also denies when Method has no policy.
    Mockito.when(context.getFallbackUserRolePermissions(methodNodeId)).thenReturn(null);
    attributesMap.put(
        objectNodeId,
        new AccessControlAttributes(
            null,
            null,
            null,
            null,
            null,
            null,
            new RolePermissionType[] {new RolePermissionType(ROLE_A, PermissionType.of())}));
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
            .get(callMethodRequest));
  }

  // Mixed Read batches request access-control attributes per NodeId rather than taking a union
  // across the whole batch, so reading RolePermissions on one node does not read
  // UserRolePermissions on unrelated nodes in the same batch.
  @Test
  void checkReadAccess_RequestsAccessAttributesPerNode() {
    var displayNodeId = new NodeId(1, "displayNode");
    var valueNodeId = new NodeId(1, "valueNode");
    var rolePermNodeId = new NodeId(1, "rolePermNode");

    var displayRead = new ReadValueId(displayNodeId, AttributeId.DisplayName.uid(), null, null);
    var browseNameRead = new ReadValueId(displayNodeId, AttributeId.BrowseName.uid(), null, null);
    var valueRead = new ReadValueId(valueNodeId, AttributeId.Value.uid(), null, null);
    var rolePermRead =
        new ReadValueId(rolePermNodeId, AttributeId.RolePermissions.uid(), null, null);

    attributesMap.put(
        displayNodeId, new AccessControlAttributes(null, null, null, null, null, null, null));
    attributesMap.put(
        valueNodeId,
        new AccessControlAttributes(
            null,
            null,
            null,
            AccessLevel.toValue(AccessLevel.READ_ONLY),
            AccessLevel.toValue(AccessLevel.READ_ONLY),
            null,
            null));
    attributesMap.put(
        rolePermNodeId,
        new AccessControlAttributes(
            null,
            null,
            null,
            null,
            null,
            null,
            new RolePermissionType[] {new RolePermissionType(ROLE_A, PermissionType.of())}));

    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    var captured = new LinkedHashMap<NodeId, Set<AttributeId>>();
    Mockito.when(context.readAccessControlAttributes(Mockito.anyMap()))
        .thenAnswer(
            invocation -> {
              Map<NodeId, Set<AttributeId>> requested = invocation.getArgument(0);
              captured.putAll(requested);
              return attributesMap;
            });

    Map<ReadValueId, AccessResult> results =
        DefaultAccessController.checkReadAccess(
            context, List.of(displayRead, valueRead, rolePermRead, browseNameRead));

    assertEquals(AccessResult.ALLOWED, results.get(displayRead));
    assertEquals(AccessResult.ALLOWED, results.get(browseNameRead));
    assertEquals(AccessResult.ALLOWED, results.get(valueRead));
    assertEquals(AccessResult.DENIED_USER_ACCESS, results.get(rolePermRead));

    assertEquals(
        Set.of(AttributeId.AccessRestrictions),
        captured.get(displayNodeId),
        "displayNode must not request UserRolePermissions or AccessLevel");
    assertEquals(
        Set.of(
            AttributeId.AccessRestrictions, AttributeId.AccessLevel, AttributeId.UserAccessLevel),
        captured.get(valueNodeId),
        "valueNode must not request UserRolePermissions");
    assertEquals(
        Set.of(
            AttributeId.AccessRestrictions, AttributeId.WriteMask, AttributeId.UserRolePermissions),
        captured.get(rolePermNodeId),
        "rolePermNode must request only AccessRestrictions, WriteMask, and UserRolePermissions");

    // Verify DefaultAccessControlContext translates per-node sets into minimal ReadValueIds.
    OpcUaServer mockServer = Mockito.mock(OpcUaServer.class);
    AddressSpaceManager mockAddressSpaceManager = Mockito.mock(AddressSpaceManager.class);
    Session mockSession = Mockito.mock(Session.class);
    Mockito.when(mockServer.getAddressSpaceManager()).thenReturn(mockAddressSpaceManager);

    var issuedReads = new ArrayList<ReadValueId>();
    Mockito.when(
            mockAddressSpaceManager.read(
                Mockito.any(), Mockito.anyDouble(), Mockito.any(), Mockito.anyList()))
        .thenAnswer(
            invocation -> {
              List<ReadValueId> reads = invocation.getArgument(3);
              issuedReads.addAll(reads);
              var values = new ArrayList<DataValue>(reads.size());
              for (ReadValueId r : reads) {
                if (AttributeId.NodeClass.uid().equals(r.getAttributeId())) {
                  values.add(DataValue.valueOnly(new Variant(NodeClass.Variable)));
                } else {
                  values.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
                }
              }
              return values;
            });

    var defaultContext =
        new DefaultAccessController.DefaultAccessControlContext(mockServer, mockSession);
    defaultContext.readAccessControlAttributes(captured);

    assertFalse(
        issuedReads.stream()
            .anyMatch(
                r ->
                    displayNodeId.equals(r.getNodeId())
                        && AttributeId.UserRolePermissions.uid().equals(r.getAttributeId())),
        "DefaultAccessControlContext must not read UserRolePermissions on displayNode");
    assertFalse(
        issuedReads.stream()
            .anyMatch(
                r ->
                    valueNodeId.equals(r.getNodeId())
                        && AttributeId.UserRolePermissions.uid().equals(r.getAttributeId())),
        "DefaultAccessControlContext must not read UserRolePermissions on valueNode");
    assertEquals(
        1,
        issuedReads.stream()
            .filter(
                r ->
                    rolePermNodeId.equals(r.getNodeId())
                        && AttributeId.UserRolePermissions.uid().equals(r.getAttributeId()))
            .count(),
        "DefaultAccessControlContext must read UserRolePermissions only on rolePermNode");
  }

  @Test
  void rolePermissionsReadScope_MemoizesPerSessionAndCleansUpOnClose() {
    Session sessionA = Mockito.mock(Session.class);
    Session sessionB = Mockito.mock(Session.class);
    UaNode node = Mockito.mock(UaNode.class);

    Mockito.when(sessionA.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));
    Mockito.when(sessionB.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_B)));

    assertNull(RolePermissionsReadScope.getRolePermissions(sessionA, node));

    var failure = new UaException(StatusCodes.Bad_InternalError);
    try (RolePermissionsReadScope outer = RolePermissionsReadScope.open(sessionA)) {
      assertEquals(Optional.of(List.of(ROLE_A)), RolePermissionsReadScope.getRoleIds(sessionA));
      assertEquals(Optional.of(List.of(ROLE_A)), RolePermissionsReadScope.getRoleIds(sessionA));
      Mockito.verify(sessionA, Mockito.times(1)).getRoleIds();

      // Another session is never served from sessionA's scope.
      assertEquals(Optional.of(List.of(ROLE_B)), RolePermissionsReadScope.getRoleIds(sessionB));
      assertNull(RolePermissionsReadScope.getRolePermissions(sessionB, node));

      // Explicit null value is distinct from cache miss.
      RolePermissionsReadScope.putRolePermissionsValue(sessionA, node, null);
      var cachedNull =
          assertInstanceOf(
              RolePermissionsReadScope.RolePermissionsResult.Value.class,
              RolePermissionsReadScope.getRolePermissions(sessionA, node));
      assertNull(cachedNull.value());

      // Nested scope isolates sessionB and restores sessionA on close.
      try (RolePermissionsReadScope inner = RolePermissionsReadScope.open(sessionB)) {
        assertNull(RolePermissionsReadScope.getRolePermissions(sessionB, node));
        RolePermissionsReadScope.putRolePermissionsFailure(sessionB, node, failure);
        var cachedFailure =
            assertInstanceOf(
                RolePermissionsReadScope.RolePermissionsResult.Failure.class,
                RolePermissionsReadScope.getRolePermissions(sessionB, node));
        assertSame(failure, cachedFailure.exception());
      }

      assertInstanceOf(
          RolePermissionsReadScope.RolePermissionsResult.Value.class,
          RolePermissionsReadScope.getRolePermissions(sessionA, node));
    }

    // Scope is removed from ThreadLocal after close.
    assertNull(RolePermissionsReadScope.getRolePermissions(sessionA, node));
    assertEquals(Optional.of(List.of(ROLE_A)), RolePermissionsReadScope.getRoleIds(sessionA));
    Mockito.verify(sessionA, Mockito.times(2)).getRoleIds();
  }

  // A failed UserRolePermissions lookup (any non-good status other than Bad_AttributeIdInvalid /
  // Bad_NodeIdUnknown) must fail closed on Read(RolePermissions), Browse, and
  // Write(RolePermissions)
  // rather than being treated as an omitted optional attribute, while Bad_AttributeIdInvalid stays
  // treated as omitted.
  @Test
  void failedUserRolePermissionsLookupFailsClosedWhileBadAttributeIdInvalidStaysOmitted() {
    var deniedNodeId = new NodeId(1, "urpDenied");
    var errorNodeId = new NodeId(1, "urpInternalError");
    var omittedNodeId = new NodeId(1, "urpOmitted");

    var deniedRead = new ReadValueId(deniedNodeId, AttributeId.RolePermissions.uid(), null, null);
    var errorRead = new ReadValueId(errorNodeId, AttributeId.RolePermissions.uid(), null, null);
    var omittedRead = new ReadValueId(omittedNodeId, AttributeId.RolePermissions.uid(), null, null);

    attributesMap.put(
        deniedNodeId,
        new AccessControlAttributes(
            NodeClass.Variable,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            new StatusCode(StatusCodes.Bad_UserAccessDenied),
            false));
    attributesMap.put(
        errorNodeId,
        new AccessControlAttributes(
            NodeClass.Variable,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            new StatusCode(StatusCodes.Bad_InternalError),
            false));
    attributesMap.put(
        omittedNodeId,
        new AccessControlAttributes(
            NodeClass.Variable,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
            false));

    // Mapped session: failed lookups deny, Bad_AttributeIdInvalid stays allowed to reach
    // AttributeReader.
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));
    Map<ReadValueId, AccessResult> mappedReadResults =
        DefaultAccessController.checkReadAccess(
            context, List.of(deniedRead, errorRead, omittedRead));
    assertEquals(AccessResult.DENIED_USER_ACCESS, mappedReadResults.get(deniedRead));
    assertEquals(
        new AccessResult.Denied(StatusCodes.Bad_InternalError), mappedReadResults.get(errorRead));
    assertEquals(AccessResult.ALLOWED, mappedReadResults.get(omittedRead));

    // Browse and Write(RolePermissions) also fail closed on bad UserRolePermissions lookup rather
    // than falling back to FallbackUserRolePermissionsFilter or allowing.
    Mockito.when(context.getFallbackUserRolePermissions(deniedNodeId))
        .thenReturn(
            new RolePermissionType[] {
              new RolePermissionType(
                  ROLE_A,
                  PermissionType.of(
                      PermissionType.Field.Browse, PermissionType.Field.WriteRolePermissions))
            });
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkBrowseAccess(context, List.of(deniedNodeId))
            .get(deniedNodeId));

    var writeRolePerms =
        new WriteValue(
            deniedNodeId,
            AttributeId.RolePermissions.uid(),
            null,
            DataValue.valueOnly(new Variant(new RolePermissionType[0])));
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkWriteAccess(context, List.of(writeRolePerms))
            .get(writeRolePerms));

    // Unmapped session (no RoleMapper): failed UserRolePermissions lookup still fails closed on
    // Read(RolePermissions) and Write(RolePermissions), while Bad_AttributeIdInvalid stays allowed.
    Mockito.when(context.getRoleIds()).thenReturn(Optional.empty());
    Map<ReadValueId, AccessResult> unmappedReadResults =
        DefaultAccessController.checkReadAccess(
            context, List.of(deniedRead, errorRead, omittedRead));
    assertEquals(AccessResult.DENIED_USER_ACCESS, unmappedReadResults.get(deniedRead));
    assertEquals(
        new AccessResult.Denied(StatusCodes.Bad_InternalError), unmappedReadResults.get(errorRead));
    assertEquals(AccessResult.ALLOWED, unmappedReadResults.get(omittedRead));
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        DefaultAccessController.checkWriteAccess(context, List.of(writeRolePerms))
            .get(writeRolePerms));

    // DefaultAccessControlContext preserves UserRolePermissions status from AddressSpaceManager.
    OpcUaServer mockServer = Mockito.mock(OpcUaServer.class);
    AddressSpaceManager mockAddressSpaceManager = Mockito.mock(AddressSpaceManager.class);
    Session mockSession = Mockito.mock(Session.class);
    Mockito.when(mockServer.getAddressSpaceManager()).thenReturn(mockAddressSpaceManager);
    Mockito.when(
            mockAddressSpaceManager.read(
                Mockito.any(), Mockito.anyDouble(), Mockito.any(), Mockito.anyList()))
        .thenAnswer(
            invocation -> {
              List<ReadValueId> reads = invocation.getArgument(3);
              var values = new ArrayList<DataValue>(reads.size());
              for (ReadValueId r : reads) {
                if (AttributeId.NodeClass.uid().equals(r.getAttributeId())) {
                  values.add(DataValue.valueOnly(new Variant(NodeClass.Variable)));
                } else if (AttributeId.UserRolePermissions.uid().equals(r.getAttributeId())) {
                  if (deniedNodeId.equals(r.getNodeId())) {
                    values.add(new DataValue(StatusCodes.Bad_UserAccessDenied));
                  } else if (errorNodeId.equals(r.getNodeId())) {
                    values.add(new DataValue(StatusCodes.Bad_InternalError));
                  } else {
                    values.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
                  }
                } else {
                  values.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
                }
              }
              return values;
            });

    var defaultContext =
        new DefaultAccessController.DefaultAccessControlContext(mockServer, mockSession);
    Map<NodeId, AccessControlAttributes> readAttrs =
        defaultContext.readAccessControlAttributes(
            List.of(deniedNodeId, errorNodeId, omittedNodeId),
            Set.of(AttributeId.AccessRestrictions, AttributeId.UserRolePermissions));
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttrs.get(deniedNodeId).userRolePermissionsStatus());
    assertNull(readAttrs.get(deniedNodeId).userRolePermissions());
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        readAttrs.get(errorNodeId).userRolePermissionsStatus());
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        readAttrs.get(omittedNodeId).userRolePermissionsStatus());
  }

  // When NodeClass succeeds (nodeUnknown == false) and only UserRolePermissions answers
  // Bad_NodeIdUnknown, the lookup must fail closed rather than looking like an omitted attribute,
  // while a genuinely unknown node (nodeUnknown == true) keeps existing unknown-node behavior.
  @Test
  void userRolePermissionsBadNodeIdUnknownOnKnownNodeFailsClosed() {
    var knownNodeId = new NodeId(1, "knownNodeBadUrp");
    var unknownNodeId = new NodeId(1, "trulyUnknownNode");
    var okNodeId = new NodeId(1, "okMethodOrObject");

    UByte fullAccess =
        AccessLevel.toValue(
            Set.of(
                AccessLevel.CurrentRead,
                AccessLevel.CurrentWrite,
                AccessLevel.HistoryRead,
                AccessLevel.HistoryWrite));

    attributesMap.put(
        knownNodeId,
        new AccessControlAttributes(
            NodeClass.Variable,
            null,
            null,
            fullAccess,
            fullAccess,
            null,
            true,
            null,
            new StatusCode(StatusCodes.Bad_NodeIdUnknown),
            false));
    attributesMap.put(
        unknownNodeId,
        new AccessControlAttributes(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            new StatusCode(StatusCodes.Bad_NodeIdUnknown),
            true));
    attributesMap.put(
        okNodeId,
        new AccessControlAttributes(
            NodeClass.Object,
            null,
            null,
            null,
            null,
            null,
            true,
            null,
            new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
            false));

    var expectedDenied = new AccessResult.Denied(StatusCodes.Bad_NodeIdUnknown);

    for (Optional<List<NodeId>> roles :
        List.of(Optional.of(List.of(ROLE_A)), Optional.<List<NodeId>>empty())) {
      Mockito.when(context.getRoleIds()).thenReturn(roles);

      var knownRead = new ReadValueId(knownNodeId, AttributeId.RolePermissions.uid(), null, null);
      var unknownRead =
          new ReadValueId(unknownNodeId, AttributeId.RolePermissions.uid(), null, null);
      Map<ReadValueId, AccessResult> readResults =
          DefaultAccessController.checkReadAccess(context, List.of(knownRead, unknownRead));
      assertEquals(expectedDenied, readResults.get(knownRead));
      assertEquals(AccessResult.NODE_UNKNOWN, readResults.get(unknownRead));

      var knownWrite =
          new WriteValue(
              knownNodeId,
              AttributeId.RolePermissions.uid(),
              null,
              DataValue.valueOnly(new Variant(new RolePermissionType[0])));
      var unknownWrite =
          new WriteValue(
              unknownNodeId,
              AttributeId.RolePermissions.uid(),
              null,
              DataValue.valueOnly(new Variant(new RolePermissionType[0])));
      Map<WriteValue, AccessResult> writeResults =
          DefaultAccessController.checkWriteAccess(context, List.of(knownWrite, unknownWrite));
      assertEquals(expectedDenied, writeResults.get(knownWrite));
      assertEquals(AccessResult.ALLOWED, writeResults.get(unknownWrite));

      Map<NodeId, AccessResult> browseResults =
          DefaultAccessController.checkBrowseAccess(context, List.of(knownNodeId, unknownNodeId));
      assertEquals(expectedDenied, browseResults.get(knownNodeId));
      assertEquals(AccessResult.ALLOWED, browseResults.get(unknownNodeId));

      var knownHistoryRead = new HistoryReadValueId(knownNodeId, null, null, null);
      var unknownHistoryRead = new HistoryReadValueId(unknownNodeId, null, null, null);
      Map<HistoryReadValueId, AccessResult> historyReadResults =
          DefaultAccessController.checkHistoryReadAccess(
              context, List.of(knownHistoryRead, unknownHistoryRead));
      assertEquals(expectedDenied, historyReadResults.get(knownHistoryRead));
      assertEquals(AccessResult.NODE_UNKNOWN, historyReadResults.get(unknownHistoryRead));

      var knownHistoryUpdate = new UpdateDataDetails(knownNodeId, PerformUpdateType.Insert, null);
      var unknownHistoryUpdate =
          new UpdateDataDetails(unknownNodeId, PerformUpdateType.Insert, null);
      assertEquals(
          expectedDenied,
          DefaultAccessController.checkHistoryUpdateAccess(
                  context, List.of(knownHistoryUpdate, unknownHistoryUpdate))
              .get(knownHistoryUpdate));
      assertEquals(
          AccessResult.NODE_UNKNOWN,
          DefaultAccessController.checkHistoryUpdateAccess(
                  context, List.of(knownHistoryUpdate, unknownHistoryUpdate))
              .get(unknownHistoryUpdate));

      var knownCall = new CallMethodRequest(knownNodeId, okNodeId, null);
      var unknownCall = new CallMethodRequest(unknownNodeId, okNodeId, null);
      Map<CallMethodRequest, AccessResult> callResults =
          DefaultAccessController.checkCallAccess(context, List.of(knownCall, unknownCall));
      assertEquals(expectedDenied, callResults.get(knownCall));
      assertEquals(AccessResult.ALLOWED, callResults.get(unknownCall));
    }
  }

  // Without a RoleMapper (roleIds == null), explicit UserRolePermissions lookup failures
  // (Bad_UserAccessDenied / Bad_InternalError) must still fail closed across Browse, HistoryRead,
  // HistoryUpdate, Call (both Object and Method sides), AddNodes, AddReferences, DeleteNodes, and
  // DeleteReferences, while Bad_AttributeIdInvalid remains allowed when other checks pass.
  @Test
  void unmappedSessionFailsClosedOnUserRolePermissionsLookupFailureAcrossAllOperations() {
    Mockito.when(context.getRoleIds()).thenReturn(Optional.empty());

    var deniedNodeId = new NodeId(1, "unmappedDenied");
    var errorNodeId = new NodeId(1, "unmappedError");
    var omittedNodeId = new NodeId(1, "unmappedOmitted");

    UByte fullAccess =
        AccessLevel.toValue(
            Set.of(
                AccessLevel.CurrentRead,
                AccessLevel.CurrentWrite,
                AccessLevel.HistoryRead,
                AccessLevel.HistoryWrite));

    attributesMap.put(
        deniedNodeId,
        new AccessControlAttributes(
            NodeClass.Variable,
            null,
            null,
            fullAccess,
            fullAccess,
            null,
            true,
            null,
            new StatusCode(StatusCodes.Bad_UserAccessDenied),
            false));
    attributesMap.put(
        errorNodeId,
        new AccessControlAttributes(
            NodeClass.Variable,
            null,
            null,
            fullAccess,
            fullAccess,
            null,
            true,
            null,
            new StatusCode(StatusCodes.Bad_InternalError),
            false));
    attributesMap.put(
        omittedNodeId,
        new AccessControlAttributes(
            NodeClass.Variable,
            null,
            null,
            fullAccess,
            fullAccess,
            null,
            true,
            null,
            new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
            false));

    var internalError = new AccessResult.Denied(StatusCodes.Bad_InternalError);

    // Browse
    Map<NodeId, AccessResult> browseResults =
        DefaultAccessController.checkBrowseAccess(
            context, List.of(deniedNodeId, errorNodeId, omittedNodeId));
    assertEquals(AccessResult.DENIED_USER_ACCESS, browseResults.get(deniedNodeId));
    assertEquals(internalError, browseResults.get(errorNodeId));
    assertEquals(AccessResult.ALLOWED, browseResults.get(omittedNodeId));

    // HistoryRead
    var hrDenied = new HistoryReadValueId(deniedNodeId, null, null, null);
    var hrError = new HistoryReadValueId(errorNodeId, null, null, null);
    var hrOmitted = new HistoryReadValueId(omittedNodeId, null, null, null);
    Map<HistoryReadValueId, AccessResult> hrResults =
        DefaultAccessController.checkHistoryReadAccess(
            context, List.of(hrDenied, hrError, hrOmitted));
    assertEquals(AccessResult.DENIED_USER_ACCESS, hrResults.get(hrDenied));
    assertEquals(internalError, hrResults.get(hrError));
    assertEquals(AccessResult.ALLOWED, hrResults.get(hrOmitted));

    // HistoryUpdate
    var huDenied = new UpdateDataDetails(deniedNodeId, PerformUpdateType.Insert, null);
    var huError = new UpdateDataDetails(errorNodeId, PerformUpdateType.Insert, null);
    var huOmitted = new UpdateDataDetails(omittedNodeId, PerformUpdateType.Insert, null);
    var huResults =
        DefaultAccessController.checkHistoryUpdateAccess(
            context, List.of(huDenied, huError, huOmitted));
    assertEquals(AccessResult.DENIED_USER_ACCESS, huResults.get(huDenied));
    assertEquals(internalError, huResults.get(huError));
    assertEquals(AccessResult.ALLOWED, huResults.get(huOmitted));

    // Call (object side failure, method side failure, and omitted control)
    var callObjDenied = new CallMethodRequest(deniedNodeId, omittedNodeId, null);
    var callMethodError = new CallMethodRequest(omittedNodeId, errorNodeId, null);
    var callOmitted = new CallMethodRequest(omittedNodeId, omittedNodeId, null);
    Map<CallMethodRequest, AccessResult> callResults =
        DefaultAccessController.checkCallAccess(
            context, List.of(callObjDenied, callMethodError, callOmitted));
    assertEquals(AccessResult.DENIED_USER_ACCESS, callResults.get(callObjDenied));
    assertEquals(internalError, callResults.get(callMethodError));
    assertEquals(AccessResult.ALLOWED, callResults.get(callOmitted));

    // AddNodes
    AddNodesItem addNodeDenied = addNodesItem(deniedNodeId.expanded());
    AddNodesItem addNodeError = addNodesItem(errorNodeId.expanded());
    AddNodesItem addNodeOmitted = addNodesItem(omittedNodeId.expanded());
    Map<AddNodesItem, AccessResult> addNodeResults =
        DefaultAccessController.checkAddNodesAccess(
            context, List.of(addNodeDenied, addNodeError, addNodeOmitted));
    assertEquals(AccessResult.DENIED_USER_ACCESS, addNodeResults.get(addNodeDenied));
    assertEquals(internalError, addNodeResults.get(addNodeError));
    assertEquals(AccessResult.ALLOWED, addNodeResults.get(addNodeOmitted));

    // AddReferences
    var addRefDenied =
        new AddReferencesItem(
            deniedNodeId,
            NodeIds.HasComponent,
            true,
            null,
            omittedNodeId.expanded(),
            NodeClass.Object);
    var addRefError =
        new AddReferencesItem(
            errorNodeId,
            NodeIds.HasComponent,
            true,
            null,
            omittedNodeId.expanded(),
            NodeClass.Object);
    var addRefOmitted =
        new AddReferencesItem(
            omittedNodeId,
            NodeIds.HasComponent,
            true,
            null,
            omittedNodeId.expanded(),
            NodeClass.Object);
    Map<AddReferencesItem, AccessResult> addRefResults =
        DefaultAccessController.checkAddReferencesAccess(
            context, List.of(addRefDenied, addRefError, addRefOmitted));
    assertEquals(AccessResult.DENIED_USER_ACCESS, addRefResults.get(addRefDenied));
    assertEquals(internalError, addRefResults.get(addRefError));
    assertEquals(AccessResult.ALLOWED, addRefResults.get(addRefOmitted));

    // DeleteNodes
    var delNodeDenied = new DeleteNodesItem(deniedNodeId, true);
    var delNodeError = new DeleteNodesItem(errorNodeId, true);
    var delNodeOmitted = new DeleteNodesItem(omittedNodeId, true);
    Map<DeleteNodesItem, AccessResult> delNodeResults =
        DefaultAccessController.checkDeleteNodesAccess(
            context, List.of(delNodeDenied, delNodeError, delNodeOmitted));
    assertEquals(AccessResult.DENIED_USER_ACCESS, delNodeResults.get(delNodeDenied));
    assertEquals(internalError, delNodeResults.get(delNodeError));
    assertEquals(AccessResult.ALLOWED, delNodeResults.get(delNodeOmitted));

    // DeleteReferences
    var delRefDenied =
        new DeleteReferencesItem(
            deniedNodeId, NodeIds.HasComponent, true, omittedNodeId.expanded(), true);
    var delRefError =
        new DeleteReferencesItem(
            errorNodeId, NodeIds.HasComponent, true, omittedNodeId.expanded(), true);
    var delRefOmitted =
        new DeleteReferencesItem(
            omittedNodeId, NodeIds.HasComponent, true, omittedNodeId.expanded(), true);
    Map<DeleteReferencesItem, AccessResult> delRefResults =
        DefaultAccessController.checkDeleteReferencesAccess(
            context, List.of(delRefDenied, delRefError, delRefOmitted));
    assertEquals(AccessResult.DENIED_USER_ACCESS, delRefResults.get(delRefDenied));
    assertEquals(internalError, delRefResults.get(delRefError));
    assertEquals(AccessResult.ALLOWED, delRefResults.get(delRefOmitted));
  }

  // A Good UserRolePermissions DataValue carrying a non-null non-RolePermissionType[] payload must
  // record a failure status (Bad_TypeMismatch) so both unmapped and mapped sessions fail closed on
  // Read(RolePermissions), Write(RolePermissions), Write(Historizing), and Browse.
  @Test
  void malformedGoodUserRolePermissionsPayloadFailsClosedForUnmappedAndMappedSessions() {
    var malformedNodeId = new NodeId(1, "malformedGoodUrp");

    OpcUaServer mockServer = Mockito.mock(OpcUaServer.class);
    AddressSpaceManager mockAddressSpaceManager = Mockito.mock(AddressSpaceManager.class);
    Session mockSession = Mockito.mock(Session.class);
    EndpointDescription endpoint = Mockito.mock(EndpointDescription.class);
    Mockito.when(mockServer.getAddressSpaceManager()).thenReturn(mockAddressSpaceManager);
    Mockito.when(mockServer.getNamespaceTable()).thenReturn(new NamespaceTable());
    Mockito.when(mockSession.getEndpoint()).thenReturn(endpoint);
    Mockito.when(endpoint.getSecurityMode()).thenReturn(MessageSecurityMode.SignAndEncrypt);
    Mockito.when(
            mockAddressSpaceManager.read(
                Mockito.any(), Mockito.anyDouble(), Mockito.any(), Mockito.anyList()))
        .thenAnswer(
            invocation -> {
              List<ReadValueId> reads = invocation.getArgument(3);
              var values = new ArrayList<DataValue>(reads.size());
              for (ReadValueId r : reads) {
                if (AttributeId.NodeClass.uid().equals(r.getAttributeId())) {
                  values.add(DataValue.valueOnly(new Variant(NodeClass.Variable)));
                } else if (AttributeId.UserRolePermissions.uid().equals(r.getAttributeId())) {
                  values.add(DataValue.valueOnly(Variant.ofString("not-permissions")));
                } else {
                  values.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
                }
              }
              return values;
            });

    var expectedDenied = new AccessResult.Denied(StatusCodes.Bad_TypeMismatch);
    var defaultContext =
        new DefaultAccessController.DefaultAccessControlContext(mockServer, mockSession);
    Map<NodeId, AccessControlAttributes> parsed =
        defaultContext.readAccessControlAttributes(
            List.of(malformedNodeId),
            Set.of(AttributeId.AccessRestrictions, AttributeId.UserRolePermissions));
    assertNull(parsed.get(malformedNodeId).userRolePermissions());
    assertEquals(
        new StatusCode(StatusCodes.Bad_TypeMismatch),
        parsed.get(malformedNodeId).userRolePermissionsStatus());

    var controller = new DefaultAccessController(mockServer);
    var readRolePerms =
        new ReadValueId(malformedNodeId, AttributeId.RolePermissions.uid(), null, null);
    var writeRolePerms =
        new WriteValue(
            malformedNodeId,
            AttributeId.RolePermissions.uid(),
            null,
            DataValue.valueOnly(new Variant(new RolePermissionType[0])));
    var writeHistorizing =
        new WriteValue(
            malformedNodeId,
            AttributeId.Historizing.uid(),
            null,
            DataValue.valueOnly(new Variant(Boolean.TRUE)));

    for (Optional<List<NodeId>> roles :
        List.of(Optional.<List<NodeId>>empty(), Optional.of(List.of(ROLE_A)))) {
      Mockito.when(mockSession.getRoleIds()).thenReturn(roles);

      assertEquals(
          expectedDenied,
          controller.checkReadAccess(mockSession, List.of(readRolePerms)).get(readRolePerms));
      assertEquals(
          expectedDenied,
          controller.checkWriteAccess(mockSession, List.of(writeRolePerms)).get(writeRolePerms));
      assertEquals(
          expectedDenied,
          controller
              .checkWriteAccess(mockSession, List.of(writeHistorizing))
              .get(writeHistorizing));
      assertEquals(
          expectedDenied,
          controller.checkBrowseAccess(mockSession, List.of(malformedNodeId)).get(malformedNodeId));
    }
  }

  // Forwarding AddressSpaces can return Good UserRolePermissions encoded as ExtensionObject[].
  // Valid encoded RolePermissionType[] entries must decode and enforce permissions normally, while
  // null, undecodable, or wrong-type ExtensionObject[] elements must fail closed with
  // Bad_TypeMismatch.
  @Test
  void encodedExtensionObjectUserRolePermissionsAreDecodedAndMalformedFailClosed() {
    var forwardedNodeId = new NodeId(1, "forwardedUrp");
    var roleAGrant =
        new RolePermissionType(
            ROLE_A,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));

    OpcUaServer mockServer = Mockito.mock(OpcUaServer.class);
    AddressSpaceManager mockAddressSpaceManager = Mockito.mock(AddressSpaceManager.class);
    Session mockSession = Mockito.mock(Session.class);
    EndpointDescription endpoint = Mockito.mock(EndpointDescription.class);
    Mockito.when(mockServer.getAddressSpaceManager()).thenReturn(mockAddressSpaceManager);
    Mockito.when(mockServer.getNamespaceTable()).thenReturn(new NamespaceTable());
    Mockito.when(mockServer.getStaticEncodingContext()).thenReturn(DefaultEncodingContext.INSTANCE);
    Mockito.when(mockSession.getEndpoint()).thenReturn(endpoint);
    Mockito.when(endpoint.getSecurityMode()).thenReturn(MessageSecurityMode.SignAndEncrypt);

    var urpDataValue =
        new AtomicReference<>(
            DataValue.valueOnly(
                new Variant(
                    ExtensionObject.encodeArray(
                        DefaultEncodingContext.INSTANCE, new RolePermissionType[] {roleAGrant}))));

    Mockito.when(
            mockAddressSpaceManager.read(
                Mockito.any(), Mockito.anyDouble(), Mockito.any(), Mockito.anyList()))
        .thenAnswer(
            invocation -> {
              List<ReadValueId> reads = invocation.getArgument(3);
              var values = new ArrayList<DataValue>(reads.size());
              for (ReadValueId r : reads) {
                if (AttributeId.NodeClass.uid().equals(r.getAttributeId())) {
                  values.add(DataValue.valueOnly(new Variant(NodeClass.Variable)));
                } else if (AttributeId.UserRolePermissions.uid().equals(r.getAttributeId())) {
                  values.add(urpDataValue.get());
                } else {
                  values.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
                }
              }
              return values;
            });

    var controller = new DefaultAccessController(mockServer);
    var readRolePerms =
        new ReadValueId(forwardedNodeId, AttributeId.RolePermissions.uid(), null, null);

    // 1. Valid encoded ExtensionObject[] decodes to RolePermissionType[] with StatusCode.GOOD.
    var defaultContext =
        new DefaultAccessController.DefaultAccessControlContext(mockServer, mockSession);
    AccessControlAttributes decodedAttrs =
        defaultContext
            .readAccessControlAttributes(
                List.of(forwardedNodeId),
                Set.of(AttributeId.AccessRestrictions, AttributeId.UserRolePermissions))
            .get(forwardedNodeId);
    assertEquals(StatusCode.GOOD, decodedAttrs.userRolePermissionsStatus());
    assertArrayEquals(new RolePermissionType[] {roleAGrant}, decodedAttrs.userRolePermissions());

    Mockito.when(mockSession.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));
    assertEquals(
        AccessResult.ALLOWED,
        controller.checkReadAccess(mockSession, List.of(readRolePerms)).get(readRolePerms));
    assertEquals(
        AccessResult.ALLOWED,
        controller.checkBrowseAccess(mockSession, List.of(forwardedNodeId)).get(forwardedNodeId));

    Mockito.when(mockSession.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_B)));
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        controller.checkReadAccess(mockSession, List.of(readRolePerms)).get(readRolePerms));
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        controller.checkBrowseAccess(mockSession, List.of(forwardedNodeId)).get(forwardedNodeId));

    // 2. Empty ExtensionObject[0] decodes to empty RolePermissionType[0] (DENIED_USER_ACCESS, not
    // Bad_TypeMismatch).
    urpDataValue.set(DataValue.valueOnly(new Variant(new ExtensionObject[0])));
    Mockito.when(mockSession.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        controller.checkReadAccess(mockSession, List.of(readRolePerms)).get(readRolePerms));

    // 3. Wrong-type, undecodable, and null ExtensionObject[] elements fail closed with
    // Bad_TypeMismatch for both mapped and unmapped sessions.
    ExtensionObject wrongType =
        ExtensionObject.encode(DefaultEncodingContext.INSTANCE, readRolePerms);
    ExtensionObject corruptBinary =
        ExtensionObject.of(
            ByteString.of(new byte[] {1, 2, 3}), NodeIds.RolePermissionType_Encoding_DefaultBinary);

    var expectedTypeMismatch = new AccessResult.Denied(StatusCodes.Bad_TypeMismatch);
    for (ExtensionObject[] badPayload :
        List.of(
            new ExtensionObject[] {wrongType},
            new ExtensionObject[] {corruptBinary},
            new ExtensionObject[] {null})) {
      urpDataValue.set(DataValue.valueOnly(new Variant(badPayload)));

      for (Optional<List<NodeId>> roles :
          List.of(Optional.of(List.of(ROLE_A)), Optional.<List<NodeId>>empty())) {
        Mockito.when(mockSession.getRoleIds()).thenReturn(roles);
        assertEquals(
            expectedTypeMismatch,
            controller.checkReadAccess(mockSession, List.of(readRolePerms)).get(readRolePerms));
        assertEquals(
            expectedTypeMismatch,
            controller
                .checkBrowseAccess(mockSession, List.of(forwardedNodeId))
                .get(forwardedNodeId));
      }
    }
  }

  // OPC 10000-3 §5.2.9: when WriteMask.RolePermissions is set on a fallback-governed node whose
  // public UserRolePermissions is omitted (no override configured), Read(RolePermissions) exposes
  // an empty array and therefore must enforce ReadRolePermissions against the fallback policy for
  // both mapped and unmapped sessions, while non-writable nodes stay allowed to reach
  // AttributeReader for Bad_AttributeIdInvalid.
  @Test
  void checkReadAccess_WritableRolePermissionsWithOmittedUserRolePermissions_EnforcesFallback() {
    var writableNodeId = new NodeId(1, "writableFallback");
    var readOnlyNodeId = new NodeId(1, "readOnlyFallback");
    var writableRead =
        new ReadValueId(writableNodeId, AttributeId.RolePermissions.uid(), null, null);
    var readOnlyRead =
        new ReadValueId(readOnlyNodeId, AttributeId.RolePermissions.uid(), null, null);

    var writableMask = UInteger.valueOf(WriteMask.RolePermissions.getValue());
    attributesMap.put(
        writableNodeId,
        new AccessControlAttributes(
            NodeClass.Variable,
            null,
            writableMask,
            writableMask,
            null,
            null,
            null,
            null,
            null,
            new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
            false));
    attributesMap.put(
        readOnlyNodeId,
        new AccessControlAttributes(
            NodeClass.Variable,
            null,
            uint(0),
            uint(0),
            null,
            null,
            null,
            null,
            null,
            new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
            false));

    var adminGrant =
        new RolePermissionType[] {
          new RolePermissionType(
              ROLE_A,
              PermissionType.of(
                  PermissionType.Field.Browse,
                  PermissionType.Field.Read,
                  PermissionType.Field.ReadRolePermissions))
        };

    // Mapped unauthorized session (fallback returns empty array): writable node denied, read-only
    // node allowed to reach AttributeReader -> Bad_AttributeIdInvalid.
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_B)));
    Mockito.when(context.getFallbackUserRolePermissions(writableNodeId))
        .thenReturn(new RolePermissionType[0]);
    Mockito.when(context.getFallbackUserRolePermissions(readOnlyNodeId))
        .thenReturn(new RolePermissionType[0]);

    Map<ReadValueId, AccessResult> unauthorizedMapped =
        DefaultAccessController.checkReadAccess(context, List.of(writableRead, readOnlyRead));
    assertEquals(AccessResult.DENIED_USER_ACCESS, unauthorizedMapped.get(writableRead));
    assertEquals(AccessResult.ALLOWED, unauthorizedMapped.get(readOnlyRead));

    // Unmapped session (no RoleMapper) lacking Anonymous.ReadRolePermissions is also denied on
    // writable fallback node, while read-only fallback node stays allowed to reach AttributeReader.
    Mockito.when(context.getRoleIds()).thenReturn(Optional.empty());
    Map<ReadValueId, AccessResult> unauthorizedUnmapped =
        DefaultAccessController.checkReadAccess(context, List.of(writableRead, readOnlyRead));
    assertEquals(AccessResult.DENIED_USER_ACCESS, unauthorizedUnmapped.get(writableRead));
    assertEquals(AccessResult.ALLOWED, unauthorizedUnmapped.get(readOnlyRead));

    // Mapped authorized session (fallback grants ReadRolePermissions) is allowed.
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));
    Mockito.when(context.getFallbackUserRolePermissions(writableNodeId)).thenReturn(adminGrant);
    assertEquals(
        AccessResult.ALLOWED,
        DefaultAccessController.checkReadAccess(context, List.of(writableRead)).get(writableRead));
  }

  // If a session-scoped WriteMask lookup fails while UserRolePermissions is omitted,
  // checkReadAccess(RolePermissions) cannot safely assume the node is non-writable and must fail
  // closed instead of returning ALLOWED, whereas Bad_AttributeIdInvalid (optional WriteMask
  // omitted) stays ALLOWED.
  @Test
  void checkReadAccess_FailedWriteMaskLookupWithOmittedUserRolePermissions_FailsClosed() {
    var nodeId = new NodeId(1, "writeMaskFailure");
    var readRolePerms = new ReadValueId(nodeId, AttributeId.RolePermissions.uid(), null, null);

    OpcUaServer mockServer = Mockito.mock(OpcUaServer.class);
    AddressSpaceManager mockAddressSpaceManager = Mockito.mock(AddressSpaceManager.class);
    Session mockSession = Mockito.mock(Session.class);
    EndpointDescription endpoint = Mockito.mock(EndpointDescription.class);
    Mockito.when(mockServer.getAddressSpaceManager()).thenReturn(mockAddressSpaceManager);
    Mockito.when(mockServer.getNamespaceTable()).thenReturn(new NamespaceTable());
    Mockito.when(mockSession.getEndpoint()).thenReturn(endpoint);
    Mockito.when(endpoint.getSecurityMode()).thenReturn(MessageSecurityMode.SignAndEncrypt);

    var writeMaskDataValue = new AtomicReference<>(new DataValue(StatusCodes.Bad_UserAccessDenied));
    Mockito.when(
            mockAddressSpaceManager.read(
                Mockito.any(), Mockito.anyDouble(), Mockito.any(), Mockito.anyList()))
        .thenAnswer(
            invocation -> {
              List<ReadValueId> reads = invocation.getArgument(3);
              var values = new ArrayList<DataValue>(reads.size());
              for (ReadValueId r : reads) {
                if (AttributeId.NodeClass.uid().equals(r.getAttributeId())) {
                  values.add(DataValue.valueOnly(new Variant(NodeClass.Variable)));
                } else if (AttributeId.WriteMask.uid().equals(r.getAttributeId())) {
                  values.add(writeMaskDataValue.get());
                } else {
                  values.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
                }
              }
              return values;
            });

    var controller = new DefaultAccessController(mockServer);

    for (Optional<List<NodeId>> roles :
        List.of(Optional.<List<NodeId>>empty(), Optional.of(List.of(ROLE_A)))) {
      Mockito.when(mockSession.getRoleIds()).thenReturn(roles);

      // 1. Bad_UserAccessDenied on WriteMask fails closed as DENIED_USER_ACCESS.
      writeMaskDataValue.set(new DataValue(StatusCodes.Bad_UserAccessDenied));
      assertEquals(
          AccessResult.DENIED_USER_ACCESS,
          controller.checkReadAccess(mockSession, List.of(readRolePerms)).get(readRolePerms));

      // 2. Bad_InternalError on WriteMask propagates Bad_InternalError.
      writeMaskDataValue.set(new DataValue(StatusCodes.Bad_InternalError));
      assertEquals(
          new AccessResult.Denied(StatusCodes.Bad_InternalError),
          controller.checkReadAccess(mockSession, List.of(readRolePerms)).get(readRolePerms));

      // 3. Attribute-level Bad_NodeIdUnknown on WriteMask (when NodeClass succeeded) fails closed.
      writeMaskDataValue.set(new DataValue(StatusCodes.Bad_NodeIdUnknown));
      assertEquals(
          new AccessResult.Denied(StatusCodes.Bad_NodeIdUnknown),
          controller.checkReadAccess(mockSession, List.of(readRolePerms)).get(readRolePerms));

      // 4. Uncertain WriteMask status fails closed as DENIED_USER_ACCESS.
      writeMaskDataValue.set(new DataValue(StatusCodes.Uncertain_InitialValue));
      assertEquals(
          AccessResult.DENIED_USER_ACCESS,
          controller.checkReadAccess(mockSession, List.of(readRolePerms)).get(readRolePerms));

      // 5. Malformed Good non-UInteger WriteMask payload fails closed with Bad_TypeMismatch.
      writeMaskDataValue.set(DataValue.valueOnly(Variant.ofString("not-a-mask")));
      assertEquals(
          new AccessResult.Denied(StatusCodes.Bad_TypeMismatch),
          controller.checkReadAccess(mockSession, List.of(readRolePerms)).get(readRolePerms));

      // 6. Good WriteMask with a null payload (Variant.NULL_VALUE) is also malformed and must fail
      // closed with Bad_TypeMismatch rather than being treated as absent.
      writeMaskDataValue.set(DataValue.valueOnly(Variant.NULL_VALUE));
      assertEquals(
          new AccessResult.Denied(StatusCodes.Bad_TypeMismatch),
          controller.checkReadAccess(mockSession, List.of(readRolePerms)).get(readRolePerms));

      // 7. Control: optional WriteMask omitted (Bad_AttributeIdInvalid) remains ALLOWED so
      // AttributeReader answers Bad_AttributeIdInvalid for RolePermissions.
      writeMaskDataValue.set(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
      assertEquals(
          AccessResult.ALLOWED,
          controller.checkReadAccess(mockSession, List.of(readRolePerms)).get(readRolePerms));
    }
  }

  // Failed or malformed AccessLevel, UserAccessLevel, and UserWriteMask lookups must fail closed
  // rather than discarding the bad status and falling through to ALLOWED.
  @Test
  void failedAccessLevelUserAccessLevelAndUserWriteMaskLookupsFailClosed() {
    var nodeId = new NodeId(1, "failedAccessAttrs");
    var readValue = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);
    var writeValue =
        new WriteValue(
            nodeId, AttributeId.Value.uid(), null, DataValue.valueOnly(Variant.ofBoolean(true)));
    var writeDescription =
        new WriteValue(
            nodeId, AttributeId.Description.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));
    var historyRead = new HistoryReadValueId(nodeId, null, null, null);
    var historyUpdate = new UpdateDataDetails(nodeId, PerformUpdateType.Insert, null);

    OpcUaServer mockServer = Mockito.mock(OpcUaServer.class);
    AddressSpaceManager mockAddressSpaceManager = Mockito.mock(AddressSpaceManager.class);
    Session mockSession = Mockito.mock(Session.class);
    EndpointDescription endpoint = Mockito.mock(EndpointDescription.class);
    Mockito.when(mockServer.getAddressSpaceManager()).thenReturn(mockAddressSpaceManager);
    Mockito.when(mockServer.getNamespaceTable()).thenReturn(new NamespaceTable());
    Mockito.when(mockSession.getEndpoint()).thenReturn(endpoint);
    Mockito.when(endpoint.getSecurityMode()).thenReturn(MessageSecurityMode.SignAndEncrypt);
    Mockito.when(mockSession.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    UByte fullAccess =
        AccessLevel.toValue(
            Set.of(
                AccessLevel.CurrentRead,
                AccessLevel.CurrentWrite,
                AccessLevel.HistoryRead,
                AccessLevel.HistoryWrite));
    var accessLevelDv = new AtomicReference<>(DataValue.valueOnly(new Variant(fullAccess)));
    var userAccessLevelDv = new AtomicReference<>(DataValue.valueOnly(new Variant(fullAccess)));
    var userWriteMaskDv = new AtomicReference<>(DataValue.valueOnly(new Variant(UInteger.MAX)));

    Mockito.when(
            mockAddressSpaceManager.read(
                Mockito.any(), Mockito.anyDouble(), Mockito.any(), Mockito.anyList()))
        .thenAnswer(
            invocation -> {
              List<ReadValueId> reads = invocation.getArgument(3);
              var values = new ArrayList<DataValue>(reads.size());
              for (ReadValueId r : reads) {
                if (AttributeId.NodeClass.uid().equals(r.getAttributeId())) {
                  values.add(DataValue.valueOnly(new Variant(NodeClass.Variable)));
                } else if (AttributeId.AccessLevel.uid().equals(r.getAttributeId())) {
                  values.add(accessLevelDv.get());
                } else if (AttributeId.UserAccessLevel.uid().equals(r.getAttributeId())) {
                  values.add(userAccessLevelDv.get());
                } else if (AttributeId.UserWriteMask.uid().equals(r.getAttributeId())) {
                  values.add(userWriteMaskDv.get());
                } else {
                  values.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
                }
              }
              return values;
            });

    var controller = new DefaultAccessController(mockServer);

    // 1. UserAccessLevel failures (including Bad_AttributeIdInvalid on a Variable where
    // UserAccessLevel is mandatory per OPC 10000-3 §5.6.2) fail closed across Read(Value),
    // Write(Value), HistoryRead, and HistoryUpdate even when node-level AccessLevel permits
    // everything.
    for (Map.Entry<DataValue, ? extends AccessResult> testCase :
        List.of(
            Map.entry(
                new DataValue(StatusCodes.Bad_UserAccessDenied), AccessResult.DENIED_USER_ACCESS),
            Map.entry(
                new DataValue(StatusCodes.Bad_AttributeIdInvalid),
                new AccessResult.Denied(StatusCodes.Bad_AttributeIdInvalid)),
            Map.entry(
                new DataValue(StatusCodes.Bad_InternalError),
                new AccessResult.Denied(StatusCodes.Bad_InternalError)),
            Map.entry(
                DataValue.valueOnly(Variant.ofString("bad-ual")),
                new AccessResult.Denied(StatusCodes.Bad_TypeMismatch)),
            Map.entry(
                DataValue.valueOnly(Variant.NULL_VALUE),
                new AccessResult.Denied(StatusCodes.Bad_TypeMismatch)))) {
      userAccessLevelDv.set(testCase.getKey());
      AccessResult expected = testCase.getValue();

      assertEquals(
          expected, controller.checkReadAccess(mockSession, List.of(readValue)).get(readValue));
      assertEquals(
          expected, controller.checkWriteAccess(mockSession, List.of(writeValue)).get(writeValue));
      assertEquals(
          expected,
          controller.checkHistoryReadAccess(mockSession, List.of(historyRead)).get(historyRead));
      assertEquals(
          expected,
          controller
              .checkHistoryUpdateAccess(mockSession, List.of(historyUpdate))
              .get(historyUpdate));
    }
    userAccessLevelDv.set(DataValue.valueOnly(new Variant(fullAccess)));

    // 2. AccessLevel failures (including Bad_AttributeIdInvalid on a Variable) fail closed across
    // Read(Value), Write(Value), HistoryRead, and HistoryUpdate.
    for (long statusCode :
        List.of(StatusCodes.Bad_InternalError, StatusCodes.Bad_AttributeIdInvalid)) {
      accessLevelDv.set(new DataValue(statusCode));
      var expected = new AccessResult.Denied(statusCode);
      assertEquals(
          expected, controller.checkReadAccess(mockSession, List.of(readValue)).get(readValue));
      assertEquals(
          expected, controller.checkWriteAccess(mockSession, List.of(writeValue)).get(writeValue));
      assertEquals(
          expected,
          controller.checkHistoryReadAccess(mockSession, List.of(historyRead)).get(historyRead));
      assertEquals(
          expected,
          controller
              .checkHistoryUpdateAccess(mockSession, List.of(historyUpdate))
              .get(historyUpdate));
    }
    accessLevelDv.set(DataValue.valueOnly(new Variant(fullAccess)));

    // 3. UserWriteMask failures fail closed on non-Value writes.
    userWriteMaskDv.set(new DataValue(StatusCodes.Bad_UserAccessDenied));
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        controller.checkWriteAccess(mockSession, List.of(writeDescription)).get(writeDescription));

    userWriteMaskDv.set(DataValue.valueOnly(Variant.NULL_VALUE));
    assertEquals(
        new AccessResult.Denied(StatusCodes.Bad_TypeMismatch),
        controller.checkWriteAccess(mockSession, List.of(writeDescription)).get(writeDescription));

    // Control: optional UserWriteMask omitted (Bad_AttributeIdInvalid) stays ALLOWED.
    userWriteMaskDv.set(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
    assertEquals(
        AccessResult.ALLOWED,
        controller.checkWriteAccess(mockSession, List.of(writeDescription)).get(writeDescription));
  }

  // Forwarding AddressSpaces may return NodeClass as its wire Integer value rather than a NodeClass
  // enum instance. DefaultAccessControlContext must decode wire Integer NodeClass values so
  // mandatory Variable AccessLevel/UserAccessLevel lookups still fail closed on
  // Bad_AttributeIdInvalid while VariableType nodes continue allowing optional omission.
  @Test
  void forwardedIntegerNodeClassFailsClosedForVariableAndAllowsOptionalOmissionForVariableType() {
    var nodeId = new NodeId(1, "forwardedIntegerNodeClass");
    var readValue = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);
    var writeValue =
        new WriteValue(
            nodeId, AttributeId.Value.uid(), null, DataValue.valueOnly(Variant.ofBoolean(true)));
    var historyRead = new HistoryReadValueId(nodeId, null, null, null);
    var historyUpdate = new UpdateDataDetails(nodeId, PerformUpdateType.Insert, null);

    OpcUaServer mockServer = Mockito.mock(OpcUaServer.class);
    AddressSpaceManager mockAddressSpaceManager = Mockito.mock(AddressSpaceManager.class);
    Session mockSession = Mockito.mock(Session.class);
    EndpointDescription endpoint = Mockito.mock(EndpointDescription.class);
    Mockito.when(mockServer.getAddressSpaceManager()).thenReturn(mockAddressSpaceManager);
    Mockito.when(mockServer.getNamespaceTable()).thenReturn(new NamespaceTable());
    Mockito.when(mockSession.getEndpoint()).thenReturn(endpoint);
    Mockito.when(endpoint.getSecurityMode()).thenReturn(MessageSecurityMode.SignAndEncrypt);
    Mockito.when(mockSession.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    UByte fullAccess =
        AccessLevel.toValue(
            Set.of(
                AccessLevel.CurrentRead,
                AccessLevel.CurrentWrite,
                AccessLevel.HistoryRead,
                AccessLevel.HistoryWrite));
    var nodeClassDv =
        new AtomicReference<>(DataValue.valueOnly(Variant.ofInt32(NodeClass.Variable.getValue())));
    var accessLevelDv = new AtomicReference<>(DataValue.valueOnly(new Variant(fullAccess)));
    var userAccessLevelDv = new AtomicReference<>(DataValue.valueOnly(new Variant(fullAccess)));

    Mockito.when(
            mockAddressSpaceManager.read(
                Mockito.any(), Mockito.anyDouble(), Mockito.any(), Mockito.anyList()))
        .thenAnswer(
            invocation -> {
              List<ReadValueId> reads = invocation.getArgument(3);
              var values = new ArrayList<DataValue>(reads.size());
              for (ReadValueId r : reads) {
                if (AttributeId.NodeClass.uid().equals(r.getAttributeId())) {
                  values.add(nodeClassDv.get());
                } else if (AttributeId.AccessLevel.uid().equals(r.getAttributeId())) {
                  values.add(accessLevelDv.get());
                } else if (AttributeId.UserAccessLevel.uid().equals(r.getAttributeId())) {
                  values.add(userAccessLevelDv.get());
                } else {
                  values.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
                }
              }
              return values;
            });

    var controller = new DefaultAccessController(mockServer);
    var expectedInvalid = new AccessResult.Denied(StatusCodes.Bad_AttributeIdInvalid);

    // 1. Forwarded wire Integer NodeClass.Variable (2): Bad_AttributeIdInvalid on UserAccessLevel
    // fails closed across Read(Value), Write(Value), HistoryRead, and HistoryUpdate.
    userAccessLevelDv.set(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
    assertEquals(
        expectedInvalid,
        controller.checkReadAccess(mockSession, List.of(readValue)).get(readValue));
    assertEquals(
        expectedInvalid,
        controller.checkWriteAccess(mockSession, List.of(writeValue)).get(writeValue));
    assertEquals(
        expectedInvalid,
        controller.checkHistoryReadAccess(mockSession, List.of(historyRead)).get(historyRead));
    assertEquals(
        expectedInvalid,
        controller
            .checkHistoryUpdateAccess(mockSession, List.of(historyUpdate))
            .get(historyUpdate));
    userAccessLevelDv.set(DataValue.valueOnly(new Variant(fullAccess)));

    // 2. Forwarded wire Integer NodeClass.Variable (2): Bad_AttributeIdInvalid on AccessLevel fails
    // closed across Read(Value), Write(Value), HistoryRead, and HistoryUpdate.
    accessLevelDv.set(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
    assertEquals(
        expectedInvalid,
        controller.checkReadAccess(mockSession, List.of(readValue)).get(readValue));
    assertEquals(
        expectedInvalid,
        controller.checkWriteAccess(mockSession, List.of(writeValue)).get(writeValue));
    assertEquals(
        expectedInvalid,
        controller.checkHistoryReadAccess(mockSession, List.of(historyRead)).get(historyRead));
    assertEquals(
        expectedInvalid,
        controller
            .checkHistoryUpdateAccess(mockSession, List.of(historyUpdate))
            .get(historyUpdate));

    // 3. Forwarded wire Integer NodeClass.VariableType (16): Bad_AttributeIdInvalid on both
    // AccessLevel and UserAccessLevel remains ALLOWED because those attributes are omitted on
    // VariableType nodes.
    nodeClassDv.set(DataValue.valueOnly(Variant.ofInt32(NodeClass.VariableType.getValue())));
    userAccessLevelDv.set(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
    assertEquals(
        AccessResult.ALLOWED,
        controller.checkReadAccess(mockSession, List.of(readValue)).get(readValue));
    assertEquals(
        AccessResult.ALLOWED,
        controller.checkWriteAccess(mockSession, List.of(writeValue)).get(writeValue));
  }
}
