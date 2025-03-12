/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record XmlElement(@Nullable String fragment) {

  public static XmlElement NULL_VALUE = XmlElement.of(null);

  public @Nullable String getFragment() {
    return fragment;
  }

  public String getFragmentOrEmpty() {
    return fragment != null ? fragment : "";
  }

  public static XmlElement of(@Nullable String fragment) {
    return new XmlElement(fragment);
  }

  public boolean isNull() {
    return fragment == null;
  }

  public boolean isNotNull() {
    return !isNull();
  }
}
