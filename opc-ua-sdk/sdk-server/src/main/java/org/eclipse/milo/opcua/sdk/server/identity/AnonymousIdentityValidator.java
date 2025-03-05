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
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.AnonymousIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

public final class AnonymousIdentityValidator extends AbstractIdentityValidator {

  /** A static instance implementing AnonymousIdentityValidator */
  public static final AnonymousIdentityValidator INSTANCE = new AnonymousIdentityValidator();

  @Override
  public Set<UserTokenType> getSupportedTokenTypes() {
    return Set.of(UserTokenType.Anonymous);
  }

  @Override
  public Identity.AnonymousIdentity validateAnonymousToken(
      Session session,
      AnonymousIdentityToken token,
      UserTokenPolicy policy,
      SignatureData signature) {

    return new DefaultAnonymousIdentity();
  }
}
