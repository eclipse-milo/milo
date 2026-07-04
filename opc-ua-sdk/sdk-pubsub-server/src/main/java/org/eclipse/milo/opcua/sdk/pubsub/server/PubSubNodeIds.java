/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * The deterministic {@link NodeId} scheme of the PubSub information model fragment: string NodeIds
 * in the server application namespace <em>are</em> component name paths with the {@value #PREFIX}
 * prefix — {@code "PubSub/<connection>[/<group>[/<writer|reader>]]"} for runtime components, {@code
 * "PubSub/PublishedDataSets/<name>"} for published datasets, and member children appended as
 * further {@code "/"}-separated segments (e.g. {@code "PubSub/conn/Status/State"}, {@code
 * "PubSub/conn/Status/Enable"}, {@code "PubSub/conn/Diagnostics"}).
 *
 * <p>Pure functions only — this is a naming rule, not a registry; ids are stable across rebuilds
 * and server restarts for an unchanged configuration.
 *
 * <p>Caveat: component names may contain {@code '/'}, so the mapping name path &rarr; NodeId is
 * injective but the reverse segment split is not — {@link #namePathOf} returns the joined name
 * path, and consumers that need component coordinates must carry name components explicitly (e.g.
 * from {@code ReconfigureResult.Change}), never parse paths.
 */
final class PubSubNodeIds {

  /** Prefix shared by every NodeId identifier minted by the info model fragment. */
  static final String PREFIX = "PubSub";

  private PubSubNodeIds() {}

  /**
   * The NodeId of a runtime component (connection, group, writer, or reader) object node.
   *
   * @param namespaceIndex the fragment's namespace index (the server application namespace).
   * @param namePath the {@code "/"}-joined component name path, e.g. {@code "conn/group/writer"}.
   * @return the deterministic {@link NodeId}, {@code "PubSub/" + namePath}.
   */
  static NodeId componentNodeId(UShort namespaceIndex, String namePath) {
    return new NodeId(namespaceIndex, PREFIX + "/" + namePath);
  }

  /**
   * The NodeId of a published dataset object node.
   *
   * @param namespaceIndex the fragment's namespace index (the server application namespace).
   * @param name the PublishedDataSet name.
   * @return the deterministic {@link NodeId}, {@code "PubSub/PublishedDataSets/" + name}.
   */
  static NodeId publishedDataSetNodeId(UShort namespaceIndex, String name) {
    return new NodeId(namespaceIndex, PREFIX + "/PublishedDataSets/" + name);
  }

  /**
   * The NodeId of a member child of {@code parent}: the parent's string identifier with {@code "/"
   * + name} appended, in the parent's namespace.
   *
   * @param parent the parent node's id; must be a fragment-minted string NodeId.
   * @param name the child's browse name, e.g. {@code "Status"} or {@code "Enable"}.
   * @return the deterministic child {@link NodeId}.
   */
  static NodeId childNodeId(NodeId parent, String name) {
    return new NodeId(parent.getNamespaceIndex(), parent.getIdentifier().toString() + "/" + name);
  }

  /**
   * Prefix-strip a fragment NodeId back to its name path: the identifier with the {@code "PubSub/"}
   * prefix removed.
   *
   * <p>The result is the joined path (component name path, published dataset path, or a member path
   * such as {@code "conn/Status/State"}); it cannot be safely split into name components because
   * names may contain {@code '/'} (see the class caveat).
   *
   * @param nodeId the id to strip.
   * @param namespaceIndex the fragment's namespace index.
   * @return the name path, or {@code null} if {@code nodeId} is not a fragment id (wrong namespace,
   *     non-String identifier, or no {@code "PubSub/"} prefix).
   */
  static @Nullable String namePathOf(NodeId nodeId, UShort namespaceIndex) {
    if (!nodeId.getNamespaceIndex().equals(namespaceIndex)) {
      return null;
    }
    if (!(nodeId.getIdentifier() instanceof String identifier)) {
      return null;
    }
    if (!identifier.startsWith(PREFIX + "/")) {
      return null;
    }
    return identifier.substring(PREFIX.length() + 1);
  }
}
