/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.api.util;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.WriteMask;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.AttributeWriter;
import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaServerNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AttributeWriterTest extends AbstractClientServerTest {

  @Test
  void writeNullAllowed() throws Exception {
    StatusCode statusCode =
        client
            .writeValues(
                List.of(new NodeId(2, "AllowNulls")),
                List.of(DataValue.valueOnly(Variant.NULL_VALUE)))
            .get(0);

    assertEquals(StatusCode.GOOD, statusCode);
  }

  @Test
  void writeNullDisallowed() throws Exception {
    StatusCode statusCode =
        client
            .writeValues(
                List.of(new NodeId(2, "DisallowNulls")),
                List.of(DataValue.valueOnly(Variant.NULL_VALUE)))
            .get(0);

    assertEquals(new StatusCode(StatusCodes.Bad_TypeMismatch), statusCode);
  }

  @Test
  void writeNullNotConfigured() throws Exception {
    // Default behavior when AllowNulls property is not configured is to reject null values.
    StatusCode statusCode =
        client
            .writeValues(
                List.of(new NodeId(2, "AllowNullsNotConfigured")),
                List.of(DataValue.valueOnly(Variant.NULL_VALUE)))
            .get(0);

    assertEquals(new StatusCode(StatusCodes.Bad_TypeMismatch), statusCode);
  }

  @Test
  void writeByteStringToUByteArray() throws Exception {
    StatusCode statusCode =
        client
            .writeValues(
                List.of(new NodeId(2, "UByteArray")),
                List.of(DataValue.valueOnly(new Variant(ByteString.of(new byte[] {1, 2, 3})))))
            .get(0);

    assertEquals(StatusCode.GOOD, statusCode);
  }

  @Test
  void writeUnsignedIntegerSubtypesToAbstractUInteger() throws Exception {
    for (Object unsignedValue : List.of(ubyte(1), ushort(2), uint(3), ulong(4))) {
      StatusCode statusCode =
          client
              .writeValues(
                  List.of(new NodeId(2, "AbstractUInteger")),
                  List.of(DataValue.valueOnly(new Variant(unsignedValue))))
              .get(0);

      assertEquals(StatusCode.GOOD, statusCode);
    }
  }

  @Test
  void rejectSignedIntegerSubtypesForAbstractUInteger() throws Exception {
    for (Object signedValue : List.of((byte) 1, (short) 2, 3, 4L)) {
      StatusCode statusCode =
          client
              .writeValues(
                  List.of(new NodeId(2, "AbstractUInteger")),
                  List.of(DataValue.valueOnly(new Variant(signedValue))))
              .get(0);

      assertEquals(new StatusCode(StatusCodes.Bad_TypeMismatch), statusCode);
    }
  }

  @Test
  void writeSignedIntegerSubtypesToAbstractInteger() throws Exception {
    for (Object signedValue : List.of((byte) 1, (short) 2, 3, 4L)) {
      StatusCode statusCode =
          client
              .writeValues(
                  List.of(new NodeId(2, "AbstractInteger")),
                  List.of(DataValue.valueOnly(new Variant(signedValue))))
              .get(0);

      assertEquals(StatusCode.GOOD, statusCode);
    }
  }

  @Test
  void rejectUnsignedIntegerSubtypesForAbstractInteger() throws Exception {
    for (Object unsignedValue : List.of(ubyte(1), ushort(2), uint(3), ulong(4))) {
      StatusCode statusCode =
          client
              .writeValues(
                  List.of(new NodeId(2, "AbstractInteger")),
                  List.of(DataValue.valueOnly(new Variant(unsignedValue))))
              .get(0);

      assertEquals(new StatusCode(StatusCodes.Bad_TypeMismatch), statusCode);
    }
  }

  @Test
  void writeNumericSubtypesToAbstractNumber() {
    UaServerNode node = getManagedNode("AbstractNumber");

    for (Object number :
        List.of(1.0f, 2.0d, (byte) 3, (short) 4, 5, 6L, ubyte(7), ushort(8), uint(9), ulong(10))) {
      StatusCode statusCode =
          AttributeWriter.writeAttribute(
              AccessContext.INTERNAL,
              node,
              AttributeId.Value,
              DataValue.valueOnly(new Variant(number)),
              null);

      assertEquals(StatusCode.GOOD, statusCode);
    }
  }

  @Test
  void rejectNonBuiltinNumberSubtypesForAbstractNumber() {
    UaServerNode node = getManagedNode("AbstractNumber");

    for (Number number : List.of(BigInteger.ONE, BigDecimal.ONE, new AtomicInteger(1))) {
      StatusCode statusCode =
          AttributeWriter.writeAttribute(
              AccessContext.INTERNAL,
              node,
              AttributeId.Value,
              DataValue.valueOnly(new Variant(number)),
              null);

      assertEquals(new StatusCode(StatusCodes.Bad_TypeMismatch), statusCode);
    }
  }

  @Test
  void writeBuiltinValueToBaseDataType() {
    UaServerNode node = getManagedNode("BaseDataType");

    StatusCode statusCode =
        AttributeWriter.writeAttribute(
            AccessContext.INTERNAL,
            node,
            AttributeId.Value,
            DataValue.valueOnly(new Variant("anything")),
            null);

    assertEquals(StatusCode.GOOD, statusCode);
  }

  @Test
  void writeAnythingToBaseDataType() {
    UaServerNode node = getManagedNode("VariantDataType");

    StatusCode statusCode =
        AttributeWriter.writeAttribute(
            AccessContext.INTERNAL,
            node,
            AttributeId.Value,
            DataValue.valueOnly(new Variant(BigDecimal.ONE)),
            null);

    assertEquals(StatusCode.GOOD, statusCode);
  }

  @Test
  void writeIntegerToEnumerationSubtype() throws Exception {
    StatusCode statusCode =
        client
            .writeValues(
                List.of(new NodeId(2, "ApplicationTypeEnum")),
                List.of(DataValue.valueOnly(new Variant(1))))
            .get(0);

    assertEquals(StatusCode.GOOD, statusCode);
  }

  @Test
  void rejectNonIntegerForEnumerationSubtype() throws Exception {
    StatusCode statusCode =
        client
            .writeValues(
                List.of(new NodeId(2, "ApplicationTypeEnum")),
                List.of(DataValue.valueOnly(new Variant("not an enum"))))
            .get(0);

    assertEquals(new StatusCode(StatusCodes.Bad_TypeMismatch), statusCode);
  }

  @Test
  void writeCustomDataTypeAddedAfterDataTypeTreeIsBuilt() {
    server.getDataTypeTree();

    NodeId dataTypeId = new NodeId(2, "LateUInt32Subtype");

    testNamespace.configure(
        (context, nodeManager) -> {
          var dataTypeNode =
              new UaDataTypeNode(
                  context,
                  dataTypeId,
                  new QualifiedName(2, "LateUInt32Subtype"),
                  LocalizedText.english("LateUInt32Subtype"),
                  LocalizedText.NULL_VALUE,
                  uint(0),
                  uint(0),
                  false);

          dataTypeNode.addReference(
              new Reference(
                  dataTypeId,
                  NodeIds.HasSubtype,
                  NodeIds.UInt32.expanded(),
                  Reference.Direction.INVERSE));

          nodeManager.addNode(dataTypeNode);

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "LateUInt32SubtypeValue"));
                b.setBrowseName(new QualifiedName(2, "LateUInt32SubtypeValue"));
                b.setDisplayName(LocalizedText.english("LateUInt32SubtypeValue"));
                b.setDataType(dataTypeId);
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });
        });

    StatusCode statusCode =
        AttributeWriter.writeAttribute(
            AccessContext.INTERNAL,
            getManagedNode("LateUInt32SubtypeValue"),
            AttributeId.Value,
            DataValue.valueOnly(new Variant(uint(42))),
            null);

    assertEquals(StatusCode.GOOD, statusCode);
  }

  @Test
  void writeValue_VariableNode_SuccessAndFailure() throws Exception {
    StatusCode ok =
        client
            .writeValues(
                List.of(new NodeId(2, "ReadWriteVariable")),
                List.of(DataValue.valueOnly(new Variant(42))))
            .get(0);
    assertEquals(StatusCode.GOOD, ok);

    StatusCode fail =
        client
            .writeValues(
                List.of(new NodeId(2, "ReadOnlyVariable")),
                List.of(DataValue.valueOnly(new Variant(7))))
            .get(0);
    assertEquals(new StatusCode(StatusCodes.Bad_NotWritable), fail);
  }

  @Test
  void writeValue_VariableTypeNode_SuccessAndFailure() throws Exception {
    StatusCode ok =
        client
            .writeValues(
                List.of(new NodeId(2, "ReadWriteVariableType")),
                List.of(DataValue.valueOnly(new Variant(123))))
            .get(0);
    assertEquals(StatusCode.GOOD, ok);

    StatusCode fail =
        client
            .writeValues(
                List.of(new NodeId(2, "ReadOnlyVariableType")),
                List.of(DataValue.valueOnly(new Variant(456))))
            .get(0);
    assertEquals(new StatusCode(StatusCodes.Bad_NotWritable), fail);
  }

  @Test
  void writeNonValueAttributeUnwrapsDataValue() throws Exception {
    LocalizedText displayName = LocalizedText.english("UpdatedDisplayName");

    StatusCode statusCode =
        client
            .writeAsync(
                List.of(
                    new WriteValue(
                        new NodeId(2, "WritableDisplayName"),
                        AttributeId.DisplayName.uid(),
                        null,
                        DataValue.valueOnly(new Variant(displayName)))))
            .get(5, TimeUnit.SECONDS)
            .getResults()[0];

    assertEquals(StatusCode.GOOD, statusCode);
    assertEquals(
        displayName,
        getManagedNode("WritableDisplayName")
            .getAttribute(AccessContext.INTERNAL, AttributeId.DisplayName));
  }

  @Test
  void rejectNonValueAttributeTypeMismatch() throws Exception {
    StatusCode statusCode =
        client
            .writeAsync(
                List.of(
                    new WriteValue(
                        new NodeId(2, "WritableDisplayName"),
                        AttributeId.DisplayName.uid(),
                        null,
                        DataValue.valueOnly(new Variant("not a LocalizedText")))))
            .get(5, TimeUnit.SECONDS)
            .getResults()[0];

    assertEquals(new StatusCode(StatusCodes.Bad_TypeMismatch), statusCode);
  }

  @Test
  void writeNonValueArrayAttributeUnwrapsDataValue() throws Exception {
    UInteger[] arrayDimensions = new UInteger[] {uint(3)};

    StatusCode statusCode =
        client
            .writeAsync(
                List.of(
                    new WriteValue(
                        new NodeId(2, "WritableArrayDimensions"),
                        AttributeId.ArrayDimensions.uid(),
                        null,
                        DataValue.valueOnly(new Variant(arrayDimensions)))))
            .get(5, TimeUnit.SECONDS)
            .getResults()[0];

    assertEquals(StatusCode.GOOD, statusCode);
    assertArrayEquals(
        arrayDimensions,
        (UInteger[])
            getManagedNode("WritableArrayDimensions")
                .getAttribute(AccessContext.INTERNAL, AttributeId.ArrayDimensions));
  }

  @Test
  void writeNonValueOptionSetAttributeConvertsBackingType() throws Exception {
    UInteger accessLevelEx = uint(3);

    StatusCode statusCode =
        client
            .writeAsync(
                List.of(
                    new WriteValue(
                        new NodeId(2, "WritableAccessLevelEx"),
                        AttributeId.AccessLevelEx.uid(),
                        null,
                        DataValue.valueOnly(new Variant(accessLevelEx)))))
            .get(5, TimeUnit.SECONDS)
            .getResults()[0];

    assertEquals(StatusCode.GOOD, statusCode);

    AccessLevelExType value =
        (AccessLevelExType)
            getManagedNode("WritableAccessLevelEx")
                .getAttribute(AccessContext.INTERNAL, AttributeId.AccessLevelEx);

    assertEquals(accessLevelEx, value.getValue());
  }

  @BeforeAll
  void configure() {
    testNamespace.configure(
        (context, nodeManager) -> {
          UaVariableNode allowNulls =
              UaVariableNode.build(
                  context,
                  b -> {
                    b.setNodeId(new NodeId(2, "AllowNulls"));
                    b.setBrowseName(new QualifiedName(2, "AllowNulls"));
                    b.setDisplayName(LocalizedText.english("AllowNulls"));
                    b.setDataType(NodeIds.String);
                    b.setAccessLevel(AccessLevel.READ_WRITE);
                    b.setUserAccessLevel(AccessLevel.READ_WRITE);
                    return b.buildAndAdd();
                  });

          allowNulls.setAllowNulls(true);

          UaVariableNode disallowNulls =
              UaVariableNode.build(
                  context,
                  b -> {
                    b.setNodeId(new NodeId(2, "DisallowNulls"));
                    b.setBrowseName(new QualifiedName(2, "DisallowNulls"));
                    b.setDisplayName(LocalizedText.english("DisallowNulls"));
                    b.setDataType(NodeIds.String);
                    b.setAccessLevel(AccessLevel.READ_WRITE);
                    b.setUserAccessLevel(AccessLevel.READ_WRITE);
                    return b.buildAndAdd();
                  });

          disallowNulls.setAllowNulls(false);

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "AllowNullsNotConfigured"));
                b.setBrowseName(new QualifiedName(2, "AllowNullsNotConfigured"));
                b.setDisplayName(LocalizedText.english("AllowNullsNotConfigured"));
                b.setDataType(NodeIds.String);
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "UByteArray"));
                b.setBrowseName(new QualifiedName(2, "UByteArray"));
                b.setDisplayName(LocalizedText.english("UByteArray"));
                b.setDataType(NodeIds.Byte);
                b.setValueRank(ValueRanks.OneDimension);
                b.setArrayDimensions(new UInteger[] {UInteger.valueOf(0)});
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "ReadWriteVariable"));
                b.setBrowseName(new QualifiedName(2, "ReadWriteVariable"));
                b.setDisplayName(LocalizedText.english("ReadWriteVariable"));
                b.setDataType(NodeIds.Int32);
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "WritableDisplayName"));
                b.setBrowseName(new QualifiedName(2, "WritableDisplayName"));
                b.setDisplayName(LocalizedText.english("WritableDisplayName"));
                b.setDataType(NodeIds.Int32);
                b.setWriteMask(UInteger.valueOf(WriteMask.DisplayName.getValue()));
                b.setUserWriteMask(UInteger.valueOf(WriteMask.DisplayName.getValue()));
                return b.buildAndAdd();
              });

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "WritableArrayDimensions"));
                b.setBrowseName(new QualifiedName(2, "WritableArrayDimensions"));
                b.setDisplayName(LocalizedText.english("WritableArrayDimensions"));
                b.setDataType(NodeIds.Int32);
                b.setValueRank(ValueRanks.OneDimension);
                b.setArrayDimensions(new UInteger[] {UInteger.valueOf(0)});
                b.setWriteMask(UInteger.valueOf(WriteMask.ArrayDimensions.getValue()));
                b.setUserWriteMask(UInteger.valueOf(WriteMask.ArrayDimensions.getValue()));
                return b.buildAndAdd();
              });

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "WritableAccessLevelEx"));
                b.setBrowseName(new QualifiedName(2, "WritableAccessLevelEx"));
                b.setDisplayName(LocalizedText.english("WritableAccessLevelEx"));
                b.setDataType(NodeIds.Int32);
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                b.setAccessLevelEx(new AccessLevelExType(uint(0)));
                b.setWriteMask(UInteger.valueOf(WriteMask.AccessLevelEx.getValue()));
                b.setUserWriteMask(UInteger.valueOf(WriteMask.AccessLevelEx.getValue()));
                return b.buildAndAdd();
              });

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "AbstractNumber"));
                b.setBrowseName(new QualifiedName(2, "AbstractNumber"));
                b.setDisplayName(LocalizedText.english("AbstractNumber"));
                b.setDataType(NodeIds.Number);
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "AbstractUInteger"));
                b.setBrowseName(new QualifiedName(2, "AbstractUInteger"));
                b.setDisplayName(LocalizedText.english("AbstractUInteger"));
                b.setDataType(NodeIds.UInteger);
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "AbstractInteger"));
                b.setBrowseName(new QualifiedName(2, "AbstractInteger"));
                b.setDisplayName(LocalizedText.english("AbstractInteger"));
                b.setDataType(NodeIds.Integer);
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "ReadOnlyVariable"));
                b.setBrowseName(new QualifiedName(2, "ReadOnlyVariable"));
                b.setDisplayName(LocalizedText.english("ReadOnlyVariable"));
                b.setDataType(NodeIds.Int32);
                b.setAccessLevel(AccessLevel.READ_ONLY);
                // allow the service call to reach AttributeWriter
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "ApplicationTypeEnum"));
                b.setBrowseName(new QualifiedName(2, "ApplicationTypeEnum"));
                b.setDisplayName(LocalizedText.english("ApplicationTypeEnum"));
                b.setDataType(NodeIds.ApplicationType);
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "BaseDataType"));
                b.setBrowseName(new QualifiedName(2, "BaseDataType"));
                b.setDisplayName(LocalizedText.english("BaseDataType"));
                b.setDataType(NodeIds.BaseDataType);
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });

          UaVariableNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "VariantDataType"));
                b.setBrowseName(new QualifiedName(2, "VariantDataType"));
                b.setDisplayName(LocalizedText.english("VariantDataType"));
                b.setDataType(OpcUaDataType.Variant.getNodeId());
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });

          UaVariableTypeNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "ReadWriteVariableType"));
                b.setBrowseName(new QualifiedName(2, "ReadWriteVariableType"));
                b.setDisplayName(LocalizedText.english("ReadWriteVariableType"));
                b.setDataType(NodeIds.Int32);
                b.setWriteMask(UInteger.valueOf(WriteMask.ValueForVariableType.getValue()));
                b.setUserWriteMask(UInteger.valueOf(WriteMask.ValueForVariableType.getValue()));
                return b.buildAndAdd();
              });

          UaVariableTypeNode.build(
              context,
              b -> {
                b.setNodeId(new NodeId(2, "ReadOnlyVariableType"));
                b.setBrowseName(new QualifiedName(2, "ReadOnlyVariableType"));
                b.setDisplayName(LocalizedText.english("ReadOnlyVariableType"));
                b.setDataType(NodeIds.Int32);
                b.setWriteMask(UInteger.valueOf(0));
                b.setUserWriteMask(UInteger.valueOf(0));
                return b.buildAndAdd();
              });
        });
  }

  private UaServerNode getManagedNode(String identifier) {
    return (UaServerNode)
        server.getAddressSpaceManager().getManagedNode(new NodeId(2, identifier)).orElseThrow();
  }
}
