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

import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;

public class DefaultAnonymousIdentity extends AbstractIdentity
    implements Identity.AnonymousIdentity {

  @Override
  public UserTokenType getUserTokenType() {
    return UserTokenType.Anonymous;
  }
}
