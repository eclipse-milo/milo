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

import static java.util.Objects.requireNonNullElse;

import java.util.Optional;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.NumericRange;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.WriteMask;
import org.eclipse.milo.opcua.sdk.core.nodes.DataTypeNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaServerNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
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
      // map Sessions to Roles. This mapping may be vendor specific, or it may use the
      // standard Role model defined in 4.8.
      //
      // This Attribute shall not be writeable.
      return new StatusCode(StatusCodes.Bad_NotWritable);
    } else if (attributeId != AttributeId.Value) {
      WriteMask writeMask = writeMaskForAttribute(attributeId);

      Set<WriteMask> writeMasks = WriteMask.fromMask(node.getWriteMask());
      if (!writeMasks.contains(writeMask)) {
        return new StatusCode(StatusCodes.Bad_NotWritable);
      }

      Set<WriteMask> userWriteMasks =
          WriteMask.fromMask(
              (UInteger)
                  requireNonNullElse(
                      node.getAttribute(context, AttributeId.UserWriteMask), UInteger.MIN));
      if (!userWriteMasks.contains(writeMask)) {
        return new StatusCode(StatusCodes.Bad_UserAccessDenied);
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

  private static WriteMask writeMaskForAttribute(AttributeId attributeId) {
    return switch (attributeId) {
      case AccessLevel -> WriteMask.AccessLevel;
      case ArrayDimensions -> WriteMask.ArrayDimensions;
      case BrowseName -> WriteMask.BrowseName;
      case ContainsNoLoops -> WriteMask.ContainsNoLoops;
      case DataType -> WriteMask.DataType;
      case Description -> WriteMask.Description;
      case DisplayName -> WriteMask.DisplayName;
      case EventNotifier -> WriteMask.EventNotifier;
      case Executable -> WriteMask.Executable;
      case Historizing -> WriteMask.Historizing;
      case InverseName -> WriteMask.InverseName;
      case IsAbstract -> WriteMask.IsAbstract;
      case MinimumSamplingInterval -> WriteMask.MinimumSamplingInterval;
      case NodeClass -> WriteMask.NodeClass;
      case NodeId -> WriteMask.NodeId;
      case Symmetric -> WriteMask.Symmetric;
      case UserAccessLevel -> WriteMask.UserAccessLevel;
      case UserExecutable -> WriteMask.UserExecutable;
      case UserWriteMask -> WriteMask.UserWriteMask;
      case Value -> WriteMask.ValueForVariableType;
      case ValueRank -> WriteMask.ValueRank;
      case WriteMask -> WriteMask.WriteMask;
      case RolePermissions -> WriteMask.RolePermissions;
      case AccessRestrictions -> WriteMask.AccessRestrictions;
      case AccessLevelEx -> WriteMask.AccessLevelEx;
      case DataTypeDefinition -> WriteMask.DataTypeDefinition;
      default -> throw new IllegalArgumentException("unknown AttributeId: " + attributeId);
    };
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

    Class<?> expectedClass = getExpectedClass(server, dataType, valueClass);

    if (expectedClass != null) {
      LOGGER.debug(
          "dataTypeId={}, valueClass={}, expectedClass={}",
          dataType,
          valueClass.getSimpleName(),
          expectedClass.getSimpleName());

      if (!expectedClass.isAssignableFrom(valueClass)) {
        // Writing a ByteString to a UByte[] is explicitly allowed by the spec.
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
    } else {
      throw new UaException(StatusCodes.Bad_TypeMismatch);
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

  private static Class<?> getExpectedClass(
      OpcUaServer server, NodeId dataTypeId, Class<?> valueClass) throws UaException {

    if (OpcUaDataType.isBuiltin(dataTypeId)) {
      return OpcUaDataType.getBackingClass(dataTypeId);
    } else if (subtypeOf(server, dataTypeId, NodeIds.Structure)) {
      return ExtensionObject.class;
    } else if (subtypeOf(server, dataTypeId, NodeIds.Enumeration)) {
      return Integer.class;
    } else {
      NodeId superBuiltInType = findConcreteBuiltInSuperTypeId(server, dataTypeId);

      if (superBuiltInType != null) {
        // One of dataTypeId's supertypes is a concrete built-in
        // type; expect the same Class<?> as that built-in type.
        return OpcUaDataType.getBackingClass(superBuiltInType);
      } else {
        int valueDataTypeId = OpcUaDataType.getBuiltinTypeId(valueClass);

        if (valueDataTypeId > -1) {
          // The value they sent us maps to a built-in type.
          // If dataTypeId is a subtype of that built-in type,
          // the expected class is the class of the value they sent.
          NodeId builtInTypeId = new NodeId(0, valueDataTypeId);

          if (dataTypeId.equals(builtInTypeId) || subtypeOf(server, builtInTypeId, dataTypeId)) {

            return valueClass;
          } else {
            throw new UaException(StatusCodes.Bad_TypeMismatch);
          }
        } else {
          throw new UaException(StatusCodes.Bad_TypeMismatch);
        }
      }
    }
  }

  /**
   * @return {@code true} if {@code dataTypeId} is a subtype of {@code potentialSuperTypeId}.
   */
  private static boolean subtypeOf(
      OpcUaServer server, NodeId dataTypeId, NodeId potentialSuperTypeId) {
    UaNode dataTypeNode = server.getAddressSpaceManager().getManagedNode(dataTypeId).orElse(null);

    if (dataTypeNode != null) {
      NodeId superTypeId = getSuperTypeId(server, dataTypeId);

      if (superTypeId != null) {
        return superTypeId.equals(potentialSuperTypeId)
            || subtypeOf(server, superTypeId, potentialSuperTypeId);
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * Find the first concrete built-in supertype for {@code dataTypeId}, if one exists.
   *
   * @return the first concrete built-in supertype for {@code dataTypeId}, if one exists.
   */
  @Nullable
  private static NodeId findConcreteBuiltInSuperTypeId(OpcUaServer server, NodeId dataTypeId) {
    if (OpcUaDataType.isBuiltin(dataTypeId) && isConcrete(server, dataTypeId)) {
      return dataTypeId;
    } else {
      NodeId superTypeId = getSuperTypeId(server, dataTypeId);

      if (superTypeId != null) {
        return findConcreteBuiltInSuperTypeId(server, superTypeId);
      } else {
        return null;
      }
    }
  }

  @Nullable
  private static NodeId getSuperTypeId(OpcUaServer server, NodeId dataTypeId) {
    UaNode dataTypeNode = server.getAddressSpaceManager().getManagedNode(dataTypeId).orElse(null);

    if (dataTypeNode != null) {
      return dataTypeNode.getReferences().stream()
          .filter(Reference.SUBTYPE_OF)
          .flatMap(r -> r.getTargetNodeId().toNodeId(server.getNamespaceTable()).stream())
          .findFirst()
          .orElse(null);
    } else {
      return null;
    }
  }

  private static boolean isAbstract(OpcUaServer server, NodeId dataTypeId) {
    UaNode node = server.getAddressSpaceManager().getManagedNode(dataTypeId).orElse(null);

    if (node instanceof DataTypeNode) {
      return ((DataTypeNode) node).getIsAbstract();
    } else {
      return false;
    }
  }

  private static boolean isConcrete(OpcUaServer server, NodeId dataTypeId) {
    return !isAbstract(server, dataTypeId);
  }
}
