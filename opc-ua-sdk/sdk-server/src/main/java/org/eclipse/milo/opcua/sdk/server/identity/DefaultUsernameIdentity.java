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
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;

public class DefaultUsernameIdentity extends AbstractIdentity implements Identity.UsernameIdentity {

  private final String username;

  public DefaultUsernameIdentity(String username) {
    this.username = username;
  }

  @Override
  public UserTokenType getUserTokenType() {
    return UserTokenType.UserName;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean equalTo(Identity identity) {
    if (identity instanceof Identity.UsernameIdentity) {
      Identity.UsernameIdentity other = (Identity.UsernameIdentity) identity;

      return Objects.equals(getUsername(), other.getUsername());
    }

    return false;
  }
}
