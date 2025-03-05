/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.identity;

import java.util.Set;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

public interface IdentityValidator {

  /**
   * Validate a {@link UserIdentityToken} and return an {@link Identity} that represents the user.
   *
   * @param session the {@link Session} making the request.
   * @param token the {@link UserIdentityToken}.
   * @param policy the {@link UserTokenPolicy} specified by the policyId in {@code token}.
   * @param signature the {@link SignatureData} from the {@link ActivateSessionRequest}.
   * @return an {@link Identity} that represents the authenticated user.
   * @throws UaException if the token is invalid, rejected, or user access is otherwise denied.
   */
  Identity validateIdentityToken(
      Session session, UserIdentityToken token, UserTokenPolicy policy, SignatureData signature)
      throws UaException;

  /**
   * Return the {@link Set} of {@link UserTokenType}s supported by this {@link IdentityValidator}.
   *
   * @return the {@link Set} of {@link UserTokenType}s supported by this {@link IdentityValidator}.
   */
  Set<UserTokenType> getSupportedTokenTypes();
}
