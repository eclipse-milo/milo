/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import com.google.common.base.MoreObjects;
import java.util.Objects;

public final class XmlElement {

  private final String fragment;

  public XmlElement(String fragment) {
    this.fragment = fragment;
  }

  public String getFragment() {
    return fragment;
  }

  public String getFragmentOrEmpty() {
    return fragment != null ? fragment : "";
  }

  public static XmlElement of(String fragment) {
    return new XmlElement(fragment);
  }

  public boolean isNull() {
    return fragment == null;
  }

  public boolean isNotNull() {
    return !isNull();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    XmlElement that = (XmlElement) o;

    return Objects.equals(fragment, that.fragment);
  }

  @Override
  public int hashCode() {
    return fragment != null ? fragment.hashCode() : 0;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("fragment", fragment).toString();
  }
}
