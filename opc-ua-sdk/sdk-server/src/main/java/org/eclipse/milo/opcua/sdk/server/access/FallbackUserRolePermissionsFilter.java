/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.access;

import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

/**
 * An {@link AttributeFilter} that supplies internal fallback {@code UserRolePermissions} to {@link
 * DefaultAccessController} when a node omits public {@code RolePermissions} and {@code
 * UserRolePermissions} attributes, or supplies the effective session-filtered policy when explicit
 * {@code RolePermissions} are configured.
 *
 * <p>This allows built-in restricted nodes to enforce default role policies for operations such as
 * Browse and attribute writes without publishing {@code UserRolePermissions} on nodes whose {@code
 * RolePermissions} attribute is absent (OPC 10000-3 §5.2.9, §5.2.10), and ensures {@code
 * ReadRolePermissions}, {@code WriteRolePermissions}, and {@code WriteHistorizing} checks apply
 * against the implicit {@code Anonymous} role even when no {@code RoleMapper} is configured.
 */
public interface FallbackUserRolePermissionsFilter extends AttributeFilter {

  /**
   * Resolve effective fallback {@code UserRolePermissions} for {@code session} on {@code node}, or
   * return {@code null} when no policy applies.
   *
   * @param session the requesting {@link Session}.
   * @param node the target {@link UaNode}.
   * @return effective {@code UserRolePermissions} for {@code session}, or {@code null} if none
   *     apply.
   */
  RolePermissionType @Nullable [] getFallbackUserRolePermissions(Session session, UaNode node);
}
