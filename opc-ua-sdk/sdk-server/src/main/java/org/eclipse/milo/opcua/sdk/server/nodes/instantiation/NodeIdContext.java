/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * The full context a request's {@code nodeIdStrategy} receives for one planned node: the instance
 * root's {@link NodeId}, the occurrence's {@link BrowsePath}, and the {@link InstanceDeclaration}
 * being realized. Any identifier type may be returned; every produced id is collision-checked
 * against the target and the rest of the plan before anything is constructed.
 *
 * <p>{@link #defaultNodeId()} and {@link #legacyNodeId()} expose the built-in derivations so a
 * strategy can special-case some paths and fall back for the rest.
 *
 * @param rootNodeId the {@link NodeId} of the instance root being planned.
 * @param path the occurrence's {@link BrowsePath} relative to the root.
 * @param declaration the declaration occurrence being realized.
 */
public record NodeIdContext(NodeId rootNodeId, BrowsePath path, InstanceDeclaration declaration) {

  /**
   * The default derivation: a String identifier in the root's namespace, the root identifier prefix
   * followed by {@code /<namespaceIndex>:<name>} per path element, with {@code \}, {@code /}, and
   * {@code :} escaped inside names ({@code \\}, {@code \/}, {@code \:}) and non-String root
   * identifiers rendered with a type marker ({@code i=}, {@code g=}, {@code b=}).
   *
   * <p>Unlike the legacy formula this is collision-resistant: a member named {@code "A/1:B"} cannot
   * collide with a nested member {@code A} containing {@code B}, and a numeric root {@code 7}
   * cannot collide with a String root {@code "7"}.
   *
   * @return the default {@link NodeId} for this occurrence.
   */
  public NodeId defaultNodeId() {
    Object rootIdentifier = rootNodeId.getIdentifier();

    String prefix;
    if (rootIdentifier instanceof String s) {
      prefix = s;
    } else if (rootIdentifier instanceof UInteger u) {
      prefix = "i=" + u;
    } else if (rootIdentifier instanceof UUID g) {
      prefix = "g=" + g;
    } else {
      prefix = "b=" + rootIdentifier;
    }

    StringBuilder sb = new StringBuilder(prefix);
    for (QualifiedName element : path.elements()) {
      sb.append('/').append(element.namespaceIndex()).append(':').append(escape(element.name()));
    }

    return new NodeId(rootNodeId.getNamespaceIndex(), sb.toString());
  }

  /**
   * The legacy {@code NodeFactory} derivation, verbatim: the root identifier's string form
   * concatenated with {@code /<namespaceIndex>:<name>} per path element, unescaped, a String
   * identifier in the root's namespace. Ambiguous by construction (that is D9); provided because
   * derived ids are persisted identity for existing deployments migrating to this API.
   *
   * @return the legacy-formula {@link NodeId} for this occurrence.
   */
  public NodeId legacyNodeId() {
    StringBuilder sb = new StringBuilder(String.valueOf(rootNodeId.getIdentifier()));
    for (QualifiedName element : path.elements()) {
      sb.append('/').append(element.namespaceIndex()).append(':').append(element.name());
    }

    return new NodeId(rootNodeId.getNamespaceIndex(), sb.toString());
  }

  private static String escape(String name) {
    StringBuilder sb = new StringBuilder(name.length());
    for (int i = 0; i < name.length(); i++) {
      char c = name.charAt(i);
      if (c == '\\' || c == '/' || c == ':') {
        sb.append('\\');
      }
      sb.append(c);
    }
    return sb.toString();
  }
}
