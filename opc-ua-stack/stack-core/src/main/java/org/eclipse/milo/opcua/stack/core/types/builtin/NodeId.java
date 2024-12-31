/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class NodeId {

  public static final NodeId NULL_NUMERIC = new NodeId(ushort(0), uint(0));
  public static final NodeId NULL_STRING = new NodeId(ushort(0), "");
  public static final NodeId NULL_GUID = new NodeId(ushort(0), new UUID(0, 0));
  public static final NodeId NULL_OPAQUE = new NodeId(ushort(0), ByteString.NULL_VALUE);

  public static final NodeId NULL_VALUE = NULL_NUMERIC;

  private final UShort namespaceIndex;
  private final Object identifier;

  /**
   * @param namespaceIndex the index for a namespace URI. An index of 0 is used for OPC UA defined
   *     NodeIds.
   * @param identifier the identifier for a node in the address space of an OPC UA Server.
   */
  public NodeId(int namespaceIndex, int identifier) {
    this(ushort(namespaceIndex), uint(identifier));
  }

  /**
   * @param namespaceIndex the index for a namespace URI. An index of 0 is used for OPC UA defined
   *     NodeIds.
   * @param identifier the identifier for a node in the address space of an OPC UA Server.
   */
  public NodeId(int namespaceIndex, UInteger identifier) {
    this(ushort(namespaceIndex), identifier);
  }

  /**
   * @param namespaceIndex the index for a namespace URI. An index of 0 is used for OPC UA defined
   *     NodeIds.
   * @param identifier the identifier for a node in the address space of an OPC UA Server.
   */
  public NodeId(int namespaceIndex, String identifier) {
    this(ushort(namespaceIndex), identifier);
  }

  /**
   * @param namespaceIndex the index for a namespace URI. An index of 0 is used for OPC UA defined
   *     NodeIds.
   * @param identifier the identifier for a node in the address space of an OPC UA Server.
   */
  public NodeId(int namespaceIndex, UUID identifier) {
    this(ushort(namespaceIndex), identifier);
  }

  /**
   * @param namespaceIndex the index for a namespace URI. An index of 0 is used for OPC UA defined
   *     NodeIds.
   * @param identifier the identifier for a node in the address space of an OPC UA Server.
   */
  public NodeId(int namespaceIndex, ByteString identifier) {
    this(ushort(namespaceIndex), identifier);
  }

  /**
   * @param namespaceIndex the index for a namespace URI. An index of 0 is used for OPC UA defined
   *     NodeIds.
   * @param identifier the identifier for a node in the address space of an OPC UA Server.
   */
  public NodeId(UShort namespaceIndex, UInteger identifier) {
    checkNotNull(namespaceIndex);
    checkNotNull(identifier);

    this.namespaceIndex = namespaceIndex;
    this.identifier = identifier;
  }

  /**
   * @param namespaceIndex the index for a namespace URI. An index of 0 is used for OPC UA defined
   *     NodeIds.
   * @param identifier the identifier for a node in the address space of an OPC UA Server.
   */
  public NodeId(UShort namespaceIndex, int identifier) {
    checkNotNull(namespaceIndex);

    this.namespaceIndex = namespaceIndex;
    this.identifier = uint(identifier);
  }

  /**
   * @param namespaceIndex the index for a namespace URI. An index of 0 is used for OPC UA defined
   *     NodeIds.
   * @param identifier the identifier for a node in the address space of an OPC UA Server.
   */
  public NodeId(UShort namespaceIndex, String identifier) {
    checkNotNull(namespaceIndex);

    if (identifier == null) identifier = "";

    this.namespaceIndex = namespaceIndex;
    this.identifier = identifier;
  }

  /**
   * @param namespaceIndex the index for a namespace URI. An index of 0 is used for OPC UA defined
   *     NodeIds.
   * @param identifier the identifier for a node in the address space of an OPC UA Server.
   */
  public NodeId(UShort namespaceIndex, UUID identifier) {
    checkNotNull(namespaceIndex);
    checkNotNull(identifier);

    this.namespaceIndex = namespaceIndex;
    this.identifier = identifier;
  }

  /**
   * @param namespaceIndex the index for a namespace URI. An index of 0 is used for OPC UA defined
   *     NodeIds.
   * @param identifier the identifier for a node in the address space of an OPC UA Server.
   */
  public NodeId(UShort namespaceIndex, ByteString identifier) {
    checkNotNull(namespaceIndex);
    checkNotNull(identifier);

    this.namespaceIndex = namespaceIndex;
    this.identifier = identifier;
  }

  NodeId(@NonNull UShort namespaceIndex, @NonNull Object identifier) {
    checkNotNull(namespaceIndex);
    checkNotNull(identifier);

    this.namespaceIndex = namespaceIndex;
    this.identifier = identifier;
  }

  public UShort getNamespaceIndex() {
    return namespaceIndex;
  }

  public Object getIdentifier() {
    return identifier;
  }

  public IdType getType() {
    if (identifier instanceof UInteger) {
      return IdType.Numeric;
    } else if (identifier instanceof String) {
      return IdType.String;
    } else if (identifier instanceof UUID) {
      return IdType.Guid;
    } else {
      return IdType.Opaque;
    }
  }

  /**
   * Convert this NodeId to a relative {@link ExpandedNodeId}.
   *
   * @return a relative {@link ExpandedNodeId}.
   * @see ExpandedNodeId#isRelative()
   */
  public ExpandedNodeId expanded() {
    return new ExpandedNodeId(namespaceIndex, null, identifier, UInteger.MIN);
  }

  /**
   * Convert this NodeId to an absolute {@link ExpandedNodeId}, using {@code namespaceTable} to
   * convert the namespace index to a namespace URI.
   *
   * @param namespaceTable a {@link NamespaceTable} to look up the namespace URI in.
   * @return an absolute {@link ExpandedNodeId}.
   * @see ExpandedNodeId#isAbsolute()
   */
  public ExpandedNodeId expanded(NamespaceTable namespaceTable) {
    String namespaceUri = namespaceTable.get(namespaceIndex);
    return new ExpandedNodeId(namespaceIndex, namespaceUri, identifier, UInteger.MIN);
  }

  public boolean isNull() {
    return namespaceIndex.intValue() == 0
        && (NULL_NUMERIC.equals(this)
            || NULL_STRING.equals(this)
            || NULL_GUID.equals(this)
            || NULL_OPAQUE.equals(this));
  }

  public boolean isNotNull() {
    return !isNull();
  }

  /**
   * Re-index this {@link NodeId} from its current namespace index to the index for {@code
   * namespaceUri}.
   *
   * <p>If the target namespace URI is not present in the namespace table this {@link NodeId} is
   * returned.
   *
   * @param namespaceTable the {@link NamespaceTable}.
   * @param namespaceUri the target namespace URI.
   * @return a new {@link NodeId} in the namespace index indicated by {@code namespaceUri}.
   */
  public NodeId reindex(NamespaceTable namespaceTable, String namespaceUri) {
    UShort namespaceIndex = namespaceTable.getIndex(namespaceUri);

    if (namespaceIndex != null && !namespaceIndex.equals(getNamespaceIndex())) {
      return withNamespaceIndex(namespaceIndex);
    } else {
      return this;
    }
  }

  /**
   * Return a new {@link NodeId} with {@code id}.
   *
   * @param id the new identifier.
   * @return a new {@link NodeId} with {@code id}.
   */
  public NodeId withId(UInteger id) {
    return new NodeId(namespaceIndex, id);
  }

  /**
   * Return a new {@link NodeId} with {@code id}.
   *
   * @param id the new identifier.
   * @return a new {@link NodeId} with {@code id}.
   */
  public NodeId withId(String id) {
    return new NodeId(namespaceIndex, id);
  }

  /**
   * Return a new {@link NodeId} with {@code id}.
   *
   * @param id the new identifier.
   * @return a new {@link NodeId} with {@code id}.
   */
  public NodeId withId(UUID id) {
    return new NodeId(namespaceIndex, id);
  }

  /**
   * Return a new {@link NodeId} with {@code id}.
   *
   * @param id the new identifier.
   * @return a new {@link NodeId} with {@code id}.
   */
  public NodeId withId(ByteString id) {
    return new NodeId(namespaceIndex, id);
  }

  /**
   * Return a new {@link NodeId} with {@code namespaceIndex}.
   *
   * @param namespaceIndex the new namespace index.
   * @return a new {@link NodeId} with {@code namespaceIndex}.
   */
  public NodeId withNamespaceIndex(UShort namespaceIndex) {
    return new NodeId(namespaceIndex, this.identifier);
  }

  /**
   * Check if this {@link NodeId} is equal to {@code xni}.
   *
   * <p>To be considered equal {@code xni} must be in serverIndex == 0, have a namespace index that
   * is equal this namespace index, or have a namespace URI at the same index in the default
   * namespace table, and an equal identifier.
   *
   * @param xni the {@link ExpandedNodeId} to check equality against.
   * @return {@code true} if this {@link NodeId} is equal to {@code xni}.
   * @deprecated use {@link #equalTo(ExpandedNodeId)} instead.
   */
  @Deprecated
  public boolean equals(ExpandedNodeId xni) {
    return equalTo(xni);
  }

  /**
   * Check if this {@link NodeId} is equal to {@code xni}.
   *
   * <p>To be considered equal {@code xni} must be in serverIndex == 0, have a namespace index that
   * is equal this namespace index, or have a namespace URI at the same index in the default
   * namespace table, and an equal identifier.
   *
   * @param xni the {@link ExpandedNodeId} to check equality against.
   * @param namespaceTable the {@link NamespaceTable} used to look up the index of a namespace URI.
   * @return {@code true} if this {@link NodeId} is equal to {@code xni}.
   * @deprecated use {@link #equalTo(ExpandedNodeId, NamespaceTable)} instead.
   */
  @Deprecated
  public boolean equals(ExpandedNodeId xni, NamespaceTable namespaceTable) {
    return equalTo(xni, namespaceTable);
  }

  /**
   * Check if this {@link NodeId} is equal to {@code xni}.
   *
   * <p>To be considered equal {@code xni} must be in serverIndex == 0, have a namespace index that
   * is equal this namespace index, or have a namespace URI at the same index in the default
   * namespace table, and an equal identifier.
   *
   * @param xni the {@link ExpandedNodeId} to check equality against.
   * @return {@code true} if this {@link NodeId} is equal to {@code xni}.
   */
  public boolean equalTo(ExpandedNodeId xni) {
    return equalTo(
        xni,
        uri -> {
          if (Namespaces.OPC_UA.equals(uri)) {
            return UShort.MIN;
          } else {
            return null;
          }
        });
  }

  /**
   * Check if this {@link NodeId} is equal to {@code xni}.
   *
   * <p>To be considered equal {@code xni} must be in serverIndex == 0, have a namespace index that
   * is equal this namespace index, or have a namespace URI at the same index in the default
   * namespace table, and an equal identifier.
   *
   * @param xni the {@link ExpandedNodeId} to check equality against.
   * @param namespaceTable the {@link NamespaceTable} used to look up the index of a namespace URI.
   * @return {@code true} if this {@link NodeId} is equal to {@code xni}.
   */
  public boolean equalTo(ExpandedNodeId xni, NamespaceTable namespaceTable) {
    return equalTo(xni, namespaceTable::getIndex);
  }

  private boolean equalTo(ExpandedNodeId xni, Function<String, UShort> getIndex) {
    if (!xni.isLocal()) {
      return false;
    }

    UShort otherNamespaceIndex =
        xni.isAbsolute() ? getIndex.apply(xni.getNamespaceUri()) : xni.getNamespaceIndex();

    return Objects.equal(namespaceIndex, otherNamespaceIndex)
        && Objects.equal(identifier, xni.getIdentifier());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NodeId nodeId = (NodeId) o;

    return identifier.equals(nodeId.identifier) && namespaceIndex.equals(nodeId.namespaceIndex);
  }

  @Override
  public int hashCode() {
    int result = namespaceIndex.hashCode();
    result = 31 * result + identifier.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("ns", namespaceIndex)
        .add("id", identifier)
        .toString();
  }

  public String toParseableString() {
    StringBuilder sb = new StringBuilder();

    sb.append("ns=").append(namespaceIndex).append(";");

    switch (getType()) {
      case Numeric:
        sb.append("i=").append(identifier);
        break;
      case String:
        sb.append("s=").append(identifier);
        break;
      case Guid:
        sb.append("g=").append(identifier);
        break;
      case Opaque:
        ByteString bs = (ByteString) identifier;
        if (bs.isNull()) sb.append("b=");
        else sb.append("b=").append(Base64.getEncoder().encodeToString(bs.bytes()));
        break;

      default:
        throw new IllegalStateException("unknown IdType: " + getType());
    }

    return sb.toString();
  }

  public static NodeId parse(@NonNull String s) throws UaRuntimeException {
    if (s.startsWith("ns=")) {
      int index = s.indexOf(";");

      if (index == -1) {
        throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid);
      }

      try {
        int namespaceIndex = Integer.parseInt(s.substring(3, index));

        return parse(s.substring(index + 1), namespaceIndex);
      } catch (NumberFormatException e) {
        throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid, e);
      }
    } else {
      return parse(s, 0);
    }
  }

  private static NodeId parse(String s, int namespaceIndex) throws UaRuntimeException {
    if (s.length() < 2) {
      throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid);
    }

    String type = s.substring(0, 2);
    String id = s.substring(2);

    switch (type) {
      case "i=":
        try {
          return new NodeId(namespaceIndex, uint(Long.parseLong(id)));
        } catch (NumberFormatException e) {
          throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid, e);
        }
      case "s=":
        return new NodeId(namespaceIndex, id);
      case "g=":
        try {
          return new NodeId(namespaceIndex, UUID.fromString(id));
        } catch (IllegalArgumentException e) {
          throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid, e);
        }
      case "b=":
        try {
          return new NodeId(namespaceIndex, ByteString.of(Base64.getDecoder().decode(id)));
        } catch (IllegalArgumentException e) {
          throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid, e);
        }
      default:
        throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid);
    }
  }

  @Nullable
  public static NodeId parseOrNull(@NonNull String s) {
    try {
      return NodeId.parse(s);
    } catch (UaRuntimeException t) {
      return null;
    }
  }

  public static Optional<NodeId> parseSafe(@NonNull String s) {
    return Optional.ofNullable(parseOrNull(s));
  }
}
