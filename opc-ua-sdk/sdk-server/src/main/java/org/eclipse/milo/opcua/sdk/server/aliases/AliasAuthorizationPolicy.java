/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.aliases;

import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * SPI deciding whether a session may search or mutate an alias category over the network.
 *
 * <p>The policy is consulted only for network Method calls ({@code FindAlias}, {@code
 * FindAliasVerbose}, {@code AddAliasesToCategory}, {@code DeleteAliasesFromCategory}); the
 * manager's programmatic API is trusted application code and bypasses it. A null session means an
 * internal, trusted call.
 *
 * <p>The policy runs <em>in addition to</em> the Server's general access control (RolePermissions
 * and {@code UserExecutable} checks), which remains the first gate. Implementations that need role
 * information can use {@link Session#getRoleIds()}, which resolves through the configured {@code
 * RoleMapper}.
 *
 * <p>A denial surfaces to the Client as {@code Bad_UserAccessDenied}.
 */
public interface AliasAuthorizationPolicy {

  /**
   * The default policy: every session may search, no session may mutate.
   *
   * <p>Note that search results are <em>not</em> filtered by the Server's Browse access control:
   * unlike the Browse service, {@code FindAlias} reads the AddressSpace directly, so
   * RolePermissions and AccessRestrictions that hide Nodes from Browse do not hide the
   * corresponding aliases or targets from search. Deployments that restrict Browse visibility per
   * session should supply a policy that compensates via {@link #checkFind} and {@link
   * #includeResult}. Mutation is deny-by-default; enabling network mutation requires an explicit
   * policy grant in addition to enabling the mutation Methods.
   */
  AliasAuthorizationPolicy ALLOW_FIND_DENY_MUTATE =
      new AliasAuthorizationPolicy() {
        @Override
        public boolean checkFind(@Nullable Session session, NodeId categoryId) {
          return true;
        }

        @Override
        public boolean checkMutate(@Nullable Session session, NodeId categoryId) {
          return false;
        }
      };

  /**
   * Decide whether a session may call {@code FindAlias} or {@code FindAliasVerbose} on a category.
   *
   * <p>Consulted once per call, for the category the Method was invoked on; search authorization is
   * all-or-nothing per call. The search then recurses into every subcategory of that category
   * <em>without</em> consulting this method again, so denying find on a subcategory does not stop
   * its aliases from appearing when an ancestor is searched — use {@link #includeResult} to filter
   * individual aliases regardless of which category the search entered through.
   *
   * @param session the calling session, or null for an internal, trusted call.
   * @param categoryId the NodeId of the category the Method was invoked on.
   * @return {@code true} to allow the call, {@code false} to deny it with {@code
   *     Bad_UserAccessDenied}.
   */
  boolean checkFind(@Nullable Session session, NodeId categoryId);

  /**
   * Decide whether a session may call {@code AddAliasesToCategory} or {@code
   * DeleteAliasesFromCategory} on a category.
   *
   * @param session the calling session, or null for an internal, trusted call.
   * @param categoryId the NodeId of the category being mutated.
   * @return {@code true} to allow the call, {@code false} to deny it with {@code
   *     Bad_UserAccessDenied}.
   */
  boolean checkMutate(@Nullable Session session, NodeId categoryId);

  /**
   * Decide whether a single alias may appear in a search result for a session.
   *
   * <p>Applied per matched alias after {@link #checkFind} has allowed the call. This is the
   * <em>only</em> per-result filter: the Server's Browse access control (RolePermissions,
   * AccessRestrictions) is not applied to search results, because the engine reads the AddressSpace
   * directly rather than going through the Browse service. The default includes every result;
   * override it in deployments where sessions must not learn of Nodes they cannot Browse.
   *
   * @param session the calling session, or null for an internal, trusted call.
   * @param aliasNodeId the NodeId of the matched alias Node.
   * @return {@code true} to include the alias in the result, {@code false} to omit it.
   */
  default boolean includeResult(@Nullable Session session, NodeId aliasNodeId) {
    return true;
  }
}
