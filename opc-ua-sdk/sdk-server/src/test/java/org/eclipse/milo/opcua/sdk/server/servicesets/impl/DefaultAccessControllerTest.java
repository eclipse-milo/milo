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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultAccessController.AccessControlAttributes;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultAccessController.AccessControlContext;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DefaultAccessControllerTest {

  private static final NodeId ROLE_A = new NodeId(1, "RoleA");
  private static final NodeId ROLE_B = new NodeId(1, "RoleB");

  private final AccessControlContext context = Mockito.mock(AccessControlContext.class);
  private final Map<NodeId, AccessControlAttributes> attributesMap = new HashMap<>();

  @BeforeEach
  void setup() {
    Mockito.when(context.readAccessControlAttributes(Mockito.anyList())).thenReturn(attributesMap);
  }

  @Test
  void checkReadAccess_Value_Allowed() {
    var nodeId = new NodeId(1, "foo");
    var readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);

    UByte userAccessLevel = AccessLevel.toValue(AccessLevel.READ_ONLY);

    var attributes = new AccessControlAttributes(null, null, userAccessLevel, null, null);
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

    var attributes = new AccessControlAttributes(null, null, userAccessLevel, null, null);
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
            new RolePermissionType[] {new RolePermissionType(ROLE_A, PermissionType.of())});
    attributesMap.put(nodeId, attributes);

    Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of(ROLE_A)));

    AccessResult result =
        DefaultAccessController.checkReadAccess(context, List.of(readValueId)).get(readValueId);

    assertEquals(AccessResult.DENIED_USER_ACCESS, result);
  }

  @Test
  void checkWriteAccess_Value_Allowed() {
    var nodeId = new NodeId(1, "foo");
    var writeValue =
        new WriteValue(
            nodeId, AttributeId.Value.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));

    UByte userAccessLevel = AccessLevel.toValue(AccessLevel.READ_WRITE);

    var attributes = new AccessControlAttributes(null, null, userAccessLevel, null, null);
    attributesMap.put(nodeId, attributes);

    AccessResult result =
        DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

    assertEquals(AccessResult.ALLOWED, result);
  }

  @Test
  void checkWriteAccess_Value_Denied() {
    var nodeId = new NodeId(1, "foo");
    var writeValue =
        new WriteValue(
            nodeId, AttributeId.Value.uid(), null, DataValue.valueOnly(Variant.NULL_VALUE));

    UByte userAccessLevel = AccessLevel.toValue(AccessLevel.READ_ONLY);

    var attributes = new AccessControlAttributes(null, null, userAccessLevel, null, null);
    attributesMap.put(nodeId, attributes);

    AccessResult result =
        DefaultAccessController.checkWriteAccess(context, List.of(writeValue)).get(writeValue);

    assertEquals(AccessResult.DENIED_USER_ACCESS, result);
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

    attributesMap.put(objectNodeId, new AccessControlAttributes(null, null, null, null, null));

    {
      attributesMap.put(methodNodeId, new AccessControlAttributes(null, null, null, false, null));

      Mockito.when(context.getRoleIds()).thenReturn(Optional.of(List.of()));

      AccessResult result =
          DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
              .get(callMethodRequest);

      assertEquals(AccessResult.DENIED_USER_ACCESS, result);
    }

    {
      attributesMap.put(methodNodeId, new AccessControlAttributes(null, null, null, true, null));

      AccessResult result =
          DefaultAccessController.checkCallAccess(context, List.of(callMethodRequest))
              .get(callMethodRequest);

      assertEquals(AccessResult.ALLOWED, result);
    }
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
}
