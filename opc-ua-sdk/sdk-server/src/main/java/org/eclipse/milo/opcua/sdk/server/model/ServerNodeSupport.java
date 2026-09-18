package org.eclipse.milo.opcua.sdk.server.model;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.IntFunction;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UNumber;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.jspecify.annotations.Nullable;

/**
 * Shared implementation of this library's immediate-child contracts and typed-value accessors.
 *
 * <p>This source is emitted into the model library so generated nodes need no compiler runtime.
 * Milo supplies address-space lookup, type trees, attribute access and serialization. This class
 * applies the generated declarations' stricter presence, ambiguity and conversion rules.
 *
 * <p>Installed parents use the complete address space. Private trees use their own node manager,
 * even when their NodeIds match installed nodes. Constructors do not call this class. Lookups stay
 * live: adding or removing a child changes the next getter result.
 *
 * <p>Visibility permits calls across configured generated packages. Applications use the generated
 * node APIs instead of this implementation helper.
 */
public final class ServerNodeSupport {
  private ServerNodeSupport() {}

  /** Resolves a generated URI-qualified identity against this server's current namespace table. */
  public static NodeId resolve(NamespaceTable namespaces, ExpandedNodeId identity) {
    NodeId resolved = identity.toNodeId(namespaces).orElse(null);
    if (resolved == null) throw new IllegalArgumentException("namespace absent for " + identity);
    return resolved;
  }

  /**
   * Finds a mandatory child by reference type and qualified BrowseName, then validates its
   * declaration.
   *
   * <p>This method does not inspect the child's Value or recursively validate its descendants.
   *
   * @return the compatible child; never null.
   * @throws UaRuntimeException if the child is missing, ambiguous or incompatible.
   */
  public static <T extends UaNode> T mandatoryChild(
      UaNode parent,
      String uri,
      String name,
      ExpandedNodeId referenceId,
      ExpandedNodeId typeId,
      @Nullable ExpandedNodeId dataTypeId,
      int rank,
      Class<T> javaType) {
    T node = lookup(parent, uri, name, referenceId, typeId, dataTypeId, rank, javaType);
    if (node == null)
      throw failure(
          StatusCodes.Bad_NotFound,
          declaration(parent, uri, name, typeId, dataTypeId, rank, javaType)
              + ": absent mandatory child",
          null);
    return node;
  }

  /**
   * Finds an optional child by reference type and qualified BrowseName, then validates its
   * declaration.
   *
   * <p>Optionality permits absence only. A present optional child must satisfy the same UA and Java
   * compatibility checks as a mandatory child. This method does not inspect the child's Value or
   * recursively validate its descendants.
   *
   * @return the compatible child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   */
  public static <T extends UaNode> @Nullable T optionalChild(
      UaNode parent,
      String uri,
      String name,
      ExpandedNodeId referenceId,
      ExpandedNodeId typeId,
      @Nullable ExpandedNodeId dataTypeId,
      int rank,
      Class<T> javaType) {
    return lookup(parent, uri, name, referenceId, typeId, dataTypeId, rank, javaType);
  }

