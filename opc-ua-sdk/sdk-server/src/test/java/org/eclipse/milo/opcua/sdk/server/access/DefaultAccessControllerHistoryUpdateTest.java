/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.access;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.access.DefaultAccessController.AccessControlAttributes;
import org.eclipse.milo.opcua.sdk.server.access.DefaultAccessController.AccessControlContext;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteAtTimeDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteEventDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateDataDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateEventDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateStructureDataDetails;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

/** {@link DefaultAccessController#checkHistoryUpdateAccess}. */
class DefaultAccessControllerHistoryUpdateTest {

  private static final NodeId ROLE_A = new NodeId(1, "RoleA");
  private static final NodeId ROLE_B = new NodeId(1, "RoleB");

  private static final NodeId NODE_ID = new NodeId(1, "history");

  private static final Set<PermissionType.Field> HISTORY_PERMISSIONS =
      EnumSet.of(
          PermissionType.Field.InsertHistory,
          PermissionType.Field.ModifyHistory,
          PermissionType.Field.DeleteHistory);

  private static final UByte HISTORY_WRITE = AccessLevel.toValue(Set.of(AccessLevel.HistoryWrite));

  /** Everything except HistoryWrite, so a check of any other bit would pass. */
  private static final UByte NO_HISTORY_WRITE =
      AccessLevel.toValue(
          Set.of(AccessLevel.CurrentRead, AccessLevel.CurrentWrite, AccessLevel.HistoryRead));

  private final AccessControlContext context = Mockito.mock(AccessControlContext.class);
  private final Map<NodeId, AccessControlAttributes> attributesMap = new HashMap<>();

  @BeforeEach
  void setup() {
    Mockito.when(context.readAccessControlAttributes(Mockito.anyList(), Mockito.any()))
        .thenReturn(attributesMap);
    Mockito.when(context.getRoleIds()).thenReturn(Optional.empty());
    Mockito.when(context.getSecurityMode()).thenReturn(MessageSecurityMode.None);
  }

  static Stream<Arguments> requiredPermissions() {
    return Stream.of(
        Arguments.of(
            "UpdateDataDetails Insert",
            updateData(PerformUpdateType.Insert),
            EnumSet.of(PermissionType.Field.InsertHistory)),
        Arguments.of(
            "UpdateDataDetails Replace",
            updateData(PerformUpdateType.Replace),
            EnumSet.of(PermissionType.Field.ModifyHistory)),
        Arguments.of(
            "UpdateDataDetails Update",
            updateData(PerformUpdateType.Update),
            EnumSet.of(PermissionType.Field.InsertHistory, PermissionType.Field.ModifyHistory)),
        Arguments.of(
            "UpdateDataDetails Remove",
            updateData(PerformUpdateType.Remove),
            EnumSet.of(PermissionType.Field.DeleteHistory)),
        Arguments.of(
            "UpdateStructureDataDetails Insert",
            new UpdateStructureDataDetails(NODE_ID, PerformUpdateType.Insert, null),
            EnumSet.of(PermissionType.Field.InsertHistory)),
        Arguments.of(
            "UpdateStructureDataDetails Remove",
            new UpdateStructureDataDetails(NODE_ID, PerformUpdateType.Remove, null),
            EnumSet.of(PermissionType.Field.DeleteHistory)),
        Arguments.of(
            "UpdateEventDetails Replace",
            updateEvent(PerformUpdateType.Replace),
            EnumSet.of(PermissionType.Field.ModifyHistory)),
        Arguments.of(
            "UpdateEventDetails Update",
            updateEvent(PerformUpdateType.Update),
            EnumSet.of(PermissionType.Field.InsertHistory, PermissionType.Field.ModifyHistory)),
        Arguments.of(
            "DeleteRawModifiedDetails",
            new DeleteRawModifiedDetails(NODE_ID, false, DateTime.MIN_VALUE, DateTime.MIN_VALUE),
            EnumSet.of(PermissionType.Field.DeleteHistory)),
        Arguments.of(
            "DeleteAtTimeDetails",
            new DeleteAtTimeDetails(NODE_ID, null),
            EnumSet.of(PermissionType.Field.DeleteHistory)),
        Arguments.of(
            "DeleteEventDetails",
            new DeleteEventDetails(NODE_ID, null),
            EnumSet.of(PermissionType.Field.DeleteHistory)));
  }

