/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.WriteMask;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.AttributeReader;
import org.eclipse.milo.opcua.sdk.server.AttributeWriter;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.VariableTypeManager;
import org.eclipse.milo.opcua.sdk.server.access.RolePermissionsReadScope;
import org.eclipse.milo.opcua.sdk.server.access.RolePermissionsSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.TypeModelCache;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.OpcUaEncodingManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UaNodeTest {

  private OpcUaServer server;
  private UaNodeContext nodeContext;
  private UaNodeManager nodeManager;

  @BeforeEach
  public void setup() throws Exception {
    server = Mockito.mock(OpcUaServer.class);

    AddressSpaceManager addressSpaceManager = new AddressSpaceManager(server);
    NamespaceTable namespaceTable = new NamespaceTable();
    ObjectTypeManager objectTypeManager = new ObjectTypeManager();
    VariableTypeManager variableTypeManager = new VariableTypeManager();

    Mockito.when(server.getNamespaceTable()).thenReturn(namespaceTable);
    Mockito.when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    Mockito.when(server.getObjectTypeManager()).thenReturn(objectTypeManager);
    Mockito.when(server.getVariableTypeManager()).thenReturn(variableTypeManager);
    Mockito.when(server.getStaticEncodingContext()).thenReturn(DefaultEncodingContext.INSTANCE);
    Mockito.when(server.getEncodingManager()).thenReturn(OpcUaEncodingManager.getInstance());
    Mockito.when(server.getTypeModelCache()).thenReturn(new TypeModelCache(server));

    nodeManager = new UaNodeManager();
    addressSpaceManager.register(nodeManager);

    nodeContext =
        new UaNodeContext() {
          @Override
          public OpcUaServer getServer() {
            return server;
          }

          @Override
          public NodeManager<UaNode> getNodeManager() {
            return nodeManager;
          }
        };
  }

  @Test
  public void testGetPropertyWithExtensionObjects() {
    // Create a UaObjectNode to test with
    NodeId objectNodeId = new NodeId(1, "TestObject");

    UaObjectNode objectNode =
        UaObjectNode.build(
            nodeContext,
            b ->
                b.setNodeId(objectNodeId)
                    .setBrowseName(new QualifiedName(1, "TestObject"))
                    .setDisplayName(LocalizedText.english("TestObject"))
                    .setTypeDefinition(NodeIds.BaseObjectType)
                    .build());

    nodeManager.addNode(objectNode);

    // Define QualifiedProperty instances for scalar, array, and matrix XVType
    QualifiedProperty<XVType> scalarProperty =
        new QualifiedProperty<>(
            "http://opcfoundation.org/UA/",
            "ScalarXVProperty",
            XVType.TYPE_ID,
            ValueRanks.Scalar,
            XVType.class);

    QualifiedProperty<XVType[]> arrayProperty =
        new QualifiedProperty<>(
            "http://opcfoundation.org/UA/",
            "ArrayXVProperty",
            XVType.TYPE_ID,
            ValueRanks.OneDimension,
            XVType[].class);

    QualifiedProperty<Matrix> matrixProperty =
        new QualifiedProperty<>(
            "http://opcfoundation.org/UA/", "MatrixXVProperty", XVType.TYPE_ID, 2, Matrix.class);

    // Set these to null to create the PropertyNodes
    objectNode.setProperty(scalarProperty, null);
    objectNode.setProperty(arrayProperty, null);
    objectNode.setProperty(matrixProperty, null);

    // Now set the values of each to contain ExtensionObjects containing the XVTypes
    XVType scalarValue = new XVType(1.0, 2.0f);
    XVType[] arrayValue = {new XVType(3.0, 4.0f), new XVType(5.0, 6.0f), new XVType(7.0, 8.0f)};
    XVType[][] matrixValue = {
      {new XVType(9.0, 10.0f), new XVType(11.0, 12.0f)},
      {new XVType(13.0, 14.0f), new XVType(15.0, 16.0f)}
    };

    objectNode
        .getPropertyNode(scalarProperty)
        .orElseThrow()
        .setValue(
            new DataValue(
                Variant.ofExtensionObject(
                    ExtensionObject.encode(server.getStaticEncodingContext(), scalarValue))));

    objectNode
        .getPropertyNode(arrayProperty)
        .orElseThrow()
        .setValue(
            new DataValue(
                Variant.ofExtensionObjectArray(
                    ExtensionObject.encodeArray(server.getStaticEncodingContext(), arrayValue))));

    objectNode
        .getPropertyNode(matrixProperty)
        .orElseThrow()
        .setValue(
            new DataValue(
                Variant.ofMatrix(
                    Matrix.ofStruct(matrixValue)
                        .transform(
                            e ->
                                ExtensionObject.encode(
                                    server.getStaticEncodingContext(), (XVType) e)))));

    // Get properties (this should decode ExtensionObjects back to XVType)
    Optional<XVType> retrievedScalar = objectNode.getProperty(scalarProperty);
    Optional<XVType[]> retrievedArray = objectNode.getProperty(arrayProperty);
    Optional<Matrix> retrievedMatrix = objectNode.getProperty(matrixProperty);

    // Assert scalar property
    XVType scalar = retrievedScalar.orElseThrow();
    assertEquals(1.0, scalar.getX(), "Scalar x value should match");
    assertEquals(2.0f, scalar.getValue(), "Scalar value should match");

    // Assert array property
    XVType[] array = retrievedArray.orElseThrow();
    assertEquals(3, array.length, "Array length should be 3");
    assertEquals(3.0, array[0].getX(), "Array[0] x value should match");
    assertEquals(4.0f, array[0].getValue(), "Array[0] value should match");
    assertEquals(5.0, array[1].getX(), "Array[1] x value should match");
    assertEquals(6.0f, array[1].getValue(), "Array[1] value should match");
    assertEquals(7.0, array[2].getX(), "Array[2] x value should match");
    assertEquals(8.0f, array[2].getValue(), "Array[2] value should match");

    // Assert matrix property
    Matrix matrix = retrievedMatrix.orElseThrow();
    int[] dimensions = matrix.getDimensions();
    assertEquals(2, dimensions.length, "Matrix should have 2 dimensions");
    assertEquals(2, dimensions[0], "Matrix should have 2 rows");
    assertEquals(2, dimensions[1], "Matrix should have 2 columns");

    // Convert back to a nested array to verify values
    XVType[][] nestedArray = (XVType[][]) matrix.nestedArrayValue();
    assertEquals(9.0, nestedArray[0][0].getX(), "Matrix[0][0] x value should match");
    assertEquals(10.0f, nestedArray[0][0].getValue(), "Matrix[0][0] value should match");
    assertEquals(11.0, nestedArray[0][1].getX(), "Matrix[0][1] x value should match");
    assertEquals(12.0f, nestedArray[0][1].getValue(), "Matrix[0][1] value should match");
    assertEquals(13.0, nestedArray[1][0].getX(), "Matrix[1][0] x value should match");
    assertEquals(14.0f, nestedArray[1][0].getValue(), "Matrix[1][0] value should match");
    assertEquals(15.0, nestedArray[1][1].getX(), "Matrix[1][1] x value should match");
    assertEquals(16.0f, nestedArray[1][1].getValue(), "Matrix[1][1] value should match");
  }

  // UaNode.isRolePermissionsWritable must dispatch through readAttribute(context, WriteMask) so a
  // UaNode subclass overriding readAttribute for WriteMask produces consistent results across
  // Read(WriteMask), Read(RolePermissions), and Write(RolePermissions).
  @Test
  public void subclassWriteMaskOverrideGovernsReadAndWriteRolePermissionsConsistently() {
    UInteger writableMask = UInteger.valueOf(WriteMask.RolePermissions.getValue());
    var overrideMode = new AtomicReference<>("return-null");

    UaObjectNode node =
        new UaObjectNode(
            nodeContext,
            new NodeId(1, "WriteMaskOverrideObject"),
            new QualifiedName(1, "WriteMaskOverrideObject"),
            LocalizedText.english("WriteMaskOverrideObject"),
            LocalizedText.NULL_VALUE,
            writableMask,
            writableMask,
            ubyte(0)) {
          @Override
          public @Nullable Object readAttribute(AccessContext context, AttributeId attributeId)
              throws UaException {
            if (attributeId == AttributeId.WriteMask) {
              return switch (overrideMode.get()) {
                case "return-null" -> null;
                case "clear-bit" -> UInteger.MIN;
                case "set-bit" -> writableMask;
                case "fail-attr-invalid" ->
                    throw new UaException(StatusCodes.Bad_AttributeIdInvalid);
                case "fail-internal" -> throw new UaException(StatusCodes.Bad_InternalError);
                case "malformed" -> "not-a-mask";
                default -> super.readAttribute(context, attributeId);
              };
            }
            return super.readAttribute(context, attributeId);
          }
        };

    Session session = Mockito.mock(Session.class);
    AccessContext context = () -> Optional.of(session);
    var customPolicy =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_SecurityAdmin,
              PermissionType.of(
                  PermissionType.Field.Browse,
                  PermissionType.Field.Read,
                  PermissionType.Field.ReadRolePermissions,
                  PermissionType.Field.WriteRolePermissions))
        };

    // 1. Stored WriteMask retains RolePermissions, but subclass returns null for WriteMask:
    // Read(WriteMask) and Read(RolePermissions) both report Bad_AttributeIdInvalid and
    // Write(RolePermissions) reports Bad_NotWritable.
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        AttributeReader.readAttribute(
                context, node, AttributeId.WriteMask, TimestampsToReturn.Neither, null, null)
            .statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        AttributeReader.readAttribute(
                context, node, AttributeId.RolePermissions, TimestampsToReturn.Neither, null, null)
            .statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_NotWritable),
        AttributeWriter.writeAttribute(
            context,
            node,
            AttributeId.RolePermissions,
            DataValue.valueOnly(new Variant(customPolicy)),
            null));

    // 2. Stored WriteMask retains RolePermissions, but subclass clears the bit (UInteger.MIN):
    // Read(WriteMask) returns 0, Read(RolePermissions) reports Bad_AttributeIdInvalid, and
    // Write(RolePermissions) reports Bad_NotWritable.
    overrideMode.set("clear-bit");
    DataValue clearedMask =
        AttributeReader.readAttribute(
            context, node, AttributeId.WriteMask, TimestampsToReturn.Neither, null, null);
    assertEquals(StatusCode.GOOD, clearedMask.statusCode());
    assertEquals(UInteger.MIN, clearedMask.value().value());
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        AttributeReader.readAttribute(
                context, node, AttributeId.RolePermissions, TimestampsToReturn.Neither, null, null)
            .statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_NotWritable),
        AttributeWriter.writeAttribute(
            context,
            node,
            AttributeId.RolePermissions,
            DataValue.valueOnly(new Variant(customPolicy)),
            null));

    // 3. Stored WriteMask is 0, while subclass sets WriteMask.RolePermissions:
    // Read(WriteMask) returns writableMask, Read(RolePermissions) returns Good empty array, and
    // Write(RolePermissions) succeeds for both setting and clearing overrides.
    node.setWriteMask(UInteger.MIN);
    overrideMode.set("set-bit");
    DataValue setMask =
        AttributeReader.readAttribute(
            context, node, AttributeId.WriteMask, TimestampsToReturn.Neither, null, null);
    assertEquals(StatusCode.GOOD, setMask.statusCode());
    assertEquals(writableMask, setMask.value().value());

    DataValue emptyRp =
        AttributeReader.readAttribute(
            context, node, AttributeId.RolePermissions, TimestampsToReturn.Neither, null, null);
    assertEquals(StatusCode.GOOD, emptyRp.statusCode());
    assertArrayEquals(new RolePermissionType[0], (RolePermissionType[]) emptyRp.value().value());

    assertEquals(
        StatusCode.GOOD,
        AttributeWriter.writeAttribute(
            context,
            node,
            AttributeId.RolePermissions,
            DataValue.valueOnly(new Variant(customPolicy)),
            null));
    assertArrayEquals(customPolicy, node.getRolePermissions());

    assertEquals(
        StatusCode.GOOD,
        AttributeWriter.writeAttribute(
            context,
            node,
            AttributeId.RolePermissions,
            DataValue.valueOnly(new Variant(new RolePermissionType[0])),
            null));
    assertNull(node.getRolePermissions());

    DataValue revertedEmptyRp =
        AttributeReader.readAttribute(
            context, node, AttributeId.RolePermissions, TimestampsToReturn.Neither, null, null);
    assertEquals(StatusCode.GOOD, revertedEmptyRp.statusCode());
    assertArrayEquals(
        new RolePermissionType[0], (RolePermissionType[]) revertedEmptyRp.value().value());

    // 4. Subclass throwing Bad_AttributeIdInvalid treats WriteMask as optional/absent; other
    // failures and malformed types fail closed consistently across Read and Write.
    node.setWriteMask(writableMask);
    overrideMode.set("fail-attr-invalid");
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        AttributeReader.readAttribute(
                context, node, AttributeId.WriteMask, TimestampsToReturn.Neither, null, null)
            .statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        AttributeReader.readAttribute(
                context, node, AttributeId.RolePermissions, TimestampsToReturn.Neither, null, null)
            .statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_NotWritable),
        AttributeWriter.writeAttribute(
            context,
            node,
            AttributeId.RolePermissions,
            DataValue.valueOnly(new Variant(customPolicy)),
            null));

    overrideMode.set("fail-internal");
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        AttributeReader.readAttribute(
                context, node, AttributeId.RolePermissions, TimestampsToReturn.Neither, null, null)
            .statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        AttributeWriter.writeAttribute(
            context,
            node,
            AttributeId.RolePermissions,
            DataValue.valueOnly(new Variant(customPolicy)),
            null));

    overrideMode.set("malformed");
    assertEquals(
        new StatusCode(StatusCodes.Bad_TypeMismatch),
        AttributeReader.readAttribute(
                context, node, AttributeId.RolePermissions, TimestampsToReturn.Neither, null, null)
            .statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_TypeMismatch),
        AttributeWriter.writeAttribute(
            context,
            node,
            AttributeId.RolePermissions,
            DataValue.valueOnly(new Variant(customPolicy)),
            null));
  }

  // A UaNode subclass whose WriteMask override reentrantly reads RolePermissions on the same node
  // and Session while RolePermissions is null must fail with a defined Bad_InternalError instead of
  // StackOverflowError (both outside and inside RolePermissionsReadScope), and clean up its guard
  // so ordinary non-reentrant virtual overrides keep working afterwards.
  @Test
  public void reentrantWriteMaskRolePermissionsCycleFailsWithDefinedErrorInsteadOfStackOverflow() {
    UInteger writableMask = UInteger.valueOf(WriteMask.RolePermissions.getValue());
    var cyclicMode = new AtomicReference<>(true);

    UaObjectNode cyclicNode =
        new UaObjectNode(
            nodeContext,
            new NodeId(1, "CyclicWriteMaskNode"),
            new QualifiedName(1, "CyclicWriteMaskNode"),
            LocalizedText.english("CyclicWriteMaskNode"),
            LocalizedText.NULL_VALUE,
            writableMask,
            writableMask,
            ubyte(0)) {
          @Override
          public @Nullable Object readAttribute(AccessContext context, AttributeId attributeId)
              throws UaException {
            if (attributeId == AttributeId.WriteMask) {
              if (cyclicMode.get()) {
                // Reentrantly read RolePermissions on the same node and context.
                readAttribute(context, AttributeId.RolePermissions);
              }
              return writableMask;
            }
            return super.readAttribute(context, attributeId);
          }
        };

    Session session = Mockito.mock(Session.class);
    AccessContext context = () -> Optional.of(session);

    // 1. Outside RolePermissionsReadScope: both Read(RolePermissions) and Read(WriteMask) fail
    // cleanly with Bad_InternalError instead of overflowing the stack.
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        AttributeReader.readAttribute(
                context,
                cyclicNode,
                AttributeId.RolePermissions,
                TimestampsToReturn.Neither,
                null,
                null)
            .statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        AttributeReader.readAttribute(
                context, cyclicNode, AttributeId.WriteMask, TimestampsToReturn.Neither, null, null)
            .statusCode());

    // 2. Inside RolePermissionsReadScope (where cached null RolePermissions still enters
    // isRolePermissionsWritable): both directions still fail cleanly with Bad_InternalError.
    try (RolePermissionsReadScope ignored = RolePermissionsReadScope.open(session)) {
      assertEquals(
          new StatusCode(StatusCodes.Bad_InternalError),
          AttributeReader.readAttribute(
                  context,
                  cyclicNode,
                  AttributeId.RolePermissions,
                  TimestampsToReturn.Neither,
                  null,
                  null)
              .statusCode());
    }
    try (RolePermissionsReadScope ignored = RolePermissionsReadScope.open(session)) {
      assertEquals(
          new StatusCode(StatusCodes.Bad_InternalError),
          AttributeReader.readAttribute(
                  context,
                  cyclicNode,
                  AttributeId.WriteMask,
                  TimestampsToReturn.Neither,
                  null,
                  null)
              .statusCode());
    }

    // 3. After unwinding, the ThreadLocal guard is clean and ordinary virtual dispatch succeeds.
    cyclicMode.set(false);
    DataValue recovered =
        AttributeReader.readAttribute(
            context,
            cyclicNode,
            AttributeId.RolePermissions,
            TimestampsToReturn.Neither,
            null,
            null);
    assertEquals(StatusCode.GOOD, recovered.statusCode());
    assertArrayEquals(new RolePermissionType[0], (RolePermissionType[]) recovered.value().value());

    // 4. Non-reentrant cross-node dependency (Node A's WriteMask reads Node B's RolePermissions)
    // is not treated as a cycle and dispatches virtually on both nodes.
    UaObjectNode delegatingNode =
        new UaObjectNode(
            nodeContext,
            new NodeId(1, "CrossNodeWriteMaskNode"),
            new QualifiedName(1, "CrossNodeWriteMaskNode"),
            LocalizedText.english("CrossNodeWriteMaskNode"),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            writableMask,
            ubyte(0)) {
          @Override
          public @Nullable Object readAttribute(AccessContext context, AttributeId attributeId)
              throws UaException {
            if (attributeId == AttributeId.WriteMask) {
              Object otherRp = cyclicNode.readAttribute(context, AttributeId.RolePermissions);
              return otherRp != null ? writableMask : UInteger.MIN;
            }
            return super.readAttribute(context, attributeId);
          }
        };

    DataValue crossNodeResult =
        AttributeReader.readAttribute(
            context,
            delegatingNode,
            AttributeId.RolePermissions,
            TimestampsToReturn.Neither,
            null,
            null);
    assertEquals(StatusCode.GOOD, crossNodeResult.statusCode());
    assertArrayEquals(
        new RolePermissionType[0], (RolePermissionType[]) crossNodeResult.value().value());
  }

  // AttributeWriter must continue honoring virtual getWriteMask() overrides on UaNode subclasses
  // for unrelated attribute writes (such as Description and VariableType Value) even when the
  // backing WriteMask and UserWriteMask fields still have the write bit set.
  @Test
  public void attributeWriterRespectsGetWriteMaskOverrideForUnrelatedAttributes() {
    UInteger descriptionMask = UInteger.valueOf(WriteMask.Description.getValue());
    var overrideDescriptionMask = new AtomicReference<>(UInteger.MIN);

    UaObjectNode objectNode =
        new UaObjectNode(
            nodeContext,
            new NodeId(1, "VirtualWriteMaskObject"),
            new QualifiedName(1, "VirtualWriteMaskObject"),
            LocalizedText.english("VirtualWriteMaskObject"),
            LocalizedText.english("initial"),
            descriptionMask,
            descriptionMask,
            ubyte(0)) {
          @Override
          public UInteger getWriteMask() {
            return overrideDescriptionMask.get();
          }
        };

    Session session = Mockito.mock(Session.class);
    AccessContext context = () -> Optional.of(session);

    // Description write rejected while virtual getWriteMask() clears WriteMask.Description.
    assertEquals(
        new StatusCode(StatusCodes.Bad_NotWritable),
        AttributeWriter.writeAttribute(
            context,
            objectNode,
            AttributeId.Description,
            DataValue.valueOnly(new Variant(LocalizedText.english("blocked"))),
            null));
    assertEquals(LocalizedText.english("initial"), objectNode.getDescription());

    // Control: once virtual getWriteMask() includes Description, the write succeeds.
    overrideDescriptionMask.set(descriptionMask);
    assertEquals(
        StatusCode.GOOD,
        AttributeWriter.writeAttribute(
            context,
            objectNode,
            AttributeId.Description,
            DataValue.valueOnly(new Variant(LocalizedText.english("updated"))),
            null));
    assertEquals(LocalizedText.english("updated"), objectNode.getDescription());

    // VariableType Value write rejected while virtual getWriteMask() clears ValueForVariableType.
    UInteger varTypeValueMask = UInteger.valueOf(WriteMask.ValueForVariableType.getValue());
    var overrideVarTypeMask = new AtomicReference<>(UInteger.MIN);

    UaVariableTypeNode variableTypeNode =
        new UaVariableTypeNode(
            nodeContext,
            new NodeId(1, "VirtualWriteMaskVariableType"),
            new QualifiedName(1, "VirtualWriteMaskVariableType"),
            LocalizedText.english("VirtualWriteMaskVariableType"),
            LocalizedText.NULL_VALUE,
            varTypeValueMask,
            varTypeValueMask,
            DataValue.valueOnly(new Variant(1)),
            NodeIds.Int32,
            ValueRanks.Scalar,
            null,
            false) {
          @Override
          public UInteger getWriteMask() {
            return overrideVarTypeMask.get();
          }
        };

    assertEquals(
        new StatusCode(StatusCodes.Bad_NotWritable),
        AttributeWriter.writeAttribute(
            context,
            variableTypeNode,
            AttributeId.Value,
            DataValue.valueOnly(new Variant(42)),
            null));
    assertEquals(1, variableTypeNode.getValue().value().value());

    // Control: once virtual getWriteMask() includes ValueForVariableType, the write succeeds.
    DataTypeTree dataTypeTree = Mockito.mock(DataTypeTree.class);
    Mockito.when(dataTypeTree.containsType(NodeIds.Int32)).thenReturn(true);
    Mockito.doReturn(Integer.class).when(dataTypeTree).getBackingClass(NodeIds.Int32);
    Mockito.when(dataTypeTree.isAssignable(NodeIds.Int32, Integer.class)).thenReturn(true);
    Mockito.when(server.getDataTypeTree()).thenReturn(dataTypeTree);

    overrideVarTypeMask.set(varTypeValueMask);
    assertEquals(
        StatusCode.GOOD,
        AttributeWriter.writeAttribute(
            context,
            variableTypeNode,
            AttributeId.Value,
            DataValue.valueOnly(new Variant(42)),
            null));
    assertEquals(42, variableTypeNode.getValue().value().value());
  }

  // Direct session-scoped UaNode.readAttribute(context, RolePermissions) on a fallback-governed
  // node must honor explicit UserRolePermissions restrictions even when RolePermissions itself is
  // populated (for example granting Anonymous ReadRolePermissions), and must fail closed on a
  // cyclic UserRolePermissions -> RolePermissions filter.
  @Test
  public void directReadRolePermissionsHonorsExplicitUserRolePermissionsWhenPopulated()
      throws UaException {
    var anonymousReadRolePermissions =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_Anonymous,
              PermissionType.of(PermissionType.Field.ReadRolePermissions))
        };
    var adminOnlyReadRolePermissions =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_SecurityAdmin,
              PermissionType.of(PermissionType.Field.ReadRolePermissions))
        };

    UaObjectNode node =
        UaObjectNode.build(
            nodeContext,
            b ->
                b.setNodeId(new NodeId(1, "ExplicitUrpDirectReadObject"))
                    .setBrowseName(new QualifiedName(1, "ExplicitUrpDirectReadObject"))
                    .setDisplayName(LocalizedText.english("ExplicitUrpDirectReadObject"))
                    .setTypeDefinition(NodeIds.BaseObjectType)
                    .build());
    node.setRolePermissions(anonymousReadRolePermissions);
    node.getFilterChain().addLast(RolePermissionsSupport.derivedUserRolePermissionsFilter());

    Session anonymousSession = Mockito.mock(Session.class);
    Mockito.when(anonymousSession.getRoleIds()).thenReturn(Optional.empty());
    AccessContext anonymousContext = () -> Optional.of(anonymousSession);

    Session adminSession = Mockito.mock(Session.class);
    Mockito.when(adminSession.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));
    AccessContext adminContext = () -> Optional.of(adminSession);

    // Baseline: no explicit UserRolePermissions -> Anonymous.ReadRolePermissions grants direct
    // read.
    assertArrayEquals(
        anonymousReadRolePermissions,
        (RolePermissionType[]) node.readAttribute(anonymousContext, AttributeId.RolePermissions));

    // Explicit empty UserRolePermissions denies direct read even though RolePermissions grants
    // Anonymous.ReadRolePermissions.
    node.setUserRolePermissions(new RolePermissionType[0]);
    UaException emptyEx =
        assertThrows(
            UaException.class,
            () -> node.readAttribute(anonymousContext, AttributeId.RolePermissions));
    assertEquals(StatusCodes.Bad_UserAccessDenied, emptyEx.getStatusCode().getValue());

    // Restricting UserRolePermissions to SecurityAdmin denies anonymous while allowing
    // SecurityAdmin.
    node.setUserRolePermissions(adminOnlyReadRolePermissions);
    UaException restrictedEx =
        assertThrows(
            UaException.class,
            () -> node.readAttribute(anonymousContext, AttributeId.RolePermissions));
    assertEquals(StatusCodes.Bad_UserAccessDenied, restrictedEx.getStatusCode().getValue());
    assertArrayEquals(
        anonymousReadRolePermissions,
        (RolePermissionType[]) node.readAttribute(adminContext, AttributeId.RolePermissions));

    // Clearing explicit UserRolePermissions restores direct read; a downstream narrowing filter on
    // UserRolePermissions denies it again, and a cyclic filter fails closed without recursing.
    node.setUserRolePermissions(null);
    assertArrayEquals(
        anonymousReadRolePermissions,
        (RolePermissionType[]) node.readAttribute(anonymousContext, AttributeId.RolePermissions));

    var downstreamNarrow = new AtomicBoolean(true);
    var cyclic = new AtomicBoolean(false);
    node.getFilterChain()
        .addLast(
            new AttributeFilter() {
              @Override
              public @Nullable Object readAttribute(
                  AttributeFilterContext ctx, AttributeId attributeId) throws UaException {
                if (attributeId == AttributeId.UserRolePermissions) {
                  if (cyclic.get()) {
                    return node.readAttribute(anonymousContext, AttributeId.RolePermissions);
                  }
                  if (downstreamNarrow.get()) {
                    return new RolePermissionType[0];
                  }
                }
                return ctx.readAttribute(attributeId);
              }
            });

    UaException narrowedEx =
        assertThrows(
            UaException.class,
            () -> node.readAttribute(anonymousContext, AttributeId.RolePermissions));
    assertEquals(StatusCodes.Bad_UserAccessDenied, narrowedEx.getStatusCode().getValue());

    downstreamNarrow.set(false);
    cyclic.set(true);
    UaException cyclicEx =
        assertThrows(
            UaException.class,
            () -> node.readAttribute(anonymousContext, AttributeId.RolePermissions));
    assertEquals(StatusCodes.Bad_UserAccessDenied, cyclicEx.getStatusCode().getValue());
  }
}