  /** Resolves and validates a present child; absence returns null for the callers to interpret. */
  private static <T extends UaNode> @Nullable T lookup(
      UaNode parent,
      String uri,
      String name,
      ExpandedNodeId referenceId,
      ExpandedNodeId typeId,
      @Nullable ExpandedNodeId dataTypeId,
      int rank,
      Class<T> javaType) {
    OpcUaServer server = parent.getNodeContext().getServer();
    AddressSpaceManager address = server.getAddressSpaceManager();
    boolean installed = address.getManagedNode(parent.getNodeId()).orElse(null) == parent;
    try {
      NodeId reference = resolve(server.getNamespaceTable(), referenceId);
      // A reference can be contributed by multiple managers. Count target identities, not
      // references.
      // Check ambiguity before compatibility so a wrong-type target cannot hide a duplicate child.
      Map<NodeId, UaNode> matches = new LinkedHashMap<>();
      for (Reference ref :
          installed
              ? address.getManagedReferences(parent.getNodeId())
              : parent.getNodeManager().getReferences(parent.getNodeId())) {
        if (!ref.isForward()
            || !(ref.getReferenceTypeId().equals(reference)
                || server.getReferenceTypeTree().isSubtypeOf(ref.getReferenceTypeId(), reference)))
          continue;
        UaNode candidate =
            installed
                ? address.getManagedNode(ref.getTargetNodeId()).orElse(null)
                : parent
                    .getNodeManager()
                    .getNode(ref.getTargetNodeId(), server.getNamespaceTable())
                    .orElse(null);
        if (candidate == null) continue;
        // BrowseName namespaces identify declarations; a child's NodeId can use a different
        // namespace.
        String actualUri =
            server.getNamespaceTable().get(candidate.getBrowseName().getNamespaceIndex());
        if (uri.equals(actualUri) && name.equals(candidate.getBrowseName().getName()))
          matches.put(candidate.getNodeId(), candidate);
      }
      if (matches.size() > 1)
        throw new UaRuntimeException(
            StatusCodes.Bad_TooManyMatches, "ambiguous targets " + matches.keySet());
      if (matches.isEmpty()) return null;
      UaNode node = matches.values().iterator().next();
      NodeClass nodeClass =
          javaType == UaMethodNode.class
              ? NodeClass.Method
              : dataTypeId == null ? NodeClass.Object : NodeClass.Variable;
      if (node.getNodeClass() != nodeClass)
        throw new UaRuntimeException(
            StatusCodes.Bad_TypeMismatch,
            "child " + node.getNodeId() + " is a " + node.getNodeClass() + ", not a " + nodeClass);
      // A present node built without the generated constructor reads as the wrong Java class,
      // which usually means the initializer ran after the nodes were loaded.
      if (!javaType.isInstance(node))
        throw new UaRuntimeException(
            StatusCodes.Bad_TypeMismatch,
            "child "
                + node.getNodeId()
                + " is a "
                + node.getClass().getSimpleName()
                + ", not the generated "
                + javaType.getSimpleName()
                + "; register constructors with the initializer before loading nodes");
      if (nodeClass == NodeClass.Method) return javaType.cast(node);
      // A Java cast alone does not establish the declared UA type, datatype or rank.
      NodeId expectedType = resolve(server.getNamespaceTable(), typeId);
      Map<NodeId, Boolean> types = new LinkedHashMap<>();
      for (Reference ref :
          installed
              ? address.getManagedReferences(node.getNodeId())
              : parent.getNodeManager().getReferences(node.getNodeId())) {
        if (ref.isForward() && ref.getReferenceTypeId().equals(NodeIds.HasTypeDefinition))
          ref.getTargetNodeId()
              .toNodeId(server.getNamespaceTable())
              .ifPresent(id -> types.put(id, Boolean.TRUE));
      }
      if (types.size() != 1)
        throw new UaRuntimeException(
            StatusCodes.Bad_TypeMismatch,
            "child " + node.getNodeId() + " has type definitions " + types.keySet());
      NodeId actualType = types.keySet().iterator().next();
      boolean compatible =
          actualType.equals(expectedType)
              || (dataTypeId == null
                  ? server.getObjectTypeTree().isSubtypeOf(actualType, expectedType)
                  : server.getVariableTypeTree().isSubtypeOf(actualType, expectedType));
      if (!compatible)
        throw new UaRuntimeException(
            StatusCodes.Bad_TypeMismatch,
            "child " + node.getNodeId() + " has incompatible type " + actualType);
      if (node instanceof UaVariableNode variable) {
        NodeId expectedDataType = resolve(server.getNamespaceTable(), dataTypeId);
        if (!(variable.getDataType().equals(expectedDataType)
                || server.getDataTypeTree().isSubtypeOf(variable.getDataType(), expectedDataType))
            || !compatibleRank(variable.getValueRank(), rank))
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "child "
                  + node.getNodeId()
                  + " has datatype="
                  + variable.getDataType()
                  + ", rank="
                  + variable.getValueRank());
      }
      return javaType.cast(node);
    } catch (RuntimeException e) {
      // Build declaration diagnostics only on failure; generated call sites carry no formatted
      // label. The original status survives; anything else reports an internal failure.
      throw failure(
          e instanceof UaRuntimeException ua
              ? ua.getStatusCode().getValue()
              : StatusCodes.Bad_InternalError,
          declaration(parent, uri, name, typeId, dataTypeId, rank, javaType)
              + ": "
              + e.getMessage(),
          e);
    }
  }

  /** Formats the declaration a child lookup was checking, for failure diagnostics. */
  private static String declaration(
      UaNode parent,
      String uri,
      String name,
      ExpandedNodeId typeId,
      @Nullable ExpandedNodeId dataTypeId,
      int rank,
      Class<?> javaType) {
    return "parent "
        + parent.getNodeId()
        + " child {"
        + uri
        + "}"
        + name
        + " [type="
        + typeId
        + ", datatype="
        + dataTypeId
        + ", rank="
        + rank
        + ", Java="
        + javaType.getName()
        + "]";
  }

  /**
   * Reads a scalar through Milo's normal Value attribute/filter path.
   *
   * <p>Bad status fails before inspecting the payload; Uncertain status permits conversion. An
   * absent optional child or null payload returns null. Raw status and timestamp access stays on
   * the node's DataValue API, including Milo's ordinary server-timestamp refresh on reads.
   */
  public static <T> @Nullable T read(
      UaNode parent,
      @Nullable UaVariableNode node,
      Class<T> type,
      @Nullable IntFunction<@Nullable T> enumeration) {
    if (node == null) return null;
    DataValue data = node.getValue();
    if (data.getStatusCode().isBad())
      throw valueFailure(
          data.getStatusCode().getValue(),
          parent,
          node,
          -1,
          "expected " + type.getName() + ": Bad status " + data.getStatusCode(),
          null);
    if (type == Variant.class)
      return data.getValue().getValue() == null ? null : type.cast(data.getValue());
    return scalar(parent, node, data.getValue().getValue(), type, enumeration, -1);
  }

  /** Reads a one-dimensional value with the same status and absence policy as scalar access. */
  public static <T> T @Nullable [] readArray(
      UaNode parent,
      @Nullable UaVariableNode node,
      Class<T> type,
      @Nullable IntFunction<@Nullable T> enumeration) {
    if (node == null) return null;
    DataValue data = node.getValue();
    if (data.getStatusCode().isBad())
      throw valueFailure(
          data.getStatusCode().getValue(),
          parent,
          node,
          -1,
          "expected " + type.getName() + "[]: Bad status " + data.getStatusCode(),
          null);
    Object raw = data.getValue().getValue();
    if (raw == null) return null;
    if (!raw.getClass().isArray())
      throw valueFailure(
          StatusCodes.Bad_DecodingError,
          parent,
          node,
          -1,
          "expected " + type.getName() + "[]: not an array: " + raw.getClass(),
          null);
    // Allocate from the declared Java type: empty and all-null ExtensionObject arrays otherwise
    // provide no decoded element from which to infer the correct component type.
    @SuppressWarnings("unchecked")
    T[] values = (T[]) Array.newInstance(type, Array.getLength(raw));
    for (int i = 0; i < values.length; i++) {
      Object element = Array.get(raw, i);
      if (element == null && !nullableElement(type))
        throw valueFailure(
            StatusCodes.Bad_DecodingError,
            parent,
            node,
            i,
            "null element for " + type.getName(),
            null);
      values[i] = scalar(parent, node, element, type, enumeration, i);
    }
    return values;
  }

  /**
   * Converts one wire payload or array element, formatting node identity and array position only on
   * failure.
   */
  private static <T> @Nullable T scalar(
      UaNode parent,
      UaVariableNode node,
      @Nullable Object raw,
      Class<T> type,
      @Nullable IntFunction<@Nullable T> enumeration,
      int index) {
    try {
      if (raw == null) return null;
      if (raw instanceof ExtensionObject extension && type != ExtensionObject.class) {
        if (extension.isNull()) return null;
        raw = extension.decode(node.getNodeContext().getServer().getStaticEncodingContext());
      }
      // UA enums travel as Int32. An unknown number is a conversion error, not a null value.
      if (enumeration != null && raw instanceof Integer number) {
        T value = enumeration.apply(number);
        if (value == null) throw new IllegalStateException("unknown enum number " + number);
        return value;
      }
      if (OptionSetUInteger.class.isAssignableFrom(type)
          && raw instanceof UNumber
          && !type.isInstance(raw)) {
        try {
          return type.getConstructor(raw.getClass()).newInstance(raw);
        } catch (ReflectiveOperationException e) {
          throw new IllegalStateException("cannot construct OptionSet " + type.getName(), e);
        }
      }
      if (type == Variant.class) return type.cast(raw instanceof Variant ? raw : new Variant(raw));
      return type.cast(raw);
    } catch (RuntimeException e) {
      throw valueFailure(
          StatusCodes.Bad_DecodingError,
          parent,
          node,
          index,
          "expected " + type.getName() + ": " + e.getMessage(),
          e);
    }
  }

  /** Reads a wire-facing Variant for a flexible or matrix rank, checking each payload element. */
  public static <T> @Nullable Variant readVariant(
      UaNode parent,
      @Nullable UaVariableNode node,
      int rank,
      Class<T> type,
      @Nullable IntFunction<@Nullable T> enumeration) {
    if (node == null) return null;
    DataValue data = node.getValue();
    if (data.getStatusCode().isBad())
      throw valueFailure(
          data.getStatusCode().getValue(),
          parent,
          node,
          -1,
          "Bad status " + data.getStatusCode(),
          null);
    Variant value = data.getValue();
    validateVariant(parent, node, value, rank, type, enumeration);
    return value.getValue() == null ? null : value;
  }

  /** Validates a complete Variant before writing; preserves matrix dimensions and wire payloads. */
  public static <T> void writeVariant(
      UaNode parent,
      @Nullable UaVariableNode node,
      @Nullable String uri,
      String name,
      @Nullable Variant value,
      int rank,
      Class<T> type,
      @Nullable IntFunction<@Nullable T> enumeration) {
    if (node == null) throw absent(parent, uri, name);
    Variant payload = value == null ? Variant.NULL_VALUE : value;
    validateVariant(parent, node, payload, rank, type, enumeration);
    node.setValue(new DataValue(payload, StatusCode.GOOD, null, null));
  }

  private static <T> void validateVariant(
      UaNode parent,
      UaVariableNode node,
      Variant value,
      int rank,
      Class<T> type,
      @Nullable IntFunction<@Nullable T> enumeration) {
    Object raw = value.getValue();
    if (raw == null) return;
    int actual =
        raw instanceof Matrix matrix ? matrix.getValueRank() : raw.getClass().isArray() ? 1 : -1;
    if (!compatibleRank(actual, rank))
      throw valueFailure(
          StatusCodes.Bad_DecodingError,
          parent,
          node,
          -1,
          "incompatible value rank " + actual + ", expected " + rank,
          null);
    Object elements = raw instanceof Matrix matrix ? matrix.getElements() : raw;
    if (elements == null) return;
    if (actual == -1) scalar(parent, node, elements, type, enumeration, -1);
    else
      for (int i = 0; i < Array.getLength(elements); i++) {
        Object element = Array.get(elements, i);
        if (element == null && !nullableElement(type))
          throw valueFailure(
              StatusCodes.Bad_DecodingError,
              parent,
              node,
              i,
              "null element for " + type.getName(),
              null);
        scalar(parent, node, element, type, enumeration, i);
      }
  }

  private static boolean compatibleRank(int actual, int expected) {
    return actual == expected
        || expected == -2
        || expected == -3 && (actual == -1 || actual == 1)
        || expected == 0 && actual > 0;
  }

  /**
   * Converts a typed payload and updates an existing node through Milo's normal Value setter.
   *
   * <p>Conversion completes before mutation, including every array element. A failed conversion
   * therefore preserves the previous DataValue. Successful writes supply Good status and cleared
   * timestamps/picoseconds; callers needing explicit metadata use the raw DataValue API.
   *
   * @param array whether the declared value rank is one-dimensional.
   * @param enumeration whether the declared payload uses an enum-to-Int32 binding.
   * @param structured whether the declared payload uses an ExtensionObject binding.
   */
  public static void write(
      UaNode parent,
      UaVariableNode node,
      @Nullable Object value,
      boolean array,
      boolean enumeration,
      boolean structured) {
    int index = -1;
    Object encoded;
    try {
      if (value == null) encoded = null;
      else if (array) {
        int length = Array.getLength(value);
        Object values =
            enumeration
                ? new Integer[length]
                : structured
                        || UaStructuredType.class.isAssignableFrom(
                            value.getClass().getComponentType())
                    ? new ExtensionObject[length]
                    : Array.newInstance(wireComponent(value.getClass().getComponentType()), length);
        for (int i = 0; i < length; i++) {
          index = i;
          Object element = Array.get(value, i);
          if (element == null && !nullableElement(value.getClass().getComponentType()))
            throw new IllegalStateException(
                "null element for " + value.getClass().getComponentType().getName());
          Array.set(values, i, element instanceof Variant ? element : encode(node, element));
        }
        encoded = values;
      } else encoded = encode(node, value);
    } catch (RuntimeException e) {
      throw valueFailure(
          StatusCodes.Bad_DecodingError,
          parent,
          node,
          index,
          "value conversion failed: " + e.getMessage(),
          e);
    }
    // Keep notification, filtering and subscription behavior on Milo's ordinary attribute path.
    node.setValue(new DataValue(new Variant(encoded), StatusCode.GOOD, null, null));
  }

  /** Only optional setters need declaration identity: there may be no node to describe. */
  public static void write(
      UaNode parent,
      @Nullable UaVariableNode node,
      String uri,
      String name,
      @Nullable Object value,
      boolean array,
      boolean enumeration,
      boolean structured) {
    if (node == null) throw absent(parent, uri, name);
    write(parent, node, value, array, enumeration, structured);
  }

  /** Matches nullable UA payload categories; numeric wrappers and enum elements require values. */
  private static boolean nullableElement(Class<?> type) {
    return type == String.class
        || type == Variant.class
        || type == ExtensionObject.class
        || UaStructuredType.class.isAssignableFrom(type);
  }

  private static Class<?> wireComponent(Class<?> component) {
    if (!OptionSetUInteger.class.isAssignableFrom(component)) return component;
    try {
      return component.getMethod("getValue").getReturnType();
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("missing OptionSet value accessor", e);
    }
  }

  /** Produces the wire-facing Variant payload using this server's registered datatype codecs. */
  private static @Nullable Object encode(UaVariableNode node, @Nullable Object value) {
    if (value instanceof OptionSetUInteger<?> options) return options.getValue();
    if (value instanceof UaEnumeratedType enumeration) return enumeration.getValue();
    if (value instanceof UaStructuredType structured)
      return ExtensionObject.encode(
          node.getNodeContext().getServer().getStaticEncodingContext(), structured);
    if (value instanceof Variant variant) return variant.getValue();
    return value;
  }

  /**
   * Returns a present optional Method node, failing exactly as a value setter does on an absent
   * child. Mandatory Method getters already fail on absence and need no check.
   *
   * @throws UaRuntimeException with Bad_NotFound if the Method node is absent.
   */
  public static UaMethodNode present(
      UaNode parent, @Nullable UaMethodNode node, String uri, String name) {
    if (node == null) throw absent(parent, uri, name);
    return node;
  }

  /** Setters never create nodes, so every absent-child operation reports the same failure. */
  private static UaRuntimeException absent(UaNode parent, @Nullable String uri, String name) {
    return failure(
        StatusCodes.Bad_NotFound,
        "parent "
            + parent.getNodeId()
            + " child {"
            + uri
            + "}"
            + name
            + ": absent child; setters do not create nodes",
        null);
  }

  /** UaRuntimeException has no message-and-cause constructor; the cause is attached separately. */
  private static UaRuntimeException failure(
      long status, String message, @Nullable Throwable cause) {
    UaRuntimeException error = new UaRuntimeException(status, message);
    if (cause != null) error.initCause(cause);
    return error;
  }

  /** Present nodes supply their own identity, avoiding diagnostic constants in every accessor. */
  private static UaRuntimeException valueFailure(
      long status,
      UaNode parent,
      UaVariableNode node,
      int index,
      String detail,
      @Nullable Throwable cause) {
    String uri =
        node.getNodeContext()
            .getServer()
            .getNamespaceTable()
            .get(node.getBrowseName().getNamespaceIndex());
    String target =
        parent == node
            ? " Value"
            : " child {"
                + uri
                + "}"
                + node.getBrowseName().getName()
                + " ("
                + node.getNodeId()
                + ")";
    return failure(
        status,
        "parent "
            + parent.getNodeId()
            + target
            + (index < 0 ? "" : "[" + index + "]")
            + ": "
            + detail,
        cause);
  }
}
