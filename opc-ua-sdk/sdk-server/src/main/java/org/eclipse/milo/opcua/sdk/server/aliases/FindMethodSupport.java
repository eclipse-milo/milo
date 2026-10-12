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
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * The shared entry checks of the {@code FindAlias} and {@code FindAliasVerbose} Method
 * implementations, kept in one place so the two implementations cannot drift.
 */
final class FindMethodSupport {

  private FindMethodSupport() {}

  /**
   * Authorize a Find-family call and validate its search pattern.
   *
   * @param policy the policy to authorize against.
   * @param session the calling session, or null for an internal call.
   * @param categoryId the NodeId of the category the Method was called on.
   * @param pattern the wire-decoded search pattern.
   * @return the search pattern, now known non-null.
   * @throws UaException with {@code Bad_UserAccessDenied} if the policy denies the call; {@code
   *     Bad_InvalidArgument} if the pattern is null.
   */
  static String checkFindCall(
      AliasAuthorizationPolicy policy,
      @Nullable Session session,
      NodeId categoryId,
      @Nullable String pattern)
      throws UaException {

    if (!policy.checkFind(session, categoryId)) {
      throw new UaException(StatusCodes.Bad_UserAccessDenied);
    }

    // The generated bridge passes wire-decoded input values through unchecked, so a null search
    // pattern must be rejected here before it enters non-nullable signatures downstream; §6.3.2
    // maps invalid search strings to Bad_InvalidArgument.
    if (pattern == null) {
      throw new UaException(StatusCodes.Bad_InvalidArgument, "AliasNameSearchPattern is null");
    }

    return pattern;
  }
}
