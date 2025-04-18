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

import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;

public class DefaultIssuedIdentity extends AbstractIdentity implements Identity.IssuedIdentity {

  private final ByteString tokenData;

  public DefaultIssuedIdentity(ByteString tokenData) {
    this.tokenData = tokenData;
  }

  @Override
  public UserTokenType getUserTokenType() {
    return UserTokenType.IssuedToken;
  }

  @Override
  public ByteString getTokenData() {
    return tokenData;
  }

  @Override
  public boolean equalTo(Identity identity) {
    if (identity instanceof IssuedIdentity other) {
      return Objects.equals(getTokenData(), other.getTokenData());
    }

    return false;
  }
}
