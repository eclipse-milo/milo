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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class ExpandedNodeId {

  public static final ExpandedNodeId NULL_VALUE =
      new ExpandedNodeId(UShort.MIN, null, UInteger.MIN, UInteger.MIN);

  private final UShort namespaceIndex;
  private final Object identifier;
  private final String namespaceUri;
  private final UInteger serverIndex;

  public ExpandedNodeId(UShort namespaceIndex, String namespaceUri, UInteger identifier) {

    this(namespaceIndex, namespaceUri, identifier, UInteger.MIN);
  }

  public ExpandedNodeId(
      UShort namespaceIndex, String namespaceUri, UInteger identifier, UInteger serverIndex) {

    this(namespaceIndex, namespaceUri, (Object) identifier, serverIndex);
  }

  public ExpandedNodeId(UShort namespaceIndex, String namespaceUri, String identifier) {

    this(namespaceIndex, namespaceUri, identifier, UInteger.MIN);
  }

  public ExpandedNodeId(
      UShort namespaceIndex, String namespaceUri, String identifier, UInteger serverIndex) {

    this(namespaceIndex, namespaceUri, (Object) identifier, serverIndex);
  }

  public ExpandedNodeId(UShort namespaceIndex, String namespaceUri, ByteString identifier) {

    this(namespaceIndex, namespaceUri, identifier, UInteger.MIN);
  }

  public ExpandedNodeId(
      UShort namespaceIndex, String namespaceUri, ByteString identifier, UInteger serverIndex) {

    this(namespaceIndex, namespaceUri, (Object) identifier, serverIndex);
  }

  public ExpandedNodeId(UShort namespaceIndex, String namespaceUri, UUID identifier) {

    this(namespaceIndex, namespaceUri, identifier, UInteger.MIN);
  }

  public ExpandedNodeId(
      UShort namespaceIndex, String namespaceUri, UUID identifier, UInteger serverIndex) {

    this(namespaceIndex, namespaceUri, (Object) identifier, serverIndex);
  }

  public ExpandedNodeId(
      @NonNull UShort namespaceIndex,
      @Nullable String namespaceUri,
      @Nullable Object identifier,
      @NonNull UInteger serverIndex) {

    Preconditions.checkNotNull(namespaceIndex);
    Preconditions.checkNotNull(serverIndex);

    this.namespaceIndex = namespaceIndex;
    this.namespaceUri = namespaceUri;
    this.identifier = identifier;
    this.serverIndex = serverIndex;
  }

  /**
   * Get the identifier object of this {@link ExpandedNodeId}.
   *
   * @return the identifier object of this {@link ExpandedNodeId}.
   */
  public Object getIdentifier() {
    return identifier;
  }

  /**
   * Get the {@link IdType} of this {@link ExpandedNodeId}.
   *
   * @return the {@link IdType} of this {@link ExpandedNodeId}.
   */
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
   * Get the namespace index of this {@link ExpandedNodeId}.
   *
   * <p>This value shall be ignored if {@link #getNamespaceUri()} is non-null.
   *
   * @return the namespace index of this {@link ExpandedNodeId}.
   */
  public UShort getNamespaceIndex() {
    return namespaceIndex;
  }

  /**
   * Get the namespace URI of this {@link ExpandedNodeId}.
   *
   * @return the namespace URI of this {@link ExpandedNodeId}.
   */
  @Nullable
  public String getNamespaceUri() {
    return namespaceUri;
  }

  /**
   * Get the server index of this {@link ExpandedNodeId}.
   *
   * @return the server index of this {@link ExpandedNodeId}.
   */
  public UInteger getServerIndex() {
    return serverIndex;
  }

  /**
   * @return {@code true} if this {@link ExpandedNodeId} is absolute, i.e. it specifies a namespace
   *     URI instead of a namespace index.
   */
  public boolean isAbsolute() {
    return namespaceUri != null;
  }

  /**
   * @return {@code true} if this {@link ExpandedNodeId} is relative, i.e. it specifies a namespace
   *     index instead of a namespace URI, and is therefore defined relative to some {@link
   *     NamespaceTable}.
   */
  public boolean isRelative() {
    return !isAbsolute();
  }

  /**
   * @return {@code true} if this {@link ExpandedNodeId} is local (serverIndex == 0).
   */
  public boolean isLocal() {
    return serverIndex.longValue() == 0;
  }

  /**
   * @return {@code true} if this {@link ExpandedNodeId} is null.
   */
  public boolean isNull() {
    if (namespaceIndex.intValue() > 0) {
      return false;
    }

    switch (getType()) {
      case Numeric:
        {
          UInteger id = (UInteger) this.identifier;
          return id.intValue() == 0;
        }
      case String:
        {
          String id = (String) this.identifier;
          return Strings.isNullOrEmpty(id);
        }
      case Guid:
        {
          UUID id = (UUID) this.identifier;
          return id.getLeastSignificantBits() == 0 && id.getMostSignificantBits() == 0;
        }
      case Opaque:
        {
          ByteString id = (ByteString) this.identifier;
          return id == null || id.isNullOrEmpty();
        }

      default:
        return true;
    }
  }

  public boolean isNotNull() {
    return !isNull();
  }

  /**
   * Convert this {@link ExpandedNodeId} to an absolute ExpandedNodeId if not already.
   *
   * <p>An absolute ExpandedNodeId is one that defines the namespace URI rather than namespace
   * index.
   *
   * <p>Returns {@link Optional#empty()} if the URI is not found in the namespace table.
   *
   * @param namespaceTable the {@link NamespaceTable} to use when converting the index to URI.
   * @return an absolute ExpandedNodeId, or this ExpandedNodeId if it's already absolute.
   * @see #isAbsolute()
   */
  public Optional<ExpandedNodeId> absolute(NamespaceTable namespaceTable) {
    if (isAbsolute()) {
      return Optional.of(this);
    } else {
      String namespaceUri = namespaceTable.get(namespaceIndex);

      if (namespaceUri != null) {
        ExpandedNodeId xni = new ExpandedNodeId(UShort.MIN, namespaceUri, identifier, serverIndex);

        return Optional.of(xni);
      } else {
        return Optional.empty();
      }
    }
  }

  /**
   * Convert this {@link ExpandedNodeId} to a relative ExpandedNodeId if not already.
   *
   * <p>A relative ExpandedNodeId is one that defines the namespace index rather than namespace URI.
   *
   * <p>Returns {@link Optional#empty()} if the URI is not found in the namespace table.
   *
   * @param namespaceTable the {@link NamespaceTable} to use when converting the URI to index.
   * @return a relative ExpandedNodeId, or this ExpandedNodeId if it's already relative.
   * @see #isRelative()
   */
  public Optional<ExpandedNodeId> relative(NamespaceTable namespaceTable) {
    if (isRelative()) {
      return Optional.of(this);
    } else {
      UShort namespaceIndex = namespaceTable.getIndex(namespaceUri);

      if (namespaceIndex != null) {
        ExpandedNodeId xni = new ExpandedNodeId(namespaceIndex, null, identifier, serverIndex);

        return Optional.of(xni);
      } else {
        return Optional.empty();
      }
    }
  }

  /**
   * Re-index this {@link ExpandedNodeId} from its current namespace index to the index for {@code
   * namespaceUri} in {@code namespaceTable}.
   *
   * <p>If the target namespace URI is not present in the namespace table this ExpandedNodeId is
   * returned.
   *
   * @param namespaceTable the {@link NamespaceTable}.
   * @param namespaceUri the target namespace URI.
   * @return a new {@link NodeId} in the namespace index indicated by {@code namespaceUri}.
   */
  public ExpandedNodeId reindex(NamespaceTable namespaceTable, String namespaceUri) {
    if (isAbsolute()) {
      // namespaceUri is specified; namespaceIndex is ignored
      return this;
    } else {
      UShort newNamespaceIndex = namespaceTable.getIndex(namespaceUri);

      if (newNamespaceIndex != null && !Objects.equals(namespaceIndex, newNamespaceIndex)) {

        return new ExpandedNodeId(newNamespaceIndex, null, identifier, serverIndex);
      } else {
        return this;
      }
    }
  }

  /**
   * If this {@link ExpandedNodeId} resides on the local server ({@code serverIndex == 0}), return
   * its representation as a local {@link NodeId}.
   *
   * <p>If this ExpandedNodeId specifies a namespace URI instead of a namespace index then the URI
   * must exist in {@code namespaceTable} or {@link Optional#empty()} is returned.
   *
   * @param namespaceTable the {@link NamespaceTable}.
   * @return a local {@link NodeId}, if {@code serverIndex == 0} and the namespace index can be
   *     determined.
   * @deprecated use {@link #toNodeId(NamespaceTable)} instead.
   */
  @Deprecated
  public Optional<NodeId> local(NamespaceTable namespaceTable) {
    return toNodeId(namespaceTable);
  }

  /**
   * Like {@link #local(NamespaceTable)}, but throws if the node is not local or the namespace is
   * not registered.
   *
   * @param namespaceTable the {@link NamespaceTable}.
   * @return a local {@link NodeId}.
   * @throws Exception if the node is not local or the namespace is not registered.
   * @deprecated use {@link #toNodeIdOrThrow(NamespaceTable)} instead.
   */
  @Deprecated
  public NodeId localOrThrow(NamespaceTable namespaceTable) throws Exception {
    return toNodeIdOrThrow(namespaceTable);
  }

  /**
   * If this {@link ExpandedNodeId} resides on the local server ({@code serverIndex == 0}), return
   * its representation as a local {@link NodeId}.
   *
   * <p>If this ExpandedNodeId specifies a namespace URI instead of a namespace index then the URI
   * must exist in {@code namespaceTable} or {@link Optional#empty()} is returned.
   *
   * @param namespaceTable the {@link NamespaceTable}.
   * @return a local {@link NodeId}, if {@code serverIndex == 0} and the namespace index can be
   *     determined.
   */
  public Optional<NodeId> toNodeId(NamespaceTable namespaceTable) {
    if (isLocal()) {
      if (namespaceUri == null || namespaceUri.isEmpty()) {
        NodeId nodeId = new NodeId(namespaceIndex, identifier);

        return Optional.of(nodeId);
      } else {
        UShort namespaceIndex = namespaceTable.getIndex(namespaceUri);

        if (namespaceIndex == null) {
          return Optional.empty();
        } else {
          NodeId nodeId = new NodeId(namespaceIndex, identifier);

          return Optional.of(nodeId);
        }
      }
    } else {
      return Optional.empty();
    }
  }

  /**
   * Like {@link #toNodeId(NamespaceTable)}, but throws if the node is not local or the namespace is
   * not registered.
   *
   * @param namespaceTable the {@link NamespaceTable}.
   * @return a local {@link NodeId}.
   * @throws Exception if the node is not local or the namespace is not registered.
   */
  public NodeId toNodeIdOrThrow(NamespaceTable namespaceTable) throws Exception {
    if (isLocal()) {
      return toNodeId(namespaceTable)
          .orElseThrow(() -> new Exception("namespace not registered: " + namespaceUri));
    } else {
      throw new Exception("not a local node (serverIndex = " + serverIndex + ")");
    }
  }

  /**
   * Check if this {@link ExpandedNodeId} is equal to {@code nodeId}.
   *
   * <p>To be considered equal this ExpandedNodeId must be in serverIndex == 0, have the same
   * namespace index as {@code nodeId} or have a namespace URI at the same index in the default
   * namespace table, and an equal identifier.
   *
   * @param nodeId the {@link NodeId} to check equality against.
   * @return {@code true} if this {@link ExpandedNodeId} is equal to {@code nodeId}.
   * @deprecated use {@link #equalTo(NodeId)} instead.
   */
  @Deprecated
  public boolean equals(NodeId nodeId) {
    return equalTo(nodeId);
  }

  /**
   * Check if this {@link ExpandedNodeId} is equal to {@code nodeId}.
   *
   * <p>To be considered equal this ExpandedNodeId must be in serverIndex == 0, have the same
   * namespace index as {@code nodeId} or have a namespace URI at the same index in the default
   * namespace table, and an equal identifier.
   *
   * @param nodeId the {@link NodeId} to check equality against.
   * @param namespaceTable the {@link NamespaceTable} used to look up the index of a namespace URI.
   * @return {@code true} if this {@link ExpandedNodeId} is equal to {@code nodeId}.
   * @deprecated use {@link #equalTo(NodeId, NamespaceTable)} instead.
   */
  @Deprecated
  public boolean equals(NodeId nodeId, NamespaceTable namespaceTable) {
    return equalTo(nodeId, namespaceTable);
  }

  /**
   * Check if this {@link ExpandedNodeId} is equal to {@code nodeId}.
   *
   * <p>To be considered equal this ExpandedNodeId must be in serverIndex == 0, have the same
   * namespace index as {@code nodeId} or have a namespace URI at the same index in the default
   * namespace table, and an equal identifier.
   *
   * @param nodeId the {@link NodeId} to check equality against.
   * @return {@code true} if this {@link ExpandedNodeId} is equal to {@code nodeId}.
   */
  public boolean equalTo(NodeId nodeId) {
    return nodeId.equalTo(this);
  }

  /**
   * Check if this {@link ExpandedNodeId} is equal to {@code nodeId}.
   *
   * <p>To be considered equal this ExpandedNodeId must be in serverIndex == 0, have the same
   * namespace index as {@code nodeId} or have a namespace URI at the same index in the default
   * namespace table, and an equal identifier.
   *
   * @param nodeId the {@link NodeId} to check equality against.
   * @param namespaceTable the {@link NamespaceTable} used to look up the index of a namespace URI.
   * @return {@code true} if this {@link ExpandedNodeId} is equal to {@code nodeId}.
   */
  public boolean equalTo(NodeId nodeId, NamespaceTable namespaceTable) {
    return nodeId.equalTo(this, namespaceTable);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExpandedNodeId that = (ExpandedNodeId) o;
    return Objects.equals(namespaceIndex, that.namespaceIndex)
        && Objects.equals(identifier, that.identifier)
        && Objects.equals(namespaceUri, that.namespaceUri)
        && serverIndex.equals(that.serverIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(namespaceIndex, identifier, namespaceUri, serverIndex);
  }

  @Override
  public String toString() {
    ToStringHelper helper = MoreObjects.toStringHelper(this);

    if (namespaceUri != null && namespaceUri.length() > 0) {
      helper.add("ns", namespaceUri);
    } else {
      helper.add("ns", getNamespaceIndex());
    }

    helper.add("id", getIdentifier());
    helper.add("serverIndex", getServerIndex());

    return helper.toString();
  }

  public String toParseableString() {
    StringBuilder sb = new StringBuilder();

    if (serverIndex.intValue() != 0) {
      sb.append("svr=").append(serverIndex).append(";");
    }

    if (namespaceUri != null) {
      sb.append("nsu=").append(namespaceUri).append(";");
    } else {
      int namespaceIndex = getNamespaceIndex().intValue();
      sb.append("ns=").append(namespaceIndex).append(";");
    }

    switch (getType()) {
      case Numeric:
        sb.append("i=").append(getIdentifier());
        break;
      case String:
        sb.append("s=").append(getIdentifier());
        break;
      case Guid:
        sb.append("g=").append(getIdentifier());
        break;
      case Opaque:
        ByteString bs = (ByteString) getIdentifier();
        if (bs.isNull()) sb.append("b=");
        else sb.append("b=").append(Base64.getEncoder().encodeToString(bs.bytes()));
        break;

      default:
        throw new IllegalStateException("IdType " + getType());
    }

    return sb.toString();
  }

  /**
   * Parse {@code s} into an {@link ExpandedNodeId}.
   *
   * @param s the String to parse.
   * @return an {@link ExpandedNodeId}.
   * @throws UaRuntimeException if parsing fails.
   */
  public static ExpandedNodeId parse(String s) {
    try {
      UInteger serverIndex = UInteger.MIN;
      if (s.startsWith("svr=")) {
        int endIndex = s.indexOf(";");
        serverIndex = uint(Integer.parseInt(s.substring(4, endIndex)));
        s = s.substring(endIndex + 1);
      }

      UShort namespaceIndex = UShort.MIN;
      String namespaceUri = null;
      if (s.startsWith("ns=")) {
        int endIndex = s.indexOf(";");
        namespaceIndex = ushort(Integer.parseInt(s.substring(3, endIndex)));
        s = s.substring(endIndex + 1);
      } else if (s.startsWith("nsu=")) {
        int endIndex = s.indexOf(";");
        namespaceUri = s.substring(4, endIndex);
        s = s.substring(endIndex + 1);
      }

      Object identifier;
      String type = s.substring(0, 2);
      String id = s.substring(2);

      switch (type) {
        case "i=":
          try {
            identifier = uint(Long.parseLong(id));
          } catch (NumberFormatException e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid, e);
          }
          break;
        case "s=":
          identifier = id;
          break;
        case "g=":
          try {
            identifier = UUID.fromString(id);
          } catch (IllegalArgumentException e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid, e);
          }
          break;
        case "b=":
          try {
            identifier = ByteString.of(Base64.getDecoder().decode(id));
          } catch (IllegalArgumentException e) {
            throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid, e);
          }
          break;
        default:
          throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid);
      }

      return new ExpandedNodeId(namespaceIndex, namespaceUri, identifier, serverIndex);
    } catch (Throwable t) {
      throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid, t);
    }
  }

  /**
   * Parse {@code s} into an {@link ExpandedNodeId}, or return {@code null} if parsing fails.
   *
   * @param s the String to parse.
   * @return an {@link ExpandedNodeId} or {@code null} if parsing fails.
   */
  @Nullable
  public static ExpandedNodeId parseOrNull(@NonNull String s) {
    try {
      return ExpandedNodeId.parse(s);
    } catch (UaRuntimeException t) {
      return null;
    }
  }

  /**
   * Parse {@code s} into an Optional containing an {@link ExpandedNodeId}, or return {@link
   * Optional#empty()}} if parsing fails.
   *
   * @param s the String to parse.
   * @return an Optional containing an {@link ExpandedNodeId}, or {@link Optional#empty()} if
   *     parsing fails.
   */
  public static Optional<ExpandedNodeId> parseSafe(@NonNull String s) {
    return Optional.ofNullable(parseOrNull(s));
  }

  /**
   * Return a new {@link ExpandedNodeId.Builder}.
   *
   * @return a new {@link ExpandedNodeId.Builder}.
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {

    private UShort namespaceIndex = UShort.MIN;
    private String namespaceUri = null;
    private Object identifier = null;
    private UInteger serverIndex = UInteger.MIN;

    public Builder setNamespaceIndex(int namespaceIndex) {
      return setNamespaceIndex(ushort(namespaceIndex));
    }

    public Builder setNamespaceIndex(UShort namespaceIndex) {
      this.namespaceIndex = namespaceIndex;
      return this;
    }

    public Builder setNamespaceUri(String namespaceUri) {
      this.namespaceUri = namespaceUri;
      return this;
    }

    public Builder setIdentifier(UInteger identifier) {
      this.identifier = identifier;
      return this;
    }

    public Builder setIdentifier(String identifier) {
      this.identifier = identifier;
      return this;
    }

    public Builder setIdentifier(UUID identifier) {
      this.identifier = identifier;
      return this;
    }

    public Builder setIdentifier(ByteString identifier) {
      this.identifier = identifier;
      return this;
    }

    public Builder setServerIndex(long serverIndex) {
      return setServerIndex(uint(serverIndex));
    }

    public Builder setServerIndex(UInteger serverIndex) {
      this.serverIndex = serverIndex;
      return this;
    }

    public ExpandedNodeId build() {
      return new ExpandedNodeId(namespaceIndex, namespaceUri, identifier, serverIndex);
    }
  }
}
