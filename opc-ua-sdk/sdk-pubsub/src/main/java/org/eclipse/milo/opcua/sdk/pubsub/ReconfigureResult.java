/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * The result of a {@link PubSubService#reconfigure} or {@link PubSubService#update} call, listing
 * the affected components as typed {@link Change} entries.
 *
 * <p>The change list is minimal and non-overlapping: at most one entry per component. {@link
 * Change#path()} alone is not a unique key across the list — the non-tree scopes use the bare
 * component name as their path, which shares its string space with connection names (e.g. a
 * restarted connection named {@code "X"} and a changed {@link Scope#PUBLISHED_DATA_SET} named
 * {@code "X"} are two entries with path {@code "X"}) — so consumers keying entries must combine
 * {@link Change#scope()} with the path or the explicit name components. A {@link Kind#CHANGED}
 * entry reported at an ancestor because its own configuration changed (e.g. a restarted connection)
 * subsumes its entire subtree — descendants added, removed, or changed in the same call are
 * realized by the ancestor's restart and carry no entries of their own. {@link Kind#ADDED} and
 * {@link Kind#REMOVED} entries are reported at their own path only when their ancestors are
 * otherwise unchanged; under {@link PubSubService.ReconfigureMode#STOP_AND_RESTART} they are then
 * realized by the containing connection's restart but still reported alongside its CHANGED entry. A
 * group-level CHANGED entry induced by a referenced {@link Scope#SECURITY_GROUP} definition change
 * may coexist with entries for that group's descendants.
 *
 * @param changes the typed changes applied by the reconfiguration, one entry per affected
 *     component.
 */
public record ReconfigureResult(List<Change> changes) {

  /**
   * Create a new {@link ReconfigureResult}.
   *
   * @param changes the typed changes applied by the reconfiguration.
   */
  public ReconfigureResult {
    changes = List.copyOf(changes);
  }

  /** The kind of component a {@link Change} applies to. */
  public enum Scope {
    CONNECTION,
    WRITER_GROUP,
    READER_GROUP,
    DATA_SET_WRITER,
    DATA_SET_READER,
    PUBLISHED_DATA_SET,
    STANDALONE_SUBSCRIBED_DATA_SET,
    SECURITY_GROUP
  }

  /**
   * The kind of change. For the runtime-tree scopes (connection, groups, writers/readers) {@code
   * CHANGED} means the component was restarted (replaced in place, invalidating its handles); for
   * {@link Scope#PUBLISHED_DATA_SET}, {@link Scope#STANDALONE_SUBSCRIBED_DATA_SET}, and {@link
   * Scope#SECURITY_GROUP} it means the definition changed — restarts of referencing components are
   * separate entries.
   */
  public enum Kind {
    ADDED,
    REMOVED,
    CHANGED
  }

  /**
   * One typed change applied by a reconfiguration. Name components are carried explicitly so
   * consumers never have to parse paths (names may contain '/'); they are {@code null} for the
   * scopes they do not apply to.
   *
   * @param kind the kind of change.
   * @param scope the kind of component the change applies to.
   * @param path the path of the component, e.g. {@code "conn/group/writer"}, or the bare name for
   *     the non-tree scopes.
   * @param connectionName the connection name, or {@code null} for non-tree scopes.
   * @param groupName the writer/reader group name, or {@code null} above group level and for
   *     non-tree scopes.
   * @param componentName the writer/reader name, or {@code null} above leaf level and for non-tree
   *     scopes.
   */
  public record Change(
      Kind kind,
      Scope scope,
      String path,
      @Nullable String connectionName,
      @Nullable String groupName,
      @Nullable String componentName) {}

  /**
   * Get the paths of components added by the reconfiguration, in change order.
   *
   * @return the paths of all {@link Kind#ADDED} entries.
   */
  public List<String> addedPaths() {
    return pathsOf(Kind.ADDED, true);
  }

  /**
   * Get the paths of components removed by the reconfiguration, in change order; their handles are
   * invalidated.
   *
   * @return the paths of all {@link Kind#REMOVED} entries.
   */
  public List<String> removedPaths() {
    return pathsOf(Kind.REMOVED, true);
  }

  /**
   * Get the paths of runtime components restarted by the reconfiguration, in change order: the
   * {@link Kind#CHANGED} entries of the runtime-tree scopes. CHANGED entries of {@link
   * Scope#PUBLISHED_DATA_SET}, {@link Scope#STANDALONE_SUBSCRIBED_DATA_SET}, and {@link
   * Scope#SECURITY_GROUP} are excluded — they restart nothing themselves; the restarts they induce
   * on referencing components are separate entries. Restarted components keep their diagnostic
   * counters and sequence numbers (see {@link PubSubService#reconfigure}); their handles are
   * invalidated.
   *
   * @return the paths of the tree-scope {@link Kind#CHANGED} entries.
   */
  public List<String> restartedPaths() {
    return pathsOf(Kind.CHANGED, false);
  }

  private List<String> pathsOf(Kind kind, boolean includeNonTreeScopes) {
    var paths = new ArrayList<String>();
    for (Change change : changes) {
      if (change.kind() == kind && (includeNonTreeScopes || isTreeScope(change.scope()))) {
        paths.add(change.path());
      }
    }
    return List.copyOf(paths);
  }

  private static boolean isTreeScope(Scope scope) {
    return switch (scope) {
      case CONNECTION, WRITER_GROUP, READER_GROUP, DATA_SET_WRITER, DATA_SET_READER -> true;
      case PUBLISHED_DATA_SET, STANDALONE_SUBSCRIBED_DATA_SET, SECURITY_GROUP -> false;
    };
  }
}
