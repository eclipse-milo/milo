/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.*;

import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference.NamespaceIndex;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference.ServerIndex;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

public record ExpandedNodeId(
    ServerReference server, NamespaceReference namespace, Object identifier) {

  public static final ExpandedNodeId NULL_VALUE = ExpandedNodeId.of(0, 0);

  public Object getIdentifier() {
    return identifier;
  }

  /**
   * Get the {@link IdType} of this {@link ExpandedNodeId}'s identifier.
   *
   * @return the {@link IdType} of this {@link ExpandedNodeId}'s identifier.
   */
  public IdType getType() {
    if (identifier instanceof UInteger) {
      return IdType.Numeric;
    } else if (identifier instanceof String) {
      return IdType.String;
    } else if (identifier instanceof UUID) {
      return IdType.Guid;
    } else if (identifier instanceof ByteString) {
      return IdType.Opaque;
    } else {
      throw new IllegalStateException("identifier: " + identifier);
    }
  }

  public @Nullable UShort getNamespaceIndex() {
    if (namespace instanceof NamespaceIndex index) {
      return index.namespaceIndex;
    } else {
      return null;
    }
  }

  public @Nullable String getNamespaceUri() {
    if (namespace instanceof NamespaceReference.NamespaceUri uri) {
      return uri.namespaceUri;
    } else {
      return null;
    }
  }

  public @Nullable UInteger getServerIndex() {
    if (server instanceof ServerIndex index) {
      return index.serverIndex;
    } else {
      return null;
    }
  }

  public @Nullable String getServerUri() {
    if (server instanceof ServerReference.ServerUri uri) {
      return uri.serverUri;
    } else {
      return null;
    }
  }

  /**
   * Check if this {@link ExpandedNodeId} is local, i.e. the server is defined in terms of a server
   * index, and that index is 0.
   *
   * @return {@code true} if this {@link ExpandedNodeId} is local.
   * @see #isLocal(ServerTable)
   */
  public boolean isLocal() {
    if (server instanceof ServerIndex index) {
      return index.serverIndex.intValue() == 0;
    } else {
      return false;
    }
  }

  /**
   * Check if this {@link ExpandedNodeId} is local.
   *
   * <p>If the server is defined in terms of a server index, this ExpandedNodeId is local if the
   * index is 0.
   *
   * <p>If the server is defined in terms of a server URI, the server table is used to determine if
   * the URI is that of the local server (index 0).
   *
   * @param serverTable the {@link ServerTable} to use when checking the server URI.
   * @return {@code true} if this {@link ExpandedNodeId} is local.
   */
  public boolean isLocal(ServerTable serverTable) {
    if (server instanceof ServerIndex index) {
      return index.serverIndex.intValue() == 0;
    } else if (server instanceof ServerReference.ServerUri uri) {
      UShort index = serverTable.getIndex(uri.serverUri);
      return index != null && index.intValue() == 0;
    } else {
      throw new IllegalStateException("ServerReference: " + server);
    }
  }

  /**
   * Check if this {@link ExpandedNodeId} is absolute, i.e. it is defined in terms of a namespace
   * URI rather than a namespace index.
   *
   * @return {@code true} if this {@link ExpandedNodeId} is absolute.
   */
  public boolean isAbsolute() {
    return namespace instanceof NamespaceReference.NamespaceUri;
  }

  /**
   * Check if this {@link ExpandedNodeId} is relative, i.e. it is defined in terms of a namespace
   * index rather than a namespace URI.
   *
   * @return {@code true} if this {@link ExpandedNodeId} is relative.
   */
  public boolean isRelative() {
    return namespace instanceof NamespaceIndex;
  }

  /**
   * Check if this {@link ExpandedNodeId} is in the OPC UA namespace, i.e. the namespace index is 0,
   * or the namespace URI is {@link Namespaces#OPC_UA}.
   *
   * @return {@code true} if this {@link ExpandedNodeId} is in the OPC UA namespace.
   */
  public boolean isOpcUaNamespace() {
    if (namespace instanceof NamespaceIndex index) {
      return index.namespaceIndex.intValue() == 0;
    } else if (namespace instanceof NamespaceReference.NamespaceUri uri) {
      return uri.namespaceUri.equals(Namespaces.OPC_UA);
    } else {
      throw new IllegalStateException("NamespaceReference: " + namespace);
    }
  }

  /**
   * @return {@code true} if this {@link ExpandedNodeId} is null.
   */
  public boolean isNull() {
    if (!isOpcUaNamespace()) {
      return false;
    }

    if (identifier instanceof UInteger id) {
      return id.intValue() == 0;
    } else if (identifier instanceof String id) {
      return id.isEmpty();
    } else if (identifier instanceof UUID id) {
      return id.getLeastSignificantBits() == 0 && id.getMostSignificantBits() == 0;
    } else if (identifier instanceof ByteString id) {
      return id.isNullOrEmpty();
    } else {
      return true;
    }
  }

  /**
   * @return {@code true} if this {@link ExpandedNodeId} is not null.
   */
  public boolean isNotNull() {
    return !isNull();
  }

  public Optional<NodeId> toNodeId(NamespaceTable namespaceTable) {
    if (!isLocal()) {
      return Optional.empty();
    }

    if (namespace instanceof NamespaceIndex index) {
      return Optional.of(new NodeId(index.namespaceIndex, identifier));
    } else if (namespace instanceof NamespaceReference.NamespaceUri uri) {
      UShort index = namespaceTable.getIndex(uri.namespaceUri);
      if (index == null) {
        return Optional.empty();
      } else {
        return Optional.of(new NodeId(index, identifier));
      }
    } else {
      throw new IllegalStateException("NamespaceReference: " + namespace);
    }
  }

  public NodeId toNodeIdOrThrow(NamespaceTable namespaceTable) throws Exception {
    if (!isLocal()) {
      throw new Exception("ExpandedNodeId is not local: " + this);
    } else {
      return toNodeId(namespaceTable)
          .orElseThrow(() -> new Exception("namespace not registered: " + namespace));
    }
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
      UShort namespaceIndex = ((NamespaceIndex) namespace).namespaceIndex;
      String namespaceUri = namespaceTable.get(namespaceIndex);

      if (namespaceUri != null) {
        ExpandedNodeId xni =
            new ExpandedNodeId(server, NamespaceReference.of(namespaceUri), identifier);

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
      String namespaceUri = ((NamespaceReference.NamespaceUri) namespace).namespaceUri;
      UShort namespaceIndex = namespaceTable.getIndex(namespaceUri);

      if (namespaceIndex != null) {
        ExpandedNodeId xni =
            new ExpandedNodeId(server, NamespaceReference.of(namespaceIndex), identifier);

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
    if (namespace instanceof NamespaceReference.NamespaceUri) {
      return new ExpandedNodeId(server, NamespaceReference.of(namespaceUri), identifier);
    } else if (namespace instanceof NamespaceIndex index) {
      UShort newNamespaceIndex = namespaceTable.getIndex(namespaceUri);
      if (newNamespaceIndex != null && !Objects.equals(index.namespaceIndex, newNamespaceIndex)) {
        return new ExpandedNodeId(server, NamespaceReference.of(newNamespaceIndex), identifier);
      } else {
        return this;
      }
    } else {
      throw new IllegalStateException("NamespaceReference: " + namespace);
    }
  }

  public boolean equalTo(NodeId nodeId) {
    return equalTo(nodeId, new NamespaceTable());
  }

  public boolean equalTo(NodeId nodeId, NamespaceTable namespaceTable) {
    if (!isLocal()) {
      return false;
    }

    if (namespace instanceof NamespaceIndex index) {
      int thisIndex = index.namespaceIndex.intValue();
      int thatIndex = nodeId.getNamespaceIndex().intValue();

      return thisIndex == thatIndex && identifier.equals(nodeId.getIdentifier());
    } else if (namespace instanceof NamespaceReference.NamespaceUri uri) {
      String thisUri = uri.namespaceUri;
      String thatUri = namespaceTable.get(nodeId.getNamespaceIndex());

      return thisUri.equals(thatUri) && identifier.equals(nodeId.getIdentifier());
    } else {
      throw new IllegalStateException("NamespaceReference: " + namespace);
    }
  }

  public String toParseableString() {
    StringBuilder sb = new StringBuilder();

    if (server instanceof ServerIndex index) {
      if (index.serverIndex.intValue() != 0) {
        sb.append("svr=").append(index.serverIndex).append(";");
      }
    } else if (server instanceof ServerReference.ServerUri uri) {
      sb.append("svu=").append(uri.serverUri).append(";");
    }

    if (namespace instanceof NamespaceIndex index) {
      if (index.namespaceIndex.intValue() != 0) {
        sb.append("ns=").append(index.namespaceIndex).append(";");
      }
    } else if (namespace instanceof NamespaceReference.NamespaceUri uri) {
      if (!uri.namespaceUri.equals(Namespaces.OPC_UA)) {
        sb.append("nsu=").append(uri.namespaceUri).append(";");
      }
    }

    switch (getType()) {
      case Numeric:
        sb.append("i=").append(getIdentifier());
        break;
      case String:
        sb.append("s=").append(getIdentifier());
        break;
      case Guid:
        sb.append("g=").append(getIdentifier().toString().toUpperCase());
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

  public static ExpandedNodeId parse(String s) throws UaRuntimeException {
    try {
      ServerReference server = ServerIndex.LOCAL;

      if (s.startsWith("svr=")) {
        int endIndex = s.indexOf(";");
        long serverIndex = Long.parseLong(s.substring(4, endIndex));
        server = ServerReference.of(uint(serverIndex));

        s = s.substring(endIndex + 1);
      } else if (s.startsWith("svu=")) {
        int endIndex = s.indexOf(";");
        String serverUri = s.substring(4, endIndex);
        server = ServerReference.of(serverUri);

        s = s.substring(endIndex + 1);
      }

      NamespaceReference namespace = NamespaceIndex.OPC_UA;

      if (s.startsWith("ns=")) {
        int endIndex = s.indexOf(";");
        int namespaceIndex = Integer.parseInt(s.substring(3, endIndex));
        namespace = NamespaceReference.of(ushort(namespaceIndex));

        s = s.substring(endIndex + 1);
      } else if (s.startsWith("nsu=")) {
        int endIndex = s.indexOf(";");
        String namespaceUri = s.substring(4, endIndex);
        namespace = NamespaceReference.of(namespaceUri);

        s = s.substring(endIndex + 1);
      }

      String type = s.substring(0, 2);
      String id = s.substring(2);

      Object identifier =
          switch (type) {
            case "i=":
              yield uint(Long.parseLong(id));
            case "s=":
              yield id;
            case "g=":
              yield UUID.fromString(id);
            case "b=":
              yield ByteString.of(Base64.getDecoder().decode(id));
            default:
              throw new IllegalArgumentException("invalid identifier type: " + type);
          };

      return new ExpandedNodeId(server, namespace, identifier);
    } catch (Throwable t) {
      throw new UaRuntimeException(StatusCodes.Bad_NodeIdInvalid, t);
    }
  }

  // region Static Factory Methods

  public static ExpandedNodeId of(long identifier) {
    return of(uint(identifier));
  }

  public static ExpandedNodeId of(int namespaceIndex, long identifier) {
    return of(ushort(namespaceIndex), uint(identifier));
  }

  public static ExpandedNodeId of(String namespaceUri, long identifier) {
    return of(namespaceUri, uint(identifier));
  }

  public static ExpandedNodeId of(UInteger identifier) {
    return new ExpandedNodeId(ServerIndex.LOCAL, NamespaceIndex.OPC_UA, identifier);
  }

  public static ExpandedNodeId of(String identifier) {
    return new ExpandedNodeId(
        ServerIndex.LOCAL, NamespaceReference.NamespaceUri.OPC_UA, identifier);
  }

  public static ExpandedNodeId of(UUID identifier) {
    return new ExpandedNodeId(
        ServerIndex.LOCAL, NamespaceReference.NamespaceUri.OPC_UA, identifier);
  }

  public static ExpandedNodeId of(ByteString identifier) {
    return new ExpandedNodeId(
        ServerIndex.LOCAL, NamespaceReference.NamespaceUri.OPC_UA, identifier);
  }

  public static ExpandedNodeId of(UShort namespaceIndex, UInteger identifier) {
    return new ExpandedNodeId(ServerIndex.LOCAL, NamespaceReference.of(namespaceIndex), identifier);
  }

  public static ExpandedNodeId of(UShort namespaceIndex, String identifier) {
    return new ExpandedNodeId(ServerIndex.LOCAL, NamespaceReference.of(namespaceIndex), identifier);
  }

  public static ExpandedNodeId of(UShort namespaceIndex, UUID identifier) {
    return new ExpandedNodeId(ServerIndex.LOCAL, NamespaceReference.of(namespaceIndex), identifier);
  }

  public static ExpandedNodeId of(UShort namespaceIndex, ByteString identifier) {
    return new ExpandedNodeId(ServerIndex.LOCAL, NamespaceReference.of(namespaceIndex), identifier);
  }

  public static ExpandedNodeId of(String namespaceUri, UInteger identifier) {
    return new ExpandedNodeId(ServerIndex.LOCAL, NamespaceReference.of(namespaceUri), identifier);
  }

  public static ExpandedNodeId of(String namespaceUri, String identifier) {
    return new ExpandedNodeId(ServerIndex.LOCAL, NamespaceReference.of(namespaceUri), identifier);
  }

  public static ExpandedNodeId of(String namespaceUri, UUID identifier) {
    return new ExpandedNodeId(ServerIndex.LOCAL, NamespaceReference.of(namespaceUri), identifier);
  }

  public static ExpandedNodeId of(String namespaceUri, ByteString identifier) {
    return new ExpandedNodeId(ServerIndex.LOCAL, NamespaceReference.of(namespaceUri), identifier);
  }

  // endregion

  public sealed interface NamespaceReference
      permits NamespaceIndex, NamespaceReference.NamespaceUri {

    record NamespaceIndex(UShort namespaceIndex) implements NamespaceReference {

      public static final NamespaceIndex OPC_UA = new NamespaceIndex(UShort.MIN);
    }

    record NamespaceUri(String namespaceUri) implements NamespaceReference {

      public static final NamespaceUri OPC_UA = new NamespaceUri(Namespaces.OPC_UA);
    }

    static NamespaceReference of(int namespaceIndex) {
      return NamespaceReference.of(ushort(namespaceIndex));
    }

    static NamespaceReference of(UShort namespaceIndex) {
      if (namespaceIndex.intValue() == 0) {
        return NamespaceIndex.OPC_UA;
      } else {
        return new NamespaceIndex(namespaceIndex);
      }
    }

    static NamespaceReference of(String namespaceUri) {
      if (Namespaces.OPC_UA.equals(namespaceUri)) {
        return NamespaceUri.OPC_UA;
      } else {
        return new NamespaceUri(namespaceUri);
      }
    }
  }

  public sealed interface ServerReference permits ServerIndex, ServerReference.ServerUri {

    record ServerIndex(UInteger serverIndex) implements ServerReference {

      public static final ServerIndex LOCAL = new ServerIndex(UInteger.MIN);
    }

    record ServerUri(String serverUri) implements ServerReference {}

    static ServerReference of(long serverIndex) {
      return ServerReference.of(uint(serverIndex));
    }

    static ServerReference of(UInteger serverIndex) {
      if (serverIndex.intValue() == 0) {
        return ServerIndex.LOCAL;
      } else {
        return new ServerIndex(serverIndex);
      }
    }

    static ServerReference of(String serverUri) {
      return new ServerUri(serverUri);
    }
  }
}
