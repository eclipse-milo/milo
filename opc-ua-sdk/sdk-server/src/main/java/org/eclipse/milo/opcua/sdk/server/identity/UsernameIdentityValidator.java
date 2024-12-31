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

import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.jspecify.annotations.Nullable;

public class UsernameIdentityValidator extends AbstractUsernameIdentityValidator {

  private final boolean anonymousAccessAllowed;
  private final Predicate<AuthenticationChallenge> predicate;

  public UsernameIdentityValidator(
      boolean anonymousAccessAllowed, Predicate<AuthenticationChallenge> predicate) {

    this.anonymousAccessAllowed = anonymousAccessAllowed;
    this.predicate = predicate;
  }

  @Override
  protected Identity.@Nullable AnonymousIdentity authenticateAnonymous(Session session) {
    if (anonymousAccessAllowed) {
      return new DefaultAnonymousIdentity();
    } else {
      return null;
    }
  }

  @Override
  protected Identity.@Nullable UsernameIdentity authenticateUsernamePassword(
      Session session, String username, String password) {

    var challenge = new AuthenticationChallenge(username, password);

    return predicate.test(challenge) ? new DefaultUsernameIdentity(username) : null;
  }

  public static final class AuthenticationChallenge {

    private final String username;
    private final String password;

    AuthenticationChallenge(String username, String password) {
      this.username = username;
      this.password = password;
    }

    public String getUsername() {
      return username;
    }

    public String getPassword() {
      return password;
    }
  }
}
