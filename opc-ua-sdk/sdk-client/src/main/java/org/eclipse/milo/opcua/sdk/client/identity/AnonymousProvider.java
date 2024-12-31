/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.identity;

import static java.util.Objects.requireNonNullElse;

import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.AnonymousIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

/**
 * An {@link IdentityProvider} that will choose the first available anonymous {@link
 * UserTokenPolicy}.
 */
public class AnonymousProvider implements IdentityProvider {

  public static final IdentityProvider INSTANCE = new AnonymousProvider();

  @Override
  public SignedIdentityToken getIdentityToken(EndpointDescription endpoint, ByteString serverNonce)
      throws Exception {

    UserTokenPolicy[] userIdentityTokens =
        requireNonNullElse(endpoint.getUserIdentityTokens(), new UserTokenPolicy[0]);

    return Stream.of(userIdentityTokens)
        .filter(t -> t.getTokenType() == UserTokenType.Anonymous)
        .findFirst()
        .map(
            policy -> {
              UserIdentityToken token = new AnonymousIdentityToken(policy.getPolicyId());

              return new SignedIdentityToken(token, new SignatureData(null, null));
            })
        .orElseThrow(() -> new Exception("no anonymous token policy found"));
  }

  @Override
  public String toString() {
    return "AnonymousProvider{}";
  }
}
