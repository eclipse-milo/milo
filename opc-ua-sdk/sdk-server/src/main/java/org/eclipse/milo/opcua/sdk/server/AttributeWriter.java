/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.NumericRange;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.WriteMask;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableTypeNode;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.server.nodes.UaServerNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttributeWriter {

  private static final Logger LOGGER = LoggerFactory.getLogger(AttributeWriter.class);

  public static StatusCode writeAttribute(
      AccessContext context,
      UaServerNode node,
      UInteger attributeId,
      DataValue value,
      @Nullable String indexRange) {

    Optional<AttributeId> aid = AttributeId.from(attributeId);

    if (aid.isPresent()) {
      return writeAttribute(context, node, aid.get(), value, indexRange);
    } else {
      return new StatusCode(StatusCodes.Bad_AttributeIdInvalid);
    }
  }

  public static StatusCode writeAttribute(
      AccessContext context,
      UaServerNode node,
      AttributeId attributeId,
      DataValue value,
      @Nullable String indexRange) {

    if (!AttributeId.getAttributes(node.getNodeClass()).contains(attributeId)) {
      return new StatusCode(StatusCodes.Bad_AttributeIdInvalid);
    }

    if (attributeId == AttributeId.UserRolePermissions) {
      // Part 3, section 5.2.10
      // https://reference.opcfoundation.org/v104/Core/docs/Part3/5.2.10/
      // The value of this Attribute is derived from the rules used by the Server to
      // map Sessions to Roles. This mapping may be vendor-specific, or it may use the
      // standard Role model defined in 4.8.
      //
      // This Attribute shall not be writeable.
      return new StatusCode(StatusCodes.Bad_NotWritable);
    } else if (attributeId == AttributeId.Value) {
      if (node instanceof UaVariableNode variableNode) {
        AccessLevelExType accessLevelEx = variableNode.getAccessLevelEx();

        if (accessLevelEx != null) {
          if (!accessLevelEx.getCurrentWrite()) {
            return new StatusCode(StatusCodes.Bad_NotWritable);
          }
        } else {
          Set<AccessLevel> accessLevels = AccessLevel.fromValue(variableNode.getAccessLevel());
          if (!accessLevels.contains(AccessLevel.CurrentWrite)) {
            return new StatusCode(StatusCodes.Bad_NotWritable);
          }
        }
      } else if (node instanceof UaVariableTypeNode variableTypeNode) {
        EnumSet<WriteMask> writeMasks = WriteMask.fromMask(variableTypeNode.getWriteMask());

        if (!writeMasks.contains(WriteMask.ValueForVariableType)) {
          return new StatusCode(StatusCodes.Bad_NotWritable);
        }
      } else {
        return new StatusCode(StatusCodes.Bad_AttributeIdInvalid);
      }
    } else {
      // attributeId != AttributeId.Value && attributeId != AttributeId.UserRolePermissions
      WriteMask writeMask = WriteMask.forAttribute(attributeId);

      Set<WriteMask> writeMasks = WriteMask.fromMask(node.getWriteMask());
      if (!writeMasks.contains(writeMask)) {
        return new StatusCode(StatusCodes.Bad_NotWritable);
      }
    }

    Variant updateVariant = value.value();

    if (indexRange != null && !indexRange.isEmpty()) {
      try {
        NumericRange range = NumericRange.parse(indexRange);

        Object currentValue = node.getAttribute(AccessContext.INTERNAL, attributeId);
        if (currentValue instanceof DataValue dataValue) {
          currentValue = dataValue.value().value();
        }
        if (currentValue instanceof Matrix matrix) {
          currentValue = matrix.nestedArrayValue();
        }

        Object updateValue = updateVariant.value();
        if (updateValue instanceof Matrix matrix) {
          updateValue = matrix.nestedArrayValue();
        }

        Object valueAtRange = NumericRange.writeToValueAtRange(currentValue, updateValue, range);

        if (ArrayUtil.getValueRank(valueAtRange) > 1) {
          valueAtRange = new Matrix(valueAtRange);
        }

        updateVariant = new Variant(valueAtRange);
      } catch (UaException e) {
        return e.getStatusCode();
      }
    }

    DateTime sourceTime = value.sourceTime();
    DateTime serverTime = value.serverTime();

    value =
        new DataValue(
            updateVariant,
            value.statusCode(),
            (sourceTime == null || sourceTime.isNull()) ? DateTime.now() : sourceTime,
            (serverTime == null || serverTime.isNull()) ? DateTime.now() : serverTime);

    if (attributeId == AttributeId.Value) {
      try {
        NodeId dataTypeId;
        if (node instanceof VariableNode) {
          dataTypeId = ((VariableNode) node).getDataType();
        } else if (node instanceof VariableTypeNode) {
          dataTypeId = ((VariableTypeNode) node).getDataType();
        } else {
          dataTypeId = null;
        }

        if (dataTypeId != null) {
          boolean allowNulls = false;
          if (node instanceof UaVariableNode) {
            Boolean b = ((UaVariableNode) node).getAllowNulls();
            allowNulls = b != null ? b : false;
          }
          value =
              validateDataType(node.getNodeContext().getServer(), dataTypeId, value, allowNulls);
        }

        Integer valueRank;
        UInteger[] arrayDimensions;
        if (node instanceof VariableNode) {
          valueRank = ((VariableNode) node).getValueRank();
          arrayDimensions = ((VariableNode) node).getArrayDimensions();
        } else if (node instanceof VariableTypeNode) {
          valueRank = ((VariableTypeNode) node).getValueRank();
          arrayDimensions = ((VariableTypeNode) node).getArrayDimensions();
        } else {
          valueRank = 0;
          arrayDimensions = null;
        }

        if (valueRank > 0) {
          validateArrayType(valueRank, arrayDimensions, value);
        }
      } catch (UaException e) {
        return e.getStatusCode();
      }
    }

    try {
      node.writeAttribute(context, attributeId, value);
      return StatusCode.GOOD;
    } catch (UaException e) {
      return e.getStatusCode();
    }
  }

  private static DataValue validateDataType(
      OpcUaServer server, NodeId dataType, DataValue value, boolean allowNulls) throws UaException {

    Variant variant = value.value();

    Object o = variant.value();
    if (o == null) {
      if (allowNulls) {
        return value;
      } else {
        throw new UaException(StatusCodes.Bad_TypeMismatch);
      }
    }

    if (o instanceof Matrix matrix) {
      o = matrix.nestedArrayValue();
    }

    Class<?> valueClass = o.getClass().isArray() ? ArrayUtil.getType(o) : o.getClass();

    DataTypeTree dataTypeTree = server.getDataTypeTree();

    if (!dataTypeTree.containsType(dataType)) {
      dataTypeTree = server.updateDataTypeTree();

      if (!dataTypeTree.containsType(dataType)) {
        throw new UaException(StatusCodes.Bad_TypeMismatch);
      }
    }

    Class<?> expectedClass = dataTypeTree.getBackingClass(dataType);
    Optional<OpcUaDataType> valueDataType = variant.getDataType();

    // Part 4, 5.11.4.2: a written value must be the same type or a subtype of the
    // Attribute's DataType; for Value, that DataType is defined by the DataType Attribute.
    if (valueDataType.isEmpty() && expectedClass != Variant.class) {
      throw new UaException(StatusCodes.Bad_TypeMismatch);
    }

    if (expectedClass == Object.class && !dataType.equals(NodeIds.BaseDataType)) {
      NodeId valueDataTypeId = valueDataType.map(OpcUaDataType::getNodeId).orElse(null);

      if (!valueDataTypeId.equals(dataType)
          && !dataTypeTree.isSubtypeOf(valueDataTypeId, dataType)) {

        throw new UaException(StatusCodes.Bad_TypeMismatch);
      }
    }

    LOGGER.debug(
        "dataTypeId={}, valueClass={}, expectedClass={}, assignable={}",
        dataType,
        valueClass.getSimpleName(),
        expectedClass.getSimpleName(),
        dataTypeTree.isAssignable(dataType, valueClass));

    if (!dataTypeTree.isAssignable(dataType, valueClass)) {
      // Part 4, 5.11.4.2 and Part 6, 5.1.5: ByteString is structurally equivalent
      // to a one-dimensional Byte array and must be accepted when Byte[] is expected.
      if (o instanceof ByteString byteString && expectedClass == UByte.class) {
        return new DataValue(
            new Variant(byteString.uBytes()),
            value.statusCode(),
            value.sourceTime(),
            value.serverTime());
      } else if (expectedClass == Variant.class) {
        // Allow writing anything to a Variant
        return value;
      } else {
        throw new UaException(StatusCodes.Bad_TypeMismatch);
      }
    }

    return value;
  }

  private static void validateArrayType(
      Integer valueRank, UInteger[] arrayDimensions, DataValue value) throws UaException {

    Variant variant = value.value();

    Object o = variant.value();
    if (o == null) return;

    if (o instanceof Matrix matrix) {
      o = matrix.nestedArrayValue();
    }

    boolean valueIsArray = o.getClass().isArray();

    switch (valueRank) {
      case ValueRanks.ScalarOrOneDimension:
        if (valueIsArray) {
          int[] valueDimensions = ArrayUtil.getDimensions(o);

          if (valueDimensions.length > 1) {
            throw new UaException(StatusCodes.Bad_TypeMismatch);
          }
        }
        break;

      case ValueRanks.Any:
        break;

      case ValueRanks.Scalar:
        if (valueIsArray) {
          throw new UaException(StatusCodes.Bad_TypeMismatch);
        }
        break;

      case ValueRanks.OneOrMoreDimensions:
        if (!valueIsArray) {
          throw new UaException(StatusCodes.Bad_TypeMismatch);
        }
        break;

      case ValueRanks.OneDimension:
      default:
        if (!valueIsArray) {
          throw new UaException(StatusCodes.Bad_TypeMismatch);
        }

        int[] valueDimensions = ArrayUtil.getDimensions(o);

        if (valueDimensions.length != valueRank) {
          throw new UaException(StatusCodes.Bad_TypeMismatch);
        }

        int[] nodeDimensions =
            Optional.ofNullable(arrayDimensions)
                .map(
                    uia -> {
                      int[] dims = new int[uia.length];
                      for (int i = 0; i < uia.length; i++) {
                        dims[i] = uia[i].intValue();
                      }
                      return dims;
                    })
                .orElse(new int[0]);

        if (nodeDimensions.length > 0) {
          if (nodeDimensions.length != valueDimensions.length) {
            throw new UaException(StatusCodes.Bad_TypeMismatch);
          }

          for (int i = 0; i < nodeDimensions.length; i++) {
            if (nodeDimensions[i] > 0 && valueDimensions[i] > nodeDimensions[i]) {
              throw new UaException(StatusCodes.Bad_TypeMismatch);
            }
          }
        }
        break;
    }
  }
}