  /**
   * Part 3 §8.55 has separate permissions for inserting, modifying, and deleting history. Holding
   * exactly the permissions an entry needs allows it.
   */
  @ParameterizedTest(name = "{0}")
  @MethodSource("requiredPermissions")
  void entryIsAllowedWithExactlyThePermissionsItNeeds(
      String name, HistoryUpdateDetails details, Set<PermissionType.Field> required) {

    givenRole(ROLE_A);
    givenAttributes(null, rolePermissions(ROLE_A, required));

    assertEquals(AccessResult.ALLOWED, check(details));
  }

  /**
   * Each permission an entry needs is necessary: holding every history permission but one of them
   * denies the entry. This is what tells Update, which needs InsertHistory and ModifyHistory, apart
   * from an entry that would accept either.
   */
  @ParameterizedTest(name = "{0}")
  @MethodSource("requiredPermissions")
  void entryIsDeniedWhenAnyPermissionItNeedsIsMissing(
      String name, HistoryUpdateDetails details, Set<PermissionType.Field> required) {

    givenRole(ROLE_A);

    for (PermissionType.Field missing : required) {
      Set<PermissionType.Field> granted = EnumSet.copyOf(HISTORY_PERMISSIONS);
      granted.remove(missing);

      givenAttributes(null, rolePermissions(ROLE_A, granted));

      assertEquals(AccessResult.DENIED_USER_ACCESS, check(details), "missing " + missing);
    }
  }

