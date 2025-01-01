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

import java.util.concurrent.atomic.AtomicReference;
import org.jspecify.annotations.Nullable;

/** A base class for {@link Identity} implementations that manages the user data. */
public abstract class AbstractIdentity implements Identity {

  protected final AtomicReference<Object> userDataRef = new AtomicReference<>();

  @Override
  public void setUserData(Object userData) {
    userDataRef.set(userData);
  }

  @Override
  public @Nullable Object getUserData() {
    return userDataRef.get();
  }
}