  // The permissions of a Session's roles add up, so InsertHistory from one role and ModifyHistory
  // from another together allow an Update.
  @Test
  void updateIsAllowedWhenDifferentRolesGrantInsertAndModify() {
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A, ROLE_B)));
    givenAttributes(
        null,
        new RolePermissionType[] {
          new RolePermissionType(ROLE_A, PermissionType.of(PermissionType.Field.InsertHistory)),
          new RolePermissionType(ROLE_B, PermissionType.of(PermissionType.Field.ModifyHistory))
        });

    assertEquals(AccessResult.ALLOWED, check(updateData(PerformUpdateType.Update)));
  }

  // A permission granted to a role the Session does not have does not count.
  @Test
  void permissionOfARoleTheSessionLacksDoesNotAllow() {
    givenRole(ROLE_A);
    givenAttributes(null, rolePermissions(ROLE_B, HISTORY_PERMISSIONS));

    assertEquals(AccessResult.DENIED_USER_ACCESS, check(updateData(PerformUpdateType.Insert)));
  }

  // An update type that did not decode says nothing about what the AddressSpace would do with the
  // entry, so only a Session that may insert, modify, and delete gets it through.
  @Test
  void undecodedUpdateTypeNeedsEveryHistoryPermission() {
    HistoryUpdateDetails details = updateData(null);
    givenRole(ROLE_A);

    givenAttributes(
        null,
        rolePermissions(
            ROLE_A,
            EnumSet.of(PermissionType.Field.InsertHistory, PermissionType.Field.ModifyHistory)));
    assertEquals(AccessResult.DENIED_USER_ACCESS, check(details));

    givenAttributes(null, rolePermissions(ROLE_A, HISTORY_PERMISSIONS));
    assertEquals(AccessResult.ALLOWED, check(details));
  }

  // Role permissions only apply to a Session that has roles, as in the other checks. Without
  // them the attributes decide.
  @Test
  void sessionWithoutRolesIsNotCheckedAgainstRolePermissions() {
    givenAttributes(
        new Attributes(NodeClass.Variable, HISTORY_WRITE, HISTORY_WRITE, null),
        rolePermissions(ROLE_A, EnumSet.noneOf(PermissionType.Field.class)));

    assertEquals(AccessResult.ALLOWED, check(updateData(PerformUpdateType.Insert)));
  }

  /**
   * Part 3 §8.57: the HistoryWrite bit of AccessLevel says whether the history of a Variable's
   * value is writable at all, so the denial is Bad_NotWritable (Part 4 §5.11.5.4) whoever asks.
   */
  @Test
  void variableWithoutHistoryWriteAccessLevelIsNotWritable() {
    givenAttributes(
        new Attributes(NodeClass.Variable, NO_HISTORY_WRITE, HISTORY_WRITE, null), null);

    assertEquals(AccessResult.DENIED_NOT_WRITABLE, check(updateData(PerformUpdateType.Insert)));
  }

  // The HistoryWrite bit of UserAccessLevel is this user's answer, so its denial is
  // Bad_UserAccessDenied.
  @Test
  void variableWithoutHistoryWriteUserAccessLevelIsUserAccessDenied() {
    givenAttributes(
        new Attributes(NodeClass.Variable, HISTORY_WRITE, NO_HISTORY_WRITE, null), null);

    assertEquals(AccessResult.DENIED_USER_ACCESS, check(updateData(PerformUpdateType.Insert)));
  }

  // As for a Value write, the Node's own answer comes before the user's.
  @Test
  void notWritableTakesPrecedenceOverUserAccessDenied() {
    givenAttributes(
        new Attributes(NodeClass.Variable, NO_HISTORY_WRITE, NO_HISTORY_WRITE, null), null);

    assertEquals(
        AccessResult.DENIED_NOT_WRITABLE,
        check(new DeleteAtTimeDetails(NODE_ID, new DateTime[] {DateTime.MIN_VALUE})));
  }

  // A Variable passes the attribute check with HistoryWrite alone; CurrentWrite is not involved.
  @Test
  void variableWithHistoryWriteIsAllowed() {
    givenAttributes(new Attributes(NodeClass.Variable, HISTORY_WRITE, HISTORY_WRITE, null), null);

    assertEquals(AccessResult.ALLOWED, check(updateData(PerformUpdateType.Insert)));
  }

  /**
   * Part 3 §8.59: for an Object or View, the HistoryWrite bit of EventNotifier says whether the
   * history of its events is writable.
   */
  @ParameterizedTest
  @MethodSource("eventNotifierNodeClasses")
  void eventNotifierWithoutHistoryWriteIsNotWritable(NodeClass nodeClass) {
    // SubscribeToEvents | HistoryRead
    givenAttributes(new Attributes(nodeClass, null, null, ubyte(0x05)), null);

    assertEquals(AccessResult.DENIED_NOT_WRITABLE, check(updateEvent(PerformUpdateType.Insert)));
    assertEquals(AccessResult.DENIED_NOT_WRITABLE, check(new DeleteEventDetails(NODE_ID, null)));
  }

  @ParameterizedTest
  @MethodSource("eventNotifierNodeClasses")
  void eventNotifierWithHistoryWriteIsAllowed(NodeClass nodeClass) {
    // HistoryWrite
    givenAttributes(new Attributes(nodeClass, null, null, ubyte(0x08)), null);

    assertEquals(AccessResult.ALLOWED, check(updateEvent(PerformUpdateType.Insert)));
  }

  static Stream<NodeClass> eventNotifierNodeClasses() {
    return Stream.of(NodeClass.Object, NodeClass.View);
  }

  // The attribute checks are a gate in front of the permissions, not an alternative to them.
  @Test
  void permissionDoesNotOverrideAMissingHistoryWriteBit() {
    givenRole(ROLE_A);
    givenAttributes(
        new Attributes(NodeClass.Variable, HISTORY_WRITE, NO_HISTORY_WRITE, null),
        rolePermissions(ROLE_A, HISTORY_PERMISSIONS));

    assertEquals(AccessResult.DENIED_USER_ACCESS, check(updateData(PerformUpdateType.Insert)));
  }

  // AccessRestrictions apply to every service that touches the Node, HistoryUpdate included.
  @Test
  void accessRestrictionsAreEnforced() {
    // SigningRequired | EncryptionRequired
    var accessRestrictions = new AccessRestrictionType(UShort.valueOf(3));

    attributesMap.put(
        NODE_ID,
        new AccessControlAttributes(
            NodeClass.Variable,
            accessRestrictions,
            null,
            HISTORY_WRITE,
            HISTORY_WRITE,
            null,
            null,
            null,
            false));

    HistoryUpdateDetails details = updateData(PerformUpdateType.Insert);

    Mockito.when(context.getSecurityMode()).thenReturn(MessageSecurityMode.Sign);
    assertEquals(AccessResult.DENIED_SECURITY_MODE, check(details));

    Mockito.when(context.getSecurityMode()).thenReturn(MessageSecurityMode.SignAndEncrypt);
    assertEquals(AccessResult.ALLOWED, check(details));
  }

  // A Node the AddressSpace does not know is neither allowed nor denied, so HistoryUpdate still
  // reaches the AddressSpace, which answers Bad_NodeIdUnknown as it would have.
  @Test
  void unknownNodeIsNoDecision() {
    attributesMap.put(
        NODE_ID, new AccessControlAttributes(null, null, null, null, null, null, null, null, true));

    AccessResult result = check(updateData(PerformUpdateType.Insert));

    assertEquals(AccessResult.NODE_UNKNOWN, result);
    assertFalse(result.isDenied());
  }

  // A details type the controller does not recognize names no Node it could check, so letting it
  // through would skip the check altogether.
  @Test
  void unrecognizedDetailsTypeIsDenied() {
    HistoryUpdateDetails details = Mockito.mock(HistoryUpdateDetails.class);

    assertEquals(
        new AccessResult.Denied(new StatusCode(StatusCodes.Bad_HistoryOperationUnsupported)),
        check(details));
  }

  // HistoryUpdate looks results up by instance, and two equal entries in one request are still
  // two operations.
  @Test
  void resultsAreKeyedByIdentity() {
    givenAttributes(new Attributes(NodeClass.Variable, HISTORY_WRITE, HISTORY_WRITE, null), null);

    HistoryUpdateDetails first = updateData(PerformUpdateType.Insert);
    HistoryUpdateDetails second = updateData(PerformUpdateType.Insert);
    assertEquals(first, second);

    Map<HistoryUpdateDetails, AccessResult> results =
        DefaultAccessController.checkHistoryUpdateAccess(context, List.of(first, second));

    assertEquals(2, results.size());
    assertEquals(AccessResult.ALLOWED, results.get(first));
    assertEquals(AccessResult.ALLOWED, results.get(second));
  }

  // Each entry of a mixed request gets the result for its own Node.
  @Test
  void entriesAreDecidedIndependently() {
    var writable = new NodeId(1, "writable");
    var readOnly = new NodeId(1, "readOnly");

    attributesMap.put(
        writable,
        new AccessControlAttributes(
            NodeClass.Variable, null, null, HISTORY_WRITE, HISTORY_WRITE, null, null, null, false));
    attributesMap.put(
        readOnly,
        new AccessControlAttributes(
            NodeClass.Variable,
            null,
            null,
            NO_HISTORY_WRITE,
            NO_HISTORY_WRITE,
            null,
            null,
            null,
            false));

    var allowed = new UpdateDataDetails(writable, PerformUpdateType.Insert, null);
    var denied = new UpdateDataDetails(readOnly, PerformUpdateType.Insert, null);

    Map<HistoryUpdateDetails, AccessResult> results =
        DefaultAccessController.checkHistoryUpdateAccess(context, List.of(denied, allowed));

    assertEquals(AccessResult.DENIED_NOT_WRITABLE, results.get(denied));
    assertEquals(AccessResult.ALLOWED, results.get(allowed));
  }

  /**
   * The controller decides from attributes it reads through the AddressSpace, so the check has to
   * ask for the ones it uses, EventNotifier among them, and decide from what comes back.
   */
  @Test
  void readsTheAttributesItDecidesFromThroughTheAddressSpace() {
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
            AttributeId.NodeClass, new Variant(NodeClass.Object),
            AttributeId.EventNotifier, new Variant(ubyte(0x05)));

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

    HistoryUpdateDetails details = updateEvent(PerformUpdateType.Insert);

    AccessResult result =
        new DefaultAccessController(server)
            .checkHistoryUpdateAccess(session, List.of(details))
            .get(details);

    assertEquals(AccessResult.DENIED_NOT_WRITABLE, result);
    assertEquals(
        List.of(
            AttributeId.NodeClass,
            AttributeId.AccessRestrictions,
            AttributeId.AccessLevel,
            AttributeId.UserAccessLevel,
            AttributeId.EventNotifier,
            AttributeId.UserRolePermissions),
        attributesRead);
  }

  private AccessResult check(HistoryUpdateDetails details) {
    return DefaultAccessController.checkHistoryUpdateAccess(context, List.of(details)).get(details);
  }

  private void givenRole(NodeId roleId) {
    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(roleId)));
  }

  /** Set the attributes of {@link #NODE_ID}; null {@code attributes} leaves them all unset. */
  private void givenAttributes(
      @Nullable Attributes attributes, RolePermissionType @Nullable [] userRolePermissions) {

    Attributes a = attributes != null ? attributes : new Attributes(null, null, null, null);

    attributesMap.put(
        NODE_ID,
        new AccessControlAttributes(
            a.nodeClass(),
            null,
            null,
            a.accessLevel(),
            a.userAccessLevel(),
            a.eventNotifier(),
            null,
            userRolePermissions,
            false));
  }

  private record Attributes(
      @Nullable NodeClass nodeClass,
      @Nullable UByte accessLevel,
      @Nullable UByte userAccessLevel,
      @Nullable UByte eventNotifier) {}

  private static RolePermissionType[] rolePermissions(
      NodeId roleId, Set<PermissionType.Field> fields) {

    return new RolePermissionType[] {
      new RolePermissionType(roleId, PermissionType.of(fields.toArray(PermissionType.Field[]::new)))
    };
  }

  private static UpdateDataDetails updateData(@Nullable PerformUpdateType updateType) {
    return new UpdateDataDetails(NODE_ID, updateType, null);
  }

  private static UpdateEventDetails updateEvent(PerformUpdateType updateType) {
    return new UpdateEventDetails(
        NODE_ID, updateType, new EventFilter(null, new ContentFilter(null)), null);
  }
}
